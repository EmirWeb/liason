package mobi.liason.annotation.creators;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.squareup.javawriter.JavaWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import mobi.liason.annotation.elements.PathActionElement;
import mobi.liason.annotation.elements.PathElement;
import mobi.liason.annotation.elements.ViewModelElement;
import mobi.liason.annotation.annotations.PathAction;
import mobi.liason.annotation.annotations.mvvm.ViewModel;
import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.helpers.VariableNameHelper;
import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class ViewModelCreator {

    private static final String NAME = "NAME";

    private static final String CONTEXT_CLASS_NAME_STRING = Context.class.getSimpleName();
    private static final String CONTEXT_DECLERATION = Modifier.FINAL.toString() + " " + CONTEXT_CLASS_NAME_STRING;
    private static final String CONTEXT_VARIABLE_NAME = VariableNameHelper.getVariableNameFromClassName(CONTEXT_CLASS_NAME_STRING);

    private static final String SQL_DATABASE_CLASS_NAME_STRING = SQLiteDatabase.class.getSimpleName();
    private static final String SQL_DATABASE_DECLERATION = Modifier.FINAL.toString() + " " + SQL_DATABASE_CLASS_NAME_STRING;
    private static final String SQL_DATABASE_VARIABLE_NAME = VariableNameHelper.getVariableNameFromClassName(SQL_DATABASE_CLASS_NAME_STRING);

    private static final String PATH_CLASS_NAME_STRING = Path.class.getSimpleName();
    private static final String PATH_DECLERATION = Modifier.FINAL.toString() + " " + PATH_CLASS_NAME_STRING;
    private static final String PATH_VARIABLE_NAME = VariableNameHelper.getVariableNameFromClassName(PATH_CLASS_NAME_STRING);

    private static final String URI_CLASS_NAME_STRING = Uri.class.getSimpleName();
    private static final String URI_DECLERATION = Modifier.FINAL.toString() + " " + URI_CLASS_NAME_STRING;
    private static final String URI_VARIABLE_NAME = VariableNameHelper.getVariableNameFromClassName(URI_CLASS_NAME_STRING);

    private static final String PROJECTION_CLASS_NAME_STRING = String[].class.getSimpleName();
    private static final String PROJECTION_DECLERATION = Modifier.FINAL.toString() + " " + PROJECTION_CLASS_NAME_STRING;
    private static final String PROJECTION_VARIABLE_NAME = "projection";

    private static final String SELECTION_CLASS_NAME_STRING = String.class.getSimpleName();
    private static final String SELECTION_DECLERATION = Modifier.FINAL.toString() + " " + SELECTION_CLASS_NAME_STRING;
    private static final String SELECTION_VARIABLE_NAME = "selection";

    private static final String SELECTION_ARGUMENTS_CLASS_NAME_STRING = String[].class.getSimpleName();
    private static final String SELECTION_ARGUMENTS_DECLERATION = Modifier.FINAL.toString() + " " + SELECTION_ARGUMENTS_CLASS_NAME_STRING;
    private static final String SELECTION_ARGUMENTS_VARIABLE_NAME = "selectionArguments";

    private static final String SORT_ORDER_CLASS_NAME_STRING = String.class.getSimpleName();
    private static final String SORT_ORDER_DECLERATION = Modifier.FINAL.toString() + " " + SORT_ORDER_CLASS_NAME_STRING;
    private static final String SORT_ORDER_VARIABLE_NAME = "sortOrder";

    private static final String CLASS = "class";

    public static void processModels(final ProcessingEnvironment processingEnv, final Map<Element, ViewModelElement> elementMappings) {
        for (final Element element : elementMappings.keySet()) {
            final ViewModelElement viewModelElement = elementMappings.get(element);

            processModel(processingEnv, viewModelElement);
        }
    }

    private static void processModel(final ProcessingEnvironment processingEnv, final ViewModelElement viewModelElement) {

        final String metaClassName = viewModelElement.getSimpleName();
        final String className = viewModelElement.getClassName();

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(className);
            final Writer writer = javaFileObject.openWriter();
            final String pacakageName = viewModelElement.getPackageName();

            final Writer stringWriter = new StringWriter();
            final JavaWriter javaWriter = new JavaWriter(stringWriter);
            javaWriter.emitPackage(pacakageName);

            final List<String> imports = getImports(viewModelElement);
            javaWriter.emitImports(imports);

            javaWriter.beginType(className, CLASS, EnumSet.of(Modifier.PUBLIC), ViewModel.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), NAME, EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), String.format("%s.class.getSimpleName()", className));
                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), CONTEXT_DECLERATION, CONTEXT_VARIABLE_NAME);
                javaWriter.emitStatement("return " + NAME);
                javaWriter.endMethod();
            }

            // Selection
            {
                final String selectionMethodName = viewModelElement.getSelectionMethodName();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getSelection", EnumSet.of(Modifier.PROTECTED), CONTEXT_DECLERATION, CONTEXT_VARIABLE_NAME);
                javaWriter.emitStatement("return %s.%s", metaClassName, selectionMethodName);
                javaWriter.endMethod();
            }

            // Columns
            {
                if (viewModelElement.hasProjectionFieldElements()) {
                    final List<FieldElement> projectionFieldElements = viewModelElement.getProjectionFieldElements();
                    javaWriter.emitAnnotation(ColumnDefinitions.class);
                    javaWriter.beginType("Columns", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                    for (final FieldElement fieldElement : projectionFieldElements) {
                        final String simpleName = fieldElement.getSimpleName();
                        javaWriter.emitAnnotation(ColumnDefinition.class);

                        final String declaration = String.format("%s.%s", metaClassName, fieldElement.getSimpleName());
                        javaWriter.emitField(ViewModelColumn.class.getSimpleName(), simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                    }
                    javaWriter.endType();
                }
            }

            // Paths
            {
                javaWriter.emitAnnotation(PathDefinitions.class);
                javaWriter.beginType("Paths", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                javaWriter.emitAnnotation(PathDefinition.class);

                // Default path
                {
                    final String declaration = String.format("new Path(%s.NAME)", className);
                    final String publicStaticNameFromClassName = VariableNameHelper.getPublicStaticNameFromClassName(metaClassName);
                    javaWriter.emitField(Path.class.getSimpleName(), publicStaticNameFromClassName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }


                if (viewModelElement.hasPathElements()) {
                    final List<PathElement> pathElements = viewModelElement.getPathElements();
                    for (final PathElement pathElement : pathElements) {

                        final String simpleName = pathElement.getSimpleName();
                        javaWriter.emitAnnotation(PathDefinition.class);

                        final String declaration = String.format("new Path(%s.%s)", metaClassName, simpleName);
                        javaWriter.emitField(Path.class.getSimpleName(), simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                    }
                }

                javaWriter.endType();
            }

            // PathActions
            {
                if (viewModelElement.hasPathActionElements()){
                    final List<PathActionElement> queryPathActionElements = viewModelElement.getPathActionElements(PathAction.PathType.query);
                    if (!queryPathActionElements.isEmpty()){

                        javaWriter.emitAnnotation(Override.class);
                        javaWriter.beginMethod(Cursor.class.getSimpleName(), "query", EnumSet.of(Modifier.PUBLIC),
                                CONTEXT_DECLERATION, CONTEXT_VARIABLE_NAME,
                                SQL_DATABASE_DECLERATION, SQL_DATABASE_VARIABLE_NAME,
                                PATH_DECLERATION, PATH_VARIABLE_NAME,
                                URI_DECLERATION, URI_VARIABLE_NAME,
                                PROJECTION_DECLERATION, PROJECTION_VARIABLE_NAME,
                                SELECTION_DECLERATION, SELECTION_VARIABLE_NAME,
                                SELECTION_ARGUMENTS_DECLERATION, SELECTION_ARGUMENTS_VARIABLE_NAME,
                                SORT_ORDER_DECLERATION, SORT_ORDER_VARIABLE_NAME
                        );

                        for (final PathActionElement pathActionElement : queryPathActionElements){
                            final String simpleName = pathActionElement.getSimpleName();
                            final String path = pathActionElement.getPathActionValue();
                            javaWriter.beginControlFlow("if (%s != null && %s.equals(new Path(\"%s\")))", PATH_VARIABLE_NAME, PATH_VARIABLE_NAME, path);
                            javaWriter.emitStatement("return %s.%s(this, %s, %s, %s, %s, %s, %s, %s)",
                                    metaClassName, simpleName,
                                    CONTEXT_VARIABLE_NAME, SQL_DATABASE_VARIABLE_NAME,
                                    URI_VARIABLE_NAME, PROJECTION_VARIABLE_NAME,
                                    SELECTION_VARIABLE_NAME, SELECTION_ARGUMENTS_VARIABLE_NAME,
                                    SORT_ORDER_VARIABLE_NAME
                                    );
                            javaWriter.endControlFlow();
                        }

                        javaWriter.emitStatement("return super.query(%s, %s, %s, %s, %s, %s, %s, %s)",
                                CONTEXT_VARIABLE_NAME, SQL_DATABASE_VARIABLE_NAME,
                                PATH_VARIABLE_NAME, URI_VARIABLE_NAME, PROJECTION_VARIABLE_NAME,
                                SELECTION_VARIABLE_NAME, SELECTION_ARGUMENTS_VARIABLE_NAME,
                                SORT_ORDER_VARIABLE_NAME
                        );

                        javaWriter.endMethod();
                    }
                }
            }

            javaWriter.endType();
            javaWriter.close();

            final String code = stringWriter.toString();
            System.out.println(className);
            System.out.println(code);
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<String> getImports(final ViewModelElement viewModelElement) {
        final List<String> imports = new ArrayList<String>();
        imports.add(Context.class.getCanonicalName());
        imports.add(mobi.liason.mvvm.database.ViewModel.class.getCanonicalName());
        imports.add(PathDefinitions.class.getCanonicalName());
        imports.add(PathDefinition.class.getCanonicalName());
        imports.add(Path.class.getCanonicalName());

        if (viewModelElement.hasProjectionFieldElements()) {
            imports.add(ColumnDefinition.class.getCanonicalName());
            imports.add(ColumnDefinitions.class.getCanonicalName());
            imports.add(ViewModelColumn.class.getCanonicalName());
        }

        if (viewModelElement.hasPathActionElements()) {
            imports.add(SQLiteDatabase.class.getCanonicalName());
            imports.add(Uri.class.getCanonicalName());
            imports.add(Cursor.class.getCanonicalName());
        }
        return imports;
    }

}


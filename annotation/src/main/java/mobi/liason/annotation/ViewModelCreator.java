package mobi.liason.annotation;

import android.content.ContentValues;
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
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Column.Type;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.PrimaryKey;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class ViewModelCreator {

    private static final String VIEW_MODEL = "ViewModel";
    private static final String JSON = "Json";
    private static final String CLASS = "class";

    public static void processModels(final ProcessingEnvironment processingEnv, final Map<Element, ViewModelStructure> elementMappings) {
        for (final Element modelElement : elementMappings.keySet()) {
            final ViewModelStructure viewModelStructure = elementMappings.get(modelElement);
            processModel(processingEnv, modelElement, viewModelStructure);
        }
    }

    private static void processModel(final ProcessingEnvironment processingEnv, final Element modelElement, final ViewModelStructure viewModelStructure) {
        final TypeElement typeElement = (TypeElement) modelElement;
        final PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        final Name typeElementQualifiedName = typeElement.getQualifiedName();
        final String typeElementQualifiedNameString = typeElementQualifiedName.toString();
        final Name typeElementSimpleName = typeElement.getSimpleName();
        final String typeElementSimpleNameString = typeElementSimpleName.toString();

        final String modelClassName = typeElementSimpleNameString + VIEW_MODEL;
        final String jsonClassNameAndPackage = typeElementQualifiedNameString + JSON;

        final String contextClassNameString = Context.class.getSimpleName();
        final String contextDecleration = Modifier.FINAL.toString() + " " + contextClassNameString;
        final String contextVariableName = contextClassNameString.toLowerCase();

        final String sqlDatabaseClassNameString = SQLiteDatabase.class.getSimpleName();
        final String sqlDatabaseDecleration = Modifier.FINAL.toString() + " " + sqlDatabaseClassNameString;
        final String sqlDatabaseVariableName = VariableNameHelper.getVariableNameFromClassName(sqlDatabaseClassNameString);

        final String pathClassNameString = Path.class.getSimpleName();
        final String pathDecleration = Modifier.FINAL.toString() + " " + pathClassNameString;
        final String pathVariableName = pathClassNameString.toLowerCase();

        final String uriClassNameString = Uri.class.getSimpleName();
        final String uriDecleration = Modifier.FINAL.toString() + " " + uriClassNameString;
        final String uriVariableName = uriClassNameString.toLowerCase();

        final String projectionClassNameString = String[].class.getSimpleName();
        final String projectionDecleration = Modifier.FINAL.toString() + " " + projectionClassNameString;
        final String projectionVariableName = "projection";

        final String selectionClassNameString = String.class.getSimpleName();
        final String selectionDecleration = Modifier.FINAL.toString() + " " + selectionClassNameString;
        final String selectionVariableName = "selection";

        final String selectionArgumentsClassNameString = String[].class.getSimpleName();
        final String selectionArgumentsDecleration = Modifier.FINAL.toString() + " " + selectionArgumentsClassNameString;
        final String selectionArgumentsVariableName = "selectionArguments";

        final String sortOrderClassNameString = String.class.getSimpleName();
        final String sortOrderDecleration = Modifier.FINAL.toString() + " " + sortOrderClassNameString;
        final String sortOrderVariableName = "sortOrder";

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(modelClassName);
            final Writer writer = javaFileObject.openWriter();
            final String packageElementQualifiedName = packageElement.getQualifiedName().toString();

            final Writer stringWriter = new StringWriter();
            final JavaWriter javaWriter = new JavaWriter(stringWriter);
            javaWriter.emitPackage(packageElementQualifiedName);

            final List<String> types = new ArrayList<String>();
            types.add(Context.class.getCanonicalName());
            types.add(mobi.liason.mvvm.database.ViewModel.class.getCanonicalName());
            types.add(PathDefinitions.class.getCanonicalName());
            types.add(PathDefinition.class.getCanonicalName());
            types.add(Path.class.getCanonicalName());

            if (!viewModelStructure.mProjectionElements.isEmpty()) {
                types.add(ColumnDefinition.class.getCanonicalName());
                types.add(ColumnDefinitions.class.getCanonicalName());
                types.add(ViewModelColumn.class.getCanonicalName());
            }

            if (!viewModelStructure.mPathActionElements.isEmpty()) {
                types.add(SQLiteDatabase.class.getCanonicalName());
                types.add(Uri.class.getCanonicalName());
                types.add(Cursor.class.getCanonicalName());
            }

            javaWriter.emitImports(types);

            javaWriter.beginType(modelClassName, CLASS, EnumSet.of(Modifier.PUBLIC), ViewModel.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), "NAME", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), modelClassName+".class.getSimpleName()");
                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), contextDecleration, contextVariableName);
                javaWriter.emitStatement("return " + "NAME");
                javaWriter.endMethod();
            }

            // Selection
            {
                final Name simpleName = viewModelStructure.mSelectionElement.getSimpleName();
                final String simpleNameString = simpleName.toString();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getSelection", EnumSet.of(Modifier.PROTECTED), contextDecleration, contextVariableName);
                javaWriter.emitStatement("return %s.%s", typeElementSimpleNameString, simpleNameString);
                javaWriter.endMethod();
            }

            // Columns
            {
                if (!viewModelStructure.mProjectionElements.isEmpty()) {
                    javaWriter.emitAnnotation(ColumnDefinitions.class);
                    javaWriter.beginType("Columns", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                    for (final Element fieldElement : viewModelStructure.mProjectionElements) {
                        final Name simpleName = fieldElement.getSimpleName();
                        final String simpleNameString = simpleName.toString();
                        javaWriter.emitAnnotation(ColumnDefinition.class);

                        final String declaration = String.format("%s.%s", typeElementSimpleNameString, fieldElement.getSimpleName());
                        javaWriter.emitField(ViewModelColumn.class.getSimpleName(), simpleNameString, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);

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
                    final String declaration = String.format("new Path(%s.NAME)", modelClassName);
                    final String publicStaticNameFromClassName = VariableNameHelper.getPublicStaticNameFromClassName(typeElementSimpleNameString);
                    javaWriter.emitField(Path.class.getSimpleName(), publicStaticNameFromClassName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }

                for (final Element fieldElement : viewModelStructure.mPathElements) {
                    final Name simpleName = fieldElement.getSimpleName();
                    final String simpleNameString = simpleName.toString();
                    javaWriter.emitAnnotation(PathDefinition.class);

                    final String declaration = String.format("new Path(%s.%s)", typeElementSimpleNameString, fieldElement.getSimpleName());
                    javaWriter.emitField(Path.class.getSimpleName(), simpleNameString, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }

                javaWriter.endType();
            }

            // PathsActions
            {
                if (!viewModelStructure.mPathActionElements.isEmpty()){
                    if (viewModelStructure.mPathActionElements.containsKey(PathAction.PathType.query)){
                        final List<Element> pathActionElements = viewModelStructure.mPathActionElements.get(PathAction.PathType.query);
                        javaWriter.emitAnnotation(Override.class);
                        javaWriter.beginMethod(Cursor.class.getSimpleName(), "query", EnumSet.of(Modifier.PUBLIC),
                                contextDecleration, contextVariableName,
                                sqlDatabaseDecleration, sqlDatabaseVariableName,
                                pathDecleration, pathVariableName,
                                uriDecleration, uriVariableName,
                                projectionDecleration, projectionVariableName,
                                selectionDecleration, selectionVariableName,
                                selectionArgumentsDecleration, selectionArgumentsVariableName,
                                sortOrderDecleration, sortOrderVariableName
                        );

                        for (final Element pathActionElement : pathActionElements){
                            final Name simpleName = pathActionElement.getSimpleName();
                            final String simpleNameString = simpleName.toString();
                            final PathAction annotation = pathActionElement.getAnnotation(PathAction.class);
                            final String path = annotation.value();
                            javaWriter.beginControlFlow("if (%s.equals(new Path(\"%s\")))", pathVariableName, path);
                            javaWriter.emitStatement("return %s.%s(%s, %s, %s, %s, %s, %s, %s)",
                                    typeElementSimpleNameString, simpleNameString,
                                    contextVariableName, sqlDatabaseVariableName,
                                    uriVariableName, projectionVariableName,
                                    selectionVariableName, selectionArgumentsVariableName,
                                    sortOrderVariableName
                                    );
                            javaWriter.endControlFlow();
                        }

                        javaWriter.emitStatement("return super.query(%s, %s, %s, %s, %s, %s, %s, %s)",
                                contextVariableName, sqlDatabaseVariableName,
                                pathVariableName, uriVariableName, projectionVariableName,
                                selectionVariableName, selectionArgumentsVariableName,
                                sortOrderVariableName
                        );

                        javaWriter.endMethod();
                    }
                }
            }

            javaWriter.endType();
            javaWriter.close();

            final String code = stringWriter.toString();
            System.out.println(code);
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

}


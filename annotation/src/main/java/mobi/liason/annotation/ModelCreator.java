package mobi.liason.annotation;

import android.content.ContentValues;
import android.content.Context;

import com.squareup.javawriter.JavaWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
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
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class ModelCreator {

    private static final String MODEL = "Model";
    private static final String JSON = "Json";
    private static final String CLASS = "class";

    public static void processModels(final ProcessingEnvironment processingEnv, final Map<Element, List<Element>> elementMappings) {
        for (final Element modelElement : elementMappings.keySet()) {
            final List<Element> fieldElements = elementMappings.get(modelElement);
            processModel(processingEnv, modelElement, fieldElements);
        }
    }

    private static void processModel(final ProcessingEnvironment processingEnv, final Element modelElement, final List<Element> fieldElements) {
        final TypeElement typeElement = (TypeElement) modelElement;
        final PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        final Name typeElementSimpleName = typeElement.getSimpleName();
        final String typeElementSimpleNameString = typeElementSimpleName.toString();

        final String modelClassName = typeElementSimpleNameString + MODEL;
        final String jsonClassName = typeElementSimpleNameString + JSON;

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(modelClassName);
            final Writer writer = javaFileObject.openWriter();
            final Writer stringWriter = new StringWriter();
            final String packageElementQualifiedName = packageElement.getQualifiedName().toString();

            final JavaWriter javaWriter = new JavaWriter(stringWriter);
            javaWriter.emitPackage(packageElementQualifiedName);

            javaWriter.emitImports(Context.class, Model.class, PathDefinitions.class, PathDefinition.class, Path.class);

            if (!fieldElements.isEmpty()) {
                javaWriter.emitImports(ContentValues.class, ColumnDefinitions.class , ColumnDefinition.class, ModelColumn.class, Column.class);
            }

            javaWriter.beginType(modelClassName, CLASS, EnumSet.of(Modifier.PUBLIC), Model.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), "NAME", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), modelClassName+".class.getSimpleName()");

                final String contextString = Context.class.getSimpleName();
                final String declaration = Modifier.FINAL.toString() + " " + contextString;
                final String variableName = contextString.toLowerCase();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), declaration, variableName);
                javaWriter.emitStatement("return " + "NAME");
                javaWriter.endMethod();
            }

            // ContentValues
            {
                if (!fieldElements.isEmpty()) {

                    final String decleration = Modifier.FINAL.toString() + " " + jsonClassName;
                    final String jsonVariableName = VariableNameHelper.getVariableNameFromClassName(jsonClassName);
                    final String contentValuesClassName = ContentValues.class.getSimpleName();
                    final String contentValuesVariableName = VariableNameHelper.getVariableNameFromClassName(contentValuesClassName);

                    javaWriter.beginMethod(contentValuesClassName, "getContentValues", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC),decleration, jsonVariableName);

                    javaWriter.emitStatement("%s %s %s = new %s()", Modifier.FINAL.toString(), contentValuesClassName, contentValuesVariableName , contentValuesClassName );

                    for (final Element fieldElement : fieldElements) {
                        final String fieldType = CreatorHelper.getFieldType(fieldElement);
                        if (fieldType != null) {
                            final AnnotationMirror annotationMirror = CreatorHelper.getAnnotationMirror(fieldElement);
                            if (!CreatorHelper.isArray(annotationMirror, fieldElement)) {
                                final Name simpleName = fieldElement.getSimpleName();
                                final String simpleNameString = simpleName.toString();
                                javaWriter.emitStatement(contentValuesVariableName + ".put(Columns." + simpleNameString + ".getName(), " + jsonVariableName + "." + VariableNameHelper.getGetMethodName(simpleNameString) + "())");
                            }
                        }
                    }

                    javaWriter.emitStatement("return " + contentValuesVariableName);
                    javaWriter.endMethod();
                }
            }

            // Columns
            {
                if (!fieldElements.isEmpty()) {
                    javaWriter.emitAnnotation(ColumnDefinitions.class);
                    javaWriter.beginType("Columns", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                    for (final Element fieldElement : fieldElements) {
                        final String fieldType = CreatorHelper.getFieldType(fieldElement);
                        if (fieldType != null) {
                            final Name simpleName = fieldElement.getSimpleName();
                            final String simpleNameString = simpleName.toString();
                            javaWriter.emitAnnotation(ColumnDefinition.class);

                            final Type type = CreatorHelper.getTypeString(fieldElement);
                            final String declaration = String.format("new ModelColumn(%s.NAME, %s.%s, %s.%s.%s)",
                                modelClassName, typeElementSimpleNameString, simpleNameString, Column.class.getSimpleName(), Type.class.getSimpleName(), type.toString());
                            javaWriter.emitField(ModelColumn.class.getSimpleName(), simpleNameString, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                        }
                    }
                    javaWriter.endType();
                }
            }

            // Paths
            {
                javaWriter.emitAnnotation(PathDefinitions.class);
                javaWriter.beginType("Paths", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                javaWriter.emitAnnotation(PathDefinition.class);

                final String declaration = String.format("new Path(%s.NAME)", modelClassName);
                final String publicStaticNameFromClassName = VariableNameHelper.getPublicStaticNameFromClassName(typeElementSimpleNameString);
                javaWriter.emitField(Path.class.getSimpleName(), publicStaticNameFromClassName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);

                javaWriter.endType();
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

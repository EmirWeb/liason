package mobi.liason.annotation;

import android.content.ContentValues;
import android.content.Context;
import com.squareup.javawriter.JavaWriter;
import com.sun.javafx.binding.StringFormatter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
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
import javax.lang.model.type.DeclaredType;
import javax.tools.JavaFileObject;

import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Column.Type;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;

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

            javaWriter.emitImports(Context.class, Model.class);

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
                        final String fieldType = getFieldType(fieldElement);
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

                    final String decleration = Modifier.FINAL.toString() + " " + jsonClassName;
                    final String variable = VariableNameHelper.getVariableNameFromClassName(jsonClassName);
                    final String contentValuesClassName = ContentValues.class.getSimpleName();
                    final String contentValuesVariableName = VariableNameHelper.getVariableNameFromClassName(contentValuesClassName);

                    for (final Element fieldElement : fieldElements) {
                        final String fieldType = getFieldType(fieldElement);
                        if (fieldType != null) {
                            final Name simpleName = fieldElement.getSimpleName();
                            final String simpleNameString = simpleName.toString();
                            javaWriter.emitAnnotation(ColumnDefinition.class);

                            final Type type = getTypeString(fieldElement);
                            final String declaration = String.format("new ModelColumn(%s.NAME, %s.%s, %s.%s.%s)",
                                modelClassName, typeElementSimpleNameString, simpleNameString, Column.class.getSimpleName(), Type.class.getSimpleName(), type.toString());
                            System.out.println(declaration);
                            javaWriter.emitField(ModelColumn.class.getSimpleName(), simpleNameString, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                        }
                    }



                    javaWriter.endType();
                }
            }
//
//            // Constructor
//            {
//                if (!fieldElements.isEmpty()) {
//                    final List<String> constructorParameters = getConstructorParameters(fieldElements);
//                    javaWriter.beginConstructor(EnumSet.of(Modifier.PUBLIC), constructorParameters, null);
//
//                    for (final Element fieldElement : fieldElements) {
//                        final Name simpleName = fieldElement.getSimpleName();
//                        final String simpleNameString = simpleName.toString();
//                        final String memberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
//                        final String fieldVariableName = VariableNameHelper.getConstructorParameterVariableName(simpleNameString);
//                        javaWriter.emitStatement(memberVariableName + " = " + fieldVariableName);
//                    }
//
//                    javaWriter.endConstructor();
//                }
//            }
//
//            // Get methods
//            {
//
//                for (final Element fieldElement : fieldElements) {
//
//                    final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
//                    final String fieldType = getFieldType(annotationMirrors);
//                    if (fieldType != null) {
//
//                        final Name simpleName = fieldElement.getSimpleName();
//                        final String simpleNameString = simpleName.toString();
//                        final String getMethodName = VariableNameHelper.getGetMethodName(simpleNameString);
//                        final String classMemberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
//
//                        javaWriter.beginMethod(fieldType, getMethodName, EnumSet.of(Modifier.PUBLIC));
//                        javaWriter.emitStatement("return " + classMemberVariableName);
//                        javaWriter.endMethod();
//                    }
//                }
//
//
//            }

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

    private static Type getTypeString(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                return Type.integer;
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                return Type.text;
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                return Type.real;
            }
        }
        return Type.text;
    }

    private static List<String> getConstructorParameters(final List<Element> fieldElements) {
        final List<String> constructorParameters = new ArrayList<String>(fieldElements.size() * 3);
        for (final Element fieldElement : fieldElements){
            final String fieldType = getFieldType(fieldElement);
            final Name name = fieldElement.getSimpleName();
            final String nameString = name.toString();
            final String fieldVariableName = VariableNameHelper.getConstructorParameterVariableName(nameString);

            if (fieldType != null) {
                constructorParameters.add(Modifier.FINAL.toString() + " " + fieldType);
                constructorParameters.add(fieldVariableName);
            }
        }
        return constructorParameters;
    }

    private static String getFieldType(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                return Long.class.getSimpleName();
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                return String.class.getSimpleName();
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                return Double.class.getSimpleName();
            }

        }
        return null;
    }

    private static boolean isAnnotationOfType(final Class<?> klass, final AnnotationMirror annotationMirror) {
        final DeclaredType annotationType = annotationMirror.getAnnotationType();
        final String annotationTypeString = annotationType.toString();
        final String KlassCanonicalName = klass.getCanonicalName();
        return annotationTypeString.equals(KlassCanonicalName);
    }
}

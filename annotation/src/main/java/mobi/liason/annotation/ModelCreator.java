package mobi.liason.annotation;

import android.content.Context;
import com.squareup.javawriter.JavaWriter;

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

import mobi.liason.mvvm.database.Model;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class ModelCreator {

    private static final String MODEL = "Model";
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
        final Name typeElementQualifiedName = typeElement.getQualifiedName();
        final String typeElementQualifiedNameString = typeElementQualifiedName.toString();

        final String className = typeElementQualifiedNameString + MODEL;

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(className);
            final Writer writer = javaFileObject.openWriter(); //new StringWriter();
            final String packageElementQualifiedName = packageElement.getQualifiedName().toString();

            final JavaWriter javaWriter = new JavaWriter(writer);
            javaWriter.emitPackage(packageElementQualifiedName);

            javaWriter.emitImports(Context.class, Model.class);

            javaWriter.beginType(className, CLASS, EnumSet.of(Modifier.PUBLIC), Model.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), "NAME", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), className+".class.getSimpleName()");

                final String contextString = Context.class.getSimpleName();
                final String declaration = Modifier.FINAL.toString() + " " + contextString;
                final String variableName = contextString.toLowerCase();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), declaration, variableName);
                javaWriter.emitStatement("return " + "NAME");
                javaWriter.endMethod();
            }

//            // ContentValues
//            {
//                for (final Element fieldElement : fieldElements) {
//                    final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
//                    final String fieldType = getFieldType(annotationMirrors);
//                    if (fieldType != null) {
//                        final Name simpleName = fieldElement.getSimpleName();
//                        final String simpleNameString = simpleName.toString();
//                        final String memberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
//                        javaWriter.emitAnnotation(SerializedName.class, typeElementQualifiedNameString + "." + simpleName);
//                        javaWriter.emitField(fieldType, memberVariableName, EnumSet.of(Modifier.PRIVATE, Modifier.FINAL));
//                    }
//                }
//            }
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

//            System.out.println(writer.toString());
        } catch (final Exception exception) {

        }
    }

    private static List<String> getConstructorParameters(final List<Element> fieldElements) {
        final List<String> constructorParameters = new ArrayList<String>(fieldElements.size() * 3);
        for (final Element fieldElement : fieldElements){
            final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
            final String fieldType = getFieldType(annotationMirrors);
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

    private static String getFieldType(final List<? extends AnnotationMirror> annotationMirrors) {
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

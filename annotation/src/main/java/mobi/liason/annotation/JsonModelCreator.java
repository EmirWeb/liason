package mobi.liason.annotation;

import com.google.gson.annotations.SerializedName;
import com.squareup.javawriter.JavaWriter;

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

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class JsonModelCreator {

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

        final String typeElementQualifiedNameString = typeElementSimpleName.toString();

        final String className = typeElementQualifiedNameString + JSON;

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(className);
            final Writer writer = javaFileObject.openWriter();

            final String packageElementQualifiedName = packageElement.getQualifiedName().toString();

            final JavaWriter javaWriter = new JavaWriter(writer);
            javaWriter.emitPackage(packageElementQualifiedName);

            if (!fieldElements.isEmpty()) {
                javaWriter.emitImports(SerializedName.class);
            }

            if (CreatorHelper.hasArray(fieldElements)) {
                javaWriter.emitImports(ArrayList.class);
            }

            final List<String> objectAnnotationTypes = CreatorHelper.getObjectAnnotationTypes(fieldElements);
            if (!objectAnnotationTypes.isEmpty()){
                javaWriter.emitImports(objectAnnotationTypes);
            }

            javaWriter.beginType(className, CLASS, EnumSet.of(Modifier.PUBLIC));

            // Fields
            {
                for (final Element fieldElement : fieldElements) {
                    final String fieldType = CreatorHelper.getFieldType(fieldElement);
                    if (fieldType != null) {
                        final Name simpleName = fieldElement.getSimpleName();
                        final String simpleNameString = simpleName.toString();
                        final String memberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
                        javaWriter.emitAnnotation(SerializedName.class, typeElementQualifiedNameString + "." + simpleName);
                        javaWriter.emitField(fieldType, memberVariableName, EnumSet.of(Modifier.PRIVATE, Modifier.FINAL));
                    }
                }
            }

            // Constructor
            {
                if (!fieldElements.isEmpty()) {
                    final List<String> constructorParameters = getConstructorParameters(fieldElements);
                    javaWriter.beginConstructor(EnumSet.of(Modifier.PUBLIC), constructorParameters, null);

                    for (final Element fieldElement : fieldElements) {
                        final String fieldType = CreatorHelper.getFieldType(fieldElement);
                        if (fieldType != null) {
                            final Name simpleName = fieldElement.getSimpleName();
                            final String simpleNameString = simpleName.toString();
                            final String memberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
                            final String fieldVariableName = VariableNameHelper.getConstructorParameterVariableName(simpleNameString);
                            javaWriter.emitStatement(memberVariableName + " = " + fieldVariableName);
                        }
                    }

                    javaWriter.endConstructor();
                }
            }

            // Get methods
            {
                for (final Element fieldElement : fieldElements) {
                    final String fieldType = CreatorHelper.getFieldType(fieldElement);
                    if (fieldType != null) {

                        final Name simpleName = fieldElement.getSimpleName();
                        final String simpleNameString = simpleName.toString();
                        final String getMethodName = VariableNameHelper.getGetMethodName(simpleNameString);
                        final String classMemberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);

                        javaWriter.beginMethod(fieldType, getMethodName, EnumSet.of(Modifier.PUBLIC));
                        javaWriter.emitStatement("return " + classMemberVariableName);
                        javaWriter.endMethod();
                    }
                }


            }

            javaWriter.endType();

            javaWriter.close();

        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<String> getConstructorParameters(final List<Element> fieldElements) {
        final List<String> constructorParameters = new ArrayList<String>(fieldElements.size() * 3);
        for (final Element fieldElement : fieldElements){

            final String fieldType = CreatorHelper.getFieldType(fieldElement);
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

}

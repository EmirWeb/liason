package mobi.liason.annotation.creators;

import com.google.gson.annotations.SerializedName;
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
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.elements.ModelElement;
import mobi.liason.annotation.helpers.CreatorHelper;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class JsonModelCreator {

    private static final String CLASS = "class";

    public static void processModels(final ProcessingEnvironment processingEnv, final Map<Element, List<Element>> elementMappings) {
        for (final Element element : elementMappings.keySet()) {
            final List<Element> elements = elementMappings.get(element);
            final TypeElement typeElement = (TypeElement) element;
            final ModelElement modelElement = new ModelElement(typeElement, elements);
            processModel(processingEnv, modelElement);
        }
    }

    private static void processModel(final ProcessingEnvironment processingEnv, final ModelElement modelElement) {

        try {
            final String className = modelElement.getJsonModelClassName();
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(className);
            final Writer writer = javaFileObject.openWriter();
            final StringWriter stringWriter = new StringWriter();
            final JavaWriter javaWriter = new JavaWriter(stringWriter);

            final String packageName = modelElement.getPackageName();
            javaWriter.emitPackage(packageName);

            final List<FieldElement> fieldElements = modelElement.getFieldElements();
            if (!fieldElements.isEmpty()) {
                javaWriter.emitImports(SerializedName.class);
            }

            if (CreatorHelper.hasArray(fieldElements)) {
                javaWriter.emitImports(ArrayList.class);
            }

            final List<String> objectAnnotationTypes = CreatorHelper.getImportsForObjectAnnotationTypes(fieldElements);
            if (!objectAnnotationTypes.isEmpty()){
                javaWriter.emitImports(objectAnnotationTypes);
            }

            javaWriter.beginType(className, CLASS, EnumSet.of(Modifier.PUBLIC));

            // Member Variables
            {
                for (final FieldElement fieldElement : fieldElements) {
                    final String fieldType = fieldElement.getJavaType();
                    if (fieldType != null) {
                        final String simpleName = fieldElement.getSimpleName();
                        final String memberVariableName = fieldElement.getClassMemberVariableName();
                        javaWriter.emitAnnotation(SerializedName.class, modelElement.getSimpleName() + "." + simpleName);
                        javaWriter.emitField(fieldType, memberVariableName, EnumSet.of(Modifier.PRIVATE, Modifier.FINAL));
                    }
                }
            }

            // Constructor
            {
                if (!fieldElements.isEmpty()) {
                    final List<String> constructorParameters = getConstructorParameters(fieldElements);
                    javaWriter.beginConstructor(EnumSet.of(Modifier.PUBLIC), constructorParameters, null);

                    for (final FieldElement fieldElement : fieldElements) {
                        final String javaType = fieldElement.getJavaType();
                        if (javaType != null) {
                            final String classMemberVariableName = fieldElement.getClassMemberVariableName();
                            final String parameterVariableName = fieldElement.getParameterVariableName();
                            javaWriter.emitStatement(classMemberVariableName + " = " + parameterVariableName);
                        }
                    }

                    javaWriter.endConstructor();
                }
            }

            // Get methods
            {
                for (final FieldElement fieldElement : fieldElements) {
                    final String fieldType = fieldElement.getJavaType();
                    if (fieldType != null) {
                        final String getMethodName = fieldElement.getMethodName();
                        final String classMemberVariableName = fieldElement.getClassMemberVariableName();

                        javaWriter.beginMethod(fieldType, getMethodName, EnumSet.of(Modifier.PUBLIC));
                        javaWriter.emitStatement("return " + classMemberVariableName);
                        javaWriter.endMethod();
                    }
                }


            }

            javaWriter.endType();

            javaWriter.close();

            final String toString = stringWriter.toString();
            System.out.println(toString);
            writer.write(toString);
            writer.flush();
            writer.close();

        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<String> getConstructorParameters(final List<FieldElement> fieldElements) {
        final List<String> constructorParameters = new ArrayList<String>(fieldElements.size() * 3);
        for (final FieldElement fieldElement : fieldElements){

            final String javaType = fieldElement.getJavaType();
            final String fieldVariableName = fieldElement.getParameterVariableName();

            if (javaType != null) {
                constructorParameters.add(Modifier.FINAL.toString() + " " + javaType);
                constructorParameters.add(fieldVariableName);
            }
        }
        return constructorParameters;
    }

}

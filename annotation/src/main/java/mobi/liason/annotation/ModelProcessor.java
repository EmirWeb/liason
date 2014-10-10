package mobi.liason.annotation;

import com.google.common.base.CaseFormat;
import com.google.gson.annotations.SerializedName;
import com.squareup.javawriter.JavaWriter;

import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.JavaFileObject;

public class ModelProcessor extends AbstractProcessor {

    private static final String JSON = "Json";
    private static final String CLASS = "class";

    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, Set<Element>> elementMappings = new HashMap<Element, Set<Element>>();

        final Set<? extends Element> modelElements = roundEnvironment.getElementsAnnotatedWith(Model.class);
        for (final Element modelElement : modelElements) {
            final ElementKind elementKind = modelElement.getKind();
            if (elementKind == ElementKind.CLASS) {
//                if (!elementMappings.containsKey(modelElement)) {
                elementMappings.put(modelElement, new HashSet<Element>());

//                Set<? extends Element> elementSet = new HashSet<Element>(modelElement);
//                elementSet.add(modelElement);
//                final Set<TypeElement> typeElements = ElementFilter.typesIn(elementSet);
//                final Set<VariableElement> typeElements1 = ElementFilter.fieldsIn(elementSet);
//
//                for (VariableElement variableElement : ElementFilter.fieldsIn(elementSet)){
//                    System.out.println("variableElement: " + variableElement);
//                }
//                }
            }
        }

        final Set<? extends Element> integerElements = roundEnvironment.getElementsAnnotatedWith(Integer.class);
        for (final Element integerElement : integerElements) {
            final ElementKind elementKind = integerElement.getKind();
            if (elementKind == ElementKind.FIELD) {
                final Element enclosingElement = integerElement.getEnclosingElement();
                if (elementMappings.containsKey(enclosingElement)) {
                    final Set<Element> elements = elementMappings.get(enclosingElement);
                    elements.add(integerElement);
                }
            }
        }

        processModels(elementMappings);
        return true;
    }

    private void processModels(final Map<Element, Set<Element>> elementMappings) {
        for (final Element modelElement : elementMappings.keySet()) {
            final Set<Element> fieldElements = elementMappings.get(modelElement);
            processModel(modelElement, fieldElements);
        }
    }

    private void processModel(final Element modelElement, final Set<Element> fieldElements) {
        final TypeElement typeElement = (TypeElement) modelElement;
        final PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        final Name typeElementQualifiedName = typeElement.getQualifiedName();
        final String typeElementQualifiedNameString = typeElementQualifiedName.toString();

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

            javaWriter.beginType(className, CLASS, EnumSet.of(Modifier.PUBLIC));

            // Fields
            {
                for (final Element fieldElement : fieldElements) {

                    final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
                    final String fieldType = getFieldType(annotationMirrors);
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
                final List<String> constructorParameters = getConstructorParameters(fieldElements);
                javaWriter.beginConstructor(EnumSet.of(Modifier.PUBLIC), constructorParameters, null);

                for (final Element fieldElement : fieldElements) {
                    final Name simpleName = fieldElement.getSimpleName();
                    final String simpleNameString = simpleName.toString();
                    final String memberVariableName = VariableNameHelper.getClassMemberVariableName(simpleNameString);
                    final String fieldVariableName = VariableNameHelper.getConstructorParameterVariableName(simpleNameString);
                    javaWriter.emitStatement(memberVariableName + " = " + fieldVariableName);
                }

                javaWriter.endConstructor();
            }

            // Get methods
            {

                for (final Element fieldElement : fieldElements) {

                    final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
                    final String fieldType = getFieldType(annotationMirrors);
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

        }
    }

    private List<String> getConstructorParameters(final Set<Element> fieldElements) {
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

    private String getFieldType(final List<? extends AnnotationMirror> annotationMirrors) {
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                return Long.class.getSimpleName();
            }
        }
        return null;
    }

    private boolean isAnnotationOfType(final Class<?> klass, final AnnotationMirror annotationMirror) {
        final DeclaredType annotationType = annotationMirror.getAnnotationType();
        final String annotationTypeString = annotationType.toString();
        final String KlassCanonicalName = klass.getCanonicalName();
        return annotationTypeString.equals(KlassCanonicalName);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<String>();
        supportedAnnotationTypes.add(Model.class.getName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

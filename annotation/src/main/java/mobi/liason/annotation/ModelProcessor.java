package mobi.liason.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

public class ModelProcessor extends AbstractProcessor {


    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, List<Element>> jsonElementMappings = getElementMappings(Json.class, annotations, roundEnvironment);
        JsonModelCreator.processModels(processingEnv, jsonElementMappings);


        final Map<Element, List<Element>> modelElementMappings = getElementMappings(Model.class, annotations, roundEnvironment);
        JsonModelCreator.processModels(processingEnv, modelElementMappings);
        ModelCreator.processModels(processingEnv, modelElementMappings);

        return true;
    }

    private Map<Element, List<Element>> getElementMappings(final Class modelClass, final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, List<Element>> elementMappings = new HashMap<Element, List<Element>>();
        final Set<? extends Element> modelElements = roundEnvironment.getElementsAnnotatedWith(modelClass);
        for (final Element modelElement : modelElements) {
            final ElementKind elementKind = modelElement.getKind();
            if (elementKind == ElementKind.CLASS) {
                elementMappings.put(modelElement, new ArrayList<Element>());
            }
        }

        final Set<? extends Element> integerElements = roundEnvironment.getElementsAnnotatedWith(Integer.class);
        final Set<? extends Element> textElements = roundEnvironment.getElementsAnnotatedWith(Text.class);
        final Set<? extends Element> realElements = roundEnvironment.getElementsAnnotatedWith(Real.class);
        final Set<? extends Element> objectElements = roundEnvironment.getElementsAnnotatedWith(Object.class);

        final List<Element> fieldElements = new ArrayList<Element>();
        fieldElements.addAll(textElements);
        fieldElements.addAll(integerElements);
        fieldElements.addAll(realElements);
        fieldElements.addAll(objectElements);


        for (final Element fieldElement : fieldElements) {
            final ElementKind elementKind = fieldElement.getKind();
            if (elementKind == ElementKind.FIELD) {
                final Element enclosingElement = fieldElement.getEnclosingElement();
                if (elementMappings.containsKey(enclosingElement)) {
                    final List<Element> elements = elementMappings.get(enclosingElement);
                    elements.add(fieldElement);
                }
            }
        }

        for (final List<Element> elements : elementMappings.values()) {
            Collections.sort(elements, new Comparator<Element>() {
                @Override
                public int compare(Element o1, Element o2) {
                    final String string2 = o2.toString();
                    final String string1 = o1.toString();
                    return string1.compareToIgnoreCase(string2);
                }
            });
        }
        return elementMappings;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<String>();
        supportedAnnotationTypes.add(Model.class.getName());
        supportedAnnotationTypes.add(Json.class.getName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

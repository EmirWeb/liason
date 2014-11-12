package mobi.liason.annotation;

import java.util.ArrayList;
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

import mobi.liason.annotation.annotations.Path;
import mobi.liason.annotation.annotations.PathAction;
import mobi.liason.annotation.annotations.Projection;
import mobi.liason.annotation.annotations.Selection;
import mobi.liason.annotation.annotations.mvvm.ViewModel;
import mobi.liason.annotation.creators.ViewModelCreator;
import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.elements.PathActionElement;
import mobi.liason.annotation.elements.PathElement;
import mobi.liason.annotation.elements.ViewModelElement;
import mobi.liason.annotation.helpers.CreatorHelper;

public class ViewModelProcessor extends AbstractProcessor {

    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, ViewModelElement> jsonElementMappings = getElementMappings(ViewModel.class, annotations, roundEnvironment);
        ViewModelCreator.processModels(processingEnv, jsonElementMappings);

        return true;
    }



    private Map<Element, ViewModelElement> getElementMappings(final Class modelClass, final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, ViewModelElement> elementMappings = new HashMap<Element, ViewModelElement>();
        final Set<? extends Element> viewModelElements = roundEnvironment.getElementsAnnotatedWith(modelClass);
        for (final Element viewModelElement : viewModelElements) {
            final ElementKind elementKind = viewModelElement.getKind();
            if (elementKind == ElementKind.CLASS) {
                elementMappings.put(viewModelElement, new ViewModelElement(viewModelElement));
            }
        }
        parseProjections(roundEnvironment, elementMappings);
        parseSelections(roundEnvironment, elementMappings);
        parsePathActions(roundEnvironment, elementMappings);
        parsePaths(roundEnvironment, elementMappings);

        return elementMappings;
    }

    private void parseProjections(RoundEnvironment roundEnvironment, Map<Element, ViewModelElement> elementMappings) {
        final ArrayList<Element> projectionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Projection.class));
        for (final Element projectionElement : projectionElements) {
            final Element enclosingElement = projectionElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelElement viewModelElement = elementMappings.get(enclosingElement);
                final FieldElement fieldElement = new FieldElement(projectionElement);
                viewModelElement.addProjectionFieldElement(fieldElement);
            }
        }
    }

    private void parseSelections(RoundEnvironment roundEnvironment, Map<Element, ViewModelElement> elementMappings) {
        final ArrayList<Element> selectionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Selection.class));
        for (final Element selectionElement : selectionElements) {
            final Element enclosingElement = selectionElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelElement viewModelElement = elementMappings.get(enclosingElement);
                viewModelElement.setSelectionElement(selectionElement);
            }
        }
    }

    private void parsePaths(RoundEnvironment roundEnvironment, Map<Element, ViewModelElement> elementMappings) {
        final ArrayList<Element> elements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Path.class));
        for (final Element element : elements) {
            final Element enclosingElement = element.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelElement viewModelElement = elementMappings.get(enclosingElement);
                final PathElement pathElement = new PathElement(element);
                viewModelElement.addPathElement(pathElement);
            }
        }
    }

    private void parsePathActions(RoundEnvironment roundEnvironment, Map<Element, ViewModelElement> elementMappings) {
        final ArrayList<Element> pathActionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(PathAction.class));
        for (final Element element : pathActionElements) {
            final Element enclosingElement = element.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelElement viewModelElement = elementMappings.get(enclosingElement);


                final PathActionElement pathActionElement = new PathActionElement(element);
                viewModelElement.addPathActionElement(pathActionElement);
            }
        }
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<String>();
        supportedAnnotationTypes.add(ViewModel.class.getName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

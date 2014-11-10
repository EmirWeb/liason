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
import mobi.liason.annotation.helpers.CreatorHelper;

public class ViewModelProcessor extends AbstractProcessor {

    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, ViewModelStructure> jsonElementMappings = getElementMappings(ViewModel.class, annotations, roundEnvironment);
        ViewModelCreator.processModels(processingEnv, jsonElementMappings);

        return true;
    }



    private Map<Element, ViewModelStructure> getElementMappings(final Class modelClass, final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        final Map<Element, ViewModelStructure> elementMappings = new HashMap<Element, ViewModelStructure>();
        final Set<? extends Element> viewModelElements = roundEnvironment.getElementsAnnotatedWith(modelClass);
        for (final Element viewModelElement : viewModelElements) {
            final ElementKind elementKind = viewModelElement.getKind();
            if (elementKind == ElementKind.CLASS) {
                elementMappings.put(viewModelElement, new ViewModelStructure());
            }
        }
        parseProjections(roundEnvironment, elementMappings);
        parseSelections(roundEnvironment, elementMappings);
        parsePathActions(roundEnvironment, elementMappings);
        parsePaths(roundEnvironment, elementMappings);

        return elementMappings;
    }

    private void parseProjections(RoundEnvironment roundEnvironment, Map<Element, ViewModelStructure> elementMappings) {
        final ArrayList<Element> projectionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Projection.class));
        for (final Element projectionElement : projectionElements) {
            final Element enclosingElement = projectionElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelStructure viewModelStructure = elementMappings.get(enclosingElement);
                viewModelStructure.mProjectionElements.add(projectionElement);
            }
        }

        for (final ViewModelStructure viewModelStructure : elementMappings.values()) {
            CreatorHelper.sortElements(viewModelStructure.mProjectionElements);
        }
    }

    private void parseSelections(RoundEnvironment roundEnvironment, Map<Element, ViewModelStructure> elementMappings) {
        final ArrayList<Element> selectionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Selection.class));
        for (final Element selectionElement : selectionElements) {
            final Element enclosingElement = selectionElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelStructure viewModelStructure = elementMappings.get(enclosingElement);
                viewModelStructure.mSelectionElement = selectionElement;
            }
        }
    }

    private void parsePaths(RoundEnvironment roundEnvironment, Map<Element, ViewModelStructure> elementMappings) {
        final ArrayList<Element> pathElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(Path.class));
        for (final Element pathElement : pathElements) {
            final Element enclosingElement = pathElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelStructure viewModelStructure = elementMappings.get(enclosingElement);
                viewModelStructure.mPathElements.add(pathElement);
            }
        }

        for (final ViewModelStructure viewModelStructure : elementMappings.values()) {
            CreatorHelper.sortElements(viewModelStructure.mPathElements);
        }
    }

    private void parsePathActions(RoundEnvironment roundEnvironment, Map<Element, ViewModelStructure> elementMappings) {
        final ArrayList<Element> pathActionElements = new ArrayList<Element>(roundEnvironment.getElementsAnnotatedWith(PathAction.class));
        for (final Element pathActionElement : pathActionElements) {
            final Element enclosingElement = pathActionElement.getEnclosingElement();
            if (elementMappings.containsKey(enclosingElement)) {
                final ViewModelStructure viewModelStructure = elementMappings.get(enclosingElement);
                final PathAction annotation = pathActionElement.getAnnotation(PathAction.class);
                final PathAction.PathType pathType = annotation.pathType();

                if (!viewModelStructure.mPathActionElements.containsKey(pathType)){
                    viewModelStructure.mPathActionElements.put(pathType, new ArrayList<Element>());
                }

                final List<Element> elements = viewModelStructure.mPathActionElements.get(pathType);
                elements.add(pathActionElement);
            }
        }

        for (final ViewModelStructure viewModelStructure : elementMappings.values()) {
            for (final PathAction.PathType pathType: viewModelStructure.mPathActionElements.keySet()){
                final List<Element> elements = viewModelStructure.mPathActionElements.get(pathType);

                CreatorHelper.sortElements(elements);
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

package mobi.liason.annotation.elements;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import mobi.liason.annotation.annotations.PathAction;

/**
 * Created by Emir on 14-11-12.
 */
public class PathActionElement extends BaseElement{

    public PathActionElement(final Element element) {
        super(element);
    }

    public PathAction.PathType getPathType(){
        final Element element = getElement();
        final PathAction annotation = element.getAnnotation(PathAction.class);
        final PathAction.PathType pathType = annotation.pathType();
        return pathType;
    }

    public PathAction getPathActionAnnotation() {
        final Element element = getElement();
        final PathAction annotation = element.getAnnotation(PathAction.class);
        return annotation;
    }

    public String getPathActionValue() {
        final PathAction pathActionAnnotation = getPathActionAnnotation();
        return pathActionAnnotation.value();
    }
}

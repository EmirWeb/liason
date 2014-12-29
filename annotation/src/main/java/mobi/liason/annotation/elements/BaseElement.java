package mobi.liason.annotation.elements;

import java.lang.reflect.Field;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

/**
 * Created by Emir on 14-11-12.
 */
public class BaseElement {

    private final Element mElement;
    private final String mSimpleName;


    public BaseElement(final Element element) {
        mElement = element;
        mSimpleName = getSimpleName(element);
    }

    public BaseElement(final String simpleName) {
        mElement = null;
        mSimpleName = simpleName;
    }

    private static String getSimpleName(final Element element){
        final Name simpleName = element.getSimpleName();
        if (simpleName == null)
            return null;
        return simpleName.toString();
    }

    public String getSimpleName(){
        return mSimpleName;
    }

    public Element getElement() {
        return mElement;
    }
}

package mobi.liason.annotation.elements;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by Emir on 14-11-09.
 */
public class ModelElement {

    private final TypeElement mTypeElement;
    private static final String JSON_MODEL_CLASS_NAME = "%sJson";
    private static final String MODEL_CLASS_NAME = "%sModel";
    private final List<FieldElement> mFieldElements = new ArrayList<FieldElement>();


    public ModelElement(final TypeElement typeElement, final List<Element> elements) {
        mTypeElement = typeElement;
        for (final Element element : elements){
            final FieldElement fieldElement = new FieldElement(element);
            mFieldElements.add(fieldElement);
        }
    }

    public String getPackageName() {
        final PackageElement packageElement = (PackageElement) mTypeElement.getEnclosingElement();
        if (packageElement == null) {
            return null;
        }
        final Name qualifiedName = packageElement.getQualifiedName();
        if (qualifiedName == null) {
            return null;
        }
        final String packageName = qualifiedName.toString();
        return packageName;
    }

    public String getJsonModelClassName() {
        final String simpleName = getSimpleName();
        return String.format(JSON_MODEL_CLASS_NAME, simpleName);
    }

    public String getModelClassName() {
        final String simpleName = getSimpleName();
        return String.format(MODEL_CLASS_NAME, simpleName);
    }

    public String getSimpleName() {
        final Name simpleName = mTypeElement.getSimpleName();
        final String simpleNameString = simpleName.toString();
        return simpleNameString;
    }

    public List<FieldElement> getFieldElements() {
        return mFieldElements;
    }
}

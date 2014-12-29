package mobi.liason.annotation.elements;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;

import mobi.liason.annotation.annotations.PathAction;
import mobi.liason.annotation.helpers.CreatorHelper;
import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.ViewModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;

public class ViewModelElement extends BaseElement{

    private static final String VIEW_MODEL = "%sViewModel";

    private final List<FieldElement> mProjectionElements = new ArrayList<FieldElement>();
    private final List<PathElement> mPathElements = new ArrayList<PathElement>();
    private final Map<PathAction.PathType, List<PathActionElement>> mPathActionElements = new HashMap<PathAction.PathType, List<PathActionElement>>();
    private SelectionElement mSelectionElement;

    public ViewModelElement(final Element element) {
        super(element);
    }

    public void setSelectionElement(final SelectionElement selectionElement) {
        mSelectionElement = selectionElement;
    }

    public SelectionElement getSelectionElement() {
        return mSelectionElement;
    }

    public void addProjectionFieldElement(final FieldElement fieldElement) {
        mProjectionElements.add(fieldElement);
    }

    public List<FieldElement> getProjectionFieldElements(){
        final ArrayList<FieldElement> fieldElements = new ArrayList<FieldElement>(mProjectionElements);
        CreatorHelper.sortBaseElements(fieldElements);
        return fieldElements;
    }

    public void addPathElement(final PathElement element) {
        mPathElements.add(element);
    }

    public List<PathElement> getPathElements(){
        final ArrayList<PathElement> elements = new ArrayList<PathElement>(mPathElements);
        CreatorHelper.sortBaseElements(elements);
        return elements;
    }

    public void addPathActionElement(final PathActionElement pathActionElement) {
        final PathAction.PathType pathType = pathActionElement.getPathType();
        if (!mPathActionElements.containsKey(pathType)){
            final ArrayList<PathActionElement> elements = new ArrayList<PathActionElement>();
            mPathActionElements.put(pathType, elements);
        }
        final List<PathActionElement> elements = mPathActionElements.get(pathType);
        elements.add(pathActionElement);
    }

    public List<PathActionElement> getPathActionElements(final PathAction.PathType pathType){
        if (!mPathActionElements.containsKey(pathType)){
            return null;
        }
        final List<PathActionElement> pathActionElements = mPathActionElements.get(pathType);
        final ArrayList<PathActionElement> sortedPathActionElementsCopy = new ArrayList<PathActionElement>(pathActionElements);
        CreatorHelper.sortBaseElements(sortedPathActionElementsCopy);
        return pathActionElements;
    }

    public String getClassName() {
        final String simpleName = getSimpleName();
        return String.format(VIEW_MODEL, simpleName);
    }

    public boolean hasPathActionElements() {
        return !mPathActionElements.isEmpty();
    }

    public boolean hasProjectionFieldElements() {
        return !mProjectionElements.isEmpty();
    }

    public String getPackageName() {
        final Element element = getElement();
        final PackageElement packageElement = (PackageElement) element.getEnclosingElement();
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

    public boolean hasPathElements() {
        return !mPathElements.isEmpty();
    }

    public String getSelectionMethodName() {
        return mSelectionElement.getSimpleName();
    }
}
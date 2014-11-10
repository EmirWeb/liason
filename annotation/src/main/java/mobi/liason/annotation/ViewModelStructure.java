package mobi.liason.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

import mobi.liason.annotation.annotations.PathAction;

public class ViewModelStructure {
    public List<Element> mProjectionElements = new ArrayList<Element>();
    public Element mSelectionElement;
    public List<Element> mPathElements = new ArrayList<Element>();
    public Map<PathAction.PathType, List<Element>> mPathActionElements = new HashMap<PathAction.PathType, List<Element>>();
}
package mobi.liason.annotation;

import com.google.common.base.CaseFormat;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import mobi.liason.annotation.helpers.CreatorHelper;
import mobi.liason.mvvm.database.Column;

/**
 * Created by Emir on 14-11-09.
 */
public class FieldElement {
    private final Element mElement;

    private static final String MEMBER_VARIABLE = "m%s";
    private static final String METHOD_NAME = "get%s";

    public FieldElement(final Element element) {
        mElement = element;
    }

    public String getSimpleName(){
        final Name simpleName = mElement.getSimpleName();
        if (simpleName == null)
            return null;
        return simpleName.toString();
    }

    public String getClassName(){
        final String simpleName = getSimpleName();
        if (simpleName == null)
            return null;

        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, simpleName);
    }

    public String getFinalStaticMemberVariableName(){
        return getSimpleName();
    }

    public String getClassMemberVariableName(){
        final String className = getClassName();
        return String.format(MEMBER_VARIABLE, className);
    }

    public String getParameterVariableName(){
        final String simpleName = getSimpleName();
        if (simpleName == null)
            return null;

        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, simpleName);
    }

    public String getVariableName(){
        return getParameterVariableName();
    }

    public String getJavaType() {
        final String javaType = CreatorHelper.getJavaType(mElement);
        return javaType;
    }

    public String getArrayListGenericJavaType() {
        final String javaType = CreatorHelper.getArrayListGenericJavaType(mElement);
        return javaType;
    }

    public Column.Type getColumneType(){
        final Column.Type type = CreatorHelper.getColumnType(mElement);
        return type;
    }

    public boolean isArray() {
        return CreatorHelper.isArray(mElement);
    }

    public boolean isPrimitiveType(){
        return CreatorHelper.isPrimitiveElement(mElement);
    }

    public boolean isPrimaryKey() {
        return CreatorHelper.isPrimaryKey(mElement);
    }

    public boolean isUnique() {
        return CreatorHelper.isUnique(mElement);
    }

    public boolean isArrayWithPrimitiveType() {
        return CreatorHelper.isPrimitiveArrayElement(mElement);
    }

    public Element getElement() {
        return mElement;
    }

    public String getMethodName() {
        final String className = getClassName();
        return String.format(METHOD_NAME, className);
    }
}

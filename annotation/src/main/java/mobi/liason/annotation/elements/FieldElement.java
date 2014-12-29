package mobi.liason.annotation.elements;

import com.google.common.base.CaseFormat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;

import mobi.liason.annotation.annotations.types.*;
import mobi.liason.annotation.annotations.types.Object;
import mobi.liason.annotation.helpers.CreatorHelper;
import mobi.liason.mvvm.database.Column;

/**
 * Created by Emir on 14-11-09.
 */
public class FieldElement extends BaseElement{


    private static final String MEMBER_VARIABLE = "m%s";
    private static final String METHOD_NAME = "get%s";
    private final Field mField;

    public FieldElement(final Element element) {
        super(element);
        mField = null;
    }

    public FieldElement(final Field field) {
        super(field.getName());
        mField = field;
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
        if (mField != null){
            final String javaType = CreatorHelper.getJavaType(mField);
            return javaType;
        }
        final Element element = getElement();
        final String javaType = CreatorHelper.getJavaType(element);
        return javaType;
    }

    public String getArrayListGenericJavaType() {
        if (mField != null){

        }
        final Element element = getElement();
        final String javaType = CreatorHelper.getArrayListGenericJavaType(element);
        return javaType;
    }

    public Column.Type getColumnType(){
        if (mField != null){
            return CreatorHelper.getColumnType(mField);
        }
        final Element element = getElement();
        final Column.Type type = CreatorHelper.getColumnType(element);
        return type;
    }

    public boolean isArray() {
        if (mField != null){

        }
        final Element element = getElement();
        return CreatorHelper.isArray(element);
    }

    public boolean isPrimitiveType(){
        if (mField != null){

        }
        final Element element = getElement();
        return CreatorHelper.isPrimitiveElement(element);
    }

    public boolean isPrimaryKey() {
        if (mField != null){

        }
        final Element element = getElement();
        return CreatorHelper.isPrimaryKey(element);
    }

    public boolean isUnique() {
        if (mField != null){

        }
        final Element element = getElement();
        return CreatorHelper.isUnique(element);
    }

    public boolean isArrayWithPrimitiveType() {
        if (mField != null){

        }
        final Element element = getElement();
        return CreatorHelper.isPrimitiveArrayElement(element);
    }

    public String getMethodName() {
        final String className = getClassName();
        return String.format(METHOD_NAME, className);
    }

    public Object getObjectAnnotation() {
        if (mField != null){

        }
        final Element element = getElement();
        final Object object = element.getAnnotation(Object.class);
        return object;
    }
}

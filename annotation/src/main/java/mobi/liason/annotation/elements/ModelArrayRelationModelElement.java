package mobi.liason.annotation.elements;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.List;

import mobi.liason.mvvm.database.Column;

/**
 * Created by Emir on 14-11-01.
 */
public class ModelArrayRelationModelElement {

    private final FieldElement mFieldElement;
    private final List<FieldElement> mFieldElements;
    private final ModelElement mModelElement;
    private static final String RELATION_MODEL = "RelationModel";

    public ModelArrayRelationModelElement(final FieldElement fieldElement, final ModelElement typeElement, final List<FieldElement> fieldElements) {
        mFieldElement = fieldElement;
        mModelElement = typeElement;
        mFieldElements = fieldElements;
    }

    public String getClassName() {
        final String foreignKeyClassName = mModelElement.getSimpleName();
        final String localClassName = getLocalClassName();
        return foreignKeyClassName + localClassName + RELATION_MODEL;
    }

    public String getPackageName() {
        return mModelElement.getPackageName();
    }

    public String getLocalArrayListGenericJavaType() {
        return mFieldElement.getArrayListGenericJavaType();
    }

    public String getLocalClassName() {
        return mFieldElement.getClassName();
    }

    public String getLocalVariableName() {
        return mFieldElement.getVariableName();
    }

    public Column.Type getLocalColumnType() {
        return mFieldElement.getColumnType();
    }

    public String getForeignKeyClassName() {
        return mModelElement.getModelClassName();
    }

    public List<FieldElement> getPrimaryKeys() {
        final List<FieldElement> primaryKeys = new ArrayList<FieldElement>();
        for (final FieldElement fieldElement : mFieldElements) {
            final String javaType = fieldElement.getJavaType();
            if (javaType != null) {
                if (!fieldElement.isArray() && fieldElement.isPrimitiveType() && fieldElement.isPrimaryKey()) {
                    primaryKeys.add(fieldElement);
                }
            }
        }
        return primaryKeys;
    }

    public String getLocalPublicStaticFinalVariableName() {
        return mFieldElement.getFinalStaticMemberVariableName();
    }

    public String getForeignKeySimpleName() {
        return mModelElement.getSimpleName();
    }

    public String getForeignKeyParameterVariableNamePrefix() {
        final String simpleName = mModelElement.getSimpleName();
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, simpleName);
    }

    public String getForeignKeyColumnVariableNamePrefix() {
        final String simpleName = mModelElement.getSimpleName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName);
    }
}

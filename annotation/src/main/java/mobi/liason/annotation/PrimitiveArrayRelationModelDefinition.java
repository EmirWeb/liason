package mobi.liason.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.mvvm.database.Column;

/**
 * Created by Emir on 14-11-01.
 */
public class PrimitiveArrayRelationModelDefinition {

    private final FieldElement mFieldElement;
    private final List<FieldElement> mFieldElements;
    private final TypeElement mTypeElement;
    private static final String RELATION_MODEL = "RelationModel";

    public PrimitiveArrayRelationModelDefinition(final FieldElement fieldElement, final TypeElement typeElement, final List<FieldElement> fieldElements) {
        mFieldElement = fieldElement;
        mTypeElement = typeElement;
        mFieldElements = fieldElements;
    }

    public String getClassName() {
        final String foreignKeyClassName = getForeignKeyClassName();
        final String localClassName = getLocalClassName();
        return foreignKeyClassName + localClassName + RELATION_MODEL;
    }

    public String getPackageName() {
        final PackageElement packageElement = (PackageElement) mTypeElement.getEnclosingElement();
        final String packageElementQualifiedName = packageElement.getQualifiedName().toString();
        return packageElementQualifiedName;
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
        return mFieldElement.getColumneType();
    }

    public String getForeignKeyClassName() {
        final Name typeElementSimpleName = mTypeElement.getSimpleName();
        final String typeElementClassName = typeElementSimpleName.toString();
        return typeElementClassName;
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
}

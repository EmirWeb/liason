package mobi.liason.annotation.elements;

import com.google.common.base.CaseFormat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mobi.liason.annotation.annotations.types.*;
import mobi.liason.mvvm.database.Column;
import mobi.liason.annotation.annotations.types.Object;
import mobi.liason.mvvm.database.annotations.PrimaryKey;

/**
 * Created by Emir on 14-11-01.
 */
public class ModelRelationModelElement {

    private final FieldElement mFieldElement;
    private final List<FieldElement> mFieldElements;
    private final ModelElement mModelElement;
    private static final String RELATION_MODEL = "RelationModel";

    public ModelRelationModelElement(final FieldElement fieldElement, final ModelElement typeElement, final List<FieldElement> fieldElements) {
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

    public String getLocalJavaType() {
        return mFieldElement.getJavaType();
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

    public List<FieldElement> getLocalPrimaryKeys() {
        final List<FieldElement> fieldElements = new ArrayList<FieldElement>();
        final Object objectAnnotation = mFieldElement.getObjectAnnotation();

        final String metaModel = objectAnnotation.metaModel();

        if (metaModel != null && !metaModel.isEmpty()) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(metaModel);
//            final java.lang.Object object = clazz.newInstance();
                final Field[] declaredFields = clazz.getDeclaredFields();
                for (final Field field : declaredFields){
                    final PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                    if (primaryKeyAnnotation != null){
                        final FieldElement fieldElement = new FieldElement(field);
                        fieldElements.add(fieldElement);
                    }
                }
            } catch (final ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
//        } catch (final InstantiationException instantiationException) {
//            instantiationException.printStackTrace();
//        } catch (final IllegalAccessException illegalAccessException) {
//            illegalAccessException.printStackTrace();
            }
        }



        return fieldElements;
    }

    public String getLocalParameterVariableNamePrefix() {
        final String simpleName = mFieldElement.getClassName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, simpleName);
    }

    public String getLocalColumnVariableNamePrefix() {
        final String simpleName = mFieldElement.getClassName();
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName);
    }

    public String getLocalMetaModelJavaType() {
        final String metaModel = mFieldElement.getSimpleName();
        return metaModel;
    }
}

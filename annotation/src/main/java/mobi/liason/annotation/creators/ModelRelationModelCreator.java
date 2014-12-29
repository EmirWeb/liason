package mobi.liason.annotation.creators;

import android.content.ContentValues;
import android.content.Context;

import com.squareup.javawriter.JavaWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.elements.ModelRelationModelElement;
import mobi.liason.annotation.helpers.VariableNameHelper;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ForeignKeyModelColumn;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.PrimaryKey;

/**
 * Created by Emir on 14-11-01.
 */
public class ModelRelationModelCreator {

    private static final String CLASS = "class";


    private static final String COLUMNS_INNER_CLASS_NAME = "Columns";
    private static final String CONTENT_VALUES_CLASS_NAME = ContentValues.class.getSimpleName();
    private static final String CONTENT_VALUES_VARIABLE_NAME = VariableNameHelper.getVariableNameFromClassName(CONTENT_VALUES_CLASS_NAME);
    private static final String CONTENT_VALUES_VARIABLE_DECLARATION = String.format("%s %s %s = new %s()", Modifier.FINAL.toString(), CONTENT_VALUES_CLASS_NAME, CONTENT_VALUES_VARIABLE_NAME, CONTENT_VALUES_CLASS_NAME);
    private static final String GET_CONTENT_VALUES = "getContentValues";
    private static final String FOREIGN_KEY_MODEL_COLUMN_DECLARATION = "new ForeignKeyModelColumn(%s.NAME, %s." + COLUMNS_INNER_CLASS_NAME + ".%s)";
    private static final String MODEL_COLUMN_DECLARATION = "new ModelColumn(%s.NAME, %s.%s, Column.Type.%s)";
    private static final String STATEMENT = "%s.put(" + COLUMNS_INNER_CLASS_NAME + ".%s_%s.getName(), %s)";
    private static final String VARIABLE_DECLARATION = Modifier.FINAL.toString() + " %s" ;


    public static void processModel(final ProcessingEnvironment processingEnv, final ModelRelationModelElement modelRelationModelElement) {

        final String modelClassName = modelRelationModelElement.getClassName();

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(modelClassName);
            final Writer writer = javaFileObject.openWriter();
            final String getPackageName = modelRelationModelElement.getPackageName();

            final Writer stringWriter = new StringWriter();
            final JavaWriter javaWriter = new JavaWriter(stringWriter);
            javaWriter.emitPackage(getPackageName);

            final List<String> types = new ArrayList<String>();
            types.add(Context.class.getCanonicalName());
            types.add(mobi.liason.mvvm.database.Model.class.getCanonicalName());
            types.add(PathDefinitions.class.getCanonicalName());
            types.add(PathDefinition.class.getCanonicalName());
            types.add(ColumnDefinitions.class.getCanonicalName());
            types.add(ColumnDefinition.class.getCanonicalName());
            types.add(PrimaryKey.class.getCanonicalName());

            types.add(ContentValues.class.getCanonicalName());
            types.add(ForeignKeyModelColumn.class.getCanonicalName());
            types.add(ModelColumn.class.getCanonicalName());
            types.add(Column.class.getCanonicalName());

            types.add(mobi.liason.loaders.Path.class.getCanonicalName());

            javaWriter.emitImports(types);

            javaWriter.beginType(modelClassName, CLASS, EnumSet.of(Modifier.PUBLIC), mobi.liason.mvvm.database.Model.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), "NAME", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), modelClassName + ".class.getSimpleName()");

                final String contextString = Context.class.getSimpleName();
                final String declaration = Modifier.FINAL.toString() + " " + contextString;
                final String variableName = contextString.toLowerCase();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), declaration, variableName);
                javaWriter.emitStatement("return " + "NAME");
                javaWriter.endMethod();
            }

            final String localFieldName = modelRelationModelElement.getLocalPublicStaticFinalVariableName();

            final String localClassName = modelRelationModelElement.getLocalJavaType();
            final String localMetaModelClassName = modelRelationModelElement.getLocalMetaModelJavaType();
            final String localParameterVariableNamePrefix = modelRelationModelElement.getLocalParameterVariableNamePrefix();
            final String localClassNameUpperCasePrefix = modelRelationModelElement.getLocalColumnVariableNamePrefix();
            final String localVariableName = modelRelationModelElement.getLocalVariableName();

            final String foreignKeyClassName = modelRelationModelElement.getForeignKeyClassName();
            final String foreignKeyClassNameUpperCasePrefix = modelRelationModelElement.getForeignKeyColumnVariableNamePrefix();
            final String foreignKeyClassNameVariableNamePrefix = modelRelationModelElement.getForeignKeyParameterVariableNamePrefix();

            final List<FieldElement> localPrimaryKeys = modelRelationModelElement.getLocalPrimaryKeys();

            final Column.Type localType = modelRelationModelElement.getLocalColumnType();

            final List<FieldElement> primaryKeys = modelRelationModelElement.getPrimaryKeys();

            // ContentValues
            {
                final List<String> parameters = new ArrayList<String>();
                for (final FieldElement fieldElement : primaryKeys){
                    final List<String> parameterDeclaration = getParameterDeclaration(foreignKeyClassNameVariableNamePrefix, fieldElement);
                    parameters.addAll(parameterDeclaration);
                }

                for (final FieldElement fieldElement : localPrimaryKeys){
                    final List<String> parameterDeclaration = getParameterDeclaration(localParameterVariableNamePrefix, fieldElement);
                    parameters.addAll(parameterDeclaration);
                }


                javaWriter.beginMethod(CONTENT_VALUES_CLASS_NAME, GET_CONTENT_VALUES, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), parameters, null);
                javaWriter.emitStatement(CONTENT_VALUES_VARIABLE_DECLARATION);

                for (final FieldElement fieldElement : primaryKeys){
                    final String statement = getStatement(foreignKeyClassNameUpperCasePrefix, foreignKeyClassNameVariableNamePrefix, CONTENT_VALUES_VARIABLE_NAME, fieldElement);
                    javaWriter.emitStatement(statement);
                }

                for (final FieldElement fieldElement : localPrimaryKeys){
                    final String statement = getStatement(localClassNameUpperCasePrefix, localParameterVariableNamePrefix, CONTENT_VALUES_VARIABLE_NAME, fieldElement);
                    javaWriter.emitStatement(statement);
                }

                javaWriter.emitStatement("return " + CONTENT_VALUES_VARIABLE_NAME);
                javaWriter.endMethod();
            }


            // Columns
            {
                javaWriter.emitAnnotation(ColumnDefinitions.class);
                javaWriter.beginType(COLUMNS_INNER_CLASS_NAME, CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                for (final FieldElement fieldElement : primaryKeys){
                    final String simpleName = fieldElement.getSimpleName();

                    final String declaration = String.format(FOREIGN_KEY_MODEL_COLUMN_DECLARATION, modelClassName, foreignKeyClassName, simpleName);
                    javaWriter.emitAnnotation(PrimaryKey.class);
                    javaWriter.emitAnnotation(ColumnDefinition.class);
                    javaWriter.emitField(ForeignKeyModelColumn.class.getSimpleName(), foreignKeyClassNameUpperCasePrefix + "_" + simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }

                for (final FieldElement fieldElement : localPrimaryKeys){
                    final String simpleName = fieldElement.getSimpleName();

                    final Column.Type columnType = fieldElement.getColumnType();
                    final String declaration = String.format(MODEL_COLUMN_DECLARATION, modelClassName, localClassName, simpleName, columnType);
                    javaWriter.emitAnnotation(PrimaryKey.class);
                    javaWriter.emitAnnotation(ColumnDefinition.class);
                    javaWriter.emitField(ForeignKeyModelColumn.class.getSimpleName(), localClassNameUpperCasePrefix+ "_" + simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }

                javaWriter.endType();
            }

            // Paths
            {
                javaWriter.emitAnnotation(PathDefinitions.class);
                javaWriter.beginType("Paths", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                javaWriter.emitAnnotation(PathDefinition.class);

                final String declaration = String.format("new Path(%s.NAME)", modelClassName);
                final String publicStaticNameFromClassName = VariableNameHelper.getPublicStaticNameFromClassName(modelClassName);
                javaWriter.emitField(mobi.liason.loaders.Path.class.getSimpleName(), publicStaticNameFromClassName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);

                javaWriter.endType();
            }

            javaWriter.endType();
            javaWriter.close();

            final String code = stringWriter.toString();
            System.out.println(code);
            writer.write(code);
            writer.flush();
            writer.close();
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    private static String getStatement(final String publicStaticPrefix, final String variableNamePrefix, final String contentValuesVariableName, final FieldElement fieldElement) {
        final String simpleName = fieldElement.getSimpleName();
        final String variableName = variableNamePrefix + fieldElement.getClassName();

        return String.format(STATEMENT, contentValuesVariableName, publicStaticPrefix, simpleName, variableName);
    }

    private static List<String> getParameterDeclaration(final String variableNamePrefix, final FieldElement fieldElement) {
        final List<String> parameters = new ArrayList<String>(2);
        final String variableName = variableNamePrefix + fieldElement.getClassName();
        final String javaType = fieldElement.getJavaType();
        final String variableDeclaration = String.format(VARIABLE_DECLARATION, javaType);

        parameters.add(variableDeclaration);
        parameters.add(variableName);

        return parameters;


    }
}

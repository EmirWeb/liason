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

import mobi.liason.annotation.PrimitiveArrayRelationModelDefinition;
import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.helpers.VariableNameHelper;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.ForeignKeyModelColumn;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;

/**
 * Created by Emir on 14-11-01.
 */
public class PrimitiveArrayRelationModelCreator {

    private static final String CLASS = "class";

    public static void processModel(final ProcessingEnvironment processingEnv, final PrimitiveArrayRelationModelDefinition primitiveArrayRelationModelDefinition) {

        final String modelClassName = primitiveArrayRelationModelDefinition.getClassName();

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(modelClassName);
            final Writer writer = javaFileObject.openWriter();
            final String getPackageName = primitiveArrayRelationModelDefinition.getPackageName();

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

            types.add(ContentValues.class.getCanonicalName());
            types.add(ForeignKeyModelColumn.class.getCanonicalName());
            types.add(ModelColumn.class.getCanonicalName());
            types.add(Column.class.getCanonicalName());

            types.add(mobi.liason.loaders.Path.class.getCanonicalName());

            javaWriter.emitImports(types);

            javaWriter.beginType(modelClassName, CLASS, EnumSet.of(Modifier.PUBLIC), mobi.liason.mvvm.database.Model.class.getSimpleName());

            // Name
            {
                javaWriter.emitField(String.class.getSimpleName(), "NAME", EnumSet.of(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC), modelClassName+".class.getSimpleName()");

                final String contextString = Context.class.getSimpleName();
                final String declaration = Modifier.FINAL.toString() + " " + contextString;
                final String variableName = contextString.toLowerCase();

                javaWriter.emitAnnotation(Override.class);
                javaWriter.beginMethod(String.class.getSimpleName(), "getName", EnumSet.of(Modifier.PUBLIC), declaration, variableName);
                javaWriter.emitStatement("return " + "NAME");
                javaWriter.endMethod();
            }


            final String indexSimpleName = "INDEX";
            final String indexVariableName = "index";
            final String localFieldName = primitiveArrayRelationModelDefinition.getLocalPublicStaticFinalVariableName();

            final String localClassName = primitiveArrayRelationModelDefinition.getLocalArrayListGenericJavaType();
            final String localVariableName = primitiveArrayRelationModelDefinition.getLocalVariableName();
            final String foreignKeyClassName = primitiveArrayRelationModelDefinition.getForeignKeyClassName();
            final String foreignKeyClassNameUpperCase = VariableNameHelper.getPublicStaticNameFromClassName(foreignKeyClassName);
            final String foreignKeyClassNameVariableName= VariableNameHelper.getVariableNameFromClassName(foreignKeyClassName);


            final Column.Type localType = primitiveArrayRelationModelDefinition.getLocalColumnType();


            final List<FieldElement> primaryKeys = primitiveArrayRelationModelDefinition.getPrimaryKeys();

            // ContentValues
            {
                final List<String> parameters = new ArrayList<String>();
                for (final FieldElement fieldElement : primaryKeys){
                    final String variableName = foreignKeyClassNameVariableName + fieldElement.getClassName();
                    final String javaType = fieldElement.getJavaType();
                    final String decleration = Modifier.FINAL.toString() + " " + javaType;

                    parameters.add(decleration);
                    parameters.add(variableName);
                }


                final String localDecleration = Modifier.FINAL.toString() + " " + localClassName;
                parameters.add(localDecleration);
                parameters.add(localVariableName);

                final String indexDecleration = Modifier.FINAL.toString() + " int";

                parameters.add(indexDecleration);
                parameters.add(indexVariableName);

                final String contentValuesClassName = ContentValues.class.getSimpleName();
                final String contentValuesVariableName = VariableNameHelper.getVariableNameFromClassName(contentValuesClassName);

                javaWriter.beginMethod(contentValuesClassName, "getContentValues", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), parameters, null);

                javaWriter.emitStatement("%s %s %s = new %s()", Modifier.FINAL.toString(), contentValuesClassName, contentValuesVariableName , contentValuesClassName );

                for (final FieldElement fieldElement : primaryKeys){
                    final String simpleName = fieldElement.getSimpleName();
                    final String variableName = foreignKeyClassNameVariableName + fieldElement.getClassName();

                    javaWriter.emitStatement(contentValuesVariableName + ".put(Columns." + foreignKeyClassNameUpperCase + "_" + simpleName + ".getName(), " + variableName + ")");
                }

                javaWriter.emitStatement(contentValuesVariableName + ".put(Columns." + localFieldName + ".getName(), " + localVariableName + ")");
                javaWriter.emitStatement(contentValuesVariableName + ".put(Columns." + indexSimpleName + ".getName(), " + "index)");

                javaWriter.emitStatement("return " + contentValuesVariableName);
                javaWriter.endMethod();
            }


            // Columns
            {
                javaWriter.emitAnnotation(ColumnDefinitions.class);
                javaWriter.beginType("Columns", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                for (final FieldElement fieldElement : primaryKeys){
                    final String simpleName = fieldElement.getSimpleName();

                    final String declaration = String.format("new ForeignKeyModelColumn(%s.NAME, %sModel.Columns.%s)",
                            modelClassName, foreignKeyClassName, simpleName);
                    javaWriter.emitAnnotation(ColumnDefinition.class);
                    javaWriter.emitField(ForeignKeyModelColumn.class.getSimpleName(), foreignKeyClassNameUpperCase + "_" + simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                }

                javaWriter.emitAnnotation(ColumnDefinition.class);

                final String LocalDeclaration = String.format("new ModelColumn(%s.NAME, %s.%s, %s.%s.%s)",
                        modelClassName, foreignKeyClassName, localFieldName, Column.class.getSimpleName(), Column.Type.class.getSimpleName(), localType);
                javaWriter.emitField(ModelColumn.class.getSimpleName(), localFieldName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), LocalDeclaration);

                javaWriter.emitAnnotation(ColumnDefinition.class);
                final String indexDeclaration = String.format("new ModelColumn(%s.NAME, \"%s\", %s.%s.%s)",
                        modelClassName, indexVariableName, Column.class.getSimpleName(), Column.Type.class.getSimpleName(), Column.Type.integer);
                javaWriter.emitField(ModelColumn.class.getSimpleName(), indexSimpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), indexDeclaration);


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
}

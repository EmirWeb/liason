package mobi.liason.annotation.creators;

import android.content.ContentValues;
import android.content.Context;

import com.squareup.javawriter.JavaWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import mobi.liason.annotation.elements.ModelArrayRelationModelElement;
import mobi.liason.annotation.elements.ModelRelationModelElement;
import mobi.liason.annotation.elements.PrimitiveArrayRelationModelElement;
import mobi.liason.annotation.elements.FieldElement;
import mobi.liason.annotation.elements.ModelElement;
import mobi.liason.annotation.helpers.CreatorHelper;
import mobi.liason.annotation.helpers.VariableNameHelper;
import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.Column.Type;
import mobi.liason.mvvm.database.Model;
import mobi.liason.mvvm.database.ModelColumn;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.PrimaryKey;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class ModelCreator {

    private static final String CLASS = "class";

    public static void processModels(final ProcessingEnvironment processingEnv, final Map<Element, List<Element>> elementMappings) {
        for (final Element element : elementMappings.keySet()) {
            final List<Element> elements = elementMappings.get(element);
            final TypeElement typeElement = (TypeElement) element;
            final ModelElement modelElement = new ModelElement(typeElement, elements);
            processModel(processingEnv, modelElement);
        }
    }

    private static void processModel(final ProcessingEnvironment processingEnv, final ModelElement modelElement) {
        final List<FieldElement> fieldElements = modelElement.getFieldElements();


        final String modelSimpleName = modelElement.getSimpleName();
        final String modelClassName = modelElement.getModelClassName();
        final String jsonModelClassName = modelElement.getJsonModelClassName();
        final String jsonModelPackageName = modelElement.getJsonModelPackageName();

        try {
            final JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(modelClassName);
            final Writer writer = javaFileObject.openWriter();
            final String packageName = modelElement.getPackageName();

            final Writer stringWriter = new StringWriter();
            final JavaWriter javaWriter = new JavaWriter(stringWriter);
            javaWriter.emitPackage(packageName);

            final List<String> types = new ArrayList<String>();
            types.add(Context.class.getCanonicalName());
            types.add(Model.class.getCanonicalName());
            types.add(PathDefinitions.class.getCanonicalName());
            types.add(PathDefinition.class.getCanonicalName());
            types.add(Path.class.getCanonicalName());

            if (!jsonModelPackageName.equals(jsonModelClassName)) {
                types.add(jsonModelPackageName);
            }

            if (!fieldElements.isEmpty()) {
                types.add(ContentValues.class.getCanonicalName());
                types.add(ColumnDefinitions.class.getCanonicalName());
                types.add(ColumnDefinition.class.getCanonicalName());
                types.add(ModelColumn.class.getCanonicalName());
                types.add(Column.class.getCanonicalName());
            }

            if (CreatorHelper.hasUnique(fieldElements)){
                types.add(Unique.class.getCanonicalName());
            }

            if (CreatorHelper.hasPrimaryKey(fieldElements)){
                types.add(PrimaryKey.class.getCanonicalName());
            }

            javaWriter.emitImports(types);


            javaWriter.beginType(modelClassName, CLASS, EnumSet.of(Modifier.PUBLIC), Model.class.getSimpleName());

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

            // ContentValues
            {
                if (!fieldElements.isEmpty()) {

                    final String decleration = Modifier.FINAL.toString() + " " + jsonModelClassName;
                    final String jsonVariableName = VariableNameHelper.getVariableNameFromClassName(jsonModelClassName);
                    final String contentValuesClassName = ContentValues.class.getSimpleName();
                    final String contentValuesVariableName = VariableNameHelper.getVariableNameFromClassName(contentValuesClassName);

                    javaWriter.beginMethod(contentValuesClassName, "getContentValues", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC),decleration, jsonVariableName);

                    javaWriter.emitStatement("%s %s %s = new %s()", Modifier.FINAL.toString(), contentValuesClassName, contentValuesVariableName , contentValuesClassName );

                    for (final FieldElement fieldElement : fieldElements) {
                        final String fieldType = fieldElement.getJavaType();
                        if (fieldType != null) {
                            if (!fieldElement.isArray() && fieldElement.isPrimitiveType()) {
                                final String simpleName = fieldElement.getSimpleName();
                                javaWriter.emitStatement(contentValuesVariableName + ".put(Columns." + simpleName + ".getName(), " + jsonVariableName + "." + VariableNameHelper.getGetMethodName(simpleName) + "())");
                            }
                        }
                    }

                    javaWriter.emitStatement("return " + contentValuesVariableName);
                    javaWriter.endMethod();
                }
            }

            // Columns
            {
                if (!fieldElements.isEmpty()) {
                    javaWriter.emitAnnotation(ColumnDefinitions.class);
                    javaWriter.beginType("Columns", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                    for (final FieldElement fieldElement : fieldElements) {
                        final String fieldType = fieldElement.getJavaType();
                        if (fieldType != null) {
                            if (!fieldElement.isArray() && fieldElement.isPrimitiveType()) {
                                final String simpleName = fieldElement.getSimpleName();
                                javaWriter.emitAnnotation(ColumnDefinition.class);

                                final boolean isUnique = fieldElement.isUnique();
                                if (isUnique){
                                    javaWriter.emitAnnotation(Unique.class);
                                }

                                final boolean isPrimaryKey = fieldElement.isPrimaryKey();
                                if (isPrimaryKey){
                                    javaWriter.emitAnnotation(PrimaryKey.class);
                                }

                                final Type type = fieldElement.getColumnType();
                                final String declaration = String.format("new ModelColumn(%s.NAME, %s.%s, %s.%s.%s)",
                                        modelClassName, modelSimpleName, simpleName, Column.class.getSimpleName(), Type.class.getSimpleName(), type.toString());
                                javaWriter.emitField(ModelColumn.class.getSimpleName(), simpleName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);
                            }
                        }
                    }
                    javaWriter.endType();
                }
            }

            // Paths
            {
                javaWriter.emitAnnotation(PathDefinitions.class);
                javaWriter.beginType("Paths", CLASS, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));

                javaWriter.emitAnnotation(PathDefinition.class);

                final String declaration = String.format("new Path(%s.NAME)", modelClassName);
                final String publicStaticNameFromClassName = VariableNameHelper.getPublicStaticNameFromClassName(modelSimpleName);
                javaWriter.emitField(Path.class.getSimpleName(), publicStaticNameFromClassName, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL), declaration);

                javaWriter.endType();
            }

            javaWriter.endType();
            javaWriter.close();

            final String code = stringWriter.toString();
            System.out.println(code);
            writer.write(code);
            writer.flush();
            writer.close();

            for (final FieldElement fieldElement : fieldElements) {
                final String javaType = fieldElement.getJavaType();
                if (javaType != null) {
                    if (!fieldElement.isArray() && fieldElement.isPrimitiveType()) {

                    } else if (fieldElement.isArrayWithPrimitiveType()) {
                        final PrimitiveArrayRelationModelElement primitiveArrayRelationModelElement = new PrimitiveArrayRelationModelElement(fieldElement, modelElement, fieldElements);
                        PrimitiveArrayRelationModelCreator.processModel(processingEnv, primitiveArrayRelationModelElement);
                    } else if (fieldElement.isArray()) {
                        final ModelArrayRelationModelElement modelArrayRelationModelElement = new ModelArrayRelationModelElement(fieldElement, modelElement, fieldElements);
                        ModelArrayRelationModelCreator.processModel(processingEnv, modelArrayRelationModelElement);
                    } else {
                        final ModelRelationModelElement modelArrayRelationModelElement = new ModelRelationModelElement(fieldElement, modelElement, fieldElements);
                        ModelRelationModelCreator.processModel(processingEnv, modelArrayRelationModelElement);
                    }
                }
            }

        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

}

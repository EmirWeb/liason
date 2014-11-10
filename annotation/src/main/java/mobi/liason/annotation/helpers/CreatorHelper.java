package mobi.liason.annotation.helpers;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import mobi.liason.annotation.*;
import mobi.liason.annotation.Integer;
import mobi.liason.annotation.Object;
import mobi.liason.mvvm.database.Column;
import mobi.liason.mvvm.database.annotations.PrimaryKey;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class CreatorHelper {
    public static boolean hasArray(final List<FieldElement> fieldElements){
        for (final FieldElement fieldElement : fieldElements){
            if (fieldElement.isArray()){
                return true;
            }
        }
        return false;
    }

    public static AnnotationMirror getModelAnnotationMirror(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(mobi.liason.annotation.Integer.class, annotationMirror)){
                return annotationMirror;
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                return annotationMirror;
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                return annotationMirror;
            }else if (isAnnotationOfType(Object.class, annotationMirror)){
                return annotationMirror;
            }
        }
        return null;
    }

    public static boolean isArray( final Element fieldElement){
        final AnnotationMirror annotationMirror = getModelAnnotationMirror(fieldElement);
        if (isAnnotationOfType(Integer.class, annotationMirror)) {
            final Integer annotation = fieldElement.getAnnotation(Integer.class);
            if (annotation.isArray()) {
                return true;
            }
        } else if (isAnnotationOfType(Text.class, annotationMirror)) {
            final Text annotation = fieldElement.getAnnotation(Text.class);
            if (annotation.isArray()) {
                return true;
            }
        } else if (isAnnotationOfType(Real.class, annotationMirror)) {
            final Real annotation = fieldElement.getAnnotation(Real.class);
            if (annotation.isArray()) {
                return true;
            }
        } else if (isAnnotationOfType(Object.class, annotationMirror)) {
            final Object annotation = fieldElement.getAnnotation(Object.class);
            if (annotation.isArray()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnnotationOfType(final Class<?> klass, final AnnotationMirror annotationMirror) {
        final DeclaredType annotationType = annotationMirror.getAnnotationType();
        final String annotationTypeString = annotationType.toString();
        final String KlassCanonicalName = klass.getCanonicalName();
        return annotationTypeString.equals(KlassCanonicalName);
    }

    public static String getArrayListGenericJavaType(final Element fieldElement) {
        if (!isArray(fieldElement)){
            return null;
        }
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                final Integer annotation = fieldElement.getAnnotation(Integer.class);
                final String longString = Long.class.getSimpleName();

                return longString;
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                final Text annotation = fieldElement.getAnnotation(Text.class);
                final String stringString = String.class.getSimpleName();

                return stringString;
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                final Real annotation = fieldElement.getAnnotation(Real.class);
                final String doubleString = Double.class.getSimpleName();

                return doubleString;
            }else if (isAnnotationOfType(Object.class, annotationMirror)){
                final Object annotation = fieldElement.getAnnotation(Object.class);
                final String className = getClassName(annotation);

                return className;
            }
        }
        return null;
    }

    public static String getJavaType(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                final Integer annotation = fieldElement.getAnnotation(Integer.class);
                final String longString = Long.class.getSimpleName();
                if (annotation.isArray()){
                    return ArrayList.class.getSimpleName() + "<" + longString + ">";
                }

                return longString;
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                final Text annotation = fieldElement.getAnnotation(Text.class);
                final String stringString = String.class.getSimpleName();
                if (annotation.isArray()){
                    return ArrayList.class.getSimpleName() + "<" + stringString + ">";
                }

                return stringString;
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                final Real annotation = fieldElement.getAnnotation(Real.class);
                final String doubleString = Double.class.getSimpleName();
                if (annotation.isArray()){
                    return ArrayList.class.getSimpleName() + "<" + doubleString + ">";
                }

                return doubleString;
            }else if (isAnnotationOfType(Object.class, annotationMirror)){
                final Object annotation = fieldElement.getAnnotation(Object.class);
                final String className = getClassName(annotation);
                if (annotation.isArray()){
                    return ArrayList.class.getSimpleName() + "<" + className + ">";
                }

                return className;
            }
        }
        return null;
    }

    public static String getClassName(final Object object){
        try {
            final String value = object.value();
            return value;
        } catch( final MirroredTypeException mirroredTypeException) {
            final TypeMirror typeMirror = mirroredTypeException.getTypeMirror();
            return typeMirror.toString();
        }
    }

    public static List<String> getImportsForObjectAnnotationTypes(final List<FieldElement> fieldElements) {
        final List<String> objectAnnotationTypes = new ArrayList<String>();
        for (final FieldElement fieldElement: fieldElements) {
            final Element element = fieldElement.getElement();
            final List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
            for (final AnnotationMirror annotationMirror : annotationMirrors) {
                if (isAnnotationOfType(Object.class, annotationMirror)) {
                    final Object annotation = element.getAnnotation(Object.class);
                    final String fullClassName = getFullClassName(annotation);
                    objectAnnotationTypes.add(fullClassName);
                }
            }
        }
        return objectAnnotationTypes;
    }

    public static String getFullClassName(final Object object){
        try {
            final String value = object.value();
            return value;
        } catch( final MirroredTypeException mirroredTypeException) {
            final TypeMirror typeMirror = mirroredTypeException.getTypeMirror();
            return typeMirror.toString();
        } catch (final AnnotationTypeMismatchException annotationTypeMismatchException){
            System.out.println("object: " + object);
            annotationTypeMismatchException.printStackTrace();
        }
        return null;
    }

    public static Column.Type getColumnType(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
                return Column.Type.integer;
            } else if (isAnnotationOfType(Text.class, annotationMirror)){
                return Column.Type.text;
            } else if (isAnnotationOfType(Real.class, annotationMirror)){
                return Column.Type.real;
            }
        }
        return Column.Type.text;
    }

    public static boolean isUnique(final Element fieldElement) {
        final Unique annotation = fieldElement.getAnnotation(Unique.class);
        return annotation != null;
    }

    public static boolean isPrimaryKey(final Element fieldElement) {
        final PrimaryKey annotation = fieldElement.getAnnotation(PrimaryKey.class);
        return annotation != null;
    }

//    public static boolean hasUnique(final List<Element> fieldElements) {
//        for (final Element fieldElement: fieldElements){
//            if (isUnique(fieldElement)){
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean hasUnique(final List<FieldElement> fieldElements) {
        for (final FieldElement fieldElement: fieldElements){
            if (fieldElement.isUnique()){
                return true;
            }
        }
        return false;
    }

    public static boolean hasPrimaryKey(final List<FieldElement> fieldElements) {
        for (final FieldElement fieldElement: fieldElements){
            if (fieldElement.isPrimaryKey()){
                return true;
            }
        }
        return false;
    }

//    public static boolean hasPrimaryKey(final List<Element> fieldElements) {
//        for (final Element fieldElement: fieldElements){
//            if (isPrimaryKey(fieldElement)){
//                return true;
//            }
//        }
//        return false;
//    }

    public static boolean isPrimitiveElement(final Element fieldElement) {
        if (isArray(fieldElement)){
            return false;
        }

        final Object annotation = fieldElement.getAnnotation(Object.class);
        if (annotation != null){
            final String value = annotation.value();
            if (value.equals("java.lang.String")){
                return true;
            } else if (value.equals("java.lang.Integer")){
                return true;
            } else if (value.equals("java.lang.Double")){
                return true;
            } else if (value.equals("java.lang.Float")){
                return true;
            } else if (value.equals("java.lang.Boolean")){
                return true;
            } else if (value.equals("java.lang.Character")){
                return true;
            } else if (value.equals("java.lang.Byte")){
                return true;
            } else if (value.equals("java.lang.Long")){
                return true;
            } else if (value.equals("java.lang.Short")){
                return true;
            }
            return false;
        }

        return true;
    }

    public static boolean isPrimitiveArrayElement(final Element fieldElement) {
        final AnnotationMirror annotationMirror = getModelAnnotationMirror(fieldElement);
        if (isAnnotationOfType(Integer.class, annotationMirror)) {
            final Integer annotation = fieldElement.getAnnotation(Integer.class);
            if (annotation.isArray()) {
                return true;
            }
        } else if (isAnnotationOfType(Text.class, annotationMirror)) {
            final Text annotation = fieldElement.getAnnotation(Text.class);
            if (annotation.isArray()) {
                return true;
            }
        } else if (isAnnotationOfType(Real.class, annotationMirror)) {
            final Real annotation = fieldElement.getAnnotation(Real.class);
            if (annotation.isArray()) {
                return true;
            }
        }
        return false;
    }

    public static void sortElements(final List<Element> elements){
        Collections.sort(elements, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                final String string2 = o2.toString();
                final String string1 = o1.toString();
                return string1.compareToIgnoreCase(string2);
            }
        });
    }
}

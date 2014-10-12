package mobi.liason.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import mobi.liason.mvvm.database.Column;

/**
 * Created by Emir Hasanbegovic on 11/10/14.
 */
public class CreatorHelper {
    public static boolean hasArray(final List<Element> fieldElements){
        for (final Element fieldElement : fieldElements){
            final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
            for (final AnnotationMirror annotationMirror : annotationMirrors) {
                if (isArray(annotationMirror, fieldElement)){
                    return true;
                }
            }
        }
        return false;
    }

    public static AnnotationMirror getAnnotationMirror(final Element fieldElement) {
        final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
        for (final AnnotationMirror annotationMirror : annotationMirrors){
            if (isAnnotationOfType(Integer.class, annotationMirror)){
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

    public static boolean isArray(final AnnotationMirror annotationMirror, final Element fieldElement){
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

    public static String getFieldType(final Element fieldElement) {
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
            final Class value = object.value();
            return value.getSimpleName();
        } catch( final MirroredTypeException mirroredTypeException) {
            final TypeMirror typeMirror = mirroredTypeException.getTypeMirror();
            return typeMirror.toString();
        }
    }

    public static List<String> getObjectAnnotationTypes(final List<Element> fieldElements) {
        final List<String> objectAnnotationTypes = new ArrayList<String>();
        for (final Element fieldElement: fieldElements) {
            final List<? extends AnnotationMirror> annotationMirrors = fieldElement.getAnnotationMirrors();
            for (final AnnotationMirror annotationMirror : annotationMirrors) {
                if (isAnnotationOfType(Object.class, annotationMirror)) {
                    final Object annotation = fieldElement.getAnnotation(Object.class);
                    final String fullClassName = getFullClassName(annotation);
                    objectAnnotationTypes.add(fullClassName);
                }
            }
        }
        return objectAnnotationTypes;
    }

    public static String getFullClassName(final Object object){
        try {
            final Class value = object.value();
            return value.getCanonicalName();
        } catch( final MirroredTypeException mirroredTypeException) {
            final TypeMirror typeMirror = mirroredTypeException.getTypeMirror();
            return typeMirror.toString();
        }
    }

    public static Column.Type getTypeString(final Element fieldElement) {
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

}

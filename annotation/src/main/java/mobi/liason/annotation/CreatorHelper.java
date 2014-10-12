package mobi.liason.annotation;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

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
            if (CreatorHelper.isAnnotationOfType(Integer.class, annotationMirror)){
                return annotationMirror;
            } else if (CreatorHelper.isAnnotationOfType(Text.class, annotationMirror)){
                return annotationMirror;
            } else if (CreatorHelper.isAnnotationOfType(Real.class, annotationMirror)){
                return annotationMirror;
            }else if (CreatorHelper.isAnnotationOfType(Object.class, annotationMirror)){
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
}

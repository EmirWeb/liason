package mobi.liason.annotation;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
public @interface Object {
    Class value();
    boolean isArray() default false;
}

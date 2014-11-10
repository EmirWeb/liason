package mobi.liason.annotation.annotations.types;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
public @interface Object {
    String value();
    boolean isArray() default false;
}

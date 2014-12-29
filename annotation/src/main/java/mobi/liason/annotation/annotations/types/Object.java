package mobi.liason.annotation.annotations.types;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Object {
    String value();
    boolean isArray() default false;
    String[] primaryKeys() default {};
    String metaModel() default "";
}

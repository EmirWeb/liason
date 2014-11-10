package mobi.liason.annotation.annotations;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
public @interface PathAction {
    String value();
    PathType pathType() default PathType.query;

    public static enum PathType {
        query,
        insert,
        delete,
        update,
        bulkInsert
    }
}

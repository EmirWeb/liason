package mobi.liason.annotation;

import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 09/10/14.
 */
@Model
public class ProductMetaModel {
    @Unique
    @Integer
    public static final String ID = "id";

    @Text
    public static final String NAME = "name";

    @Text
    public static final String IMAGE_THUMB_URL = "image_thumb_url";

    @Text
    public static final String IMAGE_URL = "image_url";

    @Text
    public static final String DESCRIPTION = "description";

    @Text
    public static final String TASTING_NOTE = "tasting_note";

    @Route
    public static final String MAIN_ROUTE = "path";

    @RouteAction(MAIN_ROUTE)
    public static final void stuff(){

    }
}

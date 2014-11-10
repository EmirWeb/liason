package mobi.liason.sample.models;


import mobi.liason.annotation.Model;
import mobi.liason.annotation.Integer;
import mobi.liason.annotation.Text;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
@Model
public class Product {
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

}

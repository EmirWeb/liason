package mobi.liason.annotation.meta;

import mobi.liason.annotation.annotations.mvvm.Model;
import mobi.liason.annotation.annotations.types.Integer;
import mobi.liason.annotation.annotations.types.Text;
import mobi.liason.annotation.annotations.types.Object;
import mobi.liason.mvvm.database.annotations.PrimaryKey;

@Model
public class Image {
    @Integer
    @PrimaryKey
    public static final String ID = "id";
    @Integer
    @PrimaryKey
    public static final String ID2 = "id2";
    @Text
    public static final String URL = "url";
}

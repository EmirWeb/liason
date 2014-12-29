package mobi.liason.annotation.meta;

import mobi.liason.annotation.annotations.mvvm.Model;
import mobi.liason.annotation.annotations.types.Integer;
import mobi.liason.annotation.annotations.types.Object;
import mobi.liason.mvvm.database.annotations.PrimaryKey;

@Model
public class Product {
    @PrimaryKey
    @Integer
    public static final String ID = "id";
    @Object(value = "mobi.liason.ImageJson", metaModel = "mobi.liason.Image")
    public static final String NAME = "name";
}

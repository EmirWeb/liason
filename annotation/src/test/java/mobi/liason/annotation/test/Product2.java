package mobi.liason.annotation.test;

import mobi.liason.annotation.Object;
import mobi.liason.annotation.Model;
import mobi.liason.annotation.Product1;

@Model
public class Product2 {

    @Object(Product1.class)
    public static final String ID = "id";
}


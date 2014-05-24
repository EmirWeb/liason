package mobi.liason.sample.tasks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mobi.liason.sample.models.Product;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductsResponse {

    @SerializedName(Fields.RESULT)
    private final ArrayList<Product> mProducts;

    public ProductsResponse(final ArrayList<Product> products) {
        mProducts = products;
    }

    public ArrayList<Product> getProducts() {
        return mProducts;
    }


    public static class Fields {
        public static final String RESULT = "result";
    }
}

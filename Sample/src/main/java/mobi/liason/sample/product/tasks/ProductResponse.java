package mobi.liason.sample.product.tasks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mobi.liason.sample.models.Product;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ProductResponse {

    @SerializedName(Fields.RESULT)
    private final Product mProduct;

    public ProductResponse(final Product product) {
        mProduct = product;
    }

    public Product getProduct() {
        return mProduct;
    }


    public static class Fields {
        public static final String RESULT = "result";
    }
}

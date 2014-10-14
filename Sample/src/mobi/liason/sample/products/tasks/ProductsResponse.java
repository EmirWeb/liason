package mobi.liason.sample.products.tasks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mobi.liason.sample.models.Product;
import mobi.liason.sample.models.ProductJson;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
import mobi.liason.annotation.Json;
import mobi.liason.annotation.Object;
import mobi.liason.sample.models.ProductJson;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
@Json
public class ProductsResponse {

    @Object(value = "mobi.liason.sample.models.ProductJson", isArray = true)
    public static final String RESULT = "result";
}

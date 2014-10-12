package mobi.liason.sample.product.tasks;

import mobi.liason.annotation.Json;
import mobi.liason.annotation.Object;
import mobi.liason.sample.models.ProductJson;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
@Json
public class ProductResponse {

    @Object(value = ProductJson.class)
    public static final String RESULT = "result";

}

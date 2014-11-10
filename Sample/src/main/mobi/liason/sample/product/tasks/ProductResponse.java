package mobi.liason.sample.product.tasks;

import mobi.liason.annotation.Json;
import mobi.liason.annotation.Object;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
@Json
public class ProductResponse {

    @Object("mobi.liason.sample.models.ProductJson")
    public static final String RESULT = "result";

}

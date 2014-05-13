package mobi.liason.sample.content.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class Product {

    @SerializedName(Fields.ID)
    private final Long mId;

    @SerializedName(Fields.NAME)
    private final String mName;

    @SerializedName(Fields.IMAGE_THUMB_URL)
    private final String mImageThumbUrl;

    @SerializedName(Fields.IMAGE_URL)
    private final String mImageUrl;

    public Product(final Long id, final String name, final String imageThumbUrl, final String imageUrl) {
        mId = id;
        mName = name;
        mImageThumbUrl = imageThumbUrl;
        mImageUrl = imageUrl;
    }

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getImageThumbUrl() {
        return mImageThumbUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public static class Fields {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String IMAGE_THUMB_URL = "image_thumb_url";
        public static final String IMAGE_URL = "image_url";
    }
}

package mobi.liason.mvvm.network;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class TaskState {

    @SerializedName(Fields.URI)
    private final String mUri;

    @SerializedName(Fields.STATE)
    private final String mState;

    @SerializedName(Fields.TIME)
    private final Long mTime;

    @SerializedName(Fields.JSON)
    private final JsonObject mJsonObject;

    public TaskState(final String uri, final String state, final Long time, final JsonObject jsonObject) {
        mUri = uri;
        mState = state;
        mTime = time;
        mJsonObject = jsonObject;
    }

    public String getUri() {
        return mUri;
    }

    public String getState() {
        return mState;
    }

    public Long getTime() {
        return mTime;
    }

    public byte[] getJsonByteArray() {
        if (mJsonObject == null) {
            return null;
        }
        final String toString = mJsonObject.toString();
        return toString.getBytes();
    }

    public JsonObject getJsonObject() {
        return mJsonObject;
    }

    public static class Fields {
        public static final String URI = "uri";
        public static final String STATE = "state";
        public static final String TIME = "time";
        public static final String JSON = "json";
    }
}

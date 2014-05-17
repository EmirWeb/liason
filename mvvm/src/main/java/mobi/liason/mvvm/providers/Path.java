package mobi.liason.mvvm.providers;

import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Emir Hasanbegovic on 17/05/14.
 */
public class Path {

    private final String mPathString;
    private final List<String> mPathSegments;

    public Path(final String path) {
        mPathString = path;
        mPathSegments = new ArrayList<String>();
        final StringTokenizer stringTokenizer = new StringTokenizer(path, "/");
        while(stringTokenizer.hasMoreTokens()){
            final String pathSegment = stringTokenizer.nextToken();
            mPathSegments.add(pathSegment);
        }

    }

    public Path(final String... pathSegments) {
        mPathString = TextUtils.join("/", pathSegments);
        mPathSegments = Lists.newArrayList(pathSegments);
    }

    @Override
    public String toString() {
        return mPathString;
    }

    public List<String> getPathSegments() {
        return mPathSegments;
    }


}

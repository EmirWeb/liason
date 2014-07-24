package mobi.liason.loaders;

import android.net.Uri;
import android.text.TextUtils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Emir Hasanbegovic on 17/05/14.
 */
public class Path {

    private static final String NUMERIC_EXCEPTION = "Expecting numeric value, cannot insert %s into '#'.";
    private static final String MATCH_COUNT_MESSAGE = "Incoming values does not match number of bindings.";
    private final String mPath;
    private final List<String> mPathSegments;


    public Path(final String path) {
        mPath = path;
        mPathSegments = new ArrayList<String>();
        final StringTokenizer stringTokenizer = new StringTokenizer(path, "/");
        while(stringTokenizer.hasMoreTokens()){
            final String pathSegment = stringTokenizer.nextToken();
            mPathSegments.add(pathSegment);
        }
    }

    public Path(final String... pathSegments) {
        mPath = TextUtils.join("/", pathSegments);
        mPathSegments = Lists.newArrayList(pathSegments);

    }

    public int getPathSegmentCount(){
        return mPathSegments.size();
    }

    public String getMatcherPath() {
        return mPath;
    }

    public List<String> getPathSegments(final Object... objects) {
        final List<String> pathSegments = new ArrayList<String>();
        int objectIndex = 0;
        for (int pathSegmentIndex = 0; pathSegmentIndex < mPathSegments.size(); pathSegmentIndex++){
            final String pathSegment = mPathSegments.get(pathSegmentIndex);
            final boolean isNumericSegmentPath = pathSegment.equals("#");
            final boolean isStar = pathSegment.equals("*");
            if (isNumericSegmentPath){
                final Object object = objects[objectIndex++];
                final boolean isObjectNumeric = object instanceof Number;
                if (isObjectNumeric){
                    final Number number = (Number) object;
                    pathSegments.add(number.toString());
                } else {
                    final String message = String.format(NUMERIC_EXCEPTION, object);
                    throw new IllegalArgumentException(message);
                }
            } else if (isStar) {
;                final Object object = objects[objectIndex++];
                pathSegments.add(object.toString());
            } else {
                pathSegments.add(pathSegment);
            }
        }

        if (objectIndex != objects.length){
            throw new IllegalArgumentException(MATCH_COUNT_MESSAGE);
        }
        return pathSegments;
    }

    public String getPath(final Object... objects){
        final List<String> pathSegments = getPathSegments(objects);
        return TextUtils.join("/", pathSegments);
    }


    @Override
    public boolean equals(final Object object) {
        if (object == null || !(object instanceof Path)){
            return false;
        }

        final Path path = (Path) object;

        if (!mPath.equals(path.mPath)){
            return false;
        }

        if (mPathSegments.size() != path.mPathSegments.size()){
            return false;
        }

        for (int index = 0; index < mPathSegments.size(); index++){
            if (!mPathSegments.get(index).equals(path.mPathSegments.get(index))){
                return false;
            }
        }

        return true;
    }

    public boolean matches(final Uri uri) {
        if (uri == null){
            return false;
        }

        final List<String> pathSegments = uri.getPathSegments();

        if (mPathSegments.size() != pathSegments.size()){
            return false;
        }

        for (int index = 0; index < mPathSegments.size(); index++){
            final String incomingPathSegment = pathSegments.get(index);
            final String internalPathSegment = mPathSegments.get(index);
            if (internalPathSegment.equals("#")) {
                if (!isNumeric(incomingPathSegment)) {
                    return false;
                }
            } else if (!internalPathSegment.equals("*") && !internalPathSegment.equals(incomingPathSegment)){
                return false;
            }
        }

        return true;
    }

    private boolean isNumeric(final String string){
        try{
            Double.parseDouble(string);
            return true;
        } catch (final NumberFormatException numberFormatException) {
            return false;
        }
    }
}

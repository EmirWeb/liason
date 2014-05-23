package mobi.liason.loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class ForceLoadCursorLoader extends CursorLoader {

	private final ForceLoadContentObserver mForceLoadContentObserver;

	public ForceLoadCursorLoader(Context context) {
		this(context, null, null, null, null, null);
	}

	public ForceLoadCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mForceLoadContentObserver = new ForceLoadContentObserver();
	}

	public ForceLoadContentObserver getForceLoadContentObserver() {
		return mForceLoadContentObserver;
	}

}

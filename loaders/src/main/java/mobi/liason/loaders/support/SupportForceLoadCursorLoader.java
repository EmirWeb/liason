package mobi.liason.loaders.support;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Created by Emir Hasanbegovic on 18/04/14.
 */
public class SupportForceLoadCursorLoader extends CursorLoader {

	private final ForceLoadContentObserver mForceLoadContentObserver;

	public SupportForceLoadCursorLoader(Context context) {
		this(context, null, null, null, null, null);
	}

	public SupportForceLoadCursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		mForceLoadContentObserver = new ForceLoadContentObserver();
	}

	public ForceLoadContentObserver getForceLoadContentObserver() {
		return mForceLoadContentObserver;
	}

}

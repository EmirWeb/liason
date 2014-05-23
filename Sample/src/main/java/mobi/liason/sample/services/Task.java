package mobi.liason.sample.services;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import java.util.List;

import mobi.liason.mvvm.providers.Path;
import mobi.liason.sample.content.models.TaskState;
import mobi.liason.sample.content.models.TaskStateTable;
import mobi.liason.sample.utilities.UriUtilities;

/**
 * Created by Emir Hasanbegovic on 2014-05-20.
 */
public abstract class Task implements Runnable {
    private static final long STALE_DATA_THRESHOLD = 30 * 1000;
    private final Context mContext;
    private final Uri mUri;
    private JsonObject mJsonObject;
    private boolean mHasFailed;

    public Task(final Context context, final Uri uri) {
        mContext = context;
        mUri = uri;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void run() {
        final boolean shouldRunResult = shouldRunRequest();
        if (!shouldRunResult){
            return;
        }

        final boolean isRunning = setRunning();
        if (!isRunning) {
            return;
        }

        notifyTaskState();

        try {
            onPreExecuteTask(mContext);
            onExecuteTask(mContext);
            onPostExecuteTask(mContext);
        } catch (final Exception exception) {
            setFailure(true);
        }

        onComplete();
        notifyTaskState();
    }

    public boolean shouldRunRequest() {
        final String uriString = mUri.toString();
        final Path path = TaskStateTable.Paths.TASK_STATE;
        final Uri taskUri = UriUtilities.getUri(mContext, path, uriString);
        final ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(taskUri, null, null, null, null);

            final boolean forceTask = forceNetworkRequest(mUri);
            if (!forceTask) {
                final boolean needsUpdate = needsUpdate(cursor);
                return !needsUpdate && !forceTask;
            }

            return forceTask;
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
    }

    public void setFailure(final boolean hasFailed) {
        mHasFailed = hasFailed;
    }

    private void onComplete() {
        if (mHasFailed) {
            onFailure();
        } else {
            onSuccess();
        }
    }

    public void setJsonObject(final JsonObject jsonObject) {
        mJsonObject = jsonObject;
    }

    private void onFailure() {
        setState(TaskStateTable.State.FAIL);
    }

    private void onSuccess() {
        setState(TaskStateTable.State.SUCCESS);
    }

    private void setState(final String state) {
        final String uriString = mUri.toString();
        final Path path = TaskStateTable.Paths.TASK_STATE;
        final Uri taskStateUri = UriUtilities.getUri(mContext, path, uriString);

        final String selection = TaskStateTable.Columns.URI.getName() + "=?";
        final String[] selectionArguments = new String[]{uriString};

        final long time = System.currentTimeMillis();
        final TaskState taskState = new TaskState(uriString, state, time, mJsonObject);
        final ContentValues contentValues = TaskStateTable.getContentValues(taskState);

        final ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.update(taskStateUri, contentValues, selection, selectionArguments);
    }

    private boolean setRunning() {
        final String uriString = mUri.toString();
        final Path path = TaskStateTable.Paths.TASK_STATE;
        final Uri taskStateUri = UriUtilities.getUri(mContext, path, uriString);

        final long time = System.currentTimeMillis();
        final TaskState taskState = new TaskState(uriString, TaskStateTable.State.RUNNING, time, null);
        final ContentValues contentValues = TaskStateTable.getContentValues(taskState);

        final ContentResolver contentResolver = mContext.getContentResolver();
        final Uri uri = contentResolver.insert(taskStateUri, contentValues);

        return uri != null;
    }

    private boolean forceNetworkRequest(final Uri uri) {
        final String forceRequestString = uri.getQueryParameter(TaskStateTable.QueryParameters.FORCE_TASK);
        return forceRequestString != null && Boolean.parseBoolean(forceRequestString);
    }

    public boolean needsUpdate(final Cursor cursor) {
        if (cursor.moveToFirst()) {
            final int timeColumnIndex = cursor.getColumnIndex(TaskStateTable.Columns.TIME.getName());
            final long time = cursor.getLong(timeColumnIndex);
            final long duration = Math.abs(System.currentTimeMillis() - time);
            final long expirationTime = getUpdateTime();
            return duration > expirationTime;
        }

        return false;
    }

    public long getUpdateTime() {
        return STALE_DATA_THRESHOLD;
    }

    private void notifyTaskState(){
        final ContentResolver contentResolver = mContext.getContentResolver();
        final List<Uri> taskUris = getTaskUris();
        for (final Uri uri : taskUris){
            contentResolver.notifyChange(uri, null);
        }
    }

    public List<Uri> getTaskUris(){
        return Lists.newArrayList(mUri);
    }

    protected void onPreExecuteTask(final Context context) throws Exception {

    }

    protected void onPostExecuteTask(final Context context) throws Exception {

    }

    protected abstract void onExecuteTask(final Context context) throws Exception;

}

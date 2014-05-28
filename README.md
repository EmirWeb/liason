Liason
===============================

## Library that uses Loaders to implement MVVM

The following library automates the majority of the loader and MVVM flow to give developers easy access to the MVVM paradigm

## Requires
Android 4.0 +

# Getting Started

## Step 1: Set up dependency
Include the Parchment library in your pom.xml file as follows:
```xml
<dependency>
    <groupId>liason.mobi</groupId>
    <artifactId>task</artifactId>
    <version>0.0.1</version>
</dependency>
```

or, to your build.gradle as follows:

```java
dependencies {
    compile 'mobi.liason:task:0.0.1'
}
```

## Step 2: Set up Overrides

```java
public class SampleDatabaseHelper extends DatabaseHelper {

    private static final String DATA_BASE_NAME = "SampleDataBaseName";
    private static final int VERSION = 1;

    public SampleDatabaseHelper(Context context) {
        super(context, DATA_BASE_NAME, VERSION);
    }

    @Override
    public List<Content> getContent(final Context context) {
        final List<Content> contentList = new ArrayList<Content>();
        //TODO: add Content
        return contentList;
    }

    private static SampleDatabaseHelper sSampleDataBaseHelper;

    public static synchronized DatabaseHelper getDatabaseHelper(final Context context){
        if (sSampleDataBaseHelper == null){
            sSampleDataBaseHelper = new SampleDatabaseHelper(context);
        }
        return sSampleDataBaseHelper;
    }
}
```

```java
public class SampleProvider extends Provider {
    @Override
    public String getAuthority(Context context) {
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.<AUTHORITY_STRING_RESOURCE>);
        return authority;
    }

    @Override
    protected DatabaseHelper onCreateDatabaseHelper(Context context) {
        return SampleDatabaseHelper.getDatabaseHelper(context);
    }
}
```

```xml
<provider
    android:name="<PROVIDER_PACKAGE_PATH>"
    android:exported="false"
    android:authorities="@string/<AUTHORITY_STRING_RESOURCE>" />
```

```java
public class SampleTaskService extends TaskService {

    public static void startTask(final Context context, final Uri uri) {
        final String uriString = uri.toString();
        final Intent intent = new Intent(context, SampleTaskService.class);
        intent.putExtra(EXTRAS.URI, uriString);
        context.startService(intent);
    }

    public static void forceStartTask(final Context context, final Uri uri) {
        final Uri forcedUri = uri.buildUpon().appendQueryParameter(Task.QueryParameters.FORCE_TASK, Boolean.toString(true)).build();
        final String uriString = forcedUri.toString();
        final Intent intent = new Intent(context, SampleTaskService.class);
        intent.putExtra(EXTRAS.URI, uriString);
        context.startService(intent);
    }

    @Override
    public String getAuthority(final Context context) {
        final Resources resources = context.getResources();
        return resources.getString(R.string.<AUTHORITY_STRING_RESOURCE>);
    }

    @Override
    public Map<Path, Class> getPathTaskMap(Context context) {
        final Map<Path, Class> mPathClassMap = new HashMap<Path, Class>();
        //TODO: Map Paths to Tasks
        return mPathClassMap;
    }
}
```

```xml
<service
    android:name="<SERVICE_PACKAGE_PATH>"
    android:exported="false" />
```

```java
public class SampleUriUtilities extends UriUtilities {

    public static final String CONTENT_SCHEME = "content";

    public static Uri getUri(final Context context, final Path path, final Object... objects) {
        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.<AUTHORITY_STRING_RESOURCE>);
        return getUri(CONTENT_SCHEME, authority, path, objects);
    }
}
```

## Step 3: Set up Model(s)

```java
public class JsonModel {

    @SerializedName(Fields.FIELD_1)
    public Long mField1;

    @SerializedName(Fields.FIELD_2)
    public String mField2;

    public static class Fields {
        public static final String FIELD_1 = "field1";
        public static final String FIELD_2 = "field2";
    }
}
```

```java
public class SqlModel extends Model {

    public static final String TABLE_NAME = SqlModel.class.getSimpleName();

    public static ContentValues getContentValues(final JsonModel jsonModel) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.FIELD_1.getName(), jsonModel.mField1);
        contentValues.put(Columns.FIELD_2.getName(), jsonModel.mField2);
        return contentValues;
    }

    @Override
    public String getName(final Context context) {
        return TABLE_NAME;
    }

    @Override
    public List<Column> getColumns(final Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PATH);
    }

    public static class Columns {
        public static final ModelColumn FIELD_1 = new ModelColumn(TABLE_NAME, JsonModel.Fields.FIELD_1, ModelColumn.Type.integer);
        public static final ModelColumn FIELD_2 = new ModelColumn(TABLE_NAME, Product.Fields.FIELD_2, ModelColumn.Type.text);
        public static final Column[] COLUMNS = new Column[]{FIELD_1, FIELD_2};
    }

    public static class Paths {
        public static final Path PATH = new Path(TABLE_NAME);
    }

}
```


## Step 4: Set up Viewmodel(s)

```java
public class SqlViewModel extends ViewModel {

    public static final String VIEW_NAME = SqlViewModel.class.getSimpleName();

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    public List<Column> getColumns(Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    protected String getSelection(Context context) {
        return SqlTable.TABLE_NAME + " ORDER BY " + Columns.FIELD_1.getName();
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PATH);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return super.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static class Columns {
        public static final ViewModelColumn FIELD_1 = new ViewModelColumn(VIEW_NAME, SqlModel.Columns.Field_1);
        public static final Column[] COLUMNS = new Column[]{FIELD_1};
    }

    public static class Paths {
        public static final Path PATH = new Path(VIEW_NAME);
    }
}
```

## Step 5: Set up Bindings(s)

list_item_sample.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/list_item_sample_text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

</RelativeLayout>
```

# AdapterItemBinding
```java
public class SampleAdapterItemBinding extends AdapterItemBinding {
    public SampleAdapterItemBinding(final int layoutResourceId) {
        super(layoutResourceId);

        final Binding binding = new TextBinder(R.id.list_item_sample_text, SqlViewModel.Columns.FIELD_1);
        addBinding(binding);
    }
}
```

# AdapterBinding
```java
public class SampleAdapterBinding extends ActivityAdapterBinding{

    private static final int ID = IdCreator.getStaticId();

    public SampleAdapterBinding(final Activity activity, final int resourceId){
        super(activity, resourceId);

        final AdapterItemBinding adapterItemBinding = new SampleAdapterItemBinding(R.layout.list_item_sample.xml);
        addItemBinding(adapterItemBinding);

    }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, SqlViewModel.Paths.PATH);
    }

    @Override
    public int getId(final Context context) {
        return ID;
    }
}
```

## Step 6: Set up Activities

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/activity_adapter_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

```java
public class SampleActivity extends Activity implements AdapterView.OnItemClickListener {

    private ActivityBindingManager mActivityBindingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mActivityBindingManager = new ActivityBindingManager(this);
        final AdapterView adapterView = (AdapterView) findViewById(R.id.activity_adapter_view);
        adapterView.setOnItemClickListener(this);

        final AdapterBinding adapterBinding = new ProductsAdapterBinding(this, R.id.activity_adapter_view);
        mActivityBindingManager.addBindDefinition(adapterBinding);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStart(context);
    }

    @Override
    protected void onStop() {
        super.onStop();
        final Context context = getApplicationContext();
        mActivityBindingManager.onStop(context);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        final long field1 = (Long) SqlViewModel.Columns.FIELD_1.getValue(cursor);
        OtherActivityActivity.startActivity(this, field1);
    }

}
```



## Step 7: Set up Task(s)

```java
public class NetworkResponse {

    @SerializedName(Fields.RESULT)
    public ArrayList<SqlModel> mSqlModel;

    public static class Fields {
        public static final String RESULT = "result";
    }
}
```

```java
public class NetworkTask extends Task {

    private static final String SCHEME = "HTTP";
    private static final String AUTHORITY = "api.com";
    public static final String path = "path";

    public ProductsTask(final Context context, final String authorty, final Uri uri) {
        super(context, authorty, uri);
    }

    @Override
    protected void onExecuteTask(final Context context) throws Exception {

        final Uri networkUri = SampleUriUtilities.getUri(SCHEME, AUTHORITY, Paths.Path);
        final String url = networkUri.toString();
        final NetworkResponse networkResponse = TaskUtilities.getModel(url, NetworkResponse.class);

        // DELETE OLD CONTENT
        final Uri modelUri = SampleUriUtilities.getUri(context, SqlModel.Paths.PATH);
        final ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<ContentProviderOperation>();
        final ContentProviderOperation deleteContentProviderOperation = ContentProviderOperation.newDelete(modelUri).build();
        contentProviderOperations.add(deleteContentProviderOperation);

        final ArrayList<JsonModel> jsonModels = networkResponse.mJsonModel;
        for (final JsonModel jsonModel : jsonModels) {
            final ContentValues contentValues = SqlModel.getContentValues(jsonModel);
            final ContentProviderOperation insertContentProviderOperation = ContentProviderOperation.newInsert(modelUri).withValues(contentValues).build();
            contentProviderOperations.add(insertContentProviderOperation);
        }

        final Resources resources = context.getResources();
        final String authority = resources.getString(R.string.<AUTHORITY_STRING_RESOURCE>);

        final Uri uri = getUri();
        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.applyBatch(authority, contentProviderOperations);
        contentResolver.notifyChange(uri, null);

        final Uri sqlViewModelUri = SampleUriUtilities.getUri(context, SqlViewModel.Paths.PATH);
        contentResolver.notifyChange(sqlViewModelUri, null);
    }

    public static class Paths {
        public static final Path PATH = new Path(NetworkTask.PRODUCTS);
    }
}
```


## Set up 8: TaskViewmodel
```java
public class NetowrkTaskStateViewModel extends ViewModel {

    public static final String VIEW_NAME = NetowrkTaskStateViewModel.class.getSimpleName();

    @Override
    public String getName(final Context context) {
        return VIEW_NAME;
    }

    @Override
    public List<Column> getColumns(Context context) {
        return Arrays.asList(Columns.COLUMNS);
    }

    @Override
    protected String getSelection(Context context) {
        return TaskStateTable.TABLE_NAME + " WHERE " + TaskStateTable.TABLE_NAME + "." + TaskStateTable.Columns.URI.getName() + " LIKE '%" + NetowrkTaskStateViewModel.Paths.PATH.getPath() + "'";
    }

    @Override
    public List<Path> getPaths(Context context) {
        return Lists.newArrayList(Paths.PRODUCTS_TASK_STATE);
    }

    @Override
    public Cursor query(Context context, SQLiteDatabase sqLiteDatabase, Path path, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SampleTaskService.startTask(context, uri);
        return super.query(context, sqLiteDatabase, path, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static class Columns {

        public static final ViewModelColumn URI = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.URI);
        public static final ViewModelColumn STATE = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.STATE);
        public static final ViewModelColumn JSON = new ViewModelColumn(VIEW_NAME, TaskStateTable.Columns.JSON);

        public static final String DATA_SELECTION = "CASE" +
                " WHEN " +
                "(SELECT COUNT(*) FROM " + SqlModel.TABLE_NAME + ") > 0" +
                " THEN " +
                "'" + Boolean.toString(true) + "'" +
                " ELSE " +
                "'" + Boolean.toString(false) + "'" +
                " END";
        public static final String PROGRESS_BAR_SELECTION = "CASE" +
                " WHEN " +
                Columns.STATE.getName() + "='" + SqlModel.State.RUNNING + "'" +
                " THEN " +
                "'" + Boolean.toString(true) + "'" +
                " ELSE " +
                "'" + Boolean.toString(false) + "'" +
                " END ";

        public static final ViewModelColumn IS_PROGRESS_BAR_VISIBLE = new ViewModelColumn(VIEW_NAME, "isProgressBarVisible", PROGRESS_BAR_SELECTION, Column.Type.text);
        public static final ViewModelColumn IS_DATA_VISIBLE = new ViewModelColumn(VIEW_NAME, "isDataVisible", DATA_SELECTION, Column.Type.text);
        public static final Column[] COLUMNS = new Column[]{URI, STATE, JSON, IS_PROGRESS_BAR_VISIBLE, IS_DATA_VISIBLE};
    }

    public static class Paths {
        public static final Path PRODUCTS_TASK_STATE = ProductsTask.Paths.PRODUCTS;
    }

}
```

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ListView
        android:id="@+id/activity_adapter_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

     <ProgressBar
            android:id="@+id/activity_progress"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

</FrameLayout>
```


```java
public class NetworkTaskStateBinding extends ActivityItemBinding {

    private static final int ID = IdCreator.getStaticId();

    public NetworkTaskStateBinding(final Activity activity) {
        super(activity);

        final VisibilityBinder dataVisibilityBinder = new VisibilityBinder(R.id.activity_adapter_view, ProductsTaskStateViewModel.Columns.IS_DATA_VISIBLE);
        addBinding(dataVisibilityBinder);

        final PulltoRefreshBinder pulltoRefreshBinder = new PulltoRefreshBinder(R.id.activity_progress, ProductsTaskStateViewModel.Columns.IS_PROGRESS_BAR_VISIBLE);
        addBinding(pulltoRefreshBinder);
  }

    @Override
    public Uri getUri(final Context context) {
        return SampleUriUtilities.getUri(context, ProductsTaskStateViewModel.Paths.PRODUCTS_TASK_STATE);
    }

    @Override
    public int getId(final Context context) {
        return ID;
    }

}
```

### License:

Copyright 2014 Emir Hasanbegovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[1]: https://www.pivotaltracker.com/s/projects/1000984#

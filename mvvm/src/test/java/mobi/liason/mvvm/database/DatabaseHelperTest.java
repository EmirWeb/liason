package mobi.liason.mvvm.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import java.util.List;

import mobi.liason.mvvm.RobolectricTestRunnerWithInjection;
import mobi.liason.mvvm.providers.Path;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunnerWithInjection.class)
public class DatabaseHelperTest {

    @Mock SQLiteDatabase mSqLiteDatabase;
    final Context mContext = Robolectric.getShadowApplication().getApplicationContext();

    @Test
    public void onCreateWithOneContentWritesSQLCorrectly(){
        final DatabaseHelper databaseHelper = new DatabaseHelper(mContext, "TEST_DB", 1){
            @Override
            public List<Content> getContent(Context context) {
                final Content mockContent = new MockContent("EMIR");
                return Lists.newArrayList(mockContent);
            }
        };

        databaseHelper.onCreate(mSqLiteDatabase);
        verify(mSqLiteDatabase).execSQL("CREATE TABLE EMIR;");
    }

    @Test
    public void onCreateWithMultipleContentWritesSQLCorrectly(){
        final DatabaseHelper databaseHelper = new DatabaseHelper(mContext, "TEST_DB", 1){
            @Override
            public List<Content> getContent(Context context) {
                final Content mockContent = new MockContent("EMIR");
                final Content mockContent1 = new MockContent("EMIR1");
                return Lists.newArrayList(mockContent, mockContent1);
            }
        };

        databaseHelper.onCreate(mSqLiteDatabase);
        verify(mSqLiteDatabase).execSQL("CREATE TABLE EMIR;");
        verify(mSqLiteDatabase).execSQL("CREATE TABLE EMIR1;");

    }

    @Test
    public void onUpgradeWithMultipleContentWritesSQLCorrectly(){
        final DatabaseHelper databaseHelper = new DatabaseHelper(mContext, "TEST_DB", 1){
            @Override
            public List<Content> getContent(Context context) {
                final Content mockContent = new MockContent("EMIR");
                final Content mockContent1 = new MockContent("EMIR1");
                return Lists.newArrayList(mockContent, mockContent1);
            }
        };

        databaseHelper.onUpgrade(mSqLiteDatabase, 0 , 1);
        verify(mSqLiteDatabase).execSQL("DROP TABLE EMIR;");
        verify(mSqLiteDatabase).execSQL("CREATE TABLE EMIR;");
        verify(mSqLiteDatabase).execSQL("DROP TABLE EMIR1;");
        verify(mSqLiteDatabase).execSQL("CREATE TABLE EMIR1;");

    }


    public static class MockContent extends Content {

        public String mName;

        public MockContent (final String name){
            mName = name;
        }

        @Override
        public int getVersion(final Context context) {
            return 0;
        }

        @Override
        public String getName(final Context context) {
            return mName;
        }

        @Override
        public String getCreate(final Context context) {
            return "CREATE TABLE " + mName + ";";
        }

        @Override
        public String getDrop(final Context context) {
            return "DROP TABLE " + mName + ";";
        }

        @Override
        public List<Path> getPaths(final Context context) {
            return Lists.newArrayList(new Path("PATH1/PATH2"));
        }
    }
}
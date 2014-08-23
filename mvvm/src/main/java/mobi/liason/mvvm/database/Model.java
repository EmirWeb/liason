package mobi.liason.mvvm.database;

import android.content.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mobi.liason.loaders.Content;
import mobi.liason.loaders.Path;
import mobi.liason.mvvm.database.annotations.ColumnDefinition;
import mobi.liason.mvvm.database.annotations.ColumnDefinitions;
import mobi.liason.mvvm.database.annotations.PrimaryKey;
import mobi.liason.mvvm.database.annotations.PathDefinition;
import mobi.liason.mvvm.database.annotations.PathDefinitions;
import mobi.liason.mvvm.database.annotations.Unique;

/**
 * Created by Emir Hasanbegovic on 13/05/14.
 */
public abstract class Model extends Content {

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS %s ( %s );";
    private static final String DROP = "DROP TABLE IF EXISTS %s;";
    private static final int VERSION = -1;
    private static final String UNIQUE = ", UNIQUE ( %s ) ON CONFLICT REPLACE";
    private static final String PRIMARY_KEYS = ", PRIMARY KEY ( %s )";
    private final List<Column> mColumns = new ArrayList<Column>();
    private final Set<Column> mUniqueColumns = new HashSet<Column>();
    private final List<Path> mPaths = new ArrayList<Path>();
    private final Set<Column> mPrimaryKeys = new HashSet<Column>();


    public Model(){
        initializeAnnotations();
    }

    public void initializeAnnotations() {
        final Class<? extends Model> klass = this.getClass();
        initializeAnnotations(klass);
    }

    public void initializeAnnotations(final Class klass) {
        final Class<? extends Model> columnClass = klass;
        final Class<?>[] declaredClasses = columnClass.getDeclaredClasses();
        for (final Class<?> declaredClass : declaredClasses) {
            final Annotation[] declaredClassAnnotations = declaredClass.getDeclaredAnnotations();
            for (final Annotation declaredClassAnnotation : declaredClassAnnotations) {
                if (declaredClassAnnotation instanceof ColumnDefinitions) {
                    initializeColumnDefinitions(declaredClass);
                }else if (declaredClassAnnotation instanceof PathDefinitions) {
                    initializePathDefinitions(declaredClass);
                }
            }
        }
    }

    public void initializePathDefinitions(Class<?> declaredClass) {
        final Field[] declaredFields = declaredClass.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            final Annotation[] declaredFieldAnnotations = declaredField.getDeclaredAnnotations();
            for (final Annotation declaredFieldAnnotation : declaredFieldAnnotations) {
                if (declaredFieldAnnotation instanceof PathDefinition) {
                    try {
                        final Path path = (Path) declaredField.get(null);
                        mPaths.add(path);
                    } catch (IllegalAccessException e) {
                    }
                }
            }
        }
    }

    public void initializeColumnDefinitions(Class<?> declaredClass) {
        final Field[] declaredFields = declaredClass.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            final Annotation[] declaredFieldAnnotations = declaredField.getDeclaredAnnotations();
            for (final Annotation declaredFieldAnnotation : declaredFieldAnnotations) {
                if (declaredFieldAnnotation instanceof PrimaryKey) {
                    try {
                        final Column column = (Column) declaredField.get(null);
                        mPrimaryKeys.add(column);
                    } catch (IllegalAccessException e) {
                    }
                } else if (declaredFieldAnnotation instanceof ColumnDefinition) {
                    try {
                        final Column column = (Column) declaredField.get(null);
                        mColumns.add(column);
                    } catch (IllegalAccessException e) {
                    }
                } else if (declaredFieldAnnotation instanceof Unique) {
                    try {
                        final Column column = (Column) declaredField.get(null);
                        mUniqueColumns.add(column);
                    } catch (IllegalAccessException e) {
                    }
                }
            }
        }
    }

    @Override
    public String getCreate(final Context context) {
        final String name = getName(context);
        final List<Column> columns = getColumns(context);
        final Set<Column> uniqueColumns = getUniqueColumns(context);
        final Set<Column> primaryKeyColumns = getPrimaryKeyColumns(context);
        final String createColumns = createColumns(columns, uniqueColumns, primaryKeyColumns);
        final String create = String.format(CREATE, name, createColumns);
        return create;
    }

    @Override
    public String getDrop(final Context context) {
        final String name = getName(context);
        final String drop = String.format(DROP, name);
        return drop;
    }

    public Set<Column> getUniqueColumns(final Context context) {
        return mUniqueColumns;
    }

    @Override
    public int getVersion(final Context context) {
        return VERSION;
    }

    public List<Column> getColumns(Context context) {
        return mColumns;
    }

    public static String createColumns(final List<Column> columns, final Set<Column> uniqueColumns, final Set<Column> primaryKeyColumns) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int size = columns.size();
        for (int index = 0; index < size; index++ ){
            final Column modelColumn = columns.get(index);
            final String columnName = modelColumn.getName();
            final ModelColumn.Type type = modelColumn.getType();
            final String typeString = ModelColumn.Type.getSqlType(type);
            stringBuilder.append(columnName);
            stringBuilder.append(' ');
            stringBuilder.append(typeString);

            if (index != size -1) {
                stringBuilder.append(", ");
            }
        }
        final String uniqueLine = getUniqueLine(uniqueColumns);
        stringBuilder.append(uniqueLine);

        final String primaryKeyLine = getPrimaryKeyLine(primaryKeyColumns);
        stringBuilder.append(primaryKeyLine);


        return stringBuilder.toString();

    }

    private static String getUniqueLine(final Set<Column> uniqueColumns){
        if (uniqueColumns == null || uniqueColumns.isEmpty()){
            return "";
        }

        return String.format(UNIQUE, getCommaSeparatedColumns(uniqueColumns));
    }

    private static String getPrimaryKeyLine(final Set<Column> primaryKeyColumns){
        if (primaryKeyColumns == null || primaryKeyColumns.isEmpty()){
            return "";
        }

        return String.format(PRIMARY_KEYS, getCommaSeparatedColumns(primaryKeyColumns));
    }

    private static String getCommaSeparatedColumns(final Set<Column> columns) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int size = columns.size();
        int index = 0;
        for(final Column modelColumn : columns){
            final String columnName = modelColumn.getName();
            stringBuilder.append(columnName);

            if (index != size -1) {
                stringBuilder.append(", ");
            }
            index++;
        }
        return stringBuilder.toString();
    }


    @Override
    public List<Path> getPaths(Context context) {
        return mPaths;
    }

    public Set<Column> getPrimaryKeyColumns(final Context context){
        return mPrimaryKeys;
    }
}

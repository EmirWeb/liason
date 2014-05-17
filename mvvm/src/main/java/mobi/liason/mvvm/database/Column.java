package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class Column {

    private final String mName;
    private final Type mType;
    private final String mStringType;
    private final String mSqlName;

    public Column(final String sqlName, final String name, final Type type, final String stringType) {
        mSqlName = sqlName;
        mName = name;
        mType = type;
        mStringType = stringType;
    }

    public Column(final String tableName, final String name, final Type type) {
        mSqlName = tableName;
        mName = name;
        mType = type;
        mStringType = Type.getSqlType(type);
    }

    public String getName() {
        return mName;
    }

    public Type getType() {
        return mType;
    }

    public String getStringType() {
        return mStringType;
    }

    public String getColumnLine() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(' ');
        stringBuilder.append(mName);
        stringBuilder.append(' ');
        stringBuilder.append(mStringType);
        stringBuilder.append(' ');
        return stringBuilder.toString();
    }

    public String getSqlName() {
        return mSqlName;
    }

    public static enum Type {
        text, integer, blob, real;

        public static String getSqlType(final Type type){
            switch (type){
                case blob:
                    return "BLOB";
                case integer:
                    return "INTEGER";
                case real:
                    return "REAL";
                case text:
                default:
                    return "TEXT";
            }
        }
    }
}

package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ViewModelColumn extends Column {

    private final Column mColumn;
    private final String mCustomSelection;

    public ViewModelColumn(final String viewName, final String name, final Type type, final String stringType) {
        super(viewName, name, type, stringType);
        mColumn = null;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final Type type) {
        super(viewName, name, type);
        mColumn = null;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final Column column) {
        super(viewName, name, column.getType());
        mColumn = column;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final Column column) {
        super(viewName, column.getName(), column.getType());
        mColumn = column;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final String customSelection, final Type type) {
        super(viewName, name, type);
        mColumn = null;
        mCustomSelection = customSelection;
    }

    @Override
    public String getColumnLine() {
        if (mColumn != null) {
            final String modelTableName = mColumn.getSqlName();
            final String modelColumnName = mColumn.getName();


            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(modelTableName);
            stringBuilder.append('.');
            stringBuilder.append(modelColumnName);

            final String source = stringBuilder.toString();
            return getColumnLine(source);
        } else if (mCustomSelection != null) {
            return getColumnLine(mCustomSelection);
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final String name = getName();
        stringBuilder.append(name);
        return stringBuilder.toString();

    }

    private String getColumnLine(final String source) {
        final String name = getName();
        final StringBuilder stringBuilder = new StringBuilder(source);
        stringBuilder.append(" AS ");
        stringBuilder.append(name);
        return stringBuilder.toString();
    }
}

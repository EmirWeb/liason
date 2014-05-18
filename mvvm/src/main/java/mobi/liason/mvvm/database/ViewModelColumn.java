package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ViewModelColumn extends Column {

    private final ModelColumn mModelColumn;
    private final String mCustomSelection;

    public ViewModelColumn(final String viewName, final String name, final Type type, final String stringType) {
        super(viewName, name, type, stringType);
        mModelColumn = null;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final Type type) {
        super(viewName, name, type);
        mModelColumn = null;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final ModelColumn modelColumn) {
        super(viewName, name, modelColumn.getType());
        mModelColumn = modelColumn;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final ModelColumn modelColumn) {
        super(viewName, modelColumn.getName(), modelColumn.getType());
        mModelColumn = modelColumn;
        mCustomSelection = null;
    }

    public ViewModelColumn(final String viewName, final String name, final String customSelection, final Type type) {
        super(viewName, name, type);
        mModelColumn = null;
        mCustomSelection = customSelection;
    }

    @Override
    public String getColumnLine() {
        if (mModelColumn != null) {
            final String modelTableName = mModelColumn.getSqlName();
            final String modelColumnName = mModelColumn.getName();


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

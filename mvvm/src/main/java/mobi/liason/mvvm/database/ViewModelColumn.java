package mobi.liason.mvvm.database;

/**
 * Created by Emir Hasanbegovic on 12/05/14.
 */
public class ViewModelColumn extends ModelColumn{

    private ModelColumn mModelColumn;

    public ViewModelColumn(final String viewName, final String name, final Type type, final String stringType) {
        super(viewName, name, type, stringType);
    }

    public ViewModelColumn(final String viewName, final String name, final Type type) {
        super(viewName, name, type);
    }

    public ViewModelColumn(final String viewName, final String name, final ModelColumn modelColumn) {
        super(viewName, name, modelColumn.getType());
        mModelColumn = modelColumn;
    }

    @Override
    public String getColumnLine() {
        if (mModelColumn != null){
            final String modelTableName = mModelColumn.getSqlName();
            final String modelColumnName = mModelColumn.getName();
            final String name = getName();

            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(modelTableName);
            stringBuilder.append('.');
            stringBuilder.append(modelColumnName);
            stringBuilder.append(" AS ");
            stringBuilder.append(name);
            return stringBuilder.toString();
        }
        return super.getColumnLine();
    }
}

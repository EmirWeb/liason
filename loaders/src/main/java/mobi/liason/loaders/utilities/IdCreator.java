package mobi.liason.loaders.utilities;

/**
 * Created by Emir Hasanbegovic on 28/04/14.
 */
public class IdCreator {

    private static IdCreator ID_CREATOR = new IdCreator();

    private final int ID_LIMIT = Integer.MAX_VALUE  - 1;
    private int sId;

    public synchronized int getId() {
        return (++sId) % ID_LIMIT;
    }

    public static int getStaticId(){
        return ID_CREATOR.getId();
    }
}

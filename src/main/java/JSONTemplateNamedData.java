/**
 * Created by hakim on 14-Jan-16.
 */
public abstract class JSONTemplateNamedData extends JSONTemplateData {
    protected String key;
    protected String path;
    protected NamedTemplateDataListener listener;

    public JSONTemplateNamedData(NamedTemplateDataListener listener, int level) {
        super(level);
        this.listener = listener;
    }

    public JSONTemplateNamedData(NamedTemplateDataListener listener, int level, String key) {
        super(level);
        this.key = key;
        this.listener = listener;
    }
}

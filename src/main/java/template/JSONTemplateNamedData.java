package template;

/**
 * Created by hakim on 14-Jan-16.
 */
public abstract class JSONTemplateNamedData extends JSONTemplateData {
    protected String key;
    protected String path;
    protected NamedTemplateDataListener listener;

    public JSONTemplateNamedData(boolean required, NamedTemplateDataListener listener, int level) {
        super(required, level);
        this.listener = listener;
    }

    public JSONTemplateNamedData(boolean required, NamedTemplateDataListener listener, int level, String key) {
        super(required, level);
        this.key = key;
        this.listener = listener;
    }
}

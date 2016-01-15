/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateSimpleElement extends JSONTemplateData {
    private JSONElementType type;
    public JSONTemplateSimpleElement(int level, JSONElementType type) {
        super(level);
        this.type = type;
    }

    @Override
    public String toTemplateString() {
        return null;
    }
}

/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateElement  extends JSONTemplateData{
    private JSONElementType type;
    private String key;
    public JSONTemplateElement(int level, String key, JSONElementType type) {
        super(level);
        this.type = type;
        this.key = key;
    }

    @Override
    public String toTemplateString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(indent()).append("<span style=\"color:red\">\"").append(key).append("\"</span>:");

        switch (type) {
            case STRING:
                stringBuilder.append("\"{{{" + key + "}}}\"");
                break;
            case INTEGER:
            case BOOLEAN:
                stringBuilder.append("{{{" + key + "}}}");
                break;
        }

        return stringBuilder.toString();
    }
}
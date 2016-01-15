/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateElement  extends JSONTemplateData{
    private JSONElementType type;
    String key;

    public JSONTemplateElement(boolean required, int level, String key, JSONElementType type) {
        super(required, level);
        this.type = type;
        this.key = key;
    }

    @Override
    public String toTemplateString() {
        StringBuilder stringBuilder = new StringBuilder();

        //stringBuilder.append(indent());
        stringBuilder.append("<span style{{=}}\"color:red\">\"").append(key).append("\"</span>:");

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

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateArray extends JSONTemplateNamedData {
    private JSONTemplateData items;
    private JSONObject swaggerDescription;

    public JSONTemplateArray(NamedTemplateDataListener listener, String parentPath, int level, String key, JSONObject swaggerDescription) {
        super(listener, level, key);
        this.path = parentPath.concat("/").concat(key);
        this.swaggerDescription = swaggerDescription;
        generateTemplate();
        listener.onCreate(this);
        //System.out.println(this.toTemplateString());
    }

    public void generateTemplate() {
        if(swaggerDescription.containsKey("$ref")) {
            String objectKey = (String) swaggerDescription.get("$ref");
            items = new JSONTemplateObject(listener, path, level+1,key, TemplateGenerator.getSwaggerObjectDescription(objectKey));
        }
        else {
            //not sure about arrays. can an array contain arrays? How does Swagger represent that?

            items = new JSONTemplateSimpleElement(level+1, JSONElementType.valueOf(swaggerDescription.get("type").toString().toUpperCase()));
        }
    }

    @Override
    public String toTemplateString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(indent()).append("<span style=\"color:red\">\"").append(key).append("\"</span>: [\n");
        stringBuilder.append(items.indent()).append("{{#if:{{{"+ (1) +"|}}}|{{{" + (1) +"}}}|}}");

        for(int i=2; i<15; i++) {
            stringBuilder.append("{{#if:{{{").append(i).append("|}}}|,\n").append(items.indent()).append("{{{").append(i).append("}}}|}}");
        }
        stringBuilder.append(indent()).append("\n").append(indent()).append("]");

        return stringBuilder.toString();
    }
}

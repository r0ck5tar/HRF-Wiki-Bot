import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateObject extends JSONTemplateNamedData{
    private List<JSONTemplateData> properties = new ArrayList<>();
    private JSONObject swaggerDescription;

    public JSONTemplateObject(NamedTemplateDataListener listener, String path, int level, JSONObject swaggerDescription) {
        super(listener, level);
        this.path = path;
        this.swaggerDescription = swaggerDescription;
        generatePropertyTemplates();
        listener.onCreate(this);
        //System.out.println(this.toTemplateString());
    }

    public JSONTemplateObject(NamedTemplateDataListener listener, String parentPath, int level, String key, JSONObject swaggerDescription) {
        super(listener, level);
        this.swaggerDescription = swaggerDescription;
        this.key = key;
        this.path = parentPath.concat("/").concat(key);
        generatePropertyTemplates();
        listener.onCreate(this);
    }

    public void generatePropertyTemplates() {
        Set<String> requiredPropertyKeys = new HashSet<>();
        if(swaggerDescription.containsKey("required")) {
            for (Iterator requiredIt = ((JSONArray) swaggerDescription.get("required")).iterator(); requiredIt.hasNext();) {
                requiredPropertyKeys.add(requiredIt.next().toString());
            }
        }

        JSONObject propertiesDescription = (JSONObject) swaggerDescription.get("properties");
        for(Object propertyKey: propertiesDescription.keySet()){
            JSONObject property = (JSONObject) propertiesDescription.get(propertyKey);
            if(property.containsKey("$ref")) {
                String objectKey = (String) property.get("$ref");
                properties.add(new JSONTemplateObject(listener, path, level+1,(String)propertyKey, TemplateGenerator.getSwaggerObjectDescription(objectKey)));
            }
            else {
                String type = property.get("type").toString();
                switch (type) {
                    case "array":
                        properties.add(new JSONTemplateArray(listener, path, level+1, (String)propertyKey, (JSONObject) property.get("items")));
                        break;
                    case "Amount":
                        properties.add(new JSONTemplateObject(listener, path,level+1, (String)propertyKey, TemplateGenerator.getSwaggerObjectDescription("Amount")));
                        break;
                    default:
                        properties.add(new JSONTemplateElement(level+1, (String)propertyKey, JSONElementType.valueOf(type.toUpperCase())));
                        break;
                }
            }
        }
    }

    @Override
    public String toTemplateString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(indent()).append(key == null? "{\n" : "<span style=\"color:red\">\"" + key + "\"</span>: {\n");

        if(properties.size()>0) {
            JSONTemplateData firstProperty = properties.get(0);
            if (firstProperty instanceof JSONTemplateObject) {
                indent();
                stringBuilder.append(firstProperty.indent()).append("{{{").append(((JSONTemplateObject) firstProperty).key).append("|{{")
                        .append(((JSONTemplateObject) firstProperty).path).append("}}}}}");
            }
            else{
                stringBuilder.append(firstProperty.toTemplateString());
            }
        }

        for(int i = 1; i < properties.size(); i++) {
            JSONTemplateData property = properties.get(i);
            stringBuilder.append(",\n");

            if (property instanceof JSONTemplateObject) {
                stringBuilder.append(property.indent()).append("{{{").append(((JSONTemplateObject) property).key).append("|{{")
                        .append(((JSONTemplateObject) property).path).append("}}}}}");
            }
            else{
                stringBuilder.append(property.toTemplateString());
            }
        }

        stringBuilder.append("\n").append(indent()).append("}");

        return stringBuilder.toString();
    }
}

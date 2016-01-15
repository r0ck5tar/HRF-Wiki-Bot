import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by hakim on 14-Jan-16.
 */
public class JSONTemplateObject extends JSONTemplateNamedData{
    private List<JSONTemplateData> properties = new ArrayList<>();
    private JSONObject swaggerDescription;
    private StringBuilder stringBuilder = new StringBuilder();

    public JSONTemplateObject(boolean required, NamedTemplateDataListener listener, String path, int level, JSONObject swaggerDescription) {
        super(required, listener, level);
        this.path = path;
        this.swaggerDescription = swaggerDescription;
        generatePropertyTemplates();
        listener.onCreate(this);
        //System.out.println(this.toTemplateString());
    }

    public JSONTemplateObject(boolean required, NamedTemplateDataListener listener, String parentPath, int level, String key, JSONObject swaggerDescription) {
        super(required, listener, level);
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
        for(Object pk: propertiesDescription.keySet()){
            String propertyKey = (String) pk;
            JSONObject property = (JSONObject) propertiesDescription.get(propertyKey);
            if(property.containsKey("$ref")) {
                String objectKey = (String) property.get("$ref");
                properties.add(new JSONTemplateObject(requiredPropertyKeys.contains(propertyKey), listener, path, level+1,propertyKey, TemplateGenerator.getSwaggerObjectDescription(objectKey)));
            }
            else {
                String type = property.get("type").toString();
                switch (type) {
                    case "array":
                        properties.add(new JSONTemplateArray(requiredPropertyKeys.contains(propertyKey), listener, path, level+1, propertyKey, (JSONObject) property.get("items")));
                        break;
                    case "Amount":
                        properties.add(new JSONTemplateObject(requiredPropertyKeys.contains(propertyKey), listener, path,level+1, propertyKey, TemplateGenerator.getSwaggerObjectDescription("Amount")));
                        break;
                    default:
                        properties.add(new JSONTemplateElement(requiredPropertyKeys.contains(propertyKey), level+1, propertyKey, JSONElementType.valueOf(type.toUpperCase())));
                        break;
                }
            }
        }
    }

    @Override
    public String toTemplateString() {

        stringBuilder.append(indent()).append(key == null? "{\n" : "<span style{{=}}\"color:red\">\"" + key + "\"</span>: {\n");

        if(properties.size()>0) {
            JSONTemplateData firstProperty = properties.get(0);
            if (firstProperty instanceof JSONTemplateObject) {
                indent();
                if(!firstProperty.required) {
                    stringBuilder.append(firstProperty.indent())
                            .append("{{#if:{{{").append(((JSONTemplateObject) firstProperty).key).append("|}}}|{{{")
                            .append(((JSONTemplateObject) firstProperty).key).append("}}}}}");
                }
                else {
                    stringBuilder.append(firstProperty.indent()).append("{{{").append(((JSONTemplateObject) firstProperty).key).append("|{{")
                            .append(((JSONTemplateObject) firstProperty).path).append("}}}}}");
                }
            }
            else{
                stringBuilder.append(firstProperty.indent()).append(firstProperty.toTemplateString());
            }
        }

        for(int i = 1; i < properties.size(); i++) {
            JSONTemplateData property = properties.get(i);

            if (property instanceof JSONTemplateNamedData) {
                if(!property.required) {
                    stringBuilder.append("{{#if:{{{").append(((JSONTemplateNamedData) property).key).append("|}}}|<code>");
                    stringBuilder.append("\n").append(property.indent()).append("</code>");
                    stringBuilder.append("{{{").append(((JSONTemplateNamedData) property).key).append("}}}");
                    comma(i);
                    closeIf(property);
                }
                else{
                    stringBuilder.append("\n").append(property.indent());
                    stringBuilder.append("{{{").append(((JSONTemplateNamedData) property).key).append("|{{");
                    stringBuilder.append(((JSONTemplateNamedData) property).path).append("}}");
                    comma(i);
                    stringBuilder.append("}}}");
                }
            }
            else{
                if(!property.required) {
                    stringBuilder.append("{{#if:{{{").append(((JSONTemplateElement) property).key).append("|}}}|<code>");
                    stringBuilder.append("\n").append(property.indent()).append("</code>").append(property.toTemplateString());
                    closeIf(property);
                }
                else{
                    stringBuilder.append("\n").append(property.indent()).append(property.toTemplateString());
                }
                comma(i);
            }
        }

        stringBuilder.append("\n").append(indent()).append(" }");

        return stringBuilder.toString();
    }

    private void comma(int i) {
        if (i < properties.size()-1) {
            stringBuilder.append(",");
        }
    }

    private void closeIf(JSONTemplateData property){
        stringBuilder.append("}}");
    }
}

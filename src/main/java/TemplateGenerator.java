import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by hakim on 14-Jan-16.
 */
public class TemplateGenerator implements NamedTemplateDataListener {
    private Map<String, JSONTemplateObject> jsonResponseStructures = new HashMap<>();
    private Set<JSONTemplateNamedData> objectStructures = new HashSet<>();
    private static JSONObject models;
    private static TemplateGenerator instance;

    private TemplateGenerator(Set<String> responseStructureNames) {
        for(String responseStructureName: responseStructureNames) {
            jsonResponseStructures.put(responseStructureName, new JSONTemplateObject(this, "HRF/" + responseStructureName, 0, (JSONObject) models.get(responseStructureName)));
        }
    }

    public static void initializeWithSwaggerJson(JSONObject swaggerJson) {
        Set<String> responseStructureNames = new HashSet<>();
        JSONArray apis = (JSONArray) swaggerJson.get("apis");
        for(Iterator apiIt = apis.iterator(); apiIt.hasNext();) {
            JSONArray operations = (JSONArray) ((JSONObject) apiIt.next()).get("operations");
            for(Iterator operationIt = operations.iterator(); operationIt.hasNext();) {
                String responseStructureName = (String) ((JSONObject) operationIt.next()).get("type");
                responseStructureNames.add(responseStructureName);
            }
        }
        responseStructureNames.remove("void");

        models = (JSONObject) swaggerJson.get("models");
        instance = new TemplateGenerator(responseStructureNames);
    }

    public static JSONObject getSwaggerObjectDescription(String ref) {
        return (JSONObject) instance.models.get(ref);
    }

    public static TemplateGenerator getInstance(){
        return instance;
    }

    public Map<String, JSONTemplateObject> getJsonResponseStructures() {
        return jsonResponseStructures;
    }

    @Override
    public void onCreate(JSONTemplateNamedData jsonTemplateNamedData) {
        objectStructures.add(jsonTemplateNamedData);
    }

    public static void printAllTemplates() {
        for(JSONTemplateNamedData objectStructure: instance.objectStructures ) {
            System.out.println(objectStructure.path + "\n" + objectStructure.toTemplateString() + "\n\n");
        }
    }
}

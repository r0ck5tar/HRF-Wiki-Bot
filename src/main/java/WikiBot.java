import org.json.simple.JSONObject;
import template.TemplateGenerator;

/**
 * Created by hakim on 14-Jan-16.
 */
public class WikiBot {
    public static void main(String[] args) {
        JSONObject swaggerJson = SwaggerReader.readSwaggerJson();
        //System.out.print(swaggerJson.toJSONString());
        TemplateGenerator.initializeWithSwaggerJson(swaggerJson);
        TemplateGenerator.printAllTemplates();
    }
}

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

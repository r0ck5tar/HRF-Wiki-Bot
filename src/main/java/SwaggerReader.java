import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hakim on 14-Jan-16.
 */
public class SwaggerReader {

    public static JSONObject readSwaggerJson() {
        StringBuilder swagger = new StringBuilder();
        Charset charset = Charset.forName("UTF-8");
        Path file = Paths.get("./reservations.json");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                swagger.append(line + "\n");
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        //System.out.print(swagger);

        JSONObject swaggerJson = null;

        try {
            swaggerJson = (JSONObject) new JSONParser().parse(swagger.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return swaggerJson;
    }
}

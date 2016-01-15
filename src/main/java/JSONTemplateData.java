import org.json.simple.JSONObject;

/**
 * Created by hakim on 14-Jan-16.
 */
public abstract class JSONTemplateData {
    protected int level = 0;


    public JSONTemplateData(int level) {
        this.level = level;
    }

    protected String indent() {
        StringBuilder sb = new StringBuilder();
        sb.append("&#32;");
        for(int i=0; i <=level*2; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public abstract String toTemplateString();
}

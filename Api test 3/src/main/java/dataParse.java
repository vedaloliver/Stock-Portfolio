import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class dataParse {

    private databaseConnect databaseConnection;

    private JSONObject jsons;

    private HashMap<String, String> dateMap;

    // sorts the chosen date category to retreive the soonest/display sorted dates
    private ArrayList<String> sortedKeys;



    public dataParse(String name) throws Exception {
        this.databaseConnection = new databaseConnect();
        this.jsons = new JSONObject();
        this.dateMap = new HashMap<>();

        // next step is get sorted hashmap
        this.sortedKeys = new ArrayList<String>();

        processrawJSON(name);
        JSONToHashmap();
    }

    public JSONObject getJSON(){
        return this.jsons;
    }
    public HashMap<String,String> getDateMap() {
        return this.dateMap;
    }
    public ArrayList<String> getSortedKeys(){
        return this.sortedKeys;
    }


    // processes converted-to-string json data from database back to jsonobject
    public void processrawJSON(String name){
        JSONObject jsonObject = new JSONObject(databaseConnection.getJSonData(name));
        this.jsons = jsonObject;
    }
    // converts the json stock data to hashmaps
    public void  JSONToHashmap() throws Exception {
        // puts it into a map correspondign to date and value
        for (String i : this.jsons.keySet()) {
            dateMap.put(i, (String) this.jsons.getJSONObject(i).get("1. open"));
        }
        this.sortedKeys = new ArrayList<String>(dateMap.keySet());
        Collections.sort(this.sortedKeys, Collections.reverseOrder());

    }


    // retrieves chronologically earliest date|value combination
    public String RecentPrice() throws Exception {
            return "Date = " + sortedKeys.get(0) + " | Value = " + getDateMap().get(sortedKeys.get(0));
        }
    // retrieves chronologically earliest date|value combination
    public void allPrices() throws Exception {
        for (int i=0;i<sortedKeys.size();i++){
            System.out.println("Date = " + sortedKeys.get(i) + " | Value = " + getDateMap().get(sortedKeys.get(i)));
        }
    }
    }








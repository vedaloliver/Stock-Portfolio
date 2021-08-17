import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    // retrieves chronologically earliest |value combination
    public String RecentPrice() throws Exception {
        String returnStatement = "";
        if (timeSinceUpdating()>1 && timeSinceUpdating()<7) {
            returnStatement = "Value = " + getDateMap().get(sortedKeys.get(0)) + " | Date = " + sortedKeys.get(0) + " (Updated " + timeSinceUpdating() + " days ago)";
        } else if (timeSinceUpdating()>7){
            returnStatement = "Value = " + getDateMap().get(sortedKeys.get(0)) + " | Date = " + sortedKeys.get(0) + " (Updated " + timeSinceUpdating() + " days ago. Please update stock data)";

        } else if (timeSinceUpdating()==1){
            returnStatement = "Value = " + getDateMap().get(sortedKeys.get(0))+" | Date = " + sortedKeys.get(0) + " (Updated "+timeSinceUpdating()+" day ago)";
        } else if (timeSinceUpdating() ==0){
            returnStatement = "Value = " + getDateMap().get(sortedKeys.get(0))+" | Date = " + sortedKeys.get(0) + " (Updated today)";
        }

        return returnStatement;
    }
    public String recentDate() {
        return sortedKeys.get(0);
    }

    // how many days since the stock entry was updated
    public long timeSinceUpdating() throws ParseException {
        long difference_In_Days=0;
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        try{

            Date d1 = sdf.parse(recentDate());
            //current time in epoch time
            long currentTimeStamp = System.currentTimeMillis();

            // gets time difference and converts it into days
            long difference_In_Time = currentTimeStamp - d1.getTime();
            difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference_In_Days;
    }
    // retrieves chronologically earliest date|value|
    public void allPrices() throws Exception {
        for (int i=0;i<sortedKeys.size();i++){
            System.out.println("Date = " + sortedKeys.get(i) + " | Value = " + getDateMap().get(sortedKeys.get(i)));
        }
    }
    }








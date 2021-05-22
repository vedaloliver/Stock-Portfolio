import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

public class stockApiCalls {
    // hashmap provides date and value for each date
    private HashMap<String, String> dailyDates;
    private HashMap<String, String> weeklyDates;
    private HashMap<String, String> monthlyDates;

    // sorts the chosen date category to retreive the soonest/display sorted dates
    private ArrayList<String> sortedKeys;

    // search class constructor
    private stockSearch search;

    // used to check if the api limit has been reached (5 a minute)
    private Boolean failstate;


    public stockApiCalls() {
        this.search = new stockSearch();
        this.dailyDates = new HashMap<String, String>();
        this.monthlyDates = new HashMap<String, String>();
        this.weeklyDates = new HashMap<String, String>();
        this.sortedKeys = new ArrayList<String>();
        this.failstate=false;
    }


    public HashMap<String, String> getDailyDates() {
        return dailyDates;
    }

    public HashMap<String, String> getWeeklyDates() {
        return weeklyDates;
    }

    public HashMap<String, String> getMonthlyDates() {
        return monthlyDates;
    }


    // gets most recent price of stock in terms of day
    public String dailyRecentPrice() throws Exception {
        apiCallDate("DAILY", "Time Series (Daily)", getDailyDates());
        // if exceeding api requests
        if (this.failstate == false) {
            Collections.sort(this.sortedKeys, Collections.reverseOrder());
            // getting the first entry / chronologically earliest
            return "Date = " + sortedKeys.get(0) + " Value = " + getDailyDates().get(sortedKeys.get(0));
        }
        return "Max requests per minute reached. Please wait and try again";
    }


    public String weeklyRecentPrice() throws Exception {
        apiCallDate("WEEKLY", "Weekly Adjusted Time Series", getWeeklyDates());
        if (this.failstate == false) {
            Collections.sort(this.sortedKeys, Collections.reverseOrder());
            // getting the first entry / chronologically earliest

            return "Date = " + sortedKeys.get(0) + " Value = " + getWeeklyDates().get(sortedKeys.get(0));
        }
        return "Max requests per minute reached. Please wait and try again";
    }

    public String monthlyRecentPrice() throws Exception {
        apiCallDate("MONTHLY", "Monthly Adjusted Time Series", getMonthlyDates());
        if (this.failstate == false) {
            Collections.sort(this.sortedKeys, Collections.reverseOrder());
            // getting the first entry / chronologically earliest

            return "Date = " + sortedKeys.get(0) + " Value = " + getMonthlyDates().get(sortedKeys.get(0));
        }
        return "Max requests per minute reached. Please wait and try again";
    }

    // api calling function
    public void apiCallDate(String timePeriod, String label, HashMap<String, String> dateMap) throws Exception {
        this.failstate=false;
        HttpResponse<JsonNode> responses = Unirest.get("https://alpha-vantage.p.rapidapi.com/query?symbol=" + search.getStockName() + "&function=TIME_SERIES_" + timePeriod + "_ADJUSTED&datatype=json")
                .header("x-rapidapi-key", "3552eb7274msh1b4efa3154d110dp111434jsnb6d80eeb8d2a")
                .header("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .asJson();

        // breaks up so can get the daily date and the value at that time
        JSONObject toObj = responses.getBody().getObject();
        // checks if the json message returned is notifying of API request max limit
        if (toObj.toString().equals("{\"message\":\"You have exceeded the rate limit per minute for your plan, BASIC, by the API provider\"}")) {
            failstate = true;

        } else {
            JSONObject dates = toObj.getJSONObject(label);
            // puts it into a map correspondign to date and value
            for (String i : dates.keySet()) {
                dateMap.put(i, (String) dates.getJSONObject(i).get("1. open"));
            }
            // sorting mechanism for ordering
            this.sortedKeys = new ArrayList<String>(dateMap.keySet());
        }
    }
}


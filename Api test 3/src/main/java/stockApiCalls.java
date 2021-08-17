import java.util.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

public class stockApiCalls {
    // hashmap provides date and value for each date
    private HashMap<String, String> dailyDates;
    private HashMap<String, String> weeklyDates;
    private HashMap<String, String> monthlyDates;


    // search class constructor
    private stockSearch search;

    // used to check if the api limit has been reached (5 a minute)
    private Boolean failstate;

    private databaseConnect databaseConnection;

    private JSONObject JSONRaw;




    public stockApiCalls() {
        this.databaseConnection = new databaseConnect();
        this.search = new stockSearch();
        this.dailyDates = new HashMap<String, String>();
        this.monthlyDates = new HashMap<String, String>();
        this.weeklyDates = new HashMap<String, String>();
        this.failstate=false;

        this.JSONRaw = new JSONObject();
    }
    // separate constructor for 'updating stock' functionality
    public stockApiCalls(int n) {
        this.databaseConnection = new databaseConnect();
        this.dailyDates = new HashMap<String, String>();
        this.monthlyDates = new HashMap<String, String>();
        this.weeklyDates = new HashMap<String, String>();
        this.failstate=false;

        this.JSONRaw = new JSONObject();
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

    public String getStockName(){
        return search.getStockName();
    }



//    public JSONObject dailyPriceToJSon() throws Exception {
//        return apiCallDate("DAILY", "Time Series (Daily)");
//
//    }

//    public JSONObject monthlyPriceToJSon() throws Exception {
//        return apiCallDate("MONTHLY", "Monthly Adjusted Time Series");
//
//    }


    // api calling function to return json
    public JSONObject apiCallDate(String timePeriod, String label, String stockName) throws Exception {
        JSONObject dateReturn = new JSONObject();

        this.failstate=false;
        HttpResponse<JsonNode> responses = Unirest.get("https://alpha-vantage.p.rapidapi.com/query?symbol=" + stockName + "&function=TIME_SERIES_" + timePeriod + "_ADJUSTED&datatype=json")
                .header("x-rapidapi-key", "3552eb7274msh1b4efa3154d110dp111434jsnb6d80eeb8d2a")
                .header("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .asJson();

        // breaks up so can get the daily date and the value at that time
        JSONObject toObj = responses.getBody().getObject();
        // checks if the json message returned is notifying of API request max limit
        if (toObj.toString().equals("{\"message\":\"You have exceeded the rate limit per minute for your plan, BASIC, by the API provider\"}")) {
            failstate = true;
            System.out.println("exceeded api requests, Please try again soon.");
            System.exit(0);

        } else {
            JSONObject dates = toObj.getJSONObject(label);
            dateReturn = dates;
        }
        return dateReturn;
    }

    public void pushToDatabase() throws Exception {
            String stockID = Integer.toString(databaseConnection.getStockID(getStockName()));
            //databaseConnection.push(getStockName(), "0", dailyPriceToJSon().toString(), stockID, "Day");
            databaseConnection.push(getStockName(), "0", apiCallDate("WEEKLY", "Weekly Adjusted Time Series",
                    search.getStockName()).toString(), stockID, "Week");
            //databaseConnection.push(getStockName(), "0", monthlyPriceToJSon().toString(), stockID, "Month");
        }
        }




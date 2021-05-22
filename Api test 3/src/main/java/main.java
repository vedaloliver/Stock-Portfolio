import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class main {
    public static void main(String[] args) throws Exception {

        stockApiCalls api = new stockApiCalls();
        //stockSearch search = new stockSearch();

      System.out.println(api.dailyRecentPrice());
        System.out.println(api.weeklyRecentPrice());
       System.out.println(api.monthlyRecentPrice());

        //stockInput();
    }

}

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

//        databaseConnect dataConnection = new databaseConnect();
//        dataConnection.push("h","222","222","222","222");

        databaseConnect connect = new databaseConnect();
        //connect.deleteAllEntries();





        stockApiCalls api = new stockApiCalls();
        System.out.println(api.dailyRecentPrice());
        System.out.println(api.weeklyRecentPrice());
       System.out.println(api.monthlyRecentPrice());
        api.pushToDatabase();


    }

}

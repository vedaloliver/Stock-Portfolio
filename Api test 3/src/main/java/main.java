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

        userInterface ui = new userInterface();
        ui.landingPage();


//        databaseConnect dataConnection = new databaseConnect();
//        System.out.println(dataConnection.getStockNamesPresent());
//        System.out.println(dataConnection.checkInputInDatabase("AAPL"));
//        System.out.println(dataConnection.checkInputInDatabase("AgfdgAPL"));
////
//        databaseConnect connect = new databaseConnect();
//        System.out.println(connect.getNumberOfStocks());



//        dataParse parsing = new dataParse("AAPL");
//        System.out.println(parsing.RecentPrice());
//        parsing.closestDate();
//        parsing.convertWealthToday();



//          FOR ADDING STOCKS TO THE DATABASE
//        stockApiCalls api = new stockApiCalls();
//
//        api.pushToDatabase();




    }

}

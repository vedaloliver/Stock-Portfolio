import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Scanner;

public class stockSearch {
    private String stockName;
    private HashMap<String, String> map;


    public stockSearch(){
        this.stockName = "";
        this.map = new HashMap<>();
        try{
            stockSearch();
        } catch (Exception e){
            e.printStackTrace();
        }
        chooseStock();
    }
    public String getStockName(){
        return this.stockName.toUpperCase();
    }

    public void setStockName(String stockSymbol){
        this.stockName = stockSymbol;
    }
    public HashMap<String, String>  getStocksList(){
        return this.map;
    }

    // requests a keyword the user gives of the company ("Alphabet","Facebook)
    public static String stockInput () {
        Scanner myObj = new Scanner(System.in);
        System.out.println("What stock do you want to search for? : ");
        String stockName = myObj.nextLine();
        System.out.println("Searching for " + stockName+"...");

        return stockName;
    }

    // uses the API to search for the keyword, returning an array of symbols
    public HashMap<String, String> stockSearch() throws Exception {
        while (true) {
            String keyword = stockInput();
            HttpResponse<JsonNode> request = Unirest.get("https://alpha-vantage.p.rapidapi.com/query?keywords=" + keyword + "&function=SYMBOL_SEARCH&datatype=json")
                    .header("x-rapidapi-key", "3552eb7274msh1b4efa3154d110dp111434jsnb6d80eeb8d2a")
                    .header("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                    .asJson();
            // parses down and gets matches
            JSONObject toObj = request.getBody().getObject();
            JSONArray arr = toObj.getJSONArray("bestMatches");

            // link symbol with name

            // interates and gets each symbol present
            int counter = 0;
            for (Object i : arr) {
                String name = ((String) arr.getJSONObject(counter).get("2. name"));
                String symbol = ((String) arr.getJSONObject(counter).get("1. symbol"));
                getStocksList().put(symbol, name);
                counter++;
            }
            if (!getStocksList().isEmpty()){
                System.out.println("valid");
                break;
            }else{
                System.out.println("Could not find any matches. please try again");
            }
        }
        return getStocksList();
    }

    // user will choose the stock symbol out of the list provided
    public  String chooseStock() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please choose which stock:");
        System.out.println(getStocksList().keySet());
        String stockName = " " ;


        while (true) {
            stockName = myObj.nextLine();
            if (getStocksList().keySet().contains(stockName.toUpperCase())) {
                System.out.println("you have chosen the stock " + stockName.toUpperCase() + ", from " + getStocksList().get(stockName));
                setStockName(stockName.toUpperCase());
                break;
            } else {
                System.out.println("not a valid input, try again");
            }
        }

        return stockName;
    }
}

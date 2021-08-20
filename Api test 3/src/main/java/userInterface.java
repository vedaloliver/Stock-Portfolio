import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Scanner;

public class userInterface {
    //dataParse dataParse;
    databaseConnect databaseConnect;
    stockSearch stockSearch;

    public userInterface() {
        //this.stockSearch = new stockSearch();
        this.databaseConnect = new databaseConnect();
        //this.dataParse = new dataParse();

    }

    public void landingPage() throws Exception {
        System.out.println("Welcome to the stonk master 3000.");
        System.out.println(databaseConnect.getNumberOfStocks() + " individual Companies' stock data are currently " +
                "in your portfolio: ");
        System.out.println("X of your items in the portfolio have not had personal valuations provided." +
                "Please provide the how much money you have invested in each.");

        choice();
    }


    public void choice() throws Exception {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("\nPlease use the menu to choose what you want to do:\n1: View your data\n2: Add new stock data to the portfolio \n" +
                    "3:Add wealth data to stock \n4: Delete a stock by name\n" +
                    "5: Clear all portfolio data\n6: Update individual stock data\nX: Exit the program");
            String choice = input.nextLine();
            System.out.println("You have chose option "+ choice+".");

            if (choice.equals("1")){
                userData();
                System.out.println("\nBringing you back to the main menu....");


            }

            else if (choice.equals("2")){
                addStock();
                System.out.println("\nBringing you back to the main menu....");

            }

            if (choice.equals("3")){
                addWealthData();
                System.out.println("\nBringing you back to the main menu....");


            }
            else if (choice.equals("4")){
                deleteStock();
                System.out.println("\nBringing you back to the main menu....");


            }
            else if (choice.equals("5")){
                databaseConnect.deleteAllEntries();
                System.out.println("\nBringing you back to the main menu....");
            }
            else if (choice.equals("6"))
                updateStock();
            else if (choice.equals("x".toUpperCase())) {
                break;
            }
        }
    }

    //provides users' stock data and most recent price
    public void userData() throws Exception {
        System.out.println("Provided are your chosen stocks and the most recent prices:\n");
        databaseConnect.getNumberOfStocks();
        for (String i: databaseConnect.getStockNamesPresent()){
            dataParse parse = new dataParse(i);
            System.out.println(i + ": "+ parse.RecentPrice() );
        }
    }


    // uses other class fucntion to add stock
    public void addStock() throws Exception {
    stockApiCalls stockAdd = new stockApiCalls();
    stockAdd.pushToDatabase();
    }


    // Step 1: ask for what stock you're including
    // Step 2 : check if that stock is actually in database 1 if not provide error
    // step 3: request for money inputted, and the date
    // step 4: array created and database will take each and upload it ]

    // make second database
    // database will have Stock data/money/date
    public void addWealthData() throws Exception{
        // checks if the name the user enters is actually in the database
        while(true) {
            System.out.println("Please provide the name of the stock you want to add monetary value to:");
            Scanner inputWealth = new Scanner(System.in);
            String stockNameChoice = inputWealth.nextLine();
            if (databaseConnect.checkInputInDatabase(stockNameChoice) == false) {
                System.out.println(stockNameChoice + " is not in the database, please input again");
            } else if (databaseConnect.checkInputInDatabase(stockNameChoice) == true) {
                System.out.println(stockNameChoice + " is in the database");
                break;
            }
        }

        //databaseConnect.push("microsoft", "13th sept","billions");

    }

    public void deleteStock() throws SQLException {
        System.out.println("What stock do you want to delete? Provide the code:");
        Scanner inputStockDelete = new Scanner(System.in);
        String choice2 = inputStockDelete.nextLine();
        databaseConnect.deleteEntry(choice2);
    }

    public void updateStock() throws Exception {
        System.out.println("Please provide the code of the stock you want to update");
        Scanner inputStockUpdate = new Scanner(System.in);
        String choice3 = inputStockUpdate.nextLine();
        databaseConnect.updateEntry(choice3);
    }


}

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
            System.out.println("\nPlease use the menu to choose what you want to do:\n1: View your data\n2: Add new stock data to the portfolio \n3: Delete a stock by name\n" +
                    "4: Clear all portfolio data\n5: Update individual stock data\nX: Exit the program");
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
            else if (choice.equals("3")){
                deleteStock();
                System.out.println("\nBringing you back to the main menu....");


            }
            else if (choice.equals("4")){
                databaseConnect.deleteAllEntries();
                System.out.println("\nBringing you back to the main menu....");
            }
            else if (choice.equals("5"))
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

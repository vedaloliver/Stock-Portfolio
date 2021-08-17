import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class databaseConnect {

    private Connection connection;
    private String stockname;
    private int choice;
    private int stockValue;
    private Set<String> nameSet;


    public databaseConnect() {
        this.choice = 0;
        this.stockValue = -1;
        this.stockname = "";
        this.nameSet = new HashSet<String>();
    }

    // Simple getters
    public int getChoice() {
        return this.choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public String getStockname() {
        return this.stockname;
    }

    // each time is called will check for connection validity
    // if true will push to database
    public void push(String stockName, String date, String value, String stockID, String dateType) {
        this.connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");
            if (this.connection != null) {
                addToDatabase(stockName, date, value, stockID, dateType);
            } else {
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // methods for adding field data
    public void addToDatabase(String stockName, String date, String value, String stockID, String dateType) {
        try {
            // sample sql addition
            String sql = "INSERT INTO stockvalues (stock_name,date,value,val_id,date_type)"
                    + " VALUES ( '" + stockName + "','" + date + "','" + value + "','" + stockID + "','" + dateType + "')";
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("A new entry is added ");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // checks if stock is present in database
    public boolean checkIfStockPresent(String stock) {
        boolean bool = false;
        try {
            // delte this later
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            Statement statement = this.connection.createStatement();
            // returns all of the values from the table
            ResultSet results = statement.executeQuery("SELECT * FROM stockvalues");

            while (results.next()) {
                String name = results.getString("stock_name");
                if (stock.equals(name)) {
                    bool = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return bool;
    }

    // pulls out the json data for the requested stock name
    // Currently only pulls out data for the 'Day' section, can easily change based on adding another parameter
    public String getJSonData(String stock) {
        String pulledJSONData = "";

        boolean bool = false;
        try {
            if (checkIfStockPresent(stock) == true) {
                // delte this later
                Class.forName("org.postgresql.Driver");
                this.connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/lakupip",
                        "postgres", "postgres");

                Statement statement = this.connection.createStatement();
                // SQL statement
                ResultSet results = statement.executeQuery("SELECT stock_name,value,date_type FROM stockvalues " +
                        "where stock_name = '" + stock + "' AND  date_type = 'Week'");

                // Gets column numbers to retreive specifc oens
                ResultSetMetaData rsmd = results.getMetaData();
                int columnNumber = rsmd.getColumnCount();

                // Retreives JSON data from table
                while (results.next()) {
                    //System.out.println(results.getString(2));
                    pulledJSONData = (results.getString(2));
                }
            } else {
                System.out.println("Stock Name not found in database");
                pulledJSONData = null;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return pulledJSONData;
    }

    // Returns the amount of individual company stocks in the database
    public int getNumberOfStocks() {
        int returnStatement = -1;
        int numbers = 0;

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            Statement statement = this.connection.createStatement();
            // SQL statement
            ResultSet results = statement.executeQuery("SELECT stock_name FROM stockvalues ");

            while (results.next()) {
                nameSet.add(results.getString(1));
            }
            for (String i : nameSet) {
                numbers += 1;
            }
            returnStatement = numbers;
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnStatement;
    }

    // Parses and provides names of the stocks present
    public Set<String> getStockNamesPresent() {
        return nameSet;
    }

    // search for the stock and see if it's in there. if so, get that value id and return it
    // if that stock isnt there, then find the highest id and the int will be the one after that
    public int getStockID(String stock) {
        int existingStockValueID = -1;
        int noStockValuePresent = -1;

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            Statement statement = this.connection.createStatement();
            // returns all of the values from the table
            ResultSet results = statement.executeQuery("SELECT * FROM stockvalues");

            // iterates through, getting stock nam and value
            while (results.next()) {
                String name = results.getString("stock_name");
                String val_id = results.getString("val_id");
                noStockValuePresent = Integer.valueOf(val_id);

                if (stock.equals(name)) {
                    existingStockValueID = Integer.valueOf(val_id);
                }
            }

            // if no stock value is present, then takes the highest(last) and adds a value
            // CHANGE THIS TO THE HIGHEST, NOT THE LAST
            noStockValuePresent += 1;
        } catch (Exception e) {
            System.out.println(e);
        }

        // if there was an existing stock inside, then return the value for that. if not then return the highest value
        int returnStatement;
        if (existingStockValueID == -1) {
            returnStatement = noStockValuePresent;
        } else {
            returnStatement = existingStockValueID;
        }
        System.out.println(returnStatement);
        return returnStatement;
    }


    // looks for the stock id when given the name ; used for deleting the stock
    public int findStockID(String stock) {
        int existingStockValueID = -1;
        int noStockValuePresent = -1;

        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            Statement statement = this.connection.createStatement();
            // returns all of the values from the table
            ResultSet results = statement.executeQuery("SELECT * FROM stockvalues");

            // iterates through, getting stock nam and value
            while (results.next()) {
                String name = results.getString("stock_name");
                String val_id = results.getString("val_id");

                if (stock.equals(name)) {
                    existingStockValueID = Integer.valueOf(val_id);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return existingStockValueID;
    }

    // clears all entries in the database
    public void deleteAllEntries() throws SQLException {
        int rowsAffected = 0;
        String delete_statement = "delete from stockvalues;";
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            PreparedStatement preparedStatement = connection.prepareStatement(delete_statement);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(rowsAffected + " Entries Deleted.");
    }

    // deletes a single entry
    // probably would be good to iterate to delete all and delete based on item
    public void deleteEntry(String name) throws SQLException {
        String delete_statement = "delete from stockvalues where stock_name =?";
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            PreparedStatement preparedStatement = this.connection.prepareStatement(delete_statement);
            preparedStatement.setString(1, name.toUpperCase());
            preparedStatement.executeUpdate();
            System.out.println("Deleted the entry successfully.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // deletes entire row and adds it again to provide updated data
    public void updateEntry(String name) throws Exception {
        stockApiCalls api = new stockApiCalls(1);
        deleteEntry(name);
        String stockID = Integer.toString(getStockID(name));
        push(name, "0",api.apiCallDate("WEEKLY","Weekly Adjusted Time Series",name).toString(),stockID,"Week");
        System.out.println("Updated entry");

    }
}

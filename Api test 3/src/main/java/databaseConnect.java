import java.sql.*;

public class databaseConnect {

    Connection connection;
    int choice;
    int stockValue;
    String stockname;

    public databaseConnect(){
        this.choice = 0;
        this.stockValue=-1;
        this.stockname = "";
    }

    public int getChoice(){
        return this.choice;
    }

    public void setChoice(int choice){
        this.choice = choice;
    }

    public String getStockname(){
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
                    addToDatabase(stockName, date, value,stockID,dateType);
            } else {
                System.out.println("Connection failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // methods for adding field data
    public void addToDatabase(String stockName, String date, String value,String stockID, String dateType) {
        try {
            // sample sql addition
            String sql = "INSERT INTO stockvalues (stock_name,date,value,val_id,date_type)"
                    + " VALUES ( '"+stockName+"','"+ date+"','"+ value+"','"+stockID+"','"+dateType+"')";
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
                    bool= true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return bool;
    }

    // search for the stock and see if it's in there. if so, get that value id and return it
    // if that stock isnt there, then find the highest id and the int will be the one after that
    public int getStockID(String stock){
        int existingStockValueID = -1;
        int noStockValuePresent=-1;

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
            noStockValuePresent +=1;
        }  catch (Exception e) {
            System.out.println(e);
        }

        // if there was an existing stock inside, then return the value for that. if not then return the highest value
        int returnStatement = 0;
        if (existingStockValueID == -1){
            returnStatement = noStockValuePresent;
        } else{
            returnStatement = existingStockValueID;
        }
        return returnStatement;
    }

    // clears all entries in the database
    public int deleteAllEntries() throws SQLException {
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
        return rowsAffected;
    }

    // deletes a single entry
    // probably would be good to iterate to delete all and delete based on item
    public int deleteEntry(int id) throws SQLException {
        int rowsAffected = 0;
        String delete_statement = "delete from stockvalues where id =?;";
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres", "postgres");

            PreparedStatement preparedStatement = connection.prepareStatement(delete_statement);
            preparedStatement.setInt(1, id);
            rowsAffected = preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rowsAffected;
    }
}

import java.sql.*;

public class DBUsingJava

{

    public static void main( String args[] ){
        Connection connection = null;
        try{
            Class.forName("org.postgressql.Driver");
            connection=DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lakupip",
                    "postgres","postgres");
            if(connection!=null){
                System.out.println("Connection OK");
            }else{
                System.out.println("Connection failed");
            }

        } catch (Exception e){
            System.out.println(e);

        }
    }

}
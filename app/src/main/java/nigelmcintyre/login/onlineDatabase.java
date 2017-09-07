package nigelmcintyre.login;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class onlineDatabase extends AsyncTask<Object, Void, Void> {

    private String connectionString = "jdbc:mysql://danu6.it.nuigalway.ie:3306/mydb1355mn&password=qa0tol";
    private Connection connection = null;
    private Statement statement =  null;
    private String sql= "INSERT INTO `mydb1355`.`meals` (`mealDate`, `mealTime`, `mealName`, `mealCarbs`, `mealFats`, `mealProtein`, `mealkCals`, `mealId`, `mealUser`) VALUES (CURRENT_DATE(), 'fsd', 'fdf', '21', '2', '2', '2', '2', 'dwew');";







    @Override
    protected Void doInBackground (Object...params){
        try {
            Class.forName("com.myspl.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

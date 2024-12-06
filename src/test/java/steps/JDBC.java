package steps;

import io.cucumber.java.bg.И;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static steps.MyStepdefs.driver;

public class JDBC {
    private static final String count = "SELECT COUNT(FOOD_NAME) AS NAME FROM FOOD WHERE FOOD_NAME = ?";
    private static final String foodInfo = "SELECT * FROM FOOD WHERE FOOD_NAME = ?";
    private static final String deleteString = "DELETE FROM FOOD WHERE FOOD_NAME = ?";
    private static Connection connection;

    @И("подключиться к БД")
    public void connectDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
    }

    @И("проверить, что в БД нет {string}")
    public void absentFoodInDB(String foodName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(count);
        statement.setString(1, foodName);
        ResultSet resultExp = statement.executeQuery();
        resultExp.next();
        int result = resultExp.getInt("NAME");
        connection.close();
        assertEquals(0, result);
    }

    @И("проверить, что в БД есть {string}")
    public static void presentFoodInDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(foodInfo);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        assertEquals(name, resultSet.getString("FOOD_NAME"));
        connection.close();
    }

    @И("очистить данные из БД {string}")
    public static void resetDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(deleteString);
        statement.setString(1, name);
        assertEquals(1, statement.executeUpdate());
        connection.close();
        driver.close();
        driver.quit();
    }
}

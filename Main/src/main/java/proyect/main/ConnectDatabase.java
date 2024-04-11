package proyect.main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectDatabase {
    private static final String url = "jdbc:oracle:thin:@//localhost:1521/XE"; //dirrecion de la base de datos
    private static final String user = "Stiff_Cipri";//el nombre del usuario
    private static final String password = "stiff1824";//va la contraseña del usuario
    public static Connection getConnection() throws SQLException {
       return DriverManager.getConnection(url, user, password);
    }
}
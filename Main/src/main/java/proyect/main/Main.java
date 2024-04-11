package proyect.main;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

       Menu v=new Menu();
        v.abrir();
try (Connection conn =  ConnectDatabase.getConnection()) {
                    System.out.println("entre");

        } catch (SQLException e) {
            System.out.println("no entre pipipi");
            e.printStackTrace();
        }
    }
}
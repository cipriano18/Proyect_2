package proyect.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Talonario {
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   public static ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        String query = "SELECT nombre FROM talonario";
        try (Connection conn = ConnectDatabase.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Talonario talonario = new Talonario();
                String name = rs.getString("nombre");
                talonario.setName(name); 
                names.add(talonario.getName()); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
}

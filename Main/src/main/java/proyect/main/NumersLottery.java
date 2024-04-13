/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyect.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author PERSONAL
 */
public class NumersLottery extends javax.swing.JFrame {
    private JPanel numerosPanel;
    private JButton reservarButton;
    private JButton pagarButton;

    public NumersLottery(String nameLottery) {
        setTitle("Talonario de Rifas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel para los números
        numerosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(numerosPanel, BorderLayout.CENTER);

        // Botones de acción
        reservarButton = new JButton("Reservar");
        pagarButton = new JButton("Pagar");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(reservarButton);
        buttonPanel.add(pagarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acción del botón de reservar
        //reservarButton.addActionListener(e -> reservarNumero());
        // Acción del botón de pagar
        //pagarButton.addActionListener(e -> pagarNumero());
        // Cargar números del talonario desde la base de datos
        cargarNumerosTalonarioDesdeDB(nameLottery);
    }

    private void cargarNumerosTalonarioDesdeDB(String nameLottery) {
        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT cantidad_numero FROM talonario WHERE nombre = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, nameLottery);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String numero = rs.getString("numero");
                JButton numeroButton = new JButton(numero);
                //numeroButton.addActionListener(new NumeroButtonActionListener(numero));
                numerosPanel.add(numeroButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

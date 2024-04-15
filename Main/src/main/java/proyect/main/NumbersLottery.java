/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package proyect.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author PERSONAL
 */
public class NumbersLottery extends javax.swing.JFrame {

    private JPanel numerosPanel;
    private JButton reservarButton;
    private JButton pagarButton;
    private List<JButton> botonesGrises = new ArrayList<>();

    public NumbersLottery(String nameLottery) {
        setTitle("Talonario de Rifas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        numerosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(numerosPanel, BorderLayout.CENTER);

        reservarButton = new JButton("Reservar");
        pagarButton = new JButton("Pagar");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(reservarButton);
        buttonPanel.add(pagarButton);
        buttonPanel.doLayout();
        add(buttonPanel, BorderLayout.SOUTH);

        cargarNumerosTalonarioDesdeDB(nameLottery);
    }

    private void cargarNumerosTalonarioDesdeDB(String nameLottery) {
        String sql = "SELECT cantidad_numero FROM talonario WHERE nombre = ?";
        String numero = new String();
        try (Connection conn = ConnectDatabase.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameLottery);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                numero = result.getString("cantidad_numero");
                //numeroButton.addActionListener(new NumeroButtonActionListener(numero));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);

        }
        if (!numero.isEmpty()) {
            for (int i = 0; i <= Integer.parseInt(numero); i++) {
                String numbers = String.valueOf(i);
                JButton numeroButton = new JButton(numbers);
                numerosPanel.add(numeroButton);
                
                numeroButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton botonPresionado = (JButton) e.getSource();
                        if (botonPresionado.getBackground() != Color.GRAY) {
                            botonPresionado.setBackground(Color.GRAY);
                            botonesGrises.add(botonPresionado);
                        } else {
                            botonPresionado.setBackground(null);
                            botonesGrises.remove(botonPresionado);
                        }
                        
                    }
                });
            }
        }
    }
    
        private void reservarNumeros() {
        BooksOfLottery newBook = new BooksOfLottery();
        // Enviamos la lista de botones grises a la función reservarNumero
        newBook.reservarNumero(botonesGrises);
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

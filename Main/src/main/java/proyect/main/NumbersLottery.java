package proyect.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
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

public class NumbersLottery extends javax.swing.JFrame {

    public NumbersLottery() {
        initComponents();
    }

    private JPanel numerosPanel;
    private JButton reservarButton;
    private JButton pagarButton;
    private JButton botonSalida;
    private List<JButton> botonesGrises = new ArrayList<>();

    public NumbersLottery(String nameLottery) {
        setTitle("Talonario de Rifas");
        setSize(560, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        numerosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(numerosPanel, BorderLayout.CENTER);
 
        reservarButton = new JButton("Reservar");
        pagarButton = new JButton("Pagar");
        botonSalida = new JButton("Menu");

        botonSalida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Menu menu = new Menu();
                menu.abrir();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(reservarButton);
        buttonPanel.add(pagarButton);
        buttonPanel.add(botonSalida);
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
    int cantidadNumeros = Integer.parseInt(numero);
    for (int i = 0; i <= cantidadNumeros; i++) {
        String numbers = String.valueOf(i);
        JButton numeroButton = new JButton(numbers);
        numerosPanel.add(numeroButton);
        numeroButton.setBackground(Color.GREEN);
        // Aquí verificamos el estado del número
        try (Connection conn = ConnectDatabase.getConnection()) {
            String sqlEstado = "SELECT estado FROM numero_talonario WHERE numero = ?";
            PreparedStatement stmtEstado = conn.prepareStatement(sqlEstado);
            stmtEstado.setString(1, numbers);
            ResultSet resultEstado = stmtEstado.executeQuery();
         
            if (resultEstado.next()) {
                String estadoNumero = resultEstado.getString("estado");
                if (estadoNumero.equals("R")) {            //estadoNumero == 'R'
                    numeroButton.setBackground(Color.yellow); // estado reservado
                } 
                else if (estadoNumero.equals("P")) {          
                     numeroButton.setBackground(Color.RED); // estado pagado
                }
                else {
                    numeroButton.setBackground(Color.GRAY); // estado disponible
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        numeroButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton botonPresionado = (JButton) e.getSource();
                 if (botonPresionado.getBackground().equals(Color.yellow)) {
                    int option = JOptionPane.showConfirmDialog(null,"Deseas pagar los numeros");
                     if (option==JOptionPane.YES_OPTION) {
                          JOptionPane.showMessageDialog(null, "NUMEROS PAGADOS"); 
                         int numeroSeleccionado = Integer.parseInt(botonPresionado.getText());
                        cambiarEstado(numeroSeleccionado);  
                     }
                     else{
           JOptionPane.showMessageDialog(null, "NO CANCELASTE MALA PAGA"); 
                     }
                 }
                 else if (botonPresionado.getBackground().equals(Color.RED)) {
         JOptionPane.showMessageDialog(null, "ESTE NUMERO ESTA PAGADO");          
                 } else {
                     botonPresionado.setBackground(Color.GRAY);
                    botonesGrises.add(botonPresionado);
                    System.out.println(botonesGrises.size());
                }
            }
        });
    }

reservarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String nameParticipant = JOptionPane.showInputDialog(null, "ingrese su nombre");
                    try (Connection conn = ConnectDatabase.getConnection()) {
                        var stmt = conn.prepareStatement("INSERT INTO participante (nombre_participante) VALUES (?)");
                        stmt.setString(1, nameParticipant);
                        stmt.executeUpdate();
                        reservarNumero(nameParticipant);
                    } catch (SQLException es) {
                        es.printStackTrace();
                    }

                }
            });
        }
      pagarButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
           String nameParticipant = JOptionPane.showInputDialog(null, "ingrese su nombre");
                    try (Connection conn = ConnectDatabase.getConnection()) {
                        var stmt = conn.prepareStatement("INSERT INTO participante (nombre_participante) VALUES (?)");
                        stmt.setString(1, nameParticipant);
                        stmt.executeUpdate();
                        pagado(nameParticipant);
                    } catch (SQLException es) {
                        es.printStackTrace();
                 }
         }
        });
   }
    

    public void reservarNumero(String nameParticipant) {
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarioSelected.getTalonarioSelected();
        System.out.println(TalonarioSelected.getTalonarioSelected());
        try (Connection conn = ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            System.out.println(botonesGrises.size());
            for (int i = 0; i < botonesGrises.size(); i++) {
                int numberSelected = Integer.parseInt(botonesGrises.get(i).getText());
                stmt.setInt(1, numberSelected); // Pasa el número del talonario
                stmt.setString(2, "R"); // Pasa el estado
                stmt.setString(3, nameParticipant); // Pasa el nombre del participante
                stmt.setString(4, opcionSelected); // Pasa el nombre del talonario (esto se tiene que conseguir del panel el nombre del talonario)
                stmt.execute(); // Ejecuta el procedimiento almacenado
            }
            JOptionPane.showMessageDialog(null, "Numeros reservados con exito");
            BooksOfLottery vista = new BooksOfLottery();
            vista.openBookOfLottery();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void pagado (String nameParticipant){
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarioSelected.getTalonarioSelected();
        System.out.println(TalonarioSelected.getTalonarioSelected());
        try(Connection conn= ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)){
            for (int i = 0; i < botonesGrises.size(); i++) {
                int numerSelect=Integer.parseInt(botonesGrises.get(i).getText());
                stmt.setInt(1, numerSelect);
                stmt.setString(2,"P");
                stmt.setString(3, nameParticipant); // Pasa el nombre del participante
                stmt.setString(4, opcionSelected); // Pasa el nombre del talonario (esto se tiene que conseguir del panel el nombre del talonario)
                stmt.execute();
            }
             JOptionPane.showMessageDialog(null, "Numeros pagados con exito");
            BooksOfLottery vista = new BooksOfLottery();
            vista.openBookOfLottery();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 public void cambiarEstado(int numeroSeleccionado) {
    String sql = "{CALL cambiar_estado_numero(?, ?)}"; // Llamar al procedimiento almacenado
    
    try (Connection conn = ConnectDatabase.getConnection(); 
         CallableStatement stmt = conn.prepareCall(sql)) {
        
        stmt.setInt(1, numeroSeleccionado); // Primer parámetro del procedimiento almacenado
        stmt.setString(2, "P"); // Segundo parámetro del procedimiento almacenado
        
        stmt.execute(); // Ejecutar el procedimiento almacenado
        
        JOptionPane.showMessageDialog(null, "Número " + numeroSeleccionado + " pagado con éxito");
        BooksOfLottery vista = new BooksOfLottery();
        vista.openBookOfLottery();
        dispose();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    /*c void rifarNumeros (){    ESTO ES EXTRAAAAAAAAAAAAAAAA!
        
        int sizeNums = botonesGrises.size();
         int numWin = (int)(Math.random()*sizeNums+1);
        System.out.println(numWin);
        
         JOptionPane.showMessageDialog(null, "EL GANADOR ES " + numWin);
    }
    */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        J1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 51));

        J1.setBackground(new java.awt.Color(0, 0, 0));
        J1.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        J1.setForeground(new java.awt.Color(255, 255, 255));
        J1.setText("MENU");
        J1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                J1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("RESERVAR");
        jButton2.setActionCommand("RESERVAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("COMPRAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(J1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap(78, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(259, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(J1)
                    .addComponent(jButton3))
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void J1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_J1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_J1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton J1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    // End of variables declaration//GEN-END:variables
}

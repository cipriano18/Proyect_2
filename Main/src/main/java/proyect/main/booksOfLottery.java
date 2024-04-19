package proyect.main;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class BooksOfLottery extends javax.swing.JFrame {

    public BooksOfLottery() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Charge = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        name = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("MENU");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("VER");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Charge.setBackground(new java.awt.Color(0, 0, 0));
        Charge.setFont(new java.awt.Font("Oswald", 0, 18)); // NOI18N
        Charge.setForeground(new java.awt.Color(255, 255, 255));
        Charge.setText("CARGAR TALONARIOS");
        Charge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChargeActionPerformed(evt);
            }
        });

        Delete.setBackground(new java.awt.Color(0, 0, 0));
        Delete.setFont(new java.awt.Font("Oswald", 0, 14)); // NOI18N
        Delete.setForeground(new java.awt.Color(255, 255, 255));
        Delete.setText("ELIMINAR ");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(51, 102, 0));

        name.setBackground(new java.awt.Color(204, 204, 204));
        name.setFont(new java.awt.Font("Oswald", 0, 18)); // NOI18N
        name.setForeground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(name);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(Delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(43, 43, 43)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Charge, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(Charge, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Delete))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Menu v = new Menu();
        v.openMenuWindow();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChargeActionPerformed
        loadNamesLoterries();
    }//GEN-LAST:event_ChargeActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        String nameSelected = name.getSelectedValue();
           System.out.println(nameSelected);
        deleteNumbersAndParticipant(nameSelected);
        loadNamesLoterries();
    }//GEN-LAST:event_DeleteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String nameSelected = name.getSelectedValue();
        NumbersLottery lotterySelected = new NumbersLottery(nameSelected);
        lotterySelected.setVisible(true);
        lotterySelected.setLocationRelativeTo(null);
        TalonarySelected.setTalonarioSelected(nameSelected);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void loadNamesLoterries() {
        ArrayList<String> nameLotteries = Talonary.getNames();

        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < nameLotteries.size(); i++) {
            String nombre = nameLotteries.get(i);
            model.addElement(nombre);
        }
        name.setModel(model);
    }

private void deleteNumbersAndParticipant(String lotteryName) {
    try (Connection conn = ConnectDatabase.getConnection();
         CallableStatement stmt = conn.prepareCall("{call delete_lottery(?)}")) {
        
        // Establecer el parámetro del nombre del talonario
        stmt.setString(1, lotteryName);
        
        // Ejecutar el procedimiento almacenado
        stmt.execute();
        
        JOptionPane.showMessageDialog(null, "Se han eliminado correctamente los registros asociados al talonario " + lotteryName + ".");
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al eliminar registros.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    public void openBookOfLottery() {
        BooksOfLottery view2 = new BooksOfLottery();
        view2.setVisible(true);
        view2.setLocationRelativeTo(null);
        view2.setTitle("VISTA TALONARIO");
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Charge;
    private javax.swing.JButton Delete;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> name;
    // End of variables declaration//GEN-END:variables
}

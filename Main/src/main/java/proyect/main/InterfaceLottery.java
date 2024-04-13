package proyect.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JOptionPane;
import proyect.main.ConnectDatabase;

public class InterfaceLottery extends javax.swing.JFrame {

    public InterfaceLottery() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        texName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        textDescription = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textCantNum = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textPrize = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        jPanel2.setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ingrese la descipcion del talonario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, -1, -1));
        getContentPane().add(texName, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 190, 30));

        jLabel2.setText("Ingrese el premio");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, -1, -1));

        textDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textDescriptionActionPerformed(evt);
            }
        });
        getContentPane().add(textDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 210, 200, 30));

        jLabel3.setText("Ingrese la cantidad de numeros");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        textCantNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCantNumActionPerformed(evt);
            }
        });
        getContentPane().add(textCantNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 190, 30));

        jButton1.setText("Crear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, -1, -1));

        jButton2.setText("Menu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, -1, -1));
        getContentPane().add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 210, -1));

        jLabel4.setText("Ingrese la fecha del sorteo");
        jLabel4.setToolTipText("");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, -1, -1));

        jLabel5.setText("Ingrese el nombre del talonario");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, -1, -1));

        textPrize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textPrizeActionPerformed(evt);
            }
        });
        getContentPane().add(textPrize, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 200, 30));

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\User\\Desktop\\JAVA\\Proyecto\\Main\\src\\main\\java\\Images\\Menu_1.jpg")); // NOI18N
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 420));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textPrizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPrizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textPrizeActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Menu v = new Menu();
        v.abrir();
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (checkingName() == true && checkingPrize() == true && checkingValues() == true && checkingDate() == true) {
            JOptionPane.showMessageDialog(null, "Creacion del talonario exitosa");
            String NameLottery = texName.getText(), description = textDescription.getText(), price = textPrize.getText();
            int sizeLottery = Integer.parseInt(textCantNum.getText());
            Date dateLottery = jDateChooser1.getDate();

            addLottery(NameLottery, sizeLottery, description, price, dateLottery);
            Menu v = new Menu();
            v.abrir();
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(null, " NO SE CREO");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void textCantNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCantNumActionPerformed

    }//GEN-LAST:event_textCantNumActionPerformed

    private void textDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textDescriptionActionPerformed

 private void addLottery(String name, int sizeLottery, String description, String prize, Date date) {
   try (Connection conn = ConnectDatabase.getConnection()) {
            try {
                var stmt = conn.prepareStatement("INSERT INTO talonario (nombre, cantidad_numero, descripcion, premio, fecha) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, name);
                stmt.setInt(2, sizeLottery);
                stmt.setString(3, description);
                stmt.setString(4, prize);
                stmt.setDate(5, new java.sql.Date(date.getTime()));

                stmt.executeUpdate();
            } catch (SQLException e) {
               e.printStackTrace();
            }
        } catch (SQLException e) {
           e.printStackTrace();
     }

 }
    private boolean checkingName() {
        String name = texName.getText();
        if (name.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkingPrize() {
        String prize = textDescription.getText();
        if (prize.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkingValues() {
        int sizeNumber;
        try {
            sizeNumber = Integer.parseInt(textCantNum.getText());
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private boolean checkingDate() {
        //Obtener la fecha actual
        Date fechaActual = new Date();
        Date fechaSeleccionada = jDateChooser1.getDate();
        //String dateSelectted = this.jDateChooser1; terminar de validar y investigar
//     if (dateSelectted.isEmpty()) {

        //  JOptionPane.showMessageDialog(null,"Debes agregar una fecha :");
        //  return false;
        // }
        if (fechaSeleccionada.before(fechaActual)) {
            JOptionPane.showMessageDialog(null, "Ingrese una fecha valida :");
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField texName;
    private javax.swing.JTextField textCantNum;
    private javax.swing.JTextField textDescription;
    private javax.swing.JTextField textPrize;
    // End of variables declaration//GEN-END:variables
}

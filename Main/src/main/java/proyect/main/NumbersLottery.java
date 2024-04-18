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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class NumbersLottery extends javax.swing.JFrame {

    public static int sizeGlobal;

    public NumbersLottery() {
        initComponents();
    }

    private JPanel numerosPanel;
    private JButton reservarButton;
    private JButton pagarButton;
    private JButton botonSalida;
    private JButton botonRifa;
    private List<JButton> botonesGrises = new ArrayList<>();

    public NumbersLottery(String nameLottery) {
        setTitle("Talonario de Rifas");
        setSize(560, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        numerosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        numerosPanel.setBackground(Color.lightGray);
        add(numerosPanel, BorderLayout.CENTER);

        reservarButton = new JButton("Reservar");
        pagarButton = new JButton("Pagar");
        botonSalida = new JButton("Regresar");
        botonRifa = new JButton("Rifar");
        reservarButton.setBackground(Color.BLACK);
        reservarButton.setForeground(Color.WHITE);
        pagarButton.setBackground(Color.BLACK);
        pagarButton.setForeground(Color.WHITE);
        botonSalida.setBackground(Color.BLACK);
        botonSalida.setForeground(Color.WHITE);
        botonRifa.setBackground(Color.BLACK);
        botonRifa.setForeground(Color.WHITE);
        botonSalida.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BooksOfLottery booksOfLottery = new BooksOfLottery();
                booksOfLottery.openBookOfLottery();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(botonSalida);
        buttonPanel.add(reservarButton);
        buttonPanel.add(pagarButton);
        buttonPanel.add(botonRifa);
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

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!numero.isEmpty()) {
            int cantidadNumeros = Integer.parseInt(numero);
            sizeGlobal = cantidadNumeros;
            for (int i = 0; i <= cantidadNumeros; i++) {
                String numbers = String.valueOf(i);
                JButton numeroButton = new JButton(numbers);
                numerosPanel.add(numeroButton);
                numeroButton.setBackground(Color.GREEN);
                // Aquí verificamos el estado del número
                int idTalonario = obtenerIdTalonario(nameLottery);
                if (obtenerIdTalonario(nameLottery) >= 0) {
                    try (Connection conn = ConnectDatabase.getConnection()) {
                        String sqlEstado = "SELECT estado FROM numero_talonario WHERE numero = ? AND rifa_id = ?";
                        PreparedStatement stmtEstado = conn.prepareStatement(sqlEstado);
                        stmtEstado.setString(1, numbers);
                        stmtEstado.setInt(2, idTalonario); // Pasa el ID del talonario
                        ResultSet resultEstado = stmtEstado.executeQuery();

                        if (resultEstado.next()) {
                            String estadoNumero = resultEstado.getString("estado");
                            if (estadoNumero.equals("R")) {            //estadoNumero == 'R'
                                numeroButton.setBackground(Color.yellow); // estado reservado
                            } else if (estadoNumero.equals("P")) {
                                numeroButton.setBackground(Color.RED); // estado pagado
                            } else {
                                numeroButton.setBackground(Color.GRAY); // estado disponible
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                numeroButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton botonPresionado = (JButton) e.getSource();
                        if (botonPresionado.getBackground().equals(Color.yellow)) {
                            int option = JOptionPane.showConfirmDialog(null, "Deseas pagar los numeros");
                            if (option == JOptionPane.YES_OPTION) {
                                JOptionPane.showMessageDialog(null, "NUMEROS PAGADOS");
                                int numeroSeleccionado = Integer.parseInt(botonPresionado.getText());
                                cambiarEstado(numeroSeleccionado);
                            } else {
                                JOptionPane.showMessageDialog(null, "NO CANCELASTE MALA PAGA");
                            }
                        } else if (botonPresionado.getBackground().equals(Color.RED)) {
                            JOptionPane.showMessageDialog(null, "ESTE NUMERO ESTA PAGADO");
                        } else {
                            botonPresionado.setBackground(Color.GRAY);
                            botonesGrises.add(botonPresionado);
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

        botonRifa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rifarNumeros();
            }
        });
    }

    public void reservarNumero(String nameParticipant) {
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarySelected.getTalonarioSelected();
        System.out.println(TalonarySelected.getTalonarioSelected());
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

    public void pagado(String nameParticipant) {
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarySelected.getTalonarioSelected();
        System.out.println(TalonarySelected.getTalonarioSelected());
        try (Connection conn = ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            for (int i = 0; i < botonesGrises.size(); i++) {
                int numerSelect = Integer.parseInt(botonesGrises.get(i).getText());
                stmt.setInt(1, numerSelect);
                stmt.setString(2, "P");
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

        try (Connection conn = ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

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

    /*    int sizeNums = sizeGlobal;
    int numWin = (int) (Math.random() * sizeNums + 1);
     String lotterySelected = TalonarySelected.getTalonarioSelected();
    
    try (Connection conn = ConnectDatabase.getConnection();
         PreparedStatement stmtEstado = conn.prepareStatement("SELECT estado FROM numero_talonario WHERE numero = ?");
         CallableStatement matt = conn.prepareCall("{ ? = call SeleccionarNombre(?, ?) }")) {
        
        stmtEstado.setInt(1, numWin); // Corregido el índice del parámetro
        try (ResultSet winningResultState = stmtEstado.executeQuery()) {
            if (winningResultState.next()) {
                String stateWinning = winningResultState.getString("estado");
                matt.registerOutParameter(1, Types.VARCHAR);
                matt.setInt(2, numWin);
                matt.setString(3, lotterySelected); // Pasar el ID del talonario en lugar del nombre
                matt.execute();
                String nombre = matt.getString(1);
                if (stateWinning.equals("P")) {
                    JOptionPane.showMessageDialog(null,  "El numero ganador es:  " + numWin + "\nNombre del ganador: " + nombre + "\nFELICIDADES!!!");
                } else if (stateWinning.equals("R")) {
                    JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero solo fue reservado asi que no existe un participante ganador");
                } else {
                    JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero no fue comprado ni reservado asi que no existe participante ganador");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún estado para el número ganador: " + numWin);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
    }*/
    public void rifarNumeros() {
        int sizeNums = sizeGlobal;
        int numWin = (int) (Math.random() * sizeNums + 1);
        String lotterySelected = TalonarySelected.getTalonarioSelected();
        String estadoTalonario = obtenerEstadoTalonarioI(numWin, lotterySelected);

        if (estadoTalonario.equals("P")) {
            try (Connection conn = ConnectDatabase.getConnection()) {
                PreparedStatement stmt1 = conn.prepareCall("SELECT nombre_participante FROM participante WHERE id = ( SELECT person_id FROM numero_talonario WHERE numero = ? AND rifa_id = ( SELECT id FROM talonario WHERE nombre = ? ) )");
                stmt1.setInt(1, numWin);
                stmt1.setString(2, lotterySelected);
                System.out.println(numWin);
                System.out.println(lotterySelected);
                stmt1.execute();
                ResultSet nameWinner = stmt1.executeQuery();
                if (nameWinner.next()) {
                    String name = nameWinner.getString("nombre_participante");
                    JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nNombre del ganador: " + name + "\nFELICIDADES!!!");
                }
            } catch (SQLException e) {
                e.getErrorCode();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (estadoTalonario.equals("R")) {
            JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero solo fue reservado\nAsí que no existe un participante ganador");
        } else if (estadoTalonario.equals("default")) {
            JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero no fue comprado ni reservado\nAsí que no existe participante ganador");
        }
    }

    public int obtenerIdTalonario(String nameLottery) {
        int idTalonario = 0;
        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT id FROM talonario WHERE nombre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameLottery);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                idTalonario = result.getInt("id"); // Cambiado a "id" en lugar de "id_talonario"
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del talonario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return idTalonario;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String obtenerEstadoTalonarioI(int numWin, String lotterySelected) {
        String stateWinning = "default";
        try (Connection conn = ConnectDatabase.getConnection()) {
            PreparedStatement stmtEstado = conn.prepareStatement("SELECT estado FROM numero_talonario WHERE rifa_id = ( SELECT id FROM talonario WHERE nombre = ? ) AND numero = ?");
            stmtEstado.setString(1, lotterySelected);
            stmtEstado.setInt(2, numWin);
            ResultSet winningResultState = stmtEstado.executeQuery();
            if (winningResultState.next()) {
                stateWinning = winningResultState.getString("estado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return stateWinning;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

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

    public static int sizeNumButtons;
 private boolean verify = true;
    public NumbersLottery() {
        initComponents();
    }

    private JPanel panelNumbers;
    private JButton reserveButton;
    private JButton payButton;
    private JButton exitButton;
    private JButton raffleButton;
    private JButton PaidInformation;
    private JButton reserveInformation;
    private JButton availableInformation;
    private List<JButton> buttonsOfNumbers = new ArrayList<>();

    public NumbersLottery(String nameLottery) {
        setTitle("Talonario de Rifas");
        setSize(630, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelNumbers = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNumbers.setBackground(Color.lightGray);
        add(panelNumbers, BorderLayout.CENTER);

        reserveButton = new JButton("Reservar");
        payButton = new JButton("Pagar");
        exitButton = new JButton("Regresar");
        raffleButton = new JButton("Rifar");
        PaidInformation = new JButton("Pago");
        reserveInformation = new JButton("Reservado");;
        availableInformation = new JButton("Disponible");;
        reserveButton.setBackground(Color.BLACK);
        reserveButton.setForeground(Color.WHITE);
        payButton.setBackground(Color.BLACK);
        payButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        raffleButton.setBackground(Color.BLACK);
        raffleButton.setForeground(Color.WHITE);
        PaidInformation.setBackground(Color.RED);
        PaidInformation.setEnabled(false);
        reserveInformation.setBackground(Color.YELLOW);
        reserveInformation.setEnabled(false);
        availableInformation.setEnabled(false);
        availableInformation.setBackground(Color.GREEN);
        availableInformation.setEnabled(false);

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BooksOfLottery booksOfLottery = new BooksOfLottery();
                booksOfLottery.openBookOfLottery();
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel.add(exitButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(payButton);
        buttonPanel.add(raffleButton);
        buttonPanel.add(PaidInformation);
        buttonPanel.add(reserveInformation);
        buttonPanel.add(availableInformation);
        buttonPanel.doLayout();
        add(buttonPanel, BorderLayout.SOUTH);
        loadCheckbookNumbersFromDB(nameLottery);
    }

    private void loadCheckbookNumbersFromDB(String nameLottery) {
        String sql = "SELECT cantidad_numero FROM talonario WHERE nombre = ?";
        String number = new String();
        try (Connection conn = ConnectDatabase.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameLottery);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                number = result.getString("cantidad_numero");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!number.isEmpty()) {
            int cantidadNumeros = Integer.parseInt(number);
            sizeNumButtons = cantidadNumeros;
            for (int i = 0; i <= cantidadNumeros; i++) {
                String numbers = String.valueOf(i);
                JButton numberButton = new JButton(numbers);
                panelNumbers.add(numberButton);
                numberButton.setBackground(Color.GREEN);
                // Aquí verificamos el estado del número
                int idTalonary = getIdTalonary(nameLottery);
                if (getIdTalonary(nameLottery) >= 0) {
                    try (Connection conn = ConnectDatabase.getConnection()) {
                        String sqlEstado = "SELECT estado FROM numero_talonario WHERE numero = ? AND rifa_id = ?";
                        PreparedStatement stmtEstado = conn.prepareStatement(sqlEstado);
                        stmtEstado.setString(1, numbers);
                        stmtEstado.setInt(2, idTalonary); // Pasa el ID del talonario
                        ResultSet resultState = stmtEstado.executeQuery();

                        if (resultState.next()) {
                            String stateNumber = resultState.getString("estado");
                            if (stateNumber.equals("R")) {            //estadoNumero == 'R'
                                numberButton.setBackground(Color.yellow); // estado reservado
                            } else if (stateNumber.equals("P")) {
                                numberButton.setBackground(Color.RED); // estado pagado
                            } else {
                                numberButton.setBackground(Color.GRAY); // estado disponible
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                numberButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JButton buttonPressed = (JButton) e.getSource();
                        if (buttonPressed.getBackground().equals(Color.yellow)) {
                            int option = JOptionPane.showConfirmDialog(null, "Deseas pagar los numeros");
                            if (option == JOptionPane.YES_OPTION) {
                                JOptionPane.showMessageDialog(null, "NUMEROS PAGADOS");
                                int numberSelected = Integer.parseInt(buttonPressed.getText());
                                changeState(numberSelected);
                            } else {
                                JOptionPane.showMessageDialog(null, "NO CANCELASTE MALA PAGA");
                            }
                        } else if (buttonPressed.getBackground().equals(Color.RED)) {
                            JOptionPane.showMessageDialog(null, "ESTE NUMERO ESTA PAGADO");
                        } else {
                            buttonPressed.setBackground(Color.GRAY);
                            buttonsOfNumbers.add(buttonPressed);
                        }
                    }
                });
            }

            reserveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (buttonsOfNumbers.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar al menos un número antes de Reservar.");
                    } else {
                        String nameParticipant = JOptionPane.showInputDialog(null, "ingrese su nombre");
                        try (Connection conn = ConnectDatabase.getConnection()) {
                            var stmt = conn.prepareStatement("INSERT INTO participante (nombre_participante) VALUES (?)");
                            stmt.setString(1, nameParticipant);
                            stmt.executeUpdate();
                            reserveNumber(nameParticipant);
                        } catch (SQLException es) {
                            es.printStackTrace();
                        }
                    }
                }
            });
        }
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (buttonsOfNumbers.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar al menos un número antes de pagar.");
                } else {
                    String nameParticipant = JOptionPane.showInputDialog(null, "ingrese su nombre");
                    try (Connection conn = ConnectDatabase.getConnection()) {
                        var stmt = conn.prepareStatement("INSERT INTO participante (nombre_participante) VALUES (?)");
                        stmt.setString(1, nameParticipant);
                        stmt.executeUpdate();
                        paid(nameParticipant);
                    } catch (SQLException es) {
                        es.printStackTrace();
                    }
                }
            }
        });

        raffleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (verify) {
                    raffleNumbers();
                    verify = false;
                } else {
                    JOptionPane.showMessageDialog(null, "Ya se rifo esta rifa");
                }
            }
        });
    }

    public void reserveNumber(String nameParticipant) {
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarySelected.getTalonarioSelected();
        try (Connection conn = ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            for (int i = 0; i < buttonsOfNumbers.size(); i++) {
                int numberSelected = Integer.parseInt(buttonsOfNumbers.get(i).getText());
                stmt.setInt(1, numberSelected); // Pasa el número del talonario
                stmt.setString(2, "R"); // Pasa el estado
                stmt.setString(3, nameParticipant); // Pasa el nombre del participante
                stmt.setString(4, opcionSelected); // Pasa el nombre del talonario (esto se tiene que conseguir del panel el nombre del talonario)
                stmt.execute(); // Ejecuta el procedimiento almacenado
            }
            JOptionPane.showMessageDialog(null, "Numeros reservados con exito");
            BooksOfLottery view3 = new BooksOfLottery();
            view3.openBookOfLottery();
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void paid(String nameParticipant) {
        String sql = "{ call insertar_talonario(?, ?, ?, ?) }"; // Llamar al procedimiento almacenado
        String opcionSelected = TalonarySelected.getTalonarioSelected();
        try (Connection conn = ConnectDatabase.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {
            for (int i = 0; i < buttonsOfNumbers.size(); i++) {
                int numerSelect = Integer.parseInt(buttonsOfNumbers.get(i).getText());
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

    public void changeState(int numeroSeleccionado) {
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

    public void raffleNumbers() {
        int sizeNums = sizeNumButtons;
        int numWin = (int) (Math.random() * sizeNums + 1);
        String lotterySelected = TalonarySelected.getTalonarioSelected();
        String stateTalonary = getStateTalonaryI(numWin, lotterySelected);

        if (stateTalonary.equals("P")) {
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
        } else if (stateTalonary.equals("R")) {
            JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero solo fue reservado\nAsí que no existe un participante ganador");
        } else if (stateTalonary.equals("default")) {
            JOptionPane.showMessageDialog(null, "El numero ganador es:  " + numWin + "\nEl numero no fue comprado ni reservado\nAsí que no existe participante ganador");
        }
    }

    public int getIdTalonary(String nameLottery) {
        int idTalonary = 0;
        try (Connection conn = ConnectDatabase.getConnection()) {
            String sql = "SELECT id FROM talonario WHERE nombre = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameLottery);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                idTalonary = result.getInt("id"); // Cambiado a "id" en lugar de "id_talonario"
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del talonario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return idTalonary;
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

    private String getStateTalonaryI(int numWin, String lotterySelected) {
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

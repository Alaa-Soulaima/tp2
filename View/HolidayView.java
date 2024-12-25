package View;

import javax.swing.*;
import java.awt.*;

public class HolidayView extends JFrame {
    public JTable holidayTable;
    public JButton addButton, deleteButton, modifyButton, switchViewButton;
    public JComboBox<String> employeeNameComboBox;
    public JTextField startDateField, endDateField;
    public JComboBox<String> typeCombo;

    public HolidayView() {
        setTitle("Gestion des Congés");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.add(new JLabel("Employé Nom Complet:"));
        employeeNameComboBox = new JComboBox<>();
        inputPanel.add(employeeNameComboBox);

        inputPanel.add(new JLabel("Date Début:"));
        startDateField = new JTextField();
        inputPanel.add(startDateField);

        inputPanel.add(new JLabel("Date Fin:"));
        endDateField = new JTextField();
        inputPanel.add(endDateField);

        inputPanel.add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(new String[]{"CONGE_PAYE", "CONGE_MALADIE", "CONGE_NON_PAYE"});
        inputPanel.add(typeCombo);

        add(inputPanel, BorderLayout.NORTH);

        holidayTable = new JTable();
        add(new JScrollPane(holidayTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Ajouter");
        buttonPanel.add(addButton);
        deleteButton = new JButton("Supprimer");
        buttonPanel.add(deleteButton);
        modifyButton = new JButton("Modifier");
        buttonPanel.add(modifyButton);

        switchViewButton = new JButton("Gérer les Employés");
        buttonPanel.add(switchViewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

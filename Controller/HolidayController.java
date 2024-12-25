package Controller;

import DAO.DBConnection;
import DAO.HolidayDAOImpl;
import Model.Holiday;
import Model.Type;
import View.HolidayView;

import javax.swing.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HolidayController {
    private final HolidayView view;
    private final HolidayDAOImpl dao;

    public HolidayController(HolidayView view) {
        this.view = view;
        this.dao = new HolidayDAOImpl();

        loadEmployeeNames();
        refreshHolidayTable();

        view.addButton.addActionListener(e -> addHoliday());
        view.deleteButton.addActionListener(e -> deleteHoliday());
        view.modifyButton.addActionListener(e -> modifyHoliday());

        view.holidayTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = view.holidayTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) view.holidayTable.getValueAt(selectedRow, 0);
                    String employeeName = (String) view.holidayTable.getValueAt(selectedRow, 1);
                    String startDate = (String) view.holidayTable.getValueAt(selectedRow, 2);
                    String endDate = (String) view.holidayTable.getValueAt(selectedRow, 3);
                    Type type = Type.valueOf(view.holidayTable.getValueAt(selectedRow, 4).toString());

                    view.employeeNameComboBox.setSelectedItem(employeeName);
                    view.startDateField.setText(startDate);
                    view.endDateField.setText(endDate);
                    view.typeCombo.setSelectedItem(type.toString());
                    view.modifyButton.setActionCommand(String.valueOf(id));
                }
            }
        });
    }

    private void loadEmployeeNames() {
        view.employeeNameComboBox.removeAllItems();
        List<String> names = dao.getAllEmployeeNames();

        for (String name : names) {
            view.employeeNameComboBox.addItem(name);
        }
    }

    private void refreshHolidayTable() {
        List<Holiday> holidays = dao.listAll();
        String[] columnNames = {"ID", "Employé", "Date Début", "Date Fin", "Type"};
        Object[][] data = new Object[holidays.size()][5];

        for (int i = 0; i < holidays.size(); i++) {
            Holiday h = holidays.get(i);
            data[i] = new Object[]{h.getId(), h.getEmployeeName(), h.getStartDate(), h.getEndDate(), h.getType()};
        }

        view.holidayTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return end.isAfter(start);
    }

    private void addHoliday() {
        try {
            String employeeName = (String) view.employeeNameComboBox.getSelectedItem();
            String startDate = view.startDateField.getText();
            String endDate = view.endDateField.getText();
            Type type = Type.valueOf(view.typeCombo.getSelectedItem().toString().toUpperCase());
            
            int employeeId = getEmployeeIdByName(employeeName);
            if (hasHolidayConflict(employeeId, startDate, endDate)) {
                JOptionPane.showMessageDialog(view, "Cet employé a déjà un congé dans cette période.");
                return;
            }

            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                throw new IllegalArgumentException("Les dates doivent être au format YYYY-MM-DD.");
            }

            if (!isEndDateAfterStartDate(startDate, endDate)) {
                throw new IllegalArgumentException("La date de fin doit être supérieure à la date de début.");
            }

            Holiday holiday = new Holiday(employeeName, startDate, endDate, type);
            dao.add(holiday);
            refreshHolidayTable();
            JOptionPane.showMessageDialog(view, "Congé ajouté avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur : " + ex.getMessage());
        }
    }

    private int getEmployeeIdByName(String employeeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void modifyHoliday() {
        try {
            String actionCommand = view.modifyButton.getActionCommand();
            if (actionCommand != null && !actionCommand.trim().isEmpty()) {
                int id = Integer.parseInt(actionCommand.trim());

                String employeeName = (String) view.employeeNameComboBox.getSelectedItem();
                String startDate = view.startDateField.getText();
                String endDate = view.endDateField.getText();
                Type type = Type.valueOf(view.typeCombo.getSelectedItem().toString().toUpperCase());

                if (!isValidDate(startDate) || !isValidDate(endDate)) {
                    throw new IllegalArgumentException("Les dates doivent être au format YYYY-MM-DD.");
                }

                if (!isEndDateAfterStartDate(startDate, endDate)) {
                    throw new IllegalArgumentException("La date de fin doit être supérieure à la date de début.");
                }

                Holiday holiday = new Holiday(employeeName, startDate, endDate, type);
                dao.update(holiday, id);
                refreshHolidayTable();
                JOptionPane.showMessageDialog(view, "Congé modifié avec succès.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur : " + ex.getMessage());
        }
    }
    
    private boolean hasHolidayConflict(int employeeId, String startDate, String endDate) {
        String sql = "SELECT COUNT(*) FROM holiday WHERE employeeId = ? AND ((startDate <= ? AND endDate >= ?) OR (startDate <= ? AND endDate >= ?))";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, endDate);
            stmt.setString(3, startDate);
            stmt.setString(4, endDate);
            stmt.setString(5, startDate);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if there is a conflict
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteHoliday() {
        try {
            String input = JOptionPane.showInputDialog(view, "Veuillez entrer l'ID du congé à supprimer:");
            if (input != null && !input.trim().isEmpty()) {
                int id = Integer.parseInt(input.trim());

                int confirm = JOptionPane.showConfirmDialog(view, 
                        "Êtes-vous sûr de vouloir supprimer le congé avec l'ID " + id + " ?", 
                        "Confirmation de suppression", 
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    dao.delete(id);
                    refreshHolidayTable();
                    JOptionPane.showMessageDialog(view, "Congé supprimé avec succès.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur : " + ex.getMessage());
        }
    }
}

package Controller;

import DAO.GenericDAO;
import DAO.EmployeeDAOImpl;
import Model.Employee;
import Model.Poste;
import Model.Role;
import View.EmployeeView;
import View.HolidayView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmployeeController {
    private final EmployeeView view;
    private final GenericDAO<Employee> dao;
    private final HolidayView holidayView;

    public EmployeeController(EmployeeView view, HolidayView holidayView) {
        this.view = view;
        this.dao = new EmployeeDAOImpl();
        this.holidayView = holidayView;

        // Listener for the Add button
        view.addButton.addActionListener(e -> addEmployee());

        // Listener for the List button
        view.listButton.addActionListener(e -> listEmployees());

        // Listener for the Delete button
        view.deleteButton.addActionListener(e -> deleteEmployee());

        // Listener for the Modify button
        view.modifyButton.addActionListener(e -> modifyEmployee());

        // ActionListener for the "Manage Holidays" button
        view.switchViewButton.addActionListener(e -> {
            view.setVisible(false);
            holidayView.setVisible(true);
        });

        // Add selection listener on the employee table
        view.employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = view.employeeTable.getSelectedRow();
                if (selectedRow != -1) {
                    // Get data from the selected row
                    int id = (int) view.employeeTable.getValueAt(selectedRow, 0);
                    String nom = (String) view.employeeTable.getValueAt(selectedRow, 1);
                    String prenom = (String) view.employeeTable.getValueAt(selectedRow, 2);
                    String email = (String) view.employeeTable.getValueAt(selectedRow, 3);
                    String phone = (String) view.employeeTable.getValueAt(selectedRow, 4);
                    double salaire = (double) view.employeeTable.getValueAt(selectedRow, 5);
                    Role role = Role.valueOf(view.employeeTable.getValueAt(selectedRow, 6).toString());
                    Poste poste = Poste.valueOf(view.employeeTable.getValueAt(selectedRow, 7).toString());
                  

                    // Populate the modification fields with the selected row's values
                    view.nameField.setText(nom);
                    view.surnameField.setText(prenom);
                    view.emailField.setText(email);
                    view.phoneField.setText(phone);
                    view.salaryField.setText(String.valueOf(salaire));
                    view.roleCombo.setSelectedItem(role.toString());
                    view.posteCombo.setSelectedItem(poste.toString());
                    

                    // Save the employee ID in the modify button's action command for update
                    view.modifyButton.setActionCommand(String.valueOf(id));
                }
            }
        });

        // Load the list of employees at startup
        listEmployees();
    }

    // Method to add an employee with validation
    private void addEmployee() {
        try {
            String nom = view.nameField.getText().trim();
            String prenom = view.surnameField.getText().trim();
            String email = view.emailField.getText().trim();
            String phone = view.phoneField.getText().trim();
            String salaireText = view.salaryField.getText().trim();
            

            // Field validation
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || phone.isEmpty() || salaireText.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tous les champs sont obligatoires.");
                return;
            }
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(view, "Veuillez entrer une adresse email valide.");
                return;
            }
            double salaire = Double.parseDouble(salaireText);

            Role role = Role.valueOf(view.roleCombo.getSelectedItem().toString().toUpperCase());
            Poste poste = Poste.valueOf(view.posteCombo.getSelectedItem().toString().toUpperCase());

            Employee employee = new Employee(nom, prenom, email, phone, salaire, role, poste);
            dao.add(employee);
            JOptionPane.showMessageDialog(view, "Employé ajouté avec succès.");
            listEmployees(); // Refresh the list
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Salaire invalide.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    private void listEmployees() {
        List<Employee> employees = dao.listAll();
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Salaire", "Rôle", "Poste"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Employee emp : employees) {
            Object[] row = {emp.getId(), emp.getNom(), emp.getPrenom(), emp.getEmail(), emp.getPhone(), emp.getSalaire(), emp.getRole(), emp.getPoste()};
            model.addRow(row);
        }

        view.employeeTable.setModel(model);
    }

    private void deleteEmployee() {
        try {
            String idInput = JOptionPane.showInputDialog(view, 
                    "Entrez l'ID de l'employé à supprimer :", 
                    "Suppression d'un employé", 
                    JOptionPane.QUESTION_MESSAGE);

            if (idInput == null || idInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Aucun ID saisi. Suppression annulée.");
                return;
            }

            int id = Integer.parseInt(idInput.trim());

            int confirm = JOptionPane.showConfirmDialog(view, 
                    "Êêtes-vous sûr de vouloir supprimer l'employé avec l'ID " + id + " ?", 
                    "Confirmation de suppression", 
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dao.delete(id);
                JOptionPane.showMessageDialog(view, "Employé supprimé avec succès.");
                listEmployees();
            } else {
                JOptionPane.showMessageDialog(view, "Suppression annulée.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID invalide. Veuillez entrer un nombre valide.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur : " + ex.getMessage());
        }
    }

    private void modifyEmployee() {
        try {
            String actionCommand = view.modifyButton.getActionCommand();
            if (actionCommand != null && !actionCommand.trim().isEmpty()) {
                int id = Integer.parseInt(actionCommand.trim());

                String nom = view.nameField.getText().trim();
                String prenom = view.surnameField.getText().trim();
                String email = view.emailField.getText().trim();
                String phone = view.phoneField.getText().trim();
                String salaireText = view.salaryField.getText().trim();
                

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || phone.isEmpty() || salaireText.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Tous les champs sont obligatoires.");
                    return;
                }
                if (!email.contains("@")) {
                    JOptionPane.showMessageDialog(view, "Veuillez entrer une adresse email valide.");
                    return;
                }
                double salaire = Double.parseDouble(salaireText);

                Role role = Role.valueOf(view.roleCombo.getSelectedItem().toString().toUpperCase());
                Poste poste = Poste.valueOf(view.posteCombo.getSelectedItem().toString().toUpperCase());

                Employee updatedEmployee = new Employee(nom, prenom, email, phone, salaire, role, poste);
                dao.update(updatedEmployee, id);

                JOptionPane.showMessageDialog(view, "Employé mis à jour avec succès.");
                listEmployees();
            } else {
                JOptionPane.showMessageDialog(view, "Veuillez sélectionner un employé à modifier.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Salaire invalide.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }
}

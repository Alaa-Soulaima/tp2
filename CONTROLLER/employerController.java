package CONTROLLER;



import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import DAO.EmployeImpl;
import model.Employe;
import model.Employe.Poste;
import model.Employe.Role;

import model.Employemodel;
import view.Employeview;

public class employerController {
private Employemodel model;
private Employeview view;

public employerController(Employemodel model,Employeview view) {
	this.model=model;
	this.view=view;
	this.view.btnAjouter.addActionListener(e->addEmploye());
	this.view.btnModifier.addActionListener(e->updateEmploye());
	this.view.btnAfficher.addActionListener(e -> afficherEmploye());
	this.view.btnSupprimer.addActionListener(e -> supprimerEmploye());
	


}
private void addEmploye() {
	String nom=view.getNom();
	String prenom=view.getPrenom();
	String email=view.getEmail();
	String telephone=view.getTelephone();
	double salaire =view.getSalaire();
	Poste poste=view.getPoste();
	Role role=view.getRole();
	
	

	boolean addEmploye=model.addEmploye(nom, prenom, email, telephone,salaire, role, poste);
	if(addEmploye) System.out.println("Employe ajoute avec Succes");
	else System.out.println("Echec d'ajout d'employe !!!!!");
}



	


	  private void updateEmploye() {
		int selectedRow = view.table.getSelectedRow();
		//int id =view.getId(view.table);
	    int id = (int) view.table.getValueAt(selectedRow, 0);
		
		String nom=view.getNom();
		String prenom=view.getPrenom();
		String email=view.getEmail();
		String telephone=view.getTelephone();
		double salaire =view.getSalaire();
		Poste poste=view.getPoste();
		Role role=view.getRole();
		
		  Employe employe = new Employe(nom, prenom, email, telephone, salaire, role, poste);
		    EmployeImpl employeImpl = new EmployeImpl();

		    employeImpl.update(employe,id);
		   // JOptionPane.showMessageDialog(null, "Employé modifié avec succès !");
		     
		

		  
		}

public void afficherEmploye() {
 EmployeImpl  EmployerDAOimpl = new  EmployeImpl();
	    List<Employe> employes =  EmployerDAOimpl.findAll();

	   DefaultTableModel model = (DefaultTableModel) view.table.getModel();
	    model.setRowCount(0);

	    for (Employe employe : employes) {
	        model.addRow(new Object[]{
	        	employe.getId(),
	            employe.getNom(),
	            employe.getPrenom(),
	            employe.getEmail(),
	            employe.getTelephone(),
	            employe.getSalaire(),
	            employe.getRole(),
	            employe.getPoste()
	        });
	    }
	}
public void supprimerEmploye() {
int selectedRow = view.table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner un employé à supprimer !");
        return;
    }
    int id =view.getId(view.table);
    //int id = (int) view.table.getValueAt(selectedRow, 0);
    EmployeImpl  EmployerDAOimpl = new  EmployeImpl();

    int confirmation = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer cet employé ?", "Confirmation", JOptionPane.YES_NO_OPTION);
    if (confirmation == JOptionPane.YES_OPTION) {
        EmployerDAOimpl.delete(id);
        JOptionPane.showMessageDialog(null, "Employé supprimé avec succès !");
      
    }
}  
}
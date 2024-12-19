package MAIN;

import DAO.EmployeImpl;
import model.Employemodel;
import view.Employeview;
import CONTROLLER.employerController;

public class main {

	public static void main(String[] args) {
		Employeview View = new Employeview();
		EmployeImpl DAO =new EmployeImpl();
		Employemodel model = new Employemodel(DAO);
		new employerController(model,View);
		
		View.setVisible(true);
		
	}

}
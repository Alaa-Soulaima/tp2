package model;

import javax.management.relation.Role;

import DAO.EmployeImpl;
import model.Employe.Poste;

public class Employemodel {
  private EmployeImpl dao;

  public Employemodel(EmployeImpl dao) {
	this.dao=dao;
}

//logique Metier
public boolean addEmploye(String nom,String pronom,String email,String telephone,double salaire,model.Employe.Role role,Poste poste) {
	if(salaire<=0) {
		System.out.println("Le salaire doit etre superieur de 0 !!!!!");
	   return false;}
	
	if (email == null || !email.contains("@")) {
        System.out.println("L'email n'est pas valide !");
        return false;
    }	
	
	Employe NvEmploye = new Employe(nom,pronom,email,telephone,salaire,role,poste);
	dao.add(NvEmploye);
return true;	

}}
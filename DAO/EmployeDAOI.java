package DAO;

import java.util.List;

import model.Employe;
import model.Employe.Poste;
import model.Employe.Role;

public interface EmployeDAOI {
	
Employe findById(int employeId);
List<Employe> findAll();
void add(Employe E);
void update(Employe E,int id);
void delete(int id);
List<Role>findAllRoles();
List<Poste>findAllPostes();
void updateSpecificField(int id, String fieldName, Object newValue);


}
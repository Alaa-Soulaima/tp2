package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
public class connexion {
public static final String url = "jdbc:mysql://localhost:3306/employe";
public static final String user = "root";
public static final String password = "";
public static Connection conn =null;

public static Connection getConnexion() {
	if(conn != null) {
		try {
			conn = DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
			System.out.println("Erreur de connexion !!!!!");	
			}
	}
	return conn;
	}


	public static void closeConnexion() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("Erreur de connexion !!!!!");	
				}
		}
	}}*/
public class Connexion {
    public static final String url = "jdbc:mysql://localhost:3306/employe";
    public static final String user = "root";
    public static final String password = "";
    private static Connection conn = null;

    public static Connection getConnexion() {
        if (conn == null) { // Vérifie si la connexion est null avant de l'établir
            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connexion établie avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion !!!!!");
               // e.printStackTrace(); // Fournit plus de détails sur l'erreur
            }
        }
        return conn;
    }

    public static void closeConnexion() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Réinitialise conn après fermeture
                System.out.println("Connexion fermée avec succès !");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion !!!!!");
                //e.printStackTrace(); // Fournit plus de détails sur l'erreur
            }
        }
    }
}
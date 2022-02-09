package objectclasses;

import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Employee extends Account {

	boolean isAdmin;
	String fname;
	String lname;
	String username;
	String password;
	
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public Employee(String FName, String LName, String phone, String email, String username, boolean isAdmin) {
		super(FName, LName, phone, email);
		this.isAdmin = isAdmin;
		this.username = username;	
	}
	
	public String hashPassword(String plainTextPassword){
	    return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	} 

	public boolean createEmployee() throws SQLException {
		String sqlQuery = "INSERT INTO Employee (emp_Fname, emp_lname, cust_Phone, cust_Email)" + " VALUES ('"
				+ this.getFName() + "', '" + this.getLName() + "', '" + this.getPhone() + "', '" + this.getEmail()
				+ "');";
		int result = Controller.connection().executeUpdate(sqlQuery);

		if (result != 0) {
			System.out.println("Employee Created");
			Controller.connection().close();
			return true;
		} else {
			System.out.println("Oops, something went wrong!");
			Controller.connection().close();
			return false;
		}
	}

	public static boolean createEmployee(String fname, String lname, String username, String password, boolean admin)
			throws SQLException {
		int i = 0;
		if (admin) {
			i = 1;
		}
		String sqlQuery = "INSERT INTO Employee (emp_Fname, emp_lname, admin, username, password) VALUES ('" + fname
				+ "', '" + lname + "', " + i + ", '" + username + "', '" + password + "');";
		int result = Controller.connection().executeUpdate(sqlQuery);

		if (result != 0) {
			System.out.println("Employee Created");
			Controller.connection().close();
			return true;
		} else {
			System.out.println("Oops, something went wrong!");
			Controller.connection().close();
			return false;
		}
	}

}

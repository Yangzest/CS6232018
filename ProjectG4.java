import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * Group 4  The depot d1 changes its name to dd1 in Depot and Stock.
 */

public class ProjectG4 {

	public static void main(String args[]) throws SQLException, IOException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connected to Driver!\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		Connection conn = null;
		try {
			//For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. 
			//You need either to explicitly disable SSL by setting useSSL=false
			 conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "DaGu2218379"); 
			 System.out.println("Connected to MySQL Database!\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		// For automicity
		conn.setAutoCommit(false);
		
		// For isolation 
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE); 
		
		Statement stmt = null;
		
		stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS Stock CASCADE");
		stmt.executeUpdate("DROP TABLE IF EXISTS Product CASCADE");
		stmt.executeUpdate("DROP TABLE IF EXISTS Depot CASCADE");
		
		//Create Table
		stmt.executeUpdate("Create table Product("
				+ "prod_id CHAR(10),"
				+ "pname VARCHAR(128),"
				+ "price FLOAT,"
				+ "PRIMARY KEY (prod_id),"
				+ "CHECK (price > 0)"
				+ ")");
		stmt.executeUpdate("Create table Depot("
				+ "dep_id CHAR(10),"
				+ "addr VARCHAR(128),"
				+ "volume INTEGER,"
				+ "PRIMARY KEY (dep_id),"
				+ "CHECK (volume >= 0)"
				+ ")");
		stmt.executeUpdate("Create table Stock("
				+ "prod_id CHAR(10),"
				+ "dep_id CHAR(10),"
				+ "quantity INTEGER,"
				+ "PRIMARY KEY (prod_id, dep_id),"
				+ "FOREIGN KEY (prod_id) REFERENCES Product (prod_id) ON UPDATE CASCADE," 
				+ "FOREIGN KEY (dep_id) REFERENCES Depot (dep_id) ON UPDATE CASCADE"
				+ ")");
		
		try {
			stmt.executeUpdate("INSERT INTO Product (prod_id, pname, price) Values" 
					+ "('p1', 'tape', 2.5)," 
					+ "('p2', 'tv', 250), "
					+ "('p3', 'vcr', 80);");
			stmt.executeUpdate("INSERT INTO Depot (dep_id, addr, volume) Values" 
					+ "('d1', 'New Yrok', 9000)," 
					+ "('d2', 'Syracuse', 6000), "
					+ "('d4', 'New Yrok', 2000);");
			stmt.executeUpdate("INSERT INTO Stock (prod_id, dep_id, quantity) Values" 
					+ "('p1', 'd1', 1000)," 
					+ "('p1', 'd2', -100)," 
					+ "('p1', 'd4', 1200)," 
					+ "('p3', 'd1', 3000)," 
					+ "('p3', 'd4', 2000)," 
					+ "('p2', 'd4', 1500)," 
					+ "('p2', 'd1', -400)," 
					+ "('p2', 'd2', 2000);");

			System.out.println("Table");
			ResultSet rs1 = stmt.executeQuery("select * from Product");
			System.out.println("Table Product");
			System.out.println(" prod_id  " + "pname " + " price ");
			while(rs1.next()) {
				System.out.println( rs1.getString("prod_id") 
						 + "\t " + rs1.getString("pname")
						 + "\t " + rs1.getInt("price"));
			} 
			ResultSet rs2 = stmt.executeQuery("select * from Depot");
			System.out.println("Table Depot");
			System.out.println(" dep_id " + "addr " + " volume ");
			while(rs2.next()) {
				System.out.println( rs2.getString("dep_id") 
						 + "\t " + rs2.getString("addr")
						 + "\t " + rs2.getInt("volume"));
			} 
			
			ResultSet rs3 = stmt.executeQuery("select * from Stock");
			System.out.println("\nTable Stock");
			System.out.println("Prod_Id  " + "Dep_Id " + " Quantity ");
			while(rs3.next()) {
				System.out.println(rs3.getString("Prod_Id") 
						+ "\t " + rs3.getString("Dep_Id") 
						+ "\t " + rs3.getInt("quantity"));
			} 
			
			stmt.executeUpdate("Update Depot SET dep_id = 'dd1' where dep_id = 'd1'");
			
			System.out.println("\nUpdated table");
			ResultSet rs4 = stmt.executeQuery("select * from Depot");
			System.out.println("Table Depot");
			System.out.println(" dep_id " + "addr " + " volume ");
			while(rs4.next()) {
				System.out.println( rs4.getString("dep_Id") 
						+ "\t " + rs4.getString("addr") 
						+ "\t " + rs4.getInt("volume"));
			} 
			
			ResultSet rs5 = stmt.executeQuery("select * from Stock");
			System.out.println("\nTable Stock");
			System.out.println("Prod_Id  " + "Dep_Id " + " Quantity ");
			while(rs5.next()) {
				System.out.println(rs5.getString("Prod_Id") 
						+ "\t " + rs5.getString("Dep_Id") 
						+ "\t " + rs5.getInt("quantity"));
			}
			conn.commit();	
		} catch (SQLException e) {
			System.out.println("catch Exception: \n" + e.toString());
			conn.rollback();
		} finally{
			stmt.close();
			conn.close();
		}
	}
}
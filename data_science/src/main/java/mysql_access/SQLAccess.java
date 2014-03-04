package mysql_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLAccess {

	private Connection connect = null;
	private String host = null;
	private String database = null;
	private String user = null;
	private String pw = null;

	public SQLAccess(String host, String database, String user, String pw) {
		super();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + 
					host + "/" + database + "?user=" + user + "&password=" + pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createUserTable() throws SQLException {

		String sql = "CREATE TABLE USERS " +
				"(user VARCHAR(255) not NULL, " +
				" pw VARCHAR(255) not NULL, " + 
				" auth_code VARCHAR(255) not NULL, " + 
				" authenticated boolean not NULL, " + 
				" PRIMARY KEY ( user ))"; 
		PreparedStatement ps = connect.prepareStatement(sql);  
		ps.executeUpdate(sql);
		ps.close();
	}
	
	public void deleteUserTable () throws SQLException {
		String sql = "drop TABLE USERS"; 
		PreparedStatement ps = connect.prepareStatement(sql);  
		ps.executeUpdate(sql);
		ps.close();
	}

	public Connection getConnect() {
		return connect;
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
} 
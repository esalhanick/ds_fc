package mysql_access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

	String user_name = new String();
	String pw = new String();
	String auth_code = new String();
	boolean authenticated;

	public User() {
		super();		
	}

	public User(String user_name, String pw, String auth_code, boolean authenticated) {
		super();
		this.user_name = user_name;
		this.pw = pw;
		this.auth_code = auth_code;
		this.authenticated = authenticated;
	}

	public void addUserToDB(SQLAccess db) throws SQLException {
		String sql = "INSERT INTO USERS (USER, PW, AUTH_CODE, AUTHENTICATED) VALUES (?, ?, ?, ?)";
		PreparedStatement ps = db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ps.setString(2, this.pw);
		ps.setString(3, this.auth_code);
		ps.setBoolean(4, this.authenticated);
		ps.executeUpdate();
		ps.close();	
	}

	public void removeUserFromDB(SQLAccess db) throws SQLException {
		String sql = "delete from USERS WHERE USER = ?";
		PreparedStatement ps = db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ps.executeUpdate();
		ps.close();	
	}

	public boolean existsInDB(SQLAccess db) throws SQLException {
		String sql = "SELECT * FROM USERS WHERE USER = ?";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			rs.close();
			return true;
		}
		return false;
	}

	public boolean isAuthenticatedInDB(SQLAccess db) throws SQLException {
		String sql = "SELECT * FROM USERS WHERE USER = ? AND AUTHENTICATED = TRUE";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}

	public void authenticateInDB(SQLAccess db) throws SQLException {

		String sql = "UPDATE USERS SET AUTHENTICATED = TRUE WHERE user = ?";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ps.executeUpdate();
	}

	public void unAuthenticateInDB(SQLAccess db) throws SQLException {

		String sql = "UPDATE USERS SET AUTHENTICATED = FALSE WHERE user = ?";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ps.executeUpdate();
	}

	public String getPasswordFromDB(SQLAccess db) throws SQLException {
		String sql = "SELECT PW FROM USERS WHERE USER = ?";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ResultSet rs = ps.executeQuery();
		
		String password = null;
		while (rs.next()) {
			password = rs.getString("pw");	
		}
		return password;
	}
	
	public String getAuthcodeFromDB(SQLAccess db) throws SQLException {
		String sql = "SELECT AUTH_CODE FROM USERS WHERE USER = ?";
		PreparedStatement ps =  db.getConnect().prepareStatement(sql);
		ps.setString(1, this.user_name);
		ResultSet rs = ps.executeQuery();
		
		String code = null;
		while (rs.next()) {
			code = rs.getString("auth_code");	
		}
		return code;
	}

	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getAuth_code() {
		return auth_code;
	}
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}
	public boolean isAuthenticated() {
		return authenticated;
	}
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}

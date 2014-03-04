package test;

import mysql_access.SQLAccess;
import mysql_access.User;

public class SQL_test {

	public static void main(String[] args) throws Exception {
		
		SQLAccess db = new SQLAccess("localhost", "test", "newuser", "password");
		db.deleteUserTable();
		db.createUserTable();
		
		User user1 = new User("bob@bah.com", "my_pw", "ABCD", false);
		User user2 = new User("joe@bah.com", "other_pw", "1234", false);
		
		System.out.println("user1 in DB: " + user1.existsInDB(db));
		System.out.println("user2 in DB: " + user2.existsInDB(db));
		
		System.err.println("Add user1 to db.");
		user1.addUserToDB(db);
		
		System.out.println("user1 in DB: " + user1.existsInDB(db));
		System.out.println("user2 in DB: " + user2.existsInDB(db));
		
		System.err.println("Add user2 to db.");
		user2.addUserToDB(db);
		
		System.out.println("user1 in DB: " + user1.existsInDB(db));
		System.out.println("user2 in DB: " + user2.existsInDB(db));
		
		System.out.println("user1 authenticated in DB: " + user1.isAuthenticatedInDB(db));
		
		System.err.println("Authenicating user1 in DB.");
		user1.authenticateInDB(db);
		System.out.println("user1 authenticated in DB: " + user1.isAuthenticatedInDB(db));
		System.out.println("user2 authenticated in DB: " + user2.isAuthenticatedInDB(db));
		
		System.out.println("user1 password : " + user1.getPasswordFromDB(db));
		System.out.println("user1 auth code : " + user1.getAuthcodeFromDB(db));
		
		System.out.println("user2 password : " + user2.getPasswordFromDB(db));
		System.out.println("user2 auth code : " + user2.getAuthcodeFromDB(db));
		
		System.err.println("Un-authenicating user1 in DB.");
		user1.unAuthenticateInDB(db);
		System.out.println("user1 authenticated in DB: " + user1.isAuthenticatedInDB(db));
		
		System.err.println("Removing user1 in DB.");
		user1.removeUserFromDB(db);
		
		System.out.println("user1 in DB: " + user1.existsInDB(db));
		System.out.println("user2 in DB: " + user2.existsInDB(db));
		
		db.getConnect().close();
	}
}

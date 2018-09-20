import database.DbUserHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
//import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoDatabase;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DbUserTests {
	

	/*
	 * Tests whether a user was successfully added to the database
	 */
	@Test
	public void testInsertUser() throws Exception {
		MongoClient mongoClient2 = new MongoClient("localhost", 27017);
		DbUserHandler dbuh = new DbUserHandler(mongoClient2);
		
		
		dbuh.insertUser("testing_database_user", "password", "9/1/2018", "12/18/2018", "black");
		Document insertedUser = dbuh.getUserByUsername("testing_database_user");
		dbuh.deleteUserByUsername("testing_database_user");
		
		mongoClient2.close();
		
		assertNotNull(insertedUser);
	}
	
	/*
	 * Tests whether the database successfully deletes a user.
	 */
	@Test
	public void testDeleteUser() throws Exception {
		MongoClient mongoClient2 = new MongoClient("localhost", 27017);
		DbUserHandler dbuh = new DbUserHandler(mongoClient2);
		
		dbuh.insertUser("testing_database_user", "password", "9/1/2018", "12/18/2018", "black");
		dbuh.deleteUserByUsername("testing_database_user");
		Document insertedUser = dbuh.getUserByUsername("testing_database_user");
		
		mongoClient2.close();
		assertNull(insertedUser);
	}
	
	/*
	 * 
	 */
	@Test
	public void testGetAllUsers() throws Exception {
		MongoClient mongoClient2 = new MongoClient("localhost", 27017);
		DbUserHandler dbuh = new DbUserHandler(mongoClient2);
		
		MongoCollection<Document> collection = dbuh.getAllUsers();
		
		assertNotNull(collection);
	}
	
	
	
}
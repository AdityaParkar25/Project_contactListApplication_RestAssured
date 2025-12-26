package TelecomAPIProject;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ContactListAPITesting {

    static String token;
	static String contactID;
	
//======================
// Change the payload for TC 1,2,3,4 everytime before running all the tests
//======================

	// Test Case 1: Add New User
	@Test (priority = 1)
	public void TC01_addNewUser() {
		
		Response res = given()
							.contentType(ContentType.JSON)
							.body("""
									{
									    "firstName": "Tony",
                                         "lastName": "Stark",
                                         "email": "tonystark2@anime.com",
                                         "password": "starkPassword"
                                     }
									""")
					  .when().post("https://thinking-tester-contact-list.herokuapp.com/users")
					  .then().statusCode(201).statusLine(containsString("Created"))
					  .extract().response();
		token = res.jsonPath().getString("token");
		Assert.assertNotNull(token);
		
	}
	
	// Test Case 2: Get user Profile
	@Test (priority = 2)
	public void TC02_getUserProfile() {
		
		given().header("Authorization", "Bearer " + token)
		.when().get("https://thinking-tester-contact-list.herokuapp.com/users/me")
		.then().statusCode(200).statusLine(containsString("OK"))
		.body("email", equalTo("tonystark2@anime.com"));
	}
	
	// Test Case 3: Update User
	@Test (priority = 3)
	public void TC03_updateUser() {
		
		given().header("Authorization", "Bearer " + token)
		       .contentType(ContentType.JSON)
			   .body("""
						{
						    "firstName": "Steve",
                             "lastName": "Roger",
                             "email": "steveroger2@anime.com",
                             "password": "starkPassword"
                         }
						""")
	   .when().patch("https://thinking-tester-contact-list.herokuapp.com/users/me")
	   .then().statusCode(200).statusLine(containsString("OK"))
	   .body("email", equalTo("steveroger2@anime.com"));	   
	}
	
	// Test Case 4: Log In User
	@Test (priority = 4)
	public void TC04_loginUser() {
		
		Response res = given()
				.contentType(ContentType.JSON)
				.body("""
						{
                             "email": "steveroger2@anime.com",
                             "password": "starkPassword"
                         }
						""")
		  .when().post("https://thinking-tester-contact-list.herokuapp.com/users/login")
		  .then().statusCode(200).statusLine(containsString("OK"))
		  .extract().response();
        token = res.jsonPath().getString("token");
        Assert.assertNotNull(token);
	}
	
	// Test Case 5: Add Contact
	@Test (priority = 5)
	public void TC05_addContact() {
		
		Response res = given()
				.header("Authorization", "Bearer " + token)
				.contentType(ContentType.JSON)
				.body("""
						{
						    "firstName": "John",
							"lastName": "Doe",
							"birthdate": "1970-01-01",
							"email": "jdoe@fake.com",
							"phone": "8005555555",
							"street1": "1 Main St.",
							"street2": "Apartment A",
							"city": "Anytown",
							"stateProvince": "KS",
							"postalCode": "12345",
							"country": "USA"
                         }
						""")
		  .when().post("https://thinking-tester-contact-list.herokuapp.com/contacts")
		  .then().statusCode(201).statusLine(containsString("Created"))
		  .extract().response();
		
       contactID = res.jsonPath().getString("_id");
       Assert.assertNotNull(contactID);
	}
	
	// Test Case 6: Get Contact List
	@Test (priority = 6)
	public void TC06_getContactList() {
		
		given().header("Authorization", "Bearer " + token)
		.when().get("https://thinking-tester-contact-list.herokuapp.com/contacts")
		.then().statusCode(200).statusLine(containsString("OK"));
	}
	
	// Test Case 7: Get Contact
	@Test (priority = 7)
	public void TC07_getContact() {
		
		given().header("Authorization", "Bearer " + token)
		.when().get("https://thinking-tester-contact-list.herokuapp.com/contacts/"+ contactID)
		.then().statusCode(200).statusLine(containsString("OK"))
		.body("firstName", equalTo("John"));
	}
	
	// Test Case 8: Update Contact
	@Test (priority = 8)
	public void TC08_updateContactPut() {
		
		given()
					   .header("Authorization", "Bearer " + token)
					   .contentType(ContentType.JSON)
					   .body("""
							{
							    "firstName": "Amy",
							    "lastName": "Miller",
								"birthdate": "1992-02-02",
								"email": "amiller@fake.com",
								"phone": "8005554242",
								"street1": "13 School St.",
								"street2": "Apt. 5",
								"city": "Washington",
								"stateProvince": "QC",
								"postalCode": "A1A1A1",
								"country": "Canada"
			                 }
							""")
       .when().put("https://thinking-tester-contact-list.herokuapp.com/contacts/" + contactID)
       .then().statusCode(200).statusLine(containsString("OK"))
       .body("email", equalTo("amiller@fake.com"));
	}
	
	// Test Case 9: Update Contact
	@Test (priority = 9)
	public void TC09_updateContactPatch() {
		
		given().header("Authorization", "Bearer " + token)
	       .contentType(ContentType.JSON)
		   .body("""
					{
					    "firstName": "Anna"
                     }
					""")
		.when().patch("https://thinking-tester-contact-list.herokuapp.com/contacts/" + contactID)
		.then().statusCode(200).statusLine(containsString("OK"))
		.body("firstName", equalTo("Anna"));
	}
	
	// Test Case 10: Logout User
	@Test (priority = 10)
	public void TC10_logoutUser() {
		
		given().header("Authorization", "Bearer " + token)
		.when().post("https://thinking-tester-contact-list.herokuapp.com/users/logout")
		.then().statusCode(200).statusLine(containsString("OK"));
	}
	
}

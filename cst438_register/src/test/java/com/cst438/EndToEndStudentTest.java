package com.cst438;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndStudentTest {
	
	// Test Data
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/Users/jamha/Downloads/chromedriver_win32/chromedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String TEST_STUDENT_EMAIL = "st@csumb.edu";
	public static final String TEST_STUDENT_NAME = "Selenium Test";
	public static final int SLEEP_DURATION = 1000; // 1 second 
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		
		// if test student is already in the system, then delete the student.
		Student x = null;
		do {
			x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (x != null)
				studentRepository.delete(x);
		} while (x != null);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		ChromeOptions ops = new ChromeOptions();
		ops.addArguments("--remote-allow-origins=*");
		
		WebDriver driver = new ChromeDriver(ops);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// Select the Add Student Button
			WebElement we = driver.findElement(By.id("addStudentButton"));
			we.click();
			Thread.sleep(SLEEP_DURATION);

			// enter student info and click Add button
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check that successful toast message is displayed
			String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			assertEquals(toast_text, "Student successfully added!");
			Thread.sleep(SLEEP_DURATION);

			// Check repository for student
			Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			assertNotNull(student, "Test Student has been found in the database.");
			
		} catch(Exception ex) {
			throw ex;
		}
		// database clean-up
		Student s = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
		if (s != null)
			studentRepository.delete(s);
		driver.close();
		driver.quit();
	}
	
	

     //Test for correct error message when leaving name, 
	// email, or both fields blank when attempting to add a student to the system
	@Test
	public void addStudentTestBlankFields() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		ChromeOptions ops = new ChromeOptions();
		ops.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(ops);
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {

			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			// Select the Add Student Button
			WebElement we = driver.findElement(By.id("addStudentButton"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// Try to submit with both email and name fields blank
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check that toast error message shows correct blank field message
			String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			assertEquals(toast_text, "Name and Email fields must be entered!");
			Thread.sleep(SLEEP_DURATION);
			
			// Select the Add Student Button
			we = driver.findElement(By.id("addStudentButton"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check for just blank name field
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Same error message
			toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			assertEquals(toast_text, "Name and Email fields must be entered!");
			Thread.sleep(SLEEP_DURATION);
			
			// Select the Add Student Button
			we = driver.findElement(By.id("addStudentButton"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check for blank email field
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
			driver.findElement(By.xpath("//button[@id='Add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Same error message
			toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			assertEquals(toast_text, "Name and Email fields must be entered!");
			Thread.sleep(SLEEP_DURATION);
			

		} catch (Exception ex) {
			throw ex;
		} finally {
			driver.close();
			driver.quit();
		}

	}
	// check for the correct error message when trying to add an existing student (like addStudentTest)
	@Test
	public void addStudentTestExists() throws Exception {

			
			// if test student is already in system, then delete the student.
			Student x = null;
			do {
				x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
				if (x != null)
					studentRepository.delete(x);
			} while (x != null);
			
			System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
			ChromeOptions ops = new ChromeOptions();
			ops.addArguments("--remote-allow-origins=*");
			WebDriver driver = new ChromeDriver(ops);
			// Puts an Implicit wait for 10 seconds before throwing exception
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			try {

				driver.get(URL);
				Thread.sleep(SLEEP_DURATION);

				// Select the Add Student Button
				WebElement we = driver.findElement(By.id("addStudentButton"));
				we.click();
				Thread.sleep(SLEEP_DURATION);

				// enter student info and click Add button
				driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
				driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
				driver.findElement(By.xpath("//button[@id='Add']")).click();
				Thread.sleep(SLEEP_DURATION);
				
				// Check that successful toast message is displayed
				String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
				assertEquals(toast_text, "Student successfully added!");
				Thread.sleep(SLEEP_DURATION);

				// Check repository for student
				Student student = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
				assertNotNull(student, "Student has been found in database!");
				
				// Click the Add Student button again
				we = driver.findElement(By.id("addStudentButton"));
				we.click();
				Thread.sleep(SLEEP_DURATION);
				
				// Attempt to add the same student again
				driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_STUDENT_NAME);
				driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_STUDENT_EMAIL);
				driver.findElement(By.xpath("//button[@id='Add']")).click();
				Thread.sleep(SLEEP_DURATION);
				
				// Check that the correct toast message error appears
				toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
				assertEquals(toast_text, "That email already exists!");
				Thread.sleep(SLEEP_DURATION);
				

			} catch (Exception ex) {
				throw ex;
			} finally {

				// database clean-up
				Student s = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
				if (s != null)
					studentRepository.delete(s);
				driver.close();
				driver.quit();
			}

		}
		
}

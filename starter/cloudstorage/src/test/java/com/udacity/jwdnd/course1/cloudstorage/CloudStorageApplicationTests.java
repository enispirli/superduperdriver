package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg-redirect")).getText().contains("You successfully signed up"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the sign up page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
     void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }


    @Test
    void unauthorizedUserAccessibleScreens() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
    }


    @Test
    void testUserSignupLoginLogout() throws InterruptedException {

        doMockSignUp("Elif Nur", "Ispirli", "enispirli", "123");
        doLogIn("enispirli", "123");

        // logout
        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();

        Assertions.assertNotEquals("Home", driver.getTitle());
        Assertions.assertEquals("Login", driver.getTitle());

        Thread.sleep(2000);
    }

    @Test
    void createNote() throws InterruptedException {

        doMockSignUp("Elif Nur", "Ispirli", "enispirli", "123");
        doLogIn("enispirli", "123");

        // go to note-tab
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());

        // Click on add note button
        WebElement addNoteButton = driver.findElement(By.id("addNoteButton"));
        addNoteButton.click();

        // Fill out the note
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputTitle = driver.findElement(By.id("note-title"));
        inputTitle.click();
        inputTitle.sendKeys("Test Note");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputDescription = driver.findElement(By.id("note-description"));
        inputDescription.click();
        inputDescription.sendKeys("Test Note Description");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitNoteButton")));
        WebElement submitNote = driver.findElement(By.id("submitNoteButton"));
        submitNote.click();

        openNotesTab();

        WebElement noteTable = driver.findElement(By.id("userTable"));
        List<WebElement> noteElementList = noteTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        Assertions.assertEquals(1, noteElementList.size());

        // Check if the note appears
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        Assertions.assertTrue(driver.findElement(By.id("noteTitle")).getText().contains("Test Note"));


        Thread.sleep(3000);

    }


    @Test
    void editNote() throws InterruptedException {
        createNote();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // open edit modal
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editNoteButton")));
        WebElement editNote = driver.findElement(By.id("editNoteButton"));
        editNote.click();

        // Edit the note description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputDescription = driver.findElement(By.id("note-description"));
        inputDescription.click();
        inputDescription.clear();
        inputDescription.sendKeys("Edit Note Description Test");

        // save changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitNoteButton")));
        WebElement submitNote = driver.findElement(By.id("submitNoteButton"));
        submitNote.click();

        openNotesTab();

        // Check if the note description has changed
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        Assertions.assertTrue(driver.findElement(By.id("noteDescription")).getText().contains("Edit Note Description Test"));

        Thread.sleep(3000);
    }

    @Test
    void deleteNote() throws InterruptedException {

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        createNote();

//         press on delete button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNoteButton")));
        WebElement deleteNote = driver.findElement(By.id("deleteNoteButton"));
        deleteNote.click();

        openNotesTab();


        WebElement notesTable = driver.findElement(By.id("userTable"));
        List<WebElement> notesList = notesTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        // check note is deleted
        Assertions.assertEquals(0, notesList.size());
    }

    @Test
    void createCredential() throws InterruptedException {
        doMockSignUp("Elif Nur", "Ispirli", "enispirli", "123");
        doLogIn("enispirli", "123");

        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String inputCredentialPassword = "1234";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialsButton")));
        WebElement addCredentialsButton = driver.findElement(By.id("addCredentialsButton"));
        addCredentialsButton.click();

        // Fill out the credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputURL = driver.findElement(By.id("credential-url"));
        inputURL.click();
        inputURL.sendKeys("https://www.google.com/");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.click();
        inputUsername.sendKeys("ElifNur");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.sendKeys(inputCredentialPassword);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitCredentialButton")));
        WebElement submitNote = driver.findElement(By.id("submitCredentialButton"));
        submitNote.click();

        openCredentialsTab();

        //  Check if the password shown in table is encrypted
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        Assertions.assertNotEquals(driver.findElement(By.id("credentialPassword")).getText(), inputCredentialPassword);

        Thread.sleep(3000);
    }

    @Test
    void editCredential() throws InterruptedException {
        createCredential();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // press on edit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredentialButton")));
        WebElement editCredentialsButton = driver.findElement(By.id("editCredentialButton"));
        editCredentialsButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputURL = driver.findElement(By.id("credential-url"));
        inputURL.click();
        inputURL.clear();
        inputURL.sendKeys("https://www.udacity.com");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        String inputPassword = driver.findElement(By.id("credential-password")).getAttribute("value");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitCredentialButton")));
        WebElement submitCredential = driver.findElement(By.id("submitCredentialButton"));
        submitCredential.click();

        openCredentialsTab();

        // check changes
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        Assertions.assertTrue(driver.findElement(By.id("credentialUrl")).getText().contains("https://www.udacity.com"));

        // Verify that the viewable password is unencrypted
        Assertions.assertNotEquals(driver.findElement(By.id("credentialPassword")).getText(), inputPassword);

        Thread.sleep(3000);

    }

    @Test
    void deleteCredential() throws InterruptedException {
        createCredential();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // press on delete button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredentialButton")));
        WebElement deleteCredentialsButton = driver.findElement(By.id("deleteCredentialButton"));
        deleteCredentialsButton.click();

        openCredentialsTab();

        WebElement credentialTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credList = credentialTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        Assertions.assertEquals(0, credList.size());

        Thread.sleep(3000);
    }

    public void openNotesTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/home");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        driver.findElement(By.id("nav-notes-tab")).click();
    }

    public void openCredentialsTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        driver.get("http://localhost:" + this.port + "/home");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        driver.findElement(By.id("nav-credentials-tab")).click();
    }

}

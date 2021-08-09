package com.sender;

import com.sender.utils.MailReader;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

public class SeleniumTest{

    public static final String NAME_EMAIL_FIELD = "email";
    public static final String NAME_PASSWORD_FIELD = "password";
    public static final String ID_SIGN_IN_BUTTON = "signInSubmit";
    public static final String ID_TEST_BUTTON = "test";
    public static final String INPUT_FIELD_CLASS = "askt-utterance__input";
    public static final String PATH_TO_APP = "https://developer.amazon.com/alexa/console/ask?";
    public static final String PATH_TO_CHROME_WEB_DRIVER = "src/test/resources/chromedriver.exe";
    public static final String SKILL_NAME = "//*[text()='Picture Sender']";
    public static final String CARD = "//*[text()='card']";
    public static final String ANSWER_MESSAGE_CLASS = "askt-dialog__message--active-response";
    public static final String OPEN_SKILL_MESSAGE = "picture sender skill";
    public static final String FIRST_SKILL_MESSAGE = "Welcome to the Picture Sender.";
    public static final String EXPECTED_CONTENT =
            "<h2> Enjoy %s sent with <a href=\"\">Picture Sender</a></h2>" +
                    "<p><img src=\"\" width=\"\" height=\"\"></p>";;
    public static final String LINK_TO_SKILL = "https://skills-store.amazon.com/deeplink/tvt/99400617949a8b030b7f7fb02510ac2d6ae0409f97aaecc8d71e967914d38691a30a8728b818ebbdb4e191c6ce856bcccb022fd46f5a86f5247129bcc732b984f9bc4080f570a94f361b1e8fdb46f1824d98b3a4d09346e5079cc4dc86ae1d02db4ebd3e41ff427286a0897f7688f7";
    public static final String EXPECTED_ADDRESS = "Picture Sender <test@implemica.com>";
    public static final String EXPECTED_SUBJECT = "Picture from Alexa Skill";
    public static final String RECIPIENT_EMAIL_ADDRESS = "test+test@implemica.com";
    public static final String RECIPIENT_EMAIL_PASSWORD = "BUREWstErTrAwM";
    public static final String AWS_USERS_EMAIL_ADDRESS = "denys.honcharenko@nure.ua";
    public static final String AWS_USERS_PASSWORD = "Respect812";
    public static final String SEND_S_TO_S = "send %s to %s";
    public static final String THE_S_WAS_SENT_TO_S = "The %s was sent to %s.";
    public static final String TEST_TO = "test";
    public static final String EXPECTED_REQUEST = "            \"card\": {\n" +
            "                \"type\": \"Standard\",\n" +
            "                \"title\": \"Success\",\n" +
            "                \"text\": \"This picture was send to test\",\n" +
            "                \"image\": {\n";
    public static final String CARD_XPATH = "//*[@id=\"right\"]/div[2]/div";

    public static WebDriver driver;

    public static WebDriverWait wait;

    private WebElement input;


    @BeforeClass
    public static void startChrome() {
        System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_WEB_DRIVER);

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get(PATH_TO_APP);
    }

    @Test
    public void positiveTest() throws Exception {
        wait = new WebDriverWait(driver,30);
        logIn();
        goToTestPage();
        openSkill();

        a("dog");
        a("sea");
        a("red car");
        a("yellow flower");
        a("send apple", "What is the receiver name?", "test", "The apple was sent to test.", "apple");
        a("send to test", "What is the picture?", "apples", "The apples was sent to test.", "apples");
        a("trees", "What is the receiver name?", "test", "The trees was sent to test.", "trees");
        a("send apple to anton", "Sorry, but I don`t know anton");
        a("send swgsfzsr to denys h", "Sorry, but I cannot find the picture on request swgsfzsr");
        as("time", "test t");
        as("student", "tset");
        as("office", "tes");
        pic("apple", "test");
    }

    private void pic(String picture, String to) {
        writeInput(String.format(SeleniumTest.SEND_S_TO_S, picture, to));
        Assert.assertEquals(String.format(SeleniumTest.THE_S_WAS_SENT_TO_S, picture, to), readAnswer());
        testRequestJson();
    }

    private void testRequestJson() {
        WebElement card = driver.findElement(By.xpath(CARD_XPATH));
        boolean a = card.getText().contains(SeleniumTest.EXPECTED_REQUEST);
        Assert.assertTrue(a);
    }

    private void openSkill() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(INPUT_FIELD_CLASS)));
        input = driver.findElement(By.className(INPUT_FIELD_CLASS));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(INPUT_FIELD_CLASS)));
        input = driver.findElement(By.className(INPUT_FIELD_CLASS));

        writeInput(OPEN_SKILL_MESSAGE);
        Assert.assertEquals(FIRST_SKILL_MESSAGE, readAnswer());
    }

    private void a(String picture) throws MessagingException {
        as(picture, SeleniumTest.TEST_TO);
    }

    private void as(String picture, String to) throws MessagingException {
        writeInput(String.format(SeleniumTest.SEND_S_TO_S, picture, to));
        Assert.assertEquals(String.format(SeleniumTest.THE_S_WAS_SENT_TO_S, picture, to), readAnswer());

        extracted(5);
        testMail(picture);
    }

    private void a(String request, String answer) throws MessagingException {
        writeInput(request);
        Assert.assertEquals(answer, readAnswer());
    }

    private void a(String request1, String answer1, String request2, String answer2) throws MessagingException {
        writeInput(request1);
        Assert.assertEquals(answer1, readAnswer());

        writeInput(request2);
        Assert.assertEquals(answer2, readAnswer());
    }

    private void a(String request1, String answer1, String request2, String answer2, String picture) throws MessagingException {
        writeInput(request1);
        Assert.assertEquals(answer1, readAnswer());

        writeInput(request2);
        Assert.assertEquals(answer2, readAnswer());

        extracted(5);
        testMail(picture);
    }

    private void testMail(String picture) throws MessagingException {
        MailReader sample = new MailReader(RECIPIENT_EMAIL_ADDRESS, RECIPIENT_EMAIL_PASSWORD);
        Message message = sample.getMailBySubject(SeleniumTest.EXPECTED_SUBJECT);
        Assert.assertNotNull(message);
        sample.deleteMessageBySubject(SeleniumTest.EXPECTED_SUBJECT);

        Address[] addresses = message.getFrom();
        String htmlContent = sample.getHtmlContent();

        Assert.assertEquals(1, addresses.length);
        Assert.assertEquals(SeleniumTest.EXPECTED_ADDRESS, addresses[0].toString());
        Assert.assertEquals(String.format(SeleniumTest.EXPECTED_CONTENT, picture), deleteAllValue(htmlContent));
    }



    private String readAnswer() {
        extracted(1);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(ANSWER_MESSAGE_CLASS)));
        WebElement answer = driver.findElement(By.className(ANSWER_MESSAGE_CLASS));
        return answer.getText();
    }

    private void writeInput(String inputText) {
        input.sendKeys(inputText);
        input.sendKeys(Keys.ENTER);
    }

    private void goToTestPage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SKILL_NAME)));
        WebElement skill = driver.findElement(By.xpath(SKILL_NAME));
        skill.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ID_TEST_BUTTON)));
        WebElement test = driver.findElement(By.id(ID_TEST_BUTTON));
        test.click();
    }

    private void logIn() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(NAME_EMAIL_FIELD)));
        WebElement email = driver.findElement(By.name(NAME_EMAIL_FIELD));
        email.sendKeys(AWS_USERS_EMAIL_ADDRESS);

        WebElement password = driver.findElement(By.name(NAME_PASSWORD_FIELD));
        password.sendKeys(AWS_USERS_PASSWORD);

        extracted(2);

        WebElement signIn = driver.findElement(By.id(ID_SIGN_IN_BUTTON));
        signIn.click();
    }

    private synchronized void extracted(int seconds) {
        try
        {
            this.wait(seconds * 1000L);
        }
        catch (InterruptedException ignored)
        {
        }
    }

    private String deleteAllValue(String html){
        StringBuilder builder = new StringBuilder();
        boolean isWrite = true;

        for(char c : html.trim().toCharArray()){
            if(c == '"'){
                builder.append(c);
                isWrite = !isWrite;
            }else if(isWrite){
                builder.append(c);
            }
        }

        return builder.toString();
    }
}

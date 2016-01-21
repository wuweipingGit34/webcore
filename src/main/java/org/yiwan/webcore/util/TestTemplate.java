package org.yiwan.webcore.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

/**
 * @author Kenny Wang
 * 
 */
public class TestTemplate {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}

	private String currentUrl;

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	private String baseUrl;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String intialUrl) {
		this.baseUrl = intialUrl;
	}

	/**
	 * testng test suite name
	 */
	private String suiteName;

	/**
	 * get testng test suite name
	 * 
	 * @return testng suite name
	 */
	public String getSuiteName() {
		return suiteName;
	}

	/**
	 * set testng test suite name
	 * 
	 * @param suiteName
	 */
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	/**
	 * testng test name
	 */
	private String testName;

	/**
	 * get testng test name
	 * 
	 * @return testng test name
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * set testng test name
	 * 
	 * @param suiteName
	 */
	public void setTestName(String suiteName) {
		this.testName = suiteName;
	}

	private String downloadFile;

	/**
	 * get last download file name with relative path
	 * 
	 * @return download file name
	 */
	public String getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	private String j_winname = "";

	public void setJ_winname(String j_winname) {
		this.j_winname = j_winname;
	}

	public String getJ_winname() {
		return j_winname;
	}

	/**
	 * enable http request archive, default value should be false, true for
	 * performance use
	 */
	private boolean enableHAR = PropHelper.ENABLE_HAR;

	/**
	 * is enable http request archive
	 * 
	 * @return enabled or disabled
	 */
	public boolean isEnableHAR() {
		return enableHAR;
	}

	/**
	 * enable or disable http request archive
	 * 
	 * @param enableHAR
	 */
	public void setEnableHAR(boolean enableHAR) {
		this.enableHAR = enableHAR;
	}

	protected final static String DISCRIMINATOR_KEY = "testcase";

	/**
	 * whether to skip next execution of left test methods
	 */
	protected Boolean skipTest = false;

	/**
	 * @return the skipTest
	 */
	public Boolean getSkipTest() {
		return skipTest;
	}

	/**
	 * @param skipTest
	 *            the skipTest to set
	 */
	public void setSkipTest(Boolean skipTest) {
		this.skipTest = skipTest;
	}

	/**
	 * log the content into the report
	 * 
	 * @param s
	 */
	public void report(String s) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now = df.format(new Date());
		Reporter.log(now + " " + this.getClass().getName() + " " + s + "<br>");
	}

	/**
	 * get test case id of current instance
	 * 
	 * @return test case id string
	 */
	public String getTestCaseId() {
		return this.getClass().getSimpleName();
	}

	/**
	 * get test result folder against suite name and test name
	 * 
	 * @return
	 */
	private String getTestResultFolder() {
		return ("target/result/" + (suiteName == null ? "" : suiteName) + "/" + (testName == null ? "" : testName)
				+ "/").replace("//", "/");
	}

	/**
	 * get log folder
	 * 
	 * @return log folder string
	 */
	public String getLogFolder() {
		return getTestResultFolder() + "log/";
	}

	/**
	 * get target data folder
	 * 
	 * @return target data folder string
	 */
	public String getTargetDataFolder() {
		return getTestResultFolder() + "data/";
	}

	/**
	 * get screenshot folder
	 * 
	 * @return screenshot folder string
	 */
	public String getScreenshotFolder() {
		return getTestResultFolder() + "screenshot/";
	}

	/**
	 * get HAR folder
	 * 
	 * @return HAR folder string
	 */
	public String getHARFolder() {
		return getTestResultFolder() + "har/" + getTestCaseId() + "/";
	}

	public JavascriptExecutor getJavascriptExecutor() {
		return (JavascriptExecutor) driver;
	}

	public Wait<WebDriver> getWebDriverWait() {
		return new WebDriverWait(driver, PropHelper.TIMEOUT_INTERVAL, PropHelper.TIMEOUT_POLLING_INTERVAL)
				.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class)
				.ignoring(UnreachableBrowserException.class);
	}

	public TakesScreenshot getTakesScreenshot() {
		if (PropHelper.REMOTE)
			// RemoteWebDriver does not implement the TakesScreenshot class if
			// the driver does have the Capabilities to take a screenshot then
			// Augmenter will add the TakesScreenshot methods to the instance
			return (TakesScreenshot) (new Augmenter().augment(driver));
		else
			return (TakesScreenshot) driver;
	}
}
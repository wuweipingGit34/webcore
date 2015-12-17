package org.yiwan.webcore.test;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.yiwan.webcore.selenium.Driver;
import org.yiwan.webcore.util.Helper;
import org.yiwan.webcore.util.Property;

/**
 * @author Kenny Wang
 * 
 */
public class TestCaseTemplate {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Driver driver;
	protected String currentUrl = "";

	private final static String DISCRIMINATOR_KEY = "testcase";

	private final static BrowserMobProxy proxy = new BrowserMobProxyServer();
	private String downloadFileName;
	private String suiteName;

	public static BrowserMobProxy getProxy() {
		return proxy;
	}

	/**
	 * get just download file name with relative path
	 * 
	 * @return download file name
	 */
	public String getDownloadFileName() {
		return downloadFileName;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	/**
	 * @return the driver
	 */
	public Driver getDriver() {
		return driver;
	}

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
	 * get test data file by specified test case id
	 * 
	 * @param testCaseId
	 * @param isSource
	 *            true indicates source file, false indicates target file
	 * @return target test data file
	 */
	protected File getTestData(String testCaseId, Boolean isSource) {
		if (testCaseId == null)
			throw new IllegalArgumentException("parameter testCaseId was null!");
		if (isSource) {
			return new File("data/" + testCaseId + ".xml");
		} else {
			return new File(getTargetDataFolder() + testCaseId + ".xml");
		}
	}

	@BeforeClass
	@Parameters({ "os", "os_version", "browser", "browser_version",
			"resolution", "url" })
	protected void beforeClass(@Optional String os,
			@Optional String os_version, @Optional String browser,
			@Optional String browser_version, @Optional String resolution,
			@Optional String url, ITestContext testContext) {
		suiteName = testContext.getCurrentXmlTest().getName();
		MDC.put(DISCRIMINATOR_KEY, getLogFolder() + getTestCaseId());

		logger.info("setup before class");

		driver = new Driver(this, os, os_version, browser, browser_version,
				resolution);

		report(Helper.getTestReportStyle("../../../" + getLogFolder()
				+ getTestCaseId() + ".log", "open test execution log"));

		if (new File("data/" + getTestCaseId() + ".xml").exists())
			report(Helper.getTestReportStyle("../../../data/" + getTestCaseId()
					+ ".xml", "open source test data"));
		report(Helper.getTestReportStyle("../../../" + getTargetDataFolder()
				+ getTestCaseId() + ".xml", "open target test data"));

		// test url load strategy
		if (url == null) {// if the parameter url is null
			// acquiring it from system property
			String serverUrl = System.getProperty("server.url");
			if (serverUrl == null) {// if system doesn't set the url property
				report(Helper.getTestReportStyle(Property.BASE_URL,
						"open test server url by property config"));
				driver.navigateTo(Property.BASE_URL);
			} else {
				report(Helper.getTestReportStyle(serverUrl,
						"open test server url by system property server.url"));
				driver.navigateTo(serverUrl);
			}
		} else {
			report(Helper.getTestReportStyle(url,
					"open test server url by testng config"));
			driver.navigateTo(url);
		}
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

	@AfterClass
	protected void afterClass() {
		logger.info("teardown after class");
		driver.closeAll();
		driver.quit();
	}

	/**
	 * get test case id of current instance
	 * 
	 * @return test case id string
	 */
	public String getTestCaseId() {
		return this.getClass().getSimpleName();
	}

	@BeforeSuite
	protected void beforeSuite() {
		logger.info("setup before suite");

		proxy.start(0);

		// set response filter rule for downloading files
		proxy.addResponseFilter(new ResponseFilter() {
			@Override
			public void filterResponse(HttpResponse response,
					HttpMessageContents contents, HttpMessageInfo messageInfo) {
				String fileName = getTargetDataFolder() + getTestCaseId() + "_"
						+ Helper.randomize();
				if (contents.getContentType() != null
						&& contents.getContentType().contains("text/csv")) {
					if (contents.getTextContents() != null) {
						downloadFileName = fileName + ".csv";
						logger.info("saving csv file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeStringToFile(file,
									contents.getTextContents());
						} catch (UnsupportedCharsetException | IOException e) {
							logger.error("chartset was unsupported", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else if (contents.getContentType() != null
						&& contents.getContentType().contains("text/xml")) {
					if (contents.getTextContents() != null) {
						downloadFileName = fileName + ".xml";
						logger.info("saving xml file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeStringToFile(file,
									contents.getTextContents());
						} catch (UnsupportedCharsetException | IOException e) {
							logger.error("charset was unsupported", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else if (contents.getContentType() != null
						&& contents.getContentType().contains(
								"application/vnd.ms-excel")) {
					if (contents.getBinaryContents() != null) {
						downloadFileName = fileName + ".xls";
						logger.info("saving xls file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeByteArrayToFile(file,
									contents.getBinaryContents());
						} catch (IOException e) {
							logger.error("IO exception occurred", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else if (contents.getContentType() != null
						&& contents.getContentType()
								.contains("application/pdf")) {
					if (contents.getBinaryContents() != null) {
						downloadFileName = fileName + ".pdf";
						logger.info("saving pdf file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeByteArrayToFile(file,
									contents.getBinaryContents());
						} catch (IOException e) {
							logger.error("IO exception occurred", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else if (contents.getContentType() != null
						&& contents.getContentType()
								.contains("application/zip")) {
					if (contents.getBinaryContents() != null) {
						downloadFileName = fileName + ".zip";
						logger.info("saving zip file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeByteArrayToFile(file,
									contents.getBinaryContents());
						} catch (IOException e) {
							logger.error("IO exception occurred", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else if (contents.getContentType() != null
						&& contents.getContentType().contains(
								"application/octet-stream")
						&& response.headers().get("Content-Disposition") != null
						&& response.headers().get("Content-Disposition")
								.contains("attachment;filename=")) {
					if (contents.getBinaryContents() != null) {
						downloadFileName = fileName
								+ "."
								+ response.headers().get("Content-Disposition")
										.replace("attachment;filename=", "")
										.replace(";", "").replace("\"", "")
										.replace("'", "").split("\\.")[1]
										.trim();
						logger.info("saving file to " + downloadFileName);
						File file = new File(downloadFileName);
						try {
							FileUtils.writeByteArrayToFile(file,
									contents.getBinaryContents());
						} catch (IOException e) {
							logger.error("IO exception occurred", e);
						}
						response.setStatus(HttpResponseStatus.NO_CONTENT);
					}
				} else { // if there is no file to download
					downloadFileName = null;
				}
			}
		});
	}

	@AfterSuite
	protected void afterSuite() {
		logger.info("teardown after suite");
		proxy.stop();
	}

	public String getSuiteName() {
		return suiteName;
	}

	public String getLogFolder() {
		return "target/" + suiteName + "/logs/";
	}

	public String getTargetDataFolder() {
		return "target/" + suiteName + "/data/";
	}
}
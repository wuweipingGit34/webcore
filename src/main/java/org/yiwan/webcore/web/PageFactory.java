package org.yiwan.webcore.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yiwan.webcore.test.TestBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PageFactory {

    private final static Logger logger = LoggerFactory.getLogger(PageFactory.class);

    /*private WebDriver driver;

    public PageFactory(TestBase testCase) {
        this.driver = testCase.getWebDriver();
    }
    
    public PageFactory(WebDriver driver) {
        this.driver = driver;
    }

    @SuppressWarnings("unchecked")
    public <T extends WebDriverWrapper> T newPage(Class<?> clazz) {
        Constructor<?> c = null;
        try {
            c = clazz.getDeclaredConstructor(WebDriver.class);
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error(e.getMessage(), e);
        }
        c.setAccessible(true);
        try {
            return (T) c.newInstance(driver);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }*/

    private TestBase testCase;

    public PageFactory(TestBase testCase) {
        this.testCase = testCase;
    }

    public <T extends WebDriverWrapper> T newPage(Class<T> clazz) {
        Constructor<T> c = null;
        try {
            c = clazz.getDeclaredConstructor(TestBase.class);
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error(e.getMessage(), e);
        }
        assert c != null;
        c.setAccessible(true);
        try {
            return (T) c.newInstance(testCase);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}

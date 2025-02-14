package com.andruy.backend.service;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.andruy.backend.repository.BrowserRepository;
import com.andruy.backend.util.AppSettings;
import com.andruy.backend.util.EmailSender;
import com.andruy.backend.util.TimeTracker;

@Service
public class BrowserService {
    Logger logger = LoggerFactory.getLogger(BrowserService.class);
    @Value("${cv.username}")
    private String username;
    @Value("${cv.password}")
    private String password;
    @Value("${cv.url}")
    private String url;
    @Value("${cv.email}")
    private String email;
    private EmailSender emailUtil = new EmailSender();
    private final String FROM = "Puncher";
    private String action = "undefined";
    private WebDriver driver;
    @Autowired
    private TimeTracker timeTracker;
    @Autowired
    private BrowserRepository browserRepository;

    public Map<String, String> clockIn(Map<String, Boolean> body) {
        String status;

        if (AppSettings.isActive()) {
            try {
                logger.trace("Starting clock in");
                halt(body.get("timer"));
                logger.trace("Starting web browser");
                driver = new ChromeDriver();
                driver.get(url);
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.id("LogOnEmployeeId")).sendKeys(username);
                driver.findElement(By.className("BtnGreen")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='password'].CustomControlInput")).sendKeys(password);
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                status = "TimeClock returned: " + driver.findElement(By.cssSelector("td.AlertContainer")).getText();
                logger.trace(status);

                if (status.equals("TimeClock returned: Clock In operation successful")) {
                    int updated = browserRepository.enterTime(action, System.currentTimeMillis());

                    if (updated == 1 && !action.equals("undefined")) {
                        logger.trace("Started timer with " + action);
                        action = "undefined";
                    } else {
                        logger.warn("Action was undefined or failed to insert into database");
                    }
                } else {
                    logger.warn("Failed to clock in");
                }

                logger.trace("Closing web browser");
                driver.close();
                driver.quit();

                logger.trace("Sending email");
                emailUtil.sendEmail(FROM, email, status);
            } catch (Exception e) {
                logger.error(e.getMessage());
                if (driver != null) {
                    driver.close();
                    driver.quit();
                }
                emailUtil.sendEmail(FROM, email, e.getMessage());
                status = "Something went wrong";
            }
        } else {
            status = "Puncher is stopped";
        }

        return Map.of("message", status);
    }

    public Map<String, String> checkDashboard() {
        String status;

        logger.trace("Checking dashboard");

        try {
            driver = new ChromeDriver();
            driver.get(url);
            Thread.sleep(AppSettings.getHaltTime());
            driver.findElement(By.id("LogOnEmployeeId")).sendKeys(username);
            driver.findElement(By.className("DefaultSubmitBehavior")).click();
            Thread.sleep(AppSettings.getHaltTime());
            driver.findElement(By.cssSelector("input[type='password'].CustomControlInput")).sendKeys(password);
            driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
            Thread.sleep(AppSettings.getHaltTime());
            status = "Current status: " + driver.findElement(By.cssSelector("div[ng-bind='getEmployeeClockStatus()']")).getText();
            logger.trace(status);
            driver.findElement(By.cssSelector("div[ng-click='logOutEmployee()']")).click();
            Thread.sleep(1000);
            driver.close();
            driver.quit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (driver != null) {
                driver.close();
                driver.quit();
            }
            status = "Something went wrong";
        }

        return Map.of("message", status);
    }

    public Map<String, String> clockOut(Map<String, Boolean> body) {
        String status;

        if (AppSettings.isActive()) {
            try {
                logger.trace("Starting clock out");
                halt(body.get("timer"));
                logger.trace("Starting web browser");
                driver = new ChromeDriver();
                driver.get(url);
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.id("LogOnEmployeeId")).sendKeys(username);
                driver.findElement(By.cssSelector("input[type='submit'].tcp-btn.BtnAction")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='password'].CustomControlInput")).sendKeys(password);
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                driver.findElement(By.cssSelector("input[type='submit'].BtnAction.DefaultSubmitBehavior")).click();
                Thread.sleep(AppSettings.getHaltTime());
                status = "TimeClock returned: " + driver.findElement(By.cssSelector("td.AlertContainer")).getText();
                logger.trace(status);

                if (status.equals("TimeClock returned: Clock Out operation successful")) {
                    int updated = browserRepository.enterTime(action, System.currentTimeMillis());

                    if (updated == 1 && !action.equals("undefined")) {
                        logger.trace("Stopped timer with " + action);
                        enterTotalTime();

                        action = "undefined";
                    } else {
                        logger.warn("Action was undefined or failed to insert into database");
                    }
                } else {
                    logger.warn("Failed to clock out");
                }

                logger.trace("Closing web browser");
                driver.close();
                driver.quit();

                logger.trace("Sending email");
                emailUtil.sendEmail(FROM, email, status);
            } catch (Exception e) {
                logger.error(e.getMessage());
                if (driver != null) {
                    driver.close();
                    driver.quit();
                }
                emailUtil.sendEmail(FROM, email, e.getMessage());
                status = "Something went wrong";
            }
        } else {
            status = "Puncher is stopped";
        }

        return Map.of("message", status);
    }

    private void halt(boolean value) {
        try {
            if (value) {
                int x = ThreadLocalRandom.current().nextInt(5) * 60000;
                int simplified = x / 60000;
                String msg = simplified == 1 ? "Halting for 1 minute" : "Halting for " + simplified + " minutes";
                logger.trace(msg);
                Thread.sleep(x);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void enterTotalTime() {
        try {
            if (action.equals("morningClockOut")) {
                logger.trace("Querying DB for current hours");
                Map<String, Long> map = browserRepository.getCurrentHours(action, "morningClockIn");
                double time = timeTracker.getTotalHours(map.get("final"), map.get("initial"));

                logger.trace("Updating DB with " + time);
                browserRepository.updateTime(timeTracker.getDayOfTheWeek(), time, AppSettings.getCurrentWeekId());
            }

            if (action.equals("afternoonClockOut")) {
                logger.trace("Querying DB for previous hours");
                double temp = browserRepository.getCurrentTimeForTheDay(timeTracker.getDayOfTheWeek(), AppSettings.getCurrentWeekId());

                logger.trace("Querying DB for current hours");
                Map<String, Long> map = browserRepository.getCurrentHours(action, "afternoonClockIn");
                double time = temp + timeTracker.getTotalHours(map.get("final"), map.get("initial"));

                logger.trace("Updating DB with " + time);
                browserRepository.updateTime(timeTracker.getDayOfTheWeek(), time, AppSettings.getCurrentWeekId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public Map<String, String> logReader() {
        StringBuilder sb = new StringBuilder();
        File log = new File("logs/app.log");

        try {
            Scanner scanner = new Scanner(log);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return Map.of("logs", sb.toString());
    }
}

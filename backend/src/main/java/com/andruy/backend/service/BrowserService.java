package com.andruy.backend.service;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.andruy.backend.repository.BrowserRepository;
import com.andruy.backend.util.AppSettings;
import com.andruy.backend.util.EmailSender;
import com.andruy.backend.util.TimeTracker;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

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
    @Autowired
    private TimeTracker timeTracker;
    @Autowired
    private BrowserRepository browserRepository;

    public Map<String, String> clockIn(Map<String, Boolean> body) {
        String status;

        if (AppSettings.isActive()) {
            Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            try {
                logger.trace("Starting clock in");
                halt(body.get("timer"));
                logger.trace("Starting web browser");
                page.navigate(url);
                page.fill("#LogOnEmployeeId", username);
                page.click(".BtnGreen");
                page.fill("input[type='password'].CustomControlInput", password);
                page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                // page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                status = page.textContent("td.AlertContainer");
                logger.trace(status);

                if (status.equals("Clock In operation successful")) {
                    status = "Clocked in";
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

                logger.trace("Sending email");
                // emailUtil.sendEmail(FROM, email, status);
            } catch (Exception e) {
                logger.error(e.getMessage());
                // emailUtil.sendEmail(FROM, email, e.getMessage());
                status = "Something went wrong";
            } finally {
                browser.close();
                playwright.close();
            }
        } else {
            status = "Puncher is stopped";
        }

        return Map.of("message", status);
    }

    public Map<String, String> checkDashboard() {
        String status;

        logger.trace("Checking dashboard");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        try {
            logger.trace("Starting web browser");
            page.navigate(url);
            page.fill("#LogOnEmployeeId", username);
            page.click("//*[@id=\"featureForm\"]/div[2]/div[1]/div/div[2]/table/tbody/tr[10]/td[2]/span/input");
            status = page.textContent("td.AlertContainer");
            logger.trace(status);

            if (status.equals("Clock In operation successful")) {
                status = "Clocked in";
                logger.trace("Dashboard is up and running");
            } else {
                logger.warn("Failed to check dashboard");
                status = "Failed to check dashboard";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            status = "Something went wrong";
        } finally {
            browser.close();
            playwright.close();
        }

        return Map.of("message", status);
    }

    public Map<String, String> clockOut(Map<String, Boolean> body) {
        String status;

        if (AppSettings.isActive()) {
            Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
    
            try {
                logger.trace("Starting clock out");
                halt(body.get("timer"));
                logger.trace("Starting web browser");
                page.navigate(url);
                page.fill("#LogOnEmployeeId", username);
                page.click(".BtnGreen");
                page.fill("input[type='password'].CustomControlInput", password);
                page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                // page.click("input[type='submit'].BtnAction.DefaultSubmitBehavior");
                status = page.textContent("td.AlertContainer");
                logger.trace(status);

                if (status.equals("Clock Out operation successful")) {
                    status = "Clocked out";
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

                logger.trace("Sending email");
                // emailUtil.sendEmail(FROM, email, status);
            } catch (Exception e) {
                logger.error(e.getMessage());
                status = "Something went wrong";
            } finally {
                browser.close();
                playwright.close();
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

    @Scheduled(cron = "0 55 7 * * 1-5")
    void morningClockIn() {
        action = "morningClockIn";
        clockIn(Map.of("timer", true));
    }

    @Scheduled(cron = "0 57 12 * * 1-5")
    void morningClockOut() {
        action = "morningClockOut";
        clockOut(Map.of("timer", true));
    }

    @Scheduled(cron = "0 55 13 * * 1-5")
    void afternoonClockIn() {
        action = "afternoonClockIn";
        clockIn(Map.of("timer", true));
    }

    @Scheduled(cron = "0 26 16 * * 1-5")
    void afternoonClockOut() {
        action = "afternoonClockOut";
        clockOut(Map.of("timer", true));
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

    public void setAction(String action) {
        this.action = action;
    }
}

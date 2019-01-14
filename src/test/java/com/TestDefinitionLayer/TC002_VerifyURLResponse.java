package com.TestDefinitionLayer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TC002_VerifyURLResponse {
    public static List<String> errorList = new ArrayList<>();
    ExecutorService executor;
    ChromeDriver driver;

    @Test
    public void URLCheckTest() {

        int MYTHREADS = 30;
        executor = Executors.newFixedThreadPool(MYTHREADS);
        System.setProperty("webdriver.chrome.driver", "chromedrivermac");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("http://www.yourwebsite.com");
        List<WebElement> list = driver.findElements(By.xpath(".//a[@href!='']"));

        for (int i = 0; i < list.size(); i++) {
            WebElement element = list.get(i);
            Runnable worker = new MyRunnable(element);
            executor.execute(worker);
        }
        executor.shutdown();

        while (!executor.isTerminated()) {
        }
        if (errorList.size() > 0) {
            for (String link : errorList
            ) {
                System.out.println("Url = " + link);
            }
            Assert.assertTrue(false);
        }

    }

    private static void sendGet(WebElement element) {
        String href = element.getAttribute("href");

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(href);

        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int code = response.getStatusLine().getStatusCode();

        if (code != 200)
            errorList.add(href);

    }

    public static class MyRunnable implements Runnable {
        private final WebElement hrefEl;

        MyRunnable(WebElement el) {
            this.hrefEl = el;
        }

        @Override
        public void run() {

            sendGet(hrefEl);
        }
    }
}

package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import static com.codeborne.selenide.Selenide.open;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class ReservationCardTest {

    private  WebDriver driver;

//    @BeforeAll
//    static void setUpAll() {
//        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//        WebDriverManager.chromedriver().setup();
//    }
//
//    @BeforeEach
//    void setUp() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
//        driver = new ChromeDriver(options);
//    }

//========= Драйвер для Microsoft Edge (не работает с Appveyor !!!) =========

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
        WebDriverManager.edgedriver().setup();
    }

    @BeforeEach
    void setUp() {
        Configuration.browser = "edge";
        EdgeOptions options = new EdgeOptions();
        options.setCapability("goog:chromeOptions", ImmutableMap.of("w3c", false)); // отключение w3c протокола
        driver = new EdgeDriver(options);
    }

//============================================================================

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldReservationCard() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        //$$(".tab-item").find(exactText("По номеру счёта")).click();
        $("[data-test-id='city'] .input__control").setValue("Калуга");
        $("[data-test-id='date'] .input__control").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
        String dateForReservation = LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] .input__control").setValue(dateForReservation);
        $("[data-test-id='name'] .input__control").setValue("Иван Петров-Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        //$(withText("Встреча успешно забронирована на " + dateForReservation)).shouldBe(visible, Duration.ofMillis(15000));
        $(withText("Встреча успешно забронирована на")).shouldBe(visible, Duration.ofMillis(15000));
        //$$("button").find(exactText("Забронировать")).click();
        //$(withText("Успешная авторизация")).shouldBe(visible, Duration.ofMillis(5000));
        //$(byText("Личный кабинет")).shouldBe(visible, Duration.ofMillis(5000));
    }
}


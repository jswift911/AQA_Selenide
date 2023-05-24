package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

//========= Драйвер для Microsoft Edge (не работает с Appveyor !!!) =========

//    @BeforeAll
//    static void setUpAll() {
//        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
//        WebDriverManager.edgedriver().setup();
//    }
//
//    @BeforeEach
//    void setUp() {
//        Configuration.browser = "edge";
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--headless");
//        driver = new EdgeDriver(options);
//        driver.get("http://localhost:9999");
//    }

//============================================================================

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    // 1) Тест, без использования виджетов

    @Test
    void shouldReservationCard() {
        //Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
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
        $(withText("Встреча успешно забронирована на")).shouldBe(visible, Duration.ofMillis(15000));
    }

    // 2) Тест, с использованием виджетов

    @Test
    void shouldReservationCardWithElements() {
        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").setValue("Ка");
        $(By.xpath("//*[contains(@class, 'menu')]//*[contains(@class, 'menu-item') and contains(text(), 'Петропавловск-Камчатский')]")).click();
        $(".calendar-input [type='button']").click();
        String dateForReservation = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("d"));
        SelenideElement day = $(By.xpath("//*[contains(@class, 'calendar-input__calendar-wrapper')]//*[contains(@class, 'calendar__layout')]//*[contains(@class, 'calendar__day') and contains(text(), " + dateForReservation + ")]"));
        int weekForward = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("d")));

        if (Integer.parseInt(dateForReservation) < weekForward) {
            $(By.xpath("//*[contains(@class, 'calendar__arrow_direction_right') and not(contains(@class, 'calendar__arrow_double'))]")).click();
            day.click();
        } else {
            day.click();
        }

        $("[data-test-id='name'] .input__control").setValue("Иван Петров-Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Встреча успешно забронирована на")).shouldBe(visible, Duration.ofMillis(15000));
    }
}


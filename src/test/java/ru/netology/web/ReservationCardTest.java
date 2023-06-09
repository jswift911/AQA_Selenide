package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
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

    // Если браузер не Chrome, то нужно менять драйвер для работы selenide

    private WebDriver driver;

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


    public String generateDate(int plusDays, String pattern) {
        if (plusDays == 0) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
        }
        return LocalDate.now().plusDays(plusDays).format(DateTimeFormatter.ofPattern(pattern));
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
        String dateForReservation = generateDate(5, "dd.MM.yyyy");
        $("[data-test-id='date'] .input__control").setValue(dateForReservation);
        $("[data-test-id='name'] .input__control").setValue("Иван Петров-Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $(By.cssSelector(".notification__content")).shouldHave(exactText("Встреча успешно забронирована на " + dateForReservation), Duration.ofMillis(15000)).shouldBe(visible);
    }

    // 2) Тест, с использованием виджетов

    @Test
    void shouldReservationCardWithWidgets() {
        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").setValue("Ка");
        $(By.xpath("//*[contains(@class, 'menu-item') and contains(text(), 'Петропавловск-Камчатский')]")).click();
        $(".calendar-input [type='button']").click();
        int defaultDay = Integer.parseInt(generateDate(3,"d"));
        int dayForReservation = Integer.parseInt(generateDate(10, "d"));
        String fullDate = generateDate(10, "dd.MM.yyyy");
        SelenideElement day = $(By.xpath("//*[contains(@class, 'calendar-input__calendar-wrapper')]//*[contains(@class, 'calendar__layout')]//*[contains(@class, 'calendar__day') and contains(text(), " + dayForReservation + ")]"));

        if (dayForReservation < defaultDay) {
            $(By.xpath("//*[contains(@class, 'calendar__arrow_direction_right') and not(contains(@class, 'calendar__arrow_double'))]")).click();
        }
        day.click();

        $("[data-test-id='name'] .input__control").setValue("Иван Петров-Иванов");
        $("[data-test-id='phone'] [name='phone']").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Забронировать")).click();
        $(By.cssSelector(".notification__content")).shouldHave(exactText("Встреча успешно забронирована на " + fullDate), Duration.ofMillis(15000)).shouldBe(visible);
    }
}


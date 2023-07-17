package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryCardTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");

    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldFillRegistrationForm() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysForMeetingOne = 4;
        String firstDate = DataGenerator.generateDate(daysForMeetingOne);
        int daysForMeetingTwo = 7;
        String secondDate = DataGenerator.generateDate(daysForMeetingTwo);

        $(By.cssSelector("[data-test-id=city] input")).sendKeys(validUser.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").doubleClick().sendKeys(firstDate);
        $(By.cssSelector("[data-test-id=name] input")).sendKeys(validUser.getName());
        $(By.cssSelector("[data-test-id=phone] input")).sendKeys(validUser.getPhone());
        $(By.cssSelector("[data-test-id=agreement]")).click();
        $(By.cssSelector(".button")).click();
        $(".notification__content").shouldHave((Condition.text("Встреча успешно запланирована на " + firstDate)), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").doubleClick().sendKeys(secondDate);
        $(By.cssSelector(".button")).click();
        $("[data-test-id=replan-notification] .notification__title").shouldHave((Condition.text("Необходимо подтверждение")), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $(By.cssSelector("[data-test-id=replan-notification] .button")).click();
        $("[data-test-id=success-notification] .notification__content").shouldHave((Condition.text("Встреча успешно запланирована на " + secondDate)), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}

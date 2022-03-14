import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryTest {

    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        Configuration.browser = "firefox";
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
    public void correctDeliveryTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        String date = DataGenerator.generateDate(3);

        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(info.getFullName());
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".notification__title").shouldHave(text("Успешно!"));
        $(".notification__content")
                .shouldHave(text("Встреча успешно запланирована на \n" + date));

        $$("button").filter(text("Запланировать")).first().click();
        $$("button").filter(text("Перепланировать")).first().click();

        DataGenerator.RegistrationInfo infoUpd = DataGenerator.Registration.generateInfo("ru", 4);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $$("button").filter(text("Запланировать")).first().click();

    }

    @Test
    public void emptyCityTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        String date = DataGenerator.generateDate(3);
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(info.getFullName()); // баг, с буквой ё не принимается
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectCityTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        String date = DataGenerator.generateDate(3);
        $("[data-test-id=city] input").setValue("Москваааа");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=name] input").setValue(info.getFullName());
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void incorrectNameTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $("[data-test-id=name] input").setValue("12 df");
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void emptyNameTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void incorrectPhoneTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $("[data-test-id=name] input").setValue(info.getFullName());
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        // баг, можно любое к-во цифр в номере оставить (должно быть 11)
    }

    @Test
    public void emptyPhoneTest() {

        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $("[data-test-id=name] input").setValue(info.getFullName());
        $(".checkbox__box").click();
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void withoutAgreementTest() {
        DataGenerator.RegistrationInfo info = DataGenerator.Registration.generateInfo("ru");
        $("[data-test-id=city] input").setValue(info.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(3));
        $("[data-test-id=name] input").setValue(info.getFullName());
        $("[data-test-id=phone] input").setValue(info.getPhoneNumber());
        $$("button").filter(text("Запланировать")).first().click();
        $(".input_invalid");
    }
}

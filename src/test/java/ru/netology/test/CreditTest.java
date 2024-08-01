package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditGate;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class CreditTest {
    String validCardNumber = DataHelper.getApprovedCard().getCardNumber();
    String validMonth = DataHelper.getValidMonth();
    String validYear = DataHelper.getYear(1);
    String validOwner = DataHelper.getValidName();
    String validcvccvv = DataHelper.getNumber(3);
    String declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
    String randomCardNumber = DataHelper.getRandomCardNumber().getCardNumber();
    public static String url = System.getProperty("set.url");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open(url);
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    @DisplayName("Отправка заявки с использованием одобренной карты")
    void happyPath() {
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки с использованием не одобренной карты")
    void declinedCard() {
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(declinedCardNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkNotificationErrorIsVisible();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки с использованием рандомной карты")
    void randomCard() {
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(randomCardNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkNotificationErrorIsVisible();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено буквами")
    void lettersCardNumber() {
        var lettersNumber = DataHelper.getValidName();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(lettersNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено спецсимволами")
    void symbolsCardNumber() {
        var symbolsNumber = DataHelper.getSymbols();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(symbolsNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено не полностью")
    void notCompletelyCardNumber() {
        var notCompletelyNumber = DataHelper.getNumber(15);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(notCompletelyNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено излишне")
    void excessiveCardNumber() {
        var excessiveNumber = "4444 4444 4444 44411";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(excessiveNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты не заполнено")
    void emptyCardNumber() {
        var emptyCardNumber = DataHelper.getEmptyFieldValue();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(emptyCardNumber, validMonth, validYear, validOwner, validcvccvv);
        creditgate.checkValidationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено буквами")
    void lettersMonth() {
        var lettersMonth = DataHelper.getValidName();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, lettersMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено спецсимволами")
    void symbolsMonth() {
        var symbolsMonth = DataHelper.getSymbols();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, symbolsMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено не полностью")
    void notCompletelyMonth() {
        var notCompletelyMonth = DataHelper.getNumber(1);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, notCompletelyMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено излишне")
    void excessiveMonth() {
        var excessiveMonth = "120";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, excessiveMonth, validYear, validOwner, validcvccvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено заполнено больше 12, но меньше 100")
    void thirteenMonth() {
        var excessiveMonth = "13";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, excessiveMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено заполнено 00")
    void zeroMonth() {
        var zeroMonth = "00";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, zeroMonth, validYear, validOwner, validcvccvv);
        creditgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц не заполнено")
    void emptyMonth() {
        var emptyMonth = DataHelper.getEmptyFieldValue();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, emptyMonth, validYear, validOwner, validcvccvv);
        creditgate.checkValidationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено буквами")
    void lettersYear() {
        var lettersYear = DataHelper.getValidName();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, lettersYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено спецсимволами")
    void symbolsYear() {
        var symbolsYear = DataHelper.getSymbols();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, symbolsYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено значением в прошлом")
    void lastYear() {
        var lastYear = DataHelper.getYear(-1);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, lastYear, validOwner, validcvccvv);
        creditgate.checkCardExpiredMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено более 5 лет в будущем")
    void overFiveYear() {
        var overFiveYear = DataHelper.getYear(6);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, overFiveYear, validOwner, validcvccvv);
        creditgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено не полностью")
    void notCompletelyYear() {
        var notCompletelyYear = DataHelper.getNumber(1);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, notCompletelyYear, validOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено излишне")
    void excessiveYear() {
        var excessiveYear = "250";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, excessiveYear, validOwner, validcvccvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год не заполнено")
    void emptyYear() {
        var emptyYear = DataHelper.getEmptyFieldValue();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, emptyYear, validOwner, validcvccvv);
        creditgate.checkValidationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено только фамилией")
    void surnameName() {
        var surnameOwner = DataHelper.getSurname();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, surnameOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено кириллицей")
    void cyrillicName() {
        var cyrillicOwner = DataHelper.getCyrillicName();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, cyrillicOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено спецсимволами")
    void symbolName() {
        var symbolOwner = DataHelper.getSymbols();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, symbolOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено цифрами")
    void numberName() {
        var numberOwner = DataHelper.getNumber(15);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, numberOwner, validcvccvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено излишне")
    void excessiveOwner() {
        var excessiveOwner = "ddddddddddddddddddddddddddddd ddddddddddddddddddddddddddddddddddd";
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, excessiveOwner, validcvccvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец не заполнено")
    void emptyName() {
        var emptyOwner = DataHelper.getEmptyFieldValue();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, emptyOwner, validcvccvv);
        creditgate.checkValidationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено буквами")
    void lettersCvcCvv() {
        var lettersCvcCvv = DataHelper.getValidName();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, lettersCvcCvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено спецсимволами")
    void symbolsCvcCvv() {
        var symbolsCvcCvv = DataHelper.getSymbols();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, symbolsCvcCvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено не полностью")
    void notCompletelyCvcCvv() {
        var notCompletelyCvcCvv = DataHelper.getNumber(1);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, notCompletelyCvcCvv);
        creditgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено заполнено излишне")
    void excessiveCvcCvv() {
        var excessiveCvcCvv = DataHelper.getNumber(4);
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, excessiveCvcCvv);
        creditgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV не заполнено")
    void emptyCvcCvv() {
        var emptyCvcCvv = DataHelper.getEmptyFieldValue();
        var creditgate = new CreditGate();
        creditgate.cleanField();
        creditgate.fillingCredForm(validCardNumber, validMonth, validYear, validOwner, emptyCvcCvv);
        creditgate.checkValidationMessage();
        assertNull(SQLHelper.getCreditStatus());
    }
}

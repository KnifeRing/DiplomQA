package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.PaymentGate;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class DebitTest {
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
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки с использованием не одобренной карты")
    void declinedCard() {
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(declinedCardNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkNotificationErrorIsVisible();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки с использованием рандомной карты")
    void randomCard() {
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(randomCardNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkNotificationErrorIsVisible();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено буквами")
    void lettersCardNumber() {
        var lettersNumber = DataHelper.getValidName();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(lettersNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено спецсимволами")
    void symbolsCardNumber() {
        var symbolsNumber = DataHelper.getSymbols();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(symbolsNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено не полностью")
    void notCompletelyCardNumber() {
        var notCompletelyNumber = DataHelper.getNumber(15);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(notCompletelyNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты заполнено излишне")
    void excessiveCardNumber() {
        var excessiveNumber = "4444 4444 4444 44411";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(excessiveNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Номер карты не заполнено")
    void emptyCardNumber() {
        var emptyCardNumber = DataHelper.getEmptyFieldValue();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(emptyCardNumber, validMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkValidationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено буквами")
    void lettersMonth() {
        var lettersMonth = DataHelper.getValidName();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, lettersMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено спецсимволами")
    void symbolsMonth() {
        var symbolsMonth = DataHelper.getSymbols();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, symbolsMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено не полностью")
    void notCompletelyMonth() {
        var notCompletelyMonth = DataHelper.getNumber(1);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, notCompletelyMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено излишне")
    void excessiveMonth() {
        var excessiveMonth = "120";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, excessiveMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено заполнено больше 12, но меньше 100")
    void thirteenMonth() {
        var excessiveMonth = "13";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, excessiveMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц заполнено заполнено 00")
    void zeroMonth() {
        var zeroMonth = "00";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, zeroMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Месяц не заполнено")
    void emptyMonth() {
        var emptyMonth = DataHelper.getEmptyFieldValue();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, emptyMonth, validYear, validOwner, validcvccvv);
        paymentgate.checkValidationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено буквами")
    void lettersYear() {
        var lettersYear = DataHelper.getValidName();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, lettersYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено спецсимволами")
    void symbolsYear() {
        var symbolsYear = DataHelper.getSymbols();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, symbolsYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено значением в прошлом")
    void lastYear() {
        var lastYear = DataHelper.getYear(-1);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, lastYear, validOwner, validcvccvv);
        paymentgate.checkCardExpiredMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено более 5 лет в будущем")
    void overFiveYear() {
        var overFiveYear = DataHelper.getYear(6);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, overFiveYear, validOwner, validcvccvv);
        paymentgate.checkWrongCardExpirationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено не полностью")
    void notCompletelyYear() {
        var notCompletelyYear = DataHelper.getNumber(1);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, notCompletelyYear, validOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год заполнено излишне")
    void excessiveYear() {
        var excessiveYear = "250";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, excessiveYear, validOwner, validcvccvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Год не заполнено")
    void emptyYear() {
        var emptyYear = DataHelper.getEmptyFieldValue();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, emptyYear, validOwner, validcvccvv);
        paymentgate.checkValidationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено только фамилией")
    void surnameName() {
        var surnameOwner = DataHelper.getSurname();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, surnameOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено кириллицей")
    void cyrillicName() {
        var cyrillicOwner = DataHelper.getCyrillicName();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, cyrillicOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено спецсимволами")
    void symbolName() {
        var symbolOwner = DataHelper.getSymbols();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, symbolOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено цифрами")
    void numberName() {
        var numberOwner = DataHelper.getNumber(15);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, numberOwner, validcvccvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец заполнено излишне")
    void excessiveOwner() {
        var excessiveOwner = "ddddddddddddddddddddddddddddd ddddddddddddddddddddddddddddddddddd";
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, excessiveOwner, validcvccvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле Владелец не заполнено")
    void emptyName() {
        var emptyOwner = DataHelper.getEmptyFieldValue();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, emptyOwner, validcvccvv);
        paymentgate.checkValidationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено буквами")
    void lettersCvcCvv() {
        var lettersCvcCvv = DataHelper.getValidName();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, lettersCvcCvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено спецсимволами")
    void symbolsCvcCvv() {
        var symbolsCvcCvv = DataHelper.getSymbols();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, symbolsCvcCvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено не полностью")
    void notCompletelyCvcCvv() {
        var notCompletelyCvcCvv = DataHelper.getNumber(1);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, notCompletelyCvcCvv);
        paymentgate.checkWrongFormatMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV заполнено заполнено излишне")
    void excessiveCvcCvv() {
        var excessiveCvcCvv = DataHelper.getNumber(4);
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, excessiveCvcCvv);
        paymentgate.checkNotificationSuccessIsVisible();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("Отправка заявки, в которой поле CVC/CVV не заполнено")
    void emptyCvcCvv() {
        var emptyCvcCvv = DataHelper.getEmptyFieldValue();
        var paymentgate = new PaymentGate();
        paymentgate.cleanPayField();
        paymentgate.fillingPayForm(validCardNumber, validMonth, validYear, validOwner, emptyCvcCvv);
        paymentgate.checkValidationMessage();
        assertNull(SQLHelper.getPaymentStatus());
    }
}

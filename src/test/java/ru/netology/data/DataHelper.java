package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRU = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    @Value
    public static class InfoCard {
        private String cardNumber;
        private String cardStatus;
    }

    public static InfoCard getApprovedCard() {

        return new InfoCard("4444 4444 4444 4441", "APPROVED");
    }

    public static InfoCard getDeclinedCard() {

        return new InfoCard("4444 4444 4444 4442", "DECLINED");
    }

    public static InfoCard getRandomCardNumber() {

        return new InfoCard(faker.business().creditCardNumber(), "");
    }

    public static String getEmptyFieldValue() {

        return "";
    }

    public static String getValidMonth() {

        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(int year) {

        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getValidName() {

        return faker.name().fullName();
    }

    public static String getSurname() {

        return faker.name().lastName();
    }

    public static String getCyrillicName() {

        return fakerRU.name().fullName();
    }

    public static String getNumber(int number) {

        return faker.number().digits(number);
    }

    public static String getSymbols() {
        String symbols = "!№%:?*";
        Random r = new Random();
        char c = symbols.charAt(r.nextInt(symbols.length()));
        return String.valueOf(c);
    }
}

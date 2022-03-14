import com.github.javafaker.Faker;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

@UtilityClass
public class DataGenerator {

    @UtilityClass
    public static class Registration {
        public static RegistrationInfo generateInfo(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationInfo(
                    generateCity(),
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber());
        }

        public static RegistrationInfo generateInfo(String locale, int days) {
            Faker faker = new Faker(new Locale(locale));
            return new RegistrationInfo(
                    generateCity(),
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber());
        }
    }

    private static final ArrayList<String> cities = new ArrayList<>();

    static {
        cities.add("Москва");
        cities.add("Санкт-Петербург");
        cities.add("Казань");
    }

    public static String generateCity() {
        return cities.get(new Random().nextInt(cities.size() - 1));
    }

    public static String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Data
    static class RegistrationInfo {
        private final String city;
        private final String fullName;
        private final String phoneNumber;
    }
}

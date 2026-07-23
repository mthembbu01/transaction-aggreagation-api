package za.co.capitec.creditcards.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class CreditCardUtils {

    public static String generateCardNumber() {
        long number = ThreadLocalRandom.current()
                .nextLong(1_000_000_000_000_0000L, 9_999_999_999_999_9999L);
        return String.valueOf(number);
    }
}


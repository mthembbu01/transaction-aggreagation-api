package za.co.capitec.creditcards.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class CreditCardUtils {

    public static Long generateAccNumber() {
        //--
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
    }

    public static Long generateCardNumber() {
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000_000_0000L, 9_999_999_999_999_9999L);
    }
}


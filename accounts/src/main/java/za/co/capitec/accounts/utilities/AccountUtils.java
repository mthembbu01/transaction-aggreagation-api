package za.co.capitec.accounts.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class AccountUtils {

    public static String generateAccNumber() {
        long number = ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
        //--
        return String.valueOf(number);
    }
}

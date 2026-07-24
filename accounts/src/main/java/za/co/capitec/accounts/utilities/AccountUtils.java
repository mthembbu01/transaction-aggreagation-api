package za.co.capitec.accounts.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class AccountUtils {

    public static Long generateAccNumber() {
        //--
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
    }
}

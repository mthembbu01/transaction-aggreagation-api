package za.co.capitec.loans.utilities;

import java.util.concurrent.ThreadLocalRandom;

public class LoanUtils {

    public static String generateLoanNumber() {
        long number = ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(number);
    }
}


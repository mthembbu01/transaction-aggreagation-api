package za.co.capitec.accounts.constants;

public final class AccountsConstants {

    private AccountsConstants() {
        // restrict instantiation
    }

    public static final String  STATUS_201 = "201";
    public static final String  MESSAGE_201 = "Record created successfully";
    public static final String  STATUS_200 = "200";
    public static final String  MESSAGE_200 = "Request processed successfully";
    public static final String  STATUS_204 = "204";
    public static final String  MESSAGE_204 = "Record deleted successfully";
    public static final String  STATUS_500 = "500";
    public static final String  MESSAGE_500_UPDATE= "Update operation failed. Please try again or contact Dev team";
    public static final String  MESSAGE_500_DELETE= "Delete operation failed. Please try again or contact Dev team";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final  String TIME_PATTERN = "HH:mm:ss";
    public static final String MAX_DATE = "9999-12-31";
    public static final String TIME_ZONE_ZA = "Africa/Johannesburg";
    public static final boolean ACTIVE_SW = true;
    public static final boolean IN_ACTIVE_SW = false;
}

package liquidacionautomatica;

import java.text.DecimalFormat;

public class Utils {

    private static final DecimalFormat currencyFormat = new DecimalFormat("$ ###,###.00");

    public static String toCurrencyFormat(double amount) {
        return currencyFormat.format(amount);
    }
}

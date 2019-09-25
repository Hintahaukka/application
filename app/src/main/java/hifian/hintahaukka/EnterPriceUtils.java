package hifian.hintahaukka;

public class EnterPriceUtils {

    public static String turnEnteredPriceToCents(String euros, String cents) {
        int eurosAsInt = 0;
        if(!euros.isEmpty()) {
            eurosAsInt = Integer.parseInt(euros);
        }

        int centsasInt = 0;
        if(!cents.isEmpty()) {
            centsasInt = Integer.parseInt(cents);
        }

        int result = eurosAsInt * 100 + centsasInt;
        return String.valueOf(result);
    }
}

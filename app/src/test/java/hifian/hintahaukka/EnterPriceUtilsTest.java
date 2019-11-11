package hifian.hintahaukka;

import org.junit.Test;

import hifian.hintahaukka.Service.EnterPriceUtils;

import static org.junit.Assert.assertEquals;

public class EnterPriceUtilsTest {

    @Test
    public void turnEnteredPriceToCentsWorksCorrectly() {
        String euros = "35";
        String cents = "05";

        String result = EnterPriceUtils.turnEnteredPriceToCents(euros, cents);

        assertEquals("3505", result);
    }

    @Test
    public void givingEmptyStringsToTurnEnteredPriceToCentsReturnsZero() {
        String result = EnterPriceUtils.turnEnteredPriceToCents("", "");
        assertEquals("0", result);
    }
}

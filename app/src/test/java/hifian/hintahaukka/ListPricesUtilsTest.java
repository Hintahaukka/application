package hifian.hintahaukka;


import org.junit.Test;
import hifian.hintahaukka.Service.ListPricesUtils;
import hifian.hintahaukka.Service.PriceListItem;

import static org.junit.Assert.assertEquals;


public class ListPricesUtilsTest {

    @Test
    public void calculatingAveragePriceWorksCorrectly() {
        PriceListItem[] priceListItem = new PriceListItem[2];
        priceListItem[0] = new PriceListItem(100, null, null);
        priceListItem[1] = new PriceListItem(200, null, null);


        double average = ListPricesUtils.getAveragePrice(priceListItem, 3);
        assertEquals(2, average, 0.01);

    }

    @Test
    public void calculatingDifferencePercentageToAveragePriceWorksCorrectly() {
        PriceListItem[] priceListItem = new PriceListItem[2];
        priceListItem[0] = new PriceListItem(100, null, null);
        priceListItem[1] = new PriceListItem(200, null, null);


        double average = ListPricesUtils.getAveragePrice(priceListItem, 3);

        int differencePercentage = ListPricesUtils.getDifferenceToAveragePriceInPercentages(3, average);
        assertEquals(50, differencePercentage);
    }
}

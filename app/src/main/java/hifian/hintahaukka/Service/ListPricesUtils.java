package hifian.hintahaukka.Service;




public class ListPricesUtils {

    public static double getAveragePrice(PriceListItem[] priceListItem, double enteredPrice) {

        double sum = enteredPrice*100;

        for (int i = 0; i < priceListItem.length; i++) {
            sum = sum + priceListItem[i].getCents();
        }
        sum = sum / (priceListItem.length+1);
        sum = sum/100;
        sum = Math.round(sum * 100.0) / 100.0;
        return sum;
    }

    public static int getDifferenceToAveragePriceInPercentages(double enteredPrice ,double averagePrice) {
        double difference = ((enteredPrice/averagePrice)*100)-100;
        int difference1 = (int) Math.round(difference);
        return difference1;
    }

}
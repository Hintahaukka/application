package hifian.hintahaukka.Domain;

public class Price {
    private double cents;

    public Price(int cents) {
        this.cents = cents;
    }

    public void setCents(double cents) {
        this.cents = cents;
    }

    public double getCents() {
        return this.cents;
    }
}

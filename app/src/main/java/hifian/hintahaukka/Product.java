package hifian.hintahaukka;

public class Product {
    private String ean;
    private String name;


    public Product(String ean, String name) {
        this.ean = ean;
        this.name = name;
    }

    public String getEan() {
        return this.ean;
    }

    public String getName() {
        return this.name;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setName(String name) {
        this.name = name;
    }
}

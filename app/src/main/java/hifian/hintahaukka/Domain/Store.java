package hifian.hintahaukka.Domain;

public class Store {
    private String storeId;
    private String name;
    private double lat;
    private double lon;

    public Store() { }

    public Store(String storeId, String name, double lat, double lon) {
        this.storeId = storeId;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}

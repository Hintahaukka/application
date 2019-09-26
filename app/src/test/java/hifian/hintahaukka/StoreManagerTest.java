package hifian.hintahaukka;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class StoreManagerTest {

    private StoreManager storeManager;

    private String testData = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<osm version=\"0.6\" generator=\"Osmosis 0.47\">\n" +
            "  <bounds minlon=\"19.02427\" minlat=\"59.28783\" maxlon=\"31.60089\" maxlat=\"70.09959\" origin=\"0.47\"/>\n" +
            "  <node id=\"26197451\" version=\"7\" timestamp=\"2010-11-04T15:20:07Z\" uid=\"0\" user=\"\" lat=\"61.0643\" lon=\"28.1734914\">\n" +
            "  </node>\n" +
            "  <node id=\"30288487\" version=\"13\" timestamp=\"2019-09-12T18:32:42Z\" uid=\"0\" user=\"\" lat=\"60.450691\" lon=\"22.2628898\">\n" +
            "    <tag k=\"addr:city\" v=\"Turku\"/>\n" +
            "    <tag k=\"name\" v=\"Stockmann Turku\"/>\n" +
            "    <tag k=\"name:ru\" v=\"Стокманн Турку\"/>\n" +
            "    <tag k=\"name:sv\" v=\"Stockmann Åbo\"/>\n" +
            "    <tag k=\"opening_hours\" v=\"Mo-Fr 09:00-20:00; Sa 10:00-19:00; Su 12:00-18:00\"/>\n" +
            "    <tag k=\"phone\" v=\"+358 9 1211\"/>\n" +
            "  </node>\n" +
            "  <node id=\"30326364\" version=\"6\" timestamp=\"2019-05-27T17:36:08Z\" uid=\"0\" user=\"\" lat=\"60.457508\" lon=\"22.2335554\">\n" +
            "    <tag k=\"addr:city\" v=\"Turku\"/>\n" +
            "    <tag k=\"addr:housenumber\" v=\"4\"/>\n" +
            "    <tag k=\"addr:postcode\" v=\"20250\"/>\n" +
            "    <tag k=\"addr:street\" v=\"Pitkämäenkatu\"/>\n" +
            "    <tag k=\"brand\" v=\"K-Supermarket\"/>\n" +
            "    <tag k=\"brand:wikidata\" v=\"Q5408668\"/>\n" +
            "    <tag k=\"brand:wikipedia\" v=\"fi:K-Supermarket\"/>\n" +
            "    <tag k=\"name\" v=\"K-Supermarket Manhattan\"/>\n" +
            "    <tag k=\"self_checkout\" v=\"yes\"/>\n" +
            "    <tag k=\"shop\" v=\"supermarket\"/>\n" +
            "    <tag k=\"website\" v=\"https://www.k-supermarket.fi/kaupat/manhattan/\"/>\n" +
            "  </node>\n" +
            "</osm>";



    @Test
    public void listNearestStoresMethodWorks() {
        this.useAllData();

        List<String> storeIdList = storeManager.listNearestStores(60.317435, 24.849496);
        assertEquals(10, storeIdList.size());
        assertEquals("K-Market Kivistö", storeManager.getStore(storeIdList.get(0)).getName());

        storeIdList = storeManager.listNearestStores(61.491727, 23.790583);
        assertEquals(10, storeIdList.size());
        assertEquals("Sale Järvensivu", storeManager.getStore(storeIdList.get(0)).getName());

        storeIdList = storeManager.listNearestStores(61.496632, 23.802789);
        assertEquals(10, storeIdList.size());
        assertEquals("K-Market Domus", storeManager.getStore(storeIdList.get(0)).getName());

        storeIdList = storeManager.listNearestStores(61.508963, 23.778021);
        assertEquals(10, storeIdList.size());
        assertEquals("K-Market Lapinniemi", storeManager.getStore(storeIdList.get(0)).getName());
    }



    private void useAllData() {
        this.storeManager = new StoreManager();
        try {
            InputStream istream = this.getClass().getClassLoader().getResourceAsStream("stores.osm");
            storeManager.fetchStores(istream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void useTestData() {
        this.storeManager = new StoreManager();
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        this.storeManager.fetchStores(inputStream);
    }

    @Test
    public void allStoresAreInTheList() {
        this.useTestData();
        List<Store> stores = storeManager.getStores();
        assertEquals(3, stores.size());
    }

    @Test
    public void storeCanBeFoundWithStoreId() {
        this.useTestData();
        Store s = storeManager.getStore("30288487");
        assertEquals("Stockmann Turku", s.getName());
    }

}

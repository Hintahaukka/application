package hifian.hintahaukka;

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

public class StoreManagerTest {

    @Test
    public void listNearestStoresMethodWorks() {
        StoreManager storeManager = new StoreManager();
        try {
            InputStream istream = this.getClass().getClassLoader().getResourceAsStream("stores.osm");
            storeManager.fetchStores(istream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

}

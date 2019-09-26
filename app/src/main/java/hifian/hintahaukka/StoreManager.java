package hifian.hintahaukka;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Class contains methods to manage Store objects
 */
public class StoreManager {

    private List<Store> stores;
    private HashMap<String, Store> storeMap;

    public StoreManager() {
        this.stores = new ArrayList<>();
        this.storeMap = new HashMap<>();
    }

    public void fetchStores(InputStream istream) {
        this.stores = this.handleStores(istream);
        for (Store s: stores) {
            this.storeMap.put(s.getStoreId(), s);
        }
    }

    public List<Store> getStores() {
        return this.stores;
    }


    /**
     * Sorts stores into ascending order according to their distances to the given reference location.
     */
    class StoreDistanceComparator implements Comparator<Store> {
        private double lat;
        private double lon;
        /**
         * @param lat The latitude of the reference location.
         * @param lon The longitude of the reference location.
         */
        public StoreDistanceComparator(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
        public int compare(Store s1, Store s2) {
            //Store s1 distance to reference location.
            double distanceS1 = Math.hypot(s1.getLat() - lat, s1.getLon() - lon);
            //Store s2 distance to reference location.
            double distanceS2 = Math.hypot(s2.getLat() - lat, s2.getLon() - lon);

            if(distanceS1 < distanceS2) {
                return -1;
            } else if(distanceS1 > distanceS2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Finds the nearest stores and converts them into Strings
     * @param lat The current latitude
     * @param lon The current longitude
     * @return A list of stores nearest to the coordinates
     */
    public List<String> listNearestStores(double lat, double lon) {
        Collections.sort(this.stores, new StoreDistanceComparator(lat, lon));

        int numberOfStoresToReturn = 10;
        if(numberOfStoresToReturn > stores.size()) numberOfStoresToReturn = stores.size();

        List<String> storesToStringList = new ArrayList<>();
        for (int i = 0; i < numberOfStoresToReturn; i++) {
            storesToStringList.add(stores.get(i).getStoreId());
        }
        return storesToStringList;
    }

    /**
     * Find the Store with the given id
     * @param id Store id
     * @return Store matching the id
     */
    public Store getStore(String id) {
        return this.storeMap.get(id);
    }

    /**
     * Calls the StoreHandler to access the list of Store objects
     * @return list of Store objects
     */
    private List<Store> handleStores(InputStream istream) {

        List<Store> stores = new ArrayList<>();
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            StoreHandler storeHandler = new StoreHandler();
            saxParser.parse(istream, storeHandler);

            stores = storeHandler.getStores();

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return stores;
    }

}

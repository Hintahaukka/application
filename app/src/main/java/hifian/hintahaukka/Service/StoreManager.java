package hifian.hintahaukka.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hifian.hintahaukka.Domain.Store;

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

    /**
     * Initializes the StoreManager with data from the InputStream
     * @param istream InputStream containing Store osm-data.
     */
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
     * Finds the nearest stores
     * @param lat The current latitude
     * @param lon The current longitude
     * @return A list of stores nearest to the coordinates
     */
    public List<Store> listNearestStores(double lat, double lon) {
        Collections.sort(this.stores, new StoreDistanceComparator(lat, lon));

        int numberOfStoresToReturn = 10;
        if(numberOfStoresToReturn > stores.size()) numberOfStoresToReturn = stores.size();

        List<Store> storesToList = new ArrayList<>();
        for (int i = 0; i < numberOfStoresToReturn; i++) {
            // Include stores only within 500m (500m is approx. 0.009094 in lat&lon difference).
            double dist = Math.hypot(stores.get(i).getLat() - lat, stores.get(i).getLon() - lon);
            if(dist > 0.009094) break;

            storesToList.add(stores.get(i));
        }
        return storesToList;
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

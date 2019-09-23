package hifian.hintahaukka;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     * Finds the nearest stores and converts them into Strings
     * @param lat The current latitude
     * @param lon The current longitude
     * @return A list of stores nearest to the coordinates
     */
    public List<String> listNearestStores(double lat, double lon) {
        List<Store> nearestStores = findNearestStores(lat, lon);
        List<String> storesToStringList = new ArrayList<>();
        for (int i = 0; i < nearestStores.size(); i ++) {
            storesToStringList.add(stores.get(i).getStoreId());
        }
        return storesToStringList;
    }

    //TODO: Return nearest 10 stores instead of theses examples
    protected List<Store> findNearestStores(double lat, double lon) {
        List<Store> exampleList = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            exampleList.add(stores.get(i));
        }
        return exampleList;
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

package hifian.hintahaukka;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains methods to manage Store objects
 */
public class StoreManager {

    private List<Store> stores;

    public StoreManager() {
        this.stores = new ArrayList<>();
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
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

}

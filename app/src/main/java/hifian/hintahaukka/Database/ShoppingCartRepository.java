package hifian.hintahaukka.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingCartRepository {

    private ShoppingCartDao shoppingCartDao;
    private LiveData<List<Product>> products;

    public ShoppingCartRepository(Application application) {
        ShoppingCartDatabase db = ShoppingCartDatabase.getDatabase(application);
        shoppingCartDao = db.shoppingCartDao();
        products = shoppingCartDao.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void insert(Product product) {
        ShoppingCartDatabase.databaseWriteExecutor.execute(() -> {
            shoppingCartDao.insert(product);
        });
    }

    public void delete(Product product) {
        ShoppingCartDatabase.databaseWriteExecutor.execute(() -> {
            shoppingCartDao.delete(product);
        });
    }
}

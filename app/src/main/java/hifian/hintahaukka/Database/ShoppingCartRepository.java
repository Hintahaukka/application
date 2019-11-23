package hifian.hintahaukka.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingCartRepository {

    private ShoppingCartDao shoppingCartDao;
    private LiveData<List<ShoppingCart>> shoppingCarts;

    public ShoppingCartRepository(Application application) {
        ShoppingCartDatabase db = ShoppingCartDatabase.getDatabase(application);
        shoppingCartDao = db.shoppingCartDao();
        shoppingCarts = shoppingCartDao.getShoppingCarts();
    }

    public LiveData<List<ShoppingCart>> getShoppingCarts() {
        return shoppingCarts;
    }

    public void insert(ShoppingCart shoppingCart) {
        ShoppingCartDatabase.databaseWriteExecutor.execute(() -> {
            shoppingCartDao.insert(shoppingCart);
        });
    }
}

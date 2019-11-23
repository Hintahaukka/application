package hifian.hintahaukka.GUI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hifian.hintahaukka.Database.ShoppingCart;
import hifian.hintahaukka.Database.ShoppingCartRepository;

public class ShoppingCartViewModel extends AndroidViewModel {

    private ShoppingCartRepository repository;
    private LiveData<List<ShoppingCart>> shoppingCarts;

    public ShoppingCartViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingCartRepository(application);
        shoppingCarts = repository.getShoppingCarts();
    }

    public LiveData<List<ShoppingCart>> getShoppingCarts() {
        return shoppingCarts;
    }

    public void insert(ShoppingCart shoppingCart) {
        repository.insert(shoppingCart);
    }
}

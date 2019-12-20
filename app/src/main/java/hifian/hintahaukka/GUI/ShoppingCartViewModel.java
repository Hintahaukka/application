package hifian.hintahaukka.GUI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.Database.ShoppingCartRepository;

public class ShoppingCartViewModel extends AndroidViewModel {

    private ShoppingCartRepository repository;
    private LiveData<List<Product>> products;

    public ShoppingCartViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingCartRepository(application);
        products = repository.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void insert(Product product) {
        repository.insert(product);
    }

    public void delete(Product product) { repository.delete(product); }
}

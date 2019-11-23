package hifian.hintahaukka.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "shopping_cart_table")
public class ShoppingCart {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;

    public ShoppingCart(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package hifian.hintahaukka.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;
    private String ean;

    public Product(String name, String ean) {
        this.name = name;
        this.ean = ean;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }
}

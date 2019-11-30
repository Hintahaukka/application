package hifian.hintahaukka.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Products")
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;

    private String ean;
    private String name;


    public Product(String ean, String name) {
        this.ean = ean;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }
}

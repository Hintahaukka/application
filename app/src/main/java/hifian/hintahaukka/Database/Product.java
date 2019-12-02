package hifian.hintahaukka.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Products")
public class Product {

    @NonNull
    @PrimaryKey
    private String ean;

    private String name;


    public Product(@NonNull String ean, String name) {
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

package hifian.hintahaukka.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.R;

public class ShoppingCartListAdapter extends RecyclerView.Adapter<ShoppingCartListAdapter.ShoppingCartViewHolder>  {

    class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        private final TextView cartNameView;

        private ShoppingCartViewHolder(View itemView) {
            super(itemView);
            cartNameView = itemView.findViewById(R.id.text_shopping_cart_name);
        }
    }

    private final LayoutInflater inflater;
    private List<Product> products; //Cached copy of products

    ShoppingCartListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.shopping_cart_list_item, parent, false);
        return new ShoppingCartViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        if (products != null) {
            Product current = products.get(position);
            holder.cartNameView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet
        }
    }

    void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        } else {
            return 0;
        }
    }
}

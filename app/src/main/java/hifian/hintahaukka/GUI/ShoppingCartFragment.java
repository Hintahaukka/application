package hifian.hintahaukka.GUI;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hifian.hintahaukka.Database.Product;
import hifian.hintahaukka.R;


public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel viewModel;


    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        
        RecyclerView shoppingCartList = view.findViewById(R.id.shopping_cart_list);
        ShoppingCartListAdapter adapter = new ShoppingCartListAdapter(getContext());
        shoppingCartList.setAdapter(adapter);
        shoppingCartList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(getActivity()).get(ShoppingCartViewModel.class);
        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
            }
        });

        return view;
    }

}

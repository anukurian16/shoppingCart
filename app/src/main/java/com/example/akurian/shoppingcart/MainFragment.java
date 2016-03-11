package com.example.akurian.shoppingcart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.akurian.shoppingcart.adapters.ProductListAdapter;
import com.example.akurian.shoppingcart.models.DataHolder;
import com.example.akurian.shoppingcart.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements SearchView.OnQueryTextListener {

    private Button btShop;
    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private List<Product> productList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

        btShop = (Button) view.findViewById(R.id.btShop);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        productList = new ArrayList<>();
        for (int i = 0; i < DataHolder.store.size(); i++) {
            productList.addAll(DataHolder.store.get(i).getProductList());
        }

        productListAdapter = new ProductListAdapter(getActivity(), productList, btShop);
        recyclerView.setAdapter(productListAdapter);
    }

    private List<Product> filter(List<Product> list, String query) {
        query = query.toLowerCase();
        final List<Product> filteredList = new ArrayList<>();
        for (Product item : list) {
            final String text = item.getName().toLowerCase();
            if (text.contains(query)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        System.out.println("Query : " + query);

        final List<Product> filteredList = filter(productList, query);
        productListAdapter.updateDataSet(filteredList);
        productListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);
        return true;
    }
}

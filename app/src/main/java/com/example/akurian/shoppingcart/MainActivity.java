package com.example.akurian.shoppingcart;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.akurian.shoppingcart.models.Category;
import com.example.akurian.shoppingcart.models.DataHolder;
import com.example.akurian.shoppingcart.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        MainFragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }

    private void loadData() {
        try {
            DataHolder.store = new ArrayList<>();

            int uid = 0;
            JSONArray data = loadJSONFromAsset();
            for (int i = 0; i < data.length(); i++) {
                JSONObject catJSON = data.getJSONObject(i);

                Category category = new Category();
                category.setName(catJSON.getString("category"));
                category.setLatitude(catJSON.getDouble("latitude"));
                category.setLongitude(catJSON.getDouble("longitude"));

                List<Product> products = new ArrayList<>();
                JSONArray proArray = catJSON.getJSONArray("products");
                for (int j = 0; j < proArray.length(); j++) {
                    JSONObject proJSON = proArray.getJSONObject(j);

                    Product product = new Product();
                    product.setName(proJSON.getString("name"));
                    product.setCost(proJSON.getInt("cost"));
                    product.setCategory(category.getName());
                    product.setUID(String.valueOf(uid++));
                    product.setImage("");

                    products.add(product);
                }
                category.setProductList(products);

                DataHolder.store.add(category);
            }

            System.out.println(data.toString());
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private JSONArray loadJSONFromAsset() {
        JSONArray json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new JSONArray(new String(buffer, "UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return json;
    }
}

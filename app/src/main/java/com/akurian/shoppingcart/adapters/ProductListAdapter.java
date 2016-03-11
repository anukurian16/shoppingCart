package com.akurian.shoppingcart.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akurian.shoppingcart.R;
import com.akurian.shoppingcart.models.DataHolder;
import com.akurian.shoppingcart.models.Product;

import java.util.List;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private Context context;
    private Button btShop;
    private List<Product> productList;

    public ProductListAdapter(Context context, List<Product> productList, Button btShop) {
        this.context = context;
        this.productList = productList;
        this.btShop = btShop;
    }

    public void updateDataSet(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategory());
        holder.tvCost.setText("\u20B9 " + String.valueOf(product.getCost()));

        holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataHolder.cart.contains(product))
                    DataHolder.cart.add(product);
                btShop.setText("Shop Now ( " + DataHolder.cart.size() + " )");
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvCategory, tvCost;
        public Button btAdd;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);

            btAdd = (Button) itemView.findViewById(R.id.btAdd);
        }
    }
}

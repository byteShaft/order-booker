package com.byteshaft.order_booker.activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byteshaft.order_booker.AppGlobals;
import com.byteshaft.order_booker.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private TextView itemCount;
    private RelativeLayout cartLayout;
    private TextView totalAmountTextView;
    private ArrayList<String> finalItems;
    private ArrayList<String> allValues;
    private int amount;
    private ListView listView;
    private View viewLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        finalItems = new ArrayList<>();
        getProductsFromHashMap();
        itemCount = (TextView) findViewById(R.id.item_count);
        itemCount.setTypeface(AppGlobals.typeface);
        cartLayout = (RelativeLayout) findViewById(R.id.cart_layout);
        totalAmountTextView = (TextView) findViewById(R.id.total_amount);
        totalAmountTextView.setTypeface(AppGlobals.typeface);
        listView = (ListView) findViewById(R.id.list_view_cart);
        viewLine = (View) findViewById(R.id.viewLine);
        initializeAllData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> arrayAdapter = new CartAdapter(getApplicationContext(),
                R.layout.single_cart_item, finalItems);
        listView.setAdapter(arrayAdapter);
    }

    private void getProductsFromHashMap() {
        for ( String key : AppGlobals.getFinalOrdersHashMap().keySet() ) {
            finalItems.add(key);
        }
    }

    private void initializeAllData() {
        amount = 0;
        allValues = new ArrayList<>();
        itemCount.setText("(" + AppGlobals.getFinalOrdersHashMap().size() + ")");
        if (AppGlobals.getFinalOrdersHashMap().size() == 0) {
            alertDialog();
            cartLayout.setVisibility(View.INVISIBLE);
            viewLine.setVisibility(View.INVISIBLE);
        } else {
            cartLayout.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        }
        for (String key : AppGlobals.getFinalOrdersHashMap().keySet()) {
            allValues.add(AppGlobals.getFinalOrdersHashMap().get(key));
        }
        System.out.println(allValues);
        for(String value: allValues) {
            int val =  Integer.valueOf(value.replaceAll("[a-zA-Z]", "").replace(".", "").replace(" ", ""));
            amount = amount+val;
        }
        totalAmountTextView.setText("Total amount: "+String.valueOf(amount)+ " L.L");
    }
    public void alertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No item");
        alertDialogBuilder
                .setMessage("You have no item in the cart")
                .setCancelable(false)
                .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(AppGlobals.getContext(), ProductsActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class CartAdapter extends ArrayAdapter<String> {

        private ArrayList<String> arrayList;
        private int layoutResource;

        public CartAdapter(Context context, int resource, ArrayList<String> items) {
            super(context, resource, items);
            this.arrayList = items;
            this.layoutResource = resource;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(layoutResource, parent, false);
                holder = new ViewHolder();
                holder.deleteItem = (ImageButton) convertView.findViewById(R.id.delete);
                holder.productName = (TextView) convertView.findViewById(R.id.product_name);
                holder.productName.setTypeface(AppGlobals.typeface);
                holder.productPrice = (TextView) convertView.findViewById(R.id.product_price);
                holder.productPrice.setTypeface(AppGlobals.typeface);
                holder.qtyText = (TextView) convertView.findViewById(R.id.quantity_text);
                holder.qtyText.setTypeface(AppGlobals.typeface);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.productName.setText(arrayList.get(position));
            holder.productPrice.setText(AppGlobals.getFinalOrdersHashMap().get(arrayList.get(position)));
            holder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(arrayList.get(position));
                    AppGlobals.removeOrderFromHashMap(arrayList.get(position));
                    finalItems.remove(arrayList.get(position));
                    totalAmountTextView.setText("");
                    allValues = null;
                    notifyDataSetChanged();
                    initializeAllData();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageButton deleteItem;
        public TextView productName;
        public TextView productPrice;
        public TextView qtyText;
    }



}

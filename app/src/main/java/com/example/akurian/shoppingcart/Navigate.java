package com.example.akurian.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Navigate extends AppCompatActivity {
    ArrayList<Integer> listOfNodes;
    ListView mylistview;
    ArrayAdapter<Integer> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listOfNodes=new ArrayList<Integer>();
        Intent i = getIntent();
        ArrayList<String> list = i.getStringArrayListExtra("arraylist");
        for(int j=0;j<list.size();j++)
        {
            String s= list.get(j);
            System.out.println(s);
            switch(s){
                case "fruits":
                    System.out.println("case 1");
                    listOfNodes.add(1);
                    break;
                case "biscuits":
                    System.out.println("case 2");
                    listOfNodes.add(2);
                    break;
                case "vegetables":
                    System.out.println("case 3");
                    listOfNodes.add(3);
                    break;
                case "beverages":
                    System.out.println("case 4");
                    listOfNodes.add(4);
                    break;
                case "bakery":
                    listOfNodes.add(5);
                    break;
                case "meat,fish& poultry":
                    listOfNodes.add(6);
                    break;
                case "household":
                    listOfNodes.add(7);
                    break;
                case "cereals":
                    listOfNodes.add(8);
                    break;
            }

        }
        try {
            Log.e("try", "entered");
            ArrayList<Integer> path = new ShortestPath().bellman(listOfNodes);
            Log.e("back from","bellman");
            mylistview = (ListView) findViewById(R.id.listView1);
            listAdapter = new ArrayAdapter<Integer>(Navigate.this, android.R.layout.simple_list_item_1,path);
            mylistview.setAdapter(listAdapter);
            Log.e("Listview",mylistview.toString());
            for(int j=0;j<path.size();j++){
                Log.e("inside","loop");
                Log.isLoggable("-->",path.get(j));
            }
        }
        catch(Exception e){

        }



    }

}

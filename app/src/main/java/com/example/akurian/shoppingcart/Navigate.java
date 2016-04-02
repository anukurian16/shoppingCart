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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Navigate extends AppCompatActivity {
    ArrayList<Integer> listOfNodes;
    ListView mylistview;
    ArrayAdapter<Integer> listAdapter;
    Node[] routingTable = new Node[20];
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
//        try {
//            Log.e("try", "entered");
//            ArrayList<Integer> path = new ShortestPath().bellman(listOfNodes);
//            Log.e("back from","bellman");
//            mylistview = (ListView) findViewById(R.id.listView1);
//            listAdapter = new ArrayAdapter<Integer>(Navigate.this, android.R.layout.simple_list_item_1,path);
//            mylistview.setAdapter(listAdapter);
//            Log.e("Listview",mylistview.toString());
//            for(int j=0;j<path.size();j++){
//                Log.e("inside","loop");
//                Log.isLoggable("-->",path.get(j));
//            }
//        }
//        catch(Exception e){
//
//        }

        try{
            Log.e("call","bellman");
            ArrayList<Integer> path = bellman(listOfNodes);
            Log.e("back","bellman");
            mylistview = (ListView) findViewById(R.id.listView1);
            listAdapter = new ArrayAdapter<Integer>(Navigate.this, android.R.layout.simple_list_item_1,path);
            mylistview.setAdapter(listAdapter);
        }
        catch (Exception e){}

    }

    public ArrayList<Integer> bellman(ArrayList<Integer> listOfNodes)throws Exception{

        int adjacencyMatrix[][] = new int[20][20];
        int min, pos, cur=0, next, prev, x;
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        ArrayList<Integer> path = new ArrayList<Integer>();
        Iterator iterator = path.iterator();
        //InputStream inputStream=openFileInput("data.txt");
        //InputStream input = getAssets().open("numbers.txt");
        Scanner scanner = new Scanner(getAssets().open(String.format("data.txt")));
        //reader = new Scanner(new File(getAssets().open(String.format("myFile.txt"))));

        Log.e("in func","bellman");
        for(int i=0;i<routingTable.length;i++){
            routingTable[i]=new Node();
        }

        int noOfNodes=scanner.nextInt();
        for(int i=0;i<noOfNodes;i++){
            for(int j=0;j<noOfNodes;j++){
                adjacencyMatrix[i][j] = scanner.nextInt();
                adjacencyMatrix[i][i]=0;
                routingTable[i].dist[j]=adjacencyMatrix[i][j];
                routingTable[i].from[j]=j;
            }

        }
        int count=0;
        do{
            count=0;
            for(int i=0;i<noOfNodes;i++){
                for(int j=0;j<noOfNodes;j++){
                    for(int k=0;k<noOfNodes;k++){
                        if((routingTable[i].dist[j])>(routingTable[i].dist[k]+adjacencyMatrix[k][j])){
                            routingTable[i].dist[j]=routingTable[i].dist[k]+adjacencyMatrix[k][j];
                            routingTable[i].from[j]=k;
                            count++;
                        }
                    }
                }
            }
        }
        while(count!=0);



        while(!listOfNodes.isEmpty()){
            min=999;
            pos=cur;
            for(int j=0;j<noOfNodes;j++){
                if(cur==j)
                    continue;
                if(checkInlist(j,listOfNodes)){
                    if(routingTable[cur].dist[j]<min){
                        min=routingTable[cur].dist[j];
                        pos=j;
                    }
                }
            }
            next=pos;
            tempList.add(next);
            while(true){
                prev=routingTable[cur].from[next];
                if(prev==next)
                    break;
                tempList.add(prev);
                next=prev;
            }
            cur=pos;

            Integer k=new Integer(pos);
            listOfNodes.remove(listOfNodes.indexOf(k));
            while(!tempList.isEmpty()){
                x=tempList.get((tempList.size()-1));
                tempList.remove((tempList.size()-1));
                path.add(x);
            }

        }
        Log.e("end of","bellman");
        return path;

    }


    public boolean checkInlist(int k,ArrayList<Integer> listOfNodes){
        Iterator iterator = listOfNodes.iterator();
        while (iterator.hasNext()) {
            if((Integer)k==iterator.next())
                return true;
        }
        return false;
    }

}

package com.example.akurian.shoppingcart;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class ShortestPath {
    Node[] routingTable = new Node[20];

    public boolean checkInlist(int k,ArrayList<Integer> listOfNodes){
        Iterator iterator = listOfNodes.iterator();
        while (iterator.hasNext()) {
            if((Integer)k==iterator.next())
                return true;
        }
        return false;
    }


    public ArrayList<Integer> bellman(ArrayList<Integer> listOfNodes)throws Exception{

        int adjacencyMatrix[][] = new int[20][20];
        int min, pos, cur=0, next, prev, x;
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        ArrayList<Integer> path = new ArrayList<Integer>();
        Iterator iterator = path.iterator();
        Scanner scanner = new Scanner(new File("/Users/akurian/samples/src/main/java/org/sample/data.txt"));
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


}

package com.example.minesweepermcalaineaidancs310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Establish row/column counts
    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;

    //Declare arraylist of cells + boolean 2d array to keep track of bombs
    private ArrayList<TextView> cells;
    String[][] bombPlacements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize cells
        cells = new ArrayList<>();

        //Initialize 2D boolean array to keep track of bomb placements
        bombPlacements = new String[ROW_COUNT][COLUMN_COUNT];
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                bombPlacements[i][j] = "";
            }
        }

        //Add dynamically created cells with LayoutInflater
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {

                //Create text view - individual cell
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                //Configure
                tv.setTextColor(Color.GRAY);
//                tv.setText("");
                tv.setBackgroundColor(Color.GRAY);
                tv.setOnClickListener(this::onClickTV);
                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                //Add to grid and array list of cells
                grid.addView(tv, lp);

                cells.add(tv);
            }
        }

        //At this point we have initialized the 2d Arraylist of TextViews, and a 2d Boolean Array to keep
        //track of the bombs

        //We now need to randomly add bombs to the 2d boolean array
        //Add bomb placements to cells
        int bombCounter = 0;
        Random rand = new Random();
        while (bombCounter < 4) {

            //generate random row value, random column value
            int rowVal = rand.nextInt(ROW_COUNT);
            int colVal = rand.nextInt(COLUMN_COUNT);

            //ensure we haven't already placed a bomb there:
            if (bombPlacements[rowVal][colVal].equals("")) {
                //place the bomb
                bombPlacements[rowVal][colVal] = "b";
                bombCounter += 1;
            }
        }

        //Iterate over the string array and record the number of bombs nearby, placing these values
        //into the array
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {

                //record the number of adjacent/diagonal mines (8 possible squares for each spot)

                //if the square is a bomb just continue
                if (bombPlacements[i][j].equals("b")) {
                    continue;
                }

                int bombCount = 0;

                //Case 1: top left
                if ((i - 1) >= 0 && (j - 1) >= 0) {
                    //(i-1,j-1)
                    if (bombPlacements[i - 1][j - 1].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 2: up middle
                if ((j - 1) >= 0) {
                    //(i,j-1)
                    if (bombPlacements[i][j - 1].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 3: top right
                if ((i + 1) < ROW_COUNT && (j - 1) >= 0) {
                    //(i+1,j-1)
                    if (bombPlacements[i + 1][j - 1].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 4: left
                if ((i - 1) >= 0) {
                    //(i-1,j)
                    if (bombPlacements[i - 1][j].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 5: right
                if ((i + 1) < ROW_COUNT) {
                    //(i+1,j)
                    if (bombPlacements[i + 1][j].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 6: bottom left
                if ((i - 1) >= 0 && (j + 1) < COLUMN_COUNT) {
                    //(i-1,j+1)
                    if (bombPlacements[i - 1][j + 1].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 7: bottom down
                if ((j + 1) < COLUMN_COUNT) {
                    //(i,j+1)
                    if (bombPlacements[i][j + 1].equals("b")) {
                        bombCount += 1;
                    }
                }
                //Case 8: bottom right
                if ((i + 1) < ROW_COUNT && (j + 1) < COLUMN_COUNT) {
                    //(i+1,j+1)
                    if (bombPlacements[i + 1][j + 1].equals("b")) {
                        bombCount += 1;
                    }
                }

                //Assign bombCount value to appropriate location
                if (bombCount > 0) {
                    bombPlacements[i][j] = Integer.toString(bombCount);
                } else {
                    bombPlacements[i][j] = " ";
                }
            }
        }

        //Debug
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                if (bombPlacements[i][j].equals("b")) {
                    System.out.println(i + " " + j);
                }
            }
        }
        System.out.println("-");
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                System.out.println(i + " " + j);
                System.out.println(bombPlacements[i][j]);
            }
        }

    }

    //Private helper function: find index of cell text view
    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cells.size(); n++) {
            if (cells.get(n) == tv)
                return n;
        }
        //return -1 if unable to find
        return -1;
    }
    public void onClickTV(View view){

        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        String cellText = i + "" + j;
        tv.setText(cellText);
        if (tv.getCurrentTextColor() == Color.GRAY) {
            tv.setTextColor(Color.GREEN);
            tv.setBackgroundColor(Color.parseColor("lime"));
        }else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }
    }
}
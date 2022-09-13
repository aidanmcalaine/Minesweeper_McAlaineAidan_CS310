package com.example.minesweepermcalaineaidancs310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    //Establish row/column counts
    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;

    //Additional member variables
    boolean mining = true;
    boolean flagging = false;
    TextView gameMode;

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

        //Initialize a textview at the bottom of the screen to represent the mode (mining/flagging)
        gameMode = (TextView) findViewById(R.id.textViewBottom);
        gameMode.setOnClickListener(this::onGamemodeChange);
        gameMode.setText(R.string.pick);

        //Add dynamically created cells with LayoutInflater
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {

                //Create text view - individual cell
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                //Configure
                tv.setTextColor(Color.GRAY);
                tv.setText("");

                tv.setBackgroundColor(Color.parseColor("lime"));
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
                    bombPlacements[i][j] = "";
                }
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

    public void onGamemodeChange(View view) {

        //toggle modes
        if (mining) {
            //mining -> switch to flagging
            gameMode.setText(R.string.flag);
            mining = false;
            flagging = true;

        } else {
            //flagging -> switch to mining
            gameMode.setText(R.string.pick);
            mining = true;
            flagging = false;
        }
    }

    public void onClickTV(View view){

        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        tv.setText(bombPlacements[i][j]);

        //perform BFS if the square is empty (opens up other empty squares)
        if (tv.getText().equals("")) {

            //perform BFS (add to back, remove from front of LL)
            LinkedList<String> queue = new LinkedList<>();
            HashMap<String, Boolean> visited = new HashMap<>();

            //keys will be indices added together, turned into a string
            //i.e. i = 2, j = 3 -> str(23)

            String index = Integer.toString(i) + Integer.toString(j);
            queue.add(index);
            visited.put(index, true);


            while (!queue.isEmpty()) {

                //remove that vertex from the queue (from the front of LL)
                String currIndex = queue.pop();

                //handle the neighbors of currIndex
                ArrayList<String> neighbors = getNeighbors(currIndex);

                if (neighbors != null) {
                    for (int k = 0; k < neighbors.size(); k++) {
                        //if not already visited

                        //get the key from neighbors
                        if (!visited.containsKey(neighbors.get(k))) {

                            //first ensure that the cell is empty
                            char iIdx = neighbors.get(k).charAt(0);
                            char jIdx = neighbors.get(k).charAt(1);

                            int iIndex = Integer.valueOf(iIdx) - '0';
                            int jIndex = Integer.valueOf(jIdx) - '0';

                            //add it to the queue and mark it as visited
                            if (bombPlacements[iIndex][jIndex].equals("")) {
                                queue.add(neighbors.get(k));
                                visited.put(neighbors.get(k), true);
                            } else{
                                //if we encounter a number, we should add it but not visit it
                                if (!bombPlacements[iIndex][jIndex].equals("b")) {
                                    visited.put(neighbors.get(k), true);
                                }
                            }
                        }
                    }
                }

                //DEBUG HERE
                //WORK THROUGH SOME TEST CASES, MAKE SURE THE NEIGHBORS BEING RETURNED ARE
                //ACTUALLY CORRECT. ONCE THEY ARE, WE CAN MOVE ON TO TRYING TO COLOR THEM GREY
            }
            for (int x = 0; x < ROW_COUNT; x++) {
                for (int y = 0; y < COLUMN_COUNT; y++) {
                    //if i + j in keys
                    String strIdx = String.valueOf(x) + String.valueOf(y);
                    if (visited.containsKey(strIdx)) {
                        //set cell background color to grey
                        int cellIndex = (x * COLUMN_COUNT) + ((y % COLUMN_COUNT));
                        cells.get(cellIndex).setBackgroundColor(Color.LTGRAY);
                        cells.get(cellIndex).setTextColor(Color.GRAY);
                        cells.get(cellIndex).setText(bombPlacements[x][y]);
                    }
                }
            }
        } else {
            //on click will either be a bomb or a number or a bomb
            tv.setBackgroundColor(Color.LTGRAY);
            tv.setTextColor(Color.GRAY);
        }

        //Added functionality
        if (flagging) {
            tv.setText(R.string.flag);
        }

    }

    public ArrayList<String> getNeighbors(String cell) {
        //separate cell into i and j components and return all neighbors (if valid)

        ArrayList<String> neighbors = new ArrayList<>();

        char i = cell.charAt(0);
        char j = cell.charAt(1);

        int iIndex = Integer.valueOf(i) - '0';
        int jIndex = Integer.valueOf(j) - '0';

        //get neighbors

        //up (i,j-1)
        if (jIndex-1 >= 0) {
            String toAdd = String.valueOf(iIndex) + String.valueOf(jIndex-1);
            neighbors.add(toAdd);
        }
        //left (i-1,j)
        if (iIndex-1 >= 0) {
            String toAdd = String.valueOf(iIndex-1) + String.valueOf(jIndex);
            neighbors.add(toAdd);
        }
        //right (i+1,j)
        if (iIndex+1 < ROW_COUNT) {
            String toAdd = String.valueOf(iIndex+1) + String.valueOf(jIndex);
            neighbors.add(toAdd);
        }
        //down (i,j+1)
        if (jIndex+1 < COLUMN_COUNT) {
            String toAdd = String.valueOf(iIndex) + String.valueOf(jIndex+1);
            neighbors.add(toAdd);
        }

        //Diagonal cases as well
        //up left (i-1, j-1)
        if (iIndex-1 >= 0 && jIndex-1 >= 0) {
            String toAdd = String.valueOf(iIndex-1) + String.valueOf(jIndex-1);
            neighbors.add(toAdd);
        }
        //up right (i+1, j-1)
        if (iIndex+1 < ROW_COUNT && jIndex-1 >= 0) {
            String toAdd = String.valueOf(iIndex+1) + String.valueOf(jIndex-1);
            neighbors.add(toAdd);
        }
        //down left (i-1, j+1)
        if (iIndex-1 >= 0 && jIndex+1 < COLUMN_COUNT) {
            String toAdd = String.valueOf(iIndex-1) + String.valueOf(jIndex+1);
            neighbors.add(toAdd);
        }
        //down right (i+1, j+1)
        if (iIndex+1 < ROW_COUNT && jIndex+1 < COLUMN_COUNT) {
            String toAdd = String.valueOf(iIndex+1) + String.valueOf(jIndex+1);
            neighbors.add(toAdd);
        }

        if (neighbors.isEmpty()) {
            return null;
        } else return neighbors;
    }

}
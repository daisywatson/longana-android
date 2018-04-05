package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Player {

    public static String compPlaysTile (Round myRound, Tiles gameTiles, Layout theBoard)
    {
        String compStrategy = "";
        //If the user passed last turn, the computer
        //can play non-double tiles on either side
        String tilesToCheck = "computer";
        if (myRound.getUserPass())
        {
            tilesToCheck = "pass";
        }

        //Computer finds playable tiles:
        ArrayList<Integer> playableTileLeft = new ArrayList<Integer>();
        ArrayList<Integer> playableTileRight = new ArrayList<Integer>();
        for (int i = 0; i < gameTiles.compLeft.size(); i++)
        {
            int lhs = gameTiles.compLeft.get(i);
            int rhs = gameTiles.compRight.get(i);
            if (theBoard.playableTile(tilesToCheck, lhs, rhs))
            {
                playableTileLeft.add(lhs);
                playableTileRight.add(rhs);
            }
        }

        //Computer decides which tile to play
        //Computer prioritizes playing tiles with the highest pip number

        //First, it adds up the sum of the pips of each tile
        ArrayList<Integer> tileSums = new ArrayList<Integer>();
        for (int j = 0; j < playableTileLeft.size(); j++)
        {
            tileSums.add(playableTileLeft.get(j) + playableTileRight.get(j));
        }

        //Then it finds the tile with the largest sum
        //Find the location of the largest sum in the array list
        int largestVal = Collections.max(tileSums);
        int largestLoc = 0;
        for (int loc = 0; loc < tileSums.size(); loc++)
        {
            if (tileSums.get(loc) == largestVal)
            {
                largestLoc = loc;
                break;
            }
        }
        //Initialize the tile to play to the tile with the largest sum of its pips:
        int playedLHS = playableTileLeft.get(largestLoc);
        int playedRHS = playableTileRight.get(largestLoc);


        //Figure out which side to play the tile on
        String whichSide;
        //Figure out whether the tile needs to be rotated or not
        String rotate = "";
        if (theBoard.playableSide("r", playedLHS, playedRHS))
        {
            whichSide = "right";
        }
        else if (theBoard.playableSide("r", playedRHS, playedLHS))
        {
            whichSide = "right";
            rotate = "transposed";
        }
        else if (theBoard.playableSide("l", playedLHS, playedRHS))
        {
            whichSide = "left";
        }
        else {
            whichSide = "left";
            rotate = "transposed";
        }

        //Convert the playSide to a format usable by the function to play the tile
        String playSide = "r";
        if (whichSide == "left") playSide = "l";
        //Play the tile:
        gameTiles.playCompTile(playSide, playedLHS, playedRHS, theBoard);

        //This is the computer's strategy
        String strategy = "It has the highest number of total pips among playable tiles.";
        if (playableTileLeft.size() < 2)
        {
            strategy = "It is the only playable tile.";
        }

        //Output what tile the computer played
        compStrategy += "The computer played " + Integer.toString(playedLHS) + "-" +
                Integer.toString(playedRHS) + " " + rotate + " on the " +  whichSide +
                " side of the engine.\n";
        compStrategy += strategy + "\n";


        return compStrategy;

    }

    public static String help(String tilesToCheck, Tiles gameTiles, Layout theBoard)
    {
        String hint = "";

        //Computer finds playable tiles fo the user:
        ArrayList<Integer> playableTileLeft = new ArrayList<Integer>();
        ArrayList<Integer> playableTileRight = new ArrayList<Integer>();
        for (int i = 0; i < gameTiles.humanLeft.size(); i++)
        {
            int lhs = gameTiles.humanLeft.get(i);
            int rhs = gameTiles.humanRight.get(i);
            if (theBoard.playableTile(tilesToCheck, lhs, rhs))
            {
                playableTileLeft.add(lhs);
                playableTileRight.add(rhs);
            }
        }

        //First, it adds up the sum of the pips of each tile
        ArrayList<Integer> tileSums = new ArrayList<Integer>();
        for (int j = 0; j < playableTileLeft.size(); j++)
        {
            tileSums.add(playableTileLeft.get(j) + playableTileRight.get(j));
        }

        //Then it finds the tile with the largest sum
        //Find the location of the largest sum in the array list
        int largestVal = Collections.max(tileSums);
        int largestLoc = 0;
        for (int loc = 0; loc < tileSums.size(); loc++)
        {
            if (tileSums.get(loc) == largestVal)
            {
                largestLoc = loc;
                break;
            }
        }

        //Initialize the tile to play to the tile with the largest sum of its pips:
        int playedLHS = playableTileLeft.get(largestLoc);
        int playedRHS = playableTileRight.get(largestLoc);

        //Figure out which side to play the tile on
        String whichSide;
        //Figure out whether the tile needs to be rotated or not
        String rotate = "";
        if (theBoard.playableSide("l", playedLHS, playedRHS))
        {
            whichSide = "left";
        }
        else if (theBoard.playableSide("l", playedRHS, playedLHS))
        {
            whichSide = "left";
            rotate = "transposed";
        }
        else if (theBoard.playableSide("r", playedLHS, playedRHS))
        {
            whichSide = "right";
        }
        else {
            whichSide = "right";
            rotate = "transposed";
        }

        //This is the computer's suggested strategy
        String strategy = "It has the highest number of total pips among playable tiles.";
        if (playableTileLeft.size() < 2)
        {
            strategy = "It is the only playable tile.";
        }

        //Output what tile the computer recommends
        hint += "The computer recommends playing " + playedLHS + "-" + playedRHS;
        hint += " " + rotate + " on the " + whichSide + " side of the engine.\n";
        hint += strategy;

        return hint;
    }

}

package Model;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Tiles {
    protected static ArrayList<Integer> compLeft = new ArrayList<Integer>();
    protected static ArrayList<Integer> compRight = new ArrayList<Integer>();

    protected static ArrayList<Integer> humanLeft = new ArrayList<Integer>();
    protected static ArrayList<Integer> humanRight = new ArrayList<Integer>();

    protected static ArrayList<Integer> stockLeft = new ArrayList<Integer>();
    protected static ArrayList<Integer> stockRight = new ArrayList<Integer>();

    //Whether or not it's the computer's turn at the beginning of a game
    //If it is, then that means the user played first
    protected static boolean compTurn = false;
    private final Context context;

    //If a loaded file was successfully opened
    //protected boolean openSuccess;

    public Tiles(Context context) {
        //openSuccess = false;
        //compTurn = false;
        this.context = context;
    }

    //Reset all values in class for a completely new tournament
    public static void reset()
    {
        compLeft.clear();
        compRight.clear();
        humanLeft.clear();
        humanRight.clear();
        stockLeft.clear();
        stockRight.clear();
        compTurn = false;
    }

    //Shuffle tiles and draw 8 for each hand
    public void newGame(Layout theBoard)
    {
        //If there was previous round, reset the tiles:
        compLeft.clear();
        compRight.clear();
        humanLeft.clear();
        humanRight.clear();
        stockLeft.clear();
        stockRight.clear();
        theBoard.playedLeft.clear();
        theBoard.playedRight.clear();

        //Create the left/right hand side pips of the initialized tiles
        ArrayList<Integer> tileLeft = new ArrayList<Integer>();
        //initialize left side of the tiles
        int count = 7;
        for (int i = 0; i <= 6; i++)
        {
            for (int j = 0; j < count; j++)
            {
                tileLeft.add(i);
            }
            count--;
        }

        ArrayList<Integer> tileRight = new ArrayList<Integer>();
        //initialize right side of tiles
        int kBegin = 0;
        while (kBegin <= 6)
        {
            for (int k = kBegin; k <= 6; k++)
            {
                tileRight.add(k);
            }
            kBegin++;
        }

        //Shuffle tiles randomly, both in the same way
        long seed = System.nanoTime();
        Collections.shuffle(tileLeft, new Random(seed));
        Collections.shuffle(tileRight, new Random(seed));

        //Put 8 tiles into the computer hand
        for (int c = 0; c < 8; c++)
        {
            compLeft.add(tileLeft.get(c));
            compRight.add(tileRight.get(c));
        }

        //Put 8 other tiles into human's hand
        for (int h = 8; h < 16; h++)
        {
            humanLeft.add(tileLeft.get(h));
            humanRight.add(tileRight.get(h));
        }

        //Put the remaining tiles in the stock
        for (int s = 16; s < 28; s++)
        {
            stockLeft.add(tileLeft.get(s));
            stockRight.add(tileRight.get(s));
        }

    }

    //Checks to see if a string is a number or not
    public static boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public void loadGame(Round myRound, Tournament myTourney, Layout theBoard, Uri uri)
            throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        //Keep track of the number of lines:
        int numLines = 1;
        boolean prevPlayerPass = false;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            String [] tokens = line.split(" ");
            if (line.contains("Tournament"))
            {
                for (int i = 0; i < tokens.length; i++)
                {
                    if (isNumber(tokens[i]))
                    {
                        myTourney.setTourneyScore(Integer.parseInt(tokens[i]));
                    }
                }
            }
            else if (line.contains("Round"))
            {
                for (int i = 0; i < tokens.length; i++)
                {
                    if (isNumber(tokens[i]))
                    {
                       myRound.setRounds(Integer.parseInt(tokens[i]));
                        myRound.setEngineNum(myRound.determineEngineNum());
                    }
                }
            }
            else if (line.contains("Previous"))
            {
                //If previous player passed
                if (line.contains("Yes"))
                {
                    prevPlayerPass = true;
                }
            }
            else if (line.contains("Next"))
            {
                //Who is the next player
                if (line.contains("Computer"))
                {
                    compTurn = true;
                }
            }
            else if (numLines == 5)
            {
                //Computer's hand
                for (int i = 0; i < tokens.length; i++)
                {
                    if (tokens[i].contains("-"))
                    {
                        String [] tile = tokens[i].split("-");
                        compLeft.add(Integer.parseInt(tile[0]));
                        compRight.add(Integer.parseInt(tile[1]));
                    }
                }
            }
            else if (numLines == 6)
            {
                //Computer's score
                for (int i = 0; i < tokens.length; i++)
                {
                    if (isNumber(tokens[i]))
                    {
                        myTourney.setCompScore(Integer.parseInt(tokens[i]));
                    }
                }
            }
            else if (numLines == 9)
            {
                //Human's hand
                for (int i = 0; i < tokens.length; i++)
                {
                    if (tokens[i].contains("-"))
                    {
                        String [] tile = tokens[i].split("-");
                        humanLeft.add(Integer.parseInt(tile[0]));
                        humanRight.add(Integer.parseInt(tile[1]));
                    }
                }
            }
            else if (numLines == 10)
            {
                //Human score
                for (int i = 0; i < tokens.length; i++)
                {
                    if (isNumber(tokens[i]))
                    {
                        myTourney.setHumanScore(Integer.parseInt(tokens[i]));
                    }
                }
            }
            else if (numLines == 13)
            {
                for (int i = 0; i < tokens.length; i++)
                {
                    if (tokens[i].contains("-"))
                    {
                        String [] tile = tokens[i].split("-");
                        theBoard.playedLeft.add(Integer.parseInt(tile[0]));
                        theBoard.playedRight.add(Integer.parseInt(tile[1]));
                    }
                }
            }
            else if (numLines == 16)
            {
                for (int i = 0; i < tokens.length; i++)
                {
                    if (tokens[i].contains("-"))
                    {
                        String [] tile = tokens[i].split("-");
                        stockLeft.add(Integer.parseInt(tile[0]));
                        stockRight.add(Integer.parseInt(tile[1]));
                    }
                }
            }

            numLines += 1;
        }
        inputStream.close();

        //Set whether the previous player passed or not AFTER finding out whose turn it is next
        if (compTurn == true && prevPlayerPass == true)
        {
            //The user previously passed
            myRound.setUserPass();
        }
        else if (prevPlayerPass == true)
        {
            //The computer previously passed
            myRound.setCompPass();
        }

    }

    public void saveGame(Round myRound, Tournament myTourney, String userFileName,
                            String nextPlayer, Layout theBoard)
    {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, userFileName + ".txt");

            //Make a string that holds whether the previous player passed or not:
            String prevPlayerPass = "No";
            if (nextPlayer == "Computer" && myRound.getCompPass())
            {
                prevPlayerPass = "Yes";
            }
            else if (nextPlayer == "Human" && myRound.getUserPass())
            {
                prevPlayerPass = "Yes";
            }

            //Make a string that holds computer's hand tiles:
            String compTiles = "";
            for (int i = 0; i < compLeft.size(); i++)
            {
                compTiles += Integer.toString(compLeft.get(i));
                compTiles += "-";
                compTiles += Integer.toString(compRight.get(i));
                compTiles += " ";
            }

            //Make string that holds human hand's tiles
            String userTiles = "";
            for (int j = 0; j < humanLeft.size(); j++)
            {
                userTiles += Integer.toString(humanLeft.get(j));
                userTiles += "-";
                userTiles += Integer.toString(humanRight.get(j));
                userTiles += " ";
            }

            //Make a string that holds the layout's tiles
            String layoutTiles = "";
            for (int k = 0; k < theBoard.playedLeft.size(); k++)
            {
                layoutTiles += Integer.toString(theBoard.playedLeft.get(k));
                layoutTiles += "-";
                layoutTiles += Integer.toString(theBoard.playedRight.get(k));
                layoutTiles += " ";
            }

            //Make a string to hold the stock's tiles
            String stockTiles = "";
            for (int m = 0; m < stockLeft.size(); m++)
            {
                stockTiles += Integer.toString(stockLeft.get(m));
                stockTiles += "-";
                stockTiles += Integer.toString(stockRight.get(m));
                stockTiles += " ";
            }

            //This string holds everything that will written to the file
            String inputText = "";
            inputText += "Tournament Score: ";
            inputText += Integer.toString(myTourney.getTourneyScore());
            inputText += "\n";
            inputText += "Round No.: ";
            inputText += Integer.toString(myRound.getRounds());
            inputText += "\n";
            inputText += "\n";
            inputText += "Computer:\n";
            inputText += "   Hand: ";
            inputText += compTiles;
            inputText += "\n";
            inputText += "   Score: ";
            inputText += Integer.toString(myTourney.getCompScore());
            inputText += "\n";
            inputText += "\n";
            inputText += "Human:\n";
            inputText += "   Hand: ";
            inputText += userTiles;
            inputText += "\n";
            inputText += "   Score: ";
            inputText += Integer.toString(myTourney.getHumanScore());
            inputText += "\n";
            inputText += "\n";
            inputText += "Layout:\n";
            inputText += "  L ";
            inputText += layoutTiles;
            inputText += "R";
            inputText += "\n";
            inputText += "\n";
            inputText += "Boneyard:\n";
            inputText += stockTiles;
            inputText += "\n";
            inputText += "\n";
            inputText += "Previous Player Passed: ";
            inputText += prevPlayerPass;
            inputText += "\n";
            inputText += "\n";
            inputText += "Next Player: ";
            inputText += nextPlayer;
            inputText +="\n";

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(inputText.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }



    public void playEngine(Round myRound, Tournament myTourney, Layout theBoard)
    {
        //Get the engine number:
        int engineNum = myRound.getEngineNum();
        compTurn = false;
        //Location of engine in hand:
        int engineLocation = 0;
        //Draw from the stock if neither player has the engine
        boolean drawAgain = true;

        //Figure out which player has the engine
        for (int e = 0; e < 8; e++)
        {
            //If the human player has the engine
            if (humanLeft.get(e) == engineNum && humanRight.get(e) == engineNum)
            {
                drawAgain = false;
                engineLocation = e;
                break;
            }
            //if the computer has the engine
            else if (compLeft.get(e) == engineNum && compRight.get(e) == engineNum)
            {
                drawAgain = false;
                compTurn = true;
                engineLocation = e;
                break;
            }
        }

        //Number of tiles in hand:
        int handNum = 9;
        //If neither player has the engine, keep drawing from the stock until one of them does
        while (drawAgain == true)
        {
            //Add a value from the stock to each player's hand
            //stockVal represents the position of the first tile in the stock
            int stockVal = 0;
            //Add to human hand and erase from stock
            humanLeft.add(stockLeft.get(stockVal));
            stockLeft.remove(stockVal);
            humanRight.add(stockRight.get(stockVal));
            stockRight.remove(stockVal);
            //Add to computer hand and erase from stock
            compLeft.add(stockLeft.get(stockVal));
            stockLeft.remove(stockVal);
            compRight.add(stockRight.get(stockVal));
            stockRight.remove(stockVal);

            //Figure out which player has the engine:
            for (int e = 0; e < handNum; e++)
            {
                //If the human player has the engine
                if (humanLeft.get(e) == engineNum && humanRight.get(e) == engineNum)
                {
                    drawAgain = false;
                    engineLocation = e;
                    break;
                }
                //if the computer has the engine
                else if (compLeft.get(e) == engineNum && compRight.get(e) == engineNum)
                {
                    drawAgain = false;
                    compTurn = true;
                    engineLocation = e;
                    break;
                }
            }
            handNum++;
        }

        //Add the engine to the layout:
        theBoard.playedLeft.add(engineNum);
        theBoard.playedRight.add(engineNum);
        //Erase the engine tile from the player's hand:
        if (compTurn == true)
        {
            //Remove the tile from the computer's hand:
            compLeft.remove(engineLocation);
            compRight.remove(engineLocation);

            //The computer has played, so the next player is the user:
            compTurn = false;
        }
        else
        {
            //Force the player to play the engine
            //Erase the tile from the user's hand:
            humanLeft.remove(engineLocation);
            humanRight.remove(engineLocation);

            //The user has played, so the next player is the computer:
            compTurn = true;
        }

    }

    public static boolean isCompTurn()
    {
        return compTurn;
    }

    public static void setCompTurn()
    {
        compTurn = true;
    }

    public static void setNotCompTurn() {compTurn = false;}

    public static String getHumanLeftTile(int index)
    {
        String tileValue = "";

        tileValue = Integer.toString(humanLeft.get(index));

        return tileValue;
    }

    public static int getHumanLeftTileNum(int index)
    {
        int tileValue = 0;

        tileValue = humanLeft.get(index);

        return tileValue;
    }

    public static String getHumanRightTile(int index)
    {
        String tileValue = "";

        tileValue = Integer.toString(humanRight.get(index));

        return tileValue;
    }

    public static int getHumanRightTileNum(int index)
    {
        int tileValue = 0;

        tileValue = humanRight.get(index);

        return tileValue;
    }

    public static int humanHandSize()
    {
        return humanLeft.size();
    }

    public static String printPlayerHand(String whichPlayer)
    {
        String hand = "";
        if (whichPlayer == "human")
        {
            for (int i = 0; i < humanLeft.size(); i++)
            {
                hand += humanLeft.get(i);
                hand += "-";
                hand += humanRight.get(i);
                hand += " ";
            }
        }
        else {
            for (int j = 0; j < compLeft.size(); j++)
            {
                hand += compLeft.get(j);
                hand += "-";
                hand += compRight.get(j);
                hand += " ";
            }
        }

        return hand;
    }


    public static String printStock()
    {
        String stock = "";
        for (int i = 0; i < stockLeft.size(); i++)
        {
            stock += stockLeft.get(i);
            stock += "-";
            stock += stockRight.get(i);
            stock += " ";
        }
        stock += "\n";

        return stock;
    }


    public static boolean anyPlayableTile(String whichPlayer, String pass, Layout theBoard)
    {
        boolean playableTileExists = false;
        if (whichPlayer == "human")
        {
            for (int i = 0; i < humanLeft.size(); i++)
            {
                int lhs = humanLeft.get(i);
                int rhs = humanRight.get(i);
                if (pass == "pass")
                {
                    if (theBoard.playableTile(pass, lhs, rhs) == true)
                    {
                        playableTileExists = true;
                        break;
                    }
                }
                else {
                    if (theBoard.playableTile(whichPlayer, lhs, rhs) == true)
                    {
                        playableTileExists = true;
                        break;
                    }
                }
            }
        }
        else {
            for (int j = 0; j < compLeft.size(); j++)
            {
                int lhs = compLeft.get(j);
                int rhs = compRight.get(j);
                if (pass == "pass")
                {
                    if (theBoard.playableTile(pass, lhs, rhs) == true)
                    {
                        playableTileExists = true;
                        break;
                    }
                }
                else {
                    if (theBoard.playableTile(whichPlayer, lhs, rhs) == true)
                    {
                        playableTileExists = true;
                        break;
                    }
                }
            }
        }
        return playableTileExists;
    }



    public void playUserTile(String side, int lhs, int rhs, Layout theBoard)
    {
        int tileLoc = 0;
        //Find location of the tile in the vectors
        for (int i = 0; i < humanLeft.size(); i++)
        {
            if (humanLeft.get(i) == lhs && humanRight.get(i) == rhs)
            {
                tileLoc = i;
            }
        }
        if (side == "r")
        {
            //insert onto the right side of the layout
            if (!theBoard.rotateTile("r", lhs, rhs))
            {
                theBoard.playedLeft.add(lhs);
                theBoard.playedRight.add(rhs);
            }
            else {
                //Swap the pips on the lhs/rhs if
                //the domino needs to be rotated
                theBoard.playedLeft.add(rhs);
                theBoard.playedRight.add(lhs);
            }
        }
        else {
            //The left side of the layout is the human side
            //Insert the tile into the beginning of playedTiles
            //aka the left hand side of the layout
            if (!theBoard.rotateTile("l", lhs, rhs))
            {
                theBoard.playedLeft.add(0, lhs);
                theBoard.playedRight.add(0, rhs);
            }
            else {
                //Swap the pips on the lhs/rhs if
                //the domino needs to be rotated
                theBoard.playedLeft.add(0, rhs);
                theBoard.playedRight.add(0, lhs);
            }
        }
        //delete from the hand
        humanLeft.remove(tileLoc);
        humanRight.remove(tileLoc);
    }

    public void playCompTile(String side, int lhs, int rhs, Layout theBoard)
    {
        int tileLoc = 0;
        //Find location of the tile in the vectors
        for (int i = 0; i < compLeft.size(); i++)
        {
            if (compLeft.get(i) == lhs && compRight.get(i) == rhs)
            {
                tileLoc = i;
            }
        }

        if (side == "r")
        {
            //Insert onto the end of the layout
            if (!theBoard.rotateTile("r", lhs, rhs))
            {
                theBoard.playedLeft.add(lhs);
                theBoard.playedRight.add(rhs);
            }
            else {
                //Swap the pips on the lhs/rhs if
                //the domino needs to be rotated
                theBoard.playedLeft.add(rhs);
                theBoard.playedRight.add(lhs);
            }
        }
        else {
            //Insert into the beginning of the layout
            if (!theBoard.rotateTile("l", lhs, rhs))
            {
                theBoard.playedLeft.add(0, lhs);
                theBoard.playedRight.add(0, rhs);
            }
            else {
                //Swap the pips on the lhs/rhs if
                //the domino needs to be rotated
                theBoard.playedLeft.add(0, rhs);
                theBoard.playedRight.add(0, lhs);
            }
        }
        //delete from the hand
        compLeft.remove(tileLoc);
        compRight.remove(tileLoc);
    }


    public static void drawFromStock(String whichPlayer)
    {
        //stockVal represents the position of the first tile in the stock
        int stockVal = 0;
        if (whichPlayer == "human")
        {
            //Add to human hand and erase from stock
            humanLeft.add(stockLeft.get(stockVal));
            stockLeft.remove(0);
            humanRight.add(stockRight.get(stockVal));
            stockRight.remove(0);
        }
        else {
            //Add to computer hand and erase from stock
            compLeft.add(stockLeft.get(stockVal));
            stockLeft.remove(0);
            compRight.add(stockRight.get(stockVal));
            stockRight.remove(0);
        }
    }

    public static boolean isStockEmpty()
    {
        if (stockLeft.isEmpty())
        {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isHandEmpty(String whichPlayer)
    {
        if (whichPlayer == "human")
        {
            if (humanLeft.isEmpty())
            {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if (compLeft.isEmpty())
            {
                return true;
            }
            else {
                return false;
            }

        }

    }

    public int handScore(String whichPlayer)
    {
        int playerScore = 0;

        if (whichPlayer == "human")
        {
            for (int i = 0; i < humanLeft.size(); i++)
            {
                playerScore += humanLeft.get(i);
                playerScore += humanRight.get(i);
            }
        }
        else {
            for (int i = 0; i < compLeft.size(); i++)
            {
                playerScore += compLeft.get(i);
                playerScore += compRight.get(i);
            }
        }

        return playerScore;
    }

    //Who won a round
    public String whoWon(Tournament myTourney)
    {
        String winner = "This round has ended!\n";

        //1. If one of the players' hand is empty, they automatically win
        //They get the score of total pips in the opposite player's hand
        if (isHandEmpty("human"))
        {
            winner += "The human player has won!\n";
            winner +="The human's score is: ";
            winner += Integer.toString(handScore("computer"));
            winner += "\n";
            myTourney.addHumanScore(handScore("computer"));
        }
        else if (isHandEmpty("computer"))
        {
            winner += "The computer has won!\n";
            winner += "The computer's score is: ";
            winner += Integer.toString(handScore("human"));
            winner += "\n";
            myTourney.addCompScore(handScore("human"));
        }
        else {
            //2. Otherwise calculate the score
            //Player with smaller number of pips in hand wins
            if (handScore("human") < handScore("computer"))
            {
                winner += "The human player has won!\n";
                winner +="The human's score is: ";
                winner += Integer.toString(handScore("computer"));
                winner += "\n";
                myTourney.addHumanScore(handScore("computer"));
            }
            else if (handScore("human") > handScore("computer"))
            {
                winner += "The computer has won!\n";
                winner += "The computer's score is: ";
                winner += Integer.toString(handScore("human"));
                winner += "\n";
                myTourney.addCompScore(handScore("human"));
            }
            else {
                winner += "It's a tie!\n";
            }
        }

        winner += "The tournament score so far is: \n";
        winner += "Computer: ";
        winner += Integer.toString(myTourney.getCompScore());
        winner += "\nHuman: ";
        winner +=  Integer.toString(myTourney.getHumanScore());

        return winner;
    }

}

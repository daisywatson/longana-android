package com.example.longana;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Layout;
import Model.Player;
import Model.Round;
import Model.Tiles;
import Model.Tournament;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class PlayGameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int[] BUTTON_IDS = {
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button10,
            R.id.button11,
            R.id.button12,
            R.id.button13,
            R.id.button14,
            R.id.button15,
            R.id.button16,
            R.id.button17,
            R.id.button18,
            R.id.button19,
            R.id.button20,
    };

    Tiles myTiles = new Tiles(this);
    Round myRound = new Round();
    Layout theBoard = new Layout();
    Tournament myTourney = new Tournament();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        // Get the Intent that started this activity and extract the string
        Intent intent = this.getIntent();

        //Play the engine if it hasn't been played yet
        if (!theBoard.enginePlayed(myRound))
        {
            playEngine();
        }
        else {
            startGame();
        }

    }

    //Beginning of each turn, set the view
    public void startGame()
    {
        //Make sure save game buttons are invisible
        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setVisibility(View.INVISIBLE);
        Button noButton = (Button)findViewById(R.id.noButton);
        noButton.setVisibility(View.INVISIBLE);

        TextView whoTurn = (TextView) findViewById(R.id.whoseTurn);
        //Set whose turn it is
        if (myTiles.isCompTurn())
        {
            whoTurn.setText("It's the computer's turn. Round: " + Integer.toString(
                    myRound.getRounds()));
        }
        else {
            whoTurn.setText("It's your turn. Round: " + Integer.toString(
                    myRound.getRounds()));
        }
        //Print the stock
        TextView stockView = (TextView) findViewById(R.id.stockView);
        stockView.setText("Stock: " + myTiles.printStock());
        //Print the layout
        TextView layoutView = (TextView) findViewById(R.id.layoutView);
        layoutView.setText(theBoard.printLayout());
        //Display the hand
        if (myTiles.isCompTurn())
        {
            computerPlays();
        }
        else {
            Button hintButton = (Button)findViewById(R.id.hintButton);
            hintButton.setVisibility(View.VISIBLE);
            humanPlays();
        }

    }

    public void humanPlays ()
    {
        String messageToUser = "";
        TextView userMessage = (TextView) findViewById(R.id.userMessage);

        //If computer has already passed and user has no playable tiles:
        if (myRound.getCompPass() == true && myRound.getUserPass() == true &&
                myTiles.isStockEmpty())
        {
            myRound.setRoundOver();
            endRound("The computer has passed and the user has no playable tiles.\n");
        }

        //If the computer's or user's hand is empty, end the round:
        if (myTiles.isHandEmpty("computer") || myTiles.isHandEmpty("human"))
        {
            Round.setRoundOver();
            if (Tiles.isHandEmpty("computer")) {
                endRound("The computer's hand is empty.\n");
            }
            else {
                endRound("The user's hand is empty.\n");
            }
        }

        //If the computer passed last turn,
        //non-doubles can be played
        String pass = "";
        if (myRound.getCompPass())
        {
            messageToUser += "The computer passed last turn. You can play non-doubles " +
                    "on either side of the engine this turn.\n";
            pass = "pass";
        }

        //If the user passed last turn but they can play a tiles this turn, let them play the tile
        if (myTiles.anyPlayableTile("human", pass, theBoard))
        {
            myRound.resetUserPass();
        }

        //Check if the player can play any of the tiles in their hand
        if (myTiles.anyPlayableTile("human", pass, theBoard) == false)
        {
            //Check if the stock is empty
            if (myTiles.isStockEmpty())
            {
                messageToUser += "Your hand: " + myTiles.printPlayerHand("human") + "\n";
                messageToUser += "There are no playable tiles in your hand.\n";
                messageToUser += "You pass this turn.\n";
                userMessage.setText(messageToUser);
                myRound.setUserPass();

                Button hintButton = (Button) findViewById(R.id.hintButton);
                hintButton.setVisibility(View.INVISIBLE);

                //Get ok from the user to proceed
                Button okButton = (Button) findViewById(R.id.okButton);
                okButton.setVisibility(View.VISIBLE);
            }
            else {
                //Draw one tile from the boneyard
                messageToUser += "You draw a tile from the stock because you have " +
                        "no playable tiles in your hand.\n";
                myTiles.drawFromStock("human");

                //If the user passed last turn but they can play a tile
                //this turn after drawing a tile, let them play the tile
                myRound.resetUserPass();

                //Check again if there are any playable tiles in the
                //user's hand after drawing one tile from the stock
                if (myTiles.anyPlayableTile("human", pass, theBoard) == false)
                {
                    messageToUser += "Your hand: " + myTiles.printPlayerHand("human") + "\n";
                    messageToUser += "There are no playable tiles in your hand.\n";
                    messageToUser += "You pass this turn.\n";
                    userMessage.setText(messageToUser);
                    myRound.setUserPass();

                    Button hintButton = (Button) findViewById(R.id.hintButton);
                    hintButton.setVisibility(View.INVISIBLE);

                    //Get ok from the user to proceed
                    Button okButton = (Button) findViewById(R.id.okButton);
                    okButton.setVisibility(View.VISIBLE);
                }
            }
        }

        if (!myRound.getUserPass()) {
            messageToUser += "Select a tile to play on the layout.";
            userMessage.setText(messageToUser);

            //Make an ArrayList of the tile buttons:
            ArrayList<Button> buttons = new ArrayList<Button>();
            for (int id : BUTTON_IDS) {
                Button button = (Button) findViewById(id);
                buttons.add(button);
            }
            //Display the tiles in the user's hand:
            for (int i = 0; i < Tiles.humanHandSize(); i++) {
                String buttonName = "";
                buttonName += Tiles.getHumanLeftTile(i);
                buttonName += "-";
                buttonName += Tiles.getHumanRightTile(i);
                buttons.get(i).setText(buttonName);
                buttons.get(i).setVisibility(View.VISIBLE);
                buttons.get(i).setOnClickListener(this);
            }
        }
    }

    public void computerPlays ()
    {
        //If the computer's or user's hand is empty, end the round:
        if (myTiles.isHandEmpty("computer") || myTiles.isHandEmpty("human"))
        {
            Round.setRoundOver();
            if (myTiles.isHandEmpty("computer")) {
                endRound("The computer's hand is empty.\n");
            }
            else {
                endRound("The user's hand is empty.\n");
            }
        }

        String messageToUser = "";
        //Print the computer's hand:
        messageToUser += "The computer's hand:\n";
        messageToUser += myTiles.printPlayerHand("computer");
        messageToUser += "\n";
        String pass = "";
        if (myRound.getUserPass())
        {
            messageToUser += "The user passed last turn. The computer can " +
                    "play non-doubles on either side of the engine this turn.\n";
            pass = "pass";
        }

        //If the computer passed last turn but has a playable tile this turn, play it
        if (myTiles.anyPlayableTile("computer", pass, theBoard))
        {
            myRound.resetCompPass();
        }

        //Computer checks if there are any playable tiles
        if (myTiles.anyPlayableTile("computer", pass, theBoard) == false) {
            //Check if the stock is empty
            if (myTiles.isStockEmpty())
            {
                //If there are no playable tiles, the computer passes
                messageToUser += "There are no playable tiles in the computer's hand.\n";
                messageToUser +=  "The computer passes this turn.\n";
                myRound.setCompPass();
            }
            else {
                //Draw one tile from the boneyard
                messageToUser += "The computer has no playable tiles in hand so " +
                        "the computer draws one tile from the stock.\n";
                myTiles.drawFromStock("computer");

                //If the computer passed last turn but now has a playable tile after drawing,
                //play it
                myRound.resetCompPass();

                //Check again if there are any playable tiles in the
                //computer's hand after drawing one tile from the stock
                if (myTiles.anyPlayableTile("computer", pass, theBoard) == false)
                {
                    messageToUser += "There are still no playable tiles in the computer's hand.\n";
                    messageToUser += "The computer passes this turn.\n";
                    myRound.setCompPass();
                }
            }
        }

        //If there are playable tiles, the computer will play a tile:
        if (!myRound.getCompPass()) {
            Player myPlayer = new Player();
            messageToUser += myPlayer.compPlaysTile(myRound, myTiles, theBoard);
        }

        //If the user has already passed on the previous turn
        //and the computer has no playable tiles, the round is over
        if (myRound.getCompPass() == true && myRound.getUserPass() == true &&
                myTiles.isStockEmpty())
        {
            myRound.setRoundOver();
            endRound("The user has already passed and the computer has no playable tiles.\n");
        }

        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);

        //If the computer's hand is empty, end the round:
        if (myTiles.isHandEmpty("computer"))
        {
            myRound.setRoundOver();
            endRound("The computer's hand is empty.\n");
        }

        //Get ok from the user to proceed
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setVisibility(View.VISIBLE);
    }

    //After getting ok from the user
    public void okSave ()
    {
        Button okButton = (Button)findViewById(R.id.okButton);
        okButton.setVisibility(View.INVISIBLE);

        saveGame();
    }

    public void saveGame()
    {
        String messageToUser = "Would you like to save the game (quit)?";
        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);

        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setVisibility(View.VISIBLE);
        Button noButton = (Button)findViewById(R.id.noButton);
        noButton.setVisibility(View.VISIBLE);
    }

    //After a round ends
    public void endRound (String whyRoundOver)
    {
        myRound.setRoundOver();

        //Print out why the round is over
        String messageToUser = "";
        messageToUser += whyRoundOver;

        //Print out who won the round
        messageToUser += myTiles.whoWon(myTourney);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PlayGameActivity.this).setCancelable(false);
        alertDialogBuilder.setMessage(messageToUser);

        //final boolean nextRound = false;
        //Get ok from user to proceed to next round:
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                //@Override
                public void onClick(DialogInterface dialog, int id) {
                    //Check if the tournament is over
                    if (myTourney.isTourneyOver()) {
                        //Print out who won the tournament
                        String userMessage = "";
                        userMessage += "The tournament is over!\n";
                        userMessage += myTourney.whoWon();
                        final AlertDialog.Builder tourneyBuilder = new AlertDialog.Builder(
                                PlayGameActivity.this).setCancelable(false);
                        tourneyBuilder.setMessage(userMessage);

                        //Get ok from user to end the game and go back to main activity
                        tourneyBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            //@Override
                            public void onClick(DialogInterface dialog, int id) {
                                myTiles.reset();
                                myTourney.reset();
                                myRound.reset();
                                theBoard.reset();

                                Intent goBack = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(goBack);

                            }
                        });
                        tourneyBuilder.show();
                    } else {
                        //Set up for next round
                        myRound.endRound();
                        Button hintButton = (Button) findViewById(R.id.hintButton);
                        hintButton.setVisibility(View.INVISIBLE);

                        newRound();
                    }
                }
            });

        alertDialogBuilder.show();
    }

    public void newRound()
    {
        myRound.resetRoundOver();

        myTiles.newGame(theBoard);
        String messageToUser = "A new round has started. Each player has drawn 8 tiles.\n";
        messageToUser += "Would you like to save the game (quit) ?\n";
        final AlertDialog.Builder roundBuilder =
                new AlertDialog.Builder(PlayGameActivity.this).setCancelable(false);;
        roundBuilder.setMessage(messageToUser);
        //Get response from user:
        roundBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int id) {
                saveGameDialog();
            }
        });
        roundBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int id) {
               playEngine();

            }
        });
        roundBuilder.show();
    }

    public void playEngine ()
    {
        myTiles.playEngine(myRound, myTourney, theBoard);

        TextView whoTurn = (TextView) findViewById(R.id.whoseTurn);
        //Set whose turn it is
        if (myTiles.isCompTurn())
        {
            //isCompTurn determines who has the next turn
            //This text displays who's playing the engine this turn
            whoTurn.setText("It's your turn. Round: " + Integer.toString(
            myRound.getRounds()));
        }
        else {
            whoTurn.setText("It's the computer's turn. Round: " + Integer.toString(
            myRound.getRounds()));
        }

        //Print the stock
        TextView stockView = (TextView) findViewById(R.id.stockView);
        stockView.setText("Stock: " + myTiles.printStock());
        //Print the layout
        TextView layoutView = (TextView) findViewById(R.id.layoutView);
        layoutView.setText("L R");

        String whoHasEngine = "";
        //Display message to user about engine being played
        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        if (myTiles.isCompTurn())
        {
            //Show the user their hand if they played the engine
            whoHasEngine += "You have the engine!\n";
            whoHasEngine += "You must play the engine, press OK to continue.\n";
            whoHasEngine += "This is your hand: ";
            //The engine has already been played when asking the user this question
            //Show the engine in the user's hand:
            String engine = " " + Integer.toString(myRound.getEngineNum());
            engine += "-" + Integer.toString(myRound.getEngineNum());
            whoHasEngine += myTiles.printPlayerHand("human");
            whoHasEngine += engine;

            //noClick reverses isCompTurn boolean for switching back and forth between user/computer
            //Switch compTurn so that the computer plays next
            myTiles.setNotCompTurn();
        }
        else {
            whoHasEngine += "The computer's hand: " + myTiles.printPlayerHand("computer");
            whoHasEngine += " " + Integer.toString(myRound.getEngineNum());
            whoHasEngine += "-" + Integer.toString(myRound.getEngineNum());
            whoHasEngine +=  "\n";
            whoHasEngine += "The computer has the engine. Computer plays the engine.\n";
            whoHasEngine += "Press OK to continue.";

            //noClick reverses isCompTurn boolean for switching back and forth between user/computer
            //Switch compTurn so that the user plays next
            myTiles.setCompTurn();
        }
        userMessage.setText(whoHasEngine);

        //Ask them to continue
        Button okButton = (Button)findViewById(R.id.okButton);
        okButton.setVisibility(View.VISIBLE);
    }

    //When a tile is selected, attempt to play it
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                playTile (0, myTiles.getHumanLeftTileNum(0), myTiles.getHumanRightTileNum(0));
                break;
            case R.id.button2:
                playTile (1, myTiles.getHumanLeftTileNum(1), myTiles.getHumanRightTileNum(1));
                break;
            case R.id.button3:
                playTile (2, myTiles.getHumanLeftTileNum(2), myTiles.getHumanRightTileNum(2));
                break;
            case R.id.button4:
                playTile (3, myTiles.getHumanLeftTileNum(3), myTiles.getHumanRightTileNum(3));
                break;
            case R.id.button5:
                playTile (4, myTiles.getHumanLeftTileNum(4), myTiles.getHumanRightTileNum(4));
                break;
            case R.id.button6:
                playTile (5, myTiles.getHumanLeftTileNum(5), myTiles.getHumanRightTileNum(5));
                break;
            case R.id.button7:
                playTile (6, myTiles.getHumanLeftTileNum(6), myTiles.getHumanRightTileNum(6));
                break;
            case R.id.button8:
                playTile (7, myTiles.getHumanLeftTileNum(7), myTiles.getHumanRightTileNum(7));
                break;
            case R.id.button9:
                playTile (8, myTiles.getHumanLeftTileNum(8), myTiles.getHumanRightTileNum(8));
                break;
            case R.id.button10:
                playTile (9, myTiles.getHumanLeftTileNum(9), myTiles.getHumanRightTileNum(9));
                break;
            case R.id.button11:
                playTile (10, myTiles.getHumanLeftTileNum(10), myTiles.getHumanRightTileNum(10));
                break;
            case R.id.button12:
                playTile (11, myTiles.getHumanLeftTileNum(11), myTiles.getHumanRightTileNum(11));
                break;
            case R.id.button13:
                playTile (12, myTiles.getHumanLeftTileNum(12), myTiles.getHumanRightTileNum(12));
                break;
            case R.id.button14:
                playTile (13, myTiles.getHumanLeftTileNum(13), myTiles.getHumanRightTileNum(13));
                break;
            case R.id.button15:
                playTile (14, myTiles.getHumanLeftTileNum(14), myTiles.getHumanRightTileNum(14));
                break;
            case R.id.button16:
                playTile (15, myTiles.getHumanLeftTileNum(15), myTiles.getHumanRightTileNum(15));
                break;
            case R.id.button17:
                playTile (16, myTiles.getHumanLeftTileNum(16), myTiles.getHumanRightTileNum(16));
                break;
            case R.id.button18:
                playTile (17, myTiles.getHumanLeftTileNum(17), myTiles.getHumanRightTileNum(17));
                break;
            case R.id.button19:
                playTile (18, myTiles.getHumanLeftTileNum(18), myTiles.getHumanRightTileNum(18));
                break;
            case R.id.button20:
                playTile (19, myTiles.getHumanLeftTileNum(19), myTiles.getHumanRightTileNum(19));
                break;
        }
    }

    //Values of tile to be played
    int lhs = 0;
    int rhs = 0;
    //For when the user plays a tile
    public void playTile (int buttonNum, int humanLeft, int humanRight)
    {
        String messageToUser = "";
        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        String tilesToCheck = "human";
        if (myRound.getCompPass())
        {
            messageToUser += "The computer passed last turn. You can play non-doubles " +
                    "on either side of the engine this turn.\n";
            tilesToCheck = "pass";
        }
        Boolean validTile = false;

        //Check if tile playable on layout
        if (theBoard.playableTile(tilesToCheck, humanLeft, humanRight) == true) {
            validTile = true;
        } else {
            messageToUser += "That is not a tile that can be played. Please try again.\n";
        }

        //If valid tile to play, make all the buttons invisible:
        if (validTile) {
            ArrayList<Button> buttons = new ArrayList<Button>();
            for (int id : BUTTON_IDS) {
                Button button = (Button) findViewById(id);
                buttons.add(button);
            }
            for (int i = 0; i < Tiles.humanHandSize(); i++) {
                buttons.get(i).setVisibility(View.INVISIBLE);
            }
            Button hintButton = (Button) findViewById(R.id.hintButton);
            hintButton.setVisibility(View.INVISIBLE);

            messageToUser += "You selected ";
            messageToUser += myTiles.getHumanLeftTile(buttonNum) + "-";
            messageToUser += myTiles.getHumanRightTile(buttonNum) + "\n";

            //Check if the tile is a double tile
            Boolean isDouble = false;
            if (humanLeft == humanRight) isDouble = true;
            //User can play on either side with a double tile
            //Or if the computer passed last turn
            if (isDouble == true || tilesToCheck == "pass") {
                //Pass on values of tile to playTile2 method
                lhs = humanLeft;
                rhs = humanRight;
                //Ask user to select left or right button
                messageToUser += "Would you like to play this tile on the left or " +
                        "right side of the layout?\n";

                Button leftButton = (Button) findViewById(R.id.leftButton);
                leftButton.setVisibility(View.VISIBLE);
                Button rightButton = (Button) findViewById(R.id.rightButton);
                rightButton.setVisibility(View.VISIBLE);
            }
            else {
                //If not double tile, play tile on left side by default
                myTiles.playUserTile("default", humanLeft, humanRight, theBoard);

                //If the user's hand is empty, end the round:
                if (myTiles.isHandEmpty("human"))
                {
                    myRound.setRoundOver();
                    endRound("The user's hand is empty.\n");
                }
                else {
                    //Get ok from the user to proceed
                    Button okButton = (Button) findViewById(R.id.okButton);
                    okButton.setVisibility(View.VISIBLE);
                }
            }
        }
        userMessage.setText(messageToUser);
    }

    //If double tile or computer passed last turn
    public void playTile2 (String whichSide)
    {
        //Play tile on user selected side
        myTiles.playUserTile(whichSide, lhs, rhs, theBoard);

        Button leftButton = (Button) findViewById(R.id.leftButton);
        leftButton.setVisibility(View.INVISIBLE);
        Button rightButton = (Button) findViewById(R.id.rightButton);
        rightButton.setVisibility(View.INVISIBLE);

        String messageToUser = "";
        messageToUser += "You played the tile on the " + whichSide + " side.\n";
        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);

        //If the user's hand is empty, end the round:
        if (myTiles.isHandEmpty("human"))
        {
            myRound.setRoundOver();
            endRound("The user's hand is empty.\n");
        }

        //Get ok from the user to proceed to saving
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setVisibility(View.VISIBLE);
    }

    public void hintClick (View view) {
        String tilesToCheck = "human";
        if (myRound.getCompPass())
        {
            tilesToCheck = "pass";
        }
        String messageToUser = "";
        Player humanPlayer = new Player();
        messageToUser += humanPlayer.help(tilesToCheck, myTiles, theBoard);
        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);
    }

    public void leftClick(View view) {
        String messageToUser = "";

        if (theBoard.playableSide("l", lhs, rhs) == true ||
                theBoard.playableSide("l", rhs, lhs))
        {
            playTile2("l");
            messageToUser += "You played the tile on the left side.\n";
        }
        else {
            messageToUser += "That is not a valid side, please try again.\n";
        }

        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);
    }

    public void rightClick(View view) {
        String messageToUser = "";

        if (theBoard.playableSide("r", lhs, rhs) == true ||
                theBoard.playableSide("r", rhs, lhs))
        {
            playTile2("r");
            messageToUser += "You played the tile on the right side.\n";
        }
        else {
            messageToUser += "That is not a valid side, please try again.\n";
        }

        TextView userMessage = (TextView) findViewById(R.id.userMessage);
        userMessage.setText(messageToUser);
    }

    public void okClick(View view) {
        okSave();
    }

    public void saveGameDialog ()
    {
        //Set up for next player's turn
        if (myTiles.isCompTurn())
        {
            //Back to user's turn
            myTiles.setNotCompTurn();

        }
        else {
            //Back to computer's turn
            myTiles.setCompTurn();
        }

        //Save game and return to main activity
        final AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(PlayGameActivity.this).setCancelable(false);;
        alertDialogBuilder.setMessage("Enter a file name: ");
        final EditText userFileName = new EditText(PlayGameActivity.this);
        alertDialogBuilder.setView(userFileName);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int id) {
                if(!userFileName.getText().toString().isEmpty()) {
                    String nextPlayer = "Human";
                    if (myTiles.isCompTurn())
                    {
                        nextPlayer = "Computer";
                    }
                    myTiles.saveGame(myRound, myTourney, userFileName.getText().toString(),
                            nextPlayer, theBoard);

                    Toast.makeText(PlayGameActivity.this, "File saved",
                            Toast.LENGTH_LONG).show();

                    //Set up for new tournament:
                    myTiles.reset();
                    myTourney.reset();
                    myRound.reset();
                    theBoard.reset();

                    //finish();
                    Intent goBack = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(goBack);
                }
                else {
                    Toast.makeText(PlayGameActivity.this, "Name empty, try again",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        alertDialogBuilder.show();
    }

    public void yesClick(View view) {
       saveGameDialog();
    }

    public void noClick(View view) {
        if (myTiles.isCompTurn())
        {
            //Back to user's turn
            myTiles.setNotCompTurn();

        }
        else {
            //Back to computer's turn
            myTiles.setCompTurn();
        }
        startGame();
    }


}

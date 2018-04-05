package com.example.longana;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Model.Layout;
import Model.Round;
import Model.Tiles;
import Model.Tournament;

public class NewGameActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.longana.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    Tournament myTourney = new Tournament();
    //1. Set the tourney score and start drawing 8 tiles:
    public void set200(View view) {
        myTourney.setTourneyScore(200);

        drawTiles();
    }

    public void set400(View view) {
        myTourney.setTourneyScore(400);

        drawTiles();
    }

    public void set600(View view) {
        myTourney.setTourneyScore(600);

        drawTiles();
    }

    public void set800(View view) {
        myTourney.setTourneyScore(800);

        drawTiles();
    }

    public void yesClick(View view) {
        //Save the game and return to MainActivity

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewGameActivity.this);
        alertDialogBuilder.setMessage("Enter a file name: ");
        final EditText userFileName = new EditText(NewGameActivity.this);
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

                    Toast.makeText(NewGameActivity.this, "File saved",
                            Toast.LENGTH_LONG).show();
                            finish();
                    }
                else {
                    Toast.makeText(NewGameActivity.this, "Name empty, try again",
                            Toast.LENGTH_LONG).show();
                }

                }
            });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(NewGameActivity.this,"You clicked cancel",
                        Toast.LENGTH_LONG).show(); }
                //finish();
        });

        alertDialogBuilder.show();

    }

    public void noClick(View view) {
        //Set both yes and no buttons invisible:
        Button noButton = (Button)findViewById(R.id.noButton);
        noButton.setVisibility(View.INVISIBLE);

        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setVisibility(View.INVISIBLE);

        //Play the engine
        if (enginePlayed == false) {
            playEngine();
        }
        else {
            //Launch PlayGameActivity
            Intent intent = new Intent(this, PlayGameActivity.class);
            String message = "";
            //Pass along whose turn it is next:
            if (myTiles.isCompTurn())
            {
                message = "computer";
            }
            else {
                message = "human";
            }
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    boolean enginePlayed = false;
    //2.
    Tiles myTiles = new Tiles(this);
    Layout theBoard = new Layout();
    public void drawTiles()
    {
        //Draw tiles:
        myTiles.newGame(theBoard);

        //Make the prompting text invisible:
        TextView tourneyScoreText = (TextView) findViewById(R.id.tourneyScoreText);
        tourneyScoreText.setVisibility(View.INVISIBLE);

        //Make the buttons invisible:
        Button button200 = (Button)findViewById(R.id.button200);
        button200.setVisibility(View.INVISIBLE);
        Button button400 = (Button)findViewById(R.id.button400);
        button400.setVisibility(View.INVISIBLE);
        Button button600 = (Button)findViewById(R.id.button600);
        button600.setVisibility(View.INVISIBLE);
        Button button800 = (Button)findViewById(R.id.button800);
        button800.setVisibility(View.INVISIBLE);

        //Ask user if they want to save the game:
        TextView textMessage = (TextView) findViewById(R.id.textMessage);
        String displayText = "A new game has started.\nEach player has drawn 8 tiles.";
        displayText += "\n";
        displayText += "Would you like to save the game?";
        textMessage.setText(displayText);

        //Set both yes and no buttons visible:
        Button noButton = (Button)findViewById(R.id.noButton);
        noButton.setVisibility(View.VISIBLE);

        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setVisibility(View.VISIBLE);
    }

    //3.
    Round myRound = new Round();
    public void playEngine()
    {
        myTiles.playEngine(myRound, myTourney, theBoard);

        enginePlayed = true;

        String whoHasEngine = "error";
        //Display message to user about engine being played
        TextView textMessage = (TextView) findViewById(R.id.textMessage);
        if (myTiles.isCompTurn())
        {
            //Show the user their hand if they played the engine
            whoHasEngine = "You have the engine!\n";
            whoHasEngine = "You must play the engine, press OK to continue.\n";
            whoHasEngine += "This is your hand: ";
            //The engine has already been played when asking the user this question
            //Show the engine in the user's hand:
            String engine = " " + Integer.toString(myRound.getEngineNum());
            engine += "-" + Integer.toString(myRound.getEngineNum());
            whoHasEngine += myTiles.printPlayerHand("human");
            whoHasEngine += engine;
            textMessage.setText(whoHasEngine);
        }
        else {
            whoHasEngine = "The computer has the engine.\nComputer plays the engine.\n";
            whoHasEngine += "Press OK to continue.";
            textMessage.setText(whoHasEngine);
        }

        //Ask them to continue
        Button okButton = (Button)findViewById(R.id.okButton);
        okButton.setVisibility(View.VISIBLE);
    }

    //4.
    public void okClick(View view) {
        //Ask user if they want to save the game:
        TextView textMessage = (TextView) findViewById(R.id.textMessage);
        String displayText = "Would you like to save the game (quit)?";
        textMessage.setText(displayText);

        Button okButton = (Button)findViewById(R.id.okButton);
        okButton.setVisibility(View.INVISIBLE);

        //Set both yes and no buttons visible:
        Button noButton = (Button)findViewById(R.id.noButton);
        noButton.setVisibility(View.VISIBLE);

        Button yesButton = (Button)findViewById(R.id.yesButton);
        yesButton.setVisibility(View.VISIBLE);

    }


}

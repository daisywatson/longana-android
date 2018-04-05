package com.example.longana;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Model.Layout;
import Model.Round;
import Model.Tiles;
import Model.Tournament;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.longana.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //1. Yes button, calls performFileSearch to get file from file directory
    public void yesClick(View view) {

        performFileSearch();

    }

    //1. No button, starts new game
    public void noClick(View view) {

        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);

    }

    //2. Open file from Android file directory
    private static final int READ_REQUEST_CODE = 42;
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //3. Called by performFileSearch to get the URI from the document
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                try {

                    Tiles myTiles = new Tiles(this);
                    Layout theBoard = new Layout();
                    Round myRound = new Round();
                    Tournament myTourney = new Tournament();
                    myTiles.loadGame(myRound, myTourney, theBoard, uri);

                    //Start the game after it's been loaded:
                    Intent intent = new Intent(this, PlayGameActivity.class);
                    String message = "load game";
                    intent.putExtra(EXTRA_MESSAGE, message);

                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


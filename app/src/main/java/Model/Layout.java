package Model;

import java.util.ArrayList;

public class Layout {
    protected static ArrayList<Integer> playedLeft = new ArrayList<Integer>();
    protected static ArrayList<Integer> playedRight = new ArrayList<Integer>();

    //Reset all values in class for a completely new tournament
    public static void reset()
    {
        playedLeft.clear();
        playedRight.clear();
    }

    public static boolean enginePlayed(Round myRound)
    {
        boolean found = false;

        //Get the engine number:
        int engineNum = myRound.getEngineNum();

        for (int i = 0; i < playedLeft.size(); i++)
        {
            if (playedLeft.get(i) == engineNum)
            {
                found = true;
                break;
            }
        }

        return found;
    }


    public static boolean playableTile(String whichPlayer, int lhs, int rhs)
    {
        boolean isPlayable = false;
        //Left pips of the tile on the lhs of the layout
        int playedLeftLeft = playedLeft.get(0);
        //Right pips of the tile on the rhs of the layout
        int playedRightRight = playedRight.get(playedRight.size() - 1);

        if (whichPlayer == "pass")
        {
            if (rhs == playedLeftLeft || lhs == playedLeftLeft
                    || lhs == playedRightRight || rhs == playedRightRight)
            {
                isPlayable = true;
            }
        }
        //If the tile is a double
        else if (lhs == rhs)
        {
            if (lhs == playedLeftLeft || lhs == playedRightRight)
            {
                isPlayable = true;
            }
        }
        //else check if the tile is playable as a non-double
        else {
            if (whichPlayer == "human")
            {
                //The user can only play tiles on the LHS of the layout
                if (rhs == playedLeftLeft || lhs == playedLeftLeft)
                {
                    isPlayable = true;
                }
            }
            else {
                if (lhs == playedRightRight || rhs == playedRightRight)
                {
                    //The computer can only play tiles on the RHS of the layout
                    isPlayable = true;
                }
            }
        }

        return isPlayable;
    }

    public static boolean playableSide(String side, int lhs, int rhs)
    {
        boolean playable = false;
        //Left pips of the tile on the lhs of the layout
        int playedLeftLeft = playedLeft.get(0);
        //Right pips of the tile on the rhs of the layout
        int playedRightRight = playedRight.get(playedRight.size() - 1);

        if (side == "r")
        {
            if (lhs == playedRightRight)
            {
                playable = true;
            }
        }
        else {
            if (rhs == playedLeftLeft)
            {
                playable = true;
            }
        }

        return playable;
    }


    public static boolean rotateTile(String side, int lhs, int rhs)
    {
        boolean rotate = false;
        //Left pips of the tile on the lhs of the layout
        int playedLeftLeft = playedLeft.get(0);
        //Right pips of the tile on the rhs of the layout
        int playedRightRight = playedRight.get(playedRight.size() - 1);

        if (side == "r")
        {
            if (rhs == playedRightRight)
            {
                rotate = true;
            }
        }
        else {
            if (lhs == playedLeftLeft)
            {
                rotate = true;
            }
        }

        return rotate;
    }


    public static String printLayout()
    {
        //Print the layout in 3 rows to represent the cross-wise double tiles
        //Top row:
        //Two spaces for L
        String toPrint = "  ";
        for (int i = 0; i < playedLeft.size(); i++)
        {
            if (playedLeft.get(i) == playedRight.get(i))
            {
                toPrint += playedLeft.get(i);
                toPrint += " ";
            }
            else
            {
                toPrint += "    ";
            }
        }
        toPrint += "\n";
        //Middle row:
        toPrint += "L ";
        for (int j = 0; j < playedLeft.size(); j++)
        {
            if (playedLeft.get(j) == playedRight.get(j))
            {
                toPrint += "| ";
            }
            else
            {
                toPrint += playedLeft.get(j);
                toPrint += "-";
                toPrint += playedRight.get(j);
                toPrint += " ";
            }
        }
        toPrint += "R";
        toPrint += "\n";
        //Bottom row:
        //Two spaces for L
        toPrint += "  ";
        for (int k = 0; k < playedLeft.size(); k++)
        {
            if (playedLeft.get(k) == playedRight.get(k))
            {
                toPrint += playedLeft.get(k);
                toPrint += " ";
            }
            else
            {
                toPrint += "    ";
            }
        }
        toPrint += "\n";

        return toPrint;
    }

}

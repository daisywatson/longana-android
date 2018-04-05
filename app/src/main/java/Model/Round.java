package Model;

public class Round {
    //Number of rounds
    protected static int rounds = 1;

    //The engine number
    protected static int engineNum = 6;

    //If the round is over:
    protected static boolean roundOver = false;

    //If the user or computer has passed last turn:
    protected static boolean userPass = false;
    protected static boolean compPass = false;


    //Reset all values in class for a completely new tournament
    public static void reset()
    {
        rounds = 1;
        engineNum = 6;
        roundOver = false;
        userPass = false;
        compPass = false;
    }

    public static int getRounds()
    {
        return rounds;
    }

    public static int getEngineNum()
    {
        return engineNum;
    }

    public static int determineEngineNum()
    {
        int determinedNum = 6;

        for (int i = 1; i < rounds; i++)
        {
            if (determinedNum == 0)
            {
                determinedNum = 6;
            }
            else {
                determinedNum--;
            }
        }

        return determinedNum;
    }



    public static boolean getUserPass()
    {
        return userPass;
    }

    public static boolean getCompPass()
    {
        return compPass;
    }

    public static void setRounds(int inputRounds)
    {
        rounds = inputRounds;
    }

    public static void setEngineNum(int inputEngineNum)
    {
        engineNum = inputEngineNum;
    }

    public static void setUserPass()
    {
        userPass = true;
    }

    public static void setCompPass()
    {
        compPass = true;
    }

    public static void resetUserPass()
    {
        userPass = false;
    }

    public static void resetCompPass()
    {
        compPass = false;
    }

    //Set the round as over
    public static void setRoundOver()
    {
        roundOver = true;
    }

    public static void resetRoundOver() {roundOver = false;}

    public static void endRound()
    {
        roundOver = false;
        userPass = false;
        compPass = false;
        rounds++;

        if (engineNum == 0)
        {
            engineNum = 6;
        }
        else {
            engineNum--;
        }

    }

    //Return if a round is over
    public boolean isRoundOver()
    {
        return roundOver;
    }

}

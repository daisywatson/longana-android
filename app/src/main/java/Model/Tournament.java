package Model;


public class Tournament {
    protected static int tourneyScore = 200;

    protected static int humanScore = 0;

    protected static int compScore = 0;


    //Reset all values in class for a completely new tournament
    public static void reset() {
        tourneyScore = 200;
        humanScore = 0;
        compScore = 0;
    }

    public static int getTourneyScore()
    {
        return tourneyScore;
    }

    public static int getHumanScore()
    {
        return humanScore;
    }

    public static int getCompScore()
    {
        return compScore;
    }


    public static void setTourneyScore(int inputScore)
    {
        tourneyScore = inputScore;
    }

    public static void setCompScore(int inputScore)
    {
        compScore = inputScore;
    }

    public static void addCompScore (int inputScore) { compScore += inputScore; }

    public static void setHumanScore(int inputScore)
    {
        humanScore = inputScore;
    }

    public static void addHumanScore (int inputScore) { humanScore += inputScore; }


    public static boolean isTourneyOver()
    {
        if (humanScore >= tourneyScore)
        {
            return true;
        }
        else if (compScore >= tourneyScore)
        {
            return true;
        }
        else return false;
    }

    //Who won the overall tournament
    public String whoWon()
    {
        String winner = "";
        if (humanScore > compScore)
        {
            winner += "The human won!\n";
        }
        else if (compScore > humanScore)
        {
            winner += "The computer won!\n";
        }
        else {
            winner += "It's a tie!\n";
        }
        winner += "The computer's score is: " + this.getCompScore() + "\n";
        winner += "The human's score is: " + this.getHumanScore() + "\n";

        return winner;
    }

}

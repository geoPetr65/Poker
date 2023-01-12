/*
rationale: Make a poker game that works with betting and stuff as long as it works
 

 */
package poker;

import java.io.FileInputStream;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Poker { // CLASS WHERE GAME TAKES PLACE

    public static char getChar(String Word) {
        Word = Word.toLowerCase();
        char letter = Word.charAt(0);
        return letter;
    }

    public static int playRound(int goToRiver, boolean keepLooping) {
        for (int i = 0; i < 2; i++) {
            goToRiver = money.play();
            if (money.player1DidFold == true | money.player2DidFold == true | goToRiver == -1) {
                goToRiver = -1;
                break;
            }
        }
        // System.out.println("for TESTING purposes: " + money.Player1Name + "'s bet is
        // " + money.player1Bet + " and their balance is " + money.player1Balance + ".
        // MEANWHILE " + money.Player2Name + "'s bet is " + money.player2Bet + " and
        // their balance is " + money.player2Balance);
        do {
            if (goToRiver == -1) {
                if (money.player1AllIn == true & money.player2AllIn == true) {
                    System.out.println(
                            "Both " + money.Player1Name + " and " + money.Player2Name + " have gone all in. Exciting!");
                    return goToRiver;
                } else if (money.player1AllIn == true & money.player2DidFold == true) {
                    System.out.println(
                            "WOW! " + money.Player1Name + " went all in, causing " + money.Player2Name + " to fold.");
                    money.wonByFold();
                    break;
                } else if (money.player1AllIn == true & money.player2Bet == money.player1Bet) {
                    System.out.println(
                            "WOW! " + money.Player2Name + " went all in, causing " + money.Player1Name + " to fold.");
                    money.wonByFold();
                    break;
                } else if (money.player1AllIn == true & money.player2Bet == money.player1Bet) {
                    System.out.println(
                            money.Player1Name + " has gone all in and " + money.Player2Name + " has called their bet.");
                    return goToRiver;
                } else if (money.player2AllIn & money.player2Bet == money.player1Bet) {
                    System.out.println(
                            money.Player2Name + " has gone all in and " + money.Player1Name + " has called their bet.");
                    return goToRiver;
                }

            } else if (money.player2Bet > money.player1Bet & money.player1AllIn != true & money.player1Balance != 0) {
                goToRiver = money.p2Raised();
            } else if (money.player1Bet > money.player2Bet & money.player2AllIn != true & money.player2Balance != 0) {
                goToRiver = money.p1Raised();
            }
        } while (money.player1Bet != money.player2Bet & keepLooping == true);
        return goToRiver;
    }

    public static void main(String[] args) {
        Scanner Input = new Scanner(System.in);
        int i = 0; // add 2 at the end of each iteration
        int whichPlayerWon = 0; // for determining final result
        money players = new money(); // creaters player of money class
        double pot = 0.0;
        boolean keepLooping = true;
        int goToRiver = 1;
        // blind
        for (;;) {
            // CREATES ALL OBJECTS AND OBJECT ARRAYS
            Deck[] deck = new Deck[1];
            deck[i] = new Deck(true);
            Player[] user = new Player[2];
            user[i] = new Player();
            user[i + 1] = new Player();
            Table[] table = new Table[1];
            table[i] = new Table(deck[i]);

            // sets player names across 2 classes
            user[i].PlayerName = money.Player1Name;
            user[i + 1].PlayerName = money.Player2Name;

            // VARIABLES FOR TESTING
            Card[] userCards1 = new Card[2];
            Card[] userCards2 = new Card[2];

            table[i].TableSet[0] = new Card(1, 12);
            table[i].TableSet[1] = new Card(2, 12);
            table[i].TableSet[2] = new Card(3, 12);
            table[i].TableSet[3] = new Card(4, 4);
            table[i].TableSet[4] = new Card(1, 6);

            // TESTING
            userCards1 = user[i].sendCardsForComparison();
            userCards1[0] = new Card(1, 1);
            userCards1[1] = new Card(1, 12);
            // user[i].printHand();

            userCards2 = user[i + 1].sendCardsForComparison();
            userCards2[0] = new Card(3, 5);
            userCards2[1] = new Card(2, 5);
            // user[i + 1].printHand();

            // dealing cards to players
            for (int j = 0; j < 2; j++) {
                user[i].addtoHand(deck[i].dealCard());
                user[i + 1].addtoHand(deck[i].dealCard());
            }

            // DISPLAYING HANDS BEFORE BLIND BET
            System.out.println(user[i].PlayerName + "'s hand:\n");
            user[i].printHand();
            System.out.println(user[i + 1].PlayerName + "'s hand:\n");
            user[i + 1].printHand();

            System.out.println("\nBLIND BETTING:\n");
            goToRiver = playRound(goToRiver, keepLooping);
            if (goToRiver == -1) {
                // flop

                System.out.println("\nFLOP:\n");

                // turn
                System.out.println("\nTURN:\n");

                // river
                System.out.println("\nRIVER:\n");
                // GOES THROUGH HAND COMPARISON IN THE TABLE CLASS TO SEE WHICH PLAYER WON THE
                // ROUND
                whichPlayerWon = table[i].handComparison(user[i], user[i + 1], table[i]);
                money.winnings(whichPlayerWon); // PERSON WHO WINS EARNS WINNNINGS
                money.saveFile(); // UPDATE FILE
                // DISPLAYS RESULTS TO USER
                System.out.println("player 1 bet: " + money.player1Bet + " player 1 balance: " + money.player1Balance);
                System.out.println("player 2 bet: " + money.player2Bet + " player 2 balance: " + money.player2Balance);
                i = i + 2;
                break;
            }
            // DISPLAYS PLAYER HANDS AND TABLE CARDS FOR FLOP BETTING (3 TABLE CARDS
            // VISIBLE)
            System.out.println(user[i].PlayerName + "'s hand:\n");
            user[i].printHand();
            System.out.println(user[i + 1].PlayerName + "'s hand:\n");
            user[i + 1].printHand();
            table[i].displayTable(table[i].TableSet, 3);
            // flop
            System.out.println("\nFLOP BETTING:\n");
            money.readyNextTurn();
            goToRiver = playRound(goToRiver, keepLooping);
            if (goToRiver == -1) {
                // turn
                System.out.println("\nTURN:\n");
                // river
                System.out.println("\nRIVER:\n");
                whichPlayerWon = table[i].handComparison(user[i], user[i + 1], table[i]);
                money.winnings(whichPlayerWon);
                money.saveFile();
                System.out.println("player 1 bet: " + money.player1Bet + " player 1 balance: " + money.player1Balance);
                System.out.println("player 2 bet: " + money.player2Bet + " player 2 balance: " + money.player2Balance);
                i = i + 2;
                break;
            }
            // DISPLAYS HANDS FOR TURN BETTING
            System.out.println(user[i].PlayerName + "'s hand:\n");
            user[i].printHand();
            System.out.println(user[i + 1].PlayerName + "'s hand:\n");
            user[i + 1].printHand();
            table[i].displayTable(table[i].TableSet, 4);
            // turn
            System.out.println("\nTURN BETTING:\n");
            money.readyNextTurn();
            goToRiver = playRound(goToRiver, keepLooping);
            if (goToRiver == -1) {
                // river
                System.out.println("\nRIVER:\n");
                whichPlayerWon = table[i].handComparison(user[i], user[i + 1], table[i]);
                money.winnings(whichPlayerWon);
                money.saveFile();
                System.out.println("player 1 bet: " + money.player1Bet + " player 1 balance: " + money.player1Balance);
                System.out.println("player 2 bet: " + money.player2Bet + " player 2 balance: " + money.player2Balance);
                i = i + 2;
                break;
            }
            // DISPLAYS HANDS FOR RIVER BETTING
            System.out.println(user[i].PlayerName + "'s hand:\n");
            user[i].printHand();
            System.out.println(user[i + 1].PlayerName + "'s hand:\n");
            user[i + 1].printHand();
            table[i].displayTable(table[i].TableSet, 5);
            // river
            System.out.println("\nRIVER BETTING:\n");
            money.readyNextTurn();
            goToRiver = playRound(goToRiver, keepLooping);
            whichPlayerWon = table[i].handComparison(user[i], user[i + 1], table[i]);
            money.winnings(whichPlayerWon);
            money.saveFile();
            System.out.println("player 1 bet: " + money.player1Bet + " player 1 balance: " + money.player1Balance);
            System.out.println("player 2 bet: " + money.player2Bet + " player 2 balance: " + money.player2Balance);
            i = i + 2;
            break;
        }
        // resultCases(result);

    }

}// poker class
/*
 * CARD CLASS IS RESPONSIBLE FOR THE CREATION OF CARD OBJECTS TO CREATE ONE,
 * INTEGERS REPRESENTING SUITS AND RANKS HAVE TO BE IN THE CONSTRUCTOR
 * PARAMETERS
 */

class Card {
    // MEMBERS
    private String Suit;
    private int rank;
    private int numValueOfSuit;

    // CONSTRUCTOR
    public Card(int Asuit, int Arank) {
        numValueOfSuit = Asuit;
        if (Asuit >= 1 && Asuit <= 4) {
            switch (Asuit) {
                case 1:
                    Suit = "Diamonds";
                    break;
                case 2:
                    Suit = "Hearts";
                    break;
                case 3:
                    Suit = "Clubs";
                    break;
                case 4:
                    Suit = "Spades";
                    break;
            }
        } else {
            System.out.println(Asuit + " is not a real position for a suit");
        }

        if (Arank >= 1 && Arank <= 13) {
            rank = Arank;
        } else {
            System.err.println(Arank + " is not a valid Card number");

        }

    }

    // GETS THE RANK INTEGER OF A CARD OBJECT
    public int getRank() {
        return rank;
    }

    // GETS THE INTEGER SUIT OF A CARD OBJECT
    public int getSuit() {
        return numValueOfSuit;
    }

    // LOOKS FOR THE STRING EQUIVALENT
    public String findSuitStr(int suitNum) {
        String SuitAsStr;
        switch (suitNum) {
            case 1:
                SuitAsStr = "Diamonds";
                break;
            case 2:
                SuitAsStr = "Hearts";
                break;
            case 3:
                SuitAsStr = "Clubs";
                break;
            case 4:
                SuitAsStr = "Spades";
                break;
            default:
                SuitAsStr = "ERR";
                break;
        }

        return SuitAsStr;
    }

    // CONVERTS CARD TO STRING SO IT CAN BE DISPLAYED
    public String convertToString() {
        String RankStr = "OOps made mistake";

        switch (rank) {
            case 1:
                RankStr = "Ace";
                break;
            case 2:
                RankStr = "Two";
                break;
            case 3:
                RankStr = "Three";
                break;
            case 4:
                RankStr = "Four";
                break;
            case 5:
                RankStr = "Five";
                break;
            case 6:
                RankStr = "Six";
                break;
            case 7:
                RankStr = "Seven";
                break;
            case 8:
                RankStr = "Eight";
                break;
            case 9:
                RankStr = "Nine";
                break;
            case 10:
                RankStr = "Ten";
                break;
            case 11:
                RankStr = "Jack";
                break;
            case 12:
                RankStr = "Queen";
                break;
            case 13:
                RankStr = "King";
                break;
        }

        return RankStr + " of " + Suit;
    }

}// end of card class
 // DECK CLASS RESPONSIBLE FOR CREATING AND MANAGING 52 CARD OBJECTS

class Deck {

    private static int numOfCards = 52;
    public static Card[] manyCards;
    boolean shuffled = false;

    public int getNumOfCards() {
        return numOfCards;
    }

    // CONSTRUCTOR FOR DECK, BOOLEAN IS USED TO SHUFFLE THE DECK SO THAT NO BOOLEAN
    // TRACKING IS NECCESARY
    Deck(boolean shuffled) { // boolean shuffle as param

        manyCards = new Card[numOfCards];
        int card = 0; // card index

        for (int s = 1; s < 5; s++) {
            for (int r = 1; r < 14; r++) {
                manyCards[card] = new Card(s, r);
                card++;
            }
        } // end of for loops
        if (shuffled == true) {
            shuffleDeck();
        }
    }

    // METHOD TO SHUFFLE A DECK OBJECT
    public void shuffleDeck() {

        int p; // placeHolderIndex

        // random num gen
        Random ran = new Random();

        // temporary card for swap to hold a value
        Card temporary;

        for (int i = 0; i < numOfCards; i++) {
            // get a random card to swap i's value with
            p = ran.nextInt(numOfCards);

            temporary = manyCards[i];
            manyCards[i] = manyCards[p];
            manyCards[p] = temporary;

        }
    }

    // DISPLAYS DECK MOSTLY USED FOR TESTING PURPOSES
    public void printDeck(int numOfCardsToPrint) {

        for (int i = 0; i < numOfCardsToPrint; i++) {
            System.out.println("Card: " + (i + 1) + "/52 " + manyCards[i].convertToString());
        }

    }

    // DEALS CARD WHEN CALLED
    public Card dealCard() {
        Card TopCard = manyCards[numOfCards - 1];

        for (int c = numOfCards - 1; c > 0; c--) {
            TopCard = manyCards[numOfCards - 1];
            manyCards[numOfCards - 1] = null;
            break;
        }

        numOfCards = numOfCards - 1;

        return TopCard;
    }
}// end of deck class
 // PLAYER CLASS RESPONSIBLE FOR CONTROL AND ACTIONS OF USERS AND THEIR HANDS

class Player {

    Scanner Input = new Scanner(System.in);
    public String PlayerName;
    private Card[] hand = new Card[2]; // the cards in the player's current hand
    private int numCards; // the num of cards in th

    Player() {

        emptyHand();
        // System.out.println("You have " + numCards + " cards in your hand");
    }

    public void emptyHand() { // reset the player's hand to have no cards

        for (int c = 0; c < 2; c++) {
            hand[c] = null;
        }
        numCards = 0; // no cards in hand

    }

    // ADDS TO HAND OF PLAYER
    public void addtoHand(Card addedCard) {
        // Player.addtoHand(deck[i].dealCard());
        try {
            hand[numCards] = addedCard;
            numCards = numCards + 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Two cards have already been dealt to " + PlayerName);
        }
    }

    // SENDS A CARDS OF HAND SO THAT THEY CAN BE COMPARED TO DECLARE A WINNER
    public Card[] sendCardsForComparison() {
        return hand;
    }

    // METHOD TO PRINT HAND OF PLAYER
    public void printHand() {
        if (hand[0] == null && hand[1] == null) {
            System.out.println(PlayerName + ", you have no cards bro");
        } else {
            try {
                System.out.println(PlayerName + ", your hand consists of " + hand[0].convertToString() + " and "
                        + hand[1].convertToString());
            } catch (NullPointerException e) {
                System.out.println(PlayerName + ", your hand consists of " + hand[0].convertToString());
            }

        }
    }

    // TO DETERMINE THE HIGHEST CARD OF THE TWO IN THE HAND
    public Card highUserCard(Card[] uHand) {

        int highest = uHand[0].getRank();
        Card HighCard = uHand[0];

        for (int i = 1; i < hand.length; i++) {
            if (uHand[i].getRank() > highest || uHand[i].getRank() == 1) {
                highest = uHand[i].getRank();
                HighCard = uHand[i];
            }
        }
        return HighCard;
    }

    // TO DETERMINE THE LOWEST CARD OF THE TWO IN THE HAND
    public Card lowUserCard(Card[] uHand) {
        int lowest = uHand[0].getRank();
        Card lowCard = uHand[0];

        for (int i = 1; i < hand.length; i++) {
            if (uHand[i].getRank() < lowest) {
                lowest = uHand[i].getRank();
                lowCard = uHand[i];
            }
        }

        return lowCard;
    }

} // end of player class
  // TABLE CLASS RESPONSIBLE FOR CHECKING THE SITUATION OF CARDS ON TABLE,
  // AND COMPARING HANDS TO DETERMINE WINNER

class Table {

    Card[] TableSet = new Card[5];
    Card[] userCards1 = new Card[2];
    Card[] userCards2 = new Card[2];

    // DEALS CARDS TO THE TABLE HAND
    Table(Deck d1) {
        for (int i = 0; i < 5; i++) {
            TableSet[i] = d1.dealCard();
        }
    }

    // USED TO DISPLAY INFO AND HOW MUCH OF IT
    public void displayTable(Card[] TableSet, int numOfCardsToPrint) {
        int[] suitArray = new int[5];
        int[] rankArray = new int[5];

        for (int j = 0; j < numOfCardsToPrint; j++) {
            suitArray[j] = TableSet[j].getSuit();
            rankArray[j] = TableSet[j].getRank();
            System.out.println(
                    "Table card " + (j + 1) + "'s suit is " + suitArray[j] + " and its rank is " + rankArray[j]);
        }
    }

    // COMPARES HANDS OF PLAYERS AND THE TABLE HAND AS WELL
    public int handComparison(Player player1, Player player2, Table table) {
        userCards1 = player1.sendCardsForComparison();
        userCards2 = player2.sendCardsForComparison();

        int[] ts = new int[5]; // table suit
        int[] tr = new int[5];
        int[] us1 = new int[2];
        int[] ur1 = new int[2];
        int[] us2 = new int[2];
        int[] ur2 = new int[2];

        int lowestValue = 0;

        Card highestCard = table.highestRank(tr, TableSet);
        Card highCard1 = player1.highUserCard(userCards1);
        Card highCard2 = player2.highUserCard(userCards2);
        Card lowCard1 = player1.lowUserCard(userCards1);
        Card lowCard2 = player1.lowUserCard(userCards1);

        for (int a = 0; a < 2; a++) {
            us1[a] = userCards1[a].getSuit();
            ur1[a] = userCards1[a].getRank();
            System.out.println(
                    player1.PlayerName + "'s card #" + (a + 1) + "'s suit is " + us1[a] + " and its rank is " + ur1[a]);
        }
        for (int b = 0; b < 2; b++) {
            us2[b] = userCards2[b].getSuit();
            ur2[b] = userCards2[b].getRank();
            System.out.println(
                    player2.PlayerName + "'s card #" + (b + 1) + "'s suit is " + us2[b] + " and its rank is " + ur2[b]);
        }
        for (int i = 0; i < 5; i++) {
            ts[i] = TableSet[i].getSuit();
            tr[i] = TableSet[i].getRank();
        }

        displayTable(TableSet, 5);

        // IF all five table cards have same value
        // only five cards can fit in the comparison (if its four of a kind, pairs dont
        // matter ONLY HIGHEST CARD)
        // VARIABLES TO AID IN THE COMPARISON
        int numSuits;
        numSuits = numOfMatchingSuits(ts); // CALLS METHOD THAT FINDS THE NUMBER OF MATCHING SUITS IN A GIVEN ARRAY
        int numOfSameRanks;
        numOfSameRanks = numOfMatchingRanks(tr); // CALLS METHOD THAT FINDS THE NUMBER OF MATCHING RANKS IN A GIVEN
                                                 // ARRAY

        lowestValue = table.lowestRank(tr); // LOWEST CARD ON TABLE
        int score = 0; // SCORES ARE USED TO KEEP TRACK OF THE THREE DIFFERENT OUTCOMES
        // ROYAL FLUSH ON TABLE
        if (table.royalFlush(numSuits, table, tr) == true) {
            System.out.println("ROYAL FLUSH ON TABLE! Tie!");
            return 3;
            // STRAIGHT FLUSH ON TABLE (8 TO 12)
        } else if (table.straightFlushEightToTwelve(numSuits, table, tr) == true) {
            System.out.println("STRAIGHT FLUSH ON TABLE! TIE UNLESS PLAYER HAND CAN PULL OFF ROYAL FLUSH");
            if (us2[0] == us2[1] && us2[1] == ts[1] && ur2[0] == 13 && ur2[1] == 1 || ur2[0] == 1 && ur2[1] == 13) {
                score = 2;
                return score;
            } else if (us1[0] == us1[1] && us1[1] == ts[1] && ur1[0] == 13 && ur1[1] == 1
                    || ur1[0] == 1 && ur1[1] == 13) {
                score = 1;
                return score;
            } else if (us1[0] == us1[1] && us1[1] == ts[1] && ur1[0] == 13) {
                score = 1;
                return score;

            } else if (us2[0] == us2[1] && us2[1] == ts[1] && ur2[0] == 13) {
                score = 2;
                return score;
            } else {
                System.out.println("Playing off of the community cards");
                score = 3;
                return score;
            }
            // STRAIGHT FLUSH
        } else if (numSuits == 5 && table.straight(tr, table) == true) {
            System.out.println("STRAIGHT FLUSH ON TABLE!");
            if (highCard1.getRank() == highestCard.getRank() + 1 && highCard1.getSuit() == highestCard.getSuit()) {
                score = 1;
                return score;
            } else if (highCard2.getRank() == highestCard.getRank() + 1
                    && highCard2.getSuit() == highestCard.getSuit()) {
                score = 2;
                return score;
            } else {
                score = 3;
                return score;
            }

            // FOUR OF A KIND ON TABLE
        } else if (numOfSameRanks == 4) {
            System.out.println("FOUR OF A KIND");
            if (highCard1.getRank() > highCard2.getRank()) {
                score = 1;
                return score;
            } else if (highCard2.getRank() > highCard1.getRank()) {
                score = 2;
                return score;
            } else if (highCard2.getRank() == highCard1.getRank()) {
                score = 3;
                return score;
            }
            // FULL HOUSE ON TABLE
        } else if (numOfSameRanks == 3 && table.pairs(tr) == true) {
            System.out.println("FULL HOUSE");
            if (table.containsHowMany(tr, highestCard.getRank()) == 2 && ur1[0] == ur1[1]
                    && ur1[1] == highestCard.getRank()) {
                score = 1;
                return score;
            } else if (table.containsHowMany(tr, highestCard.getRank()) == 2 && ur2[0] == ur2[1]
                    && ur2[1] == highestCard.getRank()) {
                score = 2;
                return score;
            } else if (table.containsHowMany(tr, highestCard.getRank()) == 3 && ur1[0] == highestCard.getRank()
                    || ur1[1] == highestCard.getRank()) {
                score = 1;
                return score;
            } else if (table.containsHowMany(tr, highestCard.getRank()) == 3 && ur2[0] == highestCard.getRank()
                    || ur2[1] == highestCard.getRank()) {
                score = 2;
                return score;
            } else if (table.containsHowMany(tr, highestCard.getRank()) == 2 && ur1[0] == highestCard.getRank()
                    || ur1[1] == highestCard.getRank()
                            && ur2[0] != highestCard.getRank()
                    || ur2[1] != highestCard.getRank()) {
                score = 1;
                return score;
            } else if (table.containsHowMany(tr, highestCard.getRank()) == 2 && ur2[0] == highestCard.getRank()
                    || ur2[1] == highestCard.getRank()
                            && ur1[0] != highestCard.getRank()
                    || ur1[1] != highestCard.getRank()) {
                score = 2;
                return score;
            } else {
                score = 3;
                return score;
            }
            // FLUSH ON TABLE
        } else if (numSuits == 5 && table.straight(tr, table) != true) {
            System.out.println("FLUSH");
            if (highCard1.getSuit() == highestCard.getSuit() && highCard1.getRank() == highestCard.getRank() + 1
                    || highCard1.getRank() == 1 && highestCard.getRank() == 13) {
                score = 1;
                return score;
            } else if (highCard2.getSuit() == highestCard.getSuit() && highCard2.getRank() == highestCard.getRank() + 1
                    || highCard2.getRank() == 1 && highestCard.getRank() == 13) {
                score = 2;
                return score;
            } else {
                score = 3;
                return score;
            }

            // STRAIGHT ON TABLE
        } else if (numSuits < 5 && table.straight(tr, table) == true) {
            System.out.println("STRAIGHT");
            if (straightToStraightF(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table) == true
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard1.getRank() < highCard2.getRank()) {
                    score = 2;
                    return score;
                }
            } else if (highCard1.getRank() == highestCard.getRank() + 1
                    || highestCard.getRank() == 13 && highCard1.getRank() == 1) {
                score = 1;
                return score;
            } else if (highCard2.getRank() == highestCard.getRank() + 1
                    || highestCard.getRank() == 13 && highCard2.getRank() == 1) {
                score = 2;
                ;
                return score;
            } else {
                score = 3;
                return score;
            }

            // THREE OF A KIND
        } else if (numOfSameRanks == 3 && table.pairs(tr) != true) {
            System.out.println("THREE OF A KIND ON TABLE");
            if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            }
        } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                && threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
            if (lowCard1.getRank() > lowCard2.getRank()) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (lowCard1.getRank() < lowCard2.getRank()) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
            } else if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;

            } else if (highCard1.getRank() > highCard2.getRank()) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (highCard1.getRank() < highCard2.getRank()) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;

            } else {
                score = 3;
                return score;
            }
            // TWO PAIRS
        } else if (twoPairs(tr, table) == true) {
            System.out.println("TWO PAIRS");

            if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    System.out.println(player1.PlayerName + " won");
                    return score;
                } else if (highCard1.getRank() < highCard2.getRank()) {
                    score = 2;
                    System.out.println(player2.PlayerName + " won");
                    return score;
                }
            } else if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                if (lowCard1.getRank() > lowCard2.getRank()) {
                    score = 1;
                    System.out.println(player1.PlayerName + " won");
                    return score;
                } else if (lowCard1.getRank() < lowCard2.getRank()) {
                    score = 2;
                    System.out.println(player2.PlayerName + " won");
                    return score;
                }
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                if (table.contains(tr, highCard1.getRank()) == true
                        && table.contains(tr, highCard2.getRank()) == true) {
                    if (highCard1.getRank() > highCard2.getRank()) {
                        score = 1;
                        System.out.println(player1.PlayerName + " won");
                        return score;
                    } else if (highCard1.getRank() < highCard2.getRank()) {
                        score = 2;
                        System.out.println(player2.PlayerName + " won");
                        return score;
                    } else {
                        score = 3;
                        return score;
                    }
                } else if (table.contains(tr, highCard1.getRank()) == true
                        && table.contains(tr, lowCard2.getRank()) == true) {
                    if (highCard1.getRank() > lowCard2.getRank()) {
                        score = 1;
                        System.out.println(player1.PlayerName + " won");
                        return score;
                    } else if (highCard1.getRank() < lowCard2.getRank()) {
                        score = 2;
                        System.out.println(player2.PlayerName + " won");
                        return score;
                    } else {
                        score = 3;
                        return score;
                    }
                } else if (table.contains(tr, lowCard1.getRank()) == true
                        && table.contains(tr, lowCard2.getRank()) == true) {
                    if (lowCard1.getRank() > lowCard2.getRank()) {
                        score = 1;
                        System.out.println(player1.PlayerName + " won");
                        return score;
                    } else if (lowCard1.getRank() < lowCard2.getRank()) {
                        score = 2;
                        System.out.println(player2.PlayerName + " won");
                        return score;
                    } else {
                        score = 3;
                        return score;
                    }
                } else if (table.contains(tr, lowCard1.getRank()) == true
                        && table.contains(tr, highCard2.getRank()) == true) {
                    if (lowCard1.getRank() > highCard2.getRank()) {
                        score = 1;
                        System.out.println(player1.PlayerName + " won");
                        return score;
                    } else if (lowCard1.getRank() < highCard2.getRank()) {
                        score = 2;
                        System.out.println(player2.PlayerName + " won");
                        return score;
                    } else {
                        score = 3;
                        return score;
                    }
                }
            } else if (highCard1.getRank() > highCard2.getRank()) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (highCard1.getRank() < highCard2.getRank()) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
            } else {
                score = 3;
                return score;
            }

            // JUST A PAIR
        } else if (twoPairs(tr, table) != true && table.pairs(tr) == true) {
            System.out.println("ONE PAIR ON TABLE");
            // ROYAL FLUSH
            if (toRoyalFlush(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (toRoyalFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
                // may become straight flush
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards1, table) == true
                    && straightToStraightF(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard1.getRank() < highCard2.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
            } else if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (ToFourOfKind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                if (lowCard1.getRank() > lowCard2.getRank()) {
                    score = 1;
                    System.out.println(player1.PlayerName + " won");
                    return score;
                } else if (lowCard1.getRank() < lowCard2.getRank()) {
                    score = 2;
                    System.out.println(player2.PlayerName + " won");
                    return score;
                }
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                System.out.println(player1.PlayerName + " won");
                return score;
            } else if (threeOfToFullH(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                System.out.println(player2.PlayerName + " won");
                return score;
                // flush
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getSuit() == highestCard.getSuit() && highCard1.getRank() == highestCard.getRank() + 1
                        || highCard1.getRank() == 1 && highestCard.getRank() == 13) {
                    score = 1;
                    return score;
                } else if (highCard2.getSuit() == highestCard.getSuit()
                        && highCard2.getRank() == highestCard.getRank() + 1
                        || highCard2.getRank() == 1 && highestCard.getRank() == 13) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }

            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards2, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
                // straight
            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                }

            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (highCard1.getRank() > highCard2.getRank()) {
                score = 1;
                return score;
            } else if (highCard2.getRank() > highCard1.getRank()) {
                score = 2;
                return score;
            } else {
                score = 3;
                return score;
            }
            // HIGH CARD
        } else {
            if (toRoyalFlush(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (toRoyalFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;
                // may become straight flush
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards1, table) == true
                    && straightToStraightF(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard1.getRank() < highCard2.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards1, table) == true) {
                score = 1;
                return score;
            } else if (straightToStraightF(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                score = 2;
                return score;

                // flush
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getSuit() == highestCard.getSuit() && highCard1.getRank() == highestCard.getRank() + 1
                        || highCard1.getRank() == 1 && highestCard.getRank() == 13) {
                    score = 1;
                    return score;
                } else if (highCard2.getSuit() == highestCard.getSuit()
                        && highCard2.getRank() == highestCard.getRank() + 1
                        || highCard2.getRank() == 1 && highestCard.getRank() == 13) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }

            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards1, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (straightToFlush(numSuits, TableSet, tr, ts, userCards2, table)
                    && straightToFlush(numSuits, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
                // straight
            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                }

            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toStraight(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toStraight(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toThreeOfAkind(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {
                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
            } else if (toOnePair(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true) {

                if (highCard1.getRank() > highCard2.getRank()) {
                    score = 1;
                    return score;
                } else if (highCard2.getRank() > highCard1.getRank()) {
                    score = 2;
                    return score;
                } else {
                    score = 3;
                    return score;
                }
            } else if (toOnePair(numOfSameRanks, TableSet, tr, ts, userCards1, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards2, table) != true) {
                score = 1;
                return score;
            } else if (toOnePair(numOfSameRanks, TableSet, tr, ts, userCards2, table) == true
                    && toTwoPairs(numOfSameRanks, TableSet, tr, ts, userCards1, table) != true) {
                score = 2;
                return score;
                // JUST HIGHEST CARD
            } else if (highCard1.getRank() > highCard2.getRank()) {
                score = 1;
                return score;
            } else if (highCard2.getRank() > highCard1.getRank()) {
                score = 2;
                return score;
            } else {
                score = 3;
                return score;
            }
        }

        return 777;
    }

    // METHOD TO CHECK IF A ONE PAIR SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean toOnePair(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean toOneP = false;
        Card temp;
        Card t;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                temp = TableSet[i];
                t = TableSet[j];

                TableSet[i] = playerHand[0];
                TableSet[j] = playerHand[1];
                rAtable[i] = TableSet[i].getRank();
                rAtable[j] = TableSet[j].getRank();
                sAtable[i] = TableSet[i].getSuit();
                sAtable[j] = TableSet[j].getSuit();

                if (twoPairs(rAtable, table) != true && table.pairs(rAtable) == true) {
                    toOneP = true;
                    return toOneP;
                } else {
                    TableSet[i] = temp;
                    TableSet[j] = t;
                    rAtable[i] = temp.getRank();
                    rAtable[j] = t.getRank();
                    sAtable[i] = temp.getSuit();
                    sAtable[j] = t.getSuit();
                }

            }
        }

        return toOneP;
    }

    // METHOD TO CHECK IF A TWO PAIR SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean toTwoPairs(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean toTwoP = false;
        Card temp;
        Card t;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                temp = TableSet[i];
                t = TableSet[j];

                TableSet[i] = playerHand[0];
                TableSet[j] = playerHand[1];
                rAtable[i] = TableSet[i].getRank();
                rAtable[j] = TableSet[j].getRank();
                sAtable[i] = TableSet[i].getSuit();
                sAtable[j] = TableSet[j].getSuit();

                if (twoPairs(rAtable, table) == true) {
                    toTwoP = true;
                    return toTwoP;
                } else {
                    TableSet[i] = temp;
                    TableSet[j] = t;
                    rAtable[i] = temp.getRank();
                    rAtable[j] = t.getRank();
                    sAtable[i] = temp.getSuit();
                    sAtable[j] = t.getSuit();
                }

            }
        }

        return toTwoP;
    }

    // METHOD TO CHECK IF A ROYAL FLUSH SITUATION WILL OCCUR
    public boolean royalFlush(int numOfSuits, Table table, int[] rankArray) {
        boolean royalF = false;
        if (numOfSuits == 5 && table.contains(rankArray, 1) == true && table.contains(rankArray, 10) == true
                && table.contains(rankArray, 11) == true && table.contains(rankArray, 12) == true
                && table.contains(rankArray, 13) == true) {
            royalF = true;
            return royalF;
        }

        return royalF;
    }

    // METHOD TO CHECK IF A STRAIGHT FLUSH SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean straightFlushEightToTwelve(int numOfSuits, Table table, int[] rankArray) {
        boolean straightF = false;
        if (numOfSuits == 5 && table.contains(rankArray, 8) == true
                && table.contains(rankArray, 9) == true && table.contains(rankArray, 10) == true
                && table.contains(rankArray, 11) == true && table.contains(rankArray, 12) == true) {
            straightF = true;
            return straightF;
        }

        return straightF;
    }

    // METHOD TO CHECK IF A STRAIGHT FLUSH 8 -12 SITUATION CAN OCCUR FROM THE TABLE
    // HAND SITUATION
    public boolean straight(int[] rankArray, Table table) {
        boolean straight = false;
        int lowestValue;
        lowestValue = table.lowestRank(rankArray);

        if (table.contains(rankArray, lowestValue + 1) != true && table.contains(rankArray, lowestValue + 2) != true
                && table.contains(rankArray, lowestValue + 3) != true
                && table.contains(rankArray, lowestValue + 4) != true) {
            // System.out.println("None of the ranks are in order, therefore no straight");
            return straight;
        } else if (table.contains(rankArray, lowestValue + 1) == true
                && table.contains(rankArray, lowestValue + 2) == true
                && table.contains(rankArray, lowestValue + 3) == true
                && table.contains(rankArray, lowestValue + 4) == true) {
            // System.out.println("STRAIGHT present on table");
            straight = true;
            return straight;
        }

        return straight;
    }

    // METHOD TO CHECK IF A STRAIGHT FLUSH SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean straightToStraightF(int numOfSuits, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean sToSf = false;

        int lowestR;
        lowestR = table.lowestRank(rAtable);

        if (numOfSuits == 3 && table.contains(rAtable, lowestR + 1) == true
                && table.contains(rAtable, lowestR + 2) == true
                && table.contains(rAtable, lowestR + 3) == true && table.contains(rAtable, lowestR + 4) == true) { // if
                                                                                                                   // there
                                                                                                                   // is
                                                                                                                   // a
                                                                                                                   // straight
                                                                                                                   // with
                                                                                                                   // potential
                                                                                                                   // to
                                                                                                                   // become
                                                                                                                   // a
                                                                                                                   // straigh
                                                                                                                   // flush
            Card temp1;
            Card temp2;

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    temp1 = TableSet[i];
                    temp2 = TableSet[j];

                    TableSet[i] = playerHand[0];
                    TableSet[j] = playerHand[1];
                    rAtable[i] = TableSet[i].getRank();
                    rAtable[j] = TableSet[j].getRank();
                    sAtable[i] = TableSet[i].getSuit();
                    sAtable[j] = TableSet[j].getSuit();

                    numOfSuits = table.numOfMatchingSuits(sAtable);

                    if (numOfSuits == 5 && table.straight(rAtable, table) == true) {
                        sToSf = true;
                        return sToSf;
                    } else {
                        TableSet[i] = temp1;
                        TableSet[j] = temp2;
                        rAtable[i] = temp1.getRank();
                        rAtable[j] = temp2.getRank();
                        sAtable[i] = temp1.getSuit();
                        sAtable[j] = temp2.getSuit();
                    }

                }
            }

        } else if (numOfSuits == 4 && table.contains(rAtable, lowestR + 1) == true
                && table.contains(rAtable, lowestR + 2) == true
                && table.contains(rAtable, lowestR + 3) == true && table.contains(rAtable, lowestR + 4) == true) {
            Card temp;
            Card t;
            for (int j = 0; j < 5; j++) {

                temp = TableSet[j];

                TableSet[j] = playerHand[1];
                rAtable[j] = TableSet[j].getRank();
                sAtable[j] = TableSet[j].getSuit();

                numOfSuits = table.numOfMatchingSuits(sAtable);

                if (numOfSuits == 5 && table.straight(rAtable, table) == true) {
                    sToSf = true;
                    return sToSf;
                } else {
                    TableSet[j] = temp;
                    rAtable[j] = temp.getRank();
                    sAtable[j] = temp.getSuit();
                }

            }
            for (int j = 0; j < 5; j++) {

                t = TableSet[j];

                TableSet[j] = playerHand[1];
                rAtable[j] = TableSet[j].getRank();
                sAtable[j] = TableSet[j].getSuit();

                numOfSuits = table.numOfMatchingSuits(sAtable);

                if (numOfSuits == 5 && table.straight(rAtable, table) == true) {
                    sToSf = true;
                    return sToSf;
                } else {
                    TableSet[j] = t;
                    rAtable[j] = t.getRank();
                    sAtable[j] = t.getSuit();
                }

            }
        }

        return sToSf;
    }

    // METHOD TO CHECK IF A FLUSH SITUATION CAN OCCUR FROM THE TABLE HAND SITUATION
    public boolean straightToFlush(int numOfSuits, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean straightToFlush = false;

        if (numOfSuits == 3) { // if there is a straight with potential to become a straigh flush
            Card temp1;
            Card temp2;

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    temp1 = TableSet[i];
                    temp2 = TableSet[j];

                    TableSet[i] = playerHand[0];
                    TableSet[j] = playerHand[1];
                    rAtable[i] = TableSet[i].getRank();
                    rAtable[j] = TableSet[j].getRank();
                    sAtable[i] = TableSet[i].getSuit();
                    sAtable[j] = TableSet[j].getSuit();

                    numOfSuits = table.numOfMatchingSuits(sAtable);

                    if (numOfSuits == 5) {
                        straightToFlush = true;
                        return straightToFlush;
                    } else {
                        TableSet[i] = temp1;
                        TableSet[j] = temp2;
                        rAtable[i] = temp1.getRank();
                        rAtable[j] = temp2.getRank();
                        sAtable[i] = temp1.getSuit();
                        sAtable[j] = temp2.getSuit();
                    }

                }
            }

        }

        return straightToFlush;
    }

    // METHOD TO CHECK IF A FOUR OF A KIND SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean ToFourOfKind(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean threeToFour = false;
        Card temp;
        Card t;
        if (numOfRanks == 2) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    temp = TableSet[i];
                    t = TableSet[j];

                    TableSet[i] = playerHand[0];
                    TableSet[j] = playerHand[1];
                    rAtable[i] = TableSet[i].getRank();
                    rAtable[j] = TableSet[j].getRank();
                    sAtable[i] = TableSet[i].getSuit();
                    sAtable[j] = TableSet[j].getSuit();

                    numOfRanks = table.numOfMatchingRanks(rAtable);

                    if (numOfRanks == 4) {
                        threeToFour = true;
                        return threeToFour;
                    } else {
                        TableSet[i] = temp;
                        TableSet[j] = t;
                        rAtable[i] = temp.getRank();
                        rAtable[j] = t.getRank();
                        sAtable[i] = temp.getSuit();
                        sAtable[j] = t.getSuit();
                    }

                }
            }
        } else if (numOfRanks == 3) {
            for (int j = 0; j < 5; j++) {
                temp = TableSet[j];
                TableSet[j] = playerHand[0];
                rAtable[j] = TableSet[j].getRank();
                sAtable[j] = TableSet[j].getSuit();
                numOfRanks = table.numOfMatchingRanks(rAtable);
                if (numOfRanks == 4) {
                    threeToFour = true;
                    return threeToFour;
                } else {
                    TableSet[j] = temp;
                    rAtable[j] = temp.getRank();
                    sAtable[j] = temp.getSuit();
                }

            }
            for (int j = 0; j < 5; j++) {
                temp = TableSet[j];
                TableSet[j] = playerHand[1];
                rAtable[j] = TableSet[j].getRank();
                sAtable[j] = TableSet[j].getSuit();
                numOfRanks = table.numOfMatchingRanks(rAtable);
                if (numOfRanks == 4) {
                    threeToFour = true;
                    return threeToFour;
                } else {
                    TableSet[j] = temp;
                    rAtable[j] = temp.getRank();
                    sAtable[j] = temp.getSuit();
                }

            }
        }
        return threeToFour;
    }

    // METHOD TO CHECK IF A FULL HOUSE SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean threeOfToFullH(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean threeOfToFull = false;

        Card temp;

        for (int j = 0; j < 5; j++) {
            temp = TableSet[j];
            TableSet[j] = playerHand[0];
            rAtable[j] = TableSet[j].getRank();
            sAtable[j] = TableSet[j].getSuit();
            numOfRanks = table.numOfMatchingRanks(rAtable);
            if (numOfRanks == 3 && table.pairs(rAtable) == true) {
                threeOfToFull = true;
                return threeOfToFull;
            } else {
                TableSet[j] = temp;
                rAtable[j] = temp.getRank();
                sAtable[j] = temp.getSuit();
            }

        }
        for (int j = 0; j < 5; j++) {
            temp = TableSet[j];
            TableSet[j] = playerHand[1];
            rAtable[j] = TableSet[j].getRank();
            sAtable[j] = TableSet[j].getSuit();
            numOfRanks = table.numOfMatchingRanks(rAtable);
            if (numOfRanks == 3 && table.pairs(rAtable) == true) {
                threeOfToFull = true;
                return threeOfToFull;
            } else {
                TableSet[j] = temp;
                rAtable[j] = temp.getRank();
                sAtable[j] = temp.getSuit();
            }

        }
        return threeOfToFull;
    }

    // METHOD TO CHECK IF A THREE OF A KIND SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean toThreeOfAkind(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean toThree = false;

        Card temp;

        for (int j = 0; j < 5; j++) {
            temp = TableSet[j];
            TableSet[j] = playerHand[0];
            rAtable[j] = TableSet[j].getRank();
            sAtable[j] = TableSet[j].getSuit();
            numOfRanks = table.numOfMatchingRanks(rAtable);
            if (numOfRanks == 3) {
                toThree = true;
                return toThree;
            } else {
                TableSet[j] = temp;
                rAtable[j] = temp.getRank();
                sAtable[j] = temp.getSuit();
            }

        }
        for (int j = 0; j < 5; j++) {
            temp = TableSet[j];
            TableSet[j] = playerHand[1];
            rAtable[j] = TableSet[j].getRank();
            sAtable[j] = TableSet[j].getSuit();
            numOfRanks = table.numOfMatchingRanks(rAtable);
            if (numOfRanks == 3) {
                toThree = true;
                return toThree;
            } else {
                TableSet[j] = temp;
                rAtable[j] = temp.getRank();
                sAtable[j] = temp.getSuit();
            }

        }

        return toThree;
    }

    // METHOD TO CHECK IF A ROYAL FLUSH SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean toRoyalFlush(int numOfSuits, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean RoyalF = false;

        // table.royalFlush(numSuits, table, tr) == true
        Card temp1;
        Card temp2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                temp1 = TableSet[i];
                temp2 = TableSet[j];

                TableSet[i] = playerHand[0];
                TableSet[j] = playerHand[1];
                rAtable[i] = TableSet[i].getRank();
                rAtable[j] = TableSet[j].getRank();
                sAtable[i] = TableSet[i].getSuit();
                sAtable[j] = TableSet[j].getSuit();

                numOfSuits = table.numOfMatchingSuits(sAtable);

                if (table.royalFlush(numOfSuits, table, rAtable) == true) {
                    RoyalF = true;
                    return RoyalF;
                } else {
                    TableSet[i] = temp1;
                    TableSet[j] = temp2;
                    rAtable[i] = temp1.getRank();
                    rAtable[j] = temp2.getRank();
                    sAtable[i] = temp1.getSuit();
                    sAtable[j] = temp2.getSuit();
                }

            }
        }
        return RoyalF;
    }

    // METHOD TO CHECK IF A JUST STRAIGHT SITUATION CAN OCCUR FROM THE TABLE HAND
    // SITUATION
    public boolean toStraight(int numOfRanks, Card[] TableSet, int[] rAtable, int[] sAtable, Card[] playerHand,
            Table table) {
        boolean toS = false;
        Card temp;
        Card t;
        int lowestValue;
        lowestValue = table.lowestRank(rAtable);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                temp = TableSet[i];
                t = TableSet[j];

                TableSet[i] = playerHand[0];
                TableSet[j] = playerHand[1];
                rAtable[i] = TableSet[i].getRank();
                rAtable[j] = TableSet[j].getRank();
                sAtable[i] = TableSet[i].getSuit();
                sAtable[j] = TableSet[j].getSuit();
                lowestValue = table.lowestRank(rAtable);
                if (table.contains(rAtable, lowestValue + 1) == true && table.contains(rAtable, lowestValue + 2) == true
                        && table.contains(rAtable, lowestValue + 3) == true
                        && table.contains(rAtable, lowestValue + 4) == true) {
                    toS = true;
                    return toS;
                } else {
                    TableSet[i] = temp;
                    TableSet[j] = t;
                    rAtable[i] = temp.getRank();
                    rAtable[j] = t.getRank();
                    sAtable[i] = temp.getSuit();
                    sAtable[j] = t.getSuit();
                }

            }
        }

        return toS;
    }

    // METHOD TO CHECK IF THERE ARE ONLY TWO PAIRS ON TABLE HAND
    public boolean twoPairs(int[] rankArray, Table table) {
        boolean twoP = false;
        if (table.containsHowMany(rankArray, rankArray[0]) == 2 && table.containsHowMany(rankArray, rankArray[1]) == 2
                && rankArray[0] != rankArray[1]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[0]) == 2
                && table.containsHowMany(rankArray, rankArray[2]) == 2 && rankArray[0] != rankArray[2]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[0]) == 2
                && table.containsHowMany(rankArray, rankArray[3]) == 2 && rankArray[0] != rankArray[3]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[0]) == 2
                && table.containsHowMany(rankArray, rankArray[4]) == 2 && rankArray[0] != rankArray[4]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[1]) == 2
                && table.containsHowMany(rankArray, rankArray[2]) == 2 && rankArray[1] != rankArray[2]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[1]) == 2
                && table.containsHowMany(rankArray, rankArray[3]) == 2 && rankArray[1] != rankArray[3]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[1]) == 2
                && table.containsHowMany(rankArray, rankArray[4]) == 2 && rankArray[1] != rankArray[4]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[2]) == 2
                && table.containsHowMany(rankArray, rankArray[3]) == 2 && rankArray[2] != rankArray[3]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[2]) == 2
                && table.containsHowMany(rankArray, rankArray[4]) == 2 && rankArray[2] != rankArray[4]) {
            twoP = true;
            return twoP;
        } else if (table.containsHowMany(rankArray, rankArray[3]) == 2
                && table.containsHowMany(rankArray, rankArray[4]) == 2 && rankArray[3] != rankArray[4]) {
            twoP = true;
            return twoP;
        }
        for (int i = 0; i < 5; i++) {
            if (table.containsHowMany(rankArray, rankArray[i]) > 3) {
                twoP = false;
                return twoP;
            }
        }

        return twoP;
    }

    // METHOD TO FIND LOWEST RANK IN GIVEN HAND
    public int lowestRank(int[] hand) {
        int lowest = hand[0];
        for (int i = 1; i < hand.length; i++) {
            if (hand[i] < lowest) {
                lowest = hand[i];
            }
        }

        return lowest;
    }

    // METHOD TO FIND THE HIGHEST RANKED CARD IN A GIVEN HAND
    public Card highestRank(int[] hand, Card[] CardArray) { // MAKE SURE TO RETURN HIGHEST CARD

        int highest = CardArray[0].getRank();
        Card HighCard = CardArray[0];
        for (int i = 1; i < hand.length; i++) {
            if (CardArray[i].getRank() > highest || CardArray[i].getRank() == 1) {
                highest = CardArray[i].getRank();
                HighCard = CardArray[i];
            }
        }
        return HighCard;
    }

    // METHOD TO CHECK WHETHER THE RANK SENT EXISTS WITHIN THE TABLE HAND
    public boolean contains(int[] rankArray, int rankSent) {
        boolean check = false;

        for (int i = 0; i < rankArray.length; i++) {
            if (rankArray[i] == rankSent) {
                // System.out.println("Rank " + rankSent + " does exist in the array");
                check = true;
                return check;
            } else {
                // System.out.println("Error");
            }
        }
        return check;
    }// end of contains method
     // METHOD TO CHECK HOW MANY OF THE RANK SENT EXISTS WITHIN THE TABLE HAND

    public int containsHowMany(int[] rankArray, int rankSent) {
        int howManyTimes = 0;

        for (int i = 0; i < rankArray.length; i++) {
            if (rankArray[i] == rankSent) {
                // System.out.println("Rank " + rankSent + " does exist in the array");
                howManyTimes = howManyTimes + 1;
            } else {
                // System.out.println("Error");
            }
        }

        return howManyTimes;
    }

    // METHOD TO CHECK SUITS THERE ARE IN A GIVEN ARRAY OF SUITS
    public int howManySuits(int[] suitArray, int suitSent) {
        int howMany = 0;

        for (int i = 0; i < suitArray.length; i++) {
            if (suitArray[i] == suitSent) {
                howMany = howMany + 1;
            }
        }

        return howMany;
    }

    // METHOD TO FIND NUM OF SAME SUITS WITHIN GIVEN ARRAY
    public int numOfMatchingSuits(int[] suitArray) {
        int numOf = 0;

        if (suitArray[0] == suitArray[1] && suitArray[1] == suitArray[2] && suitArray[2] == suitArray[3]
                && suitArray[3] == suitArray[4]) {
            // System.out.println("All suits are the same");
            return 5;
        } else if (suitArray[0] == suitArray[1] && suitArray[1] == suitArray[2] && suitArray[2] == suitArray[3]
                && suitArray[3] != suitArray[4]
                || suitArray[0] == suitArray[1] && suitArray[1] == suitArray[2] && suitArray[2] == suitArray[4]
                        && suitArray[4] != suitArray[3]
                || suitArray[4] == suitArray[3] && suitArray[3] == suitArray[2] && suitArray[2] == suitArray[1]
                        && suitArray[1] != suitArray[0]
                || suitArray[4] == suitArray[3] && suitArray[3] == suitArray[1] && suitArray[1] == suitArray[0]
                        && suitArray[0] != suitArray[2]
                || suitArray[0] == suitArray[2] && suitArray[2] == suitArray[3] && suitArray[3] == suitArray[4]
                        && suitArray[4] != suitArray[1]) {
            // System.out.println("Four suits same");
            return 4;
        } else if (suitArray[4] == suitArray[3] && suitArray[3] == suitArray[2] && suitArray[2] != suitArray[0]
                && suitArray[2] != suitArray[1]
                || suitArray[4] == suitArray[3] && suitArray[3] == suitArray[1] && suitArray[1] != suitArray[0]
                        && suitArray[1] != suitArray[2]
                || suitArray[4] == suitArray[2] && suitArray[2] == suitArray[1] && suitArray[1] != suitArray[0]
                        && suitArray[1] != suitArray[3]
                || suitArray[3] == suitArray[2] && suitArray[2] == suitArray[1] && suitArray[1] != suitArray[0]
                        && suitArray[1] != suitArray[4]
                || suitArray[4] == suitArray[3] && suitArray[3] == suitArray[0] && suitArray[0] != suitArray[1]
                        && suitArray[2] != suitArray[2]
                || suitArray[4] == suitArray[2] && suitArray[2] == suitArray[0] && suitArray[0] != suitArray[1]
                        && suitArray[0] != suitArray[3]
                || suitArray[3] == suitArray[2] && suitArray[2] == suitArray[0] && suitArray[0] != suitArray[1]
                        && suitArray[0] != suitArray[4]
                || suitArray[4] == suitArray[1] && suitArray[1] == suitArray[0] && suitArray[0] != suitArray[2]
                        && suitArray[0] != suitArray[3]
                || suitArray[3] == suitArray[1] && suitArray[1] == suitArray[0] && suitArray[0] != suitArray[2]
                        && suitArray[0] != suitArray[4]
                || suitArray[2] == suitArray[1] && suitArray[1] == suitArray[0] && suitArray[0] != suitArray[3]
                        && suitArray[0] != suitArray[4]) {
            // System.out.println("Three suits same");
            return 3;
        }

        return numOf;
    }

    // METHOD TO FIND NUM OF SAME RANKS WITHIN GIVEN ARRAY
    public int numOfMatchingRanks(int[] rankArray) {
        int numOfR = 0;

        if (rankArray[0] == rankArray[1] && rankArray[1] == rankArray[2] && rankArray[2] == rankArray[3]
                && rankArray[3] != rankArray[4]
                || rankArray[0] == rankArray[1] && rankArray[1] == rankArray[2] && rankArray[2] == rankArray[4]
                        && rankArray[4] != rankArray[3]
                || rankArray[4] == rankArray[3] && rankArray[3] == rankArray[2] && rankArray[2] == rankArray[1]
                        && rankArray[1] != rankArray[0]
                || rankArray[4] == rankArray[3] && rankArray[3] == rankArray[1] && rankArray[1] == rankArray[0]
                        && rankArray[0] != rankArray[2]
                || rankArray[0] == rankArray[2] && rankArray[2] == rankArray[3] && rankArray[3] == rankArray[4]
                        && rankArray[4] != rankArray[1]) {
            // System.out.println("4 ranks are the same in table's hand");
            return 4;
        } else if (rankArray[4] == rankArray[3] && rankArray[3] == rankArray[2] && rankArray[2] != rankArray[0]
                && rankArray[2] != rankArray[1]
                || rankArray[4] == rankArray[3] && rankArray[3] == rankArray[1] && rankArray[1] != rankArray[0]
                        && rankArray[1] != rankArray[2]
                || rankArray[4] == rankArray[2] && rankArray[2] == rankArray[1] && rankArray[1] != rankArray[0]
                        && rankArray[1] != rankArray[3]
                || rankArray[3] == rankArray[2] && rankArray[2] == rankArray[1] && rankArray[1] != rankArray[0]
                        && rankArray[1] != rankArray[4]
                || rankArray[4] == rankArray[3] && rankArray[3] == rankArray[0] && rankArray[0] != rankArray[1]
                        && rankArray[2] != rankArray[2]
                || rankArray[4] == rankArray[2] && rankArray[2] == rankArray[0] && rankArray[0] != rankArray[1]
                        && rankArray[0] != rankArray[3]
                || rankArray[3] == rankArray[2] && rankArray[2] == rankArray[0] && rankArray[0] != rankArray[1]
                        && rankArray[0] != rankArray[4]
                || rankArray[4] == rankArray[1] && rankArray[1] == rankArray[0] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[3]
                || rankArray[3] == rankArray[1] && rankArray[1] == rankArray[0] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[4]
                || rankArray[2] == rankArray[1] && rankArray[1] == rankArray[0] && rankArray[0] != rankArray[3]
                        && rankArray[0] != rankArray[4]) {
            return 3;

        } else if (rankArray[4] == rankArray[3] && rankArray[3] != rankArray[0] && rankArray[3] != rankArray[1]
                && rankArray[3] != rankArray[2]
                || rankArray[4] == rankArray[2] && rankArray[2] != rankArray[0] && rankArray[2] != rankArray[1]
                        && rankArray[2] != rankArray[3]
                || rankArray[3] == rankArray[2] && rankArray[2] != rankArray[0] && rankArray[2] != rankArray[1]
                        && rankArray[2] != rankArray[4]
                || rankArray[4] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[2]
                        && rankArray[1] != rankArray[3]
                || rankArray[3] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[2]
                        && rankArray[1] != rankArray[4]
                || rankArray[2] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[3]
                        && rankArray[1] != rankArray[4]
                || rankArray[4] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[3]
                || rankArray[3] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[4]
                || rankArray[2] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[3]
                        && rankArray[0] != rankArray[4]
                || rankArray[1] == rankArray[0] && rankArray[0] != rankArray[2] && rankArray[0] != rankArray[3]
                        && rankArray[0] != rankArray[4]) {
            // System.out.println("Only a pair is present on table (no 3 of a kind)");
            return 2;
        } else {
            // System.out.println("NO MATCHING RANKS ON TABLE");
        }
        return numOfR;
    }

    // METHOD TO DETERMINE IF PAIRS EXIST
    public boolean pairs(int[] rankArray) {
        boolean pairs = false;

        if (rankArray[4] == rankArray[3] && rankArray[3] != rankArray[0] && rankArray[3] != rankArray[1]
                && rankArray[3] != rankArray[2]
                || rankArray[4] == rankArray[2] && rankArray[2] != rankArray[0] && rankArray[2] != rankArray[1]
                        && rankArray[2] != rankArray[3]
                || rankArray[3] == rankArray[2] && rankArray[2] != rankArray[0] && rankArray[2] != rankArray[1]
                        && rankArray[2] != rankArray[4]
                || rankArray[4] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[2]
                        && rankArray[1] != rankArray[3]
                || rankArray[3] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[2]
                        && rankArray[1] != rankArray[4]
                || rankArray[2] == rankArray[1] && rankArray[1] != rankArray[0] && rankArray[1] != rankArray[3]
                        && rankArray[1] != rankArray[4]
                || rankArray[4] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[3]
                || rankArray[3] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[2]
                        && rankArray[0] != rankArray[4]
                || rankArray[2] == rankArray[0] && rankArray[0] != rankArray[1] && rankArray[0] != rankArray[3]
                        && rankArray[0] != rankArray[4]
                || rankArray[1] == rankArray[0] && rankArray[0] != rankArray[2] && rankArray[0] != rankArray[3]
                        && rankArray[0] != rankArray[4]) {
            pairs = true;
            return pairs;
        }

        return pairs;
    }

} // end of table
  // MONEY CLASS RESPONSIBLE FOR ALL THE BETTING, THE PLAYER BALANCES
  // , PLAYER DECISIONS, PROGRAM CONTROL AND READING AND WRITING TO SEPARATE FILE

class money {
    // MEMBERS
    static double player1Balance;
    static boolean player1DidTurn = false;
    static String Player1Name;
    static boolean player1AllIn = false;
    static double player1Bet = 0;
    static boolean player1DidFold = false;
    static boolean player1DidCheck = false;
    static double player2Balance;
    static boolean player2DidTurn = false;
    static String Player2Name;
    static boolean player2AllIn = false;
    static double player2Bet = 0;
    static boolean player2DidFold = false;
    static boolean player2DidCheck = false;
    static double pot = 0;

    // CONSTRUCTOR
    money() {
        Scanner input = new Scanner(System.in);
        String Choice;
        System.out.println("Hello and welcome to Poker by Benjamin (epic programmer) and George (smelly caveman)");
        OUTER: for (;;) {
            System.out.println(
                    "Do you wish to start fresh, continue a previous save, or read a short tutorial? (start/continue/tutorial)");
            Choice = input.nextLine();
            char letterChoice = Poker.getChar(Choice);
            switch (letterChoice) {
                case 'c':
                    System.out.println("Fetching save file...");
                    String FileContents = "";
                    int x;
                    try {
                        // FileInputStream FileIn = new
                        // FileInputStream("C:\\Users\\zalepab211\\Documents\\NetBeansProjects\\Poker\\src\\poker2\\Poker
                        // Balances.txt"); //at school
                        FileInputStream FileIn = new FileInputStream(
                                "G:\\Projects\\Assignments\\Poker\\src\\poker\\Poker Balances.txt"); // at home
                        try {
                            do {
                                x = FileIn.read();
                                if (x != -1) {
                                    FileContents = FileContents + (char) x;
                                }
                            } while (x != -1);
                            FileIn.close();
                            int p1NameEnd = FileContents.indexOf(':');
                            int slashLocation = FileContents.indexOf('/');
                            int p2NameEnd = FileContents.indexOf(':', slashLocation);
                            Player1Name = FileContents.substring(0, p1NameEnd);
                            String P1Balance = FileContents.substring((p1NameEnd + 3), (slashLocation - 1));
                            player1Balance = Double.parseDouble(P1Balance);
                            Player2Name = FileContents.substring((slashLocation + 2), p2NameEnd);
                            String P2Balance = FileContents.substring((p2NameEnd + 3), FileContents.length());
                            player2Balance = Double.parseDouble(P2Balance);
                            System.out.println("Save file fetched :) Poker time!");
                        } catch (IOException | StringIndexOutOfBoundsException ex) {
                            System.out.println(
                                    "Can't read the file with players' balances. It's probably since its empty. I would suggest starting a new save.");
                        }
                    } catch (FileNotFoundException ex) {
                        System.out.println("The file with the players' balances does not exist. Time to panic.");
                    }
                    break OUTER;
                case 's':
                    System.out.println("Okay new save!");
                    for (;;) {
                        System.out.println("What will player one's name be?");
                        Player1Name = input.nextLine();
                        for (;;) {
                            System.out.println("And how much money should " + Player1Name + " start with?");
                            double p1Balance = input.nextDouble();
                            if (p1Balance <= 0) {
                                System.out.println("Sorry but " + Player1Name
                                        + " cannot start with less than zero money or zero money.");
                            } else {
                                player1Balance = p1Balance;
                                break;
                            }
                        }
                        System.out.println("On to player two. What will their name be?");
                        Player2Name = input.nextLine();
                        Player2Name = input.nextLine();
                        for (;;) {
                            System.out.println("And how much money should " + Player2Name + " start with?");
                            double p2Balance = input.nextDouble();
                            if (p2Balance <= 0) {
                                System.out.println("Sorry but " + Player2Name
                                        + " cannot start with less than zero money or zero money.");
                            } else {
                                player2Balance = p2Balance;
                                break;
                            }
                        }
                        CHECK: for (;;) {
                            System.out.println("Alright. So " + Player1Name + " will start with $" + player1Balance
                                    + " and " + Player2Name + " will start with $" + player2Balance
                                    + "? Is that okay? (yes/no)");
                            Choice = input.nextLine();
                            Choice = input.nextLine();
                            letterChoice = Poker.getChar(Choice);
                            switch (letterChoice) {
                                case 'n':
                                    System.out.println("Not good? Understood.");
                                    break CHECK;
                                case 'y':
                                    System.out.println("All good? Understood.");
                                    try {
                                        FileContents = (Player1Name + ": $" + player1Balance + " / " + Player2Name
                                                + ": $" + player2Balance);
                                        // FileOutputStream FileOut = new
                                        // FileOutputStream("C:\\Users\\zalepab211\\Documents\\NetBeansProjects\\Poker\\src\\poker2\\Poker
                                        // Balances.txt"); //at school
                                        FileOutputStream FileOut = new FileOutputStream(
                                                "G:\\Projects\\Assignments\\Poker\\src\\poker\\Poker Balances.txt"); // at
                                                                                                                     // home
                                        try {
                                            for (int i = 0; i < FileContents.length(); i++) {
                                                FileOut.write(FileContents.charAt(i));
                                            }
                                            FileOut.write('\n');
                                        } catch (IOException | StringIndexOutOfBoundsException ex) {
                                            System.out.println(
                                                    "Can't write to the file with players' balances for some reason.");
                                        }
                                    } catch (FileNotFoundException ex) {
                                        System.out.println(
                                                "The file with the players' balances does not exist. Time to panic.");
                                    }
                                    break OUTER;
                                default:
                                    System.out
                                            .println("YES OR NO COWARD ANSWER THE QUESTION I AM NOT PLAYING GAMES!!!");
                            }
                        }
                    }
                case 't':
                    System.out.println(
                            "Benjamin and George, the perfect computer programmers they are, made TEXAS HOLD'EMS.");
                    System.out.println(
                            "This game is played with a traditional deck of 52 playing cards. Cards can be either a TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, or an ACE.");
                    System.out.println("Each card also belongs to a suit. Either SPADES, CLUBS, DIAMONDS, or HEARTS.");
                    System.out.println("");
                    System.out.println(
                            "The object of the game is to create the best possible 'hand' of five cards for yourself made from 2 private 'hold' cards and 5 public 'community' cards (so 7 total cards to use.");
                    System.out.println(
                            "Players are each dealt their two 'hold' cards at the beginning of a hand/round, and, following this, may bet blind (prior to seeing any 'community' cards) or they may 'check'.");
                    System.out.println(
                            "Checking means that you do not bet and still move on to the next part of the round. However, checking requires ALL players to check.");
                    System.out.println(
                            "If one player bets than all other players who wish to stay in on the hand must match that bet or bet a higher amount (which the other players must then match)");
                    System.out.println(
                            "Three 'community' cards will be flipped after the blind betting. This is called the flop. Following it, another round of betting may take place.");
                    System.out.println(
                            "Then another 'community' card will be flipped known as the turn. It is followed by more betting. And finally the fifth 'community' card is flipped called the river.");
                    System.out.println(
                            "It is now that final bets can be made before players reveal their hands and a winner takes all money put in the 'pot' by bets throughout the hand/round.");
                    System.out.println("");
                    System.out.println(
                            "There are many possible hands one can be dealt in Poker. The highest ranking is called a ROYAL FLUSH. It consists of an ACE, KING, QUEEN, JACK, and a TEN all of the same suit.");
                    System.out.println(
                            "The second highest is called a STAIGHT FLUSH. Similarly to the ROYAL FLUSH all cards share the same suit and are in order. The difference is that a STRAIGHT FLUSH does not strecth from TEN to ACE.");
                    System.out.println(
                            "The next highest hand is FOUR OF A KIND. To get this you must have the same card in all four suits.");
                    System.out.println(
                            "The fourth highest hand is known as a FULL HOUSE. It consists of three of the same card in different suits, and two of the same card in different sutis. (ie 3 JACKS and 2 SEVENS)");
                    System.out.println(
                            "Following the FULL HOUSE is the FLUSH. A FLUSH is when you have five cards of matching suits, but that are not in order.");
                    System.out.println(
                            "#6th highest hand is the STRAIGHT. This is when you have five cards in order but in different suits.");
                    System.out.println(
                            "Next is the THREE OF A KIND. This is like 3/5th of one FULL HOUSE. It is when you have three of the same card in different suits.");
                    System.out.println(
                            "Following THREE OF A KIND is TWO OF A KIND which is the missing 2/5th of one FULL HOUSE and is when you have only two of the same card in different suits.");
                    System.out.println(
                            "The lowest possible hand is HIGH CARD. This is when you cannot form any other hand and so your score defaults to that of the highest card out of the seven available to you.");
                    System.out.println(
                            "HIGH CARD is also used to break ties if players end up with the same hand score. (ie TWO OF A KIND with an ACE beats a TWO OF A KIND with a TEN)");
                    System.out.println("Have fun playing this flwaless mastrerpiece!");

            }
        }
    }

    public static void saveFile() {
        try {
            String FileContents = (Player1Name + ": $" + player1Balance + " / " + Player2Name + ": $" + player2Balance);
            // FileOutputStream FileOut = new
            // FileOutputStream("C:\\Users\\zalepab211\\Documents\\NetBeansProjects\\Poker\\src\\poker2\\Poker
            // Balances.txt"); //at school
            FileOutputStream FileOut = new FileOutputStream(
                    "G:\\Projects\\Assignments\\Poker\\src\\poker\\Poker Balances.txt"); // at home
            try {
                for (int i = 0; i < FileContents.length(); i++) {
                    FileOut.write(FileContents.charAt(i));
                }
                FileOut.write('\n');
            } catch (IOException | StringIndexOutOfBoundsException ex) {
                System.out.println("Can't write to the file with players' balances for some reason.");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("The file with the players' balances does not exist. Time to panic.");
        }
    }

    public static int allIn(int whichPlayer) {
        switch (whichPlayer) {
            case 1:
                if (player1Balance > 0) {
                    if ((player1Balance + player1Bet) > (player2Balance + player2Bet)) {
                        System.out.println("You want to go all in, but it looks like you've got more money than "
                                + Player2Name
                                + " could handle. What's going to happen is that you will bet an amount equal to all of "
                                + Player2Name + "'s money, instead of going all in.");
                        bet(1);
                    } else {
                        System.out.println("All in! My wife wishes I had that much blind faith (in our marriage).");
                        pot = pot + player1Balance;
                        player1Bet = player1Bet + player1Balance;
                        player1Balance = 0;
                        System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                        System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                    }
                    player1DidTurn = true;
                    return 1;
                } else if (player1Balance <= 0) {
                    System.out.println("You have no money to go all in with.");
                    return -1;
                }
                break;
            case 2:
                if (player2Balance > 0) {
                    if ((player2Balance + player2Bet) > (player1Balance + player1Bet)) {
                        System.out.println("You want to go all in, but it looks like you've got more money than "
                                + Player1Name
                                + " could handle. What's going to happen is that you will bet an amount equal to all of "
                                + Player1Name + "'s money, instead of going all in.");
                        bet(2);
                    } else {
                        System.out.println("All in! My wife wishes I had that much blind faith (in our marriage).");
                        pot = pot + player2Balance;
                        player2Bet = player2Bet + player2Balance;
                        player2Balance = 0;
                        System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                        System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                    }
                    player2DidTurn = true;
                    return 1;
                } else if (player2Balance <= 0) {
                    System.out.println("You have no money to go all in with.");
                    return -1;
                }
            default:
                System.out.println("This should not have ran");
                break;
        }

        return -1;
    }

    public static int bet(int whichPlayer, String BetAsString) {
        double newBet;
        switch (whichPlayer) {
            case 1:
                try {
                    newBet = Double.parseDouble(BetAsString);
                    if (newBet > player1Balance) {
                        System.out.println("Don't try to bet money you dont have you doorknob.");
                        break;
                    }
                    if (newBet > player2Balance) {
                        System.out.println("Looks like you want to bet $" + newBet + " however " + Player2Name
                                + " cannot afford that. As such you will bet an amount equal to all of " + Player2Name
                                + "'s money,");
                        bet(1);
                        break;
                    }
                    for (;;) {
                        if (player1Balance - newBet >= 0) {
                            if (newBet > 0) {
                                player1Balance = player1Balance - newBet;
                                player1Bet = player1Bet + newBet;
                                System.out.println(
                                        "player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                                System.out.println(
                                        "player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                                pot = pot + newBet;
                                System.out.println("Okay. " + Player1Name + " bet $" + player1Bet + ".");
                                player1DidTurn = true;
                                return 1;
                            } else if (newBet <= 0) {
                                System.out.println("Bet cannot be less than or equal to zero.");
                                break;
                            }
                        } else if (player1Balance - newBet < 0) {
                            System.out
                                    .println("Seems you don't have the funds. Try a number less than your balance of $"
                                            + player1Balance);
                            break;
                        }
                    }
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("Not cool bro. Enter a number this time.");
                }
                break;
            case 2:
                try {
                    newBet = Double.parseDouble(BetAsString);
                    if (newBet > player2Balance) {
                        System.out.println("Don't try to bet money you dont have you doorknob.");
                        break;
                    }
                    if (newBet > player1Balance) {
                        System.out.println("Looks like you want to bet $" + newBet + " however " + Player1Name
                                + " cannot afford that. As such you will bet an amount equal to all of " + Player1Name
                                + "'s money,");
                        bet(2);
                        break;
                    }
                    for (;;) {
                        if (player2Balance - newBet >= 0) {
                            if (player2Bet > 0) {
                                player2Balance = player2Balance - newBet;
                                player2Bet = player2Bet + newBet;
                                System.out.println(
                                        "player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                                System.out.println(
                                        "player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                                pot = pot + newBet;
                                System.out.println("Okay. " + Player2Name + " bet $" + player2Bet + ".");
                                player2DidTurn = true;
                                return 1;
                            } else if (newBet <= 0) {
                                System.out.println("Bet cannot be less than or equal to zero.");
                                break;
                            }

                        } else if (player2Balance - newBet < 0) {
                            System.out
                                    .println("Seems you don't have the funds. Try a number less than your balance of $"
                                            + player2Balance);
                            break;
                        }
                    }
                } catch (java.lang.NumberFormatException e) {
                    System.out.println("Not cool bro. Enter a number this time.");
                }
                break;
            default:
                System.out.println("This should not have ran");
                break;
        }
        return -1;
    }

    public static void bet(int whichPlayer) {
        switch (whichPlayer) {
            case 1:
                pot = pot + player2Balance;
                player1Balance = player1Balance - player2Balance;
                player1Bet = player1Bet + player2Balance;
                System.out.println("player 1 bet: " + player1Bet + " and their balance: " + player1Balance);
                System.out.println("player 2 bet: " + player2Bet + " and their balance: " + player2Balance);
                break;
            case 2:
                pot = pot + player1Balance;
                player2Balance = player2Balance - player1Balance;
                player2Bet = player2Bet + player1Balance;
                System.out.println("player 1 bet: " + player1Bet + " and their balance: " + player1Balance);
                System.out.println("player 2 bet: " + player2Bet + " and their balance: " + player2Balance);
                break;
        }
    }

    public static int raise(int whichPlayer, boolean askUser) {
        Scanner input = new Scanner(System.in);
        String RaiseAsString;
        String Choice;
        char letterChoice;
        boolean allIn;
        int keepGoing;
        if (askUser == true) {
            switch (whichPlayer) {
                case 1:
                    System.out.println("Yo " + Player1Name + ", sounds to me like " + Player2Name
                            + " raised your bet. Do you want to raise it back? (the chad move) Or simply call or fold?");
                    Choice = input.nextLine();
                    letterChoice = Poker.getChar(Choice);
                    switch (letterChoice) {
                        case 'r':
                            int placeholder = raise(1, false);
                            break;
                        case 'c':
                            keepGoing = call(1);
                            if (keepGoing == 1) {
                                player1DidTurn = true;
                                break;
                            }
                        case 'f':
                            player1DidFold = true;
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Yo " + Player2Name + ", sounds to me like " + Player1Name
                            + " raised your bet. Do you want to raise it back? (the chad move) Or simply call or fold?");
                    Choice = input.nextLine();
                    letterChoice = Poker.getChar(Choice);
                    switch (letterChoice) {
                        case 'r':
                            int placeholder = raise(2, false);
                            break;
                        case 'c':
                            keepGoing = call(2);
                            if (keepGoing == 1) {
                                player2DidTurn = true;
                                break;
                            }
                        case 'f':
                            player2DidFold = true;
                            break;
                    }
            }
            return 1;
        }
        switch (whichPlayer) {
            case 1:
                System.out.println("Okay, " + Player1Name
                        + ". How much do you want to raise the bet by? Or do you want to go all in?");
                RaiseAsString = input.nextLine();
                allIn = RaiseAsString.equalsIgnoreCase("all in");
                if (allIn == true) {
                    for (;;) {
                        keepGoing = allIn(1);
                        if (keepGoing == 1) {
                            break;
                        }
                    }
                    if (player1Bet == player2Bet) {
                        return 1;
                    }
                    int placeholder = raise(2, true);
                    return 1;
                } else {
                    double raise = Double.parseDouble(RaiseAsString);
                    if (player1Balance - (Math.abs(player1Bet) + raise) >= 0) {
                        pot = pot + (getDifference() + raise);
                        player1Balance = player1Balance - (getDifference() + raise);
                        System.out.println("Got it. " + Player1Name + " has raised " + Player2Name + "'s bet of $"
                                + player2Bet + " by $" + raise + ".");
                        player1DidTurn = true;
                        player1Bet = player1Bet + (getDifference() + raise);
                        System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                        System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                        pot = pot + (player2Bet + raise);
                        if (player1Bet == player2Bet) {
                            return 1;
                        }
                        int placeholder = raise(2, true);
                        return 1;
                    } else if (player1Balance - (player2Bet + raise) < 0) {
                        System.out.println(
                                "Seems you don't have the funds to raise by that much. Make sure you are not includng the bet amount in your raise amount and ensure than your balance of $"
                                        + player1Balance + " will cover it.");
                        return -1;
                    }

                }
            case 2:
                System.out.println("Okay, " + Player2Name
                        + ". How much do you want to raise the bet by? Or do you want to go all in?");
                RaiseAsString = input.nextLine();
                allIn = RaiseAsString.equalsIgnoreCase("all in");
                if (allIn == true) {
                    for (;;) {
                        keepGoing = allIn(1);
                        if (keepGoing == 1) {
                            break;
                        }
                    }
                    if (player1Bet == player2Bet) {
                        return 1;
                    }
                    int placeholder = raise(1, true);
                    return 1;
                } else {
                    double raise = Double.parseDouble(RaiseAsString);
                    if (player2Balance - (getDifference() + raise) >= 0) {
                        pot = pot + (getDifference() + raise);
                        player2Balance = player2Balance - (getDifference() + raise);
                        System.out.println("Got it. " + Player2Name + " has raised " + Player1Name + "'s bet of $"
                                + player1Bet + " by $" + raise + ".");
                        player2DidTurn = true;
                        System.out.println(getDifference());
                        player2Bet = player2Bet + (getDifference() + raise);
                        System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                        System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                        pot = pot + (player1Bet + raise);
                        if (player1Bet == player2Bet) {
                            return 1;
                        }
                        int placeholder = raise(1, true);
                        return 1;
                    } else if (player2Balance - (player1Bet + raise) < 0) {
                        System.out.println(
                                "Seems you don't have the funds to raise by that much. Make sure you are not includng the bet amount in your raise amount and ensure than your balance of $"
                                        + player2Balance + " will cover it.");
                        return -1;
                    }

                }
        }
        return -1;
    }

    public static int call(int whichPlayer) {
        Scanner input = new Scanner(System.in);
        String Choice;
        int keepGoing;
        switch (whichPlayer) {
            case 1:
                if ((player1Balance + player1Bet) < player2Bet) {
                    System.out.println(
                            "You do not have enough money to call the bet if you wanted to. However, you can go all in to cover yourself even if you are lacking in funds. Do you want to do so? yes/no (No will result in you folding)");
                    Choice = input.nextLine();
                    char letterChoice = Poker.getChar(Choice);
                    switch (letterChoice) {
                        case 'y':
                            keepGoing = allIn(1);
                            if (keepGoing == 1) {
                                player1DidTurn = true;
                                break;
                            }
                            System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                            System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                            return 1;
                        case 'n':
                            System.out.println("Coward.");
                            player1DidFold = true;
                            return (1);
                    }
                } else {
                    keepGoing = allIn(2);
                    if (keepGoing == 1) {
                        player1DidTurn = true;
                        break;
                    }
                    System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                    System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                    System.out.println(
                            "Got it. " + Player1Name + " has called " + Player2Name + "'s bet of $" + player2Bet + ".");
                    return 1;
                }
                break;
            case 2:
                if ((player2Balance + player2Bet) < player1Bet) {
                    System.out.println(
                            "You do not have enough money to call the bet if you wanted to. However, you can go all in to cover yourself even if you are lacking in funds. Do you want to do so? yes/no (No will result in you folding)");
                    Choice = input.nextLine();
                    char letterChoice = Poker.getChar(Choice);
                    switch (letterChoice) {
                        case 'y':
                            pot = pot + player2Balance;
                            player2Balance = 0;
                            player2AllIn = true;
                            System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                            System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                            return 1;
                        case 'n':
                            System.out.println("Coward.");
                            player2DidFold = true;
                            return (1);
                    }
                } else {
                    pot = pot + getDifference();
                    player2Balance = player2Balance - getDifference();
                    player2Bet = player2Bet + getDifference();
                    System.out.println("player 1 bet: " + player1Bet + " player 1 balance: " + player1Balance);
                    System.out.println("player 2 bet: " + player2Bet + " player 2 balance: " + player2Balance);
                    System.out.println(
                            "Got it. " + Player2Name + " has called " + Player1Name + "'s bet of $" + player1Bet + ".");
                    return 1;
                }
                break;
        }
        return -1;
    }

    public static int play() {
        Scanner input = new Scanner(System.in);
        String Choice;
        int keepGoing;
        int placeholder;
        OUTER_1: for (;;) {
            if (player1DidTurn == false & player1Balance != 0) {
                System.out.println(Player1Name + " do you want to bet? yes/no");
                Choice = input.nextLine();
                char letterChoice = Poker.getChar(Choice);
                switch (letterChoice) {
                    case 'y':
                        for (;;) {
                            System.out.println("How much money do you want to bet, " + Player1Name
                                    + ", or do you want to go all in? (Current balance: $" + player1Balance + ")");
                            String BetAsString = input.nextLine();
                            boolean allIn = BetAsString.equalsIgnoreCase("all in");
                            if (allIn == true) {
                                placeholder = allIn(1);
                                break;
                            } else {
                                placeholder = bet(1, BetAsString);
                                player1DidTurn = true;
                                break;
                            }
                        }
                        break;
                    case 'n':
                        System.out.println("No bet, got it. Coward");
                        player1DidTurn = true;
                        player1DidCheck = true;
                        break OUTER_1;
                    default:
                        System.out.println(
                                "Please type 'yes' or anything beginning with a 'y', or 'no' or anything beginning with an 'n'.");
                        break;
                }
                return 1;
            } else if (player1DidTurn == true & player2DidTurn == false & player1DidCheck == true
                    & player2Balance != 0) {
                System.out.println("I can't help but notice that " + Player1Name
                        + " checked and as such is a yellow-bellied scardy-cat. " + Player2Name
                        + " do you want to bet? yes/no (Current balance: $" + player2Balance);
                Choice = input.nextLine();
                char letterChoice = Poker.getChar(Choice);
                switch (letterChoice) {
                    case 'y':
                        for (;;) {
                            System.out.println("How much money do you want to bet, " + Player2Name
                                    + ", or do you want to go all in?");
                            String BetAsString = input.nextLine();
                            boolean allIn = BetAsString.equalsIgnoreCase("all in");
                            if (allIn == true) {
                                keepGoing = allIn(2);
                                if (keepGoing == 1) {
                                    player2DidTurn = true;
                                    break;
                                }
                            } else {
                                keepGoing = bet(2, BetAsString);
                                if (keepGoing == 1) {
                                    player2DidTurn = true;
                                    break;
                                }
                            }
                        }

                    case 'n':
                        System.out.println("No bet, got it. Coward");
                        player2DidTurn = true;
                        break OUTER_1;
                    default:
                        System.out.println(
                                "Please type 'yes' or anything beginning with a 'y', or 'no' or anything beginning with an 'n'.");
                        break;
                }
                return 1;
            } else if (player1DidTurn == true & player2DidTurn == false & player1DidCheck == false
                    & player2Balance != 0) {
                System.out.println("Okay " + Player2Name + ", since " + Player1Name + " has bet $" + player1Bet
                        + " total and you have only bet $" + player2Bet
                        + " total, you are going to have to call that or raise it to stay in the game. Alternatively you can fold. However if you fold I will judge you.");
                for (;;) {
                    if (player2Balance < player1Bet) {
                        keepGoing = call(2);
                        if (keepGoing == 1) {
                            player2DidTurn = true;
                            break;
                        }
                    } else {
                        System.out.println(
                                "Do you want to call, raise, or fold? (Current balance: $" + player2Balance + ")");
                        Choice = input.nextLine();
                        char letterChoice = Poker.getChar(Choice);
                        switch (letterChoice) {
                            case 'c':
                                keepGoing = call(2);
                                if (keepGoing == 1) {
                                    player2DidTurn = true;
                                    break;
                                }
                            case 'r':
                                for (;;) {
                                    keepGoing = raise(2, false);
                                    if (keepGoing == 1) {
                                        player2DidTurn = true;
                                        break;
                                    }
                                }
                                break;
                            case 'f':
                                System.out.println("Coward.");
                                player2DidFold = true;
                                return (-1);
                        }
                        break;
                    }
                }
            } else if (player1Balance == 0) {
                System.out.println("Darn " + Player1Name + " you are broke. Oh well, river time!");
                return -1;
            } else if (player2Balance == 0) {
                System.out.println("Darn " + Player2Name + " you are broke. Oh well, river time!");
                return -1;
            } else if (player1Balance == 0 & player2Balance == 0) {
                System.out.println("SHEESH both parties empty on cash. No worries we'll get right to the river");
                return -1;
            } else {
                return 1;
            }
        }

        return 1;
    }

    public static int p2Raised() {
        Scanner input = new Scanner(System.in);
        String Choice;
        int keepGoing;
        System.out.println("Okay " + Player1Name + ", " + Player2Name
                + " decided to raise you so now you have three options. Do you want to call, raise or fold?\n(btw they have put a total of $"
                + player2Bet + " into the pot and you have put a total of $" + player1Bet + " into the pot)");
        Choice = input.nextLine();
        char letterChoice = Poker.getChar(Choice);
        switch (letterChoice) {
            case 'c':
                if (player1AllIn == true) {
                    System.out.println("Actually, looks like you're covered since you went all in this hand.");
                    return 1;
                } else if (player1Balance < player2Bet) {
                    for (;;) {
                        System.out.println(
                                "However it seems like you don't have enough cash to cover what you need. Your only option is to either all in or fold. So what will it be?");
                        String HowDoYouCall = input.nextLine();
                        char letterHowDoYouCall = Poker.getChar(HowDoYouCall);
                        switch (letterHowDoYouCall) {
                            case 'a':
                                keepGoing = allIn(1);
                                if (keepGoing == 1) {
                                    break;
                                }
                            case 'f':
                                System.out.println("Geez! Loser.");
                                return (-1);
                        }
                    }

                } else if ((player1Balance + player1Bet) > player2Bet) {
                    keepGoing = call(1);
                    if (keepGoing == 1) {
                        break;
                    }
                }
            case 'f':
                System.out.println(Player1Name + " has folded.");
                return (-1);
            case 'r':
                System.out.println("Ohoho! A battle of raises, I see.");
                keepGoing = raise(2, true);
                if (keepGoing == 1) {
                    break;
                }
        }
        return 1;
    }

    public static int p1Raised() {
        Scanner input = new Scanner(System.in);
        String Choice;
        int keepGoing;
        System.out.println("Okay " + Player2Name + ", " + Player1Name
                + " decided to raise you so now you have three options. Do you want to call, raise or fold?\n(btw they have put a total of $"
                + player1Bet + " into the pot and you have put a total of $" + player2Bet + " into the pot)");
        Choice = input.nextLine();
        char letterChoice = Poker.getChar(Choice);
        switch (letterChoice) {
            case 'c':
                if (player2AllIn == true) {
                    System.out.println("Actually, looks like you're covered since you went all in this hand.");
                    return 1;
                } else if (player2Balance < player1Bet) {
                    for (;;) {
                        System.out.println(
                                "However it seems like you don't have enough cash to cover what you need. Your only option is to either all in or fold. So what will it be?");
                        String HowDoYouCall = input.nextLine();
                        char letterHowDoYouCall = Poker.getChar(HowDoYouCall);
                        switch (letterHowDoYouCall) {
                            case 'a':
                                keepGoing = allIn(1);
                                if (keepGoing == 1) {
                                    break;
                                }
                            case 'f':
                                System.out.println("Geez! Loser.");
                                return (-1);
                        }
                    }

                } else if ((player2Balance + player2Bet) > player1Bet) {
                    keepGoing = call(1);
                    if (keepGoing == 1) {
                        break;
                    }
                }
            case 'f':
                System.out.println(Player2Name + " has folded.");
                return (-1);
            case 'r':
                System.out.println("Ohoho! A battle of raises, I see.");
                keepGoing = raise(1, true);
                if (keepGoing == 1) {
                    break;
                }
        }
        return 1;
    }

    public static void clear() {
        money.player1DidTurn = false;
        money.player2DidTurn = false;
    }

    public static void winnings(int whichPlayerWon) {
        switch (whichPlayerWon) {
            case 1:
                System.out.println("Congrats " + Player1Name + " on beating " + Player2Name + "!");
                player1Balance = player1Balance + pot;
                break;
            case 2:
                System.out.println("Congrats " + Player2Name + " on beating " + Player1Name + "!");
                player2Balance = player2Balance + pot;
                break;
            case 3:
                System.out.println("Arcite! Fetch me that rulebook. That... Shouldn't have happened. "
                        + "IT WAS A TIE! you're both equally bad at the game (or good, depending on how you see things)");
                player1Balance = player1Balance + (pot / 2);
                player2Balance = player2Balance + (pot / 2);
                break;

        }
        pot = 0;
    }

    public static void readyNextTurn() {
        player1DidTurn = false;
        player2DidTurn = false;
        player1DidCheck = false;
        player2DidCheck = false;
    }

    public static double getDifference() {
        double difference = 0;
        if (player1Bet == player2Bet) {
            difference = 0;
        } else if (player1Bet < player2Bet) {
            difference = player2Bet - player1Bet;
        } else if (player1Bet > player2Bet) {
            difference = player1Bet - player2Bet;
        }
        return difference;
    }

    public static void wonByFold() {
        if (player1DidFold == true) {
            player2Balance = player2Balance + pot;
        } else if (player2DidFold == true) {
            player1Balance = player1Balance + pot;
        }
    }

}

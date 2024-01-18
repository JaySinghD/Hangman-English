/*
Programmer: Jay Dadhiala
Date: 2022-06-15
Program Description: This program recreates the game Hangman in Java
The game consists of one player trying to guess a word chosen by the computer
 */

//Imports for use in the program.
import javax.swing.*; //importing the swing package
import java.awt.*; //Importing the AWT package;

public class gameoverScreen {
    /*
    loseScreen
    This method generates the a spash screen when the user loses the game.
    Parameter: String randWord, int score
    Returns: void
   */    
    public static void loseScreen (String randWord, int score) { 
        //declaring and initializing an imageIcon variable to be outputted on the gameover screen. 
        ImageIcon imgGameover = new ImageIcon("src\\images\\gameover.png"); 
         
        //creating JLabels to be displayed to the user
        JLabel lblGameover= new JLabel (imgGameover); //creating a JLabel and storing within it the image imgGameover.
        //this label gives a message to the user.
        //The string is surrounded with "<html></html>" in order to use "<br/>" to break the lines.
        JLabel lblYouLose = new JLabel ("<html>The word was \"" + randWord + "\"<br/>"
                + "Your score was: " + score +"<html>");
        lblYouLose.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); //Setting the font of the information.
        lblYouLose.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); //Aligning the text to be centred
         
        //Creating and organizing a JPanel 
        JPanel pnlPopUp = new JPanel(); //creating a JPanel
        pnlPopUp.setLayout (new BorderLayout ()); //setting the layout of the JPanel to be "BorderLayout"
        pnlPopUp.add(lblGameover, BorderLayout.CENTER); //adding an image to the JPanel and centring it 
        //adds some text to the JPanel and puts it at the bottom of the panel.
        pnlPopUp.add(lblYouLose, BorderLayout.PAGE_END);
        
        //Creates a JOptionPane that displays the JPanel "pnlPopUp", adds header text, and sets the message in the plane style.
        JOptionPane.showMessageDialog(null, pnlPopUp, "", JOptionPane.PLAIN_MESSAGE);
    }
}

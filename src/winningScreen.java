/*
Programmer: Jay Dadhiala
Date: 2022-06-15
Program Description: This program recreates the game Hangman in Java
The game consists of one player trying to guess a word chosen by the computer
 */

//Imports for use in the program.
import javax.swing.*; //importing the swing package
import java.awt.*; //Importing the AWT package

public class winningScreen {
    /*
    winScreen
    This method generates the a spash screen when the user wins a round.
    Parameter: String randWord
    Returns: void
   */    
    public static void winScreen (String randWord) { 
        //declaring and initializing an imageIcon variable to be outputted on the win screen. 
        ImageIcon imgHappyMan= new ImageIcon("src\\images\\happyStickMan.jpg"); 
         
        //creating JLabels to be displayed to the user
        JLabel lblHappyMan= new JLabel (imgHappyMan); //creating a JLabel and storing within it the image imgHappyMan.
        //this label gives a congratulatory message to the user.
        JLabel lblCongrats = new JLabel ("You've guessed the word \"" + randWord + "\"");
        lblCongrats.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); //Setting the font of the information.
        lblCongrats.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); //Aligning the text to be centred
         
        //Creating and organizing a JPanel 
        JPanel pnlPopUp = new JPanel(); //creating a JPanel
        pnlPopUp.setLayout (new BorderLayout ()); //setting the layout of the JPanel to be "BorderLayout"
        pnlPopUp.add(lblHappyMan, BorderLayout.CENTER); //adding an image to the JPanel and centring it 
        //adds some text to the JPanel and puts it at the bottom of the panel.
        pnlPopUp.add(lblCongrats, BorderLayout.PAGE_END);
        
        //Creates a JOptionPane that displays the JPanel "pnlPopUp", adds header text, and sets the message in the plane style.
        JOptionPane.showMessageDialog(null, pnlPopUp, "You Won!", JOptionPane.PLAIN_MESSAGE);
    }   
}

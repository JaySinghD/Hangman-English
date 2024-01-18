/*
Programmer: Jay Dadhiala
Date: 2022-06-15
Program Description: This program recreates the game Hangman in Java
The game consists of one player trying to guess a word chosen by the computer
 */

//Imports for use in the program.
import javax.swing.*; //importing the swing package
import java.awt.*; //Importing the AWT package

public class splashScreen {
    
    /*
    createIntro
    This method generates the program's introduction splash screen.
    Parameter: void
    Returns: void
   */    
    public static void createIntro () {
        //declaring and initializing an imageIcon variable to be outputted on the introduction screen. 
        ImageIcon imgHangman= new ImageIcon("src\\images\\hangman.jpg"); 
         
        //creating JLabels to be displayed to the user later in the program
        JLabel lblImage = new JLabel (imgHangman); //creating a JLabel and storing within it the image imgHangman.
        //this label gives info to the user.
        //The string is surrounded with "<html></html>" in order to use "<br/>" to break the lines.
        JLabel lblInfo = new JLabel ("<html>Welcome to Virtual Hangman.<br/>"
                + "The goal of this game is to guess the hidden word, without hanging the man.<br/>"
                + "You must guess what letters are in the word in order to figure out what it is.<br/>"
                + "For a more in depth explanation, please check the User Guide.</html>");
        lblInfo.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); //Setting the font of the information.
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); //Aligning the information to be centred
         
        //Creating and organizing a JPanel 
        JPanel pnlPopUp = new JPanel (); //creating a JPanel
        pnlPopUp.setLayout (new BorderLayout ()); //setting the layout of the JPanel to be "BorderLayout"
        pnlPopUp.add(lblImage, BorderLayout.CENTER); //adding an image to the JPanel and centring it 
        //adds some information to the JPanel and puts it at the bottom of the panel.
        pnlPopUp.add(lblInfo, BorderLayout.PAGE_END); 
        
        //using the UIManager to change the background colour of OptionPanes to white.  
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.white);
        //changing the colour of the background of the JPanel to white.
        pnlPopUp.setBackground(Color.WHITE);


        //Creates a JOptionPane that displays the JPanel "pnlPopUp", adds header text, and sets the message in the plane style.
        JOptionPane.showMessageDialog(null, pnlPopUp, "Created by: The Jay Dadhiala Coding Company", JOptionPane.PLAIN_MESSAGE);
    }
    
    /*
    usernamePrompt
    This method generates a prompt for the user to enter their username, and sends it to the main program.
    Parameter: void
    Returns: String username
   */
    public static String usernamePrompt () {
        //creates a JOptionPane that has an input box that the user is asked to input their username into. 
        //The username is stored in a String.
        String username = JOptionPane.showInputDialog("Please enter your username");
        
        //if statement to ensure when the user does not enter anything into the username box, their name is set to "null"
        if ("".equals(username)) {
            username = "null";
        }
        
        //returns the username to the main program.
        return (username);
    }
}

/*
Programmer: Jay Dadhiala
Date: 2022-06-15
Program Description: This program recreates the game Hangman in Java
The game consists of one player trying to guess a word chosen by the computer
 */

//Imports for use in the program.
import javax.swing.*; //importing the swing package
import java.awt.*; //Importing the AWT package;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class leaderboard {
    
    /*
    leaderboardXML
    The method also modifies the XML in order to add new scores to the leaderboard.
    Parameter: String Array names, Integer Array scores
    Returns: void
   */    
    public static void leaderboardXML (String [] names, int [] scores) {
        try { //try and catch statement to handle any error checking in the file.
            
            //NOTE: the commented out code is how I created an XML file for the program.
            //It generates an XML file that stores the data of the game's leaderboard, even after the program is closed.
            //Since the XML file only needed to be created once, the commented out code is no longer needed, and is here for viewing my process only.
            
            /*
            //the following code creates the new file to be written to, and then defines it to be XML with the ISO 8859_1 encoding style.
            OutputStream fout = new FileOutputStream("leaderboard.xml");
            OutputStream bout = new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
            
            //the following two lines define the XML coding.
            out.write("<?xml version=\"1.0\" ");
            out.write("encoding=\"ISO-8859-1\"?>\r\n");
            
            //opens the element "info"
            out.write("<info>\r\n");
            
            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Rick</name>\r\n");
            out.write("<score>10</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".
            
            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Cole</name>\r\n");
            out.write("<score>20</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Emma</name>\r\n");
            out.write("<score>30</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Brad</name>\r\n");
            out.write("<score>40</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Tommy</name>\r\n");
            out.write("<score>50</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Fiona</name>\r\n");
            out.write("<score>60</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Simon</name>\r\n");
            out.write("<score>70</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Jacob</name>\r\n");
            out.write("<score>80</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Derek</name>\r\n");
            out.write("<score>90</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".

            out.write("<player>\r\n"); //opens the root element "player".
           //opens and close the child element of "player". The following elements are siblings.
            out.write("<name>Jay</name>\r\n");
            out.write("<score>100</score>\r\n");
            out.write("</player>\r\n"); //closes the root element "player".            
            
            //closes the element "info"
            out.write("</info>\r\n");
            
            out.flush();  //flushes things from the buffer to the output stream.
            out.close(); //closes the stream
            */
            
           //allows for the parsing and reading/writing of the xml file.
            String filepath = "leaderboard.xml"; //setting the filepath as a String for clairity 
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);
            
            //Using a for loop to add all the new leaderboard data to the xml file
            for (int i = 0; i < 10; i++) {
                //changes the usernames to be updated.
                Node name = doc.getElementsByTagName("name").item(i);
                name.setTextContent("" + names[i]); //sets the text content as names[i].
                //changes the scores to be updated.
                Node score = doc.getElementsByTagName("score").item(i);
                score.setTextContent("" + scores[i]); //sets the text content as scores[i].                
            }
            
            //writes the modifications into the xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result =  new StreamResult(new File(filepath));
            transformer.transform(source, result);
        }
        //catch statements to deal with any errors.
        catch (UnsupportedEncodingException e) {
            System.out.println("This VM does not support the Latin-1 character set.");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch(TransformerException tfe){
            tfe.printStackTrace();
        }            
        catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        catch (SAXException sae) {
            sae.printStackTrace();
        }
    }   
    
    /*
    showLeaderboard
    This method generates a leaderboard on a spash screen that shows the top scores on the leaderboard.
    Parameter: String [] leaderNames, int [] score
    Returns: void
   */    
    public static void showLeaderboard (String [] leaderNames, int [] score) {
        //creating JLabels to be displayed to the user
        //this label acts as a title for the Leaderboard
        JLabel lblLeadBoardTitle = new JLabel ("LEADERBOARD");
        lblLeadBoardTitle.setFont(new java.awt.Font("Yu Gothic UI", 1, 30)); //Setting the font of the information.
        lblLeadBoardTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); //Aligning the text to be centred
        
        //this label shows the contents of the leaderboard
        JLabel lblLeadBoardContents = new JLabel ("<html>#1 " + leaderNames[9] + " | Score: " + score[9] + "<br/>"
                + "#2 " + leaderNames[8] + " | Score: " + score[8] + "<br/>"
                + "#3 " + leaderNames[7] + " | Score: " + score[7] + "<br/>"     
                + "#4 " + leaderNames[6] + " | Score: " + score[6] + "<br/>"       
                + "#5 " + leaderNames[5] + " | Score: " + score[5] + "<br/>"       
                + "#6 " + leaderNames[4] + " | Score: " + score[4] + "<br/>"       
                + "#7 " + leaderNames[3] + " | Score: " + score[3] + "<br/>"       
                + "#8 " + leaderNames[2] + " | Score: " + score[2] + "<br/>"       
                + "#9 " + leaderNames[1] + " | Score: " + score[1] + "<br/>"       
                + "#10 " + leaderNames[0] + " | Score: " + score[0] + "<br/>"                               
                + "<html>");
        lblLeadBoardContents.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); //Setting the font of the information.
        lblLeadBoardContents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); //Aligning the text to be centred        
         
        //Creating and organizing a JPanel 
        JPanel pnlPopUp = new JPanel(); //creating a JPanel
        pnlPopUp.setLayout (new BorderLayout ()); //setting the layout of the JPanel to be "BorderLayout"
        pnlPopUp.add(lblLeadBoardTitle, BorderLayout.NORTH); //adding text to the JPanel and putting it at the top.
        //adds some text to the JPanel and puts it at the bottom of the panel.
        pnlPopUp.add(lblLeadBoardContents, BorderLayout.PAGE_END);
        
        //Creates a JOptionPane that displays the JPanel "pnlPopUp", adds header text, and sets the message in the plane style.
        JOptionPane.showMessageDialog(null, pnlPopUp, "", JOptionPane.PLAIN_MESSAGE);        
    }   
}



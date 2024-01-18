/*
Programmer: Jay Dadhiala
Date: 2022-06-15
Program Description: This program recreates the game Hangman in Java
The game consists of one player trying to guess a word chosen by the computer
 */

//Imports for use in the program.
import javax.swing.*; //importing the swing package
import java.io.*; //importing io package
import java.util.ArrayList; //allowing for the use of ArrayLists
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class frmHangman extends javax.swing.JFrame {
    //Declaring public variables
    String username;
    JButton[] buttons; //Delaring a button array
    String randWord;
    String blankWord = "";
    String blankWordSpaceless; //the blank word string but without spaces
    int limbCount;
    int score;
    int [] allScores = new int [11];
    String [] leaderNames = new String [11];
    ArrayList <String> words = new ArrayList <String>(); //The words ArrayList is created in order to hold the information from the .txt file. 
    //Declaring and initializing image icon variables that hold each state of the Hanging Man
    ImageIcon a = new ImageIcon("src\\images\\manStage0.png");
    ImageIcon b = new ImageIcon("src\\images\\manStage1.png");
    ImageIcon c = new ImageIcon("src\\images\\manStage2.png");
    ImageIcon d = new ImageIcon("src\\images\\manStage3.png");
    ImageIcon e = new ImageIcon("src\\images\\manStage4.png");
    ImageIcon f = new ImageIcon("src\\images\\manStage5.png"); 
    ImageIcon g = new ImageIcon("src\\images\\manStage6.png");
    ImageIcon h = new ImageIcon("src\\images\\manStage7.png");
    ImageIcon[] images = {a, b, c, d, e, f, g, h} ; //This imageicon array hold the previously declared images.
    
    /*
    enable
    This method enables or disables the letter buttons on the GUI
    Parameter: boolean
    Returns: void
   */
    public void enable (boolean enable) {
       //for loop that cycles through all of the letter buttons to enable or disable them
        for (int i = 0; i <= 25; i++) {
            buttons[i].setEnabled(enable); //enables or disables the buttons.
        }
    }
    
  /*
    findLeaders
    This method finds who the top ten players on the leaderboard are and their scores. 
    Parameter: void
    Returns: void
   */
    public void findLeaders () {
        //try and catch in case of errors.
        try {
            //allows for the parsing and reading/writing of the xml file.
            String filepath = "leaderboard.xml"; //setting the filepath as a String for clairity 
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);
        
            //creating a nodeList that contains scores in the XML file.
            NodeList nodeList1 = doc.getElementsByTagName("score"); 
            
            //creating a for loop to cycle through all of the elements in nodeList and stores them in an array
            for (int i = 0; i < nodeList1.getLength(); i++) {
                Element element = (Element) nodeList1.item(i); //making an element that stores the item in the node list based on "i"
                 allScores[i] = Integer.parseInt(element.getFirstChild().getNodeValue());
            }
            
            //creating a nodeList that contains scores in the XML file.
            NodeList nodeList2 = doc.getElementsByTagName("name"); 
            
            //creating a for loop to cycle through all of the elements in nodeList and stores them in an array
            for (int i = 0; i < nodeList2.getLength(); i++) {
                Element element = (Element) nodeList2.item(i); //making an element that stores the item in the node list based on "i"
                 leaderNames[i] = element.getFirstChild().getNodeValue();
            }            
        }
        catch (ParserConfigurationException | IOException | SAXException pce) { //the catch statement catches various errors.
            System.out.println("ERROR"); //sends an error message
        }
        
    }

    /*
    quickSort
    This method sorts the allScores and leaderNames arrays using a quick sort algorithm.
    Parameter:int, int
    Returns: void
   */
    public void quickSort (int low, int high) {
        //uses recursion to sort the array.
        if (low < high) {  //recursion ends if low is greater than high
            int pivotLocation = partition( low, high); //using the partition method, the location of the pivot is found.
            //recursion used to deal with the segments greater than and less than the partition.
            quickSort(low, pivotLocation - 1);
            quickSort(pivotLocation + 1, high);
        }
    }
    
    /*
    partition
    This method is to be used for quick sort in order to exchange values around.
    Parameter: int, int
    Returns: int 
   */
    public int partition (int low, int high) {
        //declaring variables
        int pivot = allScores[high];
        int i = low - 1;
        
        //This code swapes the values around.
        for (int j = low; j < high; j++) {
            if (allScores[j] <= pivot) {
                i++;
                //swaping allScores[i] and allScores[j]
                int temp = allScores[i];
                allScores[i] = allScores[j];
                allScores[j] = temp;
                
                //swaping leaderNames[i] and leaderNames[j]
                String temp2 = leaderNames[i];
                leaderNames[i] = leaderNames[j];
                leaderNames[j] = temp2;
            }
        }
        //swaping allScores[i+1] and allScores[high]
        int temp = allScores[i+1];
        allScores[i+1] = allScores[high];
        allScores[high] = temp;
        
        //swaping leaderNames[i+1] and leaderNames[high]
        String temp2 = leaderNames[i+1];
        leaderNames[i+1] = leaderNames[high];
        leaderNames[high] = temp2;        
        
        //returning i + 1
        return i + 1;
    }    

    /*
    letterPress
    This method dictates what happens when the user presses a letter button. It causes the location of the 
    clicked letter to be shown in the word, and also handles what happens when the use looses or wins the game.
    Parameter: int letterIndex
    Returns: void
   */
    public void letterPress (int letterIndex) {
        //creating variables
        ArrayList <Integer> indices = new ArrayList <Integer>();
        String guess = buttons[letterIndex].getText().toLowerCase();

        //Disables the letter button that the user clicked.
        buttons[letterIndex].setEnabled(false);
        //finds the first index of the letter the user picked in the computer genereated word.
        int index = randWord.indexOf(guess);
        
        //if else to differentiate between if the user's letter guess is in the word or not.
        if (index == -1) {
            limbCount++; //tracks the number of limbs added.
            lblManImage.setIcon(images[limbCount]); //showing that the Hanging Man has gained a limb
            //If the player looses this runs
            if (limbCount == 7) {
                //disables all of the buttons
                enable(false); 
                //displaying a pop up that tells the user that they have lost.
                gameoverScreen.loseScreen(randWord, score); //referencing the gameoverScreen class in order to use its methods
                //recieves information from the XML file
                findLeaders();
                //Adding the user's name and score to the last index of the two arrays
                allScores[10] = score;
                leaderNames[10] = username;
                //The two arrays are sorted.
                quickSort(0, 10);
                
                 //rearranging the allScores and leaderNames array so that index 10 in both of them contains the user or the score with the smallest value.
                for (int i = 1; i <= allScores.length - 1; i++) {      
                    //swaping integers
                    int temp = allScores[i];
                    allScores[i] = allScores[i - 1];
                    allScores[i -1] = temp;
                    
                    //swaping strings
                    String temp2 = leaderNames[i];
                    leaderNames[i] = leaderNames[i - 1];
                    leaderNames[i - 1] = temp2;                
                }
                
                //Showing the player the leaderboard pop up.
                leaderboard.showLeaderboard(leaderNames, allScores);
                //Updating the XML file so that the data is stored even when the game is closed.
                leaderboard.leaderboardXML(leaderNames, allScores);
                
                //Setting the user's scores to default and outputting it to the user.
                score = 0;
                lblScore.setText("Score: " + score);
                //Resetting the start game button to be the try again button, and enabling it.
                btnStartGame.setEnabled(true);
                btnStartGame.setText("Try Again");
                //Setting the word to default
                lblWord.setText("word");
            }
        }
        else {
            //while loop finds all indices of where the letter apears in the word
            while (index >= 0) {
                indices.add(index);//adds the indices to an ArrayList
                index = randWord.indexOf(guess, index + 1);
            }
            //for loop replaces the locations 
            for (int i = 0; i < indices.size(); i++) {
                 //This creates substrings in order to split the string, before and after the character that is going to be replaced.
                 //The replacing character is then added to the correct spot in the string.
                blankWordSpaceless = blankWordSpaceless.substring(0, indices.get(i)) + guess + blankWordSpaceless.substring(indices.get(i) + 1);
            }
            //adding a space between each character for the user's ease of viewing.
            blankWord = blankWordSpaceless.replace(""," ").trim();
            
            //Outputting the more filled in word to the user.
            lblWord.setText(blankWord); 
            
            //Making the game stop when the user figuers out the word.
            if (blankWord.contains("_")) {
                //Nothing happens
            }
            else { //else statement runs in the user has won.
                //disables all of the buttons
                enable(false);
                //Setting the start game button to become the play again button.
                btnStartGame.setText("Play Again");
                //enables the play again button
                btnStartGame.setEnabled(true);
                
                //finding the user's score based on the length of the word.
                score += randWord.length();
                //outputting the score to the user.
                lblScore.setText("Score: " + score);
                
                //displaying a pop up that tells the user that they have won.
                winningScreen.winScreen(randWord);  //referencing the winingScreen class in order to use its methods 
            }
        }
    }

    /**
     * Creates new form frmHangman
     */
    public frmHangman() {
        initComponents();
        //uses the createIntro method to output a pop up that desplays the introductory splash screen, with the creator information at the header.
        splashScreen.createIntro(); //referencing the spashScreen class in order to use its methods 
        //uses the usernamePrompt method to output a pop up that prompts the user to enter their username, and stores the username in a public variable.
        username = splashScreen.usernamePrompt(); //referencing the spashScreen class in order to use its methods
        //shows the user their username at the top of the GUI.
        lblUsername.setText("User: " + username); 
        
        //Declaring and initializing a button array for each of the letter buttons.
        buttons = new JButton[]{btnA, btnB, btnC, btnD, btnE, 
                                btnF, btnG, btnH, btnI, btnJ,
                                btnK, btnL, btnM, btnN, btnO,
                                btnP, btnQ, btnR, btnS, btnT,
                                btnU, btnV, btnW, btnX, btnY, btnZ};
        enable (false); //Using the enable method to disable all of the buttons.      

        BufferedReader br = null; //This locates the file and opens it for reading.
        
        //The purpose of this code is to read information from wordList.txt.
        try { //starting a try and catch
            br = new BufferedReader(new FileReader("src\\txtFiles\\wordList.txt"));
            String entry;
            //while loop reads the information line by line until the file ends.
            while ((entry = br.readLine()) != null ){
                words.add(entry); //each entry is sent to the words arrayList
            }
        } catch (IOException e) { //catch ensures that the program does not read past the end of the file.
            e.printStackTrace();
        } finally {
            try { //this try section closes the file.
                br.close(); //this closes the file
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBackground = new javax.swing.JPanel();
        lblManImage = new javax.swing.JLabel();
        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        btnA = new javax.swing.JButton();
        btnB = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btnD = new javax.swing.JButton();
        btnE = new javax.swing.JButton();
        btnF = new javax.swing.JButton();
        btnG = new javax.swing.JButton();
        btnH = new javax.swing.JButton();
        btnI = new javax.swing.JButton();
        btnJ = new javax.swing.JButton();
        btnK = new javax.swing.JButton();
        btnL = new javax.swing.JButton();
        btnM = new javax.swing.JButton();
        btnN = new javax.swing.JButton();
        btnO = new javax.swing.JButton();
        btnP = new javax.swing.JButton();
        btnQ = new javax.swing.JButton();
        btnR = new javax.swing.JButton();
        btnS = new javax.swing.JButton();
        btnT = new javax.swing.JButton();
        btnU = new javax.swing.JButton();
        btnV = new javax.swing.JButton();
        btnW = new javax.swing.JButton();
        btnX = new javax.swing.JButton();
        btnY = new javax.swing.JButton();
        btnZ = new javax.swing.JButton();
        btnStartGame = new javax.swing.JButton();
        pnlWord = new javax.swing.JPanel();
        lblWord = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBackground.setBackground(new java.awt.Color(237, 247, 249));

        lblManImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manStage0.png"))); // NOI18N

        pnlHeader.setBackground(new java.awt.Color(186, 239, 255));

        lblTitle.setFont(new java.awt.Font("Lucida Sans", 0, 36)); // NOI18N
        lblTitle.setText("Hangman");

        lblUsername.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lblUsername.setText("Username");

        lblScore.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lblScore.setText("Score: 0");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblTitle)
                .addGap(161, 161, 161)
                .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(lblScore, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(lblScore))
                .addGap(25, 25, 25))
        );

        btnA.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnA.setForeground(new java.awt.Color(0, 102, 255));
        btnA.setText("A");
        btnA.setFocusable(false);
        btnA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAActionPerformed(evt);
            }
        });

        btnB.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnB.setForeground(new java.awt.Color(0, 102, 255));
        btnB.setText("B");
        btnB.setFocusable(false);
        btnB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBActionPerformed(evt);
            }
        });

        btnC.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnC.setForeground(new java.awt.Color(0, 102, 255));
        btnC.setText("C");
        btnC.setFocusable(false);
        btnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCActionPerformed(evt);
            }
        });

        btnD.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnD.setForeground(new java.awt.Color(0, 102, 255));
        btnD.setText("D");
        btnD.setFocusable(false);
        btnD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDActionPerformed(evt);
            }
        });

        btnE.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnE.setForeground(new java.awt.Color(0, 102, 255));
        btnE.setText("E");
        btnE.setFocusable(false);
        btnE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEActionPerformed(evt);
            }
        });

        btnF.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnF.setForeground(new java.awt.Color(0, 102, 255));
        btnF.setText("F");
        btnF.setFocusable(false);
        btnF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFActionPerformed(evt);
            }
        });

        btnG.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnG.setForeground(new java.awt.Color(0, 102, 255));
        btnG.setText("G");
        btnG.setFocusable(false);
        btnG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGActionPerformed(evt);
            }
        });

        btnH.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnH.setForeground(new java.awt.Color(0, 102, 255));
        btnH.setText("H");
        btnH.setFocusable(false);
        btnH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHActionPerformed(evt);
            }
        });

        btnI.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnI.setForeground(new java.awt.Color(0, 102, 255));
        btnI.setText("I");
        btnI.setFocusable(false);
        btnI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIActionPerformed(evt);
            }
        });

        btnJ.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnJ.setForeground(new java.awt.Color(0, 102, 255));
        btnJ.setText("J");
        btnJ.setFocusable(false);
        btnJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJActionPerformed(evt);
            }
        });

        btnK.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnK.setForeground(new java.awt.Color(0, 102, 255));
        btnK.setText("K");
        btnK.setFocusable(false);
        btnK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKActionPerformed(evt);
            }
        });

        btnL.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnL.setForeground(new java.awt.Color(0, 102, 255));
        btnL.setText("L");
        btnL.setFocusable(false);
        btnL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLActionPerformed(evt);
            }
        });

        btnM.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnM.setForeground(new java.awt.Color(0, 102, 255));
        btnM.setText("M");
        btnM.setFocusable(false);
        btnM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMActionPerformed(evt);
            }
        });

        btnN.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnN.setForeground(new java.awt.Color(0, 102, 255));
        btnN.setText("N");
        btnN.setFocusable(false);
        btnN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNActionPerformed(evt);
            }
        });

        btnO.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnO.setForeground(new java.awt.Color(0, 102, 255));
        btnO.setText("O");
        btnO.setFocusable(false);
        btnO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOActionPerformed(evt);
            }
        });

        btnP.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnP.setForeground(new java.awt.Color(0, 102, 255));
        btnP.setText("P");
        btnP.setFocusable(false);
        btnP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPActionPerformed(evt);
            }
        });

        btnQ.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnQ.setForeground(new java.awt.Color(0, 102, 255));
        btnQ.setText("Q");
        btnQ.setFocusable(false);
        btnQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQActionPerformed(evt);
            }
        });

        btnR.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnR.setForeground(new java.awt.Color(0, 102, 255));
        btnR.setText("R");
        btnR.setFocusable(false);
        btnR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRActionPerformed(evt);
            }
        });

        btnS.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnS.setForeground(new java.awt.Color(0, 102, 255));
        btnS.setText("S");
        btnS.setFocusable(false);
        btnS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSActionPerformed(evt);
            }
        });

        btnT.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnT.setForeground(new java.awt.Color(0, 102, 255));
        btnT.setText("T");
        btnT.setFocusable(false);
        btnT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTActionPerformed(evt);
            }
        });

        btnU.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnU.setForeground(new java.awt.Color(0, 102, 255));
        btnU.setText("U");
        btnU.setFocusable(false);
        btnU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUActionPerformed(evt);
            }
        });

        btnV.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnV.setForeground(new java.awt.Color(0, 102, 255));
        btnV.setText("V");
        btnV.setFocusable(false);
        btnV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVActionPerformed(evt);
            }
        });

        btnW.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnW.setForeground(new java.awt.Color(0, 102, 255));
        btnW.setText("W");
        btnW.setFocusable(false);
        btnW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWActionPerformed(evt);
            }
        });

        btnX.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnX.setForeground(new java.awt.Color(0, 102, 255));
        btnX.setText("X");
        btnX.setFocusable(false);
        btnX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXActionPerformed(evt);
            }
        });

        btnY.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnY.setForeground(new java.awt.Color(0, 102, 255));
        btnY.setText("Y");
        btnY.setFocusable(false);
        btnY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYActionPerformed(evt);
            }
        });

        btnZ.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        btnZ.setForeground(new java.awt.Color(0, 102, 255));
        btnZ.setText("Z");
        btnZ.setFocusable(false);
        btnZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZActionPerformed(evt);
            }
        });

        btnStartGame.setText("Start Game");
        btnStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartGameActionPerformed(evt);
            }
        });

        pnlWord.setBackground(new java.awt.Color(237, 247, 249));

        lblWord.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblWord.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWord.setText("word");

        javax.swing.GroupLayout pnlWordLayout = new javax.swing.GroupLayout(pnlWord);
        pnlWord.setLayout(pnlWordLayout);
        pnlWordLayout.setHorizontalGroup(
            pnlWordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblWord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlWordLayout.setVerticalGroup(
            pnlWordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlWordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWord)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlBackgroundLayout = new javax.swing.GroupLayout(pnlBackground);
        pnlBackground.setLayout(pnlBackgroundLayout);
        pnlBackgroundLayout.setHorizontalGroup(
            pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBackgroundLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(lblManImage)
                .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBackgroundLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlBackgroundLayout.createSequentialGroup()
                                .addComponent(btnJ, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnK, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnL, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnM)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnN)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnO)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnP, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnQ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnR, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(pnlWord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlBackgroundLayout.createSequentialGroup()
                                    .addComponent(btnA)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnB, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnD)
                                    .addGap(5, 5, 5)
                                    .addComponent(btnE, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnG)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnH)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnI, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pnlBackgroundLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnS, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnT, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnU)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnStartGame)
                            .addGroup(pnlBackgroundLayout.createSequentialGroup()
                                .addComponent(btnV, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnW)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnX, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnY, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnZ, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(23, Short.MAX_VALUE))
            .addComponent(pnlHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlBackgroundLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL, btnM, btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ});

        pnlBackgroundLayout.setVerticalGroup(
            pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBackgroundLayout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBackgroundLayout.createSequentialGroup()
                        .addComponent(pnlWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnB, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnD, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnF, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnG, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnH, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnI, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnE, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnA, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnJ, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnK, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnM, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnN, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnO, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnQ, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnR, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnS, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnT, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnU, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnV, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnW, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnX, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnY, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnZ, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addComponent(btnStartGame))
                    .addComponent(lblManImage))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBackgroundLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL, btnM, btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartGameActionPerformed
        //giving default values to variables for when the game is restarted.
        blankWord = "";
        blankWordSpaceless = "";
        limbCount = 0;
        lblManImage.setIcon(images[0]);

        //enabling the game
        enable(true); //Using the enable method to disable all of the buttons.
        btnStartGame.setEnabled(false); //disabling the Start Game button so the user cannot use it until they have finished one round of the game.

        //Choosing the randomized word for the user to guess
        int numLimit = words.size() - 1; //finds the amount of words in the words ArrayList, and subracts it by one
        int randNum = (int)(Math.random() * (numLimit + 1)); //generating a number between the amount of words in the words ArrayList and 0
        randWord = words.get(randNum); //setting the random word that corresponds with the number generated

        //Using a for loop to turn the selected word in to blank spaces (aka underscores)
        for (int i = 0; i < randWord.length(); i++) {
            blankWord += "_ ";
        }
        //Stores the blankWord String without any spaces
        blankWordSpaceless =  blankWord.replace(" ", "").trim();

        //Showing the blank word to the user
        lblWord.setText(blankWord);
    }//GEN-LAST:event_btnStartGameActionPerformed

    private void btnZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(25);
    }//GEN-LAST:event_btnZActionPerformed

    private void btnYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(24);
    }//GEN-LAST:event_btnYActionPerformed

    private void btnXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(23);
    }//GEN-LAST:event_btnXActionPerformed

    private void btnWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(22);
    }//GEN-LAST:event_btnWActionPerformed

    private void btnVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVActionPerformed
        letterPress(21);
    }//GEN-LAST:event_btnVActionPerformed

    private void btnUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(20);
    }//GEN-LAST:event_btnUActionPerformed

    private void btnTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(19);
    }//GEN-LAST:event_btnTActionPerformed

    private void btnSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(18);
    }//GEN-LAST:event_btnSActionPerformed

    private void btnRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(17);
    }//GEN-LAST:event_btnRActionPerformed

    private void btnQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(16);
    }//GEN-LAST:event_btnQActionPerformed

    private void btnPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(15);
    }//GEN-LAST:event_btnPActionPerformed

    private void btnOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(14);
    }//GEN-LAST:event_btnOActionPerformed

    private void btnNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(13);
    }//GEN-LAST:event_btnNActionPerformed

    private void btnMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(12);
    }//GEN-LAST:event_btnMActionPerformed

    private void btnLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(11);
    }//GEN-LAST:event_btnLActionPerformed

    private void btnKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(10);
    }//GEN-LAST:event_btnKActionPerformed

    private void btnJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(9);
    }//GEN-LAST:event_btnJActionPerformed

    private void btnIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(8);
    }//GEN-LAST:event_btnIActionPerformed

    private void btnHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(7);
    }//GEN-LAST:event_btnHActionPerformed

    private void btnGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(6);
    }//GEN-LAST:event_btnGActionPerformed

    private void btnFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(5);
    }//GEN-LAST:event_btnFActionPerformed

    private void btnEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(4);
    }//GEN-LAST:event_btnEActionPerformed

    private void btnDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(3);
    }//GEN-LAST:event_btnDActionPerformed

    private void btnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(2);
    }//GEN-LAST:event_btnCActionPerformed

    private void btnBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(1);
    }//GEN-LAST:event_btnBActionPerformed

    private void btnAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAActionPerformed
        //Uses the letterPress method to allow for the opperation of the letter button when it is pressed.
        letterPress(0);
    }//GEN-LAST:event_btnAActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("FlatLaf".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmHangman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmHangman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmHangman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmHangman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmHangman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnA;
    private javax.swing.JButton btnB;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnD;
    private javax.swing.JButton btnE;
    private javax.swing.JButton btnF;
    private javax.swing.JButton btnG;
    private javax.swing.JButton btnH;
    private javax.swing.JButton btnI;
    private javax.swing.JButton btnJ;
    private javax.swing.JButton btnK;
    private javax.swing.JButton btnL;
    private javax.swing.JButton btnM;
    private javax.swing.JButton btnN;
    private javax.swing.JButton btnO;
    private javax.swing.JButton btnP;
    private javax.swing.JButton btnQ;
    private javax.swing.JButton btnR;
    private javax.swing.JButton btnS;
    private javax.swing.JButton btnStartGame;
    private javax.swing.JButton btnT;
    private javax.swing.JButton btnU;
    private javax.swing.JButton btnV;
    private javax.swing.JButton btnW;
    private javax.swing.JButton btnX;
    private javax.swing.JButton btnY;
    private javax.swing.JButton btnZ;
    private javax.swing.JLabel lblManImage;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblWord;
    private javax.swing.JPanel pnlBackground;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlWord;
    // End of variables declaration//GEN-END:variables
}

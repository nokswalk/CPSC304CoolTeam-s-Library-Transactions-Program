package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

import users.BorrowerUser;
import users.ClerkUser;
import users.LibrarianUser;


/**
 * Loads application.
 * Using simple text interface like "branch" tutorial until GUI is set up
 */
public class Main implements ActionListener {

	// command line reader 
    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
	public static Connection con;
	
    // user is allowed 3 login attempts
    private int loginAttempts = 0;

	// components of the login window
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JFrame mainFrame;


    /*
     * constructs login window and loads JDBC driver
     */ 
	public Main() {
		// TODO
		mainFrame = new JFrame("User Login");

		JLabel usernameLabel = new JLabel("Enter username: ");
		JLabel passwordLabel = new JLabel("Enter password: ");

		usernameField = new JTextField(10);
		passwordField = new JPasswordField(10);
		passwordField.setEchoChar('*');

		JButton loginButton = new JButton("Log In");

		JPanel contentPane = new JPanel();
		mainFrame.setContentPane(contentPane);


		// layout components using the GridBag layout manager

		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the username label 
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		contentPane.add(usernameLabel);

		// place the text field for the username 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		contentPane.add(usernameField);

		// place password label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		contentPane.add(passwordLabel);

		// place the password field 
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);

		// place the login button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		contentPane.add(loginButton);

		passwordField.addActionListener(this);
		loginButton.addActionListener(this);
		loginButton.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			mainFrame.dispose();
    			try {
    				showUserMenu();
    			} catch (ParseException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}     
    		}
    	});

		// anonymous inner class for closing the window
		mainFrame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{ 
				System.exit(0); 
			}
		});
		
		// size the window to obtain a best fit for the components
		mainFrame.pack();

		// center the frame
		Dimension d = mainFrame.getToolkit().getScreenSize();
		Rectangle r = mainFrame.getBounds();
		mainFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// make the window visible
		mainFrame.setVisible(true);

		// place the cursor in the text field for the username
		usernameField.requestFocus();

		try 
		{
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
	}


	/*
	 * connects to Oracle database named ug using user supplied username and password
	 */ 
	private boolean connect(String username, String password)
	{
		String connectURL = "jdbc:oracle:thin:@localhost:1522:ug"; 

		try 
		{
			con = DriverManager.getConnection(connectURL,username,password);

			System.out.println("\nConnected to Oracle!");
			return true;
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
			return false;
		}
	}


	/*
	 * event handler for login window
	 */ 
	public void actionPerformed(ActionEvent e) 
	{
		if ( connect(usernameField.getText(), String.valueOf(passwordField.getPassword())) )
		{
			// if the username and password are valid, 
			// remove the login window and display a text menu 
			mainFrame.dispose();
			try {
				showUserMenu();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}     
		}
		else
		{
			loginAttempts++;

			if (loginAttempts >= 3)
			{
				mainFrame.dispose();
				System.exit(-1);
			}
			else
			{
				// clear the password
				passwordField.setText("");
			}
		}             

	}
	
	private void setupButton(JButton button, JPanel panel){ //aligns and places button to the proper panel
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);
	}
	
	private void showMenu(int user) throws ParseException{
		
		final JFrame menu = new JFrame("Menu");
		final JPanel panelLibrarianMenu = new JPanel();
		final JPanel panelLibrarianSubmenu = new JPanel();
		final JPanel panelBorrowerMenu = new JPanel();
		final JPanel panelBorrowerSubmenu = new JPanel();
		final JPanel panelClerkMenu = new JPanel();
		final JPanel panelClerkSubmenu = new JPanel();
		
		final JFrame oneInputWindow = new JFrame("Input");
		final JPanel panelSomethingWindow = new JPanel();
		
		
		
		JTextArea textArea = new JTextArea();

		TextAreaOutputStream taOutputStream = new TextAreaOutputStream(textArea, "Console output");
		
		//setting how we want the panel to be shown on the frame
		panelLibrarianMenu.setLayout(new BoxLayout(panelLibrarianMenu,BoxLayout.Y_AXIS));
		panelLibrarianSubmenu.setLayout(new BoxLayout(panelLibrarianSubmenu, BoxLayout.Y_AXIS));
		panelBorrowerMenu.setLayout(new BoxLayout(panelBorrowerMenu, BoxLayout.Y_AXIS));
		panelBorrowerSubmenu.setLayout(new BoxLayout(panelBorrowerSubmenu, BoxLayout.Y_AXIS));
		panelClerkMenu.setLayout(new BoxLayout(panelClerkMenu, BoxLayout.Y_AXIS));
		panelClerkSubmenu.setLayout(new BoxLayout(panelClerkSubmenu, BoxLayout.Y_AXIS));
		
		//panel.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)); //make a scrollbar for navigation purposes
		//System.setOut(new PrintStream(taOutputStream));
		if (user == 1){ //user is librarian
		//make addNewBook frame and panel
			int ROWS = 5;
			int COLUMNS = 2;
			final JFrame addNewBookFrame = new JFrame("Add a New Book");
			final JPanel paneladdNewBook = new JPanel();
			GridLayout layout = new GridLayout(ROWS, COLUMNS);
			paneladdNewBook.setLayout(layout);
			addNewBookFrame.setPreferredSize(new Dimension (300, 200));
			addNewBookFrame.setLocationRelativeTo(null);
			
			
		//make addNewBookCopy frame and panel
			final JFrame addNewBookCopyFrame = new JFrame("Add a New Book Copy");
			final JPanel paneladdNewBookCopy = new JPanel();
		//make reportCheckOutBooks frame and panel
			final JFrame reportCheckedoutBooksFrame = new JFrame("Report Checked out Books");
			final JPanel panelreportCheckedoutBooks = new JPanel();
		//make mostPopular frame and panel
			final JFrame mostPopularFrame = new JFrame("Most Popular");
			final JPanel panelmostPopular = new JPanel();
			
		//making labels
			JLabel isbn = new JLabel("ISBN:");
			JLabel title = new JLabel("Title:");
			JLabel mainAuthor = new JLabel("Main Author:");
			JLabel publisher = new JLabel("Publisher:");
			JLabel year = new JLabel("Year:");
			
		//making buttons
			JButton addBook = new JButton("Add Book");
			JButton reportCheckedOutBooks = new JButton("Report Checked Out Books");
			JButton mostPopular = new JButton("Most Popular Books");
			JButton addNewBook = new JButton("Add New Book");
			JButton addNewBookCopy = new JButton("Add New Book Copy");
			JButton back = new JButton("Go Back");
			JButton quit = new JButton("Quit Program");
			
		//making textarea
			JTextField isbntxt = new JTextField();
			JTextField titletxt = new JTextField();
			JTextField mainAuthortxt = new JTextField();
			JTextField publishertxt = new JTextField();
			JTextField yeartxt = new JTextField();
			
			//setupButton(addBook, panelLibrarianMenu);
			//setupButton(reportCheckedOutBooks, panelLibrarianMenu);
			
		//center align buttons
			addBook.setAlignmentX(Component.CENTER_ALIGNMENT);
			reportCheckedOutBooks.setAlignmentX(Component.CENTER_ALIGNMENT);
			mostPopular.setAlignmentX(Component.CENTER_ALIGNMENT);
			addNewBook.setAlignmentX(Component.CENTER_ALIGNMENT);
			addNewBookCopy.setAlignmentX(Component.CENTER_ALIGNMENT);
			back.setAlignmentX(Component.CENTER_ALIGNMENT);
			quit.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			menu.setPreferredSize(new Dimension(200, 300));
			
		//attaching buttons to the panel
			panelLibrarianMenu.add(addBook);
			panelLibrarianMenu.add(reportCheckedOutBooks);
			panelLibrarianMenu.add(mostPopular);
			panelLibrarianMenu.add(quit);
			
			panelLibrarianSubmenu.add(addNewBook);
			panelLibrarianSubmenu.add(addNewBookCopy);
			panelLibrarianSubmenu.add(back);
			
		//attaching labels and txt field to the panel
			paneladdNewBook.add(isbn);
			paneladdNewBook.add(isbntxt);
			paneladdNewBook.add(title);
			paneladdNewBook.add(titletxt);
			paneladdNewBook.add(mainAuthor);
			paneladdNewBook.add(mainAuthortxt);
			paneladdNewBook.add(publisher);
			paneladdNewBook.add(publishertxt);
			paneladdNewBook.add(year);
			paneladdNewBook.add(yeartxt);
			
		//add the panel into JFrame
			
			menu.getContentPane().add(panelLibrarianMenu);
			
			//add listeners to the buttons
			addBook.addActionListener(new ActionListener() { //go into submenu
	    		public void actionPerformed(ActionEvent e) {
	    			menu.getContentPane().add(panelLibrarianSubmenu); //attach submenu to the frame
					panelLibrarianMenu.setVisible(false);
					panelLibrarianSubmenu.setVisible(true);
	    		}
	    	});
			reportCheckedOutBooks.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			LibrarianUser.reportCheckedOutBooks();
	    		}
	    	});
			mostPopular.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			addNewBook.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			addNewBookFrame.getContentPane().add(paneladdNewBook);
	    			addNewBookFrame.pack();
	    			addNewBookFrame.setVisible(true);
	    		}
	    	});
			addNewBookCopy.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			back.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			panelLibrarianSubmenu.setVisible(false);
	    			panelLibrarianMenu.setVisible(true);
	    		}
	    	});
			quit.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			menu.dispose();
	    			System.exit(0);
	    		}
	    	});
			
			
			//panelSubmenu.setVisible(false);
			
			//display the window
			menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			menu.pack();
			menu.setVisible(true);
			
			//System.out.println("HELLOLLOLOLOOLOOOOO");
			//LibrarianUser.main();
		}
		else if (user == 2){ //user is borrower
			//menu.setPreferredSize(new Dimension(700, 800));
			//menu.add(textArea);
			
		//make some buttons
			JButton searchBook = new JButton("Search Book");
			JButton searchBookbyTitle = new JButton("Search Book by Title");
			JButton searchBookbySubject = new JButton("Search Book by Subject");
			JButton searchBookbyAuthor = new JButton("Search Book by Author");
			JButton checkAccount = new JButton("Check Account");
			JButton requestHold = new JButton("Place a Hold Request");
			JButton payFines = new JButton("Pay Fines");
			JButton back = new JButton("Go Back");
			JButton quit = new JButton("Quit Program");
			
		//center align buttons
			searchBook.setAlignmentX(Component.CENTER_ALIGNMENT);
			searchBookbyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
			searchBookbySubject.setAlignmentX(Component.CENTER_ALIGNMENT);
			searchBookbyAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
			checkAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
			requestHold.setAlignmentX(Component.CENTER_ALIGNMENT);
			payFines.setAlignmentX(Component.CENTER_ALIGNMENT);
			back.setAlignmentX(Component.CENTER_ALIGNMENT);
			quit.setAlignmentX(Component.CENTER_ALIGNMENT);
			
		//set the menu frame's size
			menu.setPreferredSize(new Dimension(200, 300));
			
		//attaching buttons to the frame
			panelBorrowerMenu.add(searchBook);
			panelBorrowerMenu.add(checkAccount);
			panelBorrowerMenu.add(requestHold);
			panelBorrowerMenu.add(payFines);
			panelBorrowerMenu.add(quit);
			
			panelBorrowerSubmenu.add(searchBookbyAuthor);
			panelBorrowerSubmenu.add(searchBookbySubject);
			panelBorrowerSubmenu.add(searchBookbyTitle);
			panelBorrowerSubmenu.add(back);
			
		//add borrower menu to the frame
			menu.getContentPane().add(panelBorrowerMenu);
			
		//add the button listeners
			searchBook.addActionListener(new ActionListener() { //go into submenu
	    		public void actionPerformed(ActionEvent e) {
	    			menu.getContentPane().add(panelBorrowerSubmenu); //attach submenu to the frame
					panelBorrowerMenu.setVisible(false);
					panelBorrowerSubmenu.setVisible(true);
	    		}
	    	});
			searchBookbyTitle.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			searchBookbySubject.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			searchBookbyAuthor.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			checkAccount.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			requestHold.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			payFines.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			back.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			panelBorrowerSubmenu.setVisible(false);
	    			panelBorrowerMenu.setVisible(true);
	    		}
	    	});
			quit.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			menu.dispose();
	    			System.exit(0);
	    		}
	    	});
			
			//display the window
			menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			menu.pack();
			menu.setVisible(true);
		}
		else if (user == 3){ //user is clerk
		//make addNewBook frame and panel
			int ROWS = 7;
			int COLUMNS = 2;
	
			final JFrame addBorrowerFrame = new JFrame("Add a New Borrower");
			final JPanel paneladdBorrower = new JPanel();
			GridLayout layout = new GridLayout(ROWS, COLUMNS);
			paneladdBorrower.setLayout(layout);
			addBorrowerFrame.setPreferredSize(new Dimension (300, 200));
			addBorrowerFrame.setLocationRelativeTo(null);
			
		//make addNewBookCopy frame and panel
			final JFrame checkOutItemsFrame = new JFrame("Check out Items");
			final JPanel panelcheckOutItemsCopy = new JPanel();
		//make reportCheckOutBooks frame and panel
			final JFrame processReturnFrame = new JFrame("Process a return");
			final JPanel panelprocessReturnBooks = new JPanel();
		
		//make mostPopular frame and panel
			final JFrame checkOverdueItemsFrame = new JFrame("Check overdue items");
			final JPanel panelcheckOverdueItems = new JPanel();
			
		//making labels for addBorrower
			JLabel password = new JLabel("Password:");
			JLabel name = new JLabel("Name:");
			JLabel address = new JLabel("Address:");
			JLabel phone = new JLabel("Phone Number:");
			JLabel email = new JLabel("Email Address:");
			JLabel sinOrStno = new JLabel("SIN or Student Number:");
			JLabel type = new JLabel("Type(student, staff, etc):");

		//making textarea for addBorrower
			JTextField passwordtxt = new JTextField();
			JTextField nametxt = new JTextField();
			JTextField addresstxt = new JTextField();
			JTextField phonetxt = new JTextField();
			JTextField emailtxt = new JTextField();
			JTextField sinOrStnotxt = new JTextField();
			JTextField typetxt = new JTextField();
			
		//make buttons
			JButton addBorrower = new JButton("Add Borrower");
			JButton checkOutItems = new JButton("Check out Items");
			JButton processReturn = new JButton("Process Return");
			JButton checkOverdueItems = new JButton("Check Overdue Items");
			JButton back = new JButton("Go Back");
			JButton quit = new JButton("Quit Program");
			
		//center align buttons
			addBorrower.setAlignmentX(Component.CENTER_ALIGNMENT);
			checkOutItems.setAlignmentX(Component.CENTER_ALIGNMENT);
			processReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
			checkOverdueItems.setAlignmentX(Component.CENTER_ALIGNMENT);
			back.setAlignmentX(Component.CENTER_ALIGNMENT);
			quit.setAlignmentX(Component.CENTER_ALIGNMENT);
			
		//set the menu frame's size
			menu.setPreferredSize(new Dimension(200, 300));
			
		//attaching buttons to the frame
			panelClerkMenu.add(addBorrower);
			panelClerkMenu.add(checkOutItems);
			panelClerkMenu.add(processReturn);
			panelClerkMenu.add(checkOverdueItems);
			panelClerkMenu.add(quit);

			
		//attaching 
		//attach panel to the frame
			menu.getContentPane().add(panelClerkMenu);
			
			
		//attaching labels and txt field to the panel
			paneladdBorrower.add(password);
			paneladdBorrower.add(passwordtxt);
			paneladdBorrower.add(name);
			paneladdBorrower.add(nametxt);
			paneladdBorrower.add(address);
			paneladdBorrower.add(addresstxt);
			paneladdBorrower.add(phone);
			paneladdBorrower.add(phonetxt);
			paneladdBorrower.add(email);
			paneladdBorrower.add(emailtxt);
			paneladdBorrower.add(sinOrStno);
			paneladdBorrower.add(sinOrStnotxt);
			paneladdBorrower.add(type);
			paneladdBorrower.add(typetxt);
			
		//add listeners to the buttons
			addBorrower.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			addBorrowerFrame.getContentPane().add(paneladdBorrower);
	    			addBorrowerFrame.pack();
	    			addBorrowerFrame.setVisible(true);
	    		}
	    	});
			checkOutItems.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			processReturn.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			checkOverdueItems.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			
	    		}
	    	});
			back.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			panelClerkSubmenu.setVisible(false);
	    			panelClerkMenu.setVisible(true);
	    		}
	    	});
			quit.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			menu.dispose();
	    			System.exit(0);
	    		}
	    	});
			

			//add the panel into JFrame
			//menu.getContentPane().add(panel);
			
			//display the window
			menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			menu.pack();
			menu.setVisible(true);
			//ClerkUser.main();
		}
		else{
			System.out.println("OMG THIS SHOULD NEVER HAPPEN BUT IT DID SO HAHAHA");
			System.exit(1);
		}
	}
	

	/*
     * displays simple text interface
     */ 
    private void showUserMenu() throws ParseException
    {
    	
    	final JFrame userMenu = new JFrame("Choose user");
    	JLabel welcomeUser = new JLabel("Please choose one of the following:");
    	JButton librarian = new JButton("Librarian");
    	JButton borrower = new JButton("Borrower");
    	JButton clerk = new JButton("Clerk");	
    	JPanel panel = new JPanel(new FlowLayout());
    	
    	userMenu.getContentPane().add(panel);
    	
    	userMenu.setPreferredSize(new Dimension(350, 100));
    	panel.add(librarian);
    	panel.add(borrower);
    	panel.add(clerk);
    	userMenu.setLocationRelativeTo(null);
    	librarian.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try {
					userMenu.dispose();
					showMenu(1);//this is librarian
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    	});
    	borrower.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try {
    				userMenu.dispose();
					showMenu(2);	
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    	});
    	clerk.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			try {
					userMenu.dispose();
					showMenu(3);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		}
    	});
    	
    	panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    	
    	
    	userMenu.pack();
    	userMenu.setVisible(true);
    	
    	
    /**
     * the old stuff using console to use the program
     */
 /*
	int choice;
	boolean quit;

	quit = false;
	
	try 
	{
	    // disable auto commit mode
	    con.setAutoCommit(false);

	    while (!quit)
	    {
		System.out.print("\n\nPlease choose one of the following: \n");
		System.out.print("1.  Borrower\n");
		System.out.print("2.  Clerk\n");
		System.out.print("3.  Librarian\n");
		System.out.print("4.  Quit\n>>");

		choice = Integer.parseInt(in.readLine());
		
		System.out.println(" ");

		switch(choice)
		{
		   case 1:  BorrowerUser.main(); break;
		   case 2:  ClerkUser.main(); break;
		   case 3:  LibrarianUser.main(); break;
		   case 4:  quit = true; 
		}
	    }

	    con.close();
        in.close();
	    System.out.println("\nGood Bye!\n\n");
	    System.exit(0);
	}
	catch (IOException e)
	{
	    System.out.println("IOException!");

	    try
	    {
		con.close();
		System.exit(-1);
	    }
	    catch (SQLException ex)
	    {
		 System.out.println("Message: " + ex.getMessage());
	    }
	}
	catch (SQLException ex)
	{
	    System.out.println("Message: " + ex.getMessage());
	}
*/
    }

    public static void main(String args[])
    {
    	Main m = new Main();
    }
}

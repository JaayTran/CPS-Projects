import java.util.*;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.awt.*; 
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.text.SimpleDateFormat;

public class AppointmentFrame extends JFrame {

    private static final int FRAME_WIDTH = 800; //constant for width of the JFrame;
    private static final int FRAME_HEIGHT = 700; //constant for height of the JFrame;

    private static final int PANEL_WIDTH = FRAME_WIDTH/2; //constant for width of main JPanels;
    private static final int PANEL_HEIGHT = FRAME_HEIGHT/3; //constant for height of main JPanels;

    private static final int MINUTE_MAX = 59; //the largest minute value;
    private static final int HOUR_MAX = 23; //the largest hour value; 
    private static final int MONTH_MAX = 12; //the largest month value;
    private static final int DAY_MAX = 31; //the largest day value for January, March, May, July, August, October, and December;
    private static final int DAY_MAX_TWO = 30; //the largerst day value for April, June, September, and November;
    private static final int FEB_MAX = 28; //the largest day value for February;
    private static final int FEB_LEAP_MAX = 29; //the largest day value for February on a leap year;

    private static String FILE = "contacts.txt";

    //private instance variables
    private ArrayList<JButton> buttonList;
    private ArrayList<Calendar> calList;
    private Stack<Appointment> stack;
    private Contacts contacts;
    private ArrayList<Appointment> appointments;
    private Calendar calendar;
    private SimpleDateFormat sdf, sdfTwo;
    private JLabel label, labelTwo, day, month, year, hour, minute, sun, mon, tue, wed, thu, fri, sat, lastNameLabel, firstNameLabel, telLabel, emailLabel, addressLabel;
    private JPanel bigPanel, mainPanel, sidePanel, monthViewPanel, dispPanel, controlPanel, controlPanelTwo, date, arrows, dayMonthYear, show, action, hourMinute, createOrCancel, desc, week, contact, info, infoTwo, contactButtons, days;
    private JTextArea textArea, descArea;
    private JTextField dayField, monthField, yearField, hourField, minuteField, lastNameField, firstNameField, telField, emailField, addressField;
    private JScrollPane scrollPane;
    private JButton left, right, showButton, create, cancel, find, clear, recall;

    /**
     * Constructor method for AppointmentFrame sets up the instance variables and adds the mainPanel to the frame, itself;
     */
    public AppointmentFrame() {

        stack = new Stack<Appointment>();
        contacts = new Contacts();
        appointments = new ArrayList<Appointment>(); //arrayList to contain Appointment objects;
        calendar = Calendar.getInstance(); //Gets the current date;

        sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");
        label = new JLabel(sdf.format(calendar.getTime())); 

        sdfTwo = new SimpleDateFormat("MMM");
        labelTwo = new JLabel(sdfTwo.format(calendar.getTime()));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(label, BorderLayout.NORTH); 

        createDisplayPanel(); //calls createDisplayPanel
        createControlPanel(); //calls createControlPanel

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(labelTwo, BorderLayout.NORTH);

        createMonthViewPanel(); //calls createMonthViewPanel;
        createControlPanelTwo(); //calls createControlPanelTwo;

        bigPanel = new JPanel();
        bigPanel.setLayout(new BorderLayout());
        bigPanel.add(mainPanel, BorderLayout.WEST);
        bigPanel.add(sidePanel);

        this.add(bigPanel); 
        setSize(FRAME_WIDTH, FRAME_HEIGHT); //sets size of the AppointmentFrame

        try {
            contacts.readContactsFile(FILE);

        } catch (IOException e) {
            descArea.setText("Error! Failed to read contacts.");
        }
    }

    /**
     * Method that generates buttons for the month view panel and adds them to the days panel, which is added to the month view panel;
     */
    public void genButtons() {
        days.removeAll(); //removes all buttons from the days panel;

        buttonList = new ArrayList<JButton>(); //an array list for the buttons;
        calList = new ArrayList<Calendar>(); //an array list for dates corresponding to the buttons;

        int year = calendar.get(Calendar.YEAR); //the current year; 
        int month = calendar.get(Calendar.MONTH); //the current month
        int day = calendar.get(Calendar.DAY_OF_MONTH); //the current day

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); //how many days there are in the current month;

        for (int i = 1; i <= daysInMonth; i++) {
            calList.add(new GregorianCalendar(year,month,i));

            int y = calList.get(i-1).get(Calendar.YEAR);
            int m = calList.get(i-1).get(Calendar.MONTH);
            int d = calList.get(i-1).get(Calendar.DAY_OF_MONTH);

            JButton button = new JButton(""+i);
            ActionListener dayListen = new dayListener(y, m, d);
            button.addActionListener(dayListen);

            if (y == year && m == month && d == day) {
                button.setBackground(Color.RED);
                button.setOpaque(true);
            }
            buttonList.add(button);
        }

        int start = calList.get(0).get(Calendar.DAY_OF_WEEK); //the day of the week of the first day of the month;

        //for days before the first of the month empty panels are added instead;
        for (int p = 1; p < start; p++) {
            days.add(new JPanel()); 
        }

        //buttons are added to the days panel;
        for (JButton button : buttonList) {
            days.add(button);
        }

        days.repaint(); //repaints the days panel;
    }   

    /**
     * Method that creates the month view panel and adds it onto the side panel;
     */
    public void createMonthViewPanel() {
        monthViewPanel = new JPanel();
        monthViewPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        monthViewPanel.setLayout(new BorderLayout());

        week = new JPanel();
        week.setLayout(new GridLayout());

        days = new JPanel();
        days.setLayout(new GridLayout(0,7));
        genButtons();

        sun = new JLabel("Sun");
        mon = new JLabel("Mon");
        tue = new JLabel("Tue");
        wed = new JLabel("Wed");
        thu = new JLabel("Thu");
        fri = new JLabel("Fri");
        sat = new JLabel("Sat");

        week.add(sun);
        week.add(mon);
        week.add(tue);
        week.add(wed);
        week.add(thu);
        week.add(fri);
        week.add(sat);

        monthViewPanel.add(week, BorderLayout.NORTH);
        monthViewPanel.add(days);
        sidePanel.add(monthViewPanel, BorderLayout.CENTER);
    }

    /**
     * Method that creates the second control panel and adds it onto the side panel;
     */
    public void createControlPanelTwo() {
        controlPanelTwo = new JPanel();
        controlPanelTwo.setLayout(new BorderLayout());

        controlPanelTwo.add(createContactPanel(), BorderLayout.NORTH);
        controlPanelTwo.add(createDescPanel(), BorderLayout.SOUTH);

        sidePanel.add(controlPanelTwo, BorderLayout.SOUTH);
    }

    /**
     * Method that creates the contact panel and returns it to be added onto the second control panel;
     */
    public JPanel createContactPanel() {
        contact = new JPanel();
        contact.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        contact.setBorder(new TitledBorder(new EtchedBorder(), "Contact"));
        contact.setLayout(new BorderLayout());

        info = new JPanel();
        info.setPreferredSize(new Dimension(PANEL_WIDTH/2, PANEL_HEIGHT/2));
        info.setLayout(new GridLayout(4,2));

        infoTwo = new JPanel();
        infoTwo.setPreferredSize(new Dimension(PANEL_WIDTH/4, PANEL_HEIGHT/4));
        infoTwo.setLayout(new GridLayout(2,1));

        contactButtons = new JPanel();
        contactButtons.setPreferredSize(new Dimension(PANEL_WIDTH/4, PANEL_HEIGHT/4));

        find = new JButton("Find");
        ActionListener findListen = new findListener();
        find.addActionListener(findListen);

        clear = new JButton("Clear");
        ActionListener clearListen = new clearListener();
        clear.addActionListener(clearListen);

        lastNameLabel = new JLabel("Last Name");
        firstNameLabel = new JLabel("First Name");
        telLabel = new JLabel("tel");
        emailLabel = new JLabel("email");
        addressLabel = new JLabel("address");

        lastNameField = new JTextField();
        firstNameField = new JTextField();
        telField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();

        info.add(lastNameLabel);
        info.add(firstNameLabel);
        info.add(lastNameField);
        info.add(firstNameField);
        info.add(telLabel);
        info.add(emailLabel);
        info.add(telField);
        info.add(emailField);

        infoTwo.add(addressLabel);
        infoTwo.add(addressField);

        contactButtons.add(find);
        contactButtons.add(clear);

        contact.add(info, BorderLayout.NORTH);
        contact.add(infoTwo, BorderLayout.CENTER);
        contact.add(contactButtons, BorderLayout.SOUTH);

        return contact;
    }

    /**
     * Method that creates the display panel and adds it onto the main panel;
     */
    public void createDisplayPanel() {
        final int ROWS = 9; //constant for rows of JTextArea;
        final int COLS = 34; //constant for columns of JTextArea;

        dispPanel = new JPanel();
        dispPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        textArea = new JTextArea(ROWS, COLS);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        dispPanel.add(scrollPane);
        
        mainPanel.add(dispPanel, BorderLayout.CENTER);
    }

    /**
     * Method that creates the control panel and adds it onto the main panel;
     */
    public void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        controlPanel.add(createDatePanel(), BorderLayout.NORTH);
        controlPanel.add(createActionPanel(), BorderLayout.SOUTH);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Method that creates the date panel and adds it onto the control panel;
     */
    public JPanel createDatePanel() {
        final int FIELD_SIZE = 4;

        date = new JPanel();
        date.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        date.setLayout(new BorderLayout());
        date.setBorder(new TitledBorder(new EtchedBorder(), "Date"));

        arrows = new JPanel();
        arrows.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT/3));

        left = new JButton("<");
        ActionListener leftListen = new leftListener();
        left.addActionListener(leftListen);

        right = new JButton(">");
        ActionListener rightListen = new rightListener();
        right.addActionListener(rightListen);

        arrows.add(left);
        arrows.add(right);
        date.add(arrows, BorderLayout.NORTH);

        dayMonthYear = new JPanel();
        
        day = new JLabel("Day");
        dayField = new JTextField(FIELD_SIZE);
        month = new JLabel("Month");
        monthField = new JTextField(FIELD_SIZE);
        year = new JLabel("Year");
        yearField = new JTextField(FIELD_SIZE);

        dayMonthYear.add(day);
        dayMonthYear.add(dayField);
        dayMonthYear.add(month);
        dayMonthYear.add(monthField);
        dayMonthYear.add(year);
        dayMonthYear.add(yearField);
        date.add(dayMonthYear, BorderLayout.CENTER);

        show = new JPanel();

        showButton = new JButton("Show");
        ActionListener listener = new showListener();
        showButton.addActionListener(listener);

        show.add(showButton);
        date.add(show, BorderLayout.SOUTH);

        return date;
    }

    /**
     * Method that creates the action panel and adds it onto the control panel;
     */
    public JPanel createActionPanel() {
        final int FIELD_SIZE = 4;

        action = new JPanel();
        action.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        action.setLayout(new BorderLayout());
        action.setBorder(new TitledBorder(new EtchedBorder(), "Appointment"));

        hourMinute = new JPanel();

        hour = new JLabel("Hour");
        hourField = new JTextField(FIELD_SIZE);
        minute = new JLabel("Minute");
        minuteField = new JTextField(FIELD_SIZE);

        hourMinute.add(hour);
        hourMinute.add(hourField);
        hourMinute.add(minute);
        hourMinute.add(minuteField);
        action.add(hourMinute, BorderLayout.NORTH);

        createOrCancel = new JPanel();
        createOrCancel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT/3));

        create = new JButton("CREATE");
        ActionListener createListen = new createListener();
        create.addActionListener(createListen);

        cancel = new JButton("CANCEL");
        ActionListener cancelListen = new cancelListener();
        cancel.addActionListener(cancelListen);

        recall = new JButton("RECALL");
        ActionListener recallListen = new recallListener();
        recall.addActionListener(recallListen);

        createOrCancel.add(create);
        createOrCancel.add(cancel);
        createOrCancel.add(recall);
        action.add(createOrCancel, BorderLayout.SOUTH);

        return action;
    }

    /**
     * Method that creates the description panel and adds it onto the control panel;
     */
    public JPanel createDescPanel() {
        final int D_ROWS = 5; //constant for rows of the description area;
        final int D_COLS = 20; //constant for columns of the description area;

        desc = new JPanel();
        desc.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        descArea = new JTextArea(D_ROWS, D_COLS);
        desc.setBorder(new TitledBorder(new EtchedBorder(), "Description"));
        desc.add(descArea);

        return desc;
    }

    /**
     * Method that prints all appointments that occur on the current day month and year;
     */
    public void printAppointments() {
        textArea.setText(""); //resets the text area;
        descArea.setText(""); //resets the description area;
        hourField.setText(""); //resests hourField;
        minuteField.setText(""); //resets minuteField
        Collections.sort(appointments); //sort the appointments arrayList;
        for (Appointment appointment : appointments) {
            int year = appointment.getDate().get(Calendar.YEAR);
            int month = appointment.getDate().get(Calendar.MONTH);
            int day = appointment.getDate().get(Calendar.DAY_OF_MONTH);
            if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) && day == calendar.get(Calendar.DAY_OF_MONTH)) {
                textArea.append(appointment.print() + "\n");
            }
        }
    }

    /**
     * Method that removes an appointment on the current day, month, year, that has the same time in the hourField and minuteField;
     */
    public void cancelAppointment() {
        boolean remove = false;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = Integer.parseInt(hourField.getText());
        int minute;
        if (minuteField.getText().equals("")){minute = 0;}
        else {minute = Integer.parseInt(minuteField.getText());}
        for (Appointment appointment : appointments) {
            if (appointment.occursOn(year, month, day, hour, minute)) {
                appointments.remove(appointment);
                removeFromStack(appointment);
                Collections.sort(appointments);
                printAppointments();
                break;
            }
        }
    }

    /**
     * Method that creates and adds appointments to the arrayList if they do not already exist, otherwise prints "CONFLICT!!" in the description area;
     */
    public void createAppointment() {
        boolean conflict = false;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour;
        int minute;

        if (hourField.getText().equals("")){hour = 0;}
        else {hour = Integer.parseInt(hourField.getText());}

        if (minuteField.getText().equals("")){minute = 0;}
        else {minute = Integer.parseInt(minuteField.getText());}

        if (minute < 0 || minute > MINUTE_MAX || hour < 0 || hour > HOUR_MAX) {
            descArea.setText("Invalid information entered");
        }

        else {
            String text = descArea.getText();
            String lName = lastNameField.getText();
            String fName = firstNameField.getText();
        
            for (Appointment appointment : appointments) {
                if (appointment.occursOn(year, month, day, hour, minute)) {
                    descArea.setText("CONFLICT!!");
                    conflict = true;
                    break;
                }
            }

            if (!conflict) { 
                Appointment newApp = new Appointment(year, month, day, hour, minute, text);
                if (!(lName.equals("")) && !(fName.equals(""))) {
                    String telephone = telField.getText();
                    String email = emailField.getText();
                    String address = addressField.getText();
                    Person per = new Person(lName, fName, address, telephone, email);
                    newApp.setPerson(per);
                }
                appointments.add(newApp);
                stack.push(newApp);
                Collections.sort(appointments);
                printAppointments();
            }
        }
    }

    /**
     * Method that finds a person in the contacts array list and returns it;
     */
    public void findPerson() {

        String lName = lastNameField.getText();
        String fName = firstNameField.getText();
        String tel = telField.getText();
        String email = emailField.getText(); 
        
        if (!(lName.equals("")) && !(fName.equals("")) && tel.equals("") && email.equals("")) {
            Person curr = contacts.findPersonByFullName(lName, fName);
            if (curr != null) {
                telField.setText(curr.getTelephone());
                emailField.setText(curr.getEmail());
                addressField.setText(curr.getAddress());
            }

            else {
                descArea.setText("Contact not found");
            }
        }

        else if (lName.equals("") && fName.equals("") && !(tel.equals("")) && email.equals("")) {
            Person curr = contacts.findPersonByTelephone(tel);
            if (curr != null) {
                lastNameField.setText(curr.getLastName());
                firstNameField.setText(curr.getFirstName());
                emailField.setText(curr.getEmail());
                addressField.setText(curr.getAddress());
            }

            else {
                descArea.setText("Contact not found");
            }
        }

        else if (lName.equals("") && fName.equals("") && tel.equals("") && !(email.equals(""))) {
            Person curr = contacts.findPersonByEmail(email);
            if (curr != null) {
                lastNameField.setText(curr.getLastName());
                firstNameField.setText(curr.getFirstName());
                telField.setText(curr.getTelephone());
                addressField.setText(curr.getAddress());
            }

            else {
                descArea.setText("Contact not found");
            }
        }

        else {
            descArea.setText("Cannot search\nPlease enter only:\nemail, telephone, or last and first name");
        }
    }

    /**
     * Method that removes any from the stack;
     * @param app       the appointment to be removed;
     */
    public void removeFromStack(Appointment app) {
        ArrayList<Appointment> temp = new ArrayList<Appointment>();
        while (!stack.isEmpty()) {
            temp.add(stack.pop());
        }
        for (int i = temp.size()-1; i >=0; i--) {
            if (app.compareTo(temp.get(i)) != 0) {
                stack.push(temp.get(i));
            }
        }
    }

    /**
     * Method that shows appointments on the current date;
     * @param year        the year of the current date;
     * @param month       the month of the current date;
     * @param day         the day of the current date;
     */
    public void showApps(int year, int month, int day) {
            int yr = calendar.get(Calendar.YEAR);
            int mth = calendar.get(Calendar.MONTH);
            int dy = calendar.get(Calendar.DAY_OF_MONTH);
            if (year == yr && month == mth && day == dy) {
                calendar.set(year, month, day);
                label.setText(sdf.format(calendar.getTime()));
                labelTwo.setText(sdfTwo.format(calendar.getTime()));
                printAppointments();
            }
            else {
                calendar.set(year, month, day);
                genButtons(); //generates buttons in the month view panel;
                label.setText(sdf.format(calendar.getTime()));
                labelTwo.setText(sdfTwo.format(calendar.getTime()));
                printAppointments();
            }
    }

    /**
     * Method that checks if a year is a leap year and returns true if it is a leap year and false if it isn't a leap year;
     * @param year      the year that the method checks;
     */
    public boolean isLeapYear(int year) {
        if (year%4 == 0) {
            if (year%100 == 0) {
                if (year%400 == 0) {return true;}
                else {return false;}
            }
            else {return true;}
        }
        else {return false;}
    }

    class recallListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            
            Appointment topApp = stack.peek();
            int topYear = topApp.getDate().get(Calendar.YEAR);
            int topMonth = topApp.getDate().get(Calendar.MONTH);
            int topDay = topApp.getDate().get(Calendar.DAY_OF_MONTH);
            int topHour = topApp.getDate().get(Calendar.HOUR_OF_DAY);
            int topMinute = topApp.getDate().get(Calendar.MINUTE);
            String desc = topApp.getDescription();

            showApps(topYear, topMonth, topDay);
            
            descArea.setText(desc);
            hourField.setText(""+topHour);
            minuteField.setText(""+topMinute);
        }
    }

    class clearListener implements ActionListener {
        
        public void actionPerformed(ActionEvent event) {
            descArea.setText("");
            lastNameField.setText("");
            firstNameField.setText("");
            telField.setText("");
            emailField.setText("");
            addressField.setText("");
        }
    }

    class findListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            findPerson();
        }
    }

    class createListener implements ActionListener {
    /**
     * Method that calls createAppointment;
     * @see createAppointment;
     */
        public void actionPerformed(ActionEvent event) {
            createAppointment();
        }
    }
    
    
    class cancelListener implements ActionListener {
     /**
     * Method that calls cancelAppointment;
     * @see cancelAppointment;
     */
        public void actionPerformed(ActionEvent event) {
            cancelAppointment();
        }
    }

    class leftListener implements ActionListener {
     /**
     * Method that calls cancelAppointment;
     * @see cancelAppointment;
     */
        public void actionPerformed(ActionEvent event) {
            calendar.add(Calendar.DATE, -1);
            label.setText(sdf.format(calendar.getTime()));
            labelTwo.setText(sdfTwo.format(calendar.getTime()));
            genButtons(); //generates buttons in the month view panel;
            printAppointments();
            
        }
    }

    class rightListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            calendar.add(Calendar.DATE, +1);
            label.setText(sdf.format(calendar.getTime()));
            labelTwo.setText(sdfTwo.format(calendar.getTime()));
            genButtons(); //generates buttons in the month view panel;
            printAppointments();
            
        }
    }

    class dayListener implements ActionListener {
        private int theYear, theMonth, theDay; //private instance variables for the year, month, and day;

        public dayListener(int year, int month, int day) {
            theYear = year;
            theMonth = month;
            theDay = day;
        }

        public void actionPerformed(ActionEvent event) {
            showApps(theYear, theMonth, theDay);
        }
    }

    class showListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (dayField.getText().equals("") || monthField.getText().equals("") || yearField.getText().equals("")) {
                descArea.setText("Invalid information entered");
            }
            else {
                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
            

                if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 0 && day <= DAY_MAX && year > 0) {
                    showApps(year, month-1, day);
                
                }
                else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 0 && day <= DAY_MAX_TWO && year > 0) {
                    showApps(year, month-1, day);
                
                }
                else if (month == 2 && isLeapYear(year) && day > 0 && day <= FEB_LEAP_MAX && year > 0) {
                    showApps(year, month-1, day);
                
                }
                else if (month == 2 && !isLeapYear(year) && day > 0 && day <= FEB_MAX && year > 0) {
                    showApps(year, month-1, day);
                
                }
                else {
                    descArea.setText("Invalid information entered");
                }
            }
        }
    } 
}
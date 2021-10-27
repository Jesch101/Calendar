import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimpleCalendarTester {

    /**
     * Main class that sets up model and
     * @param args
     */
    public static void main(String[] args)
    {

        //LABELS AND TEXT FIELDS
        JLabel label = new JLabel("Enter date info here.");
        label.setFont(new Font("Open Sans", Font.BOLD, 12));
        label.setBounds(50, 25, 300, 25);
        label.setVisible(false);

        JLabel label2 = new JLabel("to");
        label2.setFont(new Font("Open Sans", Font.PLAIN, 13));
        label2.setBounds(200, 78, 20, 25);
        label2.setVisible(false);

        JLabel errorLabel = new JLabel("Error: There is a conflict with another event!");
        errorLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        errorLabel.setBounds(50, 200, 300, 25);
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);

        JTextField eventName = new JTextField("");
        eventName.setFont(new Font("Open Sans", Font.BOLD, 12));
        eventName.setBounds(50, 50, 300, 25);
        eventName.setVisible(false);

        JTextField dateInfo = new JTextField("");
        dateInfo.setFont(new Font("Open Sans", Font.PLAIN, 11));
        dateInfo.setBounds(50, 78, 70, 25);
        dateInfo.setVisible(false);

        JTextField startTime = new JTextField("");
        startTime.setFont(new Font("Open Sans", Font.PLAIN, 11));
        startTime.setBounds(125, 78, 70, 25);
        startTime.setVisible(false);

        JTextField endTime = new JTextField("");
        endTime.setFont(new Font("Open Sans", Font.PLAIN, 11));
        endTime.setBounds(220, 78, 70, 25);
        endTime.setVisible(false);

        //BUTTONS
        Button quit = new Button("QUIT");
        quit.setBounds(700, 120, 60, 30);
        quit.setFocusable(false);
        quit.setFont(new Font("Open Sans", Font.BOLD, 12));
        quit.setVisible(true);

        Button create = new Button("CREATE");
        create.setBounds(150, 120, 100, 40);
        create.setBackground(Color.red);
        create.setForeground(Color.white);
        create.setFocusable(false);
        create.setFont(new Font("Open Sans", Font.BOLD, 12));

        Button back = new Button("<");
        back.setBounds(448, 120, 30, 30);
        back.setFont(new Font("Open Sans", Font.BOLD, 12));
        back.setFocusable(false);
        back.setVisible(true);

        Button next = new Button(">");
        next.setBounds(480, 120, 30, 30);
        next.setFont(new Font("Open Sans", Font.BOLD, 12));
        next.setFocusable(false);
        next.setVisible(true);

        Button save = new Button("Save");
        save.setBounds(300, 78, 50, 25);
        save.setFont(new Font("Open Sans", Font.BOLD, 12));
        save.setBackground(Color.LIGHT_GRAY);
        save.setFocusable(false);
        save.setVisible(false);

        Button exit = new Button("Close");
        exit.setBounds(310, 22,40,25);
        exit.setFont(new Font("Open Sans", Font.BOLD, 12));
        exit.setBackground(Color.LIGHT_GRAY);
        exit.setFocusable(false);
        exit.setVisible(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame calFrame = new JFrame("Calendar");
        calFrame.setLayout(new BorderLayout());
        calFrame.setSize(new Dimension(screenSize.width/2, screenSize.height/2));
        calFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calFrame.setLocationRelativeTo(null);

        //Event frames
        JFrame eventFrame = new JFrame("Event info");
        eventFrame.setLayout(new BorderLayout());
        eventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eventFrame.setSize(new Dimension(screenSize.width/4, screenSize.height/4));
        eventFrame.setLocationRelativeTo(calFrame);
        eventFrame.setVisible(false);
        label.setVisible(true);
        label2.setVisible(true);
        eventName.setVisible(true);
        dateInfo.setVisible(true);
        startTime.setVisible(true);
        endTime.setVisible(true);
        save.setVisible(true);
        exit.setVisible(true);
        errorLabel.setVisible(false);

        calFrame.add(create);
        calFrame.add(back);
        calFrame.add(next);
        calFrame.add(quit);
        eventFrame.add(save);
        eventFrame.add(exit);
        eventFrame.add(label);
        eventFrame.add(label2);
        eventFrame.add(eventName);
        eventFrame.add(dateInfo);
        eventFrame.add(startTime);
        eventFrame.add(endTime);
        eventFrame.add(errorLabel);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setVisible(true);
        calFrame.add(p);

        calFrame.setVisible(true);


        MyCalendar cal = new MyCalendar();
        View v = new View(cal);
        v.setVisible(true);
        calFrame.add(v);
        cal.attach(v);


        create.addActionListener(e -> {

            eventFrame.setVisible(true);
            exit.addActionListener(l -> {
                eventFrame.setVisible(false);
            });


            save.addActionListener(l -> {
                boolean conflict = false;
                String name = eventName.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate date;
                date = LocalDate.parse(dateInfo.getText(), formatter);

                LocalTime st;
                st = LocalTime.parse(startTime.getText());

                LocalTime et;
                et = LocalTime.parse(endTime.getText());

                Event newEvent  = new Event(name, new TimeInterval(st,et,date));
                if(cal.hasConflict(newEvent))
                {
                    conflict = true;
                    errorLabel.setVisible(true);
                }
                else
                {
                    cal.addSingleEvent(newEvent);
                }
                if(!conflict) {
                    eventFrame.setVisible(false);
                }

            });

            eventName.setText("");
            startTime.setText("");
            dateInfo.setText(cal.getSelectedDateStr());
            endTime.setText("");
        });

        quit.addActionListener(e -> { ;
            cal.saveEvents();
            System.exit(0);
        });



        //CHANGING MONTH VIEW
        back.addActionListener(e -> {
            cal.setOffset(-1);
            cal.clicked(false);
            cal.setClickCoords(0,0);
            cal.setInitialScreen(false);
        });

        next.addActionListener(e -> {
            cal.setOffset(1);
            cal.clicked(false);
            cal.setClickCoords(0,0);
            cal.setInitialScreen(false);
        });

    }

}

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Scanner;

/**
 * Defines underlying data structure that holds events
 * @author Jeremy Esch
 * @version 1.0
 */
public class MyCalendar implements Comparable<Event>{

    private ArrayList<Event> recurringEvents;
    private ArrayList<Event> singleEvents;
    private ArrayList<Event> allEvents;

    ChangeListener l;

    public int offset = 0;
    public boolean sorted = true;
    public boolean newClick = false;
    public int clickX = 0;
    public int clickY = 0;
    public LocalDate clickedMonth;
    public int selectedDay;
    public boolean initialScreen = true;
    public boolean firstOutsideClick = true;

    /**
     * Initializes ArrayLists containing event objects
     */
    public MyCalendar()
    {
        recurringEvents = new ArrayList<Event>();
        singleEvents = new ArrayList<Event>();
        allEvents = new ArrayList<Event>();
    }

    /**
     * Attaches change listener to Calendar object
     * @param cl
     */
    public void attach(ChangeListener cl)
    {
        l = cl;
    }


    /**
     * String representation of Calendar date
     * @param val offset to get accurate return date
     * @return String including month and year
     */
    public String getCalHeader(int val)
    {
        LocalDate cal = LocalDate.now().plusMonths(val);

        return cal.getMonth().toString() + " " + cal.getYear();
    }

    /**
     * Gets ArrayList of all events
     * @return ArrayList of allEvents
     */
    public ArrayList<Event> getAllEvents()
    {
        Collections.sort(allEvents);
        return allEvents;
    }



    /**
     * Returns string for date given active month, date, and year. Used for event creation frame
     * @return String representation of active date
     */
    public String getSelectedDateStr()
    {
        String str = "";
        if(this.getClickedMonth() != null)
        {

            if(this.getClickedMonth().getMonthValue() < 10)
            {
                if(selectedDay < 10)
                {
                    str = "0"+ this.getClickedMonth().getMonthValue() + "/" + "0" + selectedDay + "/" + this.getClickedMonth().getYear();
                }
                else
                {
                    str = "0"+ this.getClickedMonth().getMonthValue() + "/" + selectedDay + "/" + this.getClickedMonth().getYear();
                }
            }
            else
            {
                if(selectedDay < 10)
                {
                    str = this.getClickedMonth().getMonthValue() + "/" + "0" + selectedDay + "/" + this.getClickedMonth().getYear();
                }
                else
                {
                    str = this.getClickedMonth().getMonthValue() + "/" + selectedDay + "/" + this.getClickedMonth().getYear();
                }
            }

            return str;
        }
        if(LocalDate.now().getMonthValue() < 10)
        {
            if(LocalDate.now().getDayOfMonth() < 10)
            {
                str = "0" + LocalDate.now().getMonthValue() + "/" + "0" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getYear();
            }
            else
            {
                str = "0" + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getYear();
            }
        }
        else
        {
            if(LocalDate.now().getDayOfMonth() < 10)
            {
                str = LocalDate.now().getMonthValue() + "/" + "0" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getYear();
            }
            else
            {
                str = LocalDate.now().getMonthValue() + "/" + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getYear();
            }
        }
        return str;
    }

    /**
     * Sets day that is selected
     * @param x Int representation of day of month
     */
    public void setSelectedDay(int x)
    {
        selectedDay = x;
    }

    /**
     * If active screen is starting screen
     * @return Boolean value if the active frame is displaying initial startup screen
     */
    public boolean isInitialScreen()
    {
        return initialScreen;
    }

    /**
     * Setter for initialScreen boolean value
     * @param x Boolean value to set initialScreen to
     */
    public void setInitialScreen(boolean x)
    {
        initialScreen = x;
    }

    /**
     * Assigns LocalDate object as active month
     * @param d LocalDate object
     */
    public void setClickedMonth(LocalDate d)
    {
        clickedMonth = d;
    }

    /**
     * Gets active month from click
     * @return LocalDate object
     */
    public LocalDate getClickedMonth()
    {
        return clickedMonth;
    }

    /**
     * Sets x and y coordinates for where the click was
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setClickCoords(int x, int y)
    {
        clickX = x;
        clickY = y;
    }

    /**
     * Gets click x coordinate
     * @return click x coordinate
     */
    public int getClickX()
    {
        return clickX;
    }

    /**
     * Gets click y coordinate
     * @return click y coordinate
     */
    public int getClickY()
    {
        return clickY;
    }

    /**
     * Used to keep track of what month is being drawn after next or back button is clicked
     * @param x Month offset int
     */
    public void setOffset(int x)
    {
        offset+= x;
        l.stateChanged(new ChangeEvent(this));
    }

    /**
     * Gets month offset int
     * @return Offset int
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * Used to set if click has happened on active screen
     * @param x Boolean if click happened
     */
    public void clicked(boolean x)
    {
        newClick = x;
    }

    /**
     * Used to determine if click happened on the active screen
     * @return Boolean newClick
     */
    public boolean isNewClick()
    {
        return newClick;
    }

    /**
     * Gets day of week given int parameter
     * @param a Day of week int representation
     * @return Day of week String representation
     */
    public String getDayOfWeek(int a)
    {
        if(a == 0)
        {
            return "Sunday";
        }
        else if(a == 1)
        {
            return "Monday";
        }
        else if(a == 2)
        {
            return "Tuesday";
        }
        else if(a == 3)
        {
            return "Wednesday";
        }
        else if(a == 4)
        {
            return "Thursday";
        }
        else if(a==5)
        {
            return "Friday";
        }
        else if(a==6)
        {
            return "Saturday";
        }

        return "";
    }

    /**
     * Adds single event to ArrayLists singleEvents and allEvents
     * @param e Event to be added
     * @return True if event is added, false if there is conflict
     */
    public void addSingleEvent(Event e)
    {
        if(!this.hasConflict(e))
        {
            singleEvents.add(e);
            allEvents.add(e);
            sorted = false;
        }
    }

    /**
     * Checks if specified date has events that exist
     * @param ti Date to be checked
     * @return True if events exist in day, false if not
     */
    public boolean dayHasEvents(LocalDate ti)
    {
        for (Event allEvent : this.allEvents) {
            if (allEvent.getTimeInterval().getStartDate().equals(ti)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if events have conflict with one another
     * @param e Event to be compared
     * @return True if events have conflict, false if not
     */
    public boolean hasConflict(Event e)
    {
        if(!eventsExist())
        {
            return false;
        }
        for(int i = 0; i < singleEvents.size(); i++) //LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2
        {
            //Check if days are same
            if(e.getTimeInterval().getStartDate().compareTo(singleEvents.get(i).getTimeInterval().getStartDate()) == 0)
            {
                if(e.getTimeInterval().hasTimeConflict(e.getTimeInterval().getSt(),e.getTimeInterval().getEt(),
                        singleEvents.get(i).getTimeInterval().getSt(), singleEvents.get(i).getTimeInterval().getEt()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if events exist in the ArrayList
     * @return True if allEvents is not empty, false otherwise
     */
    public boolean eventsExist()
    {
        return !allEvents.isEmpty();
    }

    /**
     * Saves events stored in allEvents to events.txt
     */
    public void saveEvents()
    {
        BufferedWriter myWriter;
        ArrayList<Event> output = new ArrayList<>();
        for (Event singleEvent : singleEvents) {
            output.add(singleEvent);
        }
        for (Event recurringEvent : recurringEvents) {
            output.add(recurringEvent);
        }
        Collections.sort(output);
        try {
            myWriter = new BufferedWriter(new FileWriter("src/events.txt"));
            for(int i = 0; i < allEvents.size(); i++)
            {
                myWriter.write(allEvents.get(i).getName());
                myWriter.write(allEvents.get(i).toPrintString());
                myWriter.write("\n");
            }
            myWriter.close();
        }
        catch(IOException e)
        {
            System.out.println("Error occurred with printing output");
            e.printStackTrace();
        }

    }

    public void loadEvents()
    {
        DateTimeFormatter eventFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        Event fileEvent;
        String[] eventInfo;
        String name;

        LocalTime st;
        LocalTime et;
        LocalDate sd;
        LocalDate ed;

        try{
            File myFile = new File("src/events.txt");
            Scanner reader = new Scanner(myFile);
            while(reader.hasNextLine())
            {
                name = reader.nextLine();

                eventInfo = reader.nextLine().split(" ");
                //Catching recurring event
                if(eventInfo[0].contains("S") || eventInfo[0].contains("M") || eventInfo[0].contains("T") || eventInfo[0].contains("W") || eventInfo[0].contains("R") ||
                        eventInfo[0].contains("F") || eventInfo[0].contains("A"))
                {

                    st = LocalTime.parse(eventInfo[1]);
                    et = LocalTime.parse(eventInfo[2]);
                    sd = LocalDate.parse(eventInfo[3], eventFormatter);
                    ed = LocalDate.parse(eventInfo[4], eventFormatter);

                    fileEvent = new Event(name, new TimeInterval(st,et,sd,ed), eventInfo[0]);

                }
                else
                {
                    sd = LocalDate.parse(eventInfo[0], eventFormatter);
                    st = LocalTime.parse(eventInfo[1]);
                    et = LocalTime.parse(eventInfo[2]);
                    fileEvent = new Event(name, new TimeInterval(st,et,sd));
                }
                addSingleEvent(fileEvent);

            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Error: File events.txt not found");
            e.printStackTrace();
        }
    }

    /**
     * Implementation of comparable to compare events
     * @param o Event to be compared
     * @return 0 if events are equal, 1 if this event is after, -1 if this event is before
     */
    @Override
    public int compareTo(Event o) {
        return this.compareTo(o);
    }




}

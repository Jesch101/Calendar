import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents an event
 */
public class Event implements Comparable<Event>{

    private String name;
    private TimeInterval ti;
    private String days;

    /**
     * Constructs event object with name and time interval
     * @param name Name of event
     * @param ti Time interval of event
     */
    public Event(String name, TimeInterval ti)
    {
        this.name = name;
        this.ti = ti;
    }

    /**
     * Constructs event object with name, time interval, and days
     * @param name Name of event
     * @param ti Time interval of event
     * @param days Days of week event occurs
     */
    public Event(String name, TimeInterval ti, String days)
    {
        this.name = name;
        this.ti = ti;
        this.days = days;
    }


    /**
     * Gets name of event
     * @return Name of event
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the TimeInterval of event
     * @return TimeInterval of event
     */
    public TimeInterval getTimeInterval()
    {
        return this.ti;
    }

    /**
     * Gets days of event
     * @return String of days
     */
    public String getDays()
    {
        return days;
    }

    /**
     * Gets days of event in full word representation
     * @return Day of week
     */
    public String getStrDays() {
        String daysOfWeek = "";
        if(days.contains("S")) {
            daysOfWeek += "Sunday ";
        }
        if(days.contains("M")) {
            daysOfWeek += "Monday ";
        }
        if(days.contains("T")) {
            daysOfWeek += "Tuesday ";
        }
        if(days.contains("W")) {
            daysOfWeek += "Wednesday ";
        }
        if(days.contains("R")) {
            daysOfWeek += "Thursday ";
        }
        if(days.contains("F")) {
            daysOfWeek += "Friday ";
        }
        if(days.contains("A")) {
            daysOfWeek += "Saturday ";
        }

        return daysOfWeek;
    }

    /**
     * Returns start date of event
     * @return Start date of event
     */
    public LocalDate getEventDate()
    {
        return ti.getStartDate();
    }



    /**
     * toString implementation that checks if event is single or recurring, and prints out accordingly
     * @return String representing event object
     */
    public String toString()
    {
        if(days != null)
        {
            return name + ": " + days + " " + ti.toString();
        }
        return name + ": " + ti.toString();
    }

    /**
     * Different toString format for neater printing
     * @return String representing event
     */
    public String toPrintString()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        if(days != null) //Has null pointer exception
        {
            return "\n" + days + " " + ti.getStrSt() + " " + ti.getStrEt() + " " + ti.getStartDate().format(formatter) +  " " + ti.getEndDate().format(formatter);
        }
        return "\n" + ti.getStartDate().format(formatter) + " " + ti.getStrSt() + " " + ti.getStrEt();
    }

    /**
     * Comparable implementation that compares this event to event o
     * @param o Event to be compared to
     * @return 0 if events are equal, 1 if this event is greater than o, -1 if this event is less than o
     */
    @Override
    public int compareTo(Event o) {
        return this.ti.compareTo(o.ti);
    }
}

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * View class that takes in mouse event and updates frame accordingly
 */
public class View extends JPanel implements ChangeListener{

    private MyCalendar calendar;

    /**
     * View object that has mouse listener
     * @param cal Calendar object
     */
    public View(MyCalendar cal)
    {
        calendar = cal;
        setLayout(new FlowLayout());

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getX() >= 63 && e.getX() <= 355 && e.getY() >= 235 && e.getY() <= 514) {
                    cal.setClickCoords(e.getX(), e.getY());
                    cal.clicked(true);
                }
                cal.setClickedMonth(LocalDate.now());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    /**
     * Paints calendar and events onto jframe
     * @param g Graphics object
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        CalendarView cal = new CalendarView(calendar);
        calendar.loadEvents();
        cal.drawCalendar(g2);
        cal.drawEvents(g2);
    }

    /**
     * Repaints canvas when called
     * @param e Change event object
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        repaint();
    }


    /**
     * Manages the entirety of the calendar drawing
     */
    public class CalendarView
    {
        private MyCalendar c;
        public int daySelected = LocalDate.now().getDayOfMonth();
        int offset = 0;
        int dayOfWeek = 0;
        int initialDayOfWeek = 0;

        /**
         * Passing in calendar object
         * @param c MyCalendar object
         */
        public CalendarView(MyCalendar c)
        {
            this.c = c;
        }

        /**
         * Draws the active calendar based on user interaction
         * @param g2 Graphics 2D component
         */
        public void drawCalendar(Graphics2D g2)
        {
            //Calculating the center to place month name
            offset = c.getOffset();
            String calHeader = c.getCalHeader(offset);
            AffineTransform at = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(at,true,true);
            Font font = new Font("Times New Roman", Font.BOLD, 25);
            int textWidth = (int)(font.getStringBounds(calHeader, frc).getWidth());
            g2.setFont(font);
            int center = 200 - (textWidth/2);
            g2.drawString(calHeader,center, 195);

            LocalDate today = LocalDate.now();
            initialDayOfWeek = today.getDayOfWeek().getValue();
            if(initialDayOfWeek == 7)
            {
                initialDayOfWeek = 0;
            }

            //Drawing days
            LocalDate cal = LocalDate.now().plusMonths(offset);
            LocalDate start = LocalDate.of(cal.getYear(), cal.getMonth(), 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            int firstDay = start.getDayOfWeek().getValue();
            int lastDay = end.getDayOfMonth();
            boolean firstDayPassed = false;
            int dayCounter = 1;
            int x=75,y=260;
            //Drawing days of week
            g2.setFont(new Font("Open Sans", Font.BOLD, 18));
            g2.drawString("S",x,y-35);
            g2.drawString("M",x+38,y-35);
            g2.drawString("T",x+38*2,y-35);
            g2.drawString("W",x+38*3,y-35);
            g2.drawString("R",x+38*4,y-35);
            g2.drawString("F",x+38*5,y-35);
            g2.drawString("A",x+38*6,y-35);
            //Drawing calendar days
            if(firstDay == 7)
            {
                firstDay = 0;
            }

            int x1,x2,y1,y2;

            for(int i = 0; i < 6; i++) //weeks
            {
                y1 = y-25;
                y2 = y+13;
                for(int j = 0; j < 7; j++)//days
                {

                    x1 = x-12;
                    x2 = x+26;
                    if((!firstDayPassed) && firstDay != j)
                    {
                        x+=38;
                    }
                    else{
                        if(c.getClickX() >= x1 && c.getClickX() <= x2 && c.getClickY() >= y1 && c.getClickY() <= y2){
                            daySelected = dayCounter;
                            c.setSelectedDay(daySelected);
                            LocalDate monthOfClick = LocalDate.now().plusMonths(offset);
                            c.setClickedMonth(monthOfClick);
                            c.clicked(true);
                            c.setInitialScreen(false);
                            dayOfWeek = j;
                        }

                        if((dayCounter == daySelected) && c.isNewClick() && c.getClickedMonth().equals(LocalDate.now().plusMonths(offset)))
                        {
                            g2.setColor(Color.lightGray);
                            g2.fillRect(x-12,y-25,38,38);
                            c.clicked(false);
                        }
                        else{
                            g2.drawRect(x-12,y-25,38,38);
                        }
                        g2.setColor(Color.black);
                        g2.drawString(String.valueOf(dayCounter), x, y);

                        repaint();

                        dayCounter++;
                        x+=38;
                        firstDayPassed = true;
                    }
                    if(dayCounter -1 == lastDay)
                    {
                        break;
                    }
                }
                if(dayCounter -1 == lastDay)
                {
                    break;
                }

                x = 75;
                y+=38;
            }
        }

        /**
         * Draws events given active selected date on calendar
         * @param g2 Graphics2D component
         */
        public void drawEvents(Graphics2D g2)
        {
            int x=450,y=235;
            if(c.isInitialScreen())
            {
                //Print out current day's events
                g2.drawString(c.getDayOfWeek(initialDayOfWeek) + " " + LocalDate.now().getMonth().getValue() + "/" + String.valueOf(daySelected),x,y);
            }
            else
            {
                if(c.getClickedMonth() != null) {
                    if (c.getClickedMonth().equals(LocalDate.now().plusMonths(offset))) {
                        g2.drawString(c.getDayOfWeek(dayOfWeek) + " " + String.valueOf(c.getClickedMonth().getMonthValue()) + "/" + String.valueOf(daySelected), x, y);
                        LocalDate day = LocalDate.of(c.getClickedMonth().getYear(),c.getClickedMonth().getMonthValue(), daySelected);
                        if(c.dayHasEvents(day))
                        {
                            for(int i = 0; i < c.getAllEvents().size(); i++)
                            {
                                if(c.getAllEvents().get(i).getEventDate().equals(day))
                                {
                                    g2.setFont(new Font("Open Sans", Font.PLAIN, 15));
                                    y+=18;
                                    g2.drawString(c.getAllEvents().get(i).toString(), x, y);
                                    g2.setFont(new Font("Open Sans", Font.BOLD, 18));
                                }
                            }
                        }
                    }
                }
            }
            repaint();

        }

    }
}



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Appointment implements Comparable<Appointment> {
    private Calendar date;
    private String description;
    private Person person;

    /**
     * Constructor method that initializes the date and description instance variables;
     * @param year        the year the appointment is on
     * @param month       the month the appointment is on
     * @param day         the day the appointment is on
     * @param hour        the hour the appointment is on
     * @param minute      the minute the appointment is on
     * @param description the description of the appointment
     *
     */
    public Appointment(int year, int month, int day, int hour, int minute, String description) {
        date = new GregorianCalendar(year,month,day,hour,minute);
        this.description = description;
        this.person = new Person();
    }

    public Appointment(int year, int month, int day, int hour, int minute, String description, String lastName, String firstName, String telephone, String address, String email) {
        date = new GregorianCalendar(year,month,day,hour,minute);
        this.description = description;
        this.person = new Person(lastName, firstName, telephone, address, email);
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person.setLastName(person.getLastName());
        this.person.setFirstName(person.getFirstName());
        this.person.setTelephone(person.getTelephone());
        this.person.setAddress(person.getAddress());
        this.person.setEmail(person.getEmail());
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String newDesc) {
        description = newDesc;
    }

    public Calendar getDate() {
        return date;
    }
    public void setDate(int year, int month, int day, int hour, int minute) {
        date = new GregorianCalendar(year,month,day,hour,minute);
    }

    public String print() {
        if (person.getEmpty()) {
            if (Integer.toString(date.get(Calendar.MINUTE)).length() == 1) {return ""+date.get(Calendar.HOUR_OF_DAY)+":0"+date.get(Calendar.MINUTE)+" "+this.description;}
            else {return ""+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+" "+this.description;}
        }
        else {
            if (Integer.toString(date.get(Calendar.MINUTE)).length() == 1) {return ""+date.get(Calendar.HOUR_OF_DAY)+":0"+date.get(Calendar.MINUTE)+" "+this.description+" "+person.toString();}
            else {return ""+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+" "+this.description+" "+person.toString();}
        }
    }

    public boolean occursOn(int year, int month, int day, int hour, int minute) {
        Calendar newDate = new GregorianCalendar(year, month, day, hour, minute);
        if  (date.compareTo(newDate) == 0) {return true;}
        else {return false;}
    }

    public int compareTo(Appointment other) {
        return this.getDate().compareTo(other.getDate());
    }
}
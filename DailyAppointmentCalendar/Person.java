import java.util.Comparator;

public class Person implements Comparable<Person> {
    
    private boolean empty;
    private String lastName;
    private String firstName;
    private String telephone;
    private String address;
    private String email;

    public Person() {

        lastName = "";
        firstName = "";
        telephone = "";
        address = "";
        email = "";
        empty = true;
    }

    public Person(String lastName, String firstName, String address, String telephone, String email) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        empty = false;
    }

    public boolean getEmpty() {
        return empty;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        empty = false;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        empty = false;
    }

    
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
        empty = false;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
        empty = false;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
        empty = false;
    }
    
    public String toString() {
        if (telephone.equals("") || email.equals("")) {
            return "WITH: "+firstName+" "+lastName;
        }

        else {
            return "WITH: "+firstName+" "+lastName+" "+telephone+" "+email;
        }
    }
    
    public int compareTo(Person other) {
        if (this.lastName.equals(other.getLastName())) {
            return this.firstName.compareToIgnoreCase(other.getFirstName());
        }

        else {
            return this.lastName.compareToIgnoreCase(other.getLastName());
        }
    }
    
    public static Comparator<Person> TelephoneComparator = new Comparator<Person>() {

        public int compare(Person person1, Person person2) {

            String telephone1 = person1.getTelephone();
            String telephone2 = person2.getTelephone();

            return telephone1.compareTo(telephone2);
        }
    };

    public static Comparator<Person> EmailComparator = new Comparator<Person>() {

        public int compare(Person person1, Person person2) {

            String email1 = person1.getEmail();
            String email2 = person2.getEmail();

            return email1.compareToIgnoreCase(email2);
            
        }
    };
}   

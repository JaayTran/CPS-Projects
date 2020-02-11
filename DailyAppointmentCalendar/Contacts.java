import java.util.*;
import java.io.*;

public class Contacts {

    private LinkedList<Person> list;
    private ListIterator iterator;

    public Contacts() {
        list = new LinkedList<Person>();
    }

    public LinkedList<Person> getList() {
        return list;
    }

    public void emptyList() {
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    public int countFileLines(String input) throws IOException {
        FileReader fileRead = new FileReader(input);
        BufferedReader buffRead = new BufferedReader(fileRead);
        int lines = 0;
        String str;
        while ((str = buffRead.readLine()) != null) {
            lines++;
        }    
        buffRead.close();
        return lines;
    }

    public void readContactsFile(String input) throws IOException {
        if ((countFileLines(input)-1) %5 != 0) {
            emptyList();
            throw new IOException();
        }
        else {
            FileReader fileRead = new FileReader(input);
            BufferedReader buffRead = new BufferedReader(fileRead);
            String nums = buffRead.readLine();
            if ( !(Character.isDigit(nums.charAt(0))) || Integer.parseInt(nums) <= 0) {
                emptyList();
                throw new IOException();
            }
            else {
                int count = Integer.parseInt(nums);
                String lastName;
                String firstName;
                String telephone;
                String address;
                String email;
                String str;
                while ((str = buffRead.readLine()) != null && count > 0) {
                    
                    lastName = str;
                    firstName = buffRead.readLine();
                    address = buffRead.readLine();
                    telephone = buffRead.readLine();
                    email = buffRead.readLine();

                    if (lastName.equals("") || email.equals("")) {
                        emptyList();
                        throw new IOException();
                    }

                    else {
                        Person person = new Person(lastName, firstName, address, telephone, email);
                        list.add(person);
                    }   
                    count--;         
                }
            }
            buffRead.close();
        }
    }

    public Person findPersonByFullName(String lastName, String firstName) {
        Person person = new Person(lastName, firstName, "", "", "");
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            Person curr = (Person) iterator.next();
            if (person.compareTo(curr) == 0) {
                return curr;
            }
        }
        return null;
    }

    public Person findPersonByTelephone(String num) {
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            Person curr = (Person) iterator.next();
            if (curr.getTelephone().equals(num)) {
                return curr;
            } 
        }
        return null;
    }

    public Person findPersonByEmail(String email) {
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            Person curr = (Person) iterator.next();
            if (curr.getEmail().equals(email)) {
                return curr;
            }
        }
        return null;
    }

}
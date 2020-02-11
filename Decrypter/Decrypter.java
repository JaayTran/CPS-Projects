import java.io.*;
import java.util.Random;

public class Decrypter {

/* writeToText - A method that accepts two string arguments and writes a new .txt file using FileWriter and PrintWriter. 
The first string is what is written to the file, the second string is the name of the file. */
public static void writeToText(String str, String output) {
    try {
        FileWriter filewrite = new FileWriter(output);
        PrintWriter printwrite = new PrintWriter(filewrite);
        printwrite.println(str);
        printwrite.close();
    } catch (IOException e) {
        System.out.println("Error");
    }
}
/* readFromText - A method that accepts two strings as arguments and reads lines from a .txt file using FileReader and BufferedReader.
The first string stores all of the lines from the text file, the second string is the name of the file/ */
public static String readFromText(String store, String input) {
    try {
        FileReader fileread = new FileReader(input);
        BufferedReader buffread = new BufferedReader(fileread);
        String str;
        while ((str = buffread.readLine()) != null){
            store += str+" ";
        }
        buffread.close();
        return(store); //returns store - a variable containing the contents of the .txt file.
    } catch (IOException e) {
        System.out.println("Could not find file");
        return(""); //If the file cannot be found returns the empty string.
    }
}

/* substitute - A method that accepts three string arguments and substitutes characters in the first string
using the characters from the second string as reference, and replacing characters based on the third string. */
public static String substitute(String password, String alpha, String newalpha) {
    boolean isAtoZ = false; //boolean variable to handle the case of characters outside the english alphabet.
    String newpassword = "";
    for (int i=0; i< password.length(); i++){
        isAtoZ = false;
        for (int n=0; n< alpha.length(); n++){
            if (Character.toUpperCase(password.charAt(i)) == alpha.charAt(n)){
                newpassword = newpassword +newalpha.charAt(n);
                isAtoZ = true;
            }
        }
        if (!isAtoZ){
            newpassword = newpassword + newalpha.charAt(newalpha.length()-1);
        }
    }
    return(newpassword); //returns a new string after the substitution.
}

/* transpose - A method that accepts a string and an array of integers as arguments. It transposes a string and reads it row by row. 
the string argument contains the encrypted text that needs to be transposed. The array of integers contains the order the columns are read. */
public static String transpose(String str, int[] list) {
    int rowlength = list.length;
    char[] tp = new char[rowlength*rowlength];
    String newstr = "";
    int column;
    int inc = 0;
    for (int values : list) {
        column = values;
        for (int row = 0; row < rowlength; row++){
            int tpos = (row*rowlength)+column;
            tp[tpos] = str.charAt(inc);
            inc++;
        }
    }
    for (char c : tp){
        newstr = newstr + String.valueOf(c);
    }
    return(newstr);
}

/* letterswap - a method that accepts a string and two integer arguments. It swaps characters in the string based on two random integers. */
public static String letterswap(String str,int numone, int numtwo) {
    char firstletter = str.charAt(numone);
    char secondletter = str.charAt(numtwo);
    String newstr = "";
    for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == firstletter) {
            newstr = newstr+secondletter;
        }
        else if (str.charAt(i) == secondletter) {
            newstr = newstr+firstletter;
        }
        else {
            newstr = newstr+str.charAt(i);
        }
    }
    return (newstr);
}

/* randSwap - a method that accepts two strings and an integer argument. It uses a key to generate two random integers. 
It passes the second string to the letterswap method along with the two random integers it generates.
The integer argument is used to control how many times the method repeats its operations. */
public static String randSwap(String key, String str, int times) {
    int hash = key.hashCode();
    Random rand = new Random(hash);
    for (int i = 0; i < times; i++){
        int randone = rand.nextInt(str.length());
        int randtwo = rand.nextInt(str.length()); 
        str = letterswap(str,randone,randtwo); //calls the letterswap method.
    }
    return (str);
}

/* arrayToString - a method that accepts an integer array as an argument and returns a String with all of the same elements as the array. */
public static String arrayToString(int[] list) {
    String liststr = "";
    for (int n=0; n < list.length; n++) {
        liststr = liststr + String.valueOf(list[n]);
    }
    return(liststr);
}

/* stringToArray - a method that takes a string and integer array as arguments 
and fills the integer array with the numeric values of the elements in the string. */
public static void stringToArray(String str, int[] list) {
    for (int n=0; n < list.length; n++) {
        list[n] = Character.getNumericValue(str.charAt(n));
    }
}

/* charSelect - a method that accepts a string and two integers as arguments. It reads from a .txt file character by character 
and returns a string of characters in increments of 64. */
public static String charSelect(String str, int charnum, int pos) {
    String newstr = "";
    for (int n=0; n < charnum; n++){
        newstr = newstr + str.charAt(n+pos);
    }
    return newstr;
}

/* cryptPrinter - A method that accepts four strings an integer and an array of integers. 
It calls methods charSelect, transpose,and substitute in a for loop 
and appends the decrypted lines to a string which it writes after it exits the for loop. */
public static void cryptPrinter(String str, int charnum, String alphabet, String randalphabet, int[] cols, String output) {
    String fullpassword = "";
    int inc = 0;
    for (int n=0; n < str.length()/charnum; n++){
        String password = charSelect(str, charnum, (n*charnum)+inc);

        password = transpose(password, cols);

        password = substitute(password,randalphabet,alphabet);

        fullpassword = fullpassword+password+"\n";
        inc++;
    }
    writeToText(fullpassword, output);
}

/* The main line of the program. */
public static void main(String[] args) {
    final int TIMES = 100;
    final int CHARS = 64;

    String key = args[0];
    String input = args[1];
    String output = args[2];

    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    String randalphabet = randSwap(key, alphabet, TIMES);

    int[] cols = {0,1,2,3,4,5,6,7};
    String colstr = randSwap(key, arrayToString(cols), TIMES);
    stringToArray(colstr, cols);

    String stored = "";
    stored = readFromText(stored, input);
    
    cryptPrinter(stored, CHARS, alphabet, randalphabet, cols, output);
}
} 
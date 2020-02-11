import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class feedforward {
    
    /* weightCreate - a method that reads .txt files and turns their contents into two dimensional double arrays */
    public static double[][] weightCreate(String input, int rows) {

        try {
            FileReader fileread = new FileReader(input);
            BufferedReader buffread = new BufferedReader(fileread);
            int rowInc = 0;
            double[][] weightArr = new double[rows][];
            String[] temp;
            String str;

            while ((str = buffread.readLine()) != null){
                temp = str.split(" ");
                weightArr[rowInc] = new double[temp.length-1];
                for (int i = 0; i < temp.length-1; i++){
                    weightArr[rowInc][i] = Double.parseDouble(temp[i]);
                }
                rowInc++;
            }

            buffread.close();
            return (weightArr); //returns the 2D array it created from the file.

        } catch (IOException e) {
            System.out.println("Could not find file");
            double[][] emptyArr = new double[0][0];
            return(emptyArr); //returns an empty array if the file can't be found.
        }
    }

    /* imageReader - a method that reads an image and turns it into a double array based on the data from the pixels. */
    public static double[] imageReader(String input) {

        try {
        BufferedImage img = ImageIO.read(new File(input));
        double[] dummy = null;
        double[] imageArr = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), dummy);
        return(imageArr); //returns the array it created from the image.
        }

        catch (IOException e) {
            System.out.println("Could not find file");
            double emptyArr[] = new double[0];
            return(emptyArr); //returns an empty array if the image can't be found.
        }
    }

    /* rescale -  a method that rescales a double array by a double constant passed through as arguments. */
    public static void rescale(double[] arr, double scale) {

        for (int i = 0; i < arr.length; i++){
            arr[i] = arr[i] / scale;
        }
    }

    /* compute - a method that multiplies a 1D double array by the rows of a 2D double array and returns a 1D double array
        of the sum of the products of the row multiplication after adding a bias and putting it through the sigmoid function. */ 
    public static double[] compute(double[] pixels, double[][] arr, int rows) {

        double[] newArr = new double[rows];

        for (int i = 0; i < rows; i++){
            double value = 0.0;

            for (int n = 0; n < arr[i].length-1; n++){
                value = value + (pixels[n] * arr[i][n]); 
            }

            value = value + arr[i][arr[i].length-1];
            newArr[i] = sigmoid(value);
        }

        return (newArr); //returns a 1D array of doubles.
    }

    /* sigmoid - a method that takes a double as the argument and puts it throught the sigmoid function. */
    public static double sigmoid(double x) {

        double y = 1.0 / (1.0 + Math.pow(Math.E,(-1.0*x)));
        return (y);
    }

    /* printer - a method that accepts a double array and prints the index number of the value in the aray thats the greatest. */
    public static void printer(double[] arr) {

        int x = 0;
        double biggest = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (biggest < arr[i]) {
                biggest = arr[i];
                x = i;
            }
        }

        System.out.println("The network prediction is "+x+".");
    }

    /* the main method of the program. */
    public static void main(String[] args) {

        final int HIDDENROWS = 300;
        final int OUTROWS = 10;
        final double SCALE = 255.0;

        String inputWeights = "output-weights.txt";
        String inputHidden = "hidden-weights.txt";
        String inputImage = args[0];

        double[][] hiddenArr = weightCreate(inputHidden, HIDDENROWS);
        double[][] outputArr = weightCreate(inputWeights, OUTROWS);
        
        
        double[] pixels = imageReader(inputImage);
        rescale(pixels, SCALE);
        
        double[] newHiddenArr = compute(pixels, hiddenArr, HIDDENROWS);
        double[] newOutputArr = compute(newHiddenArr, outputArr, OUTROWS);
        
        printer(newOutputArr);
    }
}
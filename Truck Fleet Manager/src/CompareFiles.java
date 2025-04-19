import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Program to compare input and output files
 * @author Alaaddin Eren Namlı
 */
public class CompareFiles {
    public static void main(String[] args) throws IOException {

        BufferedReader reader1 = new BufferedReader(new FileReader("filepath1"));
        BufferedReader reader2 = new BufferedReader(new FileReader("filepath2"));

        boolean isEqual = true;
        String line1;
        String line2;
        int counter = 1;

        while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
            if (!line1.equals(line2)) {
                System.out.println("WARNING: "+ counter);
                isEqual = false;
                break;
            }
            counter++;
        }

        if (isEqual) {
            System.out.println("EQUAL");
        } else {
            System.out.println("NOT EQUAL");
        }

    }
}

import java.io.IOException;

/**
 * Test out WordReader class.
 */

public class Words {

    public static void main(String args[]) {
        
        try {
            
            //Create a WordReader object to read words from a file.
            WordReader in = new WordReader(args[0]);
            String str = null;

            // Keep reading words while next word is not-null.
            while ((str = in.readWord()) != null) {
                System.out.println(str);
            }

            in.close(); // Close the file.
            
        } catch (IOException ex) {
            System.out.println("Error :" + ex);
            System.exit(1);
        }
        
    }
}

import java.io.*;
import java.util.StringTokenizer;

/**
 * Opens an ASCII text file and reads words from it. 
 * A word is a string of characters separated by 'white'-space i.e. 
 * spaces, taps, new-lines.
 * @author Maurice Hendrix
 * @version 13 January 2009
 */

public class WordReader{

    private BufferedReader br;
    private StringTokenizer currentLine;
    private String currentLineText="";

    /**
     * Open a new word reader on filename.
     * @param filename the file to be read word for word
     * @throws java.io.IOException
     */
    public WordReader(String filename) throws IOException{
        //open the bufferreader to read the file specified
	br = new BufferedReader(new FileReader(filename));
    }

    /**
     * Reads the next word from the file
     * @return The next word if it exists or null if end of input is reached.
     * @throws java.io.IOException
     */
    public String readWord() throws IOException{
        //if the end of the file has been reached variable currentLineText is null
        //if no line has been read before variable currentLine is null
        //if all words on the line read last have been returned before (can be 0 words), currentLine.hasMoreTokens() = false
        //SO while we have not reached teh end of the file and the current line has no more words that have not been returned yet
        while (currentLineText!=null && (currentLine==null || !currentLine.hasMoreTokens())){
            //read the next line as text (string)
            currentLineText=br.readLine();
            //if the line could be read (hence there is a next line)
            //create a tokenizer for the line
            if(currentLineText!=null){
                currentLine = new StringTokenizer(currentLineText, " \t\n\r");
            }else{//else we have reached teh end of the file, return null
                return null;
            }
        }
        //return the next word, note that if we have reached teh end of the file 
        // we will have returned null (and not reach this statement)
        return currentLine.nextToken();
    }

    /**
     * Close the open file to free up resources
     * @throws java.io.IOException
     */
    public void close() throws IOException{
        //close theBufferReader to free up resources
        br.close();
    }
}



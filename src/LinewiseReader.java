import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Read a file, and return the ArrayList representation of the file.
 */
public class LinewiseReader
{
    /**
     * Read a file, and return the ArrayList representation of the file.
     * @param fn Inpute file name
     * @return ArrayList representation of the file
     */
    public ArrayList<String> read(String fn)
    {
        BufferedReader bfReader;
        ArrayList<String> lines = new ArrayList<String>();
        try
        {
            bfReader = new BufferedReader(new FileReader(fn));
            for (String line = bfReader.readLine(); line != null; line = bfReader.readLine())
                lines.add(line);
            bfReader.close();
        } catch (IOException e) { e.printStackTrace(); }
        return lines;
    }
}

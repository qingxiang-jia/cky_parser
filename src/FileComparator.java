import java.util.ArrayList;

public class FileComparator
{
    /**
     * Return an ArrayList of lines with the following conditions:
     * For a line number, the lines in f1 and f2 are the same, but the line
     * in f3 is different.
     * @param fn1 Name of file1
     * @param fn2 Name of file2
     * @param fn3 Name of file3
     * @return The common line in f1 and f2.
     */
    public ArrayList<String> compare(String fn1, String fn2, String fn3)
    {
        LinewiseReader reader = new LinewiseReader();
        ArrayList<String> res = new ArrayList<String>();
        ArrayList<String> f1 = reader.read(fn1);
        ArrayList<String> f2 = reader.read(fn2);
        ArrayList<String> f3 = reader.read(fn3);
        for(int lineNum = 0; lineNum < f1.size(); lineNum++)
        {
            String l2 = f2.get(lineNum).replaceAll("[\\^][<][A-Z]+[+]?[A-Z]?[>]", "");
            String l1 = f1.get(lineNum);
            String l3 = f3.get(lineNum);
            if (l1.equals(l2) && (!l2.equals(l3)))
            {
                res.add(fn3 + ":\n" + l3);
                res.add(fn2 + ":\n" + l2);
                res.add(fn1 + ":\n" + l1);
                res.add("\n");
            }
        }
        return res;
    }

    public static void main(String args[])
    {
        FileComparator comp = new FileComparator();
        ArrayList<String> lines = comp.compare("parse_dev.key", "r1.txt", "r0.txt");
        for(String line: lines)
            System.out.println(line);
    }
}

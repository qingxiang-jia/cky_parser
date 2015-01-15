import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Read in counts and generate maximum likelihood estimates for rule parameters.
 */
public class ParameterGen
{
    String[] nonterms; // nonterminals

    /**
     * Read in counts line by line, which is stored in an ArrayList passed in.
     * @param lines ArrayList contains counts.
     * @return A map that uses StringKey as key, the probability as value.
     */
    public Map<StringKey, Double> gen(ArrayList<String> lines)
    {
        Map<String, Integer> nontermCount = new HashMap<String, Integer>();
        Map<StringKey, Integer> ruleCount = new HashMap<StringKey, Integer>();
        Map<StringKey, Double> ruleEstimates = new HashMap<StringKey, Double>();
        ArrayList<String> nontermArr = new ArrayList<String>();
        for(String line: lines)
        {
            String[] tokens = line.split("\\s+");
            if(tokens[1].equals("UNARYRULE"))
                ruleCount.put(new StringKey(tokens[2], tokens[3]), Integer.parseInt(tokens[0]));
            else if(tokens[1].equals("BINARYRULE"))
                ruleCount.put(new StringKey(tokens[2], tokens[3], tokens[4]), Integer.parseInt(tokens[0]));
            else if(tokens[1].equals("NONTERMINAL"))
            {
                nontermCount.put(tokens[2], Integer.parseInt(tokens[0]));
                nontermArr.add(tokens[2]);
            }
        }
        nonterms = nontermArr.toArray(new String[nontermArr.size()]);
        /** Calculate maximum likelihood estimates **/
        for (StringKey key: ruleCount.keySet())
            ruleEstimates.put(key, (double) ruleCount.get(key) / nontermCount.get(key.k1));
        return ruleEstimates;
    }

    /* Test */
    public static void main(String[] args)
    {
        ParameterGen gen = new ParameterGen();
        LinewiseReader reader = new LinewiseReader();
        Map<StringKey, Double> res = gen.gen(reader.read("cfg_small.counts"));
        for(StringKey key: res.keySet())
        {
            System.out.println(key.toString());
            System.out.println(res.get(key));
            System.out.println("");
        }
        for(String nonterm: gen.nonterms)
            System.out.println(nonterm);
    }
}

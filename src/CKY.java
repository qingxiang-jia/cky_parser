import java.util.*;

/**
 * The class that is the core of Part 5 and Part 6.
 * Implemtation of CKY algorithm on PCFG.
 */
public class CKY
{
    Map<StringKey, Double> allRules;    // Map that contains all rules (binary, unary).
    Map<StringKey, Double> uRules;      // Map that only contains unary rules.
    Set<String> knownWords;             // Set that contains known words, support for _RARE_.
    String[] nonterms;                  // Contains all nonterminal: int -> String
    Map<String, Integer> N;             // Contains the mapping between nonterminals and their integer representation.
    Map<Integer, ArrayList<BinaryRHS>> idxRHS; // A hash table use X as key, and its right-hand sides as value.

    /**
     * Contructor
     * @param fn Name of the counts file
     */
    public CKY(String fn)
    {
        ParameterGen paraGen = new ParameterGen();    // Estimate probability for all rules.
        LinewiseReader reader = new LinewiseReader(); // Going to read in a file
        allRules = paraGen.gen(reader.read(fn));      // Get estimated probability for all rules.
        uRules = new HashMap<StringKey, Double>();    // Stores all unary rules.
        knownWords = new HashSet<String>();           // Stores all trained words.
        nonterms = paraGen.nonterms;                  // Get all nonterminals.
        idxRHS = new HashMap<Integer, ArrayList<BinaryRHS>>();
        /* Collect all nonterminals */
        N = new HashMap<String, Integer>();           // Mapping: String (nonterminal) -> Int (corresponding int representation)
        for(int i = 0; i < nonterms.length; i++)
            N.put(nonterms[i], i);
        /* Collect all binary rules */
        for(StringKey key: allRules.keySet())         // Filling binary rules and unary rules
            if (key.k3 != null) // binary rule
                addIdxRHS(key.k1, key.k2, key.k3, allRules.get(key));
            else
            {
                uRules.put(key, allRules.get(key));
                knownWords.add(key.k2);
            }
    }

    private void addIdxRHS(String k1, String k2, String k3, double prob)
    {
        int X = N.get(k1), Y = N.get(k2), Z = N.get(k3);
        if(idxRHS.containsKey(X))
        {
            ArrayList<BinaryRHS> rhsArr = idxRHS.get(X);
            rhsArr.add(new BinaryRHS(Y, Z, prob));
        } else
        {
            ArrayList<BinaryRHS> rhsArr = new ArrayList<BinaryRHS>();
            rhsArr.add(new BinaryRHS(Y, Z, prob));
            idxRHS.put(X, rhsArr);
        }
    }

    /**
     * Implementation of CKY algorithm
     * @param sentence A sentence to be parsed
     * @return The parse tree in JSON format
     */
    public String parse(String[] sentence)
    {
        /** Initialization **/
        int n = sentence.length;
        String[] sen = sentence.clone();
        /** Replace unknown words with _RARE_d **/
        for(int d = 0; d < sen.length; d++)
            if(!knownWords.contains(sen[d]))
                sen[d] = "_RARE_";
        double[][][] p = new double[n][n][N.size()]; // pi
        int bp[][][][] = new int[n][n][N.size()][7]; // backpointers
        for(int i = 0; i < n; i++)                   // Initialize the bottom level of pi (named p).
            for(String nonterminal: N.keySet())
            {
                StringKey key = new StringKey(nonterminal, sen[i]);
                if(uRules.containsKey(key))
                    p[i][i][N.get(nonterminal)] = uRules.get(key);
                else
                    p[i][i][N.get(nonterminal)] = 0.0;
            }
        /** Main body of CKY **/
        for(int l = 1; l < n; l++)
            for(int i = 0; i < n-l; i++)
            {
                int j = i+l;
                for(int X = 0; X < N.size(); X++)
                {
                    p[i][j][X] = 0;
                    if(idxRHS.containsKey(X))
                    {
                        ArrayList<BinaryRHS> XRHS = idxRHS.get(X);
                        for(BinaryRHS rhs: XRHS)
                        {
                            int Y = rhs.k2;
                            int Z = rhs.k3;
                            for(int s = i; s < j; s++)
                            {
                                double t = rhs.prob * p[i][s][Y] * p[s + 1][j][Z];
                                if(t > p[i][j][X])       // Store a possible solution.
                                {
                                    bp[i][j][X][0] = X;
                                    bp[i][j][X][1] = Y;
                                    bp[i][j][X][2] = Z;
                                    bp[i][j][X][3] = i;
                                    bp[i][j][X][4] = s;
                                    bp[i][j][X][5] = s + 1;
                                    bp[i][j][X][6] = j;
                                    p[i][j][X] = t;
                                }
                            }
                        }
                    }
                }
            }
        /** Construct solution **/
        StringBuilder sb = new StringBuilder();
        if(p[0][n-1][N.get("S")] != 0)
            solutionGen(bp, 0, n-1, N.get("S"), sentence, sb);
        else
        {
            int sym = 0;
            double pi = -Integer.MAX_VALUE;
            for(int X = 0; X < p[0][n-1].length; X++)
                if(p[0][n-1][X] > pi)
                {
                    pi = p[0][n-1][X];
                    sym = X;
                }
            solutionGen(bp, 0, n-1, sym, sentence, sb);
        }
        return(sb.toString());
    }

    /**
     * A resursive method that constructs the parse tree from the backpointers.
     * @param bp   the backpointers
     * @param l    Spans [l, r].
     * @param r    Spans [l, r].
     * @param sym  the nonterminal
     * @param sentence the original sentence
     * @param sb   StringBuilder used to construct the parse tree
     */
    private void solutionGen(int[][][][] bp, int l, int r, int sym, String[] sentence, StringBuilder sb)
    {
        if(l == r) // base case
            sb.append("[\"" + nonterms[sym] + "\", \"" + sentence[l] + "\"]");
        else // recursive case
        {
            int l_sym = bp[l][r][sym][1];
            int r_sym = bp[l][r][sym][2];
            int l_l   = bp[l][r][sym][3];
            int l_r   = bp[l][r][sym][4];
            int r_l   = bp[l][r][sym][5];
            int r_r   = bp[l][r][sym][6];
            sb.append("[\"" + nonterms[sym] + "\", ");
            solutionGen(bp, l_l, l_r, l_sym, sentence, sb);
            sb.append(", ");
            solutionGen(bp, r_l, r_r, r_sym, sentence, sb);
            sb.append("]");
        }
    }

    /* Allow testing script to invoke. */
    public static void main(String args[])
    {
        CKY cky = new CKY(args[0]); // cfg_rare.counts or cfg_vert.counts
        /* Parsing Task */
        LinewiseReader reader = new LinewiseReader();
        ArrayList<String> sentences = reader.read("parse_dev.dat");
        for(String sentence: sentences)
            System.out.println(cky.parse(sentence.split("\\s+")));

//        /** Debugging code **/
//        CKY cky = new CKY("cfg_vert.counts");
//        LinewiseReader reader = new LinewiseReader();
//        ArrayList<String> sentences = reader.read("parse_dev.dat");
//        for(String sentence: sentences)
//            System.out.println(cky.parse(sentence.split("\\s+")));
    }
}

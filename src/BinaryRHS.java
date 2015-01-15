/**
 * This object represents the right-hand side of a binary rule.
 * For example, if the rule is X -> Y Z, then the object includes
 * the values of Y, Z, and the corresponding probability of this
 * rule.
 */
public class BinaryRHS
{
    int k2, k3; // k1 -> k2 k3
    Double prob; // probability of this rule
    public BinaryRHS(int Y, int Z, Double p)
    {
        k2 = Y;
        k3 = Z;
        prob = p;
    }
}

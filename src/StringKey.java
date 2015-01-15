/**
 * The object that represents the key of the rule in a hash table.
 */
public class StringKey
{
    String k1, k2, k3; // binary rule: k1 -> k2 k3

    /**
     * Unary rule constructor
     * @param k1 k1 -> k2
     * @param k2 k1 -> k2
     */
    public StringKey(String k1, String k2)
    {
        this.k1 = k1;
        this.k2 = k2;
    }

    /**
     * Binary rule constructor
     * @param k1 k1 -> k2 k3
     * @param k2 k1 -> k2 k3
     * @param k3 k1 -> k2 k3
     */
    public StringKey(String k1, String k2, String k3)
    {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
    }

    /**
     * Customized hashCode method so that both keys for unary and binary rule are hashable.
     * @return Customized hash code
     */
    public int hashCode()
    {
        if(k3 != null)
            return k1.hashCode() + k2.hashCode() + k3.hashCode();
        else
            return k1.hashCode() + k2.hashCode();
    }

    /**
     * Customized equals method so that the String with the same content (but different memory address) yields the same
     * hash code.
     * @param obj
     * @return
     */
    public boolean equals(Object obj)
    {
        if(this.getClass() != obj.getClass()) return false;
        StringKey o = (StringKey) obj;
        if(k3 != null && o.k3 != null)
            return k1.equals(o.k1) && k2.equals(o.k2) && k3.equals(o.k3);
        else if(k3 == null && o.k3 == null)
            return k1.equals(o.k1) && k2.equals(o.k2);
        else
            return false;
    }

    /**
     * Customized toString to make debugging easier.
     * @return Customized toString
     */
    public String toString()
    {
        if(k3 != null)
            return "(" + k1 + ", " + k2 + ", " + k3 + ")";
        else
            return "(" + k1 + ", " + k2 +  ")";
    }
}

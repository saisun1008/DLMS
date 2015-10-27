package dlms.interfaces.corba.DlmsInterfaceModule;

/**
 * Holder class for : DlmsInterface
 * 
 * @author OpenORB Compiler
 */
final public class DlmsInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal DlmsInterface value
     */
    public DlmsInterface value;

    /**
     * Default constructor
     */
    public DlmsInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public DlmsInterfaceHolder(DlmsInterface initial)
    {
        value = initial;
    }

    /**
     * Read DlmsInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = DlmsInterfaceHelper.read(istream);
    }

    /**
     * Write DlmsInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        DlmsInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the DlmsInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return DlmsInterfaceHelper.type();
    }

}

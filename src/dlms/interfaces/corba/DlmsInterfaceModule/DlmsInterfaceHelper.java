package dlms.interfaces.corba.DlmsInterfaceModule;

/** 
 * Helper class for : DlmsInterface
 *  
 * @author OpenORB Compiler
 */ 
public class DlmsInterfaceHelper
{
    /**
     * Insert DlmsInterface into an any
     * @param a an any
     * @param t DlmsInterface value
     */
    public static void insert(org.omg.CORBA.Any a, DlmsInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract DlmsInterface from an any
     *
     * @param a an any
     * @return the extracted DlmsInterface value
     */
    public static DlmsInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return DlmsInterfaceHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the DlmsInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "DlmsInterface" );
        }
        return _tc;
    }

    /**
     * Return the DlmsInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:DlmsInterfaceModule/DlmsInterface:1.0";

    /**
     * Read DlmsInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed DlmsInterface value
     */
    public static DlmsInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(DlmsInterface)istream.read_Object(_DlmsInterfaceStub.class);
    }

    /**
     * Write DlmsInterface into a marshalled stream
     * @param ostream the output stream
     * @param value DlmsInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, DlmsInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to DlmsInterface
     * @param obj the CORBA Object
     * @return DlmsInterface Object
     */
    public static DlmsInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof DlmsInterface)
            return (DlmsInterface)obj;

        if (obj._is_a(id()))
        {
            _DlmsInterfaceStub stub = new _DlmsInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to DlmsInterface
     * @param obj the CORBA Object
     * @return DlmsInterface Object
     */
    public static DlmsInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof DlmsInterface)
            return (DlmsInterface)obj;

        _DlmsInterfaceStub stub = new _DlmsInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}

package comm;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import misc.Log;

public class MXLookup {
	private ArrayList mxList = null;
	public MXLookup(String targetEmail){
		int pos = targetEmail.indexOf( '@' );
		String domain = targetEmail.substring( ++pos );
		try {
			mxList = getMX( domain );
		} 
		catch (NamingException ex) {}
		for(int i=0; i < mxList.size(); i++){
			Log.msg(mxList.get(i)+"");
		}
	}
	private static ArrayList getMX( String hostName )
	throws NamingException {
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial",
		"com.sun.jndi.dns.DnsContextFactory");
		DirContext ictx = new InitialDirContext( env );
		Attributes attrs = ictx.getAttributes
		( hostName, new String[] { "MX" });
		Attribute attr = attrs.get( "MX" );
		if (( attr == null ) || ( attr.size() == 0 )) {
			attrs = ictx.getAttributes( hostName, new String[] { "A" });
			attr = attrs.get( "A" );
			if( attr == null ) 
				throw new NamingException
				( "No match for name '" + hostName + "'" );
		}
		ArrayList res = new ArrayList();
		NamingEnumeration en = attr.getAll();

		while ( en.hasMore() ) {
			String x = (String) en.next();
			String f[] = x.split( " " );
			if ( f[1].endsWith( "." ) ) 
				f[1] = f[1].substring( 0, (f[1].length() - 1));
			res.add( f[1] );
		}
		return res;
	}
	public ArrayList getMXList(){
		return mxList;
	}
}

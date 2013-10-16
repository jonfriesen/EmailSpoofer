package comm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.naming.NamingException;

import misc.Log;

import obj.Message;

public class SendMessage {
	public SendMessage(){}
	public SendMessage(ArrayList mxList, Message msg){
		Log.msg("Starting Send!");
		sendIt(mxList, msg);
	}
	public static boolean sendIt( ArrayList mxList, Message msg ) {
		
		for ( int mx = 0 ; mx < mxList.size() ; mx++ ) {
			boolean sent = false;
			try {
				int res;
				Socket skt = new Socket( (String) mxList.get( mx ), 25 );
				BufferedReader rdr = new BufferedReader
				( new InputStreamReader( skt.getInputStream() ) );
				BufferedWriter wtr = new BufferedWriter
				( new OutputStreamWriter( skt.getOutputStream() ) );

				res = hear( rdr );
				if ( res != 220 ) throw new Exception( "Invalid header" );
				say( wtr, "HELO " + getDomain(msg.getFrom()));

				res = hear( rdr );
				if ( res != 250 ) throw new Exception( "Not ESMTP" );

				say( wtr, "MAIL FROM: <"+msg.getFrom()+">" );
				res = hear( rdr );
				if ( res != 250 ) throw new Exception( "Sender rejected" );

				say( wtr, "RCPT TO: <" + msg.getTo() + ">" );
				res = hear( rdr );
				if ( res != 250 ) throw new Exception( "Sender rejected" );
				
				say(wtr, "DATA"); res = hear( rdr );
				
				say(wtr, "Date: " + msg.getDate()); 
				
				say(wtr, "Time: " + msg.getTime()); 
				
				say(wtr, "From: " + msg.getFromName() +" <"+msg.getFrom()+">");
				
				say(wtr, "Subject: " + msg.getSubject());
				
				say(wtr, msg.getContent());
				
				say(wtr, "."); res = hear( rdr );

				say( wtr, "QUIT" ); hear( rdr );

				sent = true;
				rdr.close();
				wtr.close();
				skt.close();
			} 
			catch (Exception ex) {
				// Do nothing but try next host
			} 
			finally {
				if ( sent ) return true;
			}
		}
		return false;
	}
	
	private static String getDomain(String email){
		int pos = email.indexOf( '@' );
		return email.substring( ++pos );
	}
	private static int hear( BufferedReader in ) throws IOException {
		String line = null;
		int res = 0;

		while ( (line = in.readLine()) != null ) {
			String pfx = line.substring( 0, 3 );
			Log.msg(""+line);
			try {
				res = Integer.parseInt( pfx );
			} 
			catch (Exception ex) {
				res = -1;
			}
			if ( line.charAt( 3 ) != '-' ) break;
		}

		return res;
	}

	private static void say( BufferedWriter wr, String text ) 
	throws IOException {
		wr.write( text + "\r\n" );
		wr.flush();

		return;
	}
}

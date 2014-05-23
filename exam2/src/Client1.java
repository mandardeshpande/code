import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.*;

public class Client1 
{
	public static void main(String args[]) throws IOException
	{
		 String serverHostname = new String ("127.0.0.1");
		 	
		 	int portnum = 8083;
	        Socket echoSocket = null;
	        FileOutputStream out = null;
	        File output;
	        DataInputStream in = null;
	        DataOutputStream outs = null;
	        byte content[] = new byte[1024];
			int readCount = 0;
			int bytesread = 0;
			int bytesreceived = 0;
			int byteswritten = 0;
		  	String messagestring = "";
            
			try 
	        {
	            echoSocket = new Socket(serverHostname, portnum);
	            in = new DataInputStream(echoSocket.getInputStream());
	            outs = new DataOutputStream(echoSocket.getOutputStream());
	            
	            int clientid = in.readInt();
	            System.out.println("Client # "+clientid);
	            int i = 0;
	            int sum=0;
	            
	            String dataStr1 = "";
	            int q = 0;
	            while((readCount = in.readInt()) != -9999)
	            {
	            	System.out.println("I got "+readCount);
	            	sum += readCount;
	            }
	            
	            System.out.println("Sum from client"+clientid+" is "+sum);
	            outs.writeInt(sum);
	            outs.flush();
	            outs.writeInt(-9999);
	            outs.flush();
	        } 
	        
	        catch (UnknownHostException e)
	        {
	            System.err.println("Couldn't get I/O for the connection to: " + serverHostname);
	            System.out.println(e.getMessage());
	            System.exit(1);
	        }
			
			
			outs.close();
	        in.close();
		
	        echoSocket.close();
		
	}
}

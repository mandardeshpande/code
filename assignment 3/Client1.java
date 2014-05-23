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
	        byte content[] = new byte[102400];
			int readCount = 0;
			int bytesread = 0;
			int bytesreceived = 0;
			int byteswritten = 0;
		  	String messagestring = "";
            
			try 
	        {
	            echoSocket = new Socket(serverHostname, portnum);
	            in = new DataInputStream(echoSocket.getInputStream());
	            int clientid = in.readInt();
	            System.out.println("Client # "+clientid);
	            output = new File("C:\\Users\\Mandar\\workspace\\ThreadClientServer\\src\\outputfile_client_"+clientid+".txt");
	            out = new FileOutputStream(output);
	            int i = 0;
	            
	            if(!output.exists())
	            	output.createNewFile();
	           
	            
	            while( ( readCount = in.read()) != -1)
	            {
	            	bytesreceived++;
	            	if(i == content.length)
	            	{
		        		out.write(content,0,content.length);
		            	out.flush();
		            	byteswritten+=content.length;
		            	i=0;
	            	}
	            	content[i] = (byte)readCount;
	            	i++;
	            }
	            
	            out.write(content,0,(bytesreceived - byteswritten));
	            out.flush();
	            	            
	            System.out.println("Bytes received "+bytesreceived); 
	        	
	           
	        } 
	        catch (UnknownHostException e)
	        {
	            System.err.println("Don't know about host: " + serverHostname);
	            System.exit(1);
	        }
	        catch (IOException e)
	        {
	            System.err.println("Couldn't get I/O for the connection to: " + serverHostname);
	            System.out.println(e.getMessage());
	            System.exit(1);
	        }
			
			
	        out.close();
	        in.close();
		
	        echoSocket.close();
		
	}
}

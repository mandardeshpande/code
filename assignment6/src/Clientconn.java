import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConn implements Runnable 
{
    private Socket client;
    private BufferedReader in = null;
    private PrintWriter out = null; 
    /* change this folderPath as per your location. */
    public String folderPath = "/home/mandar/workspace/assignment6/src/";

	
    public ClientConn(Socket client)
    {
        this.client = client;
        try
        {
            /* obtain an input stream to this client ... */
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            /* ... and an output stream to the same client */
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e)
        {
            System.err.println(e);
            return;
        }
		
    }
 
    public void run()
    {
        String msg, response;
        ChatServerProtocol protocol = new ChatServerProtocol(this);
        try
        {
            /* loop reading lines from the client which are processed 
             * according to our protocol and the resulting response is 
             * sent back to the client */
            while ((msg = in.readLine()) != null) 
            {
                response = protocol.process(msg);
                out.println("SERVER: " + response);
            }
        } 
        catch (IOException e)
        {
            System.err.println(e);
        }
    }
 
    public void sendMsg(String sender,String receiver,String fileName) 
    {
    	//out.println(fileName);
    	
    	System.out.println(" this is in sendcon send "+fileName);
    	
    	File file = new File(folderPath+fileName);
    	File outputFile = new File(folderPath+sender+"_"+receiver+".txt");
    	
		FileInputStream fis = null;
		FileOutputStream fout = null;
		
		byte[] buffer = new byte[10240];
		//String fileData;
		
		try 
		{
			if(!outputFile.exists())
				outputFile.createNewFile();
			
			fis = new FileInputStream(file);
			fout = new FileOutputStream(outputFile);
			System.out.println("Total file size to read (in bytes) : "+ fis.available());
 
			int content;
			while ((content = fis.read(buffer)) != -1) 
			{
				//fileData = new String(buffer);
				fout.write(buffer);
			}
		}
		catch(IOException e)
		{
			System.out.println(" OOPS"+e.getMessage());
		}
		
    }
}
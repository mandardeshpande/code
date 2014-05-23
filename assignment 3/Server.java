import java.io.*;
import java.net.*;

public class Server 
{
	int port_number = 8083;
	ServerSocket echoserver = null;
	Socket clientsocket = null;
	int num_connections = 0;
	
	
	public static void main(String args[]) throws IOException
	{
		Server mserv = new Server();
		mserv.StartServer();
	}
	
	public void StartServer()
	{
		try
		{
			echoserver = new ServerSocket(port_number);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		System.out.println("Server has started.....Waiting for clients to connect.");
		
		while(true)
		{
			try
			{
				clientsocket = echoserver.accept();
				num_connections++;
				ServerConnections ser = new ServerConnections(clientsocket, num_connections, this);
				Thread t = new Thread(ser);
				t.setName(" connection "+num_connections);
				t.start();
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
		
		
			
	}
	
	
	public void stopServer()
	{
		System.out.println(" Server is shutting down.....");
		System.exit(1);
	}
}

class ServerConnections implements Runnable
{
	DataInputStream in = null;
	DataOutputStream outd =  null;
	Socket clientsocket = null;
	int clientId;
	Server server;
	boolean serverStop = true;
	Thread runningthread = null;
	
	public ServerConnections(Socket clientsocket, int id, Server server)
	{
		this.clientsocket = clientsocket;
		this.clientId = id;
		this.server = server;
	
		try
		{
			in = new DataInputStream(clientsocket.getInputStream());
			outd = new DataOutputStream(clientsocket.getOutputStream());
		} 
		catch (IOException e)
		{
		    System.out.println(e);
		}
	 }
	
	public void run()
	{
        try 
        {
        	synchronized(this)
        	{
        		this.runningthread = Thread.currentThread();
        		serverStop = false;
        	}
        	outd.writeInt(clientId);
        	byte sendcontent[] = new byte[1];
        	int bytesread = 0;
        	int bytessent=0;
        	FileInputStream is = new FileInputStream("C:\\Users\\Mandar\\workspace\\ThreadClientServer\\src\\testdata.txt");
        	
        	System.out.println(" Now sending data to client "+clientId);
        
            while ((bytesread = is.read(sendcontent)) != -1 ) 
            {
            	bytessent++;
            	outd.write(sendcontent);
            	
            }
            System.out.println(" Bytes sent  "+bytessent);
            
            stop();
            in.close();
            outd.close();
       	} 
	    catch (IOException  e) 
		{
		    System.out.println(e);
		}
    }
	
	public synchronized void stop()
	{
		try
		{
			clientsocket.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

import java.io.*;
import java.net.*;
import java.util.*;

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
				ClientConn ser = new ClientConn(clientsocket);
				Thread t = new Thread(ser);
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
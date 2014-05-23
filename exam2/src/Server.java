import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server 
{
	int port_number = 8083;
	ServerSocket echoserver = null;
	Socket clientsocket = null;
	int num_connections = 0;
	ArrayList<Integer> numList = new ArrayList<Integer>();
	public static boolean running = true; 
	
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
		int i;
		
		for(i=0; i < 20 ; i++)
		{
			numList.add(i);
		}
		//numList.add(0);		
		while(running)
		{
			try
			{
				clientsocket = echoserver.accept();
				num_connections++;
				
				ServerConnections ser = new ServerConnections(clientsocket, num_connections, this,numList);
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
	DataOutputStream outd =  null;
	DataInputStream in =  null;
	Socket clientsocket = null;
	int clientId;
	Server server;
	boolean serverStop = true;
	Thread runningthread = null;
	ArrayList<Integer> listNum = new ArrayList<Integer>();
	public static int sumClient = 0;
	int intialSize = 0;
	
	public ServerConnections(Socket clientsocket, int id, Server server,ArrayList list)
	{
		this.clientsocket = clientsocket;
		this.clientId = id;
		this.server = server;
		this.listNum = list;
		this.intialSize = list.size();
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
        	int start = 0; int end =0;int i =0;
        	byte data[] = new byte[1024];
        	outd.writeInt(clientId);
        	
        	int bytesread = 0;
        	int bytessent=0;
        	String dataStr = "";
        	System.out.println(" Now sending Number from ArrayList to client "+clientId);
        	
        	
        	int half = (int)Math.abs(listNum.size()/2);
        	i   = (clientId - 1)*(half) + end;
        	end = i+half-1;
        	int count = clientId*half;
        	
        	for(start=i; start <= end; start++)
        	{
        		System.out.println("Number from ArrayList are "+listNum.get(start));
        		outd.writeInt(listNum.get(start));
        		bytessent++;
        	}
        	if(sumClient != 0)
    			outd.writeInt(sumClient);
        	outd.flush();
        	outd.writeInt(-9999);
        	outd.flush();
        	
        	while((bytesread = in.readInt()) != -9999)
        	{
        		sumClient = bytesread;
        	}
        	 
        	
        	if(listNum.size() == count)
        	{
        		System.out.println(" Sum is "+sumClient);
        		System.out.println(" Job Completed");
        		System.exit(1);
        	}
        	
       	} 
	    catch (Exception  e) 
		{
		    System.out.println(e);
		}
    }
	
	public void stop()
	{
		try
		{	
			System.out.println("Server stopping.....");
			outd.close();
			clientsocket.close();
			Server.running = false;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}




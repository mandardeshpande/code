import java.io.*;
import java.net.*;

public class ScanHost
{
	public static void main(String args[])
	{
		
		InetAddress host = null;
		
		try
		{
			host = InetAddress.getByName("Excalibur");
			int len = host.getAddress().length;
			byte ip[] = host.getAddress();
			for(int i=0; i< len; i++)
			{
				ip[3] = (byte)i;
				InetAddress inetadd = InetAddress.getByAddress(ip);
				System.out.println(inetadd.getHostAddress()+"  "+inetadd.getHostName());
			}
		}
		catch(Exception e)
		{
			e.getCause();
		}
	}
}

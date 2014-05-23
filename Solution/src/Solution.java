import java.io.*;

public class Solution 
{
	public static void main(String args[])
	{
		getData();
		
	}
	
	public static int[][] getData()
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			int farmSize = Integer.parseInt(reader.readLine());
			int farm[][] = new int[farmSize][farmSize];
			int row=0;
			int col = 0;
			String farmElevation[] = new String[farmSize];
			
			for(row=0;row<farmSize;row++)
			{
				farmElevation = reader.readLine().split(" ");
				for(col=0;col<farmSize;col++)
					farm[row][col] = Integer.parseInt(farmElevation[col]);
				
			}
			
			return farm;
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
		
	}

}

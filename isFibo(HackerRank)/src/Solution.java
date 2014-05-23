import java.io.*;
import java.util.*;

public class Solution
{
	public static int num_testcases = 0;
	public static ArrayList fibNum = null;
	public static int index = 2;
	
	public static void main(String args[])
	{
		try 
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			num_testcases = Integer.parseInt(reader.readLine());
			
			fibNum = new ArrayList();
			fibNum.add(index-2, 0);
			fibNum.add(index-1, 1);
			int fibVal =0;
			String fibString = "";
			
			while((fibString = reader.readLine()) != null)
			{
				fibVal = Integer.parseInt(fibString);
				calFib(fibVal);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void calFib(int fibInputVal)
	{
		int fibVal = 0;
		
		System.out.println("I am here and index is "+fibInputVal);
		while(fibVal < fibInputVal)
		{	
			
			fibVal = (Integer)fibNum.get(index-1) + (Integer)fibNum.get(index-2);
			fibNum.add(index, fibVal);
			index++;
			//System.out.println(fibVal);
		}
		
		isFibo(fibNum,fibInputVal);
	}
	
	public static void isFibo(ArrayList fibArr, int fibNum)
	{
		int arr[] = new int[fibArr.size()];
		arr = fibArr.toArray();
		System.out.println(binarySearch(fibArr,fibNum));
	}
	
	public static boolean binarySearch(int arr[],int key)
	{
		int low=0;
		int high = arr.length-1;
		int mid = 0;
		
		while(low < high)
		{
			mid = (low+high)/2;
			
			if(arr[mid] == key)
				return true;
			else if(arr[mid] < key)
				low = mid +1;
			else if(arr[mid] > key)
				high = mid-1;
		}
		
		return false;
		
	}
}

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCursor;

public class NextWord {
	
	public List<String> topWords(MongoCursor<Document> cursor) throws FileNotFoundException, IOException
	{
		int startOfWordIndex = 0;
		int startOfAppearances = 0;
		int appearances = 0;
		int index = 0;
		List<String> list = new ArrayList<String>();
		List<Integer> numList = new ArrayList<Integer>();
		
		char[] info;
		String line;

		while(cursor.hasNext())
		{
			//line = cursor.next().get("following_frequency").toString();
			line = cursor.next().toString();
			//System.out.println(line);
		    info = line.toCharArray();
		    index = 8;
		    
	    	while(true)
			{
			    
			    while(line.substring(index, index+5).equals("text=") == false)
			    {
			    	if(index == line.length()-5 )
			    	{
			    		break;
			    	}
			    	
			    	index++;
			    }
			    if(index == line.length()-5)
			    {
			    	break;
			    }
			    else
			    {
				    index = index+5;
				    startOfWordIndex = index;
				    while(line.substring(index, index+14).equals(", appearances=") == false)
				    {
				    	index++;
				    }
				    list.add(line.substring(startOfWordIndex, index));
				    index = index+14;
				    startOfAppearances = index;
				    
				    while(line.substring(index, index+5).equals("}}, D") == false)
				    {
				    	index++;
				    	if(line.substring(index,index+5).equals("}}]}}") == true)
					    {
					    	break;
					    }
				    }
				    appearances = Integer.parseInt(line.substring(startOfAppearances, index));
				    numList.add(appearances);
				    
				    //System.out.println(line.substring(index,index+5));
				    if(line.substring(index,index+5).equals("}}]}}") == true)
				    {
				    	break;
				    }
				}
			}
		}
		
		
	    //System.out.println(appearances);
	    //System.out.println(line.substring(index, index+7));
	    /*for(int i=0;i<list.size();i++)
	    {
	    	System.out.print(list.get(i) + ", ");
	    }
	    System.out.println("");*/
	    
	    
		
		//Sorts best 10
		List<String> results = new ArrayList<String>();
	    for(int i = 0;i<10;i++)
	    {
	    	int max = 0;
	    	int maxIndex = 0;
	    	for(int j = 0; j<numList.size() ;j++)
		    {
		    	if(max < numList.get(j))
		    	{
		    		max = numList.get(j);
		    		maxIndex = j;
		    	}
		    }
	    	
	    	results.add(list.get(maxIndex) + " word Frequency: " + numList.get(maxIndex));
	    	
	    	numList.remove(maxIndex);
	    	list.remove(maxIndex);
	    }
	    
	    /*for(int i = 0;i<10;i++)
	    {
	    	System.out.println(results.get(i));
	    }*/
		
		return results;
	    
	    //return list;
	}
	
	
	public List<String> topWordsOld(MongoCursor<Document> cursor) throws FileNotFoundException, IOException
	{
		
		String input = "At";
		List<String> list = new ArrayList<String>();
		List<Integer> numList = new ArrayList<Integer>();
		
		char[] info;
		String line;
	    //line = cursor.next().toString();
	    //System.out.println(line);
	    
	    while ( cursor.hasNext() == true ) {
	    	line = cursor.next().toString();
		    info = line.toCharArray();
		    //System.out.println(info);
		    int startOfWord = 15;
		    while(info[startOfWord] != '=')
		    {
		    	startOfWord++;
		    }
		    startOfWord++;
		    int endOfWord = 0;
		    
		    //System.out.println(info.length);
		    
		    endOfWord = startOfWord;
		    //String temp = ""+(info[startOfWord] + (""+ info [startOfWord+1]) + (""+ info [startOfWord+2])  );
		    String temp = line.substring(startOfWord, (startOfWord+15));
		    //System.out.println(temp);
		    while( temp.equals(", wordFrequency") != true )
	    	{
		    	endOfWord++;
		    	temp = line.substring(endOfWord, (endOfWord+15));
	    	}
		    //endOfWord--;
		    /*System.out.println();
		    System.out.println(line.substring(startOfWord, endOfWord));
		    System.out.println();*/
		    String firstGram = line.substring(startOfWord, endOfWord);
		    
		    
		    list.add(firstGram);
	    	
	    	int wordFrequencyIndex = endOfWord + 16;
	    	String numberToKeepString = "";
	    	while(info[wordFrequencyIndex] != ',')
	    	{
	    		numberToKeepString += info[wordFrequencyIndex];
	    		wordFrequencyIndex++;
	    		//System.out.println(numberToKeepString);
	    	}
	    	int numToKeep = Integer.parseInt(numberToKeepString);
	    	numList.add(numToKeep);
	    	
	    }
	    
	    
	    //System.out.println(list.size());
	    //System.out.println(numList.size());
	    
	    List<String> results = new ArrayList<String>();
	    for(int i = 0;i<10;i++)
	    {
	    	int max = 0;
	    	int maxIndex = 0;
	    	for(int j = 0; j<numList.size() ;j++)
		    {
		    	if(max < numList.get(j))
		    	{
		    		max = numList.get(j);
		    		maxIndex = j;
		    	}
		    }
	    	
	    	results.add(list.get(maxIndex) + " word Frequency: " + numList.get(maxIndex));
	    	
	    	numList.remove(maxIndex);
	    	list.remove(maxIndex);
	    }
	    
	   /* for(int i = 0;i<10;i++)
	    {
	    	System.out.println(results.get(i));
	    }*/
		
		return results;
	}
	
	//File file = null;
	/*public static void main(String args[]) throws IOException   {
		
		
		String input = "At";
		List<String> list = new ArrayList<String>();
		List<Integer> numList = new ArrayList<Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/Thomas/workspace/ClassProject/google_Twogram_Subset's.txt"))) {
			char[] info;
			String line;
		    line = br.readLine();
		    //System.out.println(line);
		    while ((line = br.readLine()) != null) {
		    	
		    
			    info = line.toCharArray();
			    int startOfWord = 0;
			    int endOfWord = 0;
			    
			    //System.out.println(info.length);
			    
			    endOfWord = startOfWord;
			    while(info[endOfWord] != ' ')
		    	{
			    	endOfWord++;
		    	}
			    //endOfWord--;
			    //System.out.println(line.substring(startOfWord, endOfWord));
			    String firstGram = line.substring(startOfWord, endOfWord);
			    
			    
			    if(firstGram.compareToIgnoreCase(input) == 0)
			    {
			    	//System.out.println(firstGram);
			    	//System.out.println("firstGram");
			    	int startOfNextGram = endOfWord + 1;
			    	int endOfNextGram = endOfWord + 1;
			    	while(info[endOfNextGram] != ' ')
			    	{
			    		endOfNextGram++;
			    	}
			    	//endOfNextGram--;
			    	//System.out.println(startOfNextGram + " " + endOfNextGram);
			    	//System.out.println(line.substring(startOfNextGram, endOfNextGram+1));
			    	String keepWord = line.substring(startOfNextGram, endOfNextGram+1);
			    	list.add(keepWord);
			    	
			    	int wordFrequencyIndex = endOfNextGram + 1;
			    	String numberToKeepString = "";
			    	while(info[wordFrequencyIndex] != ' ')
			    	{
			    		numberToKeepString += info[wordFrequencyIndex];
			    		wordFrequencyIndex++;
			    		//System.out.println(numberToKeepString);
			    	}
			    	int numToKeep = Integer.parseInt(numberToKeepString);
			    	numList.add(numToKeep);
			    }
		    }
		    
		    
		    System.out.println(list.size());
		    System.out.println(numList.size());
		    
		    List<String> results = new ArrayList<String>();
		    for(int i = 0;i<10;i++)
		    {
		    	int max = 0;
		    	int maxIndex = 0;
		    	for(int j = 0; j<numList.size() ;j++)
			    {
			    	if(max < numList.get(j))
			    	{
			    		max = numList.get(j);
			    		maxIndex = j;
			    	}
			    }
		    	
		    	results.add(list.get(maxIndex));
		    	
		    	numList.remove(maxIndex);
		    	list.remove(maxIndex);
		    }
		    
		    for(int i = 0;i<10;i++)
		    {
		    	System.out.println(results.get(i));
		    }
		    
		    
		}
	}*/
}

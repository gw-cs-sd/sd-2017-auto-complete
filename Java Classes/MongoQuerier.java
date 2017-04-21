import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import com.mongodb.client.MongoCursor;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MongoQuerier {
	
	public static boolean doesNotHave(String completion, ArrayList alReadyHave)
	{
		if(alReadyHave.contains(completion))
			return true;
		else
			return false;
	}
	
	//teset method to redo trim
	public static Document trimResults(List<Document> queryData, String inputWord)
	{
		//the doucment full of completions we want to return
		Document returnData = new Document();
		returnData.append("text", inputWord);//adding the word now so the format of the returned document matches proper conventions
		ArrayList alReadyHave = new ArrayList();//List that will hold words that we already have to avoid duplicates
		List<Document> returnCompletions = new ArrayList();
		
		List<Document> completions;
		Document gramData;
		for(int i=0; i<queryData.size(); i++)
		{
			gramData = queryData.get(i);
			completions = (List<Document>) gramData.get("completions");
			Document completion;//singular document in the list completions (list of documents)
			
			for(int j=0; j<completions.size(); j++)
			{
				completion = completions.get(j);
				
				//thresholds will trim out completions we do not want
				//change thresholds scores later
				if( (int)gramData.get("gramLength") == 1 && (double)completion.get("score") < .10)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 2 && (double)completion.get("score") < .10)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 3 && (double)completion.get("score") < .10)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 4 && (double)completion.get("score") < .10)
				{
					completions.remove(j);
					j--;
				}
			}	
		}
		
		while(returnCompletions.size() < 10 && queryData.size() > 1)
		{
			int maxPos = 0;
			double maxScore = 0;
			
			//find the max score completion
			for(int i=0; i<queryData.size() - 1; i++)
			{
				completions = (List<Document>)queryData.get(i).get("completions");
				
				if(completions.isEmpty())
				{
					queryData.remove(i);
					i--;
				}
				else if(doesNotHave(completions.get(0).get("completion").toString(), alReadyHave))
				{
					//System.out.println("yes: " + completions.get(0).get("completion"));
					completions.remove(0);
				}
				else if( (double)completions.get(0).get("score") > maxScore)
				{
					maxScore = (double)completions.get(0).get("score");
					maxPos = i;
				}
			}
			
			//add max score to return list and already have list and remove from the completions list it came from
			completions = (List<Document>)queryData.get(maxPos).get("completions");
			boolean replacementHappened = false;
			if(completions.isEmpty())
			{
			}
			else if(replacementHappened == false && queryData.size() > 1)
			{
				returnCompletions.add(completions.get(0));
				alReadyHave.add(completions.get(0).get("completion"));
				completions.remove(0);
			}
			
		}
		
		returnData.append("completions", returnCompletions);//add returnCompletions to returnData document
		return returnData;
	}
	
	//test method to redo everything
	public static Document queryResults(String inputWord, int size)
	{
		Document returnResult = new Document();
		
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db1Gram = mongoClient.getDatabase("oneGram");
		MongoDatabase db2Gram = mongoClient.getDatabase("twoGram");
		MongoDatabase db3Gram = mongoClient.getDatabase("threeGram");
		MongoDatabase db4Gram = mongoClient.getDatabase("fourGram");
		
		int posOfBreak = 0;//will hold to position of a space in input word to tokenize it
		String inputWordCut = inputWord;//a copy of the input word that will be tokenized
		String inputToken;
		List<Document> queryData = new ArrayList(10);
		
		int count = 0;//tells what gram we are at
		//querying the four different inputs
		for(int i = 0; i < 4; i++)
		{
			int jLoopLength = 4 - i;//determines how many times the j loop will run
			posOfBreak = inputWordCut.lastIndexOf(' ');
			inputToken = inputWord.substring(posOfBreak+1, inputWord.length());
			inputWordCut = inputWordCut.substring(0, posOfBreak);
			if(posOfBreak != -1)
			{	
				BasicDBObject query = new BasicDBObject();
				query.append("text", inputToken);
				String firstTwoLetters = inputToken.substring(0, 2);
				if(size != -1)
				{
					if(size == 1  && i < 4)
					{
						queryData.add(db1Gram.getCollection((firstTwoLetters+1)).find(query).first());
					}
					else if(size == 2 && i < 3)
					{
						queryData.add(db2Gram.getCollection((firstTwoLetters+2)).find(query).first());
					}
					else if(size == 3 && i < 2)
					{
						queryData.add(db3Gram.getCollection((firstTwoLetters+3)).find(query).first());
					}
					else if(size == 4 && i < 1)
					{
						queryData.add(db4Gram.getCollection((firstTwoLetters+4)).find(query).first());
					}
				}
				else
				{
					for(int j = 0; j < jLoopLength; j++)
					{
						
						if(j == 0)
						{
							queryData.add(db1Gram.getCollection((firstTwoLetters+1)).find(query).first());
						}
						else if(j == 1)
						{
							queryData.add(db2Gram.getCollection((firstTwoLetters+2)).find(query).first());
						}
						else if(j == 2)
						{
							queryData.add(db3Gram.getCollection((firstTwoLetters+3)).find(query).first());
						}
						else if(j == 3)
						{
							queryData.add(db4Gram.getCollection((firstTwoLetters+4)).find(query).first());
						}
					}
				}
			}
			else
			{
				break;
			}
		}
		
		System.out.println("Done");
		returnResult = trimResults(queryData, inputWord);
		mongoClient.close();
		return returnResult;
	}
	
	public static Document queryResultsOld(String inputWord, int size)
	{
		List<Document> queryData = new ArrayList();
		//If we have more than 1 word query the different grams
		if(inputWord.indexOf(' ') > 0)
		{
			//Open call to MongoDB and create objects to query the database
			MongoClient mongoClient = new MongoClient();
			MongoDatabase db = mongoClient.getDatabase("ngrams");
			BasicDBObject query = new BasicDBObject();
			if(inputWord.indexOf(' ') == 0)
			{
				inputWord = inputWord.substring(1, inputWord.length());
				System.out.println(inputWord);
			}
			String tempInput = inputWord;
			
			String first2letters = "";
			int currentGramPos = inputWord.lastIndexOf(' ');
			
			tempInput = inputWord.substring(0, currentGramPos);
			String currentGram = inputWord.substring(currentGramPos+1, inputWord.length());
			
			query.append("word", currentGram);
			first2letters = currentGram.substring(0, 2);
			//query for the data
			//Document myDoc = db.getCollection(first2letters).find(query).first();
			//queryData.add(myDoc);
			
			query.remove("word");
			currentGramPos = tempInput.lastIndexOf(' ');
			currentGram = inputWord.substring(currentGramPos+1, inputWord.length());
			tempInput = inputWord.substring(0, currentGramPos);
			query.append("word", currentGram);
			first2letters = currentGram.substring(0, 2);
			//query for the data
			//Document myDoc = db.getCollection(first2letters).find(query).first();
			//queryData.add(myDoc);
			
			System.out.println(tempInput);
			System.out.println(currentGram);
			
			query.remove("word");
			currentGramPos = tempInput.lastIndexOf(' ');
			currentGram = inputWord.substring(currentGramPos+1, inputWord.length());
			tempInput = inputWord.substring(0, currentGramPos);
			query.append("word", currentGram);
			first2letters = currentGram.substring(0, 2);
			//query for the data
			//Document myDoc = db.getCollection(first2letters).find(query).first();
			//queryData.add(myDoc);
			
			System.out.println(tempInput);
			System.out.println(currentGram);
			
			query.remove("word");
			currentGramPos = tempInput.lastIndexOf(' ');
			if(currentGramPos == -1)
			{
				currentGramPos = 0;
				currentGram = inputWord.substring(currentGramPos, inputWord.length());//dont need a +1 for this one because it will cut off the first letter
				tempInput = inputWord.substring(0, currentGramPos);
			}
			else
			{
				currentGram = inputWord.substring(currentGramPos+1, inputWord.length());
				tempInput = inputWord.substring(0, currentGramPos);
			}
			query.append("word", currentGram);
			first2letters = currentGram.substring(0, 2);
			//query for the data
			//Document myDoc = db.getCollection(first2letters).find(query).first();
			//queryData.add(myDoc);
			
			System.out.println(tempInput);
			System.out.println(currentGram);
			
			
			
			
			/*
			System.out.println(myDoc.toString()); //testing purposes
			System.out.println(fakeData.fabricateData1Gram()); //testing purposes
			*/			
		}
		FakeDataMaker fakeDataMaker = new FakeDataMaker();
		List<Document> fakeData = fakeDataMaker.fabricateAllData();
		//System.out.println(fakeDataMaker.fabricateData1Gram()); //testing purposes
		
		//use this code when actually quering MongoDB
		/*String first2letters = "";
		first2letters = inputWord.substring(0, 2);
		//System.out.println(first2letters); //testing
		
		
		
		mongoClient.close();*/
		return trimResultsOld(fakeData);//myDoc;
	}
	
	public static Document trimResultsOld(List<Document> queryData)
	{
		ArrayList allReadyHave = new ArrayList();//List that will hold words that we already have to avoid duplicates
		ArrayList allFollowingFrequency = new ArrayList();//List that will hold all of the results we want to return
		Document result = new Document();
		
		Document gramData = queryData.get(0);
		result.append("word", gramData.get("word"));//adding the word now so the format of the returned document matches proper conventions
		
		//loop through all 4 gram data sets. Start with 4 grams at the end of the list because they are the most important
		for(int i = 3; i >= 0; i--)
		{
			gramData = queryData.get(i);
			System.out.println(gramData);
			List<Document> followingFrequency = (List<Document>) gramData.get("following_frequency");// get the following frequency List
			
			Document tempData;
			//loop through each word in the following frequency list
			for(int j = 0;j < followingFrequency.size(); j++)
			{
				tempData = (Document) followingFrequency.get(j);// get the following word
				//if the following word does not meet trim percent based its "word" gram number, remove it
				if( (double) tempData.get("percent_compared") < 15 && i == 0)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 13 && i == 1)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 11 && i == 2)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 10 && i == 3)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if(allReadyHave.contains(tempData.get("text")) == true )
				{
					//removes duplicates from the data that will be returned
					//System.out.println(tempData.get("text")); //testing
					followingFrequency.remove(j);
					j--;
				}
				else
				{
					allReadyHave.add(tempData.get("text"));
				}
			}
			//System.out.println(followingFrequency);
			allFollowingFrequency.addAll(followingFrequency);
		}
	
		//System.out.println(allFollowingFrequency);
		//Only want to return 10 results to the users
		while(allFollowingFrequency.size() > 10)
		{
			allFollowingFrequency.remove(10);
		}
		
		result.append("following_frequency", allFollowingFrequency);
		System.out.println(result);
		
		
		
		//System.out.println( result.toString());
		return result;
	}
	
	public static void main(String args[]) throws IOException   {
		long totalTime = 0;
		
		NextWord test = new NextWord();
		for(int i=0;i<11;i++)
		{
			
			
			final long startTime = System.currentTimeMillis();
			Document allData = queryResults(" when you wish upon", -1);
			System.out.println(allData.toString());
			
			/*MongoClient mongoClient = new MongoClient();
			MongoDatabase db = mongoClient.getDatabase("ngrams");*/
			//{"name": /m/}
			/*BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put("word",
					new BasicDBObject("$regex", "vote.*")
					.append("$options", "i"));*/
			//DBCursor cursor = db.getCollection("twoGrams_wh").find( regexQuery );
			//BasicDBObject query = new BasicDBObject("gram", new BasicDBObject("$lt", "gram"));
			/*BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("word", "vote");*/
			//MongoCursor<Document> cursor2 = db.getCollection("vo_twoGrams").find(whereQuery).iterator();
			
			/*db.getCollection("vo").find(whereQuery).forEach((Block<Document>) document -> {
			    ((Iterable<Document>) document.get("following_frequency")).forEach((Consumer<? super Document>) text -> {
			    	//System.out.println(text);
			    	//System.out.println(text.get("text") + " " + text.get("appearances"));
			    });
			});*/
			//MongoCursor<Document> cursor3 = db.getCollection("threeGrams_wh").find(regexQuery).iterator();
			//FindIterable<Document> iterable = db.getCollection("twoGrams_wh").find(new Document( "gram", "what Anya" ));
			
			//Document test = iterable.first();
			/*while(cursor2.hasNext())
			{
				System.out.println(cursor2.next());
			}*/
			//System.out.println(test);
			
			//test.topWords(cursor2);
			
			//test.topWords(cursor3);
			
			/*while(cursor2.hasNext() == true)
			{
				System.out.println(cursor2.next().toString());
				//cursor2.next();
			}*/
			
			//mongoClient.close();
			
			final long endTime = System.currentTimeMillis();
			
			if(i>0)
			{
				totalTime += (endTime - startTime);
			}
			//System.out.println("execution time: " + (endTime - startTime) );
			
			
			
			
		}
		System.out.println("average execution time: " + (totalTime/10) );
		
		
	}
}
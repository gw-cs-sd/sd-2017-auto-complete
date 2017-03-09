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
	
	public static Document queryResults(String inputWord)
	{
		FakeDataMaker fakeDataMaker = new FakeDataMaker();
		List<List> fakeData = fakeDataMaker.fabricateAllData();
		//System.out.println(fakeDataMaker.fabricateData1Gram()); //testing purposes
		
		//use this code when actually quering MongoDB
		/*String first2letters = "";
		first2letters = inputWord.substring(0, 2);
		//System.out.println(first2letters); //testing
		
		MongoClient mongoClient = new MongoClient();
		
		MongoDatabase db = mongoClient.getDatabase("ngrams");
		BasicDBObject query = new BasicDBObject();
		query.put("word", inputWord);
		
		Document myDoc = db.getCollection(first2letters).find(query).first();
		System.out.println(myDoc.toString()); //testing purposes
		System.out.println(fakeData.fabricateData1Gram()); //testing purposes
		
		mongoClient.close();*/
		return trimResults(fakeData);//myDoc;
	}
	
	public static Document trimResults(List<List> queryData)
	{
		ArrayList allReadyHave = new ArrayList();//List that will hold words that we already have to avoid duplicates
		ArrayList allFollowingFrequency = new ArrayList();//List that will hold all of the results we want to return
		Document result = new Document();
		
		List gramData = queryData.get(0);
		Document gramDoc = (Document) gramData.get(0);
		result.append("word", gramDoc.get("word"));//adding the word now so the format of the returned document matches proper conventions
		
		//loop through all 4 gram data sets
		for(int i = 0; i < queryData.size(); i++)
		{
			gramData = queryData.get(i);
			gramDoc = (Document) gramData.get(0);//this will always be 0 because the List can only have 1 set of data per gram
			List<Document> followingFrequency = (List<Document>) gramDoc.get("following_frequency");// get the following frequency List
			
			Document tempData;
			//loop through each word in the following frequency list
			for(int j = 0;j < followingFrequency.size(); j++)
			{
				tempData = (Document) followingFrequency.get(j);// get the following word
				//if the following word does not meet trim percent based its "word" gram number, remove it
				if( (double) tempData.get("percent_compared") < 10 && i == 0)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 11 && i == 1)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 13 && i == 2)
				{
					followingFrequency.remove(j);
					j--;
				}
				else if( (double) tempData.get("percent_compared") < 15 && i == 3)
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
			
			allFollowingFrequency.addAll(followingFrequency);
		}
	
		//System.out.println(allFollowingFrequency);
		
		result.append("following_frequency", allFollowingFrequency);
		System.out.println(result);
		
		
		
		//System.out.println( result.toString());
		return result;
	}
	
	public static void main(String args[]) throws IOException   {
		long totalTime = 0;
		
		NextWord test = new NextWord();
		for(int i=0;i<1;i++)
		{
			
			
			final long startTime = System.currentTimeMillis();
			Document allData = queryResults("vote");
			//System.out.println(allData.toString());
			
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
		//System.out.println("average execution time: " + (totalTime/10) );
		
		
	}
}
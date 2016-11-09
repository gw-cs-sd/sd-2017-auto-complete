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

public class HelloWordNgram {
	
	public static void main(String args[]) throws IOException   {
		long totalTime = 0;
		
		NextWord test = new NextWord();
		for(int i=0;i<101;i++)
		{
			final long startTime = System.currentTimeMillis();
			
			MongoClient mongoClient = new MongoClient();
			MongoDatabase db = mongoClient.getDatabase("ngrams");
			//{"name": /m/}
			BasicDBObject regexQuery = new BasicDBObject();
			regexQuery.put("word",
					new BasicDBObject("$regex", "vote.*")
					.append("$options", "i"));
			//DBCursor cursor = db.getCollection("twoGrams_wh").find( regexQuery );
			//BasicDBObject query = new BasicDBObject("gram", new BasicDBObject("$lt", "gram"));
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("word", "Vote");
			MongoCursor<Document> cursor2 = db.getCollection("vo_twoGrams").find(whereQuery).iterator();
			//MongoCursor<Document> cursor2 = db.getCollection("vo_twoGrams").find(regexQuery).iterator(); //.sort(new BasicDBObject("gram", 1))
			//MongoCursor<Document> cursor3 = db.getCollection("threeGrams_wh").find(regexQuery).iterator();
			//FindIterable<Document> iterable = db.getCollection("twoGrams_wh").find(new Document( "gram", "what Anya" ));
			
			//Document test = iterable.first();
			/*while(cursor2.hasNext())
			{
				System.out.println(cursor2.next());
			}*/
			//System.out.println(test);
			
			test.topWords(cursor2);
			
			//test.topWords(cursor3);
			
			/*while(cursor2.hasNext() == true)
			{
				System.out.println(cursor2.next().toString());
				//cursor2.next();
			}*/
			
			mongoClient.close();
			
			final long endTime = System.currentTimeMillis();
			
			if(i>0)
			{
				totalTime += (endTime - startTime);
			}
			//System.out.println("execution time: " + (endTime - startTime) );
			
			
			
			
		}
		System.out.println("average execution time: " + (totalTime/100) );
		
		
	}
}
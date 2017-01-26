package gwcssd.autocomplete;

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
import java.util.Map;
import java.util.function.Consumer;

public class MongoQuerier {

	public static Document queryResults(String inputWord)
	{
		String first2letters = "";
		first2letters = inputWord.substring(0, 2);
		//System.out.println(first2letters); //testing

		MongoClient mongoClient = new MongoClient();

		MongoDatabase db = mongoClient.getDatabase("ngrams");
		BasicDBObject query = new BasicDBObject();
		query.put("word", inputWord);

		Document myDoc = db.getCollection(first2letters).find(query).first();
		//System.out.println(myDoc.toString()); //testing purposes

		mongoClient.close();
		return myDoc;
	}
}

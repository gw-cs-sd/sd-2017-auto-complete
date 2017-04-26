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
				if( (int)gramData.get("gramLength") == 1 && (double)completion.get("score") < .001)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 2 && (double)completion.get("score") < .001)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 3 && (double)completion.get("score") < .001)
				{
					completions.remove(j);
					j--;
				}
				else if( (int)gramData.get("gramLength") == 4 && (double)completion.get("score") < .001)
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
			if(posOfBreak != -1)
			{
        inputWordCut = inputWordCut.substring(0, posOfBreak);
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

}

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

//This class creates data for testing purposes
public class FakeDataMaker {
	
	public List<Document> fabricateData1Gram()
	{
		List<Document> result = new ArrayList();
		List<Document> followingFrequency = new ArrayList();
		
		//manually putting in data for now
		Document fakeNextWord = new Document();
		fakeNextWord.append("appearances", 8183008);
		fakeNextWord.append("text", "for");
		fakeNextWord.append("percent_compared", 23.78);
		followingFrequency.add(fakeNextWord);
		//System.out.println(followingFrequency.get(0).toString());
		
		Document fakeNextWord1 = new Document();
		fakeNextWord1.append("appearances", 3858126);
		fakeNextWord1.append("text", "of");
		fakeNextWord1.append("percent_compared", 11.21);
		followingFrequency.add(fakeNextWord1);
		//System.out.println(followingFrequency.get(1).toString());
		
		Document fakeNextWord2 = new Document();
		fakeNextWord2.append("appearances", 3639476);
		fakeNextWord2.append("text", "for the");
		fakeNextWord2.append("percent_compared", 10.58);
		followingFrequency.add(fakeNextWord2);
		//System.out.println(followingFrequency.get(2).toString());
		
		Document fakeNextWord3 = new Document();
		fakeNextWord3.append("appearances", 3622265);
		fakeNextWord3.append("text", "in");
		fakeNextWord3.append("percent_compared", 10.53);
		followingFrequency.add(fakeNextWord3);
		//System.out.println(followingFrequency.get(3).toString());
		
		Document fakeNextWord4 = new Document();
		fakeNextWord4.append("appearances", 3232694);
		fakeNextWord4.append("text", "on");
		fakeNextWord4.append("percent_compared", 9.40);
		followingFrequency.add(fakeNextWord4);
		//System.out.println(followingFrequency.get(4).toString());

		Document fakeNextWord5 = new Document();
		fakeNextWord5.append("appearances", 3053352);
		fakeNextWord5.append("text", "of");
		fakeNextWord5.append("percent_compared", 8.87);
		followingFrequency.add(fakeNextWord5);
		//System.out.println(followingFrequency.get(5).toString());
		
		Document fakeNextWord6 = new Document();
		fakeNextWord6.append("appearances", 2663828);
		fakeNextWord6.append("text", "in the");
		fakeNextWord6.append("percent_compared", 7.74);
		followingFrequency.add(fakeNextWord6);
		//System.out.println(followingFrequency.get(6).toString());
		
		Document fakeNextWord7 = new Document();
		fakeNextWord7.append("appearances", 2214430);
		fakeNextWord7.append("text", "was");
		fakeNextWord7.append("percent_compared", 6.43);
		followingFrequency.add(fakeNextWord7);
		//System.out.println(followingFrequency.get(7).toString());
		
		Document fakeNextWord8 = new Document();
		fakeNextWord8.append("appearances", 8183008);
		fakeNextWord8.append("text", "for");
		fakeNextWord8.append("percent_compared", 4.21);
		followingFrequency.add(fakeNextWord8);
		//System.out.println(followingFrequency.get(8).toString());
		
		Document fakeNextWord9 = new Document();
		fakeNextWord9.append("appearances", 1949646);
		fakeNextWord9.append("text", "to");
		fakeNextWord9.append("percent_compared", 5.67);
		followingFrequency.add(fakeNextWord9);
		//System.out.println(followingFrequency.get(9).toString());
			
		Document fakeDataReturn = new Document();
		fakeDataReturn.append("_id", "5858be5ba9d09b027a8e4190");
		fakeDataReturn.append("word", "vote");
		fakeDataReturn.append("following_frequency", followingFrequency);
		result.add(fakeDataReturn);
		//System.out.println(fakeDataReturn.toString());
		return result;	
	}

	//TODO implement. does same as fabricateData1Gram
	public List<Document> fabricateData2Gram()
	{
		List<Document> result = new ArrayList();
		List<Document> followingFrequency = new ArrayList();
		
		//manually putting in data for now
		Document fakeNextWord = new Document();
		fakeNextWord.append("appearances", 8183008);
		fakeNextWord.append("text", "for");
		fakeNextWord.append("percent_compared", 23.78);
		followingFrequency.add(fakeNextWord);
		//System.out.println(followingFrequency.get(0).toString());
		
		Document fakeNextWord1 = new Document();
		fakeNextWord1.append("appearances", 3858126);
		fakeNextWord1.append("text", "of");
		fakeNextWord1.append("percent_compared", 11.21);
		followingFrequency.add(fakeNextWord1);
		//System.out.println(followingFrequency.get(1).toString());
		
		Document fakeNextWord2 = new Document();
		fakeNextWord2.append("appearances", 3639476);
		fakeNextWord2.append("text", "for the");
		fakeNextWord2.append("percent_compared", 10.58);
		followingFrequency.add(fakeNextWord2);
		//System.out.println(followingFrequency.get(2).toString());
		
		Document fakeNextWord3 = new Document();
		fakeNextWord3.append("appearances", 3622265);
		fakeNextWord3.append("text", "in");
		fakeNextWord3.append("percent_compared", 10.53);
		followingFrequency.add(fakeNextWord3);
		//System.out.println(followingFrequency.get(3).toString());
		
		Document fakeNextWord4 = new Document();
		fakeNextWord4.append("appearances", 3232694);
		fakeNextWord4.append("text", "on");
		fakeNextWord4.append("percent_compared", 9.40);
		followingFrequency.add(fakeNextWord4);
		//System.out.println(followingFrequency.get(4).toString());

		Document fakeNextWord5 = new Document();
		fakeNextWord5.append("appearances", 3053352);
		fakeNextWord5.append("text", "of");
		fakeNextWord5.append("percent_compared", 8.87);
		followingFrequency.add(fakeNextWord5);
		//System.out.println(followingFrequency.get(5).toString());
		
		Document fakeNextWord6 = new Document();
		fakeNextWord6.append("appearances", 2663828);
		fakeNextWord6.append("text", "in the");
		fakeNextWord6.append("percent_compared", 7.74);
		followingFrequency.add(fakeNextWord6);
		//System.out.println(followingFrequency.get(6).toString());
		
		Document fakeNextWord7 = new Document();
		fakeNextWord7.append("appearances", 2214430);
		fakeNextWord7.append("text", "was");
		fakeNextWord7.append("percent_compared", 6.43);
		followingFrequency.add(fakeNextWord7);
		//System.out.println(followingFrequency.get(7).toString());
		
		Document fakeNextWord8 = new Document();
		fakeNextWord8.append("appearances", 8183008);
		fakeNextWord8.append("text", "for");
		fakeNextWord8.append("percent_compared", 4.21);
		followingFrequency.add(fakeNextWord8);
		//System.out.println(followingFrequency.get(8).toString());
		
		Document fakeNextWord9 = new Document();
		fakeNextWord9.append("appearances", 1949646);
		fakeNextWord9.append("text", "to");
		fakeNextWord9.append("percent_compared", 5.67);
		followingFrequency.add(fakeNextWord9);
		//System.out.println(followingFrequency.get(9).toString());
			
		Document fakeDataReturn = new Document();
		fakeDataReturn.append("_id", "5858be5ba9d09b027a8e4190");
		fakeDataReturn.append("word", "vote");
		fakeDataReturn.append("following_frequency", followingFrequency);
		result.add(fakeDataReturn);
		//System.out.println(fakeDataReturn.toString());
		return result;	
	}

	//TODO implement. does same as fabricateData1Gram
	public List<Document> fabricateData3Gram()
	{
		List<Document> result = new ArrayList();
		List<Document> followingFrequency = new ArrayList();
		
		//manually putting in data for now
		Document fakeNextWord = new Document();
		fakeNextWord.append("appearances", 8183008);
		fakeNextWord.append("text", "for");
		fakeNextWord.append("percent_compared", 23.78);
		followingFrequency.add(fakeNextWord);
		//System.out.println(followingFrequency.get(0).toString());
		
		Document fakeNextWord1 = new Document();
		fakeNextWord1.append("appearances", 3858126);
		fakeNextWord1.append("text", "of");
		fakeNextWord1.append("percent_compared", 11.21);
		followingFrequency.add(fakeNextWord1);
		//System.out.println(followingFrequency.get(1).toString());
		
		Document fakeNextWord2 = new Document();
		fakeNextWord2.append("appearances", 3639476);
		fakeNextWord2.append("text", "for the");
		fakeNextWord2.append("percent_compared", 10.58);
		followingFrequency.add(fakeNextWord2);
		//System.out.println(followingFrequency.get(2).toString());
		
		Document fakeNextWord3 = new Document();
		fakeNextWord3.append("appearances", 3622265);
		fakeNextWord3.append("text", "in");
		fakeNextWord3.append("percent_compared", 10.53);
		followingFrequency.add(fakeNextWord3);
		//System.out.println(followingFrequency.get(3).toString());
		
		Document fakeNextWord4 = new Document();
		fakeNextWord4.append("appearances", 3232694);
		fakeNextWord4.append("text", "on");
		fakeNextWord4.append("percent_compared", 9.40);
		followingFrequency.add(fakeNextWord4);
		//System.out.println(followingFrequency.get(4).toString());

		Document fakeNextWord5 = new Document();
		fakeNextWord5.append("appearances", 3053352);
		fakeNextWord5.append("text", "of");
		fakeNextWord5.append("percent_compared", 8.87);
		followingFrequency.add(fakeNextWord5);
		//System.out.println(followingFrequency.get(5).toString());
		
		Document fakeNextWord6 = new Document();
		fakeNextWord6.append("appearances", 2663828);
		fakeNextWord6.append("text", "in the");
		fakeNextWord6.append("percent_compared", 7.74);
		followingFrequency.add(fakeNextWord6);
		//System.out.println(followingFrequency.get(6).toString());
		
		Document fakeNextWord7 = new Document();
		fakeNextWord7.append("appearances", 2214430);
		fakeNextWord7.append("text", "was");
		fakeNextWord7.append("percent_compared", 6.43);
		followingFrequency.add(fakeNextWord7);
		//System.out.println(followingFrequency.get(7).toString());
		
		Document fakeNextWord8 = new Document();
		fakeNextWord8.append("appearances", 8183008);
		fakeNextWord8.append("text", "for");
		fakeNextWord8.append("percent_compared", 4.21);
		followingFrequency.add(fakeNextWord8);
		//System.out.println(followingFrequency.get(8).toString());
		
		Document fakeNextWord9 = new Document();
		fakeNextWord9.append("appearances", 1949646);
		fakeNextWord9.append("text", "to");
		fakeNextWord9.append("percent_compared", 5.67);
		followingFrequency.add(fakeNextWord9);
		//System.out.println(followingFrequency.get(9).toString());
			
		Document fakeDataReturn = new Document();
		fakeDataReturn.append("_id", "5858be5ba9d09b027a8e4190");
		fakeDataReturn.append("word", "vote");
		fakeDataReturn.append("following_frequency", followingFrequency);
		result.add(fakeDataReturn);
		//System.out.println(fakeDataReturn.toString());
		return result;	
	}
	
	//TODO implement. does same as fabricateData1Gram
	public List<Document> fabricateData4Gram()
	{
		List<Document> result = new ArrayList();
		List<Document> followingFrequency = new ArrayList();
		
		//manually putting in data for now
		Document fakeNextWord = new Document();
		fakeNextWord.append("appearances", 8183008);
		fakeNextWord.append("text", "for");
		fakeNextWord.append("percent_compared", 23.78);
		followingFrequency.add(fakeNextWord);
		//System.out.println(followingFrequency.get(0).toString());
		
		Document fakeNextWord1 = new Document();
		fakeNextWord1.append("appearances", 3858126);
		fakeNextWord1.append("text", "of");
		fakeNextWord1.append("percent_compared", 11.21);
		followingFrequency.add(fakeNextWord1);
		//System.out.println(followingFrequency.get(1).toString());
		
		Document fakeNextWord2 = new Document();
		fakeNextWord2.append("appearances", 3639476);
		fakeNextWord2.append("text", "for the");
		fakeNextWord2.append("percent_compared", 10.58);
		followingFrequency.add(fakeNextWord2);
		//System.out.println(followingFrequency.get(2).toString());
		
		Document fakeNextWord3 = new Document();
		fakeNextWord3.append("appearances", 3622265);
		fakeNextWord3.append("text", "in");
		fakeNextWord3.append("percent_compared", 10.53);
		followingFrequency.add(fakeNextWord3);
		//System.out.println(followingFrequency.get(3).toString());
		
		Document fakeNextWord4 = new Document();
		fakeNextWord4.append("appearances", 3232694);
		fakeNextWord4.append("text", "on");
		fakeNextWord4.append("percent_compared", 9.40);
		followingFrequency.add(fakeNextWord4);
		//System.out.println(followingFrequency.get(4).toString());

		Document fakeNextWord5 = new Document();
		fakeNextWord5.append("appearances", 3053352);
		fakeNextWord5.append("text", "of");
		fakeNextWord5.append("percent_compared", 8.87);
		followingFrequency.add(fakeNextWord5);
		//System.out.println(followingFrequency.get(5).toString());
		
		Document fakeNextWord6 = new Document();
		fakeNextWord6.append("appearances", 2663828);
		fakeNextWord6.append("text", "in the");
		fakeNextWord6.append("percent_compared", 7.74);
		followingFrequency.add(fakeNextWord6);
		//System.out.println(followingFrequency.get(6).toString());
		
		Document fakeNextWord7 = new Document();
		fakeNextWord7.append("appearances", 2214430);
		fakeNextWord7.append("text", "was");
		fakeNextWord7.append("percent_compared", 6.43);
		followingFrequency.add(fakeNextWord7);
		//System.out.println(followingFrequency.get(7).toString());
		
		Document fakeNextWord8 = new Document();
		fakeNextWord8.append("appearances", 8183008);
		fakeNextWord8.append("text", "for");
		fakeNextWord8.append("percent_compared", 4.21);
		followingFrequency.add(fakeNextWord8);
		//System.out.println(followingFrequency.get(8).toString());
		
		Document fakeNextWord9 = new Document();
		fakeNextWord9.append("appearances", 1949646);
		fakeNextWord9.append("text", "to");
		fakeNextWord9.append("percent_compared", 5.67);
		followingFrequency.add(fakeNextWord9);
		//System.out.println(followingFrequency.get(9).toString());
			
		Document fakeDataReturn = new Document();
		fakeDataReturn.append("_id", "5858be5ba9d09b027a8e4190");
		fakeDataReturn.append("word", "vote");
		fakeDataReturn.append("following_frequency", followingFrequency);
		result.add(fakeDataReturn);
		//System.out.println(fakeDataReturn.toString());
		return result;	
	}
	
}

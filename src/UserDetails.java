import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;


public class UserDetails {
	public List<Status> gettimeline(String username){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("bNQCmLEnor57Byf4X1YkC3PoC");
		cb.setOAuthConsumerSecret("tlKPB7591X4LtIW4qM8HOwHrYSlWTQbBRyyUukPD6HPsRP4kAp");
		cb.setOAuthAccessToken("2340064458-Rig32mLfFdu0SETAZY34j9nGeP0h9t8fR2ls0Dk");
		cb.setOAuthAccessTokenSecret("taLbBeeJBuQnh23r6foMvcXlAFZhlAb83jectccnMahoi");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		boolean exists = false;
		System.out.println("inside the func");
		List<Status> statuses;
		try{
			List<Status> statuse = twitter.getUserTimeline(username);
			File file = new File("TestTweets2.txt");
			User currUser = twitter.showUser(username);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			int val = currUser.getStatusesCount();
			if(val>=500){
				
				StringBuilder twits = new StringBuilder();
				twits.append(currUser.getScreenName()+";;"+currUser.getTimeZone()+";;"+currUser.getFavouritesCount()+";;"+currUser.getFollowersCount()+";;"+currUser.getFriendsCount()+";;"+currUser.getName()+";;;;;");				
				for(int pageNo=1;pageNo<=5;++pageNo){
					statuses = twitter.getUserTimeline(username, new Paging(pageNo,100));		
	        		for (Status status1 : statuses) {
	        			if(status1.isTruncated()||(!status1.getLang().contains("en")))
	        				continue;
	            		//System.out.println("@" + status1.getUser().getScreenName() + " - " + status1.getText());
	            //		if(!status1.getText().toString().contains("???")){
	        			//System.out.println(status1.getCreatedAt().toString());
	        			
	            		twits.append(status1.getCreatedAt()+";;"+status1.getFavoriteCount()+";;"+status1.getRetweetCount()+";;"+status1.isPossiblySensitive()+";;"+status1.getText()+";;;;;");
	                        
	            //}
	        		}
				}
				
					twits.append("#####");
					twits.append("\n");
					bw.write(twits.toString());
					bw.close();
					
				
			}
		  twitter.showUser(username);
		  exists = true;
		  System.out.println("true");
		  
		  return statuse;
		}catch(TwitterException te){
		  if(te.getStatusCode() == 404){
		    exists = false;
		    return null;
		  }else{
			  return null;
		    // exception handling
		  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}

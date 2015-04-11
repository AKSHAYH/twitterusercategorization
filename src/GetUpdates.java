import java.io.BufferedWriter;
import javax.swing.JLabel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class GetUpdates {

	String name[];
	int i;
	List<Status>Target_status;
	int field=0;
	public GetUpdates(final int count) {
		name = new String[100];
		i=0;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("bNQCmLEnor57Byf4X1YkC3PoC");
		cb.setOAuthConsumerSecret("tlKPB7591X4LtIW4qM8HOwHrYSlWTQbBRyyUukPD6HPsRP4kAp");
		cb.setOAuthAccessToken("2340064458-Rig32mLfFdu0SETAZY34j9nGeP0h9t8fR2ls0Dk");
		cb.setOAuthAccessTokenSecret("taLbBeeJBuQnh23r6foMvcXlAFZhlAb83jectccnMahoi");
		TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
		final TwitterStream twitterStream = tf.getInstance();
		StatusListener listener = new StatusListener() {

			@Override
			public void onStatus(Status status) {
				if(!status.getUser().getLang().startsWith("en"))
					return;
			System.out.println(status.getUser().getLang());
				name[i]=status.getUser().getScreenName()+";;"+status.getUser().getTimeZone()+";;"+status.getUser().getFavouritesCount()+";;"+status.getUser().getFollowersCount()+";;"+status.getUser().getFriendsCount()+";;"+status.getUser().getName()+";;;;;";
				System.out.println(name[i]);
				i=i+1;
			System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			if(i==count){

					twitterStream.clearListeners();
					twitterStream.shutdown();
				try {
					 
					String content = "This is the content to write into file";

					File file = new File("new50.txt");
					File file1 = new File("/users/akshay/tweets2.txt");
					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
					if (!file1.exists()) {
						file1.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					
					FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
					BufferedWriter bw1 = new BufferedWriter(fw1);
					
					bw1.write("status CreatedAt"+";"+"status FavoriteCount"+";"+"status RetweetCount"+";"+"status isPossiblySensitive"+";"+"status");
					
					bw1.newLine();
					for(int j=0;j<i;j++)
					{
						ConfigurationBuilder cb = new ConfigurationBuilder();
						cb.setDebugEnabled(true);
						cb.setOAuthConsumerKey("bNQCmLEnor57Byf4X1YkC3PoC");
						cb.setOAuthConsumerSecret("tlKPB7591X4LtIW4qM8HOwHrYSlWTQbBRyyUukPD6HPsRP4kAp");
						cb.setOAuthAccessToken("2340064458-Rig32mLfFdu0SETAZY34j9nGeP0h9t8fR2ls0Dk");
						cb.setOAuthAccessTokenSecret("taLbBeeJBuQnh23r6foMvcXlAFZhlAb83jectccnMahoi");
						TwitterFactory tf = new TwitterFactory(cb.build());
						Twitter twitter = tf.getInstance();
						
						System.out.println(name[j]);
						
						
						List<Status> statuses;
						int flag=0;
						StringBuilder twits = new StringBuilder();
						statuses = twitter.getUserTimeline(name[j].split(";;")[0]);
			            for (Status status1 : statuses) {
			            	if(status1.isTruncated()||(!status1.getLang().contains("en")))
			            		continue;
			            	if(!status1.getText().toString().contains(";")){
			            	flag=1;
			                System.out.println("@" + status1.getUser().getScreenName() + " - " + status1.getText());
			                twits.append(status1.getCreatedAt()+";;"+status1.getFavoriteCount()+";;"+status1.getRetweetCount()+";;"+status1.isPossiblySensitive()+";;"+status1.getText()+";;;;;");
			            	}
			            }
			            if(flag==1){
			            bw.write(name[j]);
			            bw.write(twits.toString());
						bw.write("#####");
			            bw.newLine();
			            }
					}
					
					bw.close();
					bw1.close();
					System.out.println("Done");
					field=1;
					
					//System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
			}
			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}
			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}
			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}
			@Override
			public void onStallWarning(StallWarning warning) {
			System.out.println("Got stall warning:" + warning);
			}
			@Override
			public void onException(Exception ex) {
			ex.printStackTrace();
			}
			};
			twitterStream.addListener(listener);
			twitterStream.sample();
			}

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
		try{
		  twitter.showUser(username);
		  exists = true;
		  System.out.println("true");
		  List<Status> statuses = twitter.getUserTimeline(username);
		  return statuses;
		}catch(TwitterException te){
		  if(te.getStatusCode() == 404){
		    exists = false;
		    return null;
		  }else{
			  return null;
		    // exception handling
		  }
		}
		
	}
		// TODO Auto-generated constructor stub
	}


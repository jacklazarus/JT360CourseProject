/*	Jackson Taylor
 *	CS360 
 * 	12/5/2017
 * 	Tutorial on AudioPlayer and MaryTTS 
 * 	from https://github.com/goxr3plus/Java-Text-To-Speech-Tutorial
 * 	acknowledgments left in the source files.
 */
package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.List;




import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
import edu.stanford.nlp.trees.Tree;

public class SpeechRecognizerMain {
	   public static void main(String[] args) throws IOException {
	        //Configuration Object
	        Configuration configuration = new Configuration();
	 
	        //Instantiate MaryTTS
	        TextToSpeech tts = new TextToSpeech();
	        	tts.setVoice("cmu-slt-hsmm");
	        // Set path to the acoustic model.
	        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
	        // Set path to the dictionary.
	        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
	        // Set path to the language model.
	        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
			// Grammars
			configuration.setGrammarPath("resource:/grammars");
			configuration.setGrammarName("grammar");
			configuration.setUseGrammar(true); 
	        //Recognizer object, Pass the Configuration object
	        LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);
	 
	        //Start Recognition Process (The bool parameter clears the previous cache if true)
	        recognize.startRecognition(true);
	        //Creating SpeechResult object
	        SpeechResult result;
	        try {
				// 1. Get a connection to  database
				Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root","newj1916");
				//2. Get a sentence from voice input
				
				//3 Run the sentence through a parser? Sphinx disabled tagging and rule matching from the grammar for some reason
				
				
				
				//4 Analyze the result - for a matching grammar pattern
				
				
				//5 Execute appropriate SQL based on the matched pattern or error message
					//Pattern 0 a single NNP (proper noun) Look through tables for matching value read out any matching rows
				
					//Pattern 1 NN* (a list of nouns i.e. actors, movies) Search for tables with matching column names read out all matches
				
					//Pattern NN* [comparator] [value] now we want to do some action on a column, search for tables with matching 
					//column names (NN) then evaluate each row in the column for the comparator value arrangement.
				
				//6 Read result with MaryTTS out to user - loop back to start waiting for another voice input
				
				//Statement myStmt = myConn.createStatement();
				/*ResultSet myRsA = myStmt.executeQuery("select * from actor");
				while (myRsA.next()) 
				{
					//System.out.println(myRsA.getString("last_name"));
					//System.out.println(myRsA.getString("first_name"));
				}
				*/
				//ResultSet myRsM = myStmt.executeQuery("select * from film");
				//while (myRsM.next()) 
				//{
					//System.out.println(myRsM.getString("title"));
					//System.out.println(myRsM.getString("description"));
				//}
				//3. Execute SQL query 
				//ResultSet myRs = myStmt.executeQuery("select * from actor");
				//4. Process Result Set
				
//create booleans for parsing voice result
	            Boolean containsACTORS = false;
	            Boolean containsMOVIES = false;
	            Boolean containsWHO = false;
	            Boolean containsactors = false;
	            Boolean containsmovies = false;
	            Boolean containsrelations = false;				
					

	 
	        //Check if recognizer recognized the speech
	        while ((result = recognize.getResult()) != null) {
	 
	            //Get the recognized speech
	            String command = result.getHypothesis();
	            String work = null;
	            	System.out.println(command);
	            //set booleans to false at the start of every loop	
	             containsACTORS = false;
	             containsMOVIES = false;
	             containsWHO = false;
	             containsactors = false;
	             containsmovies = false;
	             containsrelations = false;
	            
	            //Parse the command string it should match a few predetermined rules
	            //we will be using command.toLowerCase().contains(stringX.toLowerCase() to evaluate and set some booleans to decide what to do with the spoken command
	            //booleans; -containsACTORS - containsMOVIES - containsWHO  -containsactors -containsmovies -containsrelations
	            	if (command.toLowerCase().contains("actress") || command.toLowerCase().contains("actor") || command.toLowerCase().contains("actors"))
	            	{
	            		containsACTORS = true;
	            	}
	            
	            	if (command.toLowerCase().contains("movie") || command.toLowerCase().contains("film") || command.toLowerCase().contains("films") || command.toLowerCase().contains("movies"))
	            	{
	            		containsMOVIES = true;
	            	}
	            	
	            	if (command.toLowerCase().contains("who"))
	            	{
	            		containsWHO = true;
	            	}
	            	
	            	if (command.toLowerCase().contains("chase") || command.toLowerCase().contains("ed") || command.toLowerCase().contains("swank") || command.toLowerCase().contains("joe"))
	            	{
	            		containsactors = true;
	            	}
	            	
	            	if (command.toLowerCase().contains("academy") || command.toLowerCase().contains("dinosaur") || command.toLowerCase().contains("wars") || command.toLowerCase().contains("pluto"))
	            	{
	            		containsmovies = true;
	            	}
	            	
	            	if (command.toLowerCase().contains("starred") || command.toLowerCase().contains("acted") || command.toLowerCase().contains("was"))
	            	{
	            		containsrelations = true;
	            	}
	            	
	           
	            	if(command.equalsIgnoreCase("hello")) 
	            	{
		                System.out.println("Welcome");
		                tts.speak("Good to see you!", 2.0f, false, true);
		            }
	            	
	            	if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("goodbye"))
	            	{
	            		System.out.println("Farewell");
	            		tts.speak("See you later!", 2.0f, false, true);
	            		return;
	            	}
	            	
	             	//evaluate the booleans to decide what we should do
	            	
	            		//asked about an actor 
	            	if (containsactors == true && containsWHO == false && containsrelations == false)
	            	{
	            		Statement myStmt = myConn.createStatement();
	            		ResultSet myRsM = myStmt.executeQuery("select * from actor");
	            		
	            		while (myRsM.next())
	            		{
	            			String string1 = command;
	            			//tts.speak(string1, 2.0f, false, true);
	            			String string2 = myRsM.getString("last_name");
	            			if (string1.toLowerCase().contains(string2.toLowerCase()))
	            			{
	            				String string3 = myRsM.getString("last_name") + " " +myRsM.getString("first_name");
								System.out.println(string3);
								tts.speak(string3, 2.0f, false, true);
	            			}
	            		}
	            	}
	            	
	            		//asked about a movie
	            	else if (containsmovies == true && containsWHO == false && containsrelations == false)
	            	{
	            		Statement myStmt = myConn.createStatement();
	            		ResultSet myRsM = myStmt.executeQuery("select * from film");
	            		
						while (myRsM.next()) 
						{
							String string1 = command;
							String string2 = myRsM.getString("title");
							if (string1.toLowerCase().contains(string2.toLowerCase()))
							{
								System.out.println(myRsM.getString("title"));
								//tts.speak(string2, 2.0f, false, true);
								String string3 = myRsM.getString("description");
								System.out.println(string3);
								//tts.speak(string3, 2.0f, false, true);
								String string4 = myRsM.getString("release_year");
								System.out.println(string4);
								tts.speak(string2+" came out in " + string4 +" it is about " + string3 , 2.0f, false, true);
								
							}
						}	
	            	}
	            	
	            		//asked who acted in a specific movie
	            	else if (containsmovies == true && containsWHO == true && containsrelations == true)
	            	{
	            		//Statement myStmt = myConn.createStatement();
	            		//ResultSet myRsM = myStmt.executeQuery("select * from  film");
	            	}
	            	
	            		//asked about the movies an actor has been in
	            	else if (containsmovies == false && containsWHO == false && containsrelations == true && containsactors == true)
	            	{
	            		
	            	}
	            	
	            	else 
	            	{
	            		if (!command.equalsIgnoreCase("hello"))
	            		{
	            			String string1 = "I'm sorry I didn't quite understand";
	            			tts.speak(string1, 2.0f, false, true);	
	            		}
	            	}	
	           /* if (command.equalsIgnoreCase(<lookup>));
	            {
	            	
	            }*/
	                //Parser parser = new Parser(); 
	                
	                /*
	                Tree tree = parser.parse(command);
	                
	                List<Tree> leaves = tree.getLeaves();
	                for (Tree leaf : leaves) { 
	                    Tree parent = leaf.parent(tree);
	                    System.out.print(leaf.label().value() + "-" + parent.label().value() + " ");
	                }
	                System.out.println();*/  
	            	
	 
	            /*if(command.equalsIgnoreCase("hello")) {
	                System.out.println("Welcome");
	            } else if (command.equalsIgnoreCase("names")) {
					 ResultSet myRsA = myStmt.executeQuery("select * from actor");
					while (myRsA.next()) 
					{
						System.out.println(myRsA.getString("last_name")+","+ myRsA.getString("first_name"));
					}
	            } else if (command.equalsIgnoreCase(command)) {
					 myRsM = myStmt.executeQuery("select * from film");
					//String sql = "Select * from production AS cust INNER JOIN location AS comp ON cust.location_id = comp.location_id where comp.name ='"+ locationnames +"' AND crop_id =1";
					while (myRsM.next()) 
					{
						String string1 = command;
						String string2 = myRsM.getString("title");
						if (string1.toLowerCase().contains(string2.toLowerCase()))
						{
							System.out.println(myRsM.getString("title"));
							tts.speak(string2, 2.0f, false, true);
							String string3 = myRsM.getString("description");
							System.out.println(string3);
							tts.speak(string3, 2.0f, false, true);
							
						}
					/*myRsM = myStmt.executeQuery("select * from actor");
						while (myRsM.next())
						{
							String string3 = command;
							String string4 = myRsM.getString("last_name")+" "+myRsM.getString("first_name");
							if (string3.equalsIgnoreCase(string4))
							{
								System.out.println(string4);
								tts.speak(string4, 2.0f, false, true);
							}
						}
					}
	            } else if (command.equalsIgnoreCase("exit")) {
	                System.out.println("Goodbye\n");
	                return;
	            }*/
	            //In case command recognized is none of the above hence work might be null
	        }
	        }
			catch (Exception exc) 
			{
			exc.printStackTrace();
			}	        
	    }
	}
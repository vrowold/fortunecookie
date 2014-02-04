package com.example.foodfinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class RestaurantInfoActivity extends Activity {

	
		private static final String TAG = "STOCKQUOTE";
		
		
		
		TextView titleTextView;
		TextView averageRatingsTextView;
		TextView totalRatingsTextView;
		TextView addressTextView;
		TextView cityTextView;
		TextView stateTextView;
		TextView phoneNumberTextView;
		TextView websiteUrlTextView;
		TextView lastReviewTextView;
		
		
		
		
		String title = "";
		String address = "";
		String city = "";
		String state = "";
		String phoneNumber = "";
		String averageRatings = "";
		String totalRatings = "";
		String lastReview = "";
		String websiteUrl = "";
		
		
		
		String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20query%3D%22";
		String yahooURLSecond="%22%20and%20location%3D%22";
		String yahooURLThird= "%2C%20";
		String yahooURLFourth= "%22&diagnostics=true";
		
		
		String[][] xmlPullParserArray = {{"Title", "0"}, {"Address", "0"}, {"City", "0"},
				{"State", "0"}, {"Phone", "0"},{"Latitude", "0"},{"Longitude", "0"}, {"AverageRating", "0"},
				{"TotalRatings", "0"},{"TotalReviews", "0"},{"LastReviewDate", "0"}, {"LastReviewIntro", "0"}};
				
		int parserArrayIncrement = 0;
				
		
		

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			
			setContentView(R.layout.activity_restaurant_info);
			
			
			Intent intent = getIntent();
			String food = intent.getStringExtra(MainActivity.FOOD);
			String city = intent.getStringExtra(MainActivity.CITY);
			String state = intent.getStringExtra(MainActivity.STATE);
			
			if (city.indexOf(" ") != -1) {
            	city = city.replaceAll(" ", "%20");
            }
			
			
			
			titleTextView= (TextView) findViewById(R.id.titleTextView) ;
			averageRatingsTextView= (TextView) findViewById(R.id.averageRatingTextView);
			totalRatingsTextView= (TextView) findViewById(R.id.totalRatingsTextView);
			addressTextView= (TextView) findViewById(R.id.address);
			cityTextView= (TextView) findViewById(R.id.cityTextView);
			stateTextView= (TextView) findViewById(R.id.stateTextView);
			phoneNumberTextView= (TextView) findViewById(R.id.phoneNumberTextView);
			websiteUrlTextView= (TextView) findViewById(R.id.websiteUrlTextView);
			lastReviewTextView= (TextView) findViewById(R.id.lastReviewTextView);
			
			
			Log.d(TAG, "Before URL Creation " + food);
			
			
			final String yqlURL = yahooURLFirst + food + yahooURLSecond +city+ yahooURLThird + state+ yahooURLFourth;
			
			
			
			new MyAsyncTask().execute(yqlURL);

		}
		
		
		
		private class MyAsyncTask extends AsyncTask<String, String, String>{
			
			
			protected String doInBackground(String... args) {
				
				
				
				try{
					
					Log.d("test","In XmlPullParser");
					
					
					
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

					  factory.setNamespaceAware(true);

					  
					  XmlPullParser parser = factory.newPullParser(); 

					  
					  
					  parser.setInput(new InputStreamReader(getUrlData(args[0])));  

					 
					  
					  beginDocument(parser,"query");

					  
					  int eventType = parser.getEventType();
					  String name = parser.getName();
					  
					  while (true){
						  if("results".equals(parser.getName())){
							  break;
						  }
						  parser.next();
						
					  }
					  eventType = parser.getEventType();
					  
					  do{
						  
						
						  
						  parser.next();
						  
						 

						  eventType = parser.getEventType();

						
						  
						  if(eventType == XmlPullParser.TEXT){
									  
							  
							  
							  String valueFromXML = parser.getText();
							  
							  
							  
							  xmlPullParserArray[parserArrayIncrement++][1] = valueFromXML;

						  
						  }

					  } while (parserArrayIncrement!=12) ;  
					
				}
				
				catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
				finally {
			    }
				
			
				
				return null;
			}
			
			
			
			public InputStream getUrlData(String url) throws URISyntaxException, 
	        ClientProtocolException, IOException {

				
				
				DefaultHttpClient client = new DefaultHttpClient();
			
				
				HttpGet method = new HttpGet(new URI(url));
				
				
				
				HttpResponse res = client.execute(method);
				
				
				
				return res.getEntity().getContent();
			}
			
			public final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
		    {
				int type;
				
				
				
			    while ((type=parser.next()) != parser.START_TAG
			                   && type != parser.END_DOCUMENT) {
			            ;
			    }
			
			    // Throw an error if a start tag isn't found
			    
			    if (type != parser.START_TAG) {
			    	throw new XmlPullParserException("No start tag found");
			    }
			
			    // Verify that the tag passed in is the first tag in the XML
			    // document
			    
			    if (!parser.getName().equals(firstElementName)) {
			            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
			                    ", expected " + firstElementName);
			    }
		    }
			
			
			
			
			protected void onPostExecute(String result){
				
				titleTextView.setText(xmlPullParserArray[0][1]);
				averageRatingsTextView.setText("Average Rating: " + xmlPullParserArray[7][1]);
				totalRatingsTextView.setText("Total Ratings: " + xmlPullParserArray[8][1]);
				addressTextView.setText("Address: " + xmlPullParserArray[1][1]);
				cityTextView.setText("City: " + xmlPullParserArray[2][1]);
				stateTextView.setText("State: " + xmlPullParserArray[3][1]);
				phoneNumberTextView.setText("Phone Number: " + xmlPullParserArray[4][1]);
				
				lastReviewTextView.setText("Last Review: " + xmlPullParserArray[11][1]);
				
				
			}

		}

	}


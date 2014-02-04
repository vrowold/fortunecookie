package com.example.foodfinder;

import java.util.Arrays;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String FOOD = "com.foodfinder.food";
	public final static String CITY = "com.foodfinder.city";
	public final static String STATE = "com.foodfinder.state";
	
	private SharedPreferences foodEntered;
	
	private EditText foodEditText;
	private EditText cityEditText;
	private EditText stateEditText;
	
	Button enterButton;
	Button clearButton;
	
	private TableLayout tableScrollView;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		foodEntered = getSharedPreferences("foodList", MODE_PRIVATE);
		
		
        tableScrollView = (TableLayout) findViewById(R.id.tableScrollView);
		foodEditText = (EditText) findViewById(R.id.foodEditText);
		cityEditText = (EditText) findViewById(R.id.cityEditText);
		stateEditText = (EditText) findViewById(R.id.stateEditText);
		enterButton = (Button) findViewById(R.id.enterButton);
		clearButton = (Button) findViewById(R.id.clearButton);
		
		enterButton.setOnClickListener(enterButtonListener);
		clearButton.setOnClickListener(clearButtonListener);
		
		updateSavedFoodList(null);
	}
	
	private void updateSavedFoodList(String newFood){
		
		String[] food = foodEntered.getAll().keySet().toArray(new String[0]);
		
		Arrays.sort(food, String.CASE_INSENSITIVE_ORDER);
		
		if(newFood != null){
			
			insertFoodInScrollView(newFood, cityEditText.getText().toString(),stateEditText.getText().toString(), 
					Arrays.binarySearch(food, newFood));
			
		} else {
			
			for(int i = 0; i < food.length; ++i){
				
				insertFoodInScrollView(food[i],cityEditText.getText().toString(),stateEditText.toString(), 
						i);
				
			}
			
		}
		
	}

	private void saveFood(String newFood){
		
		String isTheFoodNew = foodEntered.getString(newFood, null);
		
		SharedPreferences.Editor preferencesEditor = foodEntered.edit();
		preferencesEditor.putString(newFood, newFood);
		preferencesEditor.apply();
		
		if(isTheFoodNew == null){
			updateSavedFoodList(newFood);
		}
		
	}

	private void insertFoodInScrollView(String food, String city, String state, int arrayIndex){
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View newFoodRow = inflater.inflate(R.layout.food_row, null);
		
		TextView newFoodTextView = (TextView) newFoodRow.findViewById(R.id.newFoodTextView);
		TextView newFoodCityTextView = (TextView) newFoodRow.findViewById(R.id.newFoodCityTextView);
		TextView newFoodStateTextView = (TextView) newFoodRow.findViewById(R.id.newFoodStateTextView);
		
		newFoodTextView.setText(food);
		newFoodCityTextView.setText(city);
		newFoodStateTextView.setText(state);
		
		Button restaurantButton = (Button) newFoodRow.findViewById(R.id.restaurantButton);
		restaurantButton.setOnClickListener(getRestaurantActivityListener);
		
		Button restaurantWebButton = (Button) newFoodRow.findViewById(R.id.restaurantWebButton);
		restaurantWebButton.setOnClickListener(getFoodFromWebsiteListener);
		
		tableScrollView.addView(newFoodRow, arrayIndex);
		
	}
	
	
	public OnClickListener enterButtonListener = new OnClickListener(){

		@Override
		public void onClick(View theView) {
			
			
			if(foodEditText.getText().length() > 0 && cityEditText.getText().length() > 0 && stateEditText.getText().length() > 0){
				
				
				saveFood(foodEditText.getText().toString());
				
				
				foodEditText.setText("");
				
				
				
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(foodEditText.getWindowToken(), 0);
			} else {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				
				builder.setTitle(R.string.invalid_input);
				
				builder.setPositiveButton(R.string.ok, null);
				
				builder.setMessage(R.string.missing_input);
				
				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();
				
			}
			
		}
		
	};
	
	private void deleteAllStocks(){
		
		tableScrollView.removeAllViews();
		
	}
	
	public OnClickListener clearButtonListener = new OnClickListener(){


		public void onClick(View v) {
			
			deleteAllStocks();
			
			SharedPreferences.Editor preferencesEditor = foodEntered.edit();
			
			preferencesEditor.clear();
			preferencesEditor.apply();
			
		}
		
	};
	
	public OnClickListener getFoodFromWebsiteListener = new OnClickListener(){

		public void onClick(View v) {
			
			
			TableRow tableRow = (TableRow) v.getParent();
            TextView newFoodTextView = (TextView) tableRow.findViewById(R.id.newFoodTextView);
            TextView newFoodCityTextView = (TextView) tableRow.findViewById(R.id.newFoodCityTextView);
            TextView newFoodStateTextView = (TextView) tableRow.findViewById(R.id.newFoodStateTextView);
            String newFood = newFoodTextView.getText().toString();
            String newFoodCity = newFoodCityTextView.getText().toString();
            String newFoodState = newFoodStateTextView.getText().toString();
            
            
            String foodURL = getString(R.string.yahoo_food_URL) + newFood+"+"+newFoodCity+"+"+newFoodState;
            
            if (foodURL.indexOf(" ") != -1) {
            	foodURL = foodURL.replaceAll(" ", "+");
            }
            
            Intent getFoodWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(foodURL));
            
            startActivity(getFoodWebPage);
			
		}
		
	};
	
	public OnClickListener getRestaurantActivityListener = new OnClickListener(){

		public void onClick(View v) {
			

			TableRow tableRow = (TableRow) v.getParent();
            
			TextView newFoodTextView = (TextView) tableRow.findViewById(R.id.newFoodTextView);
            String food = newFoodTextView.getText().toString();
            
            TextView newFoodCityTextView = (TextView) tableRow.findViewById(R.id.newFoodCityTextView);
            String city = newFoodCityTextView.getText().toString();
            
            TextView newFoodStateTextView = (TextView) tableRow.findViewById(R.id.newFoodStateTextView);
            String state = newFoodStateTextView.getText().toString();
            
            Intent intent = new Intent(MainActivity.this, RestaurantInfoActivity.class);
            
            intent.putExtra(FOOD, food);
            intent.putExtra(CITY, city);
            intent.putExtra(STATE, state);
            
            startActivity(intent);
			
		}
		
	};

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

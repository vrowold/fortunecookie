package com.example.foodfinder;

public class RestaurantInfo {

		
		private String title = "";
		private String averageRating = "";
		private String totalRatings = "";
		private String address = "";
		private String city = "";
		private String state = "";
		private String phoneNumber = "";
		private String websiteUrl = "";
		private String lastReview = "";

		public String getTitle() { return title; }
		public String getAverageRating() { return averageRating; }
		public String getTotalRatings() { return totalRatings; }
		public String getAddress() { return address; }
		public String getCity() { return city; }
		  
		public String getState() { return state; }
		public String getPhoneNumber() { return phoneNumber;}
		public String getWebsiteUrl() {return websiteUrl;}
		public String getLastReview() {return lastReview;}

		public RestaurantInfo(String title, String averageRating, String totalRatings, 
				String address, String city, String state,
				String phoneNumber, String websiteUrl, String lastReview) {
			this.title = title;
			this.averageRating = averageRating;
			this.totalRatings = totalRatings;
			this.address = address;
			this.city = city;
			this.state = state;
			this.phoneNumber = phoneNumber;
			this.websiteUrl = websiteUrl;
			this.lastReview = lastReview;
		}

	}

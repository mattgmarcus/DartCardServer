package edu.dartmouth.cs.dartcardserver;

import java.util.logging.Logger;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

// This class represents the components of an image as they're stored in the database:
// 		blobKey, latitude, longitude, and sector
public class ImageData {
	public String blobKey;
	public double latitude;
	public double longitude;
	public int sector;
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("blobKey", blobKey);
			obj.put("latitude", latitude);
			obj.put("longitude", longitude);
			obj.put("sector", sector);
		} 
		catch (JSONException e) {
			return null;
		}
		
		return obj;
	}

	public JSONObject fromJSONObject(JSONObject obj) {
		latitude = obj.optDouble("latitude");
		longitude = obj.optDouble("longitude");
		sector = obj.optInt("sector");
		
		return obj;
	}
	
	public String toString() {
		return "Blobkey is " + blobKey + ", latitude, longitude are " + String.valueOf(latitude) + 
				"," + String.valueOf(longitude) + " and sector is " + String.valueOf(sector);
	}

	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;		
	}

}

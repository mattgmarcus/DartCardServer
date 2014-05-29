package edu.dartmouth.cs.dartcardserver;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

// Class that handles conversions between ImageData object and Entity objects
public class ImageDataEntityConverter {
	public static Entity toEntity(ImageData entry, String kind) {
		Entity image = new Entity(kind);

		image.setProperty("blobKey", entry.blobKey);
		image.setProperty("latitude", entry.latitude);
		image.setProperty("longitude", entry.longitude);
		image.setProperty("sector", entry.sector);
				
		return image;
	}

	public static ImageData fromEntity(Entity entity) {
		ImageData image = new ImageData();
		
		image.blobKey = (String) entity.getProperty("blobKey");
		image.latitude = (double) entity.getProperty("latitude");
		image.longitude = (double) entity.getProperty("longitude");
		image.sector = (int)(long) entity.getProperty("sector");
		
		return image;
	}

}

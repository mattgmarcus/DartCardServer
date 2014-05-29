package edu.dartmouth.cs.dartcardserver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.api.datastore.Transaction;

// This is the Datastore which interacts with the Google Cloud Engine and Blobservice API.
// It also works when you run it locally
public class Datastore {
	private static int MULTICAST_SIZE = 1000;
	private static final String ENTITY_KIND_IMAGE_ENTRY = "ImageEntry";
	//Number of photos we want retrieved
	private static final int MAGIC_NUMBER = 27;
	private static final FetchOptions DEFAULT_FETCH_OPTIONS = FetchOptions.Builder
			.withPrefetchSize(MULTICAST_SIZE)
			.chunkSize(MULTICAST_SIZE);
	private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	// Function to save an image's blobkey and it's associated data to the datastore
	public static void saveData(String blobKey, String data) {
		JSONArray imageDataList = null;
		ImageData entry;
		
		try {
			imageDataList = new JSONArray(data);
		} catch (Exception e) {
			return;
		}
		
		Transaction txn = datastore.beginTransaction();
		
		try {
			entry = new ImageData();
			entry.fromJSONObject(imageDataList.getJSONObject(0));
			entry.setBlobKey(blobKey);

			datastore.put(ImageDataEntityConverter.toEntity(entry, ENTITY_KIND_IMAGE_ENTRY));
			txn.commit();
		} 
		catch (Exception ex) {	
			ex.printStackTrace();
		} 
		finally {
			if (txn.isActive()) 
				txn.rollback();
		}
	}
	
	// Function to query the database for all images and their information, given a sector
	// id to filter by. If there are less than the magic number (27) images in the sector
	// we will also get photos from adjacent sectors
	public static List<ImageData> getSectorImageEntries(int sector) {
		// Get images from current sector
		ArrayList<ImageData> result = new ArrayList<ImageData>();
		Filter exactSectorFilter = new FilterPredicate("sector", FilterOperator.EQUAL, sector);
		Query query = new Query(ENTITY_KIND_IMAGE_ENTRY).setFilter(exactSectorFilter);
		
		Iterable<Entity> entities = datastore.prepare(query).asIterable(
				DEFAULT_FETCH_OPTIONS);

		for (Entity entity : entities) {
			result.add(ImageDataEntityConverter.fromEntity(entity));
		}
		
		// If there aren't enough images, get from nearby sectors
		if (MAGIC_NUMBER > result.size()) {
			ArrayList<Integer> adjacentSectors = SectorHelper.getAdjacentSectors(sector);
			Filter adjacentSectorFilter = new FilterPredicate("sector", FilterOperator.IN, adjacentSectors);
			query = new Query(ENTITY_KIND_IMAGE_ENTRY).setFilter(adjacentSectorFilter);
			entities = datastore.prepare(query).asIterable(DEFAULT_FETCH_OPTIONS);

			for (Entity entity : entities) {
				result.add(ImageDataEntityConverter.fromEntity(entity));
			}
		}
		return result;
	}
}

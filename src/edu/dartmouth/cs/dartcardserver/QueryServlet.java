package edu.dartmouth.cs.dartcardserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.*;

import org.apache.geronimo.mail.util.Base64;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

// This is the servlet that handles requests for images. The basic idea here is to query by
// sector in the database. That logic is handled in the Datastore class. The images are 
// further compressed by converting their bitmaps to base64-encoded strings
// The images are then returned to the user
@SuppressWarnings("serial")
public class QueryServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	int sector = Integer.valueOf(req.getParameter("sector"));

    	ArrayList<ImageData> images = (ArrayList<ImageData>) Datastore.getSectorImageEntries(sector);

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");
		
		JSONArray values = new JSONArray();
        JSONObject json;

        for (ImageData image: images) {
        	json = new JSONObject();
            try {
            	byte[] base64Image = Base64.encode(blobstoreService.fetchData(new BlobKey(image.blobKey),
    					0L, BlobstoreService.MAX_BLOB_FETCH_SIZE-1L));
            	json.put("image", new String(base64Image));
    			json.put("latitude", image.latitude);
    			json.put("longitude", image.longitude);
    			json.put("sector", image.sector);
    			values.put(json);
    		} catch (JSONException e) {
    			// Pass, won't cause an issue if the json isn't added to what's sent to the phone
    		}
        }

        PrintWriter out = resp.getWriter();
        out.print(values.toString());
		out.flush();
		out.close();
	}

}

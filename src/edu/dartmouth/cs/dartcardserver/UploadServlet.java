package edu.dartmouth.cs.dartcardserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

// This servlet is used to upload the image at the url the device got from UploadRequestServlet.
// After uploading the image, it'll return the blobkey used to identify the image
@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        BlobKey blobKey = blobs.get("image").get(0);

        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");

        JSONObject json = new JSONObject();

        try {
			json.put("blobKey", blobKey.getKeyString());
		} catch (JSONException e) {
			// Pass. This will just cause the android to stop the upload process
		}

        PrintWriter out = res.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }
}


package edu.dartmouth.cs.dartcardserver;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

// This servlet handles the first request the backend gets, which is a request for a url to
// upload the image to. It uses the BlobstoreServe functions to get a url at /blob/upload/[blobkey]
@SuppressWarnings("serial")
public class UploadRequestServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//UploadOptions uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName("dartcard");
		//String blobUploadUrl = blobstoreService.createUploadUrl("/blob/upload", uploadOptions);
		String blobUploadUrl = blobstoreService.createUploadUrl("/blob/upload");

		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");

		PrintWriter out = resp.getWriter();
		out.print(blobUploadUrl);
		out.flush();
		out.close();
	}
}

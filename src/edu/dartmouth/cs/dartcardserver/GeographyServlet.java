package edu.dartmouth.cs.dartcardserver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// This servlet handles receiving images and saving them by their Geography, or by 
// a sector id that we calculate
@SuppressWarnings("serial")
public class GeographyServlet extends HttpServlet {
	//Get the data for an image and save it
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	String blobKey = req.getParameter("blobKey");
    	String data = req.getParameter("data");
    	
    	Datastore.saveData(blobKey, data);
    	resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/plain");
		resp.setContentLength(0);
    }
}

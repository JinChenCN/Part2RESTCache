package Cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.representation.AppendableRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class CachedFiles extends ServerResource{
	@Get("html")
    public Representation getResource() throws JSONException {
		AppendableRepresentation html = new AppendableRepresentation("<html xmlns=\"http://www.w3.org/1999/xhtml\">", MediaType.TEXT_HTML);
		try {
		    html.append("<body>");
		    html.append("Files cached on the Cache:<br>");
		    html.append("<ul>");
			ArrayList<File> files= Main.listOfCachedFiles;
			for(File file : files)
			{
				html.append("<li>"+file.getName()+"</li>");
			}			 
		    html.append("</ul>");
		    html.append("</body>");
		  }
		 catch (  IOException e) {
		    throw new ResourceException(e);
		  }
		  return html;
		
	}

}

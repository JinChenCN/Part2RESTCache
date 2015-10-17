package Cache;

import java.io.IOException;
import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.representation.AppendableRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class FileList extends ServerResource{
	@Get("html")
    public Representation getResource() throws JSONException, ResourceException, IOException {
		AppendableRepresentation html = new AppendableRepresentation("<html xmlns=\"http://www.w3.org/1999/xhtml\">", MediaType.TEXT_HTML);
		try {
		    html.append("<body>");
		    html.append("Files On the Server: <br>");
		    html.append("<ul>");
		    for(String name : Main.ServerList)
			{
		    	html.append("<li>"+name+"  <a href=\""+"http://localhost:" +Main.port + "/711P2/file?fileName=" + name+"\">Download</a></li><br>");
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

package Cache;

import java.io.File;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Log  extends ServerResource{
	@Get
    public Representation getResource() {
		Representation result = null;
		File log = Main.logName;
		if(log.length()==0)
		{
			result = new StringRepresentation("<h3>You haven't dowanloaded any file yet!</h3>", MediaType.TEXT_HTML);
		}
		else
		{
			result = new FileRepresentation(Main.logName, MediaType.TEXT_HTML);
		}		
		return result;
	}
}
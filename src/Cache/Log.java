package Cache;

import org.restlet.data.MediaType;
import org.restlet.representation.FileRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Log  extends ServerResource{
	@Get
    public FileRepresentation getResource() {
		FileRepresentation result = null;
		result = new FileRepresentation(Main.logName, MediaType.TEXT_HTML);
		return result;
	}
}
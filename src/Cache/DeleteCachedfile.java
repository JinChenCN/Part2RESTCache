package Cache;

import java.io.File;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class DeleteCachedfile extends ServerResource {
	String fileName = "";
	
	@Get
    public StringRepresentation clearResource() {
		StringRepresentation result = null;
		Request request = getRequest();
		Form form = request.getResourceRef().getQueryAsForm();
		if(form.getValues("fileName") != null)
		{
			fileName += form.getValues("fileName");
		}
		if(Delete(fileName))
		{
			result = new StringRepresentation("The file has been deleted successfully!");
		}else
		{
			result = new StringRepresentation("There was something wrong, please try again later!");
		}
			
		return result;	
	}
	
	private Boolean Delete(String fileName)
	{
		Boolean result = false;
		File folder = new File(Main.filePath);
		 for(File cachedFile : folder.listFiles()) {
			 if(fileName.equals(cachedFile.getName()))
			 {
				 cachedFile.delete();
				 Main.listOfCachedFiles.remove(cachedFile);
				 result = true; 
			 }
        }
		 return result;
	}
}

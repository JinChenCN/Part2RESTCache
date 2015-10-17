package Cache;

import java.io.File;
import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class CachedFiles extends ServerResource{
	@Get
    public StringRepresentation clearResource() throws IOException {
		StringRepresentation result = new StringRepresentation("There was something wrong, please try again later!");
		if(Delete(Main.filePath) && Delete(Main.segmentPath))
		{
			 Main.listOfCachedFiles.clear();
			 Main.listOfCachedSegments.clear();
			 ClientResource file = new ClientResource("http://localhost:" + Main.serverPort + "/711P2/clearCache");
			 Representation rep = file.get();
			 if(rep.getText().equals("OK"))
			 {
				 result = new StringRepresentation("All cached files have been cleared!");
			 }			 
		}			
		return result;	

	}
		
	private Boolean Delete(String filePath)
	{
		Boolean result = false;
		File folder = new File(filePath);
		 for(File cachedFile : folder.listFiles()) {
			 cachedFile.delete();               
	        }
		 if(folder.list().length == 0)
		 {
			 result = true;
		 }
		 return result;
	}
}

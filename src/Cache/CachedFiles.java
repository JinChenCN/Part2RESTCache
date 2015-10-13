package Cache;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class CachedFiles extends ServerResource{
	@Post
    public StringRepresentation clearResource() {
		StringRepresentation result = null;
		if(Delete(Main.filePath))
		{
			result = new StringRepresentation("All cached files have been cleared!");
		}else
		{
			result = new StringRepresentation("There was something wrong, please try again later!");
		}
			
		return result;	
	}
	
	@Get
    public JsonRepresentation getResource() throws JSONException {
		JSONArray list =  new JSONArray();
		for(File singlefile : Main.listOfCachedFiles)
		{
			JSONObject file = new JSONObject() ;  
			file.put("name", singlefile.getName());
			file.put("deleteUrl", "http://localhost:" +Main.port + "/api/DeleteCachedfile?fileName=" + singlefile.getName());
			list.put(file);
		}	
		return new JsonRepresentation(list);
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
			 Main.listOfCachedFiles.clear();
			 result = true;
		 }
		 return result;
	}
}

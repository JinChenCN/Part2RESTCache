package Cache;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class FileList extends ServerResource{
	@Get
    public JsonRepresentation getResource() throws JSONException, ResourceException, IOException {
		JSONArray list =  new JSONArray();
		for(String name : Main.ServerList)
		{
			JSONObject file = new JSONObject() ;  
			file.put("name", name);
			file.put("url", "http://localhost:" +Main.port + "/711P2/file?fileName=" + name);
			list.put(file);
		}	
		return new JsonRepresentation(list);
    }
}

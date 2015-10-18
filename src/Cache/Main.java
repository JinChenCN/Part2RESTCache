package Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Main {
	static Integer port = 8086;
	static Integer serverPort = 8085;
	static String filePath = "";
	static String segmentPath = "";
	static ArrayList<String> ServerList = new ArrayList<String>();
	static ArrayList<File> listOfCachedFiles = new ArrayList<File>();
	static ArrayList<File> listOfCachedSegments = new ArrayList<File>();
    static File logName = new File("log.log");
	
	public static void main(String[] args) throws Exception {  		
		getProperties(args[0]);
		
		// Initiate cached Files
		File folder = new File(filePath);
		for(File cachedFile : folder.listFiles()) {
			 cachedFile.delete();               
	        }
		
		// Initiate cached segments
		File segfolder = new File(segmentPath);
		for(File cachedSeg : segfolder.listFiles()) {
			cachedSeg.delete();               
	        }	
			
		// Init server
		clearCache();
    	getFileList();
    	
    	// Initiate log
    	FileWriter writer = new FileWriter(logName, false);
    	PrintWriter printWriter = new PrintWriter(writer, false);
    	printWriter.flush();
    	printWriter.close();
    	writer.close();
    	
	    // Create a new Component.  
	    Component component = new Component(); 

	    // Add a new HTTP server listening on port configured, the default port is 8184.  
	    component.getServers().add(Protocol.HTTP, port); 
	    
	    component.getClients().add(Protocol.HTTP);

	    // Attach the application.  
	    component.getDefaultHost().attach("/711P2",  
	            new APISever());  

	    // Start the component.  
	    component.start();	
	   
	} 
	
	private static void getProperties(String configFilePath){
		Properties configFile = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream(configFilePath);
			configFile.load(file);
			file.close();
			port = Integer.parseInt(configFile.getProperty("CachePort"));
			serverPort = Integer.parseInt(configFile.getProperty("ServerPort"));
			filePath = configFile.getProperty("CacheFilePath");
			segmentPath = configFile.getProperty("CachedSegmentFilePath");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void clearCache()
	{
		ClientResource list = new ClientResource("http://localhost:" +serverPort + "/711P2/clearCache");
		list.get();
	}
	
	private static void getFileList() throws JSONException
	{
		try {
			ClientResource list = new ClientResource("http://localhost:" +serverPort + "/711P2/files");
			Representation result = list.get();
			JsonRepresentation jsonRepresentation = new JsonRepresentation(result);			
			JSONArray array = jsonRepresentation.getJsonArray();
			for(int i =0; i<array.length(); i++)
			{
				ServerList.add(array.get(i).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}

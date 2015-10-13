package Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

public class Main {
	static Integer port = 8186;
	static Integer serverPort = 8185;
	static String filePath = "";
	static String segmentPath = "";
	static ArrayList<File> listOfCachedFiles = new ArrayList<File>();
	static ArrayList<File> listOfCachedSegments = new ArrayList<File>();
	static Map<String, List<String>> ServerList = null;
    static File logName = new File("log.log");
	
	public static void main(String[] args) throws Exception {  		
		getProperties(args[0]);
		
		// Initiate cached Files
		File folder = new File(filePath);
		for(File cachedFile : folder.listFiles()) {
			 cachedFile.delete();               
	        }
		
		// Initiate cached segments
		File segfolder = new File(filePath);
		for(File cachedFile : segfolder.listFiles()) {
			 cachedFile.delete();               
	        }	
			
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

	private static void getFileList() throws JSONException
	{
		try {
			ClientResource list = new ClientResource("http://localhost:" +serverPort + "/api/fileSegMap");
			Representation result = list.get();
			JsonRepresentation jsonRepresentation = new JsonRepresentation(result);			
			JSONArray array = jsonRepresentation.getJsonArray();
			for(int i =0; i<array.length(); i++)
			{
				JSONObject ob = new JSONObject(array.get(i));
				String fileContent = ob.get("content").toString();
				List<String> segementList=new ArrayList<String>();
				segementList = getSegementList(fileContent);
				ServerList.put(ob.get("name").toString(), segementList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private static List<String> getSegementList(String fileContent){
        List<String> ls=new ArrayList<String>();
        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher matcher = pattern.matcher(fileContent);
        while(matcher.find())
            ls.add(matcher.group());
        return ls;
    }
	
	public static String getDate()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss yyyy-MM-dd");     
		return sDateFormat.format(new java.util.Date()); 
	}
}

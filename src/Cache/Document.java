package Cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Document extends ServerResource {
	String fileName = "";
	String prefix= "";
	
	@Get
    public FileRepresentation getResource() throws IOException, JSONException {
		FileRepresentation result = null;
		Request request = getRequest();
		Form form = request.getResourceRef().getQueryAsForm();		
		
		if(form.getValues("fileName") != null)
		{
			fileName += form.getValues("fileName");
		}
		
		for (int i = 0; i<Main.listOfCachedFiles.size(); i++)
    	{
    		if(fileName.equals(Main.listOfCachedFiles.get(i).getName()))
    		{
    			writeLog(fileName, "100% of file " + fileName + " was constructed with the cached data");   					
    			return new FileRepresentation(new File(Main.filePath+"//"+fileName), MediaType.TEXT_HTML);
    		} 
    	}		

		int cached = 0, downloaded = 0;
		FileOutputStream out = new FileOutputStream(Main.filePath+"//"+fileName);

		ClientResource file = new ClientResource("http://localhost:" + Main.serverPort + "/711P2/file?filename="+fileName);
		Representation rep = file.get();
		JsonRepresentation jsonRepresentation = new JsonRepresentation(rep);			
		 try {
			JSONArray array = jsonRepresentation.getJsonArray();
			 for (int i = 0; i < array.length(); i++)
			 {
				 JSONObject object = new JSONObject();
				 object = (JSONObject) array.get(i);
				 String segName = object.getString("name");
				 ArrayList<String> segNames = new ArrayList<String>();
				 for(int k = 0; k < Main.listOfCachedSegments.size(); k++)
				 {
					 segNames.add(Main.listOfCachedSegments.get(k).getName());
				 }
				 if (segNames.contains(segName))
				 {
					 
					 for (int j = 0; j<Main.listOfCachedSegments.size(); j++)
				    	{
				    		if(segName.equals(Main.listOfCachedSegments.get(j).getName()))
				    		{
				    			cached ++;
				    			out.write(Files.readAllBytes(Main.listOfCachedSegments.get(j).toPath()));				    			
				    		} 
				    	}							 					
				 }
				 else
				 {
					 FileOutputStream newSeg = new FileOutputStream(Main.segmentPath + "//" + segName);
					JSONArray entries = new JSONArray(object.get("content").toString());
					 byte[] content = new byte[entries.length()];
					 for(int h = 0; h < entries.length(); h++)
					 {
						 content[h] = (byte) entries.getInt(h);
					 }
					 newSeg.write(content);
					 newSeg.close();
					 out.write(content);
					 Main.listOfCachedSegments.add(new File(Main.segmentPath + "//" + segName));
					 downloaded ++;
				 }
				 
			 }
			 out.close();
			 NumberFormat nf = NumberFormat.getPercentInstance(); 
			 nf.setMinimumFractionDigits(2);
			 if(cached == 0)
			 {
				 writeLog(fileName, "0% of file " + fileName + " was constructed with the cached data"); 
			 }
			 else
			 {
				 String percent = nf.format((double)cached/(cached + downloaded));
				 writeLog(fileName, percent + " of file " + fileName + " was constructed with the cached data"); 				 
			 }							
		       
	    }catch (IOException e)
		 {
	    	e.printStackTrace();
		 }
		 out.close();
		 Main.listOfCachedFiles.add(new File(Main.filePath + "//" + fileName));
		 String fileType = Files.probeContentType(new File(Main.filePath + "//" + fileName).toPath());
			if(fileType==null)
			{
				result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.TEXT_HTML);
			}
			else
			{
				switch (fileType) {
		         case "text/plain":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.TEXT_HTML);
		             break;
		         case "image/jpeg":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.IMAGE_JPEG);
		             break;
		         case "image/png":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.IMAGE_PNG);
		             break;
		         case "application/pdf":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.APPLICATION_PDF);
		        	 break;
		         case "application/msword":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.APPLICATION_WORD);
		             break;
		         case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.APPLICATION_MSOFFICE_DOCX);
		        	 break;
		         case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.APPLICATION_MSOFFICE_XLSX);
		        	 break;
		         default:
		        	 result = new FileRepresentation(Main.filePath + "//" + fileName, MediaType.TEXT_HTML);
				}			
			}		
		 return result;
    }
	
	private void writeLog(String fileName, String info){
        String content=info(fileName,info);
        RandomAccessFile mm = null;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(Main.logName,true);
            o.write(content.getBytes("utf-8"));
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mm != null) {
                try {
                    mm.close(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 
            }
        }
    }  
 
    private String info(String fileName, String info){
    	  StringBuffer buffer=new StringBuffer();
          Date date = new Date();   
          SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");   
          String d = sd.format(date)+System.getProperty("line.separator");  
          buffer.append("<br>user request: file "+ fileName +" at "+ d+"<br>");
          buffer.append("response: ").append(info).append(System.getProperty("line.separator"));
          buffer.append("<br>");
          return buffer.toString();
    }
    
}

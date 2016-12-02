package bulat.diet.helper_sport.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.api.client.util.IOUtils;

public class InputStreamToFileApp {
    public static File convert(InputStream inputStream) {
    	
    	File tempFile = null;
				try {
					tempFile = File.createTempFile("p12", "p12");
	    			
				    tempFile.deleteOnExit();
				    FileOutputStream out = new FileOutputStream(tempFile);
				    IOUtils.copy(inputStream, out);
				    out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
		    return tempFile;
    }
}

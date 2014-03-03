package s3_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class Upload {

	public static void main(String[] args) throws IOException, ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
		SimpleDateFormat startFormat = new SimpleDateFormat("yyyy/MM/dd/HH/yyyyMMddHHmmss");
		startFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat endFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		endFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usEast = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usEast);
		String bucketName = "bah-ds";
		
		@SuppressWarnings("unchecked")
		Collection<File> listOfFiles = FileUtils.listFiles(new File("C:/Users/572284/Desktop/tweets_raw/"),
		        TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		int indx = 0;
		String created = new String("created_at");
		String firstLine = new String();
		String lastLine = new String();
		String key = new String();

		for (File file : listOfFiles) {
		    if (file.isFile())// && file.getName().contains("1367523670393")) 
		    {
		        InputStream fileStream = new FileInputStream(file);
				InputStream gzipStream = new GZIPInputStream(fileStream);
				Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
				BufferedReader reader = new BufferedReader(decoder);
				
				String next, line = reader.readLine();
	            for (boolean first = true, last = (line == null); !last; first = false, line = next) 
	            {
	                last = ((next = reader.readLine()) == null);

	                if (first) {
	                	indx = line.indexOf(created);
	                	firstLine = line.substring(indx+13, indx+43);
	                } 
	                else if (last) {
	                	indx = line.indexOf(created);
	                	lastLine = line.substring(indx+13, indx+43);
	                }
	            }
	            reader.close();
	            decoder.close();
	            gzipStream.close();
	            fileStream.close();

	            key = new String(startFormat.format(sdf.parse(firstLine).getTime()) 
	            		+ "-" + endFormat.format(sdf.parse(lastLine).getTime()));
	            
	            System.out.println("Uploading: " + file.getName() + " as -> " + key);
	            s3.putObject(new PutObjectRequest(bucketName, key, file));
		    }
		}	
	}
}
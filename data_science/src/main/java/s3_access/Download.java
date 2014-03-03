package s3_access;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Download {

	public static void main(String[] args) throws IOException, ParseException {

		// args
		String start = 	args[0];
		String end = 	args[1];	
		int maxFiles = Integer.parseInt(args[2]);


		// ************* //
		long range = Long.parseLong(end) - Long.parseLong(start);
		int places = (int) Math.log10(range);

		String dir = null;
		if(places == 9 || places == 8) {
			dir = start.substring(0, 4) + "/";
		}
		else if(places == 7 || places == 6) {
			dir = start.substring(0, 4) + "/" + start.substring(4, 6) + "/";
		}
		else if(places == 5 || places == 4) {
			dir = start.substring(0, 4) + "/" + (start).substring(4, 6) + "/" + start.substring(6, 8) + "/";
		}
		else if(places <= 3) {
			dir = start.substring(0, 4) + "/" + (start).substring(4, 6) + "/" + start.substring(6, 8) + "/" + start.substring(8, 10) + "/";
		}
		else {
			dir = "";
		}
		//System.out.println(dir);

		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usEast = Region.getRegion(Regions.US_EAST_1);
		s3.setRegion(usEast);
		String bucketName = "bah-ds";

		System.out.println("Listing objects");
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
			.withBucketName(bucketName)
			.withMaxKeys(maxFiles)
			.withPrefix(dir));

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:/Users/572284/Desktop/ouput.txt")));
		
		
		int count = 0;
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {

			String dates = objectSummary.getKey().split("/")[4];
			String startFile = dates.split("-")[0];
			String endFile = dates.split("-")[1];

			if((Long.parseLong(start) <= Long.parseLong(startFile) && Long.parseLong(end) >= Long.parseLong(startFile))
					|| (Long.parseLong(start) <= Long.parseLong(endFile) && Long.parseLong(end) >= Long.parseLong(endFile))
					|| (Long.parseLong(start) >= Long.parseLong(startFile) && Long.parseLong(end) <= Long.parseLong(endFile))) 
			{
				System.out.println(startFile + "  " + endFile);
				S3Object object = s3.getObject(new GetObjectRequest(bucketName, objectSummary.getKey()));
				
				GZIPInputStream stream = new GZIPInputStream(object.getObjectContent());
				IOUtils.copy(stream, writer);
				object.close();
				stream.close();
				count ++;
			}
		}
		System.out.println("File Count: " + count);
	}
}
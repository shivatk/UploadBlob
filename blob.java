// Created by ShivaTk //
// Can be used to upload apps to the AirWatch database //
// Requires access to AirWatch API //


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.nio.file.Path;

public class blob {

	static URL url;
	static HttpURLConnection urlconnect = null;
	static String encodecall,encodedcreds,auth;
	static byte[] app_stream;
	static Path path;
			
	public static void uploadBlob() {
		
		new Thread (new Runnable () {	
			
			public void run() {
				
			/* Reading the application file into a byte stream*/
				
			Path path = Paths.get("/Users/ShivaTk/Documents/Blob/guidebook.ipa");
			try {
				app_stream = Files.readAllBytes(path);
			} catch (IOException e) {
				System.out.println("Unable to read file");
				e.printStackTrace();
			}
						
			String userCredentials = "username:password"; //Insert username and password
			
	        try {
	        	encodedcreds = Base64.getEncoder().encodeToString(userCredentials.getBytes());
				auth = "BASIC " + encodedcreds;
				
				/* Initializing the HTTP Connection*/
				
	        	url = new URL("https://host/API/v1/mam/blobs/uploadblob?filename={filename}&organizationgroupid={id}"); // Insert filename and LGID
				urlconnect = (HttpURLConnection)url.openConnection();
				urlconnect.setRequestMethod("POST");
				urlconnect.setRequestProperty ("Authorization",auth);
				urlconnect.setRequestProperty ("aw-tenant-code", "API Key"); //Insert API key
				urlconnect.setRequestProperty("Content-Type", "application/json");
				urlconnect.setDoOutput(true);
				urlconnect.setDoInput(true);
				urlconnect.setConnectTimeout(0);
				urlconnect.setUseCaches(false);
				
				/* Writing byte stream to the POST body */
				
				DataOutputStream wr = new DataOutputStream(urlconnect.getOutputStream());
				wr.write(app_stream);
				wr.flush();
				wr.close();
				
				/* Obtaining response from the server */
				
				String server_response = null;
			    StringBuffer sb = new StringBuffer();
			    InputStream is = new BufferedInputStream(urlconnect.getInputStream());
		        BufferedReader br = new BufferedReader(new InputStreamReader(is));
		        String inputLine = "";
		        while ((inputLine = br.readLine()) != null) {
		            sb.append(inputLine);
		        }
		        server_response = sb.toString();				
		        System.out.println(server_response); //Printing response
		        
		        /* Response will contain the BlobID. Use this to call the BeginInstall API*/
		        
			} catch (Exception e) {
				e.printStackTrace();
				}	
			}
		}).start();		
	}

	public static void main(String[] args) {		
		uploadBlob();		
	}
}

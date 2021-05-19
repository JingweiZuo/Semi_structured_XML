package service;

import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class GetData {
	  OkHttpClient client = new OkHttpClient();
	  
	  String run(String url) throws IOException {
	    Request request = new Request.Builder()
	        .url(url)
	        .build();
	    try (Response response = client.newCall(request).execute()) {
	      return response.body().string();
	    }
	  }

	  public static void main(String[] args) throws IOException, URISyntaxException, TransformerException {
	    GetData data = new GetData();
	    DB_test db = new DB_test();
	    String artistName = "Luca Barbarossa";
	    String Url = "http://musicbrainz.org/ws/2/recording/?query=artistname:%22" + artistName + "%22";
	    System.out.println(Url);
	    String response = data.run(Url);
	    System.out.println(response);
	    try {
		    PrintWriter writer = new PrintWriter("songs.xml", "UTF-8");
		    writer.println(response);
		    writer.close();
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Source xslt = new StreamSource(new File("BrainZ_getsong.xsl"));
	        Transformer transformer = factory.newTransformer(xslt);
	        Source text = new StreamSource(new File("songs.xml"));
	        transformer.transform(text, new StreamResult(new File("songs_output.xml")));
	    } catch(IOException e){
	    	e.printStackTrace();
	    }
	  }
}

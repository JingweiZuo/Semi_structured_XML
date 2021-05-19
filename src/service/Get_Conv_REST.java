package service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.omg.CORBA.portable.InputStream;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


public class Get_Conv_REST {
   OkHttpClient client = new OkHttpClient();

   String run(String url) throws IOException {
     Request request = new Request.Builder()
         .url(url)
         .build();
     try (Response response = client.newCall(request).execute()) {
       return response.body().string();
     }
   }

   public static void get_XML(String query_url, String output_path) throws IOException{
     Get_Conv_REST data = new Get_Conv_REST();
     System.out.println(query_url);
     String response = data.run(query_url);
     System.out.println(response);
     try {
       PrintWriter writer = new PrintWriter(output_path, "UTF-8");
       writer.println(response);
       writer.close();
      } catch(IOException e){
       e.printStackTrace();
      }
   }
   
   public static void conv(String input_xml_path, String input_xsl_path, String output_path) throws IOException, URISyntaxException, TransformerException {
     TransformerFactory factory = TransformerFactory.newInstance();
     Source xslt = new StreamSource(new StringReader(input_xsl_path));
     Transformer transformer = factory.newTransformer(xslt);
     Source text = new StreamSource(new File(input_xml_path));
     transformer.transform(text, new StreamResult(new File(output_path)));
   }
   
   public static void get_XML_rm_OpenSearch(String query_url, String output_path) throws IOException{
     Get_Conv_REST data = new Get_Conv_REST();
     System.out.println(query_url);
     String response = data.run(query_url);
     int start_index = response.indexOf("<trackmatches>");
     int last_index = response.indexOf("</trackmatches>") + "</trackmatches>".length();
     response = response.substring(start_index, last_index);
     System.out.println(response);
     try {
       PrintWriter writer = new PrintWriter(output_path, "UTF-8");
       writer.println(response);
       writer.close();
      } catch(IOException e){
       e.printStackTrace();
      }
    }
  
   public static String convertXMLFileToString(String fileName) 
   { 
     try{ 
       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
       FileInputStream inputStream = new FileInputStream(new File(fileName)); 
       org.w3c.dom.Document doc = documentBuilderFactory.newDocumentBuilder().parse(inputStream); 
       StringWriter stw = new StringWriter(); 
       Transformer serializer = TransformerFactory.newInstance().newTransformer(); 
       serializer.transform(new DOMSource(doc), new StreamResult(stw)); 
       return stw.toString(); 
     } 
     catch (Exception e) { 
       e.printStackTrace(); 
     } 
       return null; 
   }
    
   public static void main(String[] args) throws Exception { 
	   String line;
	   line = convertXMLFileToString("Lastfm_getsong.xsl");
	   line = line.replace("\n","");
	   line = line.replace("\"", "\\\"");
	   System.out.println(line);
   }
   

}

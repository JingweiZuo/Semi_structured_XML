package service;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.IOException;  
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;  
import org.xml.sax.SAXException; 
public class DB_test {

  public DB_test() {
    // TODO Auto-generated constructor stub
  }
  private String url;
  /*
    //jackson for test
    public void set_url(String artist_name){
      // TODO Auto-generated constructor stub
      this.url = "http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist="+ artist_name+"&api_key=fdc873135eefe53670783bd15d92eed5";
    }
    */
    public static final String DB_CONNECTION = "dbconnection";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/XML_projet";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "6233";
    static Connection con;
    
    public ResultSet search_DB(String sql) {
      Statement stmt = null;
      ResultSet result = null;
      try{   
        //Load JDBC Driver  
        Class.forName("com.mysql.jdbc.Driver") ;   
        }catch(ClassNotFoundException e){   
        System.out.println("Error: can't find the JDBC driver");   
        e.printStackTrace() ;   
        } 
      try{   
        con = DriverManager.getConnection(DB_URL , USER , PASS ) ;   
        stmt = con.createStatement();
        result = stmt.executeQuery(sql);
        System.out.println("Created table in given database...");
         }catch(SQLException se){   
        System.out.println("Connection with DB failed");   
        se.printStackTrace() ;   
         }   
        System.out.println("Connection with DB succeed!");
        return result;
    }//end con_DB()
    
    static private String getTextContent(Node parentNode,String childName) {
        NodeList nlist = parentNode.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
        Node n = nlist.item(i);
        String name = n.getNodeName();
        if ( name != null && name.equals(childName) )
            return n.getTextContent();
        }
        return "";
    }
    
    private static Document getDocument(String doc_path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        //get Document Builder
        DocumentBuilder builder = factory.newDocumentBuilder();  
        InputStream in = DB_test.class.getClassLoader().getResourceAsStream(doc_path);  
        //get Document  
        Document doc = builder.parse(in);  
        return doc;  
    }
    
    public void insertSongsIntoDB(String input_xml, String source) throws SQLException, ParserConfigurationException, SAXException, IOException {
    	con = DriverManager.getConnection(DB_URL , USER , PASS ) ;   
//        Statement stmt = null;
//        stmt = con.createStatement();
        //parse XML
        File file = new File(input_xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.parse(file);
        //insert into mySQL 
        PreparedStatement stmt;
        if(source == "BrainZ"){
        	stmt = con
            	    .prepareStatement("INSERT INTO songs\n" +
            	              " (title, author, album, score_brainz, listeners_lastfm)\n" +
            	                  " VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
            	                  + "album= VALUES(album)," 
            	                  + "score_brainz=VALUES(score_brainz)");
        }
        else{
        	stmt = con
            	    .prepareStatement("INSERT INTO songs\n" +
            	              " (title, author, album, score_brainz, listeners_lastfm)\n" +
            	                  "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE"
            	                  + " listeners_lastfm= VALUES(listeners_lastfm)");
        }
        
        Element  root=xmlDoc.getDocumentElement();//获取根节点  
        System.out.println(root.getNodeName());//打印根节点root 
        NodeList nlist = root.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
            Node node = nlist.item(i);
            List<String> columns = Arrays
            .asList(
                getTextContent(node, "song_title"),
                getTextContent(node, "Artist"),
                getTextContent(node, "Album"),
                getTextContent(node, "Score"),
                getTextContent(node, "listeners"));
            for (int n = 0 ; n < columns.size() ; n++) {
            stmt.setString(n+1, columns.get(n));
            }
            stmt.execute();
        }
    }
    
    public void insertAlbumsIntoDB(String input_xml, String source) throws SQLException, ParserConfigurationException, SAXException, IOException {
    	con = DriverManager.getConnection(DB_URL , USER , PASS ) ;   
//        Statement stmt = null;
//        stmt = con.createStatement();
        //parse XML
        File file = new File(input_xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.parse(file);
        //insert into mySQL 
        PreparedStatement stmt;
        if(source == "BrainZ"){
        	stmt = con
            	    .prepareStatement("INSERT IGNORE INTO Albums\n" +
            	              " (title, author)\n" +
            	                  " VALUES(?, ?)");
        }
        else{
        	stmt = con
            	    .prepareStatement("INSERT IGNORE INTO Albums\n" +
            	              " (title, author)\n" +
            	                  "VALUES(?, ?) ");
        }
        
        Element  root=xmlDoc.getDocumentElement();//获取根节点  
        System.out.println(root.getNodeName());//打印根节点root 
        NodeList nlist = root.getChildNodes();
        for (int i = 0 ; i < nlist.getLength() ; i++) {
            Node node = nlist.item(i);
            List<String> columns = Arrays
            .asList(
                getTextContent(node, "Album_title"),
                getTextContent(node, "Artist"));
            for (int n = 0 ; n < columns.size() ; n++) {
            stmt.setString(n+1, columns.get(n));
            }
            stmt.execute();
        }
    }
    
    public static void main(String[] args) throws Exception {  
//      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
//      //Get the builder
//      DocumentBuilder builder = factory.newDocumentBuilder();
//      //Get the old document
//      Document doc = getDocument("BrainZ_getsong.out.xml");
//      Element  root=doc.getDocumentElement();//获取根节点  
//      System.out.println(root.getNodeName());//打印根节点root  
//    
//      NodeList song_list = root.getElementsByTagName("song");
//      System.out.println(song_list.getLength());
//      //con_DB();
//      con = DriverManager.getConnection(DB_URL , USER , PASS ) ;   
//      Statement stmt = null;
//      stmt = con.createStatement();
//     /* String sql = "CREATE TABLE TABLE1(song VARCHAR(100), artist VARCHAR(100))"; 
//      stmt.executeUpdate(sql); */
//      for(int i = 0; i < song_list.getLength(); i++) {
//        
//        Element song = (Element)song_list.item(i);
//        /*
//        //Méthode 1
//        System.out.println(song.getElementsByTagName("Artist").item(0).getTextContent());
//        System.out.println(song.getElementsByTagName("song_title").item(0).getTextContent());
//        //Méthode 2
//        System.out.println(song.getChildNodes().item(1).getTextContent());
//        System.out.println(song.getChildNodes().item(3).getTextContent());
//        */
//        
//        String SQL = "INSERT INTO TABLE1(song, artist) VALUES (?,?)";
//        try {
//            PreparedStatement statement = con.prepareStatement(SQL);
//            statement.setString(1,song.getElementsByTagName("Artist").item(0).getTextContent());
//            statement.setString(2,song.getElementsByTagName("song_title").item(0).getTextContent());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//      }//end for
//    }//end main
    
//    public static  void main(String args[]) {
//    	DB_test db = new DB_test();
//    	String sql = "";
//    	if(db.search_DB(sql) == null) 
//    		System.out.println("Still return");
//    	else {
//    		System.out.println("Catch stop the function");
//    	}
//    }
//    	insertSongsIntoDB("BrainZ_getsong.out.xml", "BrainZ");
//    	insertSongsIntoDB("Lastfm_getsong.out.xml", "lastfm");
    	}
}
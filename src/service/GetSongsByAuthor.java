package service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class GetSongsByAuthor {
	 static String xsl_brainz="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:ext=\"http://musicbrainz.org/ns/ext#-2.0\" xmlns:n=\"http://musicbrainz.org/ns/mmd-2.0#\" version=\"2.0\">  <xsl:output encoding=\"utf-8\" indent=\"yes\" method=\"xml\"/><!--XSLT conversion pour MusicBrainz-->    <xsl:template match=\"/n:metadata\">      <Brainz_1>        <xsl:apply-templates/>      </Brainz_1>     </xsl:template>        <xsl:template match=\"n:recording-list\">           <xsl:for-each select=\"n:recording\">        <song>             <song_title>              <xsl:value-of select=\"n:title\"/>             </song_title>            <Artist>              <xsl:value-of select=\"n:artist-credit/n:name-credit/n:artist/n:name\"/>            </Artist>             <Album>              <xsl:value-of select=\"n:release-list/n:release/n:title\"/>             </Album>             <Score>              <xsl:value-of select=\"@ext:score\"/>             </Score>             <listeners/>        </song>      </xsl:for-each>      </xsl:template></xsl:stylesheet>";
	 static String xsl_lastfm="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\"><xsl:output encoding=\"utf-8\" indent=\"yes\" method=\"xml\"/>	<!-- Get songs by author -->	<xsl:template match=\"/lfm\">      <lastfm_1>        <xsl:apply-templates/>        </lastfm_1>	</xsl:template>		 <xsl:template match=\"toptracks\">        <xsl:for-each select=\"track\">          <song>             <song_title>              <xsl:value-of select=\"name\"/>             </song_title>             <Artist>                <xsl:value-of select=\"artist/name\"/>             </Artist>             <Album/>             <Score/>             <listeners>              <xsl:value-of select=\"listeners\"/>             </listeners>          </song>      </xsl:for-each>                 </xsl:template>  </xsl:stylesheet>";		
	public static ArrayList<String> getSongsByAuthor(String artist) throws IOException, URISyntaxException, TransformerException, SQLException, ParserConfigurationException, SAXException  {
		ResultSet result = null;
		ResultSet result_after_insert = null;
		//Return definition 
		ArrayList<String> result_list = new ArrayList<String>();
		int i=0;
		DB_test db = new DB_test();
		// First search in the DB
		String sql= "SELECT * from Songs WHERE author = '" + artist+"'";
		System.out.println("printsql"+sql);
		result = db.search_DB(sql);
		//System.out.println(result.next());
		try {
			if(result ==null || !result.next()) {
				//System.out.println("test");
					// If cannot find result in DB, search externally
					//Query from brainz
					String url_brainz = "http://musicbrainz.org/ws/2/recording/?query=artistname:%22" + artist + "%22";
					Get_Conv_REST.get_XML(url_brainz, "songs.xml");
					Get_Conv_REST.conv("songs.xml", xsl_brainz,"BrainZ_getsong.out.xml");
					// todo: Dom get xml and insert into DB
					db.insertSongsIntoDB("BrainZ_getsong.out.xml", "BrainZ");
					//Query from lastfm
					String Url = "http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=" +artist+"&api_key=fdc873135eefe53670783bd15d92eed5";
					Get_Conv_REST.get_XML(Url, "songs.xml");
					Get_Conv_REST.conv("songs.xml", xsl_lastfm,"Lastfm_getsong.out.xml");
					// todo: Dom get xml and insert into DB
					db.insertSongsIntoDB("Lastfm_getsong.out.xml", "lastfm");
					result_after_insert = db.search_DB(sql);
					while(result_after_insert.next()) {
						if(result_after_insert.getObject(1) == null)
							break;
						//System.out.println(result_after_insert.getObject(1));
						result_list.add(i++,(String) result_after_insert.getObject(1));
					}
				}
				else {
				  if(result!=null && result.next()){
  					while(result.next()){
  						if(result.getObject(1) == null)
  							break;
  						System.out.println(result.getObject(1));
              result_list.add(i++,(String) result.getObject(1));
  					}
				  }
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result_list;
	}
	
  public static  void main(String args[]) throws IOException, URISyntaxException, TransformerException, SQLException, ParserConfigurationException, SAXException {
	  for(int i=0; i<getSongsByAuthor("Wang").size(); i++)
	    System.out.println(getSongsByAuthor("Wang").get(i));
    }
}

package service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class GetAlbumByAuthor {
	static String xsl_brainz = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:ext=\"http://musicbrainz.org/ns/ext#-2.0\" xmlns:n=\"http://musicbrainz.org/ns/mmd-2.0#\" version=\"2.0\">  <xsl:output encoding=\"utf-8\" indent=\"yes\" method=\"xml\"/><!--XSLT conversion pour MusicBrainz_Album-->    <xsl:template match=\"/n:metadata\">      <Brainz_2>        <xsl:apply-templates/>      </Brainz_2>    </xsl:template>        <xsl:template match=\"n:release-list\">	      <xsl:for-each select=\"n:release\">	         <Album>            <Album_title>	             <xsl:value-of select=\"n:title\"/>             </Album_title>	           <Artist>               <xsl:value-of select=\"n:artist-credit/n:name-credit/n:artist/n:name\"/>             </Artist>	         </Album>	      </xsl:for-each>                   </xsl:template></xsl:stylesheet>";
	static String xsl_lastfm ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\"><xsl:output encoding=\"utf-8\" indent=\"yes\" method=\"xml\"/>	<!-- Get songs by author -->	<xsl:template match=\"/lfm\">      <lastfm_2>        <xsl:apply-templates/>        </lastfm_2>	</xsl:template>		 <xsl:template match=\"topalbums\">              <xsl:for-each select=\"album\">             <album>              <Album_title>                <xsl:value-of select=\"name\"/>              </Album_title>              <Artist>                <xsl:value-of select=\"artist/name\"/>              </Artist>             </album>      </xsl:for-each>                 </xsl:template>  </xsl:stylesheet>";
	public static ArrayList<String> getAlbumsByAuthor(String artist) throws IOException, URISyntaxException, TransformerException, SQLException, ParserConfigurationException, SAXException  {
		ResultSet result = null;
		ResultSet result_after_insert = null;
		int i=0;
		ArrayList<String> result_list = new ArrayList<String>();
		DB_test db = new DB_test();
		// First search in the DB
		String sql= "SELECT * from Albums WHERE author = '" + artist+"'";
		result = db.search_DB(sql);
		try {
			if(result ==null || !result.next()) {
				// If cannot find result in DB, search externally
				//Query from brainz
				String url_brainz = "http://musicbrainz.org/ws/2/release/?query=artist:%22" + artist + "%22";
				Get_Conv_REST.get_XML(url_brainz, "albums.xml");
				Get_Conv_REST.conv("albums.xml", xsl_brainz,"BrainZ_getAlbum.out.xml");
				// todo: Dom get xml and insert into DB
				db.insertAlbumsIntoDB("BrainZ_getAlbum.out.xml", "BrainZ");
				//Query from lastfm
				String Url = "http://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist=" +artist+"&api_key=fdc873135eefe53670783bd15d92eed5";
				Get_Conv_REST.get_XML(Url, "albums.xml");
				Get_Conv_REST.conv("albums.xml", xsl_lastfm,"Lastfm_getAlbum.out.xml");
				// todo: Dom get xml and insert into DB
				db.insertAlbumsIntoDB("Lastfm_getAlbum.out.xml", "lastfm");
				result_after_insert = db.search_DB(sql);
				while(result_after_insert.next()) {
					if(result_after_insert.getObject(1) == null)
						break;
					System.out.println(result_after_insert.getObject(1));
					result_list.add(i++,(String) result_after_insert.getObject(1));
				}
			}
			else {
				while(result!=null && result.next()){
					if(result.getObject(1) == null)
						break;
					System.out.println(result.getObject(1));
					result_list.add(i++,(String) result.getObject(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result_list;
	}
	
  public static  void main(String args[]) throws IOException, URISyntaxException, TransformerException, SQLException, ParserConfigurationException, SAXException {
	  getAlbumsByAuthor("Wason");
}
}
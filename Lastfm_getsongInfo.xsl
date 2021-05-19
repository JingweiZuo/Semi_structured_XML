<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" encoding="utf-8" indent="yes" />
	<!-- Get songs by author -->

	 <xsl:template match="/trackmatches">
	     <lastfm_3>
        <xsl:for-each select="track">
          <song>
             <song_title>
              <xsl:value-of select="name"/>
             </song_title>
             <Artist>
                <xsl:value-of select="artist"/>
             </Artist>
             <Album></Album>
             <Score></Score>
             <listeners>
                <xsl:value-of select="listeners"/>
             </listeners>
          </song>
        </xsl:for-each>    
        </lastfm_3>
  </xsl:template>
  
</xsl:stylesheet>
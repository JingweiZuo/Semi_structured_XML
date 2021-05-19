<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" encoding="utf-8" indent="yes" />
	<!-- Get songs by author -->
	<xsl:template match="/lfm">
      <lastfm_2>
        <xsl:apply-templates/>
        </lastfm_2>
	</xsl:template>
	
	 <xsl:template match="topalbums">
      
        <xsl:for-each select="album">
             <album_title>
              <xsl:value-of select="name"/>
              <Artist>
                <xsl:value-of select="artist/name"/>
              </Artist>
             </album_title>
      </xsl:for-each>         
      
  </xsl:template>
  
</xsl:stylesheet>
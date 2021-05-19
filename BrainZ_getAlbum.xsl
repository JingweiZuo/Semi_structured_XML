<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n = "http://musicbrainz.org/ns/mmd-2.0#" xmlns:ext="http://musicbrainz.org/ns/ext#-2.0">
  <xsl:output method="xml" encoding="utf-8" indent="yes" />

<!--XSLT conversion pour MusicBrainz_Album-->
    <xsl:template match="/n:metadata" >
      <Brainz_2>
        <xsl:apply-templates/>
      </Brainz_2>
    </xsl:template>
    
    <xsl:template match="n:release-list">

	      <xsl:for-each select="n:release">
	         <Album>
            <Album_title>
	             <xsl:value-of select="n:title"/>
             </Album_title>
	           <Artist>
               <xsl:value-of select="n:artist-credit/n:name-credit/n:artist/n:name"/>
             </Artist>
	         </Album>
	      </xsl:for-each>         
      
    </xsl:template>
</xsl:stylesheet>
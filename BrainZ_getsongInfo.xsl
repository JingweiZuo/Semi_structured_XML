<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:n = "http://musicbrainz.org/ns/mmd-2.0#" xmlns:ext="http://musicbrainz.org/ns/ext#-2.0">
  <xsl:output method="xml" encoding="utf-8" indent="yes" />

<!--XSLT conversion pour MusicBrainz-->
    <xsl:template match="/n:metadata" >
      <Brainz_3>
        <xsl:apply-templates/>
      </Brainz_3>
    </xsl:template>
    
    <xsl:template match="n:recording-list">
      <xsl:for-each select="n:recording">
        <song>
          <song_title>
          <xsl:value-of select="n:title"/>
          </song_title>
          <Artist>
            <xsl:value-of select="//n:name"/>
          </Artist>
          <Album>
            <xsl:value-of select="n:release-list/n:release/n:title"/>
          </Album>
          <Score>
            <xsl:value-of select="@ext:score"/>
          </Score>
          <listeners></listeners>
        </song>
        
      </xsl:for-each>     
         
    </xsl:template>

</xsl:stylesheet>
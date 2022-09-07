<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  This stylesheet alters the structure of the elements from the utility domain.
-->
<xsl:stylesheet version="2.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
  exclude-result-prefixes="#all">
  
  <xsl:template match="*[contains(@class, ' ut-d/imagemap ')]">
    <!-- Let the default stylesheet generate the HTML map element, 
      this will be interpreted by Chemistry and other processors. -->
    <xsl:variable name="scale" select="@scale"/>
    <xsl:variable name="nm">
      <xsl:next-match/>
    </xsl:variable>
    
    <!-- Scale imagemap areas -->
    <xsl:apply-templates select="$nm" mode="scale-areas">
      <xsl:with-param name="scale" select="$scale" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="node() | @*" mode="scale-areas">
    <xsl:copy>
      <xsl:apply-templates select="node() | @* except @dita-ot:*" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@coords" mode="scale-areas">
    <xsl:param name="scale" tunnel="yes"/>
    <xsl:choose>
      <xsl:when test="$scale">
        <xsl:variable name="coords" select="tokenize(., ',')"/>
        
        <xsl:variable name="array">
          <xsl:for-each select="$coords">
            <xsl:variable name="i" select="position()"/>
            <xsl:variable name="coord" select="floor(number($coords[$i]) * number($scale) div 100)"/>
            <coord>
              <xsl:value-of select="$coord"/>
            </coord>
          </xsl:for-each>
        </xsl:variable>
        <xsl:attribute name="coords" select="$array/*" separator=", "/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:next-match/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>

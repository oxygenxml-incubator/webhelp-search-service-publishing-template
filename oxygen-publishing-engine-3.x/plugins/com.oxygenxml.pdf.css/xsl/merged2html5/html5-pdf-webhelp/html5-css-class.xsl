<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xs"
  version="2.0">
  
  <xsl:template match="
    *[contains(@class, ' equation-d/equation-figure ')] |
    *[contains(@class, ' topic/pre ')]">
    <xsl:variable name="scale" select="@scale"/>
    <xsl:variable name="result">
      <xsl:next-match/>
    </xsl:variable>
    <xsl:apply-templates select="$result" mode="process-css-class">
      <xsl:with-param name="scale" select="$scale" tunnel="yes"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="node() | @*" mode="process-css-class">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="
    *[contains(@class, ' equation-d/equation-figure ')]/@class |
    *[contains(@class, ' topic/pre ')]/@class" mode="process-css-class">
    <xsl:param name="scale" tunnel="yes"/>
    <xsl:choose>
      <xsl:when test="$scale">
        <xsl:variable name="css-class">
          <xsl:apply-templates select="$scale" mode="css-class"/>
        </xsl:variable>
        <xsl:attribute name="class" select="concat(., ' ', $css-class)"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:next-match/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>

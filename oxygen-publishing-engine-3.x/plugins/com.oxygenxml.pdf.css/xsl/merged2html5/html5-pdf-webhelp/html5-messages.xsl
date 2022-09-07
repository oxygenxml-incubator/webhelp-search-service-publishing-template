<?xml version="1.0" encoding="UTF-8"?>
<!--
  Custom messages handling.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:ditamsg="http://dita-ot.sourceforge.net/ns/200704/ditamsg" exclude-result-prefixes="xs ditamsg" version="2.0">

  <xsl:template match="*" mode="ditamsg:area-element-without-href-target">
    <xsl:variable name="imageref" select="preceding-sibling::*[contains(@class, ' topic/image ')][@keyref or @href]/(@keyref | @href)"/>
    <xsl:choose>
      <xsl:when test="$imageref">
        <xsl:call-template name="output-message">
          <xsl:with-param name="id" select="'OXYX001E'"/>
          <xsl:with-param name="msgparams">%1=<xsl:value-of select="$imageref"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="output-message">
          <xsl:with-param name="id" select="'DOTX044E'"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="*" mode="ditamsg:area-element-missing-coords">
    <xsl:variable name="imageref" select="preceding-sibling::*[contains(@class, ' topic/image ')][@keyref or @href]/(@keyref | @href)"/>
    <xsl:choose>
      <xsl:when test="$imageref">
        <xsl:call-template name="output-message">
          <xsl:with-param name="id" select="'OXYX001W'"/>
          <xsl:with-param name="msgparams">%1=<xsl:value-of select="$imageref"/></xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="output-message">
          <xsl:with-param name="id" select="'DOTX047W'"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<!-- Oxygen WeHhelp Plugin Copyright (c) 1998-2022 Syncro Soft SRL, Romania. All rights reserved. -->

<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:xs="http://www.w3.org/2001/XMLSchema" 
  xmlns:oxygen="http://www.oxygenxml.com/functions"  
  xmlns:relpath="http://dita2indesign/functions/relpath"
  
  exclude-result-prefixes="xs oxygen relpath" version="2.0">


  <xsl:import href="../util/relpath_util.xsl"/>
  <xsl:import href="plugin:org.dita.base:xsl/common/output-message.xsl"/>
  <xsl:import href="plugin:org.dita.base:xsl/common/dita-utilities.xsl"/>


  <!-- 
    Extracts the index terms, if available, as a separate file.
   -->

  <xsl:variable name="msgprefix">DOTX</xsl:variable>

  <!-- The prefix of the input XML file path. -->
  <xsl:param name="TEMPDIR_URL" />

  <!-- Extension of output files for example .html -->
  <xsl:param name="OUT_EXT" />

  <xsl:template match="/">

    <xsl:variable name="result">
      <index xmlns="http://www.oxygenxml.com/ns/webhelp/index">
        <xsl:apply-templates/>
      </index>
    </xsl:variable>
    
  
    <!-- Check if we have index terms, only then create the file. -->
    <xsl:if test="count($result/*/*) > 0">
      <xsl:result-document href="{concat(base-uri(), '.indexterms')}">
        <xsl:copy-of select="$result" />
      </xsl:result-document>
    </xsl:if>
  </xsl:template>

  <xsl:template match="text()|@*"/>

  <xsl:template match="*[contains(@class, ' topic/indexterm ')]">

    <xsl:variable name="thisIndexTerm" select="." />
    <xsl:variable name="textContent" 
      select="normalize-space(string-join(.//text()[ancestor::*[contains(@class, ' topic/indexterm ')][1] = $thisIndexTerm], ' '))" />
    <term xmlns="http://www.oxygenxml.com/ns/webhelp/index" 
      name="{$textContent}" 
      sort-as="{$textContent}">

      <xsl:if test="*[contains(@class, ' indexing-d/index-sort-as ')]">
        <xsl:attribute name="sort-as" 
          select="*[contains(@class, ' indexing-d/index-sort-as ')]" />
      </xsl:if>

      <xsl:if test="*[contains(@class, ' ut-d/sort-as ')]/@value">
        <xsl:attribute name="sort-as"
          select="*[contains(@class, ' ut-d/sort-as ')]/@value" />
      </xsl:if>

      <xsl:choose>
        <xsl:when test="*[contains(@class, ' topic/indexterm ')]">
          <xsl:apply-templates select="*[contains(@class, ' topic/indexterm ')]" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="target" select="oxygen:get-current-html-file-name(/)" />
        </xsl:otherwise>
      </xsl:choose>
    </term>
  </xsl:template>
  
  
  <xsl:function name="oxygen:get-current-html-file-name">
    <xsl:param name="node"/>
    <xsl:call-template name="replace-extension">
      <xsl:with-param name="filename" 
        select="substring-after(relpath:unencodeUri(document-uri($node)), 
                                relpath:unencodeUri($TEMPDIR_URL))"/>
      <xsl:with-param name="extension" select="$OUT_EXT"/>
    </xsl:call-template>
  </xsl:function>
  
</xsl:stylesheet>
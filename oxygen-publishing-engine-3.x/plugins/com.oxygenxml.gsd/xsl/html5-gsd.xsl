<?xml version="1.0" encoding="UTF-8"?>
<!--
    
Oxygen WebHelp Plugin
Copyright (c) 1998-2022 Syncro Soft SRL, Romania.  All rights reserved.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:oxygen="http://www.oxygenxml.com/functions"
    exclude-result-prefixes="xs"
    xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
    version="3.0">

	<xsl:param name="google.structured.data" />
    
	<xsl:include href="html5-gsd-from-qatopic.xsl"/>
	<xsl:include href="html5-gsd-from-prolog.xsl"/>
	<xsl:include href="html5-gsd-from-body.xsl" />
	<xsl:include href="html5-gsd-from-task-topic.xsl" />
	
	<!-- 
    	Adds custom script tags in head tag when running the HTML5 transformation.
	 -->
	<xsl:template 
		match="*[$google.structured.data = 'yes']" 
		mode="gen-user-scripts">
		
		<xsl:apply-templates select="." mode="generate-google-structered-data"/>
		
		<xsl:next-match />
	</xsl:template>
    
  <!-- 
  	Converts HTML nodes to a string used for JSON objects. 
   -->
  <xsl:function name="oxygen:convertHtmlToJsonString" as="xs:string">
  	<xsl:param name="html" />
  	
  	<xsl:variable name="serial-params" xmlns:output="http://www.w3.org/2010/xslt-xquery-serialization">
    	<output:serialization-parameters>
           	<output:method value="json"/>
           	<output:json-node-output-method value="xml"/>
           	<output:use-character-maps>
               	<output:character-map character="/" map-string="/"/>
               	<!-- Replace \n -->
               	<output:character-map character="&#10;" map-string=" "/>
               	<!-- Replace \t -->
               	<output:character-map character="&#09;" map-string=" "/>
               	<!-- Replace \r -->
               	<output:character-map character="&#13;" map-string=" "/>
           	</output:use-character-maps>
       	</output:serialization-parameters>
    </xsl:variable>
        
    <xsl:value-of select="normalize-space(serialize($html, $serial-params/*))" disable-output-escaping="yes"/>
  </xsl:function>
  
  <!-- 
  	Escape the given string for JSON serialization. 
   -->
  <xsl:function name="oxygen:escapeStringInJson" as="xs:string">
  	<xsl:param name="string" as="xs:string" />

  	<xsl:value-of select="oxygen:convertHtmlToJsonString($string)"/>
  </xsl:function>
	
</xsl:stylesheet>
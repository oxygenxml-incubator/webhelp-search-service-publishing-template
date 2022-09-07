<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    A set of HTML5 structre fixes that are used from PDF and Webhelp transformation.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
      
  <xsl:include href="html5-class-attributes.xsl"/>
  <xsl:include href="html5-figures.xsl"/>
  <xsl:include href="html5-images.xsl"/>
  <xsl:include href="html5-tables.xsl"/>
  <xsl:include href="html5-choicetables.xsl"/>
  <xsl:include href="html5-mathml.xsl"/>
  <xsl:include href="html5-rel-links.xsl"/>
  <xsl:include href="html5-learning.xsl"/>
  <xsl:include href="html5-tm.xsl"/>
  <xsl:include href="html5-hi-domain.xsl"/>
  <xsl:include href="html5-steps.xsl"/>
  <xsl:include href="html5-hazard.xsl"/>
  <xsl:include href="html5-css-class.xsl"/>
  <xsl:include href="html5-messages.xsl"/>
  <xsl:include href="html5-ut-domain.xsl"/>
	
  <!-- Custom handling for dita documents resulted from OpenAPI conversion (EXM-50477)-->	
  <xsl:include href="html5-openAPI-lists.xsl"/>
</xsl:stylesheet>
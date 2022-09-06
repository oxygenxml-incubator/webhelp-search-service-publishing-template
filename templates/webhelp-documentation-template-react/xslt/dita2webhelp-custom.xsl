<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">    
    <!--
        Replace search form with Algolia autocomplete form
    -->
    <xsl:import href="custom-search-page.xsl"/>
    
    <!--
        Display topic keywords as labels in topic
    -->
    <xsl:import href="labels-show.xsl"/>
    
    <!--
        Push profiling information from toc.xml in topic HTML as meta
    -->
    <xsl:import href="push-conditional-profiling.xsl"/>
</xsl:stylesheet>
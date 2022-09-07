<?xml version="1.0" encoding="UTF-8"?>
<!--
    
Oxygen WebHelp Plugin
Copyright (c) 1998-2022 Syncro Soft SRL, Romania.  All rights reserved.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                    xmlns:oxygen="http://www.oxygenxml.com/functions"
                    xmlns:xs="http://www.w3.org/2001/XMLSchema"
                    xmlns:micro="http://www.oxygenxml.com/ns/webhelp/microcontent"
                    exclude-result-prefixes="oxygen micro"
                    version="2.0">
    
    <xsl:import href="../util/functions.xsl"/>
    
    <!-- URL of folder with microcontent files. -->
    <xsl:param name="TEMPFOLDER"/>
    
    <!-- URL of the properties file that indicates microcontent exists or not. -->
    <xsl:param name="MICROCONTENT_PROPERTIES_URL"/>
    
    <!-- The job file contains a list of all topic files. -->
    <xsl:param name="JOB_FILE"/>
    
    <!-- Define the output for the properties file. -->
    <xsl:output name="properties" 
                omit-xml-declaration="yes" 
                method="text"/> 
        
    <xsl:template match="/">
        <xsl:variable
          name="jobFiles" 
          select="document(oxygen:makeURL($JOB_FILE), .)//file[@format='dita'][not(@resource-only='true')]/@uri"/>


        <xsl:variable name="intents">
          <xsl:for-each select="$jobFiles">
              <xsl:variable 
                  name="MICROCONTENT_URL" 
                  select="oxygen:makeURL(concat($TEMPFOLDER, '/', ., '.microcontent'))"/>
  
              <xsl:copy-of select="document($MICROCONTENT_URL, .)/*/*"/>                
          </xsl:for-each>
        </xsl:variable>
        
        <!-- Output the xml file to the default output configured from the ANT pipeline. -->
        <micro:microcontent>
          <xsl:copy-of select="$intents"/>
        </micro:microcontent>
                
        <!-- Output the properties file. -->
        <xsl:result-document href="{$MICROCONTENT_PROPERTIES_URL}" format="properties">
            <xsl:text>microcontent.available=</xsl:text><xsl:value-of select="count($intents/*) gt 0"/>
        </xsl:result-document>
    </xsl:template>


    
</xsl:stylesheet>
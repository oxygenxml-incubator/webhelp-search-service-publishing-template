<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:toc="http://www.oxygenxml.com/ns/webhelp/toc"
    
    exclude-result-prefixes="xs"
    version="2.0">
    
    <!--
        Generate <meta> element with conditional profiling values associated with this topic.
    -->
    <xsl:template match="/|node()|@*" mode="gen-user-head-child-elements">
        
        <xsl:variable name="topicIDURL" select="
            concat($TEMPDIR_URL, '/', $FILEDIR, '/', substring-before($FILENAME, '.'), '.tocid')"/>
        <xsl:variable name="tocid" select="unparsed-text($topicIDURL)"/>
        
        <xsl:variable name="topic" select="$toc//toc:topic[@wh-toc-id=$tocid]"/>
        
        <xsl:if test="$topic/@data-product">
            <!-- Topic with data-product info -->
            <xsl:element name="meta" namespace="http://www.w3.org/1999/xhtml">
                <xsl:attribute name="name" select="'wh-data-product'"></xsl:attribute>
                <xsl:attribute name="content" select="$topic/@data-product"></xsl:attribute>
            </xsl:element>
        </xsl:if>
        
        <xsl:if test="$topic/@data-audience">
            <!-- Topic with data-product info -->
            <xsl:element name="meta" namespace="http://www.w3.org/1999/xhtml">
                <xsl:attribute name="name" select="'wh-data-audience'"></xsl:attribute>
                <xsl:attribute name="content" select="$topic/@data-audience"></xsl:attribute>
            </xsl:element>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
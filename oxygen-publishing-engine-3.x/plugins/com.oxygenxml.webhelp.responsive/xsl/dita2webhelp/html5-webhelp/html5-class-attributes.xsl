<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Propagate the DITA class attribute to the output.
    In this way the print CSSs developed for the direct 
    transformation are common with the ones for the HTML transformation.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <!-- 
        Overrides template from org.dita.html5/xsl/topic.xsl.
        
        Adds @class="- topic/topic topic" on first level articles.
    -->
    
    <xsl:template match="*" mode="addContentToHtmlBodyElement" priority="2">
        <!-- 
            Calls com.oxygenxml.webhelp.responsive/xsl/dita2webhelp/html5-pdf-webhelp/html5-class-attributes.xsl.
        -->
        <xsl:variable name="class">
            <xsl:apply-templates select="." mode="get-element-ancestry"/>
        </xsl:variable>
        
        <xsl:variable name="nm">
            <xsl:next-match/>
        </xsl:variable>
        
        <xsl:apply-templates select="$nm" mode="add-class-attribute">
            <xsl:with-param name="class" select="$class" tunnel="yes"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="add-class-attribute">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*" mode="#current"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*:article[not(@class)]" mode="add-class-attribute">
        <xsl:param name="class" tunnel="yes"/>
        <xsl:copy>
            <xsl:attribute name="class" select="$class"/>
            <xsl:apply-templates select="node() | @*" mode="#current"/>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>
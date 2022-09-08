<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"    
    xmlns:oxygen="http://www.oxygenxml.com/functions"
    exclude-result-prefixes="oxygen"     
    
    version="2.0">
    
    <xsl:output name="json" omit-xml-declaration="yes" indent="yes"/>
    
    <xsl:template match="/">
        <xsl:next-match/>
        
        <!-- Test if there is subject scheme map info -->
        <xsl:if test="//*[contains(@class, ' subjectScheme/hasInstance ')]">
            <xsl:variable name="subjectScheme" select="
                oxygen:makeURL(
                    concat(oxygen:getParameter('dita.map.output.dir'),'/subject-scheme-values.json'))"/>
            <xsl:result-document format="json" href="{$subjectScheme}">
                <xsl:text disable-output-escaping="yes">{</xsl:text>
                <xsl:text disable-output-escaping="yes">"subjectScheme" : {</xsl:text>
                <xsl:text disable-output-escaping="yes">"attrValues" : [</xsl:text>
                <xsl:apply-templates mode="process-subject"/>
                <xsl:text disable-output-escaping="yes">]</xsl:text>
                <xsl:text disable-output-escaping="yes">}</xsl:text>
                <xsl:text disable-output-escaping="yes">}</xsl:text>
            </xsl:result-document>
            
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' subjectScheme/enumerationdef ')]/*[contains(@class, ' subjectScheme/attributedef ')]" 
        mode="process-subject">
        
        <xsl:variable name="subjectDefinition" 
            select="parent::*/*[contains(@class, ' subjectScheme/subjectdef ')]/@keyref"/>
        
        <xsl:if test="exists($subjectDefinition)">
            <xsl:variable name="subjectDefValues" select="
                //*[contains(@class, ' subjectScheme/hasInstance ')]/*[contains(@class, ' subjectScheme/subjectdef ')][@keys=$subjectDefinition]"/>
            <xsl:if test="exists($subjectDefValues)">
                <xsl:text disable-output-escaping="yes">{</xsl:text>
                <xsl:text disable-output-escaping="yes">"name" : "</xsl:text> <xsl:value-of select="@name"/><xsl:text>",</xsl:text>
                <xsl:text disable-output-escaping="yes">"values" : [</xsl:text>
                <xsl:apply-templates select="$subjectDefValues/*" mode="process-subject-values"></xsl:apply-templates>
                <xsl:text disable-output-escaping="yes">]</xsl:text>
                <xsl:text disable-output-escaping="yes">}</xsl:text>
                
                <xsl:if test="parent::*/following-sibling::*/*[contains(@class, ' subjectScheme/subjectdef ')]/@keyref = //*[contains(@class, ' subjectScheme/hasInstance ')]/*[contains(@class, ' subjectScheme/subjectdef ')]/@keys">
                    <xsl:text disable-output-escaping="yes">,</xsl:text>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="process-subject">
        <xsl:apply-templates select="node() | @*" mode="process-subject"/>
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' subjectScheme/subjectdef ')][@keys]"
        mode="process-subject-values">
        <xsl:text disable-output-escaping="yes">{</xsl:text>
        <xsl:text disable-output-escaping="yes">"key" : "</xsl:text> <xsl:value-of select="@keys"/><xsl:text>",</xsl:text>
        <xsl:apply-templates mode="process-subject-values"/>
        <xsl:text disable-output-escaping="yes">}</xsl:text>
        <xsl:if test="following-sibling::*[contains(@class, ' subjectScheme/subjectdef ')][@keys]">,</xsl:if>
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' topic/navtitle ')]"
        mode="process-subject-values">
        <xsl:text disable-output-escaping="yes">"navTitle" : "</xsl:text> <xsl:value-of select="."/><xsl:text>"</xsl:text>
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="process-subject-values">
        <xsl:apply-templates select="node() | @*" mode="process-subject-values"/>
    </xsl:template>
    
    
            
    
</xsl:stylesheet>
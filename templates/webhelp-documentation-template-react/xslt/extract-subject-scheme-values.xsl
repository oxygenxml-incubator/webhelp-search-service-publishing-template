<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"    
    xmlns:oxygen="http://www.oxygenxml.com/functions"
    exclude-result-prefixes="oxygen"     
    
    version="2.0">
    <xsl:template match="/">
        <xsl:next-match/>
        
        <!-- Test if there is subject scheme map info -->
        <xsl:if test="//*[contains(@class, ' subjectScheme/hasInstance ')]">
            <xsl:variable name="subjectScheme" select="
                oxygen:makeURL(
                    concat(oxygen:getParameter('dita.map.output.dir'),'/subject-scheme-values.xml'))"/>
            
            <xsl:result-document href="{$subjectScheme}">
                <subjectScheme>
                    <xsl:apply-templates mode="process-subject"/>
                </subjectScheme>
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
                <attrValues>
                    <name><xsl:value-of select="@name"/></name>
                    <values>
                        <xsl:apply-templates select="$subjectDefValues/*" mode="process-subject-values"></xsl:apply-templates>
                    </values>
                </attrValues>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="process-subject">
        <xsl:apply-templates select="node() | @*" mode="process-subject"/>
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' subjectScheme/subjectdef ')][@keys]"
        mode="process-subject-values">
        <value key="{@keys}">
            <xsl:apply-templates mode="process-subject-values"/>
        </value>        
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' topic/navtitle ')]"
        mode="process-subject-values">
        <navTitle><xsl:value-of select="."/></navTitle>        
    </xsl:template>
    
    <xsl:template match="node() | @*" mode="process-subject-values">
        <xsl:apply-templates select="node() | @*" mode="process-subject-values"/>
    </xsl:template>
    
    
            
    
</xsl:stylesheet>
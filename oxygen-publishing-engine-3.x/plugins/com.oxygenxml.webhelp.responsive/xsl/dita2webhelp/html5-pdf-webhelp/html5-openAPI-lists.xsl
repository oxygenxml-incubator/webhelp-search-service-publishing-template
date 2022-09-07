<?xml version="1.0" encoding="UTF-8"?>
<!--
  Convert some definition lists from DITA documents resulted from OpenAPI conversion to tables.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    
    <!-- The outputclasses of lists to convert in tables -->
    <xsl:variable name="listsToConvert" 
        select="('openapi-parameter-properties', 'openapi-schema-values', 'openapi-link-fields-list',
        'openapi-link-parameters-list', 'openapi-mediaType-fields-list', 'openapi-securityScheme-properties',
        'openapi-specification-extensions')"/>
    
    <xsl:template match="*[contains(@class, ' topic/dl ')][@outputclass = $listsToConvert]">
        <xsl:call-template name="setaname"/>
        <xsl:apply-templates select="
            *[contains(@class,
            ' ditaot-d/ditaval-startprop ')]" mode="out-of-line"/>
        <!-- Wrap in a table -->
        <table>
            <xsl:call-template name="commonattributes"/>
            <xsl:call-template name="setid"/>
            <xsl:apply-templates/>
        </table>
        <xsl:apply-templates select="
            *[contains(@class,
            ' ditaot-d/ditaval-endprop ')]" mode="out-of-line"/>
    </xsl:template>
    
    <xsl:template match="*[contains(@class, ' topic/dl ')][@outputclass = $listsToConvert]/*[contains(@class, ' topic/dlentry ')]">
        <!-- Wrap in a table row -->
        <tr>
            <xsl:call-template name="commonattributes"/>
            <xsl:call-template name="setidaname"/>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>
    
  <xsl:template match="*[contains(@class, ' topic/dl ')][@outputclass = $listsToConvert]/*[contains(@class, ' topic/dlentry ')]/*[contains(@class, ' topic/dd ') or contains(@class, ' topic/dt ')]">
        <!-- Wrap in a cell -->
        <td valign="top">
        	<xsl:choose>
                <xsl:when test=".[contains(@class, ' topic/dt ')]">
                    <xsl:call-template name="commonattributes">
                        <xsl:with-param name="default-output-class" select="'dltermexpand'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="commonattributes"/>
                </xsl:otherwise>
            </xsl:choose>
            
            <xsl:call-template name="setidaname"/>
            <xsl:apply-templates select="
                ../*[contains(@class,
                ' ditaot-d/ditaval-startprop ')]" mode="out-of-line"/>
            <xsl:apply-templates/>
            <xsl:apply-templates select="
                ../*[contains(@class,
                ' ditaot-d/ditaval-endprop ')]" mode="out-of-line"/>
        </td>
    </xsl:template>
</xsl:stylesheet>
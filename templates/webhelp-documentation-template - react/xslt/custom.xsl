<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    
    
    xmlns:whc="http://www.oxygenxml.com/webhelp/components"
    xmlns="http://www.w3.org/1999/xhtml"
    
    
    exclude-result-prefixes="xs"
    version="2.0">
    
    <!-- 
         Generate  <div  id="autocomplete"> for Algolia autocomplete integration. 
    -->
    <xsl:template match="whc:webhelp_search_input" mode="copy_template">
        <!-- EXM-36737 - Context node used for messages localization -->
        <xsl:param name="i18n_context" tunnel="yes" as="element()*"/>
        <div>
            <xsl:call-template name="generateComponentClassAttribute">
                <xsl:with-param name="compClass">wh_search_input</xsl:with-param>
            </xsl:call-template>
            <!-- Copy attributes -->
            <xsl:copy-of select="@* except @class"/>
            
            <xsl:variable name="localizedSearch">
                <xsl:choose>
                    <xsl:when test="exists($i18n_context)">
                        <xsl:for-each select="$i18n_context[1]">
                            <xsl:call-template name="getWebhelpString">
                                <xsl:with-param name="stringName" select="'webhelp.search'"/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>Search</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:variable name="localizedSearchQuery">
                <xsl:choose>
                    <xsl:when test="exists($i18n_context)">
                        <xsl:for-each select="$i18n_context[1]">
                            <xsl:call-template name="getWebhelpString">
                                <xsl:with-param name="stringName" select="'search.query'"/>
                            </xsl:call-template>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>Search query</xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            
            <xsl:variable name="search_comp_output">
                <form id="searchForm"
                    method="get"
                    role="search"                            
                    action="{concat($PATH2PROJ, 'search', $OUTEXT)}">
                    <div  id="autocomplete">
                        <!--<input type="search" placeholder="{$localizedSearch} " class="wh_search_textfield"
                            id="textToSearch" name="searchQuery" aria-label="{$localizedSearchQuery}" required="required"/>
                        <button type="submit" class="wh_search_button" aria-label="{$localizedSearch}"><span class="search_input_text"><xsl:value-of select="$localizedSearch"/></span></button>-->
                    </div>
                </form>
            </xsl:variable>
            
            <xsl:call-template name="outputComponentContent">
                <xsl:with-param name="compContent" select="$search_comp_output"/>
                <xsl:with-param name="compName" select="local-name()"/>
            </xsl:call-template>
        </div>
    </xsl:template>
    <xsl:template match="whc:webhelp_search_results" mode="copy_template"><div id="search-results"></div></xsl:template>
</xsl:stylesheet>
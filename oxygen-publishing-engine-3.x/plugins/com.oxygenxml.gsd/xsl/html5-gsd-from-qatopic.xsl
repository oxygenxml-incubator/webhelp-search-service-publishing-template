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
	
	<!-- 
    	Adds a script that contains a JSON-LD object of type FAQPage which will be generated for every for a qatopic.
     -->
	<xsl:template match="*[contains(@class, ' qatopic/qatopic')]" mode="generate-google-structered-data">
		<script type="application/ld+json" id="jsonld-faq">
			{
				"@context": "https://schema.org",
				"@type": "FAQPage",
				"mainEntity": [
					<xsl:apply-templates select="*[contains(@class, ' qatopic/qabody')]/*[contains(@class, ' qatopic/qagroup')]"  mode="#current" />			
				]
			}
		</script>
		
		<xsl:next-match/>
	</xsl:template>
    
    <!-- 
		Matches every qagroup element from the qa topic. 
	-->
	<xsl:template match="*[contains(@class, ' qatopic/qagroup')]" mode="generate-google-structered-data">
		<xsl:variable name="answerHtml">
			<xsl:apply-templates select="*[contains(@class, ' qatopic/answer')]"/>
		</xsl:variable>
		{
			"@type": "Question",
			"name": <xsl:value-of select="oxygen:escapeStringInJson(*[contains(@class, ' qatopic/question')])"/>,
	        "acceptedAnswer": {
	          "@type": "Answer",
	          "text": <xsl:value-of select="oxygen:convertHtmlToJsonString($answerHtml)" />
	        }
		}
		<xsl:if test="following-sibling::*[contains(@class, ' qatopic/qagroup')]">,</xsl:if>
	</xsl:template>
    
</xsl:stylesheet>
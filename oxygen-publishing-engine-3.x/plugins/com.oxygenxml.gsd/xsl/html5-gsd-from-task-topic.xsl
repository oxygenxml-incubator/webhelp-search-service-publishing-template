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
    	Adds a script that contains a JSON-LD object of type HowTo which will be generated for every for a task topic.
     -->
	<xsl:template match="*[contains(@class, ' task/task ')]" mode="generate-google-structered-data">
		
		<script type="application/ld+json" id="jsonld-howto">
			{
				"@context": "https://schema.org",
				"@type": "HowTo",
				"name": <xsl:apply-templates select="//*[contains(@class, ' task/task ')]/*[contains(@class, ' topic/title ')]" mode="generate-google-structered-data-task-topic" />,
				"description": <xsl:apply-templates select="//*[contains(@class, ' task/task ')]/*[contains(@class, ' topic/shortdesc ')]" mode="generate-google-structered-data-task-topic" />,
				"supply": [],
				"tool": [],
				"step":[
					<xsl:apply-templates select="*[contains(@class, ' task/taskbody ')]/*[contains(@class, ' task/steps ')]/*[contains(@class, ' task/step ')]" mode="generate-google-structered-data-task-topic" />
				]
			}
		</script>
		
		<xsl:next-match/>
	</xsl:template>
    
    <!-- 
		Matches the task title. Copies the element text.
	-->
	<xsl:template match="*[contains(@class, ' topic/title ')]" mode="generate-google-structered-data-task-topic">
		<xsl:value-of select="oxygen:escapeStringInJson(text())"/>
	</xsl:template>
	
	<!-- 
		Matches the task short description. Copies the element text.
	-->
	<xsl:template match="*[contains(@class, ' topic/shortdesc ')]" mode="generate-google-structered-data-task-topic">
		<xsl:value-of select="oxygen:escapeStringInJson(text())"/>
	</xsl:template>
	
	<!-- 
		Matches each step from the task. Generates a JSON object of type HowToStep.
	-->
	<xsl:template match="*[contains(@class, ' task/step ')]" mode="generate-google-structered-data-task-topic">
		<xsl:variable name="answerHtml">
			<xsl:apply-templates select="node()[not(contains(@class, ' task/substeps '))]"/>	
		</xsl:variable>
		
		<xsl:variable name="imagesInStep" select="descendant::*[contains(@class, ' topic/image ')]" />
		<xsl:if test="count($imagesInStep) &gt; 1">
			<xsl:message>More than one image found in a task step content. Selecting the first one to be used in Google Structured Data.</xsl:message>
		</xsl:if>
		{
			"@type": "HowToStep",
			"name": "",
			"url": "",
			"text": <xsl:value-of select="oxygen:convertHtmlToJsonString($answerHtml)" />
			<xsl:if test="count($imagesInStep) gt 0">
				, "image": <xsl:apply-templates select="$imagesInStep[1]" mode="generate-google-structered-data-task-topic" />
			</xsl:if>
			
			<xsl:apply-templates select="*[contains(@class, ' task/substeps ')]" mode="generate-google-structered-data-task-topic" />
			
		}
		<xsl:if test="following-sibling::*[contains(@class, ' task/step ')]">,</xsl:if>
	</xsl:template>
	
	
	<!-- 
		Matches the substeps from a steps task. Generates an JSON array that 
		will contain objects of type HowToDirection.
	-->
	<xsl:template match="*[contains(@class, ' task/substeps ')]" mode="generate-google-structered-data-task-topic">
		,"itemListElement": [
			<xsl:apply-templates select="*[contains(@class, ' task/substep ')]" mode="generate-google-structered-data-task-topic" />
		]
	</xsl:template>
	
	<!-- 
		Matches the substep. Generates an JSON object of type HowToDirection.
	-->
	<xsl:template match="*[contains(@class, ' task/substep ')]" mode="generate-google-structered-data-task-topic">
		<xsl:variable name="answerHtml">
			<xsl:apply-templates select="."/>
		</xsl:variable>
		{
			"@type": "HowToDirection",
			"text": <xsl:value-of select="oxygen:convertHtmlToJsonString($answerHtml)" />
		}
		<xsl:if test="following-sibling::*[contains(@class, ' task/substep ')]">,</xsl:if>
	</xsl:template>
	
	<!-- 
		Matches the image in a step. Retrieves the href value of the image as a JSON string.
	-->
	<xsl:template match="*[contains(@class, ' topic/image ')]" mode="generate-google-structered-data-task-topic">
    	<xsl:value-of select="oxygen:escapeStringInJson(./@href)" />
	</xsl:template>
	
</xsl:stylesheet>
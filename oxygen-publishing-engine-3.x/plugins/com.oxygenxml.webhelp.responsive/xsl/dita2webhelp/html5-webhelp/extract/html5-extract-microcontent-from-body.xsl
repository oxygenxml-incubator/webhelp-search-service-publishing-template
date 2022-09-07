<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:oxygen="http://www.oxygenxml.com/functions"
    xmlns:micro="http://www.oxygenxml.com/ns/webhelp/microcontent"
    xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
    exclude-result-prefixes="xs dita-ot oxygen"
    version="2.0">
	
	<!-- 
		Extracts the microcontent from a data elements associated 
		to paragraphs, lists, sections, etc..
	-->
	

	<!-- 
  ========================================================
  Templates that apply when generating the microcontent
  file
  ========================================================
   -->
	
	<xsl:template name="extract-microcontent-from-body">
		<xsl:apply-templates 
      select="/*[contains(@class,' topic/topic ')]/*[contains(@class,' topic/body ')]" 
      mode="extract-microcontent-from-body"/>		
	</xsl:template>
	
	<!-- Matches any question parent element. This is the intent.-->
	<xsl:template 
    match="*[./*[contains(@class,' topic/data ')][@name='oxy:question']]" 
    mode="extract-microcontent-from-body">
    
		<micro:intent>
			<xsl:apply-templates mode="#current"/>
			<micro:answer>
				<xsl:call-template name="create-href-using-id-from-current-node"/>    
				<xsl:apply-templates select="."/>
			</micro:answer>        			
		</micro:intent>
	</xsl:template>

  <xsl:template match="text()" mode="extract-microcontent-from-body"/>
	
	<xsl:template 
    match="*[contains(@class,' topic/data ')][@name='oxy:question']" 
    mode="extract-microcontent-from-body">
		<micro:question>
			<xsl:apply-templates select="child::node()"/>
		</micro:question>        
	</xsl:template>
	
	<xsl:template name="create-href-using-id-from-current-node">
		<xsl:choose>
			<xsl:when test="@id">
				<xsl:attribute name="href" select="concat(
					oxygen:get-current-html-file-name(/),
					'#', 
          dita-ot:generate-id(
            ancestor::*[contains(@class, ' topic/topic ')][1]/@id, 
            @id)
          )"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="href" select="concat(
					oxygen:get-current-html-file-name(/),
					'#',
          dita-ot:generate-id(
            ancestor::*[contains(@class, ' topic/topic ')][1]/@id, 
            generate-id(.))
					)"/>
			</xsl:otherwise>      
		</xsl:choose>
	</xsl:template>


	<!-- 
  ========================================================
  Templates that apply when generating the HTML
  file
  ========================================================
   -->
	
	<!-- 
    Generate an ID on the question parent that do not have an ID. 
    This is necessary in order to create links to these elements,
    from the microcontent (let say the chatbot identifies an answer, it may
    very well provide a link to the answer). 
   -->
	<xsl:template match="*[./*[contains(@class,' topic/data ')][@name='oxy:question']][not(@id)]" priority="2">		
		<xsl:call-template name="add-id-for-microcontent"/>		
	</xsl:template>

</xsl:stylesheet>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:oxygen="http://www.oxygenxml.com/functions"
	xmlns:micro="http://www.oxygenxml.com/ns/webhelp/microcontent"
	xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
	exclude-result-prefixes="xs dita-ot oxygen" version="2.0">

	<!-- 
		Extracts the microcontent from a 'qatopic' specialization.
	-->

	<!-- 
  ========================================================
  Templates that apply when generating the microcontent
  file
  ========================================================
   -->
	<xsl:template name="extract-microcontent-from-qatopic">
		<xsl:apply-templates select="." mode="extract-microcontent-from-qatopic"/>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' qatopic/qagroup ')]"
		mode="extract-microcontent-from-qatopic">
		<micro:intent>
			<xsl:apply-templates mode="#current"/>
		</micro:intent>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' qatopic/question ')]"
		mode="extract-microcontent-from-qatopic">
		<micro:question>
			<xsl:apply-templates select="child::node()"/>
		</micro:question>
	</xsl:template>

	<xsl:template match="*[contains(@class, ' qatopic/answer ')]"
		mode="extract-microcontent-from-qatopic">
		<micro:answer>
			<xsl:call-template name="create-href-using-id-from-parent"/>
			<xsl:apply-templates select="child::node()"/>
		</micro:answer>
	</xsl:template>

	<xsl:template match="text()" mode="extract-microcontent-from-qatopic"/>

	<xsl:template name="create-href-using-id-from-parent">
		<xsl:choose>
			<xsl:when test="../@id">
				<xsl:attribute name="href" select="
						concat(
						oxygen:get-current-html-file-name(/),
						'#',
						dita-ot:generate-id(
						ancestor::*[contains(@class, ' topic/topic ')][1]/@id,
						../@id)
						)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="href" select="
						concat(
						oxygen:get-current-html-file-name(/),
						'#',
						dita-ot:generate-id(
						ancestor::*[contains(@class, ' topic/topic ')][1]/@id,
						generate-id(..))
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
    Generate an ID on the qagroups that do not have an ID. 
    This is necessary in order to create links to these elements,
    from the microcontent (let say the chatbot identifies an answer, it may
    very well provide a link to the answer, in the FAQ). 
   -->
	<xsl:template match="*[contains(@class, ' qatopic/qagroup ')][not(@id)]" priority="2">
		<xsl:call-template name="add-id-for-microcontent"/>
	</xsl:template>

</xsl:stylesheet>

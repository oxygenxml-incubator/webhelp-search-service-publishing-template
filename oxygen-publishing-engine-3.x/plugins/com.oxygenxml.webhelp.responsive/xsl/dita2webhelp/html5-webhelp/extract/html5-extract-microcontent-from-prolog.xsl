<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:oxygen="http://www.oxygenxml.com/functions"
	xmlns:micro="http://www.oxygenxml.com/ns/webhelp/microcontent"
	xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
	exclude-result-prefixes="xs dita-ot oxygen" version="2.0">

	<xsl:template name="extract-microcontent-from-prolog">
		<xsl:param name="questions-in-prolog"/>

		<micro:intent>
			<xsl:for-each select="$questions-in-prolog">
				<micro:question>
					<xsl:apply-templates select="child::node()"/>
				</micro:question>
			</xsl:for-each>
			<micro:answer href="{oxygen:get-current-html-file-name(/)}"/>
		</micro:intent>

	</xsl:template>

</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<!-- 

  This stylesheet changes the tasks steps structure.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs"
                version="2.0">
  
  <!-- Copied from: org.dita.html5/xsl/task.xsl -->
  <!-- Marks the parent step section with step class (ol/ul) -->
  <xsl:template match="*[contains(@class,' task/steps ') or contains(@class,' task/steps-unordered ')]"
    mode="common-processing-within-steps">
    <xsl:param name="step_expand"/>
    <xsl:param name="list-type">
      <xsl:choose>
        <xsl:when test="contains(@class,' task/steps ')">ol</xsl:when>
        <xsl:otherwise>ul</xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <xsl:variable name="result">
      <xsl:next-match/>
    </xsl:variable>
    
    <xsl:apply-templates select="$result" mode="add-section-class">
      <xsl:with-param name="list-type" select="$list-type"/>
    </xsl:apply-templates>
  </xsl:template>
  
  <xsl:template match="node() | @*" mode="add-section-class">
    <xsl:copy>
      <xsl:apply-templates select="node() | @*" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="section[not(@class)]" mode="add-section-class">
    <xsl:param name="list-type"/>
    <xsl:copy>
      <!-- Default HTML5 style-sheet already set an id on section child (div, ol or ul). -->
      <xsl:apply-templates select="@* except (@id, @data-ofbid)" mode="#current"/>
      <xsl:choose>
        <xsl:when test="$list-type = 'ol'">
          <xsl:attribute name="class">- topic/ol task/steps ol steps</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:attribute name="class">- topic/ul task/steps-unordered ul steps-unordered</xsl:attribute>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates select="node()" mode="#current"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="*" mode="step-elements-with-stepsection">
    <xsl:param name="step_expand"/>
    <xsl:param name="list-type"/>
    <xsl:for-each select="*">
      <xsl:choose>
        <xsl:when test="contains(@class,' task/stepsection ')">
          <xsl:apply-templates select="."/>
        </xsl:when>
        <xsl:when test="contains(@class,' task/step ') and preceding-sibling::*[1][contains(@class,' task/step ')]">
          <!-- Do nothing, was pulled in through recursion -->
        </xsl:when>
        <xsl:when test="not(contains(@class,' task/stepsection ') or contains(@class,' task/step ')) and
          preceding-sibling::*[1][not(contains(@class,' task/stepsection ') or contains(@class,' task/step '))]">
          <!-- Do nothing, was pulled in through recursion -->
        </xsl:when>
        <xsl:when test="not(contains(@class,' task/stepsection ') or contains(@class,' task/step ')) and
          following-sibling::*[1][contains(@class,' task/stepsection ') or contains(@class,' task/step ')]">
          <!-- Do nothing, was pulled in through recursion -->
        </xsl:when>
        <xsl:otherwise>
          <!-- First step in a series of steps -->
          <xsl:element name="{$list-type}">
            <xsl:for-each select=".."><xsl:call-template name="commonattributes"/></xsl:for-each>
            <xsl:if test="$list-type='ol' and preceding-sibling::*[contains(@class,' task/step ')]">
              <!-- Restart numbering for ordered steps that were interrupted by stepsection.
                   The start attribute is valid in XHTML 1.0 Transitional, but not for XHTML 1.0 Strict.
                   It is possible (preferable) to keep stepsection within an <li> and use CSS to
                   fix numbering, but with testing in March of 2009, this does not work in IE. 
                   It is possible in Firefox 3. -->
              <xsl:attribute name="start"><xsl:value-of select="count(preceding-sibling::*[contains(@class,' task/step ')])+1"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="." mode="steps">
              <xsl:with-param name="step_expand" select="$step_expand"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="following-sibling::*[1][contains(@class,' task/step ')]" mode="sequence-of-steps">
              <xsl:with-param name="step_expand" select="$step_expand"/>
            </xsl:apply-templates>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  
  <!-- Recursive template processing non-standard elements like oxy review. -->
  <xsl:template match="*[not(contains(@class,' task/stepsection ') or contains(@class,' task/step '))]" mode="steps">
    <xsl:apply-templates select="."/>
    <xsl:apply-templates select="following-sibling::*[1][not(contains(@class,' task/stepsection ') or contains(@class,' task/step '))]" mode="#current"/>
  </xsl:template>
  
  <xsl:template match="*[contains(@class,' task/step ')]" mode="onestep">
    <xsl:param name="step_expand"/>
    <!-- Process non-standard elements like oxy review before the step. -->
    <xsl:apply-templates select="parent::node()/*[1][not(contains(@class,' task/stepsection ') or contains(@class,' task/step '))]" mode="steps"/>
    <div class="p">
      <xsl:call-template name="commonattributes">
        <xsl:with-param name="default-output-class" select="'p'"/>
      </xsl:call-template>
      <xsl:call-template name="setidaname"/>
      <xsl:apply-templates select="." mode="add-step-importance-flag"/>
      <xsl:apply-templates/>
    </div>
    <!-- Process non-standard elements like oxy review after the step. -->
    <xsl:apply-templates select="following-sibling::*[1][not(contains(@class,' task/stepsection ') or contains(@class,' task/step '))]" mode="steps"/>
  </xsl:template>
  
  <!-- Created using template from: org.dita.html5/xsl/task.xsl -->
  <!-- Process task/steptroubleshooting as other task sub-elements -->
  <!-- Maybe to be removed if DITA-OT update includes this template -->
  <xsl:template match="*[contains(@class,' task/steptroubleshooting ')]" name="topic.task.steptroubleshooting">
    <xsl:call-template name="generateItemGroupTaskElement"/>
  </xsl:template>
  
</xsl:stylesheet>

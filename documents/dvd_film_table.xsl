<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"/>
   <xsl:template match="/">
	<html>
   	<!-- table -->
      <table border="1">
         <tr>
            <td>Title</td>
            <td>Director</td>
            <td>Year</td>
            <td>Type</td>
            <td>Actors</td>
         </tr>
         <!--display films-->
         <xsl:apply-templates select="//film" />
      </table>
</html>
   </xsl:template>

   <xsl:template match="film">
      <tr>
         <td><xsl:value-of select="title" /></td>
         <td><xsl:value-of select="director" /></td>
         <td><xsl:value-of select="year" /></td>
         <td><xsl:value-of select="type" /></td>
         <!-- apply the rules on actors -->
         <td><xsl:apply-templates select="actor" /></td>
      </tr>
   </xsl:template>

	<!-- display the actors-->
   <xsl:template match="actor">
      <li>
         <xsl:value-of select="lastName" />
         <xsl:value-of select="firstName" />
      </li>
   </xsl:template>
</xsl:stylesheet>


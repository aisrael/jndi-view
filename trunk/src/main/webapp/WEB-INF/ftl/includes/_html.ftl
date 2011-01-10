<#--
This FTL file defines macros to replace commonly used HTML tags.
-->

<#--
Replacement for the "html" tag, using XHTML5 syntax.
-->
<#macro xhtml5>
<#compress>
<!DOCTYPE html>
<?xml version="1.0" encoding="utf-8"?>
<html>
<#nested>
</html>
</#compress>
</#macro>

<#--
Replacement for the HTML "head" tag.
-->
<#macro head>
<head>
<#nested>
</head>
</#macro>

<#--
Replacement for the HTML "body" tag.
-->
<#macro body>
<body>
<#nested>
</body>
</#macro>

<#--
Write the HTML title tag. Use the global variable "pageTitle" if available, otherwise default to "Untitled".
-->
<#macro pageTitle><title>${.globals.pageTitle!"Untitled"}</title></#macro>

<@xhtml5>
<@head>
<title>JNDI: ${displayPath}</title>
</@head>
<@body>
<p>Browsing ${displayPath}:</p>
<dl>
<#list entries as entry>
<dt><#if entry.context><a href="${prefix}/${entry.link}"></#if>${entry.name}<#if entry.context></a></#if></dt>
<dd>${entry.className}<#if entry.targetClassName??> (${entry.targetClassName})</#if></dd>
</#list>
</dl>
</@body>
</@xhtml5>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Code</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<h1></h1>
<table>

    <tr> <td><span id=\"load_date\" >${code.date}</span></td></tr>
    <#if (code.views > 0) >
    <tr> <td><span id=\"views_restriction\" >${code.views} more views allowed</span></td></tr>
    </#if>
    <#if (code.views < 0) >
    <tr> <td><span id=\"views_restriction\" >0 more views allowed</span></td></tr>
    </#if>
    <#if (code.time > 0) >
    <tr> <td><span id=\"time_restriction\" >The code will be available for ${code.time} seconds</span></td></tr>
    </#if>
    <tr> <td><pre id=\"code_snippet\"><code>${code.code}</code></pre></td></tr>
</table>
</body>
</html>
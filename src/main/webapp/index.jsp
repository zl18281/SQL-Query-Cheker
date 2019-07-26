<%--
  Created by IntelliJ IDEA.
  User: fan
  Date: 19/07/19
  Time: 16:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <title>CodeMirror: Full Screen Editing</title>
  <meta charset="utf-8"/>
  <link rel=stylesheet href="doc/docs.css">

  <link rel="stylesheet" href="lib/codemirror.css">
  <link rel="stylesheet" href="addon/display/fullscreen.css">
  <link rel="stylesheet" href="theme/night.css">
  <link rel="stylesheet" href="addon/hint/show-hint.css">

  <script src="js/jquery.js"></script>
  <script src="lib/codemirror.js"></script>
  <script src="mode/sql/sql.js"></script>
  <script src="addon/display/fullscreen.js"></script>
  <script src="addon/hint/show-hint.js"></script>
  <script src="addon/hint/sql-hint.js"></script>
</head>
<div id=nav>
  <a href="https://codemirror.net"><h1>CodeMirror</h1><img id=logo src="doc/logo.png"></a>

  <ul>
    <li><a href="index.jsp">Home</a>
    <li><a href="doc/manual.html">Manual</a>
    <li><a href="https://github.com/codemirror/codemirror">Code</a>
  </ul>
  <ul>
    <li><a class=active href="#">MySQL Editor</a>
  </ul>
</div>

<article id="core">
  <h2>Editor</h2>
  <form><textarea id="code" name="code" rows="5"></textarea></form>
  <button onclick="compileOne()">ANTLR</button>
  <button onclick="compileTwo()">Packrat</button>
  <button onclick="tree()">Tree</button>
  <button onclick="save()">Save</button>
  <a href="syntax.html">See Syntax</a>
  <br><br><br>
  <div style="width: 300px;height: auto">
    <h2>Result</h2>
    <pre style="word-wrap:break-word;" id="result"></pre>
  </div>
  <div style="width: 300px;height: auto;">
    <p style="word-wrap:break-word;" id="error"></p>
  </div>
  <div style="width: 300px;height: auto;">
    <h2>Lisp Tree</h2>
    <p style="word-wrap:break-word;" id="tree"></p>
  </div>
  <!--
  <img id="tree" style="height: auto; width:auto; "src="img/tree.jpg"/>
  -->
</article>
<script>
  var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    lineNumbers: true,
    theme: "night",
    extraKeys: {
      "F11": function (cm) {
        cm.setOption("fullScreen", !cm.getOption("fullScreen"));
      },
      "Esc": function (cm) {
        if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
      }
    }
  });
</script>
<script src="js/antlr.js"></script>
<script src="js/tree.js"></script>
<script src="js/packrat.js"></script>

</html>
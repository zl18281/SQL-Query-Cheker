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
    <pre id="result"></pre>
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
<script>
  function compileOne() {
    var code = editor.getValue();
    console.log(code);
    $.ajax({
      type: "POST",
      url: "/SQL/sqlEditor",
      data: {code: code},
      async: false,
      success: function () {
        alert('success');
      },
      error: function () {
        alert('error');
      }
    });
    $.ajax({
      type: "GET",
      url: "/SQL/sqlEditor",
      success: function (data) {
        console.log(data);
        if (data == '' || data == null) {
          document.getElementById('result').innerHTML = '';
        } else {
          var errorInfo = JSON.parse(data);
          console.log(errorInfo);

          var space = '';
          var numOfSpaces = errorInfo['numOfSpaces'];
          for(let i = 0; i < numOfSpaces; i++) {
            space += ' ';
          }
          var arrow ='';
          var numOfArows = errorInfo['numOfArrows'];
          for(let j = 0; j < numOfArows; j++) {
            arrow += '^';
          }
          var errorInformation = '';

          let i = 0;
          while(errorInfo['msg'][i] != null) {
            errorInformation += errorInfo['msg'][i] + ' ';
            i++;
          }

          var errorLine = '';
          let j = 0;
          while(errorInfo['errorLine'][j] != null) {
            errorLine += errorInfo['errorLine'][j] + ' ';
            j++;
          }

          console.log(errorInfo['errorLine']);
          document.getElementById('result').innerHTML =
            'Stack: ' + '<br>' + errorInfo['stack'] + '<br><br>' +
            'Line: ' + '<br>' + errorInfo['line'] + '<br><br>' +
            'Char Position In Line' + '<br>' + errorInfo['charPositionInLine'] + '<br><br>' +
            'Offending Symbol' + '<br>' + errorInfo['offendingSymbol'] + '<br><br>' +
            'Error Message' + '<br>' + errorInformation + '<br><br>' +
            'Error Line' + '<br>' + errorLine + '<br>' +
            space + arrow;

        }
      }
    });
  }

</script>

<script>
  function compileTwo(){

  }
</script>

<script>
  function tree() {
    const Http = new XMLHttpRequest();
    const url = '/SQL/tree';
    Http.open("GET", url);
    Http.send();
    Http.onreadystatechange = (e) => {
      console.log(Http.responseText)
    }
  }

</script>

</html>
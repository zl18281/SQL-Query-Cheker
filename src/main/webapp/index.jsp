<%@ page import="java.net.URLDecoder" %><%--
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
  <script src="js/getDB.js"></script>
  <script src="js/getTable.js"></script>
  <script src="js/cursor.js"></script>
  <script src="js/execute.js"></script>
  <script src="js/showUser.js"></script>
  <link rel="stylesheet" href="css/toolTip.css">

  <style>
    table {
      border-collapse: collapse;
    }
    table, td, th {
      border: 1px solid black;
      padding: 5px;
      text-align: center;
    }
  </style>

</head>
<div style="margin-left: 100px; float: left; margin-right: 25px; margin-top: 100px">
  <fieldset>
    <legend>
      Login Menu
    </legend>
    <div id="loginSection">
      <form id="login" action="/SQL/session" method="post" onsubmit="showUser();">
        <br>Login<br>
        username: <input id="username" type="text" name="username" value="username" onfocus="value=''"/><br>
        password: <input id="password" type="password" name="password" value=""onfocus="value=''"/><br>
        <input type="submit" value="Login">
        <a href="/SQL" onclick="signOut();">Sign Out</a><br><br>
      </form>
    </div>
    <%
      String username="";
      Cookie[] cookieArr = request.getCookies();
      if(cookieArr != null && cookieArr.length != 0) {
        for(Cookie c:cookieArr){
          if(c.getName().equals("username")){
            username = URLDecoder.decode(c.getValue(), "utf-8");
          }
        }
      }
      out.print("User Name(refresh): " + "<p id=\"username\">" + username + "</p>");
    %>
  </fieldset>
  <br>
  <div>
    <h3>Instructions</h3>
    <ul>
      <li>Use Chrome Browser or the syntax tree will not display</li>
      <li>Refresh page after login and then username is visible</li>
      <li>First press "Init database", then "Init table"</li>
      <li>Press "ANTLR" to check SQL syntax, and "Tree" to show syntax tree</li>
      <li>Query is executed and displayed by pressing "Execute Query"</li>
      <li>Script can be saved with a filename</li>
    </ul>
  </div>
</div>


<div id="core" style="width: 500px;margin-left:30px;margin-top:100px;float: left;" >
  <fieldset>
    <legend>
      Editor
    </legend>
  <textarea id="code" class="textBox" name="code" rows="5" style="width: 500px"></textarea>
    <div class="tooltip" style="float: right" onmouseenter="insertCodeHint();">Code Hint (Hover)
      <div id="hint" class="tooltiptext">Tooltip text</div>
    </div>
  </fieldset>
  <br>
  <fieldset>
    <legend>
      Operation Panel
    </legend>
    <button onclick="getDB();">Init Database</button>
    <b>Databases</b>
    <select id="database">
    </select>
    <br>
    <button onclick="getTable();">Init Table</button>
    <b>Tables</b>
    <select id="table">
    </select><br><br>
    <button onclick="compileOne()">ANTLR</button>
    <button onclick="semantic()">Semantic</button>
    <button onclick="compileTwo()">Packrat</button>
    <button onclick="tree()">Tree</button>
    <a href="syntax.html">See Syntax Diagram</a>
    <br><br>
    <button onclick="result();">Execute Query</button>
    <br><br>
    <div>
      <input id="fileName" type="text" value="filename" onfocus="value=''"/>
      <button onclick="save()">Save</button>
    </div>
  </fieldset>


  <!--
  <img id="tree" style="height: auto; width:auto; "src="img/tree.jpg"/>
  -->
</div>

<div style="width: 600px;margin-left:40px;margin-top:100px;float: left;">
  <fieldset>
    <legend>
      Result Display
    </legend>
  <div style="width: 300px;height: auto">
    <h2>Result</h2>
    <pre style="word-wrap:break-word;" id="result"></pre>
    <pre style="word-wrap:break-word;" id="error"></pre>
    <pre style="word-wrap:break-word;" id="semantic"></pre>
  </div>

  <div style="width: 400px;height: auto;">
    <h2>View Query Result</h2>
    <div id="queryResult"></div>
  </div>
    </fieldset>
</div>

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
  editor.on("change", function(){
    setInterval(compileOne, 3000);
  });
  editor.on("change", function(){
    setInterval(semantic, 6000);
  });

</script>

<script src="js/antlr.js"></script>
<script src="js/tree.js"></script>
<script src="js/packrat.js"></script>
<script src="js/save.js"></script>
<script src="js/codeHint.js"></script>
<script src="js/semantic.js"></script>
<script>function save() {
  let file = new File([editor.getValue()],
    document.getElementById("fileName").value,
    {type: "text/plain;charset=utf-8"});
  saveAs(file);
}
</script>

</html>
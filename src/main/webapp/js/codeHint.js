function insertCodeHint() {
  let hint = generateHint();
  document.getElementById("hint").innerHTML = "None";
  document.getElementById("hint").appendChild(hint);

}

function generateHint() {
  let code = editor.getValue();

  var cursorPosition = editor.getCursor();

  console.log(cursorPosition);
  if (code.substr(cursorPosition['ch'] - 6, 6) == "SELECT") {
    let database = document.getElementById("database").value;
    let table = document.getElementById("table").value;
    let ul = document.createElement("ul");
    $.ajax({
      type: "POST",
      url: "/SQL/getColumn",
      data: {db: database, table: table},
      success: function (data) {
        console.log(data);
        let i = 1;
        while(data[i.toString()] != null ) {
          let li = document.createElement("li");
          li.innerText = data[i.toString()];
          ul.appendChild(li);
          i++;
        }
      },
      error: function () {
        alert('error');
      }
    });
    return ul;
  }
}



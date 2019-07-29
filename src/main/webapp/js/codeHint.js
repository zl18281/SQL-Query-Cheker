function insertCodeHint() {
  let hint = generateHint();
  if(hint == undefined || hint == null) {
    document.getElementById("hint").innerHTML = "None";
  }else {
    document.getElementById("hint").innerHTML = "";
    document.getElementById("hint").appendChild(hint);
  }
}


//This part is extensible, just add more "else if"s to get more hint functionality
function generateHint() {
  let code = editor.getValue();
  var cursorPosition = editor.getCursor();
  let offset = cursorLineOffset();

  console.log(cursorPosition);
  if ((code.substr(cursorPosition['ch'] - 6 + offset, 6) == "SELECT") ||
    (code.substr(cursorPosition['ch'] - 5 + offset, 5) == "WHERE")) {
    return columnHint ();
  }
  else if((code.substr(cursorPosition['ch'] - 4 + offset, 4) == "FROM") ||
    (code.substr(cursorPosition['ch'] - 4 + offset, 4) == "JOIN")) {
    return tableHint();
  }
  else if((code.substr(cursorPosition['ch'] - 1 + offset, 1) == ".") &&
    (code.substr(cursorPosition['ch'] - 1 + offset, 1) == ".")) {
    return dotColumnHint();
  }
}

function columnHint () {
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

function tableHint() {
  let database = document.getElementById("database").value;
  let ul = document.createElement("ul");
  $.ajax({
    type: "POST",
    url: "/SQL/getTable",
    data: {db: database},
    success: function(data) {
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

function dotColumnHint() {
  let database = document.getElementById("database").value;
  let table = getTableNameBeforeDot();
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

function getTableNameBeforeDot () {
  let code = editor.getValue();
  var cursorPosition = editor.getCursor();
  let offset = cursorLineOffset();

  let tempChar = '.';
  let start = cursorPosition['ch'] + offset - 2, end = cursorPosition['ch'] + offset - 1;
  while(tempChar != ' ') {
    tempChar = code.charAt(start);
    start--;
    console.log(start);
  }
  return code.substring(start + 1, end);
}

function cursorLineOffset () {
  let code = editor.getValue();
  var cursorPosition = editor.getCursor();
  var codeInArray = code.split('\n');
  let offset = 0;

  for(let i = 0; i < cursorPosition['line']; i++) {
    offset += (codeInArray[i].length + 1);
    console.log(offset);
  }
  return offset;
}

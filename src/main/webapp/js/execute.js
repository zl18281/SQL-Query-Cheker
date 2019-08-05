function result() {
  var d = {};
  let database = document.getElementById("database").value;
  d["db"] = database;
  let code = editor.getValue().split(";");
  for(let i = 0; i < code.length - 1; i++) {
    code[i] += ";";
    d[i] = code[i];
  }
  console.log(JSON.stringify(d));
  $.ajax({
    type: "POST",
    url: "/SQL/result",
    data: {codeArr: JSON.stringify(d)},
    success: function (data) {
      console.log(data);
      dataToTable(data);
      alert('success');
    },
    error: function () {
      alert('error');
    }
  });
}

function dataToTable (data) {
  var t = document.getElementById("queryResult")
  t.innerHTML = "";
  let tablesInJson = data;
  let tables = [];
  let numOfTables = 1;
    while(numOfTables <= Object.keys(tablesInJson).length) {
      tables.push(tablesInJson[numOfTables.toString()]);
      numOfTables++;
    }
  for(let i = 0; i < numOfTables - 1; i++) {
    let htmlTable = document.createElement("table");
    let rows = [];
    let numOfRows = 1;
    while(numOfRows <= Object.keys(tables[i]).length) {
      rows.push(tables[i][numOfRows.toString()]);
      numOfRows++;
    }
    console.log(rows);
    for(let j = 0; j < numOfRows - 1; j++) {
      let htmlRow = document.createElement("tr");
      let cells = [];
      let numOfCells = 1;
      console.log(rows[j]);
      while(numOfCells <= Object.keys(rows[j]).length) {
        cells.push(rows[j][numOfCells.toString()]);
        numOfCells++;
      }
      for(let k = 0; k < numOfCells - 1; k++) {
        let htmlCell = document.createElement("td");
        htmlCell.innerText = cells[k];
        htmlRow.appendChild(htmlCell);
      }
      htmlTable.appendChild(htmlRow);
    }
    document.getElementById("queryResult").appendChild(htmlTable);
  }
}
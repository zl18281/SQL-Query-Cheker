function result() {
  var data = {};
  let database = document.getElementById("database").value;
  data["db"] = database;
  let code = editor.getValue().split(";");
  for(let i = 0; i < code.length - 1; i++) {
    code[i] += ";";
    data[i] = code[i];
  }
  console.log(JSON.stringify(data));
  $.ajax({
    type: "POST",
    url: "/SQL/result",
    data: {codeArr: JSON.stringify(data)},
    success: function (d) {
      console.log(d);
      alert('success');
    },
    error: function () {
      alert('error');
    }
  });
}
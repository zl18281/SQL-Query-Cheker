function getTable () {
  let database = document.getElementById("database").value;
  console.log(database);
  $.ajax({
    type: "POST",
    url: "/SQL/getTable",
    data: {db: database},
    success: function(data) {
      document.getElementById("table").innerHTML = "";
      console.log(data);
      let i = 1;
      while(data[i.toString()] != null) {
        let newNode = document.createElement('option');
        newNode.value = data[(i).toString()];
        newNode.innerText = data[(i).toString()];
        document.getElementById("table").appendChild(newNode);
        i++;
      }
    },
    error: function () {
      alert('error');
    }
  });
}
function getDB () {
  $.ajax({
    type: "GET",
    url: "/SQL/getDB",
    dataType:"json",
    success: function(data) {
      document.getElementById("database").innerHTML = "";
      console.log(data);
      let i = 1;
      while(data[i.toString()] != null) {
        let newNode = document.createElement('option');
        newNode.value = data[(i).toString()];
        newNode.innerText = data[(i).toString()];
        document.getElementById("database").appendChild(newNode);
        i++;
      }
    },
    error: function () {
      alert('error');
    }
  });
}


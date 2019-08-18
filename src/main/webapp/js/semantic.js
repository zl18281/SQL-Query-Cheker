function semantic() {
  $.ajax({
    type: "GET",
    url: "/SQL/semantic",
    dataType:"json",
    success: function(data) {
      document.getElementById("semantic").innerText="";
      console.log(data);
      let s = "";
      let i = 1;
      while(data[i.toString()] != null) {
        s += data[i.toString()] + ", ";
        i++;
      }
      s = s.substring(0, s.length - 2);
      console.log(s);
      document.getElementById("semantic").innerText = "Error Columns: " + s;
    },
    error: function () {
      alert('error');
    }
  });
}
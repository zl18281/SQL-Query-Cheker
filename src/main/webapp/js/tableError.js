function tableError() {
  $.ajax({
    type: "GET",
    url: "/SQL/table",
    dataType:"json",
    success: function(data) {
      document.getElementById("tableError").innerText="";
      console.log(data);
      let s = "";
      let i = 1;
      while(data[i.toString()] != null) {
        s += data[i.toString()] + ", ";
        i++;
      }
      s = s.substring(0, s.length - 2);
      console.log(s);
      document.getElementById("tableError").innerText = "Error Tables: " + s;
    },
    error: function () {
      alert('error');
    }
  });
}
function tree() {
  $.ajax({
    type: "GET",
    url: "/SQL/tree",
    success: function (data) {
      console.log(data);
      document.getElementById('tree').innerText = data;
    },
    error: function () {
      alert('error');
    }
  });
}
function tree() {
  $.ajax({
    type: "GET",
    url: "/SQL/tree",
    success: function (data) {
      console.log(data);
      window.open('syntaxTree.html');
    },
    error: function () {
      alert('error');
    }
  });
}
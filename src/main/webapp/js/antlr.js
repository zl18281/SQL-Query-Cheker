function compileOne() {
  var code = editor.getValue();
  let database = document.getElementById("database").value;
  console.log(code);
  $.ajax({
    type: "POST",
    url: "/SQL/sqlEditor",
    data: {code: code, db:database},
    async: false,
    success: function () {
    },
    error: function () {
      alert('error');
    }
  });
  $.ajax({
    type: "GET",
    url: "/SQL/sqlEditor",
    success: function (data) {
      console.log(data);
      if (data == '' || data == null) {
        document.getElementById('result').innerHTML = '';
        document.getElementById('error').innerHTML = '';
      } else {
        let errorInfo = JSON.parse(data);
        console.log(errorInfo);

        let space = '';
        let numOfSpaces = errorInfo['numOfSpaces'];
        for(let i = 0; i < numOfSpaces; i++) {
          space += '&nbsp';
        }
        let arrow ='';
        let numOfArows = errorInfo['numOfArrows'];
        for(let j = 0; j < numOfArows; j++) {
          arrow += '^';
        }

        let errorInformation = '';

        let k = 0;
        while(errorInfo['msg'][k] != null) {
          errorInformation += (errorInfo['msg'][k] + '&nbsp');
          k++;
        }

        let errorLine = '';
        let s = 0;
        while(errorInfo['errorLine'][s] != null) {
          errorLine += errorInfo['errorLine'][s] + '&nbsp';
          s++;
        }

        console.log(errorInfo['errorLine']);
        document.getElementById('result').innerHTML =
          'Line: ' + '<br>' + errorInfo['line'] + '<br><br>' +
          'Char Position In Line' + '<br>' + errorInfo['charPositionInLine'] + '<br><br>' +
          'Offending Symbol' + '<br>' + errorInfo['offendingSymbol'] + '<br><br>' +
          'Error Line' + '<br>' + errorLine + '<br>' +
          space + arrow;
        document.getElementById('error').innerHTML = 'Error Message' + '<br>' + errorInformation + '<br><br>';

      }
    }
  });
}
$(document).ready(function(){
  var getCompletion = function(){
    var input = $("#input").val();

    $.getJSON("http://localhost:4567/autocomplete/" + input, function(json) {
      console.log(json);
      result = "";
      counter = 0;
      for(completion of json["completions"]) {
        counter++;
        result += counter + ". " + completion["completion"] + "<br>"
      }
      $("#results").html(result);
    });
  };

  $("#autocomplete").click(getCompletion);
});

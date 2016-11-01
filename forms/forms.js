$(function(){
  $("#transGitBtn").click(function(){
    var file_data = $("#fileInput").prop("files")[0];
    var original_input = $("#originalInput").text();
    var form_data = new FormData();
    form_data.append("fileInput", file_data);
    form_data.append("originalInput", original_input)
    $.ajax({
                url: "/forms/upload.php",
                dataType: 'script',
                cache: false,
                contentType: false,
                processData: false,
                data: form_data,
                type: 'post',
                success: function(data){
                  alert(data);
                },
                error: function() {
                  alert("error");
                }
     });
  });
})

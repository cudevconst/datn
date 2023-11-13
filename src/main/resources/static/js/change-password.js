$(document).ready(function(){
  $("#change-info").click(function(){
    var oldPassword  = $("#currentPassword").val()
    var newPassword = $("#newPassword").val()
    var confirmPassword  = $("#confirmPassword").val()
    var data = new FormData()
    data.append("oldPassword", oldPassword)
    data.append("newPassword", newPassword)
    data.append("confirmPassword", confirmPassword)
    $.ajax({
        type: "POST",
        url: "/public/change-password",
        data: data,
        processData: false,
         contentType: false,
         success: function(response){
            $("#error").html('')
            $("#success").html(`<div class="alert alert-success alert-dismissible fade show" role="alert">Thay đổi mật khẩu thành công</div>`)
         },
         error: function(xhr, textStatus, errorThrown){
            if(xhr.status === 400){
                var errorResponse = JSON.parse(xhr.responseText)
                console.log(errorResponse)
                var html = `<div class="alert alert-danger alert-dismissible fade show" role="alert" >${errorResponse.message}</div>`
                $("#success").html('')
                $("#error").html(html)
            }

         }
    })
  })
})
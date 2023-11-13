$(document).ready(function () {
    $("#addButton").click(function () {
        $("#addNewSection").show();
        $("#addButton").hide();
        $("#cancelButton").show();
        $("#inputField").val(""); // Clear the input field
    });

    $("#cancelButton").click(function () {
        $("#addNewSection").hide();
        $("#addButton").show();
        $("#cancelButton").hide();
    });

    $('button[id^="deleteButton_"]').click(function(){
        var typeId = this.id.split('_')[1];
        if(confirm("Bạn có chắc chắn muốn xoá loại sản phẩm?")){
            $.ajax({
                type: "POST",
                url: "/admin/type/delete/" + typeId, // Thay đổi URL theo cấu trúc của bạn
                success: function(data) {

                alert("Loại đã được xoá thành công.");
                location.reload()
                },
                error: function() {
                                    // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi
                alert("Có lỗi xảy ra khi xoá loại.");
                }
                });
        }
    })
});

function showEditRow(button){
    var editRow = $(button).closest('tr').next('.edit-row');

        // Toggle the visibility of the edit row
        editRow.toggle();
}

function cancelEditRow(button) {
    // Find the edit row (parent of the clicked button)
    var editRow = $(button).closest('.edit-row');

    // Hide the edit row
    editRow.hide();
}
 function saveUpdateType(button){
    var typeId = button.getAttribute("data-type-id");
    var inputField = $(button).closest('tr').find('input[type="text"]');
    var updateTypeInput = inputField.val();
    if(confirm("Bạn có chắc chắn muốn cập nhật loại sản phẩm?")){
                $.ajax({
                    type: "POST",
                    url: "/admin/type/update/" + typeId + "/" + updateTypeInput, // Thay đổi URL theo cấu trúc của bạn
                    success: function(data) {

                    alert("Loại đã được cập nhật thành công.");
                    location.reload()
                    },
                    error: function() {
                                        // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi
                    alert("Có lỗi xảy ra khi cập nhật loại.");
                    }
                    });
            }
 }

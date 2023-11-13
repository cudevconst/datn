

$(document).ready(function(){
    var currentPath = window.location.pathname;
    var slug = currentPath.split('/').pop();
    getProduceDetail(slug)

    $("#submitBtn").click(async function(){
            $("#spinnerLoading").show();
            var imageUrl = await uploadFile()
            await uploadProduct(imageUrl, slug)
        })
    loadType()
})
function getProduceDetail(slug){
    $.ajax({
        type: "GET",
        url: "/api/product/detail/" + slug,
        success: function(data){
        console.log(data)
            renderData(data)
        },
        error: function(error){
            alert("Error")
        }
    })
}

function renderData(data){
    $("#nameInput").val(data.name);
    $("#priceInput").val(data.price)
    $("#colorInput").val(data.colors)
    $("#sizeInput").val(data.sizes)
    $("#srcImage1").attr("src", data.image1)
    $("#srcImage2").attr("src", data.image2)
    $("#srcImage3").attr("src", data.image3)
}

function loadType(){
    var types = $("#listTypes")
    $.ajax({
        type: "GET",
        url: "/admin/type/all",
        success: function(response){
            var listTypeHtml =''
            response.forEach(function(element){
                listTypeHtml += '<div class="form-check">'
                listTypeHtml += '<input type="checkbox" class="form-check-input" id="option' + element.id + '" name="selectedOptions" value="' + element.id +'">'
                listTypeHtml += '<label class="form-check-label" for="option' + element.id + '">' + element.nameType+'</label>'
                listTypeHtml += '</div>'
            })
            console.log(listTypeHtml)
        $("#listTypes").append(listTypeHtml)

        },
        error: function(error){
            alert(error)
        }
    })
}

function selectType(){
        var selectedValues = [];

        // Sử dụng phương thức each để lặp qua tất cả các checkbox đã chọn
        $('input[name="selectedOptions"]:checked').each(function() {
            selectedValues.push($(this).val());
        });

        // Hiển thị giá trị của các checkbox đã chọn
        return selectedValues.join(", ")
}

function uploadFile(){
    return new Promise(function(resolve, reject){
        var image1 = $("#image1Input")[0].files[0];
                var image2 = $("#image2Input")[0].files[0];
                var image3 = $("#image3Input")[0].files[0];
                if(image1 && image2 && image3){
                    var formData = new FormData();
                    formData.append("image1", image1)
                    formData.append("image2", image2)
                    formData.append("image3", image3)
                        $.ajax({
                        url: "/api/upload", // Điều chỉnh URL máy chủ của bạn
                        type: "POST",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function (response) {
                            resolve(response)
                        },
                        error: function(error){
                            $("#errorMessage").show()
                            reject(error)
                        }

                    });
                }
    })
}

function uploadProduct(imageUrl, slug){
   return new Promise(function(resolve, reject){
    var formData = new FormData();
    var name  = $("#nameInput").val();
    var image1 = imageUrl.image1;
    var image2 = imageUrl.image2;
    var image3 = imageUrl.image3;
    var price = $("#priceInput").val();
    var colors = $("#colorInput").val();
    var sizes = $("#sizeInput").val();

    formData.append("name", name);
    formData.append("price", price);
    formData.append("image1", image1);
    formData.append("image2", image2);
    formData.append("image3", image3);
    formData.append("sizes", sizes);
    formData.append("colors", colors);
    formData.append("ids", selectType())

    $.ajax({
        type: "POST",
        url: "/api/product/update/" + slug,
        data: formData,
        processData: false,
        contentType: false,
        success: function(response){
            console.log("Upload product success")
            $("#spinnerLoading").hide();
            $("#successMessage").show();
            resolve(resolve)
        },
        error: function(error){
            alert("fail")
            $("#errorMessage").show()
            reject(error)
        }
    })

   })
}
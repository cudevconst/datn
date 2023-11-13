$(document).ready(function(){
    console.log(1)
    getAllProduct()

})
function deleteRow(button){
    var slug = $(button).attr("data-slug")
    if(confirm("Bạn có chắc chắn muốn xoá sản phẩm?")){
        deleteProduct(slug)
    }
}

function deleteProduct(slug){
    $.ajax({
        type: "POST",
        url: "/api/product/delete/" + slug,
        processData: false,
        contentType: false,
        success: function(data){
            alert("Xoá vật phẩm thành công")
            location.reload()
        },
        error: function(err){
            alert("Lỗi xoá sản phẩm")
        }
    })
}
function getAllProduct(){
        $.ajax({
                type: "GET",
                url: "/api/product/all",
                success: function(data){
                    var counter = 1
                    data.forEach(function(product){

                    var productHtml = '<tr>'
                    var listTypes = ''
                    product.types.forEach(function(type){
                        listTypes += type.nameType
                        listTypes += ","
                    })
                    productHtml += '<th scope="row" class="align-middle text-center" >' + counter  + '</th>'
                    productHtml +='<td class="align-middle font-weight-bold text-center" >'
                    productHtml +='<a href="">'
                    productHtml +='<img src="'+ product.image1+'" alt="" class="w-50">'
                    productHtml +='</a>'
                    productHtml +='</td>'
                    productHtml +='<td class="align-middle text-center">' + product.name+'</td>'
                    productHtml +='<td class="align-middle text-center">' + listTypes+'</td>'
                    productHtml +='<td class="align-middle text-center">'+product.colors+'</td>'
                    productHtml +='<td class="align-middle text-center">'+ product.sizes+'</td>'
                    productHtml +='<td class="align-middle text-center">'
                    productHtml +='<div>'
                    productHtml +='<button class="btn btn-danger m-1" onclick="deleteRow(this)" data-slug="' + product.slug+'">Xoá</button>'
                    productHtml += '<a href="/product/update/'+product.slug+'">'
                    productHtml +='<button class="btn btn-success m-1">Thay đổi</button>'
                    productHtml += '</a>'
                    productHtml +='</div>'
                    productHtml +='</td>'
                    productHtml +='</tr>'

                    $("#listProduct").append(productHtml)
                    })
                },
                error: function(error){
                console.log("fail")}

            })
}


$(document).ready(async function(){
    await renderCart();
    $("button[data-action").click(function(){
        var action = $(this).data("action")
        var input = $(this).closest(".input-group").find("input");
        if (action === "reduce") {
            if (input.val() > 1) {
                input.val(parseInt(input.val()) - 1);
            }
        } else if (action === "increase") {
            input.val(parseInt(input.val()) + 1);
        }
    })
    $(".button-order").on('click', function(){
        var button = $(this)
        var orders = []
            var row = button.closest('tr')
            var quantity = row.find("#quantityInput").val()
            var color = row.find(".color-order").text()
            var size = row.find(".size-order").text()
            var productId = $(button).data("product")
            data = {
                color: color,
                size: size,
                quantity: quantity,
                productId: productId
            }
            orders.push(data)
            sessionStorage.setItem("orderProduct", JSON.stringify(orders))
            window.location.href="/order"
    })

})

function renderCart(){
    return new Promise(function(resolve, reject){

        $.ajax({
            type: "GET",
            url: "/api/cart/user",
            success: function(response){
                var html = ''
                response.forEach(function(data){
                    console.log(data)
                    var sumCost = data.productResponse.price * data.quantity
                    html += `<tr>
                    <th scope="row">
                    <img src="${data.productResponse.image1}" alt="" height="100">
                    </th>
                    <td class="align-middle font-weight-bold">${data.productResponse.name}</td>
                    <td class="align-middle font-weight-bold text-danger">${data.productResponse.price}đ</td>
                    <td class="align-middle text-dark color-order">${data.color}</td>
                    <td class="align-middle text-dark size-order">${data.size}</td>
                    <td class="align-middle">
                    <div class="input-group">
                    <button class="btn btn-light font-weight-bold" style="width: 50px;" data-action="reduce">-</button>
                    <input type="text" name="quantityInput" id="quantityInput" class="pb-2 pt-1 text-center border-0" value="${data.quantity}" style="width: 70px;">
                    <button class="btn btn-light font-weight-bold" style="width: 50px;" data-action="increase">+</button>
                    </div>
                    </td>
                    <td class="align-middle font-weight-bold text-danger">
                    ${sumCost}đ
                    </td>
                    <td class="align-middle ">
                    <div>
                    <button class="btn btn-danger m-1" data-product="${data.id}" onclick="deleteProduct(this)">Xoá</button>
                    <button class="btn btn-primary m-1 button-order" data-product="${data.productResponse.id}">Mua ngay</button>
                    </div>
                    </td>
                    </tr>`
                })
                $("#listCart").append(html)
                resolve()
            },
            error: function(error){
                console.error(error);
                reject(error);
            }
        })
    })
}
//function orderProduct(button){
//    console.log(1)
//    var orders = []
//    var row = $(button).closest('tr')
//    var quantity = row.find("#quantityInput").val()
//    var color = row.find(".color-order").text()
//    var size = row.find(".size-order").text()
//    var productId = $(button).data("product")
//    data = {
//        color: color,
//        size: size,
//        quantity: quantity,
//        productId: productId
//    }
//    orders.push(data)
//    sessionStorage.setItem("orderProduct", JSON.stringify(orders))
//    window.location.href="/order"
//}
function deleteProduct(button){
    var productId = $(button).data("product")
    if(confirm("Bạn chắc chắn muốn xoá sản phẩm khỏi giỏ hàng")){
        $.ajax({
            type: "DELETE",
            url: "/api/cart/delete/" + productId,
            success: function(response){
                alert("Xoá vật phẩm thành công")
                location.reload()
            },
            error: function(error){
                alert("Có lỗi xảy ra")
            }
        })
    }
}
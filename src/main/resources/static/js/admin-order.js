$(document).ready(async function(){
    await dataInfoOrderByStatus($('#statusOrder').find(":selected").val())
    $("#statusOrder").on('change', async function(){
        var status = $('#statusOrder').find(":selected").val()
        dataInfoOrderByStatus(status)
    })

})

function dataInfoOrderByStatus(status){
    return new Promise(function(resolve, reject){
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/order/admin/find?status=" + status,
            success: function(response){
                var html = ''
                var htmlInPU =''
              response.forEach(function(data){
                html += `<tr>
                <div data-toggle="modal" data-target="#exampleModalCenter${data.id}">
                <td class="align-middle">${data.phoneNumber}</td>
                <td class="align-middle ">${data.city}</td>
                <td class="align-middle text-dark">${data.district}</td>
                <td class="align-middle text-dark">${data.wards}</td>
                <td class="align-middle">
                ${data.addressInfo}
                </td>

                <td class="align-middle font-weight-bold text-danger">
                ${data.createAt}
                </td>
                </div>
                <td class="align-middle ">
                    <div>
                        <button class="btn btn-danger m-1" onclick="cancelOrder(this)" data-product="${data.id}">Huỷ</button>
                        <button class="btn btn-primary m-1" onclick="acceptOrder(this)" data-product="${data.id}">Xác nhận</button>
                    </div>
                </td>
              </tr>`

                htmlInPU += `<div class="modal fade" id="exampleModalCenter${data.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle${data.id}" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLongTitle${data.id}">Modal title</h5>
                      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                      </button>
                    </div>
                    <div class="modal-body">
                        ${renderDataInPopUp(data, data.product)}
                    </div>
                    <div class="modal-footer">
                      <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      <button type="button" class="btn btn-primary">Save changes</button>
                    </div>
                  </div>
                </div>
              </div>`

              })
              $("#listInfoOrder").html(html)
              $("#detailPopup").html(htmlInPU)
                resolve()
            },
            error: function(error){
                console.error(error)
                reject()
            }
        })
    })
}

function renderDataInPopUp(response, data){
    var html = ''
    data.forEach(function(order){
        html += `<div class="d-flex border-2 border-bottom border-ligh p-4">

        <div class="ml-4 d-flex flex-column text-left">
            <div>
                <span class="font-weight-bold">Tên sản phẩm:</span>
                <span>${order.product.name}</span>
            </div>

            <div>
                <span class="font-weight-bold">Màu sắc:</span>
                <span>${order.color}</span>
            </div>
            <div>
                <span class="font-weight-bold">Kích thước:</span>
                <span>${order.size}</span>
            </div>
            <div>
                <span class="font-weight-bold">Đơn giá:</span>
                <span>${order.product.price}</span>
            </div>
            <div>
                <span class="font-weight-bold">Số lượng:</span>
                <span>${order.quantity}</span>
            </div>
            <div>
                <span class="font-weight-bold">Ngày đặt hàng:</span>
                <span>${response.createAt}</span>
            </div>
            <div>
                <span class="font-weight-bold">Thành phố/Tỉnh:</span>
                <span>${response.city}</span>
            </div>

            <div>
                <span class="font-weight-bold">Quận/Huyện:</span>
                <span>${response.district}</span>
            </div>

            <div>
                <span class="font-weight-bold">Phường/Xã:</span>
                <span>${response.wards}</span>
            </div>
            <div>
            <span class="font-weight-bold">Địa chỉ chi tiết:</span>
            <span>${response.addressInfo}</span>
        </div>
            <div>
                <span class="font-weight-bold">Số điện thoại:</span>
                <span>${response.phoneNumber}</span>
            </div>
        </div>
    </div>`
    })
    return html
}

function cancelOrder(button){
    var orderId = $(button).data("product")
    if(confirm("Bạn chắc chắn huỷ order này?")){
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/order/update?orderId=" + orderId +"&status=0",
            success: function(response){
                console.log(response)
            },
            error: function(error){
                console.log(error)
            }
        })
    }
}

function acceptOrder(button){
   var orderId = $(button).data("product")
   if(confirm("Xác nhận order")){
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/order/update?orderId=" + orderId +"&status=2",
        success: function(response){
            console.log(response)
        },
        error: function(error){
            console.log(error)
        }
    })

   }
}
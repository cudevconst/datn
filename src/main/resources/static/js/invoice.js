$(document).ready(async function(){
    var response = await getInvoice()
    renderInvoiceDetail(response)
    renderInvoice(response)
    renderDetail(response)
})

function getInvoice(){
    return new Promise(function(resolve, reject){
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/invoice/user",
            success: function(response){

                console.log(response)
                resolve(response)
            },
            error: function(error){
                console.error(error)
            }
        })
    })
}

function renderInvoice(responses){

    responses.forEach(function(response){
        console.log(response)
        var orderProduct = response.order.product
        // console.log(orderProduct[0])
        orderProduct.forEach(function(order){
            var html = `<div class="d-flex border-2 border-bottom border-ligh p-4">
            <div class="text-center">
            <img src="${order.product.image1}" alt="" height="150">
            </div>
            <div class="ml-4 ">
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
                <span>199.999đ</span>
            </div>
            <div>
                <span class="font-weight-bold">Số lượng:</span>
                <span>${order.quantity}</span>
            </div>
            <div>
                <span class="font-weight-bold">Ngày đặt hàng:</span>
                <span>${convertDate(response.order.createAt)}</span>
            </div>
            </div>
        </div>`
        // $("#listProductOrder").append(html)
        })

    })

}

function renderInvoiceDetail(responses){
    responses.forEach(function(response){
        var status = ''
        if(response.status === '1'){
            status = `<td class="align-middle text-center text-success">Đã thanh toán</td>`
        }
        else{
            status = `<td class="align-middle text-center text-danger">Chưa thanh toán</td>`

        }
        var statusOrder = ''
        if(response.order.status === '1'){
            statusOrder = `<td class="align-middle text-center text-danger">Đang xử lý</td>`
        }
        else if(response.order.status === '0'){
                    statusOrder = `<td class="align-middle text-center text-danger">Đã huỷ</td>`
                }
        else if(response.order.status === '2'){
            statusOrder = `<td class="align-middle text-center text-warning">Đã xác nhận</td>`
        }
        else if(response.order.status === '3'){
            statusOrder = `<td class="align-middle text-center text-success">Đã hoàn thành</td>`
        }
        var html = `<tr data-toggle="modal" data-target="#exampleModalCenter${response.id}">
        <th scope="row">
            <img src="${response.order.product[0].product.image1}" alt="" height="100">
        </th>
        <td class="align-middle text-center font-weight-bold">${response.order.product[0].product.name}</td>
        <td class="align-middle font-weight-bold text-danger text-center">
            ${response.amout + response.transaction - response.coupon}
        </td>
            ${statusOrder}
            ${status}
        </tr>`

        $("#listInvoice").append(html)
    })


}
function renderDetailInPopup(response){
    var orderProduct = response.order.product
    var html = ''
    orderProduct.forEach(function(order){
        html += `<div class="d-flex border-2 border-bottom border-ligh p-4">
        <div class="">
            <img src="${order.product.image1}" alt="" height="150">
        </div>
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
                <span>${convertDate(response.order.createAt)}</span>
            </div>
        </div>
    </div>`
    })
    return html;
}
function renderDetail(responses){
    responses.forEach(function(response){
        var html = `<div class="modal fade" id="exampleModalCenter${response.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title text-center" id="exampleModalLongTitle">Chi tiết đặt hàng</h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              ${renderDetailInPopup(response)}

            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Mua lại</button>
            </div>
          </div>
        </div>
        </div>`
        $("#detailPopup").append(html)
    })
}
function convertDate(originalTimeString){
    const originalDate = new Date(originalTimeString);


    const hours = originalDate.getHours();
    const minutes = originalDate.getMinutes();
    const seconds = originalDate.getSeconds();
    const day = originalDate.getDate();
    const month = originalDate.getMonth() + 1;
    const year = originalDate.getFullYear();

    const formattedTimeString = `${hours}:${minutes}:${seconds} ${day}-${month}-${year}`;

    return formattedTimeString
}
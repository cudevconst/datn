function getCity(){
    return new Promise(function(resolve, reject){
        $.ajax({
            type:"GET",
            url:"https://online-gateway.ghn.vn/shiip/public-api/master-data/province",
            headers: {
                'token': '706eeea2-7b1d-11ee-96dc-de6f804954c9'
            },
            dataType: "json",
            success: function(data){
                var html = ""
                $.each(data.data, function(index, item){
                    html += '<option value="' + item.ProvinceID + '">' + item.ProvinceName + '</option>';
                })
                $("#city").html(html)
                resolve()
            },
            error: function(){
                console.log("Error")
                reject()
            }
        })
    })
}

function getDistrictByProvinceCode(provinceCode){
    return new Promise(function(resolve, reject){
        $.ajax({
            url:"https://online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=" + provinceCode,
            type: "GET",
            headers: {
                'token': '706eeea2-7b1d-11ee-96dc-de6f804954c9'
            },
            dataType: "json",
            success: function(data){
                console.log(data)
                var html = ""
                $.each(data.data, function(index, item){
                    html += '<option value="' + item.DistrictID + '">' + item.DistrictName + '</option>';
                })
                $("#district").html(html)
                resolve()
            },
            error: function(){
                console.log("Error")
                reject()
            }
        })
    })
}
function getWardsByDistrictsCode(districtCode){
    return new Promise(function(resolve, reject){
        $.ajax({
            url:"https://online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + districtCode,
            type: "GET",
            headers: {
                'token': '706eeea2-7b1d-11ee-96dc-de6f804954c9'
            },
            dataType: "json",
            success: function(data){
                console.log(data)
                var html = ""
                $.each(data.data, function(index, item){
                    html += '<option value="' + item.WardCode + '">' + item.WardName + '</option>';
                })
                $("#ward").html(html)
                resolve()
            },
            error: function(){
                console.log("Error")
                reject()
            }
        })
    })
}
$(document).ready(async function(){
    await getCity()

    var selectValueCity = $("#city").val()
    await getDistrictByProvinceCode(selectValueCity)

    var selectValueDistrict = $("#district").val()

    await getWardsByDistrictsCode(selectValueDistrict)
    selectValueWard = $("#ward option:selected").val()
    var serviceId = await getServiceTransction(1542, selectValueDistrict, 4675858)
    await getFeeTransaction(serviceId, 500000, selectValueDistrict, selectValueWard);
    console.log(1)
    $("#city").on("change", async function(){
        selectValueCity = $(this).val();
        console.log(selectValueCity)
        await getDistrictByProvinceCode(selectValueCity)
        selectValueDistrict = $("#district").val()
        await getWardsByDistrictsCode(selectValueDistrict)
        selectValueWard = $("#ward option:selected").val()
        var serviceId = await getServiceTransction(1542, selectValueDistrict, 4675858)
        await getFeeTransaction(serviceId, 500000, selectValueDistrict, selectValueWard);
        costAmout()
    })
    $("#district").on("change", async function(){
        var selectValueDistrict = $(this).val();
        console.log(selectValueDistrict)
        await getWardsByDistrictsCode(selectValueDistrict)
        selectValueWard = $("#ward option:selected").val()
        var serviceId = await getServiceTransction(1542, selectValueDistrict, 4675858)
        await getFeeTransaction(serviceId, 500000, selectValueDistrict, selectValueWard);
        costAmout()
    })

    $("#ward").on("change", async function(){
        selectValueCity = $("#city option:selected").val()
        selectValueDistrict = $("#district option:selected").val()
        selectValueWard = $("#ward option:selected").val()
        var serviceId = await getServiceTransction(1542, selectValueDistrict, 4675858)
        console.log(selectValueCity, selectValueDistrict, selectValueWard)
        await getFeeTransaction(serviceId, 500000, selectValueDistrict, selectValueWard);
        costAmout()
    })

    toggleCheckbox()
    $("#click").click(async function(){
        selectValueCity = $("#city option:selected").val()
        selectValueDistrict = $("#district option:selected").val()
        selectValueWard = $("#ward option:selected").val()
        var fee  = $("#feeTransaction").text()

        var checkValue = $('input[type="checkbox"]:checked').val()
        var url = ''
        var data = {}
        console.log(checkValue)
        var orderProduct = sessionStorage.getItem("orderProduct")
        if(checkValue === 'payment-live'){
            url = "/api/order/create";

            data =
            {
                transaction: Number(fee),
                coupon: 0,
                status: "0",
                type: "Offline",
                order: {
                    orderProduct: JSON.parse(orderProduct),
                    city: $("#city option:selected").text(),
                    district:$("#district option:selected").text(),
                    wards: $("#ward option:selected").text(),
                    addressInfo: $("#addressInfoInput").val(),
                    phoneNumber: $("#sdtInput").val()
                }
            }

        }
        else{
            url = "/api/payment/create"
            data =
                        {
                            transaction: Number(fee),
                            coupon: 0,
                            status: "1",
                            type: "VNPay",
                            order: {
                                orderProduct: JSON.parse(orderProduct),
                                city: $("#city option:selected").text(),
                                district:$("#district option:selected").text(),
                                wards: $("#ward option:selected").text(),
                                addressInfo: $("#addressInfoInput").val(),
                                phoneNumber: $("#sdtInput").val()
                            }

                        }
            locationHref = "/"
        }

        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(data),
            processData: "json",
            contentType: "application/json; charset=utf-8",
            success: function(response){
                  if(checkValue === "payment-live"){
                    window.location.href = "/history/order"
                  }
                  else{
                    window.location.href = response
                  }
            },
            error: function(error){
                  console.error(error)
            }
        })

    })
    // renderProductSelect();
    renderProductOrder()
})

function renderProductOrder(){
ordersList = JSON.parse(sessionStorage.getItem("orderProduct"))
    var ids = ''
    ordersList.forEach(function(orderProduct, index){

        if (index < ordersList.length - 1) {
            ids += orderProduct.productId + ",";
        } else {
            ids += orderProduct.productId;
        }
    })
    console.log(ids)
    var productOrderHtml = ''
    var amout = 0
    $.ajax({
        type: "GET",
        url: "/api/product/productId?ids=" + ids,
        success: function(products){
            products.forEach(function(product, index){
                                var amoutDetail = product.price * ordersList[index].quantity
                                amout += amoutDetail
                                productOrderHtml += `<div class="p-2 d-flex justify-content-between align-items-center border-bottom border-1 border-ligh">
                                                                         <div class="col-md-2">
                                                                             <img src="${product.image1}" alt="Product" class="w-75">
                                                                         </div>
                                                                         <div class="col-md-6">
                                                                             <div>
                                                                                 <span>${product.name}</span>
                                                                                 <div>
                                                                                                                                                         <span class="text-muted">Màu sắc: ${ordersList[index].color}/Size: ${ordersList[index].size}</span>
                                                                                                                                                         <span class="text-muted">Số lượng: ${ordersList[index].quantity}</span>
                                                                                                                                                      </div>
                                                                             </div>
                                                                         </div>
                                                                         <div>
                                                                             <span>${amoutDetail}d</span>
                                                                         </div>
                                                                     </div>`

                            })
                            $("#productSelect").append(productOrderHtml)
                            $("#amout").html(amout)
                            costAmout()
        }
    })
}
function getServiceTransction(fromValueDistrict, toValueDistrict, storeId){
    return new Promise(function(resolve, reject){
        $.ajax({
            url:"https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services?from_district=" + fromValueDistrict+"&to_district=" + toValueDistrict +"&shop_id=" + storeId,
            type: "GET",
            headers: {
                'token': '706eeea2-7b1d-11ee-96dc-de6f804954c9'
            },
            dataType: "json",
            success: function(data){
                serviceId = data.data[0].service_id
                resolve(serviceId)
            },
            error: function(error){
                console.error(error)
                reject()
            }
        })
    })
}

function getFeeTransaction(serviceId, amout, selectValueDistrict, selectValueWard){
    console.log(selectValueDistrict, selectValueWard)
    return new Promise(function(resolve, reject){
        $.ajax({
            url:"https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee?service_id=" + serviceId +"&insurance_value=" + amout +"&coupon=&from_district_id=3303&to_district_id=" + selectValueDistrict +"&to_ward_code=" + selectValueWard +"&height=15&length=15&weight=1000&width=15",
            type: "GET",
            headers: {
                'token': '706eeea2-7b1d-11ee-96dc-de6f804954c9'
            },
            dataType: "json",
            success: function(data){
                var fee = data.data.total
                $("#feeTransaction").html(fee)
                resolve()
            },
            error: function(){
                $("#feeTransaction").html(50000)
                reject()
            }
        })

    })
}
  function toggleCheckbox(){
        $("input[type='checkbox']").click(function () {
            var checkboxes = $("input[type='checkbox']");

            checkboxes.not(this).prop("checked", false);
          });
    }

 function costAmout(){
    var amout = Number($("#amout").text())
    console.log(amout, Number($("#amout").text()))
    var feeTransaction = Number($("#feeTransaction").text())
    var sumAmout = amout + feeTransaction
    console.log(sumAmout)
    $("#sumAmout").html(sumAmout)
 }
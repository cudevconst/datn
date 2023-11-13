function getValueInput(){
    return parseInt($("#quantityInput").val(), 10)
}


$(document).ready(async function() {

    var clickColor = 'a';



    await renderInfoProduct()

    $("#reduce").click(function(){
        if(getValueInput() == 1){
            $("#quantityInput").val(1)
        }
        else{
            $("#quantityInput").val(getValueInput() - 1)
        }
    })
    $("#increase").click(function(){
        $("#quantityInput").val(getValueInput() + 1)
    })
    var x = sessionStorage.getItem("product")
    var productId = JSON.parse(x).id
    await findQuantity(productId,$(".activeColor").text(),$(".activeSize").text())



    selectColor()
    console.log(clickColor)
    selectSize()
    $("#clickImage img").click(function() {
        var aUrl = $("#imageDisplay a");
        var imgUrl = $("#imageDisplay img");
        var imageUrl = $(this).attr("src");
        console.log(imageUrl)
        aUrl.attr("href", imageUrl)
        imgUrl.attr("src", imageUrl)
    })
    $("#addCart").click(function(){
        console.log(1)
        console.log($(".activeColor").text())
        console.log($(".activeSize").text())
        console.log($("#quantityInput").val())
    })
});

function selectColor(){
    var x = sessionStorage.getItem("product")
    var productId = JSON.parse(x).id
    $("#buttonColor button").click(function(){
        $("#buttonColor button").removeClass("border")
        $("#buttonColor button").removeClass("border-dark")
        $("#buttonColor button").removeClass("activeColor")
        $(this).addClass("border border-dark")
        $(this).addClass("activeColor")
        var color = $(".activeColor").text()
        var size = $(".activeSize").text()
        findQuantity(productId, color, size)
})
}

function selectSize(){
    var x = sessionStorage.getItem("product")
    var productId = JSON.parse(x).id
    $("#buttonSize button").click(function(){
        $("#buttonSize button").removeClass("border")
        $("#buttonSize button").removeClass("border-dark")
        $("#buttonSize button").removeClass("activeSize")
        $(this).addClass("border border-dark")
        $(this).addClass("activeSize")
        var color = $(".activeColor").text()
        var size = $(".activeSize").text()
        findQuantity(productId, color, size)

})
}

function renderInfoProduct(){
    return new Promise(function(resolve, reject){

        var currentPath = window.location.pathname;
    var slug = currentPath.split('/').pop();
    console.log(slug)
    $.ajax({

        type: "GET",
        url: "http://localhost:8080/api/product/detail/" + slug,
        success: function(product){
            var infoProductHtml = ''
            infoProductHtml += '<div class="col-md-4">'
            infoProductHtml += '<div class="text-center m-4" id="imageDisplay">'
            infoProductHtml += '<a href="'+ product.image1+'" data-fancybox="gallery" class="fancybox">'
            infoProductHtml += '<img src="'+ product.image1+'" alt="Ảnh" class="w-100">'
            infoProductHtml += '</a> '
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="d-flex justify-content-around" id="clickImage">'
            infoProductHtml += '<img src="'+ product.image1+'" alt="" class="w-25">'
            infoProductHtml += '<img src="'+ product.image2+'" alt="" class="w-25">'
            infoProductHtml += '<img src="'+ product.image3+'" alt="" class="w-25">'
            infoProductHtml += '</div>'
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="col-md-8 p-3">'
            infoProductHtml += '<h3 class="font-weight-bold">'+product.name+'</h3>'
            infoProductHtml += '<h6>Chi tiết sản phẩm</h6>'
            infoProductHtml += '<h4 class="text-danger font-weight-bold">'+product.price+'</h4>'
            infoProductHtml += '<span >Còn lại: <span id="quantity"></span></span>'
            infoProductHtml += '<div class="my-4">'
            infoProductHtml += '<span>Màu sắc:</span>'
            infoProductHtml += '<div class=" my-2" id="buttonColor">'
            product.colors.forEach(function(color, index){
                if(index === 0){
                    infoProductHtml += '<button class="btn btn-light m-1 border border-dark activeColor" style="width: 60px;">'+color+'</button>'
                }
                else{
                    infoProductHtml += '<button class="btn btn-light m-1" style="width: 60px;">'+color+'</button>'
                }
            })


            infoProductHtml += '</div>'
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="my-4">'
            infoProductHtml += '<span>Kích thước:</span>'
            infoProductHtml += '<div class=" my-2" id="buttonSize">'

            product.sizes.forEach(function(size, index){
                if(index === 0){
                    infoProductHtml += '<button class="btn btn-light m-1 border border-dark activeSize" style="width: 60px;">'+size+'</button>'
                }
                else{
                    infoProductHtml += '<button class="btn btn-light m-1" style="width: 60px;">'+size+'</button>'
                }
            })


            infoProductHtml += '</div>'
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="d-flex align-items-center my-4">'
            infoProductHtml += '<div class="" style="margin-right: 20px;">'
            infoProductHtml += 'Số lượng:'
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="">'
            infoProductHtml += '<button class="btn btn-light font-weight-bold" style="width: 50px;" id="reduce">-</button>'
            infoProductHtml += '<input type="text" name="quantityInput" id="quantityInput" class="p-1 text-center border-0" value="1" style="width: 70px;">'
            infoProductHtml += '<button class="btn btn-light font-weight-bold" style="width: 50px;" id="increase">+</button>'
            infoProductHtml += '</div>'
            infoProductHtml += '</div>'
            infoProductHtml += '<div class="my-4">'
            infoProductHtml += '<button class="btn btn-primary" onclick="addCart()">Thêm vào giỏ hàng</button>'
            infoProductHtml += '<a href="/order"><button class="btn btn-primary mx-2" onclick="orderProduct()" data-product='+ product.id+'>Mua hàng ngay</button></a>'
            infoProductHtml += '</div>'

            $("#infoProduct").append(infoProductHtml)
            sessionStorage.setItem("product", JSON.stringify(product));
            resolve();
    },
        error: function(error){
            console.error(error)
            reject(error);
        }
    })
    })
}


function findQuantity(productId, color, size){
    return new Promise(function(resolve, reject){
        $.ajax({
            type: "GET",
            url: "/api/inventory/quantity?productId="+ productId+ "&color=" + color+ "&size=" + size,
            success: function(response){
                $("#quantity").text(response)
                resolve()
            },
            error: function(error){
                reject()
            }

        })
    })
}
function orderProduct(){
    var orders = []
    var color = $(".activeColor").text()
    var size = $(".activeSize").text()
    var quantity = $("#quantityInput").val()
    var button = event.target
    var productId = button.getAttribute("data-product");
    data = {
        color: color,
        size: size,
        quantity: quantity,
        productId: productId
    }
    orders.push(data)
    sessionStorage.setItem("orderProduct", JSON.stringify(orders))
}
function addCart(){
    var color = $(".activeColor").text()
    var size = $(".activeSize").text()
    var quantity = $("#quantityInput").val()
    var x = sessionStorage.getItem("product")
    var productId = JSON.parse(x).id
    $.ajax({
        type: "POST",
        url: "/api/cart/add",
        data: JSON.stringify({
            color: color,
            size: size,
            quantity: quantity,
            productId: productId

        }),
        processData: "json",
        contentType: "application/json; charset=utf-8",
        success: function(response){
            console.log(response)

        },
        error: function(error){
            console.error(error)
        }

    })
}


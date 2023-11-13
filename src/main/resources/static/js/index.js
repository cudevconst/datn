$(document).ready(async function(){
    await renderData()

    $(window).on('resize', setImgSizeHeight)
   setImgSizeHeightFirst()


})
function renderData(){
    return new Promise(function(resolve, reject){
        $.ajax({
                type: "GET",
                url: "/api/product/all",
                success: function(data){
                    var productHtml = ''
                    data.forEach(function(product){
                        productHtml += '<div class="col-md-3 text-center">'
                        productHtml += '<div class="my-4 product d-flex align-items-center justify-content-center">'
                        productHtml += '<a href="/product/' + product.slug +'">'
                        productHtml += '<img src="' + product.image1+ '" alt="product" class="w-75">'
                        productHtml += '</a>'
                        productHtml += '</div>'
                        productHtml += '<h5 class="font-weight-bold">' + product.name + '</h5>'
                        productHtml += '<div>'
                        productHtml += '<h6 class="font-weight-bold text-danger">'+product.price + '</h6>'
                        productHtml += '</div>'
                        productHtml += '<div>'
//                        productHtml += '<button class="btn btn-lg btn-link"> <i class="bi bi-cart-plus"></i></button>'
//                        productHtml += '<a href="/order"><button data-product="' + product.id +'" class="btn btn-primary" onclick="orderFunction()">Mua ngay</button></a>'
//                        productHtml += '<button data-product="' + product.id +'" class="btn btn-primary" onclick="orderFunction()">Mua ngay</button>'
                        productHtml += '</div>'
                        productHtml += '</div>'

                    })
                    $("#listProduct").append(productHtml)
                    console.log('ok')

                    resolve(data)
                },
                error: function(error){
                    console.error(error)
                    reject(error)
                }
            })
    })
}
function setImgSizeHeight(){

        var maxHeight = 0;
        var cnt = 1;
            $('.col-md-3').each(function() {
                console.log(cnt)
                var imgHeight = $(this).find('.product img').height();
                if (imgHeight > maxHeight) {
                    maxHeight = imgHeight;
                }
                console.log(imgHeight)
                cnt += 1

            });
            console.log(maxHeight)
            $('.col-md-3').each(function() {
                $(this).find('.product').css('height', maxHeight + 'px');
            })

}

function setImgSizeHeightFirst(){
        return new Promise(function(resolve, reject) {
            var maxHeight = 0;
            var cnt = 1;
            $('.col-md-3').each(function() {
                var img = $(this).find('.product img')[0]; // Lấy phần tử ảnh đầu tiên trong .product
                var $img = $(img); // Chuyển đổi thành đối tượng jQuery

                $img.on('load', function() {
                    var imgHeight = $img.height();
                    if (imgHeight > maxHeight) {
                        maxHeight = imgHeight;
                    }
                    console.log(imgHeight)
                    if ($(this).is(':last-child')) {
                        $('.col-md-3').each(function() {
                            $(this).find('.product').css('height', maxHeight + 'px');
                        });
                    }

                });
            console.log(maxHeight)

            });
        });
}

//function orderFunction(){
//    var button = event.target
//    var productData = button.getAttribute("data-product");
//    console.log("Giá trị data-product: " + productData);
//}
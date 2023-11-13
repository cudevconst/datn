$(document).ready(async function(){
    function isContentFullyVisible() {
        const content = $('.container');

        if (content.height() + content.offset().top < $(window).height()) {
            return true; // Nội dung đã hiển thị hoàn toàn trong trình duyệt
        } else {
            return false; // Nội dung chưa hiển thị hoàn toàn
        }
    }

    if (isContentFullyVisible()) {
        $(".footer").addClass("fixed-bottom")
        console.log("full")
    } else {
        $(".footer").removeClass("fixed-bottom")
        console.log("none")
    }
    $(window).on('resize', function() {
        if(isContentFullyVisible()){
            $(".footer").addClass("fixed-bottom")
            console.log("full")
        }
        else{
            $(".footer").removeClass("fixed-bottom")
            console.log("none")
        }
    })
})
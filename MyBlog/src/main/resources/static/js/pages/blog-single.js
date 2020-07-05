const singleBackUrl = "https://www.vergessen.top";
// const singleBackUrl = "http://127.0.0.1:8666";

window.onload = function () {

    let strs = window.location.href.split("/")
    let articleId = strs[strs.length - 1];
    // 添加文章评论信息
    $.ajax({
        type: "get",
        url: singleBackUrl + "/api/comment/article/" + articleId,
        dataType: "json",
        success: function (json) {
            // 解析json对象，并向页面添加数据
            $.each(json, function (i, item) {
                $('#commentList').append(
                    '<div class="row" style="margin-top: 15px;border-bottom: 2px solid #1f272a">\n' +
                    // '  <div class="col-sm-2">\n' +
                    // '   <div class="comment-image">\n' +
                    // '     <img src="images/comments/1.jpg" class="img-fluid" alt="">\n' +
                    // '   </div>\n' +
                    // '  </div>\n' +
                    '  <div class="col-sm-12" style="margin-bottom: 15px;">\n' +
                    '    <h4 style="color: #fffdf2">' + item.name + '</h4>\n' +
                    '    <span style="color: #413c40">' + item.createBy + '</span>\n' +
                    // '    <a class="reply-btn" id="reply" href="##"><i class="fa fa-reply"></i>回复</a>\n' +
                    '    <p>' + item.content + '</p>\n' +
                    '  </div>\n' +
                    '<div>'
                );
            });
        }
    })

    // 搜索按钮
    $('#searchBtn').click(function () {
        let searchContent = $("#searchContent").val();
        window.location = "../search/" + searchContent;
    });
}

















const backUrl = "https://www.vergessen.top";
// const backUrl = "http://127.0.0.1:8666";

$(document).ready(function () {
    // $.ajax({
    //     url: backUrl + "/api/category/list",
    //     type: "GET",
    //     dataType: "json",
    //     success: function (json) {
    //         $.each(json, function (i, item) {
    //             $('#categoryInfo').append('' +
    //                 '<div class="col-xl-4 col-lg-3 col-sm-3" style="margin-bottom: 20px;">\n' +
    //                 ' <div class="item" style="height: 240px;display: inline-table;">\n' +
    //                 '   <img class="img-fluid" style="height: 240px;display: inline-table;" src="'+ item.pictureUrl +'">\n' +
    //                 '   <div class="overlay">\n' +
    //                 '    <h4 class="item-title">' + item.name + '</h4>\n' +
    //                 '    <a class="icon-img" href="blog.html?categoryId=' + item.id + '"><i class="fa fa-search-plus"></i></a>\n' +
    //                 '    <div class="item-category">\n' +
    //                 '       <span style="color: #f9c828">' + item.number + '</span>\n' +
    //                 '    </div>\n' +
    //                 '   </div>\n' +
    //                 ' </div>\n' +
    //                 '</div>');
    //         });
    //     }
    // });

    $.ajax({
        url: backUrl + "/api/article/top",
        type: "GET",
        dataType: "json",
        success: function (json) {
            $.each(json, function (i, item) {
                if (i < 6){
                    let split = item.createBy.split("T");
                    item.createBy = split[0] + ' ' + split[1];
                    if (item.isTop)
                        $('#articles').append('' +
                            '<div class="col-xl-4 col-lg-12 col-md-4">\n' +
                            '  <div class="post">\n' +
                            '   <!-- Post Image -->\n' +
                            '    <div class="post-img">\n' +
                            '    <img src="'+ item.pictureUrl +'" class="img-fluid">\n' +
                            '    </div>\n' +
                            ' <!-- Post Content -->\n' +
                            '   <div class="post-content">\n' +
                            '   <div class="post-date">'+ item.createBy +'</div>\n' +
                            '  <div class="post-title">\n' +
                            '   <a href="blog-single/' + item.id + '" target="_blank"><h4><span style="color: yellow;font-size: 18px">【置顶】</span>'+ item.title+'</h4></a>\n' +
                            '  </div>\n' +
                            '  <div class="post-text">\n' +
                            '  <p>'+ item.summary +'</p>\n' +
                            '  </div>\n' +
                            '  <a href="blog-single/' + item.id + '" target="_blank" class="post-more">查看更多</a>\n' +
                            '  </div>\n' +
                            ' </div>\n' +
                            ' </div>');
                    else
                        $('#articles').append('' +
                            '<div class="col-xl-4 col-lg-12 col-md-4">\n' +
                            '  <div class="post">\n' +
                            '   <!-- Post Image -->\n' +
                            '    <div class="post-img">\n' +
                            '    <img src="'+ item.pictureUrl +'" class="img-fluid">\n' +
                            '    </div>\n' +
                            ' <!-- Post Content -->\n' +
                            '   <div class="post-content">\n' +
                            '   <div class="post-date">'+ item.createBy +'</div>\n' +
                            '  <div class="post-title">\n' +
                            '   <a href="blog-single/' + item.id + '" target="_blank"><h4>'+ item.title+'</h4></a>\n' +
                            '  </div>\n' +
                            '  <div class="post-text">\n' +
                            '  <p>'+ item.summary +'</p>\n' +
                            '  </div>\n' +
                            '  <a href="blog-single/' + item.id + '" target="_blank" class="post-more">查看更多</a>\n' +
                            '  </div>\n' +
                            ' </div>\n' +
                            ' </div>');
                }
            });
        },


    });

    $.ajax({
        url: backUrl + "/api/friends/list",
        type: "GET",
        dataType: "json",
        success: function (json) {
            $.each(json, function (i, item) {
                $('#myfriends').append('' +
                    '<div class="col-xl-5" style="margin: 0px 9px 20px;' +
                    '           background-color: #1d1d1d;float:left;box-shadow: 0 3px 12px 0 rgba(255, 255, 255, 0.2);" >\n' +
                    '   <div style="float:left;">\n' +
                    '      <a href="' + item.url + '" target="_blank">\n'+
                    '        <img style="width: 100px;height:100px;display:inline-block;border-radius: 50%;" src="' + item.photo + '" \n' +
                    '      </a>\n'+
                    '   </div>\n' +
                    '   <div class="friendDetail" style="float:left;">\n' +
                    '      <a href="' + item.url + '" target="_blank">\n'+
                    '        <p style="padding-top: 20px;padding-left: 10px;width: 220px;overflow:hidden;">' + item.name + '</p>\n' +
                    '        <p style="padding-left: 10px;width: 220px;overflow:hidden;">' + item.detail + '</p>\n' +
                    '      </a>\n'+
                    '   </div>\n' +
                    '</div>\n');
                if (i % 2 === 0)
                    $('#myfriends').append('' +
                        '<div class="col-xl-1" style="float:left;"></div>');
            });
        }
    });

    //发送留言
    $("form").submit(function () {

        let name = $('#name').val();
        let cause = $('#subject').val();
        let email = $('#email').val();
        let message = $('#message').val();

        // 不为空才能增加
        var sendMessage = {
            name: name,
            email: email,
            cause:cause,
            message: message
        }
        // 提交AJAX请求
        $.ajax({
            url: backUrl + "/api/message/add",
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(sendMessage),
            success: function () {
                // 显示成功提示信息
                alert("发送成功!");
                window.location.replace(backUrl)
            },
            error: function () {
                alert("发送成功!");
                window.location.replace(backUrl)
            }
        })
    });
});




















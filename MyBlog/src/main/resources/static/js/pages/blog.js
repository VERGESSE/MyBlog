// const backUrl = "https://www.vergessen.top";
const backUrl = "http://127.0.0.1:8666";

// 页面初始化：填充数据
window.onload = function () {
    // let categoryId = getQueryVariable("categoryId");
    // let search = getQueryVariable("search");
    //
    // //判断是否是从搜索页过来
    // if (search != false) {
    //                 showArticleByCategoryId(categoryId);
    //     //             }
    //     //
    //     //         }
    //     //     });
    //     // }; search = decodeURI(decodeURI(search))
    //     $("#category").append(search);
    //     queryArticleBySearch(search);
    // }else {
    //     $.ajax({
    //         url: backUrl + "/api/category/list/",
    //         type: "GET",
    //         dataType: "json",
    //         success: function (json) {
    //             // 先填充分类信息
    //             $.each(json, function (i, item) {
    //                 if (categoryId.toString().length != 0 && categoryId == item.id) {
    //                     $("#category").append(item.name);
    //                 }
    //             });
    //
    //             if (categoryId == false) {
    //                 $("#category").append("全部");
    //                 // 如果没有指定分类则获取全部文章
    //                 showAllArticleInfo();
    //             } else {
    //                 // 有指定分类则显示指定分类下的文章
    //

    // 搜索按钮
    $('#searchBtn').click(function () {
        let searchContent = $("#searchContent").val();
        window.location = "../search/" + searchContent;
    });
}

// 获取网页中的参数
// function getQueryVariable(variable) {
//     var query = window.location.search.substring(1);
//     var vars = query.split("&");
//     for (var i = 0; i < vars.length; i++) {
//         var pair = vars[i].split("=");
//         if (pair[0] == variable) {
//             return pair[1];
//         }
//     }
//     return (false);
// }

// 显示全部文章信息
// function showAllArticleInfo() {
//
//     $.ajax({
//         type: "get",
//         url: backUrl + "/api/article/list",
//         dataType: "json",
//         success: function (json) {
//             $.each(json, function (i, item) {
//                 // 填充文章信息
//                 $("#articleList").append('' +
//                     '<div class="col-md-4">\n' +
//                     '  <div class="post">\n' +
//                     '    <!-- Post Image -->\n' +
//                     '    <div class="post-img">\n' +
//                     '      <img src="' + item.pictureUrl + '" class="img-fluid">\n' +
//                     '    </div>\n' +
//                     '    <!-- Post Content -->\n' +
//                     '    <div class="post-content">\n' +
//                     '      <div class="post-date">' + item.createBy + '</div>\n' +
//                     '      <div class="post-title">\n' +
//                     '        <a href="blog-single.html?' + item.id + '" target="_blank"><h4>' + item.title + '</h4></a>\n' +
//                     '      </div>\n' +
//                     '      <div class="post-text">\n' +
//                     '        <p>' + item.summary + '</p>\n' +
//                     '      </div>\n' +
//                     '      <a href="blog-single.html?' + item.id + '" target="_blank" class="post-more">查看全文</a>\n' +
//                     '    </div>\n' +
//                     '  </div>\n' +
//                     '</div>')
//             });
//         }
//     });
// }

// 显示指定分类下的文章列表
// function showArticleByCategoryId(id) {
//     $.ajax({
//         type: "get",
//         url: backUrl + "/api/article/list/" + id,
//         dataType: "json",
//         success: function (json) {
//             $.each(json, function (i, item) {
//                 // 填充文章信息
//                 $("#articleList").append('' +
//                     '<div class="col-md-4">\n' +
//                     '  <div class="post">\n' +
//                     '    <!-- Post Image -->\n' +
//                     '    <div class="post-img">\n' +
//                     '      <img src="' + item.pictureUrl + '" class="img-fluid">\n' +
//                     '    </div>\n' +
//                     '    <!-- Post Content -->\n' +
//                     '    <div class="post-content">\n' +
//                     '      <div class="post-date">' + item.createBy + '</div>\n' +
//                     '      <div class="post-title">\n' +
//                     '        <a href="blog-single.html?' + item.id + '" target="_blank"><h4>' + item.title + '</h4></a>\n' +
//                     '      </div>\n' +
//                     '      <div class="post-text">\n' +
//                     '        <p>' + item.summary + '</p>\n' +
//                     '      </div>\n' +
//                     '      <a href="blog-single.html?' + item.id + '" target="_blank" class="post-more">查看全文</a>\n' +
//                     '    </div>\n' +
//                     '  </div>\n' +
//                     '</div>')
//             });
//         }
//     });
// }

//根据搜索信息查询博文
// function queryArticleBySearch(search) {
//     // 提交AJAX请求
//     $.ajax({
//         url: backUrl + "/api/search/" + search,
//         type: "GET",
//         success: function (json) {
//             $.each(json, function (i, item) {
//                 // 填充文章信息
//                 $("#articleList").append('' +
//                     '<div class="col-md-4">\n' +
//                     '  <div class="post">\n' +
//                     '    <!-- Post Image -->\n' +
//                     '    <div class="post-img">\n' +
//                     '      <img src="' + item.pictureUrl + '" class="img-fluid">\n' +
//                     '    </div>\n' +
//                     '    <!-- Post Content -->\n' +
//                     '    <div class="post-content">\n' +
//                     '      <div class="post-date">' + item.createBy + '</div>\n' +
//                     '      <div class="post-title">\n' +
//                     '        <a href="blog-single.html?' + item.id + '" target="_blank"><h4>' + item.title + '</h4></a>\n' +
//                     '      </div>\n' +
//                     '      <div class="post-text">\n' +
//                     '        <p>' + item.summary + '</p>\n' +
//                     '      </div>\n' +
//                     '      <a href="blog-single.html?' + item.id + '" target="_blank" class="post-more">查看全文</a>\n' +
//                     '    </div>\n' +
//                     '  </div>\n' +
//                     '</div>')
//             });
//
//         }
//     })


}


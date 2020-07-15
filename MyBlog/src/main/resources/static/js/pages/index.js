let host = window.location.host;
let applyFriendDia = null;
let applyLinkDia = null;
let page = 1;
let msgPage = 1;
$(document).ready(function () {
    $("#apply-friend").click(function () {
        if (applyFriendDia == null) {
            let dia = document.getElementById("apply-friend-dia");
            applyFriendDia = dia.innerHTML;
            dia.parentElement.removeChild(dia);
        }
        layer.open({
            title: '友链申请',
            type: 1,
            icon: 5,
            anim: 5,
            area: ['550px', '700px'],
            shadeClose: true, //点击遮罩关闭
            content: applyFriendDia
        });
    });

    $("#apply-good-link").click(function () {
        if (applyLinkDia == null) {
            let dia = document.getElementById("apply-good-link-dia");
            applyLinkDia = dia.innerHTML;
            dia.parentElement.removeChild(dia);
        }
        layer.open({
            title: '外链申请',
            type: 1,
            icon: 5,
            anim: 5,
            area: ['500px', '460px'],
            shadeClose: true, //点击遮罩关闭
            content: applyLinkDia
        });
    });

    // 加载更多外链
    $('#load-link').click(function () {
        page++;
        $.ajax({
           url: '/article/link?page=' + page + '&size=15',
           type: "GET",
           dataType: "json",
           success: function (json) {
               layer.msg('加载更多外链成功');
               // 如果加载完全部外链则把加载更多按钮隐藏
               if (json.list.length < 15)
                   $('#load-link').css('visibility','hidden');
               $.each(json.list, function (i, item) {
                  $('#link-list').append(
                     '<div class="col-lg-12" style="margin-bottom: 25px">\n' +
                     '     <a class="good-link" href="'+ item.url +'" target="_blank">\n' +
                     '         <span>'+ item.title +'</span>\n' +
                     '     </a>\n' +
                     ' </div>'
                  )
               });
           }
        });
    });

    // 加载新留言
    $('#next-msg').click(function () {
        msgPage++;
        $.ajax({
            url: '/message/message?page=' + msgPage + '&size=8',
            type: "GET",
            dataType: "json",
            success: function (json) {
                if (json.list.length === 0){
                    layer.msg('已经到底~~~');
                    msgPage = 0;
                    return
                }
                layer.msg('成功加载新一批留言');
                $('#msg-list').empty();
                $.each(json.list, function (i, item) {
                    $('#msg-list').append(
                        '<div class="contact-box">\n' +
                        '    <h4 class="title-box">'+item.name+'</h4>\n' +
                        '    <div class="content-box">\n' +
                        '        <p>\n' +
                        '            <span><i class="fa fa-commenting"></i>&nbsp&nbsp'+item.message+'</span>\n' +
                        '        </p>\n' +
                        '    </div>\n' +
                        '</div>'
                    )
                });
            }
        });
    });
});

function applyFriend() {
    // 提交AJAX请求
    $.ajax({
        url: "/friend/friend",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            name: $('#friend-nickname').val(),
            detail: $('#friend-detail').val(),
            url: $('#friend-url').val(),
            photo: $('#friend-photo').val(),
            email: $('#friend-email').val()
        }),
        success: function () {
            layer.close(layer.index);
            layer.msg('友链申请提交成功,请等待博主审核...');
        },
        error: function () {
            layer.close(layer.index);
            layer.msg('友链申请提交失败,请重试...');
        }
    });
    return false
}

function applyLink() {
    // 提交AJAX请求
    $.ajax({
        url: "/article/link",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            title: $('#link-title').val(),
            url: $('#link-url').val(),
            email: $('#link-email').val()
        }),
        success: function () {
            layer.close(layer.index);
            layer.msg('外链申请提交成功,请等待博主审核...');
        },
        error: function () {
            layer.close(layer.index);
            layer.msg('外链申请提交失败,请重试...');
        }
    });
    return false
}

// 新增留言
function addMessage() {
    $.ajax({
        url: "/message/msg",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            name: $('#msg-name').val(),
            message: $('#message').val(),
            email: $('#msg-email').val()
        }),
        success: function () {
            $('#msg-name').val('');
            $('#message').val('');
            $('#msg-email').val('');
            layer.msg('留言添加成功,博主审核成功后会在首页显示哦。');
        },
        error: function () {
            layer.msg('留言添加失败,请重试...');
        }
    });
    return false
}

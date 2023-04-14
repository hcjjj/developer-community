$(function () {
    // 绑定单击事件
    // $("#topBtn").click(setTop);
    $("#topBtn").click(updateTop);
    // $("#wonderfulBtn").click(setWonderful);
    $("#wonderfulBtn").click(updateWonderful);
    $("#deleteBtn").click(setDelete);
    $("#gotoTop").click(gotoTop());
});

// 收藏
function collect(btn, postId) {
    $.post(
        CONTEXT_PATH + "/collect",
        {"postId": postId},
        function (data) {
            // 字符串转json对象
            data = $.parseJSON(data);
            if (data.code == 0) {
                // 请求成功
                $(btn).children("i").text(data.collectCount);
                $(btn).children("b").text(data.collectStatus == 1 ? '已收藏' : "收藏");
            } else {
                // 请求失败
                alert(data.msg);
            }
        }
    );
}


// 点赞
function like(btn, entityType, entityId, entityUserId, postId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType": entityType, "entityId": entityId, "entityUserId": entityUserId, "postId": postId},
        function (data) {
            // 字符串转json对象
            data = $.parseJSON(data);
            if (data.code == 0) {
                // 请求成功
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus == 1 ? '已赞' : "赞");
            } else {
                // 请求失败
                alert(data.msg);
            }
        }
    );
}

// 置顶
// function setTop() {
//     // 异步post请求
//     $.post(
//         CONTEXT_PATH + "/discuss/top",
//         {"id":$("#postId").val()},
//         function(data) {
//             // 解析为字符串对象
//             data = $.parseJSON(data);
//             if(data.code == 0) {
//                 // 成功，修改按钮属性
//                 $("#topBtn").attr("disabled", "disabled");
//             } else {
//                 // 失败
//                 alert(data.msg);
//             }
//         }
//     );
// }

// 加精
// function setWonderful() {
//     $.post(
//         CONTEXT_PATH + "/discuss/wonderful",
//         {"id": $("#postId").val()},
//         function (data) {
//             data = $.parseJSON(data);
//             if (data.code == 0) {
//                 $("#wonderfulBtn").attr("disabled", "disabled");
//             } else {
//                 alert(data.msg);
//             }
//         }
//     );
// }

// 置顶or取消置顶
function updateTop() {
    let type = $("#topBtn").val();
    let updateType = type == 1 ? 0 : 1;
    $.post(
        CONTEXT_PATH + "/discuss/top",
        {
            "id": $("#postId").val(),
            "type": updateType
        },
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                // 置顶成功后, 修改button
                $("#topBtn").attr('value', updateType);
                $("#topBtn").html(updateType == 1 ? "取消置顶" : "置顶");
                // location.reload();
            } else {
                alert(data.msg);
            }
        }
    )
}

// 加精or取消加精
function updateWonderful() {
    let status = $("#wonderfulBtn").val();
    let updateStatus = status == 1 ? 0 : 1;
    $.post(
        CONTEXT_PATH + "/discuss/wonderful",
        {
            "id": $("#postId").val(),
            "status": updateStatus
        },
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                // 置顶成功后, 修改button
                $("#wonderfulBtn").attr('value', updateStatus);
                $("#wonderfulBtn").html(updateStatus == 1 ? "取消加精" : "加精");
                // location.reload();
            } else {
                alert(data.msg);
            }
        }
    )
}

// 删除帖子按钮
function setDelete() {

    var tags = [-1];
    $('input[name="tag"]').each(function () {
        tags.push($(this).val());
    });

    // alert(tags);
    // 点击确认按钮会返回true
    var flag = confirm("确定删除这篇文章吗");

    if (flag) {
        $.post(
            CONTEXT_PATH + "/discuss/delete",
            {"userId": $("#userId").val(), "postId": $("#postId").val(), "typeId": $("#typeId").val(), "tags": tags},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    // 删除成功跳转到首页
                    location.href = CONTEXT_PATH + "/index";
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}

/**
 * JavaScript脚本实现回到页面顶部示例
 * @param acceleration 速度
 * @param stime 时间间隔 (毫秒)
 **/
function gotoTop(acceleration, stime) {
    acceleration = acceleration || 0.1;
    stime = stime || 10;
    var x1 = 0;
    var y1 = 0;
    var x2 = 0;
    var y2 = 0;
    var x3 = 0;
    var y3 = 0;
    if (document.documentElement) {
        x1 = document.documentElement.scrollLeft || 0;
        y1 = document.documentElement.scrollTop || 0;
    }
    if (document.body) {
        x2 = document.body.scrollLeft || 0;
        y2 = document.body.scrollTop || 0;
    }
    var x3 = window.scrollX || 0;
    var y3 = window.scrollY || 0;

    // 滚动条到页面顶部的水平距离
    var x = Math.max(x1, Math.max(x2, x3));
    // 滚动条到页面顶部的垂直距离
    var y = Math.max(y1, Math.max(y2, y3));

    // 滚动距离 = 目前距离 / 速度, 因为距离原来越小, 速度是大于 1 的数, 所以滚动距离会越来越小
    var speeding = 1 + acceleration;
    window.scrollTo(Math.floor(x / speeding), Math.floor(y / speeding));

    // 如果距离不为零, 继续调用函数
    if (x > 0 || y > 0) {
        var run = "gotoTop(" + acceleration + ", " + stime + ")";
        window.setTimeout(run, stime);
    }
}
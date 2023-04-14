$(function () {
    $("#publishBtn").click(publish);
    $("#checkBtn").click(check);
    $("#backIndexBtn").click(backIndex);
    $("#checkTag").click(checkTag);
});

function checkTag() {
    var tags = [];
    $('input[name="tag"]:checked').each(function () {
        tags.push($(this).val());
    });

    var btn = document.getElementById('addTags');
    if (tags.length > 5) {
        alert("最多只能选择5个标签");
        btn.style.color = 'rgb(255 0 0)';
    } else {
        btn.style.color = 'rgb(76 83 90)';
    }
}

// 检查字段
function check() {
    // 获取标题和内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
    var typeId = $("#typeId option:selected").val();

    var tags = [];
    $('input[name="tag"]:checked').each(function () {
        tags.push($(this).val());
    });

    if (title == "" || content == "") {
        alert("标题或内容未填写");
    } else {
        if (typeId == "") {
            alert("文章类型未选择");
        } else {
            if (title.length > 30) {
                alert("标题不能超过30个字符");
            } else {
                if (tags.length > 5 || tags.length < 1) {
                    alert("标签个数最多5个，至少1个");
                } else {
                    $("#exampleModal").modal("show");
                }
            }
        }
    }
}

// 发布文章
function publish() {
    $("#exampleModal").modal("hide");
    // 获取标题和内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();
    var typeId = $("#typeId option:selected").val();

    var tags = [];
    $('input[name="tag"]:checked').each(function () {
        tags.push($(this).val());
    });

    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title": title, "content": content, "typeId": typeId, "tags": tags},
        // 处理服务端返回的数据
        function (data) {
            // String -> Json 对象
            data = $.parseJSON(data);
            // 在提示框 hintBody 显示服务端返回的消息
            $("#hintBody").text(data.msg);
            // 显示提示框
            $("#hintModal").modal("show");
            // 2s 后自动隐藏提示框
            setTimeout(function () {
                $("#hintModal").modal("hide");
                // 操作完成后，跳转到首页
                if (data.code == 0) {
                    location.href = CONTEXT_PATH + "/index";
                }
            }, 2000);
        }
    )
}

// 返回主页
function backIndex() {
    location.href = CONTEXT_PATH + "/index";
}
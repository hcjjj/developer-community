$(function () {
    $("#sendBtn").click(send_letter);
    $(".close").click(delete_msg);
});

// 点击发送按钮的动作
function send_letter() {
    // 把编辑框关闭
    $("#sendModal").modal("hide");
    // 发送私信
    var toName = $("#recipient-name").val();
    var content = $("#message-text").val();
    if (toName == "" || content == "") {
        alert("有数据为空")
    } else {
        $.post(
            CONTEXT_PATH + "/letter/send",
            {"toName": toName, "content": content},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    $("#hintBody").text("发送成功!");
                } else {
                    $("#hintBody").text(data.msg);
                }

                // 显示发送成功提示
                $("#hintModal").modal("show");
                // 2s 后自动关闭
                setTimeout(function () {
                    $("#hintModal").modal("hide");
                    // 页面刷新
                    location.reload();
                }, 2000);
            }
        );

    }
}

// 删除私信
function delete_msg() {
    var btn = this;
    var id = $(btn).prev().val();
    $.post(
        CONTEXT_PATH + "/letter/delete",
        {"id":id},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                $(btn).parents(".my-media").remove();
            } else {
                alert(data.msg);
            }
        }
    );
}
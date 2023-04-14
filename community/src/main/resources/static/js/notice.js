$(function () {
    $(".close").click(delete_msg);
});

// 删除私信
function delete_msg() {
    var flag = confirm("确定删除该条系统通知信息吗");
    if (flag) {
        var btn = this;
        var id = $(btn).prev().val();
        $.post(
            CONTEXT_PATH + "/letter/delete",
            {"id": id},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    $(btn).parents(".media").remove();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}
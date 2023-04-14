// 客户端上传
$(function(){
    $("#uploadForm").submit(upload);
    $("form").submit(check_data);
});

function check_data() {
    var pwd1 = $("#new-password").val();
    var pwd2 = $("#confirm-password").val();
    if(pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}

function upload() {
    // $.ajax 比 $.post 强大
    $.ajax({
        // https://developer.qiniu.com/kodo/1671/region-endpoint-fq
        url: "http://upload-z2.qiniup.com",
        method: "post",
        // 上传的是文件二进制的不转字符串
        processData: false,
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function(data) {
            if(data && data.code == 0) {
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function(data) {
                        data = $.parseJSON(data);
                        if(data.code == 0) {
                            window.location.reload();
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!");
            }
        }
    });
    // 事件到此为止
    return false;
}
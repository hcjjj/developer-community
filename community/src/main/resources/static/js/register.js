// 校验用户名
var usernameInput = document.getElementById("username");
usernameInput.onblur = checkUsername;

function checkUsername() {
    var username = usernameInput.value.trim();
    // alert(username);
    // 用户名的长度 ：3 ～ 12
    // 正则表达式 单词字符6-12位：
    // var reg = /^\w{6,12}$/;
    // var flag = reg.test(username);
    var flag = username.length >= 3 && username.length <= 12;
    if (flag) {
        //如果前端校验通过，发送异步请求校验一下用户名是否已经存在
        $.post(
            CONTEXT_PATH + "/check",
            {"username": username},
            function (data) {
                data = $.parseJSON(data);
                if (data.code) {
                    if (username != "") {
                        $("#username").addClass("is-invalid");
                    }
                    return !flag;
                }
            }
        )
    } else {
        // 如果没有输入，则不显示相关提示
        if (username != "") {
            $("#username").addClass("is-invalid");
        }
    }
    return flag;
}

// 校验密码一致性
var passwordInput = document.getElementById("confirm-password");
passwordInput.onblur = checkPassword;

function checkPassword() {
    var pwd1 = $("#password").val();
    var pwd2 = $("#confirm-password").val();
    // 两次输入的密码不一致
    if (pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}


$(function () {
    $("form").submit(check_data);
    $("input").focus(clear_error);
});

function check_data() {

    // 点击提交按钮后的校验
    if (checkUsername() && checkPassword()) {
        return true;
    }
    return false;

}

function clear_error() {
    $(this).removeClass("is-invalid");
}
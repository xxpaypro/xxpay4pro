<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>错误提示</title>
    <link rel="stylesheet" href="/css/demos.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/weui/1.1.1/style/weui.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css">
</head>
<body ontouchstart>
<header class='demos-header'>
    <h1 class="demos-title">错误</h1>
</header>
<div class="page__bd">
    <div class="weui_msg">
        <div class="weui_icon_area"><i class="weui_icon_warn weui_icon_msg"></i></div>
        <div class="weui_text_area">
            <h2 class="weui_msg_title">操作异常</h2>
            <p class="weui_msg_desc">[${errMsg.code}]${errMsg.message}</p>
        </div>
        <div class="weui_opr_area">
            <p class="weui_btn_area">
                <a href="javascript:win.close();" class="weui_btn weui_btn_default">取消</a>
            </p>
        </div>
        <div class="weui_extra_area">
            <a href="">聚合支付</a>
        </div>
    </div>
</div>
<script src="//cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
</body>

</html>
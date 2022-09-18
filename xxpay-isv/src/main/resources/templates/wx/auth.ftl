<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
    <title>提示</title>
    <link rel="stylesheet" href="/css/demos.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/weui/1.1.1/style/weui.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/jquery-weui/1.0.1/css/jquery-weui.min.css">
</head>
<body ontouchstart>
<header class='demos-header'>

    <#if errMsg?? >
        <h1 class="demos-title" style="color:red">${errMsg}</h1>
    <#else>
        <h1 class="demos-title">授权成功！</h1>
    </#if>

</header>
<div class="page_bd">
    <div class="weui_msg">
        <div class="weui_icon_area"><i class="weui_icon_warn weui_icon_msg"></i></div>
        <div class="weui_opr_area">
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
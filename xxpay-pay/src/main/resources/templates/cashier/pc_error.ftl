<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>聚合支付|收银台</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link href="/css/cashier.css" rel="stylesheet">
</head>
<body>
<div style="width: 1000px; margin: 0 auto">
    <ul class="layui-nav" lay-filter="">
        <li class="layui-nav-item" style="font-size: 18px;">收 银 台</li>
    </ul>
    <div class="err-visit">
        <div class="err-cont">
            <div class="tit">访问页面出错</div>
            <p>错误代码：<span>${errMsg.code}</span></p>
            <p>错误详情：<span>${errMsg.message}</span></p>
        </div>
    </div>
</div>
<div style="margin-top: 20px;text-align: center">©2018 聚合支付</div>
</body>
</html>
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
    <div class="mid-cont">
        <div class="mid-cont-inner">
            <!-- pay-tit start -->
            <div class="pay-tit botline clearfix">
                <div class="pay-logo fl">
                    <img src="/images/img_${payType!}.png" />
                </div>
            </div>
            <!-- pay-tit end -->
        <#if status == 2>
        <div class="pay-result suc">
        <h4>
            支付成功
        <#elseif status == -1>
        <div class="pay-result fail">
        <h4>
            支付失败
        <#else>
        <div class="pay-result process">
        <h4>
            支付处理中
        </#if>
        </h4>
            <!-- order-info start -->
            <div class="order-info">
                <p><strong>产品名称： </strong><span>${subject!}</span></p>
                <p><strong>支付金额： </strong><span>${amountStr!}</span></p>
                <p><strong>收款方： </strong><span>${mchName!}</span></p>
                <p><strong>订单编号： </strong><span>${mchOrderNo!}</span></p>
            </div>
            <!-- order-info end -->
        </div>
        </div>
        </div>
</div>
        <div style="margin-top: 20px;text-align: center">©2018 聚合支付</div>
</body>
</html>
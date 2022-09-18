<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>聚合支付|收银台</title>
    <link href="/css/payComplete.css" rel="stylesheet">
    <link href="/css/index.css" rel="stylesheet">
</head>
<body>
<div class="head clearfix">
    <div class="main">
        <div class="logo-area">
            <h2 style="border-left-width: 0px">收银台</h2>
        </div>
    </div>
</div>
<#--<div class="main clearfix">
    <div class="progress">
        <ul class="all clearfix">
            <li>1、提交订单</li>
            <li>2、选择支付方式</li>
            <li>3、
            <#if status == 2>
                购买成功
            <#elseif status == -1>
                购买失败
            <#else>
                购买处理中
            </#if>
            </li>
        </ul>
    </div>
</div>-->

<div class="main clearfix">
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

<div class="footer clearfix">©2018 聚合支付</div>
<#--<script type="text/javascript" src="/pub/js/payComplete.js?873a00e0b8b4d935e7ca"></script>--></body>
</html>.
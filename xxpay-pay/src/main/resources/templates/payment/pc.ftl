<!DOCTYPE html>
<html lang="zh">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>聚合支付|收银台</title>
    <script type="text/javascript">
        var base_url = "";
    </script>
    <link href="/css/common.css" rel="stylesheet">
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
<div class="main clearfix">
    <div class="progress">
        <ul class="cur clearfix">
            <li>1、提交订单</li>
            <li>2、选择支付方式</li>
            <li>3、购买成功</li>
        </ul>
    </div>
    <div class="pay-content">
        <p>
            <strong>请您及时付款,以便订单尽快处理！</strong>请您在提交订单后
            <span>24小时</span>内完成支付，否则订单会自动取消。
        </p>

        <p class="order-id">支付金额：
            <span id="PaysumOfMoney">${amountStr!}</span> 元</p>
        <ul class="payInfor">
            <li>
                <strong>收款方</strong>
                <em>：</em>${mchName!}</li>
            <li>商品名称：${subject!}</li>
        </ul>
        <dl class="pay-list">
            <dt>支付方式</dt>
            <dd>
                <#list products as pro>
                    <a id="${pro.id}" data-pmtprodcode="${pro.payType}" data-mchId="${mchId!}" data-appId="${appId!}" data-mchOrderNo="${mchOrderNo!}" data-productId="${pro.id!}" data-payType="${pro.payType!}" data-amount="${amount!}" data-subject="${subject!}" data-body="${body!}" data-notifyUrl="${notifyUrl!}" target="_blank">
                        <span>
                            <img src="/images/img_${pro.payType}.png" alt="${pro.productName}">
                        </span>
                    </a>
                </#list>
            </dd>
        </dl>
    </div>
</div>

<div id="pop-wxpay-native" class="pop-layer pop-wx code12" style="display:none">
    <div class="pop-tit">
        <h5>微信扫一扫付款</h5>
        <#--<button class="cls-btn">关闭</button>-->
    </div>
    <div class="scancode-box">
        <h4>请使用
            <em>微信</em>
            <span>扫一扫</span>
        </h4>
        <h3>扫描二维码支付</h3>
        <div class="scancode">
            订单二维码
        </div>
        <p>二维码有效时长为2小时，请尽快支付</p>
    </div>
</div>

<div id="pop-alipay-native" class="pop-layer pop-zfb code16 clearfix">
    <div class="pop-tit">
        <h5>支付宝扫一扫付款</h5>
        <#--<button class="cls-btn">关闭</button>-->
    </div>
    <div class="scancode-box fl">
        <div class="scancode">
            订单二维码
        </div>
        <p>打开
            <span>支付宝扫一扫</span>
        </p>
    </div>
    <div class="alipay-step fr">
        <p>1、打开手机支付宝</p>
        <p>2、扫描二维码付款</p>
    </div>
</div>

<div class="footer clearfix">©2018 聚合支付</div>
<div class="mask-layer "></div>

<div class="pop-layer" id="confirmPayBox">
    <div class="pop-tit">
        <h5>确认支付</h5>
        <#--<button class="cls-btn">关闭</button>-->
    </div>
    <div class="pop-cont">
        <p>
            <strong>请在新打开的支付页面完成支付</strong>支付完成前请勿关闭本窗口</p>
        <div class="pop-btns">
            <button class="pay-success-btn">
                <strong>已完成支付</strong>
            </button>
            <#--<button class="pay-erros-btn cls-btn">
                <strong>更换支付方式</strong>
            </button>-->
        </div>
        <p class="pro">支付遇到问题？请联系&nbsp;
            <span class="hotline">4000-888-6666</span>&nbsp;获得帮助</p>
    </div>
</div>

<div class="prop-box">
    <div class="pop-tit">
        <h5>提示</h5>
        <button class="cls-btn prop-close">关闭</button>
    </div>

    <div class="pop-cont">
        <p class="error"></p>
        <p class="pro">支付遇到问题？请联系&nbsp;
            <span class="ft-btn">4000-888-6666</span>&nbsp;获得帮助</p>
    </div>
</div>


<form id="payComplete" style="display: none" method="post" action="/api/cashier/pc_complete">
    <input id="payOrderId" name="payOrderId" type="hidden" value="">
    <input id="mchId" name="mchId" type="hidden" value="${mchId}">
    <input id="mchName" name="mchName" type="hidden" value="${mchName}">
</form>

<form id="paySubmit" style="display: none" method="" action="" target="_blank"></form>

<script>
    var goSuccessUrl = '';
    var goFailUrl = '';
</script>

<script src="/js/index.js"></script>
<script src="//cdn.bootcss.com/jquery/2.2.3/jquery.js"></script>

</body>

</html>
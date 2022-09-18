<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>收银台_XxPay</title>
    <style>
        * {
            padding: 0;
            margin: 0;
        }
        body {
            background: #f0eff4;
        }
        .wrapper {
            /* width: 100%; */
            background: #f0eff4;
            padding: 20px 10px;
        }
        .header {
            position: relative;
            text-align: center;
            margin-bottom: 40px;;
        }

        .header img {
            position: absolute;
            left: 0;
            right: 0;
            margin:auto;
        }
        .main-wrap {
            border: 1px solid #ddd;
            background-color: #fff;
            padding: 50px 10px 15px;
            text-align: center;
        }
        .main-wrap h4 {
            padding-bottom: 15px;
            border-bottom: 1px dashed #ddd;
        }
        .clearFix:after,
        .clearFix:before {
            content: '';
            clear: both;
            display: block;
        }
        .main-wrap .wap-form {
            width: 100%;
            padding-top: 15px;
            /* border:1px solid #f00; */
            text-align: left;
            line-height: 1.5;

        }
        .main-wrap .wap-form label {
            color: #a5a5a5;
            float: left;
            font-size: 1rem;
        }
        .main-wrap .wap-form .shuru {
            width: 70%;
            float: right;
            border: none;
            text-align: right;
            padding: 5px;
        }
        .main-wrap .wap-form input:focus {
            border: none;
        }
        p.beizhu {
            text-align: right;
            color: #7694ca;
            font-size: 12px;
            margin-top: 10px;
        }
        p.beizhu input {
            border: none;
            display: none;
            padding: 5px 0;
            width: 100%;
            background: #f0eff4;
            text-align: right;
        }
        textarea {
            text-decoration: none;
            color: #7694ca;
            font-size: 12px;
            border: none;
            text-align: right;
            width: 100%;
        }
        /* @media screen  and (){

        } */
        .jianpan {
            width: 100%;
            position: fixed;
            bottom: 0;
            box-shadow: 1px 1px #ddd;

        }
        .jianpan table {
            width: 100%;
            border-collapse: collapse;
            color: #676767;
            background: #fff;
        }
        .jianpan td {
            text-align: center;
            width: 25%;
            height: 70px;
            vertical-align: middle;
            margin: 0;
            font-size: 1.5rem;
            border: 1px solid #ddd;
        }
        .blank50 {
            height: 50px;
        }
        .fukuan {
            background-color: #a0c0ef;
            color: #fff;
            text-align: center;
        }
        .fukuan span {
            display: inline-block;
            width: 30px;
        }
        .lightB {
            background-color: #a0c0ef !important;
        }
        .darkB {
            background-color: #28a4e6 !important;
        }
        .xybank {
            text-align: center;
            margin-bottom: 10px;
        }
        .xybank img {
            max-width: 100%;
            vertical-align: middle
        }
        .xybank span {
            padding-left: 5px;
            border-left: 1px solid #ddd;
            font-size: 12px;
            color: #676767;
        }
    </style>
</head>
<body>
<div id='zf'>
    <div class="wrapper">
        <div class="header">
            <img src="/images/logo.png" alt="">
        </div>
        <div class="main-wrap">
            <h4>向${codeName}付款</h4>
            <div class="wap-form clearFix">
                <label for="">金额</label>
                <input class="shuru" name="qrCode" v-model="something" >
            </div>
        </div>
        <p class="beizhu">
            <span>添加付款备注</span>
            <input type="text">
        </p>
        <div class="blank50"></div>
    </div>

    <div class="jianpan">
        <div class="xybank">
            <img src="/images/xybank.png">
            <span>提供服务</span>
        </div>
        <table>
            <tr>
                <td class="commC">1</td>
                <td class="commC">2</td>
                <td class="commC">3</td>
                <td class="del">x</td>
            </tr>
            <tr>
                <td class="commC">4</td>
                <td class="commC">5</td>
                <td class="commC">6</td>
                <td class="fukuan" rowspan="3"><span>付款</span></td>
            </tr>
            <tr>
                <td class="commC">7</td>
                <td class="commC">8</td>
                <td class="commC">9</td>
            </tr>
            <tr>
                <td colspan="2" class="commC">0</td>
                <td class="commC">.</td>
            </tr>
        </table>
    </div>

</div>
<script src="https://cdn.bootcss.com/jquery/2.2.3/jquery.js"></script>
<script src="https://cdn.bootcss.com/vue/2.5.13/vue.js"></script>
<script>
    $('.shuru').on("focus",function(){
        document.activeElement.blur();//屏蔽默认键盘弹出；
    });

    //当点击添加备注的时候
    $('.beizhu').click(function(){
        $('.beizhu input').focus();
        $('.beizhu input').css({"display":"block","outline":'none'})

        $('.beizhu span').css('display','none');
    })

    $(function(){
        $('.shuru').focus().css('outline','none');

    })
    var html = '';
    $('.commC').click(function(){
        var curVal = $.trim($(this).html());
        html+=curVal;
        $('.fukuan').addClass('darkB').removeClass('lightB');
        html = html.replace(/[^\d.]/g, "").
        //只允许一个小数点
        replace(/^\./g, "").replace(/\.{2,}/g, ".").
        //只能输入小数点后两位
        replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3')

        $('.shuru').val(html);
    })

    // if($('.fukuan').hasClass('darkB')){

    $('.fukuan').click(function(){
        console.log(3333)
        $('.shuru').val(($('.shuru').val().replace(/\.$/g, "")));
        var curS = $.trim($('.shuru').val());
        if(!curS){
            alert('金额不能为空')
        }else{
            //需要发起Ajax请求
            console.log('需要发请求')
            paymentOrder(curS);
        }

    })
    // }

    $('.del').click(function(){
        var curS = $.trim($('.shuru').val());
        if(curS && curS.length){
            curS = curS.substring(0,curS.length-1);
            $('.shuru').val(curS);
            $('.shuru').focus().css('outline','none');
            html='';
        }else{
            $('.fukuan').addClass('lightB').removeClass('darkB');
        }
    })

    var flag = false;
    var appId = "";
    var timeStamp = "";
    var nonceStr = "";
    var package = "";
    var signType = "";
    var paySign = "";
    var payUrl = "";

    //开始支付
    function paymentOrder(amount){
        $.ajax({
            url: '/payment/scan_pay?mchId=${mchId}&appId=${appId}&codeId=${codeId}&channelId=${channelId}&openId=${openId!}&amount=' + amount,
            type: 'GET',
            cache: false,
            async: false,
            dataType: 'JSON',
            timeout: 5000,
            error: function(textStatus){
                $.alert('系统错误~');
            },
            success: function(msg){
                if(msg.code == 0){
                    var data = msg.data;
                    appId = data.payParams.appId;
                    timeStamp = data.payParams.timeStamp;
                    nonceStr = data.payParams.nonceStr;
                    package = data.payParams.package;
                    signType = data.payParams.signType;
                    paySign = data.payParams.paySign;
                    payUrl = data.payParams.payUrl;
                    flag = true;
                }else {
                    alert('[' + msg.code + ']' + msg.msg);
                }
            }
        });
        if(flag) {
            //唤起微信支付
            if('${channelId}'.toUpperCase() =='WXPAY_JSAPI'){
                pay();
            }else if('${channelId}'.toUpperCase() =='ALIPAY_WAP') {
                $('#alipay').html(payUrl);
            }
        }
    }

    //唤起微信支付
    function pay(){
        if (typeof WeixinJSBridge == "undefined"){
            if( document.addEventListener ){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        }else{
            onBridgeReady();
        }
    }

    //开始支付
    function onBridgeReady(){
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId" : appId,                    //公众号名称，由商户传入
                    "timeStamp": timeStamp+"",          //时间戳，自1970年以来的秒数
                    "nonceStr" : nonceStr,              //随机串
                    "package" : package,
                    "signType" : signType,              //微信签名方式:
                    "paySign" : paySign                 //微信签名
                },

                function(res){
                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                        alert("付款成功");
                        window.close();
                        //$.alert("支付成功");  // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                        //回到用户订单列表
                        //window.location.href="http://wx.ooklady.com/wechat/order/orderlist";
                    }else if (res.err_msg == "get_brand_wcpay_request:cancel")  {
                        alert("支付过程中用户取消");
                    }else{
                        //支付失败
                        alert(res.err_msg)
                    }
                }
        );
    }

</script>
<div style="display: none" id="alipay"></div>
<script src="//cdn.bootcss.com/jquery-weui/1.0.1/js/jquery-weui.min.js"></script>
</body>
</html>
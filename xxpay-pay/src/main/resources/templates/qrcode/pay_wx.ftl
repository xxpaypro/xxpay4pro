<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>正在支付中...</title>
</head>
<body>
<script>

    if (typeof WeixinJSBridge == "undefined"){
        if( document.addEventListener ){
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        }else if (document.attachEvent){
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    }

    function onBridgeReady(){

        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', ${payParams!},
                function(res){
                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                        alert("付款成功");
                        window.close();
                    }else if (res.err_msg == "get_brand_wcpay_request:cancel")  {
                        alert("支付过程中用户取消");
                    }else{ //支付失败
                        alert(JSON.stringify(res));
                    }
                }
        );
    }

    var state = '${state!}';
    var msg = '${msg!}';

    if(state != 'ok'){
        alert(msg);
    }else{
        onBridgeReady();
    }

</script>
</body>
</html>
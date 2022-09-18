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

    // 由于js的载入是异步的，所以可以通过该方法，当AlipayJSBridgeReady事件发生后，再执行callback方法
    function ready(callback) {
        if (window.AlipayJSBridge) {
            callback && callback();
        } else {
            document.addEventListener('AlipayJSBridgeReady', callback, false);
        }
    }

    function tradePay(tradeNO) {

        alert(tradeNO);
        ready(function(){
            // 通过传入交易号唤起快捷调用方式(注意tradeNO大小写严格)
            AlipayJSBridge.call("tradePay", {
                tradeNO: tradeNO
            }, function (data) {
                if ("9000" == data.resultCode) {
                    alert("支付成功");
                }
            });
        });
    }

    window.onload = function(){
        tradePay("${alipayTradeNo!}");  // 页面载入完成后即唤起收银台
    };

</script>
</body>
</html>
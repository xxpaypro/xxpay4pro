<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>收银台|聚合支付</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link href="/css/cashier.css" rel="stylesheet">
</head>
<body>
<div style="display: none" id="formJump"></div>
<div style="width: 1000px; margin: 0 auto">
    <ul class="layui-nav" lay-filter="">
        <li class="layui-nav-item" style="font-size: 18px;">收 银 台</li>
    </ul>

    <div class="layui-card">
        <div class="layui-card-header"><span style="color: blue">您的订单已提交,请尽快完成支付,预期订单将取消!</span></div>
        <div class="layui-card-body" style="padding:10px; background-color: #f6fbff">
            <div class="layui-row">
                <div class="layui-col-xs6">
                    <ul style="line-height: 28px;">
                        <li>订单金额：<span style="color: red; font-size: 24px;">${amountStr!}</span> 元</li>
                        <li>收款方：${mchName!}</li>
                    </ul>
                </div>
                <div class="layui-col-xs6">
                    <ul style="line-height: 28px;">
                        <li>订单标题：${subject!}</li>
                        <li>订单编号：${mchOrderNo!}</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="layui-card">
        <div class="layui-tab layui-tab-card">
            <ul class="layui-tab-title">
                <#if plat??>
                    <li class="layui-this">平台支付</li>
                </#if>
                <#if bankExtra??>
                    <#if plat??>
                        <li >网银支付</li>
                    <#else>
                        <li class="layui-this">网银支付</li>
                    </#if>
                </#if>
            </ul>
            <div class="layui-tab-content" style="padding-left: 30px; padding-bottom: 10px; padding-right: 15px;">
                <#if plat??>
                <div class="layui-tab-item layui-show">
                <form class="layui-form layui-form-pane" action="">
                    <input type="hidden" id="platProductId" value="">
                    <div class="layui-form-item">
                        <dl id="platProductList" class="pay-list">
                            <dd>
                                <#list plat as pro>
                                    <a data-id="${pro.id}" ><span><img src="/images/img_${pro.payType}.png" alt="${pro.productName}"></span></a>
                                </#list>
                            </dd>
                        </dl>
                    </div>
                    <div class="layui-form-item">
                        <button style="margin-top: 15px;" type="button" id="plat_pay_btn" class="layui-btn" lay-submit lay-filter="plat_cashier">前往支付</button>
                    </div>
                </form>
                </div>
                </#if>
                <#if bankExtra??>
                    <#if plat??>
                        <div class="layui-tab-item">
                    <#else>
                        <div class="layui-tab-item layui-show">
                    </#if>
                    <form class="layui-form layui-form-pane" action="">
                        <input type="hidden" id="bankCode" value="">
                        <div class="layui-form-item">
                            <dl id="bankCodeList" class="pay-list">
                                <dd >
                                    <#list bankExtra as bank>
                                        <a class="aborder" data-code="${bank.code}"><span><img src="/images/bank/${bank.bank}.png" alt="${bank.name}"></span></a>
                                    </#list>
                                </dd>
                            </dl>
                        </div>
                        <div class="layui-form-item">
                            <button style="margin-top: 15px;"  type="button" id="bank_pay_btn" class="layui-btn" lay-submit lay-filter="bank_cashier">前往支付</button>
                        </div>
                    </form>
                </div>
                </#if>
            </div>
        </div>

        <fieldset class="layui-elem-field layui-field-title" style="margin-top: 50px;">
            <legend>支付常见问题</legend>
        </fieldset>
        <div class="layui-collapse" lay-accordion="">
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">如何使用快捷支付付款？</h2>
                <div class="layui-colla-content layui-show">
                    <p>首次开通快捷支付时,需要填写您办理银行卡的身份信息和预留手机号码,信息通过验证后,通过您接收到的短信验证码进行开通和支付开通成功后,下次可凭支付密码和短信验证码进行支付.
                    </p>
                </div>
            </div>
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">手机已经更换,怎么更新手机号码？</h2>
                <div class="layui-colla-content">
                    <p>您需要现在银行变更手机号码</p>
                </div>
            </div>
            <div class="layui-colla-item">
                <h2 class="layui-colla-title">支付订单一直处理中怎么办？</h2>
                <div class="layui-colla-content">
                    <p>如遇订单处理中,如您确保已经支付成功,则等待10分钟.如果10分钟后还是处理中,请联系客服,请勿重复支付.</p>
                </div>
            </div>
        </div>
    </div>
    <div style="margin-top: 20px;text-align: center">©2018 聚合支付</div>
</div>



<script src="/layui/layui.js"></script>
<script>
    //注意：导航 依赖 element 模块，否则无法进行功能性操作
    layui.use(['element', 'form'], function(){
        var form = layui.form
                ,$ = layui.$
                ,element = layui.element
                ,layer = layui.layer ;

        form.on('submit(bank_cashier)', function(data) {
            var bankCode = $("#bankCode").val();
            if(bankCode == '' || bankCode == undefined) {
                layer.alert("请选择银行",{title: '提示'});
                return;
            }
            var productId = '${bank.id!}';
            if(productId == '') {
                layer.alert("请选择支付产品",{title: '提示'});
                return;
            }

            $("#bank_pay_btn").attr('class', "layui-btn layui-btn-disabled");  // 按钮不可用
            var load = layer.msg('正在转向银行...', {
                icon: 16
                ,shade: 0.01
                ,time: 9999999999
            });

            //这里可以写ajax方法提交表单
            $.ajax({
                type: "POST",
                url: '/api/cashier/pc_pay',
                data: {
                    mchId: '${mchId!}',
                    appId: '${appId!}',
                    mchOrderNo : '${mchOrderNo!}',
                    productId: productId,
                    subject: '${subject!}',
                    body: '${body!}',
                    notifyUrl: '${notifyUrl!}',
                    amount: '${amount!}',
                    bankCode: bankCode,
                    payPassageAccountId: '${payPassageAccountId!}'
                },
                success: function(res){
                    if(res.code == 0) {
                        var data = res.data;
                        var payMethod = data.payParams.payMethod;
                        if(payMethod == 'formJump') {
                            var payUrl = data.payParams.payUrl;
                            $('#formJump').html(payUrl);
                        }else if(payMethod == 'codeImg') {
                            var codeImgUrl = data.payParams.codeImgUrl;
                            layer.open({
                                title: "扫描二维码支付",
                                type: 1,
                                anim: 1,
                                content: "<img width='200' height='200' src='" +  codeImgUrl + "' />"
                            });
                        }else {
                            layer.open({
                                title: "支付结果",
                                content: JSON.stringify(data.payParams)
                            });
                        }
                    }else {
                        layer.open({
                            title: "下单失败",
                            content: res.msg
                        });
                    }
                    layer.close(load);
                    $("#bank_pay_btn").attr('class', "layui-btn");  // 按钮恢复可用
                },
                error: function(res){
                    layer.close(load);
                    $("#bank_pay_btn").attr('class', "layui-btn");  // 按钮恢复可用
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

        form.on('submit(plat_cashier)', function(data) {
            var productId = $("#platProductId").val();
            if(productId == '' || productId == undefined) {
                layer.alert("请选择支付产品",{title: '提示'});
                return;
            }

            $("#bank_plat_btn").attr('class', "layui-btn layui-btn-disabled");  // 按钮不可用
            var load = layer.msg('正在请求支付...', {
                icon: 16
                ,shade: 0.01
                ,time: 9999999999
            });

            //这里可以写ajax方法提交表单
            $.ajax({
                type: "POST",
                url: '/api/cashier/pc_pay',
                data: {
                    mchId: '${mchId!}',
                    appId: '${appId!}',
                    mchOrderNo : '${mchOrderNo!}',
                    productId: productId,
                    subject: '${subject!}',
                    body: '${body!}',
                    notifyUrl: '${notifyUrl!}',
                    amount: '${amount!}'
                },
                success: function(res){
                    if(res.code == 0) {
                        var data = res.data;
                        var payMethod = data.payParams.payMethod;
                        if(payMethod == 'formJump') {
                            var payUrl = data.payParams.payUrl;
                            $('#formJump').html(payUrl);
                        }else if(payMethod == 'codeImg') {
                            var codeImgUrl = data.payParams.codeImgUrl;
                            layer.open({
                                title: "扫描二维码支付",
                                type: 1,
                                anim: 1,
                                content: "<img width='200' height='200' src='" +  codeImgUrl + "' />"
                            });
                        }else {
                            layer.open({
                                title: "支付结果",
                                content: JSON.stringify(data.payParams)
                            });
                        }
                    }else {
                        layer.open({
                            title: "下单失败",
                            content: res.msg
                        });
                    }
                    layer.close(load);
                    $("#bank_plat_btn").attr('class', "layui-btn");  // 按钮恢复可用
                },
                error: function(res){
                    layer.close(load);
                    $("#bank_plat_btn").attr('class', "layui-btn");  // 按钮恢复可用
                }
            });
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

        form.render();

        // 选择网银银行
        $("#bankCodeList dd a").on("click", function(){
            $("#bankCode").val($(this).attr("data-code"));
            $.each($("#bankCodeList dd a"), function(i, n){
                $(this).attr("style", "border-color: #e2e2e2");
            });
            $(this).attr("style", "border-color: #35a7ff");
        });

        // 选择网银银行
        $("#platProductList dd a").on("click", function(){
            $("#platProductId").val($(this).attr("data-id"));
            $.each($("#platProductList dd a"), function(i, n){
                $(this).attr("style", "border-color: #e2e2e2");
            });
            $(this).attr("style", "border-color: #35a7ff");
        });

    });

</script>

</body>
</html>
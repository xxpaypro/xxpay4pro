<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!--#include file="../_Config.asp" -->
<!--#include file="../_Utils.asp" -->
<%
Session.CodePage=65001

Amount = Request("amount")
AppId = Request("appId")
ProductId = Request("productId")
MchOrderNo = Request("mchOrderNo")
Extra = Request("extra")
ChannelUserId = Request("channelUserId")

IF IsEmpty(Amount) Or IsEmpty(ProductId) Or IsEmpty(MchOrderNo) Then
	response.Write("丢失参数")
	response.end()
End If

Amount = Amount * 100  '元转换为分

Dim ParamsDict
set ParamsDict=CreateObject("Scripting.Dictionary")

ParamsDict.Add "mchId", MchId   '商户ID
ParamsDict.Add "appId", AppId   '商户应用ID
ParamsDict.Add "productId", ProductId   '支付产品ID
ParamsDict.Add "mchOrderNo", MchOrderNo   '商户订单号
ParamsDict.Add "currency", "cny"   '币种
ParamsDict.Add "amount", Amount   '支付金额
ParamsDict.Add "clientIp", "210.73.10.148"   '客户端IP
ParamsDict.Add "device", "ios10.3.1"   '客户端设备
ParamsDict.Add "returnUrl", "http://localhost/html/return_page.html"   '支付结果前端跳转URL
ParamsDict.Add "notifyUrl", "http://localhost/api/pay/Notify.asp"   '支付结果后台回调URL
ParamsDict.Add "subject", "网络购物"   '商品主题
ParamsDict.Add "body", "网络购物"   '商品描述信息
ParamsDict.Add "param1", ""   '扩展参数1
ParamsDict.Add "param2", ""   '扩展参数2
ParamsDict.Add "extra", Extra   '附加参数
ParamsDict.Add "channelUserId", ChannelUserId   '渠道用户ID

'Md5加密，生成摘要信息
Sign=ParamsDictSign(ParamsDict, MchKey)
ParamsDict.Add "sign", Sign

'请求支付网关并将数据原样返回页面
response.Write PostHTTPPage( (PayHost & "/api/pay/create_order" ), GetUrlStr(ParamsDict))

%>

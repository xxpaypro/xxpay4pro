<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!--#include file="../_Config.asp" -->
<!--#include file="../_Utils.asp" -->
<%
Session.CodePage=65001

Amount = Request("amount")
MchOrderNo = Request("mchOrderNo")
MchRefundNo = Request("mchRefundNo")

IF IsEmpty(Amount) Or IsEmpty(MchOrderNo) Or IsEmpty(MchRefundNo) Then
	response.Write("丢失参数")
	response.end()
End If

Amount = Amount * 100  '元转换为分

Dim ParamsDict
set ParamsDict=CreateObject("Scripting.Dictionary")

ParamsDict.Add "mchId", MchId   '商户ID
ParamsDict.Add "mchOrderNo", MchOrderNo   '支付订单-商户订单号
ParamsDict.Add "mchRefundNo", MchRefundNo   '商户退款单号
ParamsDict.Add "amount", Amount   '支付金额
ParamsDict.Add "currency", "cny"   '币种
ParamsDict.Add "clientIp", "210.73.10.148"   '客户端IP
ParamsDict.Add "device", "ios10.3.1"   '客户端设备
ParamsDict.Add "extra", ""   '附加参数
ParamsDict.Add "param1", ""   '扩展参数1
ParamsDict.Add "param2", ""   '扩展参数2

'如果notifyUrl 不为空表示异步退款，具体退款结果以退款通知为准
ParamsDict.Add "notifyUrl", ""   '同步处理
'ParamsDict.Add "notifyUrl", "http://localhost/api/refund/Notify.asp"   '异步处理

ParamsDict.Add "channelUser", ""   '渠道用户标识,如微信openId,支付宝账号
ParamsDict.Add "userName", ""   '用户姓名
ParamsDict.Add "remarkInfo", "用户退款"   '备注


'Md5加密，生成摘要信息
Sign=ParamsDictSign(ParamsDict, MchKey)
ParamsDict.Add "sign", Sign

'请求支付网关并将数据原样返回页面
response.Write PostHTTPPage( (PayHost & "/api/refund/create_order" ), GetUrlStr(ParamsDict))

%>

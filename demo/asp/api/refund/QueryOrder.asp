<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!--#include file="../_Config.asp" -->
<!--#include file="../_Utils.asp" -->
<%
Session.CodePage=65001

MchRefundOrder = Request("mchRefundOrder")
IF IsEmpty(MchRefundOrder) Then
	response.Write("丢失参数")
	response.end()
End If


'签名串 示例传参，实际项目请按照字典值排序
Dim ParamsDict
set ParamsDict=CreateObject("Scripting.Dictionary")

ParamsDict.Add "mchId", MchId   '商户ID
ParamsDict.Add "appId", ""   '应用ID
ParamsDict.Add "mchRefundNo", MchRefundOrder   '商户退款单号
ParamsDict.Add "refundOrderId", ""   '支付中心退款订单号
ParamsDict.Add "executeNotify", "false"   ' 是否执行回调

'Md5加密，生成摘要信息
Sign=ParamsDictSign(ParamsDict, MchKey)
ParamsDict.Add "sign", Sign

'请求支付网关并将数据原样返回页面
response.Write PostHTTPPage( (PayHost & "/api/refund/query_order" ), GetUrlStr(ParamsDict))

%>

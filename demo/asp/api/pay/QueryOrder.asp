<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!--#include file="../_Config.asp" -->
<!--#include file="../_Utils.asp" -->
<%
Session.CodePage=65001

MchOrderNo = Request("mchOrderNo")
IF IsEmpty(MchOrderNo) Then
	response.Write("丢失参数")
	response.end()
End If


'签名串 示例传参，实际项目请按照字典值排序
Dim ParamsDict
set ParamsDict=CreateObject("Scripting.Dictionary")

ParamsDict.Add "mchId", MchId   '商户ID
ParamsDict.Add "appId", ""   '应用ID
ParamsDict.Add "payOrderId", ""   '支付中心生成的订单号，与mchOrderNo二者传一即可
ParamsDict.Add "mchOrderNo", MchOrderNo   '商户生成的订单号，与payOrderId二者传一即可
ParamsDict.Add "executeNotify", "false"   ' 是否执行回调

'Md5加密，生成摘要信息
Sign=ParamsDictSign(ParamsDict, MchKey)
ParamsDict.Add "sign", Sign

'请求支付网关并将数据原样返回页面
response.Write PostHTTPPage( (PayHost & "/api/pay/query_order" ), GetUrlStr(ParamsDict))

%>

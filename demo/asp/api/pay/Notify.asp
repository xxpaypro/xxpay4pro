<%@LANGUAGE="VBSCRIPT" CODEPAGE="65001"%>
<!--#include file="../_Config.asp" -->
<!--#include file="../_Utils.asp" -->

<%
Session.CodePage=65001

Sign = Request("sign")
IF IsEmpty(Sign) Then
	response.Write("fail(sign not exists)")
	response.end()
End If

Dim ParamsDict
set ParamsDict=CreateObject("Scripting.Dictionary")

ParamsDict.Add "payOrderId", Request("payOrderId")
ParamsDict.Add "mchId", Request("mchId")
ParamsDict.Add "appId", Request("appId")
ParamsDict.Add "productId", Request("productId")
ParamsDict.Add "mchOrderNo", Request("mchOrderNo")
ParamsDict.Add "amount", Request("amount")
ParamsDict.Add "status", Request("status")
ParamsDict.Add "channelOrderNo", Request("channelOrderNo")
ParamsDict.Add "channelAttach", Request("channelAttach")
ParamsDict.Add "param1", Request("param1")
ParamsDict.Add "param2", Request("param2")
ParamsDict.Add "paySuccTime", Request("paySuccTime")
ParamsDict.Add "backType", Request("backType")
ParamsDict.Add "income", Request("income")

DictSign=ParamsDictSign(ParamsDict, MchKey)

IF DictSign=Sign then

	'处理业务...
	
	'向支付网关 返回“success”
	response.Write "success"
ELSE
	response.Write "fail(verify fail)"
End IF 

response.end

%>

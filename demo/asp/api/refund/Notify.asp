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

ParamsDict.Add "refundOrderId", Request("refundOrderId")
ParamsDict.Add "mchId", Request("mchId")
ParamsDict.Add "appId", Request("appId")
ParamsDict.Add "mchRefundNo", Request("mchRefundNo")
ParamsDict.Add "refundAmount", Request("refundAmount")
ParamsDict.Add "status", Request("status")
ParamsDict.Add "channelOrderNo", Request("channelOrderNo")
ParamsDict.Add "param1", Request("param1")
ParamsDict.Add "param2", Request("param2")
ParamsDict.Add "refundSuccTime", Request("refundSuccTime")
ParamsDict.Add "backType", Request("backType")

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

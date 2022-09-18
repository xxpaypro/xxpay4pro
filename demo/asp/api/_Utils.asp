<%

'Dict 转换为url参数格式
function GetUrlStr(ParamsDict)

KeyArray=ParamsDict.keys
UrlStr = ""

For I = Lbound(KeyArray) to Ubound(KeyArray)
	IF I=Ubound(KeyArray) Then
		UrlStr = UrlStr & KeyArray(I) & "=" & ParamsDict(KeyArray(I))
	ELSE
		UrlStr = UrlStr & KeyArray(I) & "=" & ParamsDict(KeyArray(I)) & "&"
	End If
Next
GetUrlStr=UrlStr
End function


'根据Dictionary 排序并计算签名
function ParamsDictSign(ParamsDict, MchKey)

SortKeys=Sort(ParamsDict.Keys) 'key按照字典排序
signStr = ""

For I = Lbound(SortKeys) to Ubound(SortKeys)
	IF (""=ParamsDict(SortKeys(I))) Then
	ELSE
		signStr = signStr & SortKeys(I) & "=" & ParamsDict(SortKeys(I)) & "&"
	End If
Next

signStr = signStr & "key=" & MchKey
ParamsDictSign=MD5(signStr)

End function

'HTTP POST请求
function PostHTTPPage(url, data) 
dim Http 
set Http=server.createobject("MSXML2.SERVERXMLHTTP.3.0")
Http.open "POST",url,false 
Http.setRequestHeader "CONTENT-TYPE", "application/x-www-form-urlencoded" 
Http.send(data) 
if Http.readystate<>4 then 
exit function 
End if
PostHTTPPage=bytesToBSTR(Http.responseBody,"utf-8") 
set http=nothing 
if err.number<>0 then err.Clear 
End function


'MD5
Function MD5(text)
	With CreateObject("MSXML.DOMDocument").createElement("a")
		.dataType = "bin.hex"
		.nodeTypedvalue = CreateObject("System.Security.Cryptography.MD5CryptoServiceProvider").ComputeHash_2(CreateObject("System.Text.UTF8Encoding").GetBytes_4(text))
		MD5 = UCase(.text)
	End With
End Function 


'数组 字典排序
function Sort(ary)
    KeepChecking = TRUE
    Do Until KeepChecking = FALSE
    KeepChecking = FALSE
        For I = 0 to UBound(ary)
            If I = UBound(ary) Then Exit For
                If ary(I) > ary(I+1) Then
                    FirstValue = ary(I)
                SecondValue = ary(I+1)
                ary(I) = SecondValue
                ary(I+1) = FirstValue
                KeepChecking = TRUE
            End If
        Next
    Loop
    Sort = ary
End function

function BytesToBstr(body,Cset) 
dim objstream 
set objstream = Server.CreateObject("adodb.stream")
objstream.Type = 1 
objstream.Mode =3 
objstream.Open 
objstream.Write body 
objstream.Position = 0 
objstream.Type = 2 
objstream.Charset = Cset 
BytesToBstr = objstream.ReadText 
objstream.Close 
set objstream = nothing 
End function

'获取当前yyyyMMddHHmmss格式的时间字符串
function GetNowDateStr()
GetNowDateStr=now()
GetNowDateStr=replace(GetNowDateStr,"-","")
GetNowDateStr=replace(GetNowDateStr,":","")
GetNowDateStr=replace(GetNowDateStr," ","")
GetNowDateStr=replace(GetNowDateStr,"/","")

end function


%>
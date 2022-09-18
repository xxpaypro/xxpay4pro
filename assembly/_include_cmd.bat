@echo off
rem PAUSE

if "%APP_HOME%"=="" (
	echo "<<<<<<<<<<<ERROR: 请正确配置[APP_HOME]参数并执行app.bat<<<<<<<<<<<<<"
	PAUSE
	exit
)

if "%APP_MAINCLASS%"=="" (
	echo "<<<<<<<<<<<ERROR: 请正确配置[APP_MAINCLASS]参数并执行app.bat<<<<<<<<<<<<<"
	PAUSE
	exit
)

rem 切换编码为utf-8,避免日志乱码
chcp 65001

rem 指定cmd窗口名称
title %WINDOW_TITLE%

rem java虚拟机启动参数 -Xms：初始值 -Xmx：最大值  -Xmn：堆内新生代的大小
set JAVA_OPTS=-Xms512m -Xmx1024m -Xmn128m -Djava.awt.headless=true -XX:MaxPermSize=64m

rem 指定CLASSPATH
rem CLASSPATH变量指定太多jar文件会出现“输入行太长。”的错误。解决办法为直接指定到lib目录通过 \*指定所有jar
set CLASSPATH=%APP_HOME%\classes;%APP_HOME%\lib\*

goto doStart

:doStart
java %JAVA_OPTS% -classpath "%CLASSPATH%" %APP_MAINCLASS%
:end

PAUSE
exit
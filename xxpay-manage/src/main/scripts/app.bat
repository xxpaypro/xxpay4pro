@echo off
rem PAUSE
rem �ýű�ΪWindows������java�����ͨ�ýű�, ����bin�ļ��в�˫��app.bat�ű�������
rem ��Ҫ��ϵͳ���������JAVA_HOME��������������path�����%JAVA_HOME%/bin;

rem ��ʾcmd���ڵ�����
set WINDOW_TITLE=xxpay-manage

rem ����Java�������ڵ�Ŀ¼��classes����һ��Ŀ¼��
rem set APP_HOME=E:\apps\xxpay-manage
rem ����ǰĿ¼����һ��Ŀ¼��Ҳ�ɰ�������ʽָ��������·����
set APP_HOME=%cd%/../

rem ��Ҫ������Java������main�����ࣩ
set APP_MAINCLASS=org.xxpay.manage.bootstrap.XxPayManageApplication

rem ���������ű�
start _include_cmd.bat
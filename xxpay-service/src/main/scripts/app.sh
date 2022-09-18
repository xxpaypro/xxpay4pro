#!/bin/sh
#
#该脚本为Linux下启动java程序的通用脚本。即可以作为开机自启动service脚本被调用，
#也可以作为启动java程序的独立脚本来使用。
#

#Java程序所在的相对目录名称
APP_DIR_NAME=xxpay-service

#需要启动的Java主程序（main方法类）
APP_MAINCLASS=org.xxpay.service.bootstrap.XxPayServiceAppliaction

#引入并执行  shell
. ./_include_shell.sh
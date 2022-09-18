package org.xxpay.service.test.generator.utils;

import java.io.*;

/**
 * @Author terrfly
 * @Date 2019/6/1 10:08
 * @Description 文件IO处理
 **/
public class FileKit {

    /** 常用字符集 **/
    public static final String CHARSET_UTF8 = "UTF-8";
    /**
     * 递归该文件下所有文件进行处理
     * 建议： processFile方法中自行包裹最大的try catch, 让其他文件继续处理, 当不捕捉异常时该异常将向上抛出
     * QA: 当查找到了某值, 不继续遍历如何操作? 1. 方法中抛异常, 2. 更改工具类源码使得回调函数返回boolean类型 ,当false时直接return
     * @param fileOrDir 文件或者目录
     * @param callBack 处理事件
     * @throws Exception
     */
    public static void handleFiles(File fileOrDir, HandleFilesCallback callBack) throws Exception {

        if(!fileOrDir.exists()) throw new FileNotFoundException(fileOrDir.getPath() + "is not a file or dir.");

        if(fileOrDir.isFile()){
            callBack.processFile(fileOrDir);
            return ;
        }

        for(File f: fileOrDir.listFiles()){
            handleFiles(f, callBack);
        }
    }

    /** handleFiles方法重载， 支持传入文件路径 **/
    public static void handleFiles(String fileOrDirPath, HandleFilesCallback callBack) throws Exception {
        handleFiles(new File(fileOrDirPath), callBack);
    }


    /** 打开文件, 按行读取 **/
    public static void openFileReadLines(File textFile, String charset, OpenFileReadLinesCallback callback) throws Exception {

        FileReader fileReader = new FileReader(textFile);
        BufferedReader br = null;
        try {
            br = new BufferedReader(fileReader);
            String lineStr ;
            while((lineStr = br.readLine()) != null){
                callback.readLine(new String(lineStr.getBytes(), charset));
            }
        } catch (Exception e) {
            throw e;
        }finally { //关闭文件流
            if(br != null) br.close();
            if(fileReader != null) fileReader.close();
        }
    }


    /** 将指定内容写入 到对应的文件中  **/
    public static void writeFile(File needWriteFile, String charset, String writeText) throws Exception {

        FileOutputStream fos = new FileOutputStream(needWriteFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
        BufferedWriter writer = new BufferedWriter (osw);

        try {
            writer.write(writeText);
            writer.flush();

        }catch (Exception e){
            throw e;
        }finally {
            if(writer != null) writer.close();
            if(osw != null) writer.close();
            if(fos != null) writer.close();
        }
    }


    /** ------- 以下为回调函数定义 ---------- **/

    /** 文件处理回调 **/
    public interface HandleFilesCallback{
        void processFile(File file) throws Exception;
    }

    /** 读取文件每行回调 **/
    public interface OpenFileReadLinesCallback{
        void readLine(String lineStr) throws Exception;
    }
}

package org.xxpay.service.test.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis 代码生成器
 * 说明：
 * 1. 首次使用请先配置 mybatisCodeGenerator.xml文件, 一般仅修改数据库连接串, 用户, 密码, 表名即可；
 * 2. 通过IDE run MybatisCodeGenerator即可自动生成对应表的model,mapper等代码， 生成目录：[org/xxpay/service/test/generator/output/]
 * 3. 每次使用后删除[org/xxpay/service/test/generator/output/]中的文件（不要删除output文件）， 避免程序编译报错；
 * 4. 由于项目已经变更为mybaits plus框架, 建议:
 *    a. 存量表的Mapper使用MybatisCodeGenerator 生成后仅使用 *Mapper.xml文件， 其他类使用MybatisPlusCodeGenerator文件替换;
 *    b. 全新表使用MybatisPlusCodeGenerator生成， 不在考虑MybatisCodeGenerator。
 */
public class MybatisCodeGenerator {
    public static void main(String[] args) {

        List<String> warnings = new ArrayList<>();
        try {

            String xmlPath = System.getProperty("user.dir");
            xmlPath += "/xxpay4new/xxpay-service/src/test/java/org/xxpay/service/test/generator/mybatisCodeGenerator.xml";
            System.out.println("加载配置文件===" + xmlPath);

            boolean overwrite = true;
            File configFile = new File(xmlPath);
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(configFile);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            ProgressCallback progressCallback = new VerboseProgressCallback();
            myBatisGenerator.generate(progressCallback);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //打印warning 信息
        for (String warning : warnings) {
            System.out.println(warning);
        }

    }

}
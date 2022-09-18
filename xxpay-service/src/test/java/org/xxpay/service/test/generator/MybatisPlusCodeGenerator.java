package org.xxpay.service.test.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.xxpay.service.test.generator.utils.FileKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author terrfly
 * @Date 2019/8/14 11:31
 * @Description
 * mybatis plus 代码生成器
 * 说明：
 * 1. 首次使用请先配置 MybatisPlusCodeGenerator中的常量, 一般仅修改数据库连接串, 用户, 密码, 表名即可；
 * 2. 通过IDE run MybatisPlusCodeGenerator即可自动生成对应表的model,mapper等代码， 生成目录：[org/xxpay/service/test/generator/output/]
 * 3. 每次使用后删除[org/xxpay/service/test/generator/output/]中的文件（不要删除output文件）， 避免程序编译报错；
 **/
public class MybatisPlusCodeGenerator {

    private static final String DB_URL = "jdbc:mysql://192.168.3.101:3306/ncpay?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USERNAME = "nc";
    private static final String DB_PASSWORD = "abcd1234";
    private static final String AUTHOR = "xxpay generator"; //作者注释

    public static final String[] GENERATOR_TABLE_NAMES = new String[]{  //生成的表名, 数组形式
                    "t_mch_trade_order_batch"
    };

    public static void main(String[] args) throws Exception {

        AutoGenerator mpg = new AutoGenerator();  //创建代码生成器对象

        //全局配置
        GlobalConfig gc = new GlobalConfig();
        String userDir = System.getProperty("user.dir"); //User's current working directory
        String outputDir = userDir + "/xxpay-service/src/test/java/org/xxpay/service/test/generator/output/";
        gc.setOutputDir(outputDir);
        gc.setAuthor(AUTHOR);  //作者
        gc.setOpen(false); //自动打开目录
        gc.setFileOverride(true); //覆写已生成的文件
        gc.setDateType(DateType.ONLY_DATE); //使用 java.util.Date
        gc.setBaseResultMap(true); //自动生成BaseResultMap
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(DB_USERNAME);
        dsc.setPassword(DB_PASSWORD);

        // 自定义类型转换器, 将tinyint类型转换为 Byte类型, 其余类型参考MySqlTypeConvert
        ITypeConvert myTypeConvert = (globalConfig, fieldType) -> {
            String t = fieldType.toLowerCase();
            if (t.contains("tinyint(1)")) {
                return DbColumnType.BOOLEAN;
            }else if (t.contains("tinyint")) {
                return DbColumnType.BYTE;
            }else{
                return new MySqlTypeConvert().processTypeConvert(globalConfig, fieldType);
            }
        };
        dsc.setTypeConvert(myTypeConvert);
        mpg.setDataSource(dsc);

        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("org.xxpay"); //所有包名的公共部分
        pc.setEntity("core.entity");
        pc.setMapper("service.dao.mapper");
        pc.setService("core.service");
        pc.setServiceImpl("service.impl");
        pc.setController("manage.ctrl");
        pc.setXml("service.dao.mapper");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String outFileName = outputDir + pc.getParent().replaceAll("\\.", "/") + "/" +
                        pc.getXml().replaceAll("\\.", "/") + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;

                return outFileName;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true); //数据库表名, 列名称为大写模式， generator将自动toLowerCase
        strategy.setNaming(NamingStrategy.underline_to_camel);  //no_change:不做任何改变，原样输出, underline_to_camel:下划线转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("org.xxpay.core.entity.BaseModel"); //自定义继承的Entity类全称，带包名
        strategy.setRestControllerStyle(true);  //生成 @RestController 控制器
        // 公共父类
        strategy.setSuperControllerClass("org.xxpay.manage.common.BaseController"); //继承父类Controller
        // 写于父类中的公共字段
        strategy.setInclude(GENERATOR_TABLE_NAMES);   //表名，多个英文逗号分割
        strategy.setControllerMappingHyphenStyle(true);     //驼峰转连字符
        strategy.setTablePrefix("t_");  //表前缀
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();

        //mybatis plus 的generator如果遇到数据库字段为 MchId, 则会直接将字段写为MchId,
        //以下为处理上述问题， 进行查找并处理替换关键字
        String modelPath = outputDir + pc.getParent().replaceAll("\\.", "/") + "/" +
                pc.getEntity().replaceAll("\\.", "/");

        final Map<String, String> fieldNameMap = new HashMap<>();

        FileKit.handleFiles(modelPath, file -> {
            FileKit.openFileReadLines(file, FileKit.CHARSET_UTF8, str -> {
                String strTrim = str.trim();
                String[] splitArr = strTrim.split(" ");
                if(strTrim.startsWith("private") && splitArr.length == 3){ //private 开头 && 数组3

                    String fieldName = splitArr[2].substring(0, splitArr[2].length() -1); //去掉;
                    String newFieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                    fieldNameMap.put(" " + fieldName, " " + newFieldName);
                }
            });
        });

        FileKit.handleFiles(modelPath, file -> {

            StringBuilder fileText = new StringBuilder();
            FileKit.openFileReadLines(file, FileKit.CHARSET_UTF8, str -> {

                String strTrim = str.trim();
                String newLineText = str;
                for(String field: fieldNameMap.keySet()){

                    if(strTrim.indexOf("@") >= 0) continue;
                    if(strTrim.startsWith("import")) continue;

                    else if(strTrim.indexOf("public") >= 0  && (strTrim.indexOf("get" + field) >= 0  || strTrim.indexOf("set" + field) >= 0) ){
                        newLineText = newLineText.replace(field + ")", fieldNameMap.get(field) + ")");
                    } else if(strTrim.indexOf("this.") >= 0 ){
                        newLineText = newLineText.replace("this." + field.substring(1), "this." + fieldNameMap.get(field).substring(1));
                        newLineText = newLineText.replace(field, fieldNameMap.get(field));
                    }else{
                        newLineText = newLineText.replace(field, fieldNameMap.get(field));
                    }
                }
                fileText.append(newLineText).append("\n");
            });
            FileKit.writeFile(file, FileKit.CHARSET_UTF8, fileText.toString());
        });


        //将mapper.xml中的property 首字母改为小写形式；
        String xmlPath = outputDir + pc.getParent().replaceAll("\\.", "/") + "/" +
                pc.getXml().replaceAll("\\.", "/");

        FileKit.handleFiles(xmlPath, file -> {
            StringBuilder fileText = new StringBuilder();
            FileKit.openFileReadLines(file, FileKit.CHARSET_UTF8, str -> {

                String newLineText = str;
                String strTrim = str.trim();
                if(strTrim.startsWith("<result column=")){ //<result column=

                    int propIndex = str.indexOf("property=\"");
                    newLineText = str.substring(0, propIndex);
                    newLineText += "property=\"" + str.substring(propIndex + 10, propIndex + 11).toLowerCase();
                    newLineText += str.substring(propIndex + 11);
                }
                fileText.append(newLineText).append("\n");
            });
            FileKit.writeFile(file, FileKit.CHARSET_UTF8, fileText.toString());
        });

    }

}
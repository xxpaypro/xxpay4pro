package org.xxpay.manage.common.ctrl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.xxpay.core.common.ctrl.AbstractController;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.manage.common.config.MainConfig;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
public abstract class BaseController extends AbstractController {

    @Autowired
    protected MainConfig mainConfig;

    /**
     * 输出excel数据
     * @param fileName
     * @param data
     */
    protected void writeExcelStream(String fileName, List<List> data) {

        try {
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xlsx").getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
            response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型

            Workbook workbook = new XSSFWorkbook(); //生成excel 2007格式
            Sheet sheet = workbook.createSheet();

            for(int i = 0; i < data.size(); i++){
                Row row = sheet.createRow(i);

                for(int j = 0; j< data.get(i).size(); j++){
                    Cell cell = row.createCell(j);
                    if(data.get(i).get(j) != null) {
                        cell.setCellValue(data.get(i).get(j).toString());
                    }
                }
            }
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("writeExcelStream", e);
        }
    }

}

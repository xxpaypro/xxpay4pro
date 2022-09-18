package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysIndustryCode;
import org.xxpay.core.service.ISysIndustryCodeService;
import org.xxpay.service.dao.mapper.SysIndustryCodeMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 行业编码表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@Service
public class SysIndustryCodeServiceImpl extends ServiceImpl<SysIndustryCodeMapper, SysIndustryCode> implements ISysIndustryCodeService {


    @Override
    public String formatIndustryInfo(Integer industryCode){

        List<SysIndustryCode> nameList = new ArrayList<>();
        recursionFormatIndustryInfo(industryCode, nameList);

        String result = "";
        for(int i = 0; i < nameList.size(); i++){

            result += "[" + nameList.get(i).getIndustryName() + "]";

            if(i != (nameList.size() - 1) ){  //非最后一位
                result += " - ";
            }
        }
        return result;
    }

    @Override
    public String getAllIndustryCode(Integer industryCode){

        List<SysIndustryCode> list = new ArrayList<>();
        recursionFormatIndustryInfo(industryCode, list);

        String result = "";
        for(int i = 0; i < list.size(); i++){

            result += list.get(i).getIndustryCode();

            if(i != (list.size() - 1) ){  //非最后一位
                result += ",";
            }
        }
        return result;
    }


    /** 递归拼接, 按照 一级 / 二级 /三级 放入list中 **/
    private void recursionFormatIndustryInfo(Integer industryCode, List<SysIndustryCode> list){

        SysIndustryCode sysIndustryCode = this.getById(industryCode);
        if(sysIndustryCode == null) return;

        if(sysIndustryCode.getParentCode() == null || sysIndustryCode.getParentCode() == 0 ){  //最顶级
            list.add(sysIndustryCode);
            return ;
        }

        recursionFormatIndustryInfo(sysIndustryCode.getParentCode(), list);
        list.add(sysIndustryCode);
    }


}

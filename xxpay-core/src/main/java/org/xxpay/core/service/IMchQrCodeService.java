package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.MchQrCode;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
public interface IMchQrCodeService extends IService<MchQrCode> {

    List<MchQrCode> select(int pageIndex, int pageSize, MchQrCode mchQrCode);

    int count(MchQrCode mchQrCode);

    MchQrCode findById(Long id);

    MchQrCode find(MchQrCode mchQrCode);

    MchQrCode findByMchIdAndAppId(Long mchId, String appId);

    int add(MchQrCode mchQrCode);

    int update(MchQrCode mchQrCode);

    int delete(Long id);

}

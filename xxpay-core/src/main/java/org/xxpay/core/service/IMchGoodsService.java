package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchGoods;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户商品表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchGoodsService extends IService<MchGoods> {

    /**
     * 商户商品列表
     * @param mchGoods
     * @param page
     * @param sortColumn
     * @param sortType
     * @return
     */
    IPage<MchGoods> list(MchGoods mchGoods, Long outGoodsId, IPage page, Byte sortColumn, Byte sortType, Long floorPrice, Long ceilPrice, String createTimeStart, String createTimeEnd);

    /**
     * 新增商品
     * @param mchGoods
     * @return
     */
    boolean saveMchGoods(MchGoods mchGoods, String storeIds, String propsCategoryIds);

    /**
     * 查询商品关联门店
     * @param goodsId
     * @return
     */
    List<Map> storesList(Long goodsId);

    /**
     * 查询商品关联属性分类
     * @param goodsId
     * @return
     */
    List<Map> propsCategoryList(Long goodsId);

    /**
     * 修改商品
     * @param mchGoods
     * @return
     */
    boolean updateMchGoods(MchGoods mchGoods, String storeIds, String propsCategoryIds);

}

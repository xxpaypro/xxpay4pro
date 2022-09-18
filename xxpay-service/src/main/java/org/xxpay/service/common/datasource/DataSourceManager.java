package org.xxpay.service.common.datasource;

import org.xxpay.service.common.enumm.DataSourceType;

/**
 * 动态数据源管理类
 */
public class DataSourceManager {

    //当前线程 用于存放使用数据源
    private static final ThreadLocal<DataSourceType> CURRENT_DATA_SOURCE = new ThreadLocal<>();

    /**
     * 设置使用数据源
     * @param dataSource
     */
    public static void setDataSource(DataSourceType dataSource){
        CURRENT_DATA_SOURCE.set(dataSource);
    }

    /**
     * 获取当前数据源
     * @return
     */
    public static DataSourceType getCurrentDataSource(){
        return CURRENT_DATA_SOURCE.get();
    }

    /**
     * 清空使用数据源, 使用默认数据源
     */
    public static void clear(){
        CURRENT_DATA_SOURCE.remove();
    }

}

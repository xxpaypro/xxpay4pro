package org.xxpay.mch.secruity;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.SysResource;
import org.xxpay.mch.common.service.RpcCommonService;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class MyInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {

    @Autowired
    private RpcCommonService rpcCommonService;

    private HashMap<String, Collection<ConfigAttribute>> map = null;

    /**
     * 加载资源，初始化资源变量
     */
    public void loadResourceDefine(){
        map = new HashMap<>();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_MCH);
        if(CollectionUtils.isNotEmpty(sysResourceList)) {
            for(SysResource sysResource : sysResourceList) {

                if(StringUtils.isBlank(sysResource.getPermName())) continue;
                cfg = new SecurityConfig(sysResource.getPermName());
                // 此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。
                // 此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。

                // 用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
                // 权限url多个时为逗号分隔,如: mch_info/**,mch_auth/**
                String permUrl = sysResource.getPermUrl();
                String[] permUrls = permUrl.split(",");
                for(String url : permUrls) {

                    //modify by terrfly 20181214 ，解决array只存在一个的问题，应为url对应多个权限ID
                    array = map.get(Constant.MCH_CONTROLLER_ROOT_PATH + url);
                    if(array == null) {
                        array = new ArrayList<>();
                    }

                    array.add(cfg);
                    map.put(Constant.MCH_CONTROLLER_ROOT_PATH + url, array);
                }
            }
        }
    }

    /**
     * 得到访问某个URL所需要的权限集合
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        if(map == null) loadResourceDefine();
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
            resUrl = iter.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
                return map.get(resUrl);
            }
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

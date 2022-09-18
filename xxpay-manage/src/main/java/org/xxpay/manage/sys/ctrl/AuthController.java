package org.xxpay.manage.sys.ctrl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.MenuTreeBuilder;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.RandomValidateCodeUtil;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.SysResource;
import org.xxpay.core.entity.SysUser;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.secruity.JwtAuthenticationRequest;
import org.xxpay.manage.sys.service.SysUserService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RpcCommonService rpcCommonService;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录鉴权
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "登录" )
    public ResponseEntity<?> auth(
            HttpServletResponse response) throws AuthenticationException{
    	

        
        String vercode = getValStringRequired( "vercode"); //验证码
        String vercodeRandomStr = getValStringRequired( "vercodeRandomStr"); //验证码key
        
        String vercodeCacheValue = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_VERCODE_PREFIX_MGR + vercodeRandomStr);
        
        if(!vercode.equals(vercodeCacheValue)){
        	return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_VERCODE_ERROR));
        }
        
        String username = getValStringRequired( "username");
        String password = getValStringRequired( "password");
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(username, password);
        String token;
        try {
           token = sysUserService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
        JSONObject data = new JSONObject();
        data.put("access_token", token);
        
        //登录成功将 验证码清除
        stringRedisTemplate.delete(MchConstant.CACHEKEY_VERCODE_PREFIX_MGR + vercodeRandomStr);
        
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MGR + token, "1",
				MchConstant.CACHE_TOKEN_TIMEOUT, TimeUnit.SECONDS);
        
        
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 修改当前用户登录密码
     * @return
     */
    @RequestMapping("/pwd_update")
    @ResponseBody
    @MethodLog( remark = "修改密码" )
    public ResponseEntity<?> updatePassword() {

        // 旧密码
        String oldRawPassword = getValStringRequired( "oldPassWord");
        // 新密码
        String rawPassword = getValStringRequired( "passWord");
        // 验证旧密码是否正确
        SysUser sysUser = rpcCommonService.rpcSysService.findByUserId(getUser().getUserId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldRawPassword, sysUser.getLoginPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        sysUser = new SysUser();
        sysUser.setUserId(getUser().getUserId());
        sysUser.setLoginPassword(encoder.encode(rawPassword));
        sysUser.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查询当前用户信息
     * @return
     */
    @RequestMapping("/current")
    @ResponseBody
    public ResponseEntity<?> current() {
        SysUser sysUser = sysUserService.findByUserId(getUser().getUserId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysUser));
    }

    /**
     * 查询用户菜单
     * @param request
     * @return
     */
    @RequestMapping("/menu")
    @ResponseBody
    public ResponseEntity<?> menu() {
        List<SysResource> sysResourceList;
        if(MchConstant.PUB_YES == getUser().getIsSuperAdmin()) {
            // 如果是超级管理用户,则得到所有权限资源
            sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.INFO_TYPE_PLAT);
        }else {
            // 得到用户的权限资源
            sysResourceList = rpcCommonService.rpcSysService.selectResourceByUserId(getUser().getUserId());
        }
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            MenuTreeBuilder.Node node = new MenuTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setName(sysResource.getName());
            node.setTitle(sysResource.getTitle());
            if(StringUtils.isNotBlank(sysResource.getJump())) node.setJump(sysResource.getJump());
            if(StringUtils.isNotBlank(sysResource.getIcon())) node.setIcon(sysResource.getIcon());
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(JSONArray.parseArray(MenuTreeBuilder.buildTree(nodeList))));
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/auth/vercode")
    public void getVercode( HttpServletResponse response, String vercodeRandomStr) throws IOException {
    	
    	if(StringUtils.isEmpty(vercodeRandomStr))return ;
        Map<String, Object> randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        String vercodeValue = randomMap.get("randomString").toString(); //码值
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_VERCODE_PREFIX_MGR + vercodeRandomStr, 
        									  vercodeValue, MchConstant.CACHEKEY_VERCODE_TIMEOUT, TimeUnit.MINUTES);
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }

}

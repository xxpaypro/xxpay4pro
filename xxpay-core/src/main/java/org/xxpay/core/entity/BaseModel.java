package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Title: </b>BaseModel.java
 * <p><b>Description: </b>model基类
 * @author terrfly
 * @version V1.0
 * <p>
 */
public abstract class BaseModel<T> extends Model implements Serializable{

	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private Map<String,Object> ps ; //ps = 简化Properties
	public Map<String, Object> getPs() {
		return ps;
	}
	public void setPs(Map<String, Object> ps) {
		this.ps = ps;
	}
	
	public void setPsVal(String key, Object value){
		if(ps == null){
			ps = new HashMap<String, Object>();
		}
		ps.put(key, value);
	}
	
	public Object getPsVal(String key){
		if(ps == null){
			return null;
		}
		return ps.get(key);
	}
	
	public String getPsStringVal(String key){
		if(ps == null){
			return null;
		}
		return (String)ps.get(key);
	}
	
	public Integer getPsIntVal(String key){
		if(ps == null){
			return null;
		}
		return Integer.parseInt(ps.get(key).toString());
	}
	
	public Boolean getPsBooleanVal(String key){
		if(ps == null){
			return null;
		}
		return (Boolean)ps.get(key);
	}
	
	public Long getPsLongVal(String key){
		if(ps == null){
			return null;
		}
		return (Long)ps.get(key);
	}

	public List getPsListVal(String key){
		if(ps == null){
			return null;
		}
		return (List)ps.get(key);
	}

	public Date getPsDateVal(String key){
		if(ps == null){
			return null;
		}
		return (Date)ps.get(key);
	}


	/** 由于需要获取泛型类型， 仅支持实例化对象调用， 不支持static **/
	public LambdaQueryWrapper<T> lambda(){
		return new LambdaQueryWrapper<T>();
	}

}

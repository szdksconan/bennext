package com.xsscd.controller;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.xsscd.service.ServiceService;
/**
 * 提供的服务
 * @author zengcy
 *
 */
@ControllerBind(controllerKey="/back/service")
public class ServiceController extends Controller {
	public void add(){
		ServiceService.service.addProxy(this);
	}
	public void edit(){
		ServiceService.service.editProxy(this);
	}
	public void delete(){
		ServiceService.service.deleteBatchProxy(this);
	}
	public void find(){
		ServiceService.service.find(this);
	}
	//查询指定serviceId的商户id
	public void findSupplyerIdByServiceId(){
		ServiceService.service.findSupplyerIdByServiceId(this);
	}
	//查询指定服务id的所有权益及权益规则
	public void findRightAndRightRuleByServiceId(){
		ServiceService.service.findRightAndRightRuleByServiceId(this);
	}
	//查询所有供应方(商户)id name列表
	public void findSupplyerIdName(){
		ServiceService.service.findSupplyerIdName(this);
	}
	//查询所有服务类型id name 列表
	public void findServiceTypeIdName(){
		ServiceService.service.findServiceTypeIdName(this);
	}
	//查询所有卡类型id name 列表
	public void findCardTypeIdName(){
		ServiceService.service.findCardTypeIdName(this);
	}
}

package com.xsscd.base;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.xsscd.exception.AddEntityException;
import com.xsscd.exception.AuditEntityException;
import com.xsscd.exception.DeleteEntityException;
import com.xsscd.exception.EditEntityException;
import com.xsscd.exception.MethodNoImplException;
import com.xsscd.exception.OperateEntityException;
import com.xsscd.util.ExcelExportUtil;
import com.xsscd.vo.ResultVo;

/**
 * 业务处理基类
 * 
 * @author zengcy
 * 
 */
public class BaseService {
	public static Logger log = LoggerFactory.getLogger(Class.class);

	public void deleteFile(String filePath) {
		// 删除保存的文件
		File delFile = new File(filePath);
		// 判断目录或文件是否存在
		if (delFile.exists()) { // 存在则删除
			delFile.delete();
		}
	}

	/**
	 * 单个操作代理
	 */
	public void operateProxy(final Controller controller, final String operateName, final String methodName) {
		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					Class<?> subClas = BaseService.this.getClass();
					Method m = subClas.getDeclaredMethod(methodName, Controller.class);
					m.invoke(BaseService.this, controller);
					rv.setIsSuccess(true);
					rv.setMessage(operateName + "成功");
					return true;
				} catch (Exception e) {
					if (e instanceof InvocationTargetException) {
						Throwable te = ((InvocationTargetException) e).getTargetException();
						if (te instanceof OperateEntityException) {
							OperateEntityException oe = (OperateEntityException) te;
							String errorInfo = (String) oe.getMap().get("errorInfo");
							rv.setIsSuccess(false);
							rv.setMessage(operateName + "出错,错误原因:" + errorInfo);
							log.error(operateName + "出错,错误原因:" + errorInfo, oe);
							oe.printStackTrace();
							return false;
						} else {
							rv.setIsSuccess(false);
							rv.setMessage(operateName + "出错,系统繁忙");
							log.error(operateName + "出错 ", te);
							e.printStackTrace();
							return false;
						}
					} else {
						rv.setIsSuccess(false);
						rv.setMessage(operateName + "出错,系统繁忙");
						log.error(operateName + "出错 ", e);
						e.printStackTrace();
						return false;
					}
				}
				// 返回响应信息
				finally {
					if (controller.getRender() == null) {
						controller.renderJson(rv);
					}
				}
			}
		});
	}

	/**
	 * 批量操作代理
	 */
	public void operateBatchProxy(final Controller controller, final String operateName, final String methodName) {
		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					final String[] objs = controller.getPara("objs").split(",");
					Class<?> subClas = BaseService.this.getClass();
					Method m = subClas.getDeclaredMethod(methodName, String.class);
					// 循环操作所有
					for (String obj : objs) {
						m.invoke(BaseService.this, obj);
					}
					rv.setIsSuccess(true);
					rv.setMessage(operateName + "成功,共" + objs.length + "条数据");
					return true;
				} catch (Exception e) {
					if (e instanceof InvocationTargetException) {
						Throwable te = ((InvocationTargetException) e).getTargetException();
						if (te instanceof OperateEntityException) {
							OperateEntityException oe = (OperateEntityException) te;
							String errorName = (String) oe.getMap().get("errorName") == null ? "" : ",出错数据:"
									+ (String) oe.getMap().get("errorName");
							String errorInfo = (String) oe.getMap().get("errorInfo");
							rv.setIsSuccess(false);
							rv.setMessage(operateName + "出错" + errorName + ",错误原因:" + errorInfo);
							log.error(operateName + "出错" + errorName + ",错误原因:" + errorInfo, oe);
							oe.printStackTrace();
							return false;
						} else {
							rv.setIsSuccess(false);
							rv.setMessage(operateName + "出错,系统繁忙");
							log.error(operateName + "出错 ", te);
							e.printStackTrace();
							return false;
						}
					} else {
						rv.setIsSuccess(false);
						rv.setMessage(operateName + "出错,系统繁忙");
						log.error(operateName + "出错 ", e);
						e.printStackTrace();
						return false;
					}
				}
				// 返回响应信息
				finally {
					controller.renderJson(rv);
					// 事务完成，把处理结果通知给使用者
					BaseService.this.operateBatchTransactionResult(controller, methodName, rv);

				}
			}
		});
	}

	// 把批量操作事务处理结果通知给子类
	public void operateBatchTransactionResult(Controller ct, String methodName, ResultVo rv) {
		String msg = "BaseService.operateBatchTransactionResult()-子类没有接收" + methodName + "批量处理结果";
		System.out.println(msg);
		log.info(msg);
	}

	/**
	 * 具体逻辑-亲需要的子类重写
	 */
	public boolean delete(Object id, Controller ct) throws Exception {
		throw new MethodNoImplException("亲！未实现方法哦");
	}

	/**
	 * 批量删除代理--处理异常-事物
	 */
	public void deleteBatchProxy(final Controller controller) {

		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					final Object[] ids = controller.getPara("ids").split(",");
					// 循环删除所有
					for (Object id : ids) {
						boolean b = BaseService.this.delete(id, controller);
						if (!b) {
							throw new Exception();
						}
					}
					rv.setIsSuccess(true);
					rv.setMessage("删除成功,共" + ids.length + "条数据");
					return true;
				} catch (DeleteEntityException de) {
					String errorName = (String) de.getMap().get("errorName") == null ? "" : ",出错数据:"
							+ (String) de.getMap().get("errorName");
					String errorInfo = (String) de.getMap().get("errorInfo");
					rv.setIsSuccess(false);
					rv.setMessage("删除数据出错" + errorName + ",错误原因:" + errorInfo);
					log.error("删除数据出错 ", de);
					de.printStackTrace();
					return false;
				} catch (Exception e) {
					rv.setIsSuccess(false);
					rv.setMessage("删除数据出错,系统繁忙");
					log.error("删除数据出错 ", e);
					e.printStackTrace();
					return false;
				}
				// 返回响应信息
				finally {
					controller.renderJson(rv);
					BaseService.this.deleteBatchTransactionResult(controller, rv);
				}
			}
		});
	}

	// 把批量删除事务处理结果通知给子类
	public void deleteBatchTransactionResult(Controller ct, ResultVo rv) {
		String msg = "BaseService.operateBatchTransactionResult()-子类没有接收批量删除事务处理结果";
		System.out.println(msg);
		log.info(msg);
	}

	/**
	 * 批量审核代理--处理异常-事物
	 */
	public void auditBatchProxy(final Controller controller) {

		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					final String[] idStates = controller.getPara("idStates").split(",");
					// 循环删除所有
					for (String idState : idStates) {
						boolean b = BaseService.this.audit(idState, controller);
						if (!b) {
							throw new Exception();
						}
					}
					rv.setIsSuccess(true);
					rv.setMessage("审核成功,共" + idStates.length + "条数据");
					return true;
				} catch (AuditEntityException de) {
					String errorName = (String) de.getMap().get("errorName") == null ? "" : ",出错数据:"
							+ (String) de.getMap().get("errorName");
					String errorInfo = (String) de.getMap().get("errorInfo");
					rv.setIsSuccess(false);
					rv.setMessage("审核出错" + errorName + ",错误原因:" + errorInfo);
					log.error("审核出错 ", de);
					de.printStackTrace();
					return false;
				} catch (Exception e) {
					rv.setIsSuccess(false);
					rv.setMessage("审核出错,系统繁忙");
					log.error("审核出错 ", e);
					e.printStackTrace();
					return false;
				}
				// 返回响应信息
				finally {

					controller.renderJson(rv);
				}
			}
		});
	}

	/**
	 * 审核数据 格式 id_state
	 * 
	 * @param idState
	 * @return
	 * @throws Exception
	 */
	public boolean audit(String idState, Controller ct) throws Exception {
		throw new MethodNoImplException("亲！未实现方法哦");
	}

	/**
	 * 修改-代理-处理异常-事物
	 */
	public void editProxy(final Controller controller) {
		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					boolean b = BaseService.this.edit(controller);
					if (!b) {
						throw new Exception();
					}
					rv.setIsSuccess(true);
					rv.setMessage("修改成功");
					return true;
				} catch (EditEntityException de) {
					String errorInfo = (String) de.getMap().get("errorInfo");
					rv.setIsSuccess(false);
					rv.setMessage("修改数据出错,错误原因:" + errorInfo);
					log.error("修改数据出错 ", de);
					de.printStackTrace();
					return false;
				} catch (Exception e) {
					rv.setIsSuccess(false);
					rv.setMessage("修改数据出错,系统繁忙");
					log.error("修改数据出错 ", e);
					e.printStackTrace();
					return false;
				}
				// 返回响应信息
				finally {
					if (controller.getRender() == null) {
						controller.renderJson(rv);
					}
				}
			}
		});
	}

	public boolean edit(Controller controller) throws Exception {
		throw new MethodNoImplException("亲！未实现方法哦");
	}

	/**
	 * 添加-代理-处理异常-事物
	 */
	public void addProxy(final Controller controller) {
		// 开启事物
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				ResultVo rv = new ResultVo();
				try {
					boolean b = BaseService.this.add(controller);
					if (!b) {
						throw new Exception();
					}
					rv.setIsSuccess(true);
					rv.setMessage("添加成功");
					return true;
				} catch (AddEntityException de) {
					String errorInfo = (String) de.getMap().get("errorInfo");
					rv.setIsSuccess(false);
					rv.setMessage("添加数据出错,错误原因:" + errorInfo);
					log.error("添加数据出错 ", de);
					de.printStackTrace();
					return false;
				} catch (Exception e) {
					rv.setIsSuccess(false);
					rv.setMessage("添加数据出错,系统繁忙");
					log.error("添加数据出错 ", e);
					e.printStackTrace();
					return false;
				}
				// 返回响应信息
				finally {
					if (controller.getRender() == null) {
						controller.renderJson(rv);
					}
				}
			}
		});
	}

	public boolean add(Controller controller) throws Exception {
		throw new MethodNoImplException("亲！未实现方法哦");
	}

	/**
	 * 导出excel
	 * 
	 * @param fileName
	 * @param exportUtil
	 * @param listWeb
	 * @param headRowNum
	 * @param response
	 * @param request
	 */
	protected void exportExcel(boolean customPaixu, String fileName, ExcelExportUtil exportUtil,
			List<Map<String, Object>> listMap, int headRowNum, HttpServletResponse response, String... noFieldsName) {
		try {
			// TODO 该编码 支持linux系统
			fileName = new String(fileName.getBytes("utf-8"), "ISO_8859_1");
			exportUtil.addTableInfoMap(customPaixu, listMap, headRowNum, noFieldsName);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/octet-stream;charset=UTF-8");
			OutputStream os;
			os = response.getOutputStream();
			exportUtil.getWorkbook().write(os);
			os.flush();
			os.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}

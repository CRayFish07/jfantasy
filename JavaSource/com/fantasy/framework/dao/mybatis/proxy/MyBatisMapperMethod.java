package com.fantasy.framework.dao.mybatis.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;

/**
 * MyBatis Mapper 方法
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:39:09
 * @version 1.0
 */
public class MyBatisMapperMethod {
	protected final Log logger = LogFactory.getLog(MyBatisMapperMethod.class);
	private SqlSession sqlSession;
	private Configuration config;
	private SqlCommandType type;
	private String commandName;
	private Class<?> declaringInterface;
	private Method method;
	private Integer pageIndex;
	private List<String> paramNames;
	private List<Integer> paramPositions;
	private boolean hasNamedParameters;
	private Map<String, Map<String, ResultMapping>> resultMaps;

	/**
	 * 
	 * @param declaringInterface
	 *            Mapper接口
	 * @param method
	 *            java.lang.reflect.Method;对象
	 * @param sqlSession
	 */
	public MyBatisMapperMethod(Class<?> declaringInterface, Method method, SqlSession sqlSession) {
		this.paramNames = new ArrayList<String>();
		this.paramPositions = new ArrayList<Integer>();
		this.sqlSession = sqlSession;
		this.method = method;
		this.config = sqlSession.getConfiguration();
		this.hasNamedParameters = false;
		this.declaringInterface = declaringInterface;
		setupFields();
		setupMethodSignature();
		setupCommandType();
		setupResultMap();
		validateStatement();
	}

	private void setupResultMap() {
		this.resultMaps = new HashMap<String, Map<String, ResultMapping>>();
		MappedStatement ms = this.sqlSession.getConfiguration().getMappedStatement(this.commandName);
		for (ResultMap resultMap : ms.getResultMaps()) {
			if (this.resultMaps.containsKey(resultMap.getId()))
				continue;
			Map<String, ResultMapping> mappings = new HashMap<String, ResultMapping>();
			for (ResultMapping mapping : resultMap.getResultMappings()) {
				if (mappings.containsKey(mapping.getProperty()))
					continue;
				mappings.put(mapping.getProperty(), mapping);
			}
			this.resultMaps.put(resultMap.getId(), mappings);
		}
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object[] args) {
		Map<String, Object> param = getParam(args);
		Pager<Object> pager = (Pager<Object>) param.get("pager");
		pager.setPageItems(this.sqlSession.selectList(this.commandName, param));
		return pager;
	}

	/**
	 * 将参数转换为Map<String, Object>
	 * 
	 * @功能描述
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getParam(Object[] args) {
		Map<String, Object> param = new HashMap<String, Object>();
		int paramCount = this.paramPositions.size();
		if (args == null || paramCount == 0) {
			if (args != null) {
				param.put("pager", getPager((Pager<Object>) args[this.pageIndex.intValue()]));
			}
			return param;
		}
		if ((!this.hasNamedParameters) && (paramCount == 1)) {
			if ((ObjectUtil.isNull(args[((Integer) this.paramPositions.get(0)).intValue()])) || (ClassUtil.isPrimitiveWrapper(args[((Integer) this.paramPositions.get(0)).intValue()].getClass())))
				param.put("value", args[((Integer) this.paramPositions.get(0)).intValue()]);
			else {
				param.putAll(ObjectUtil.toMap(args[((Integer) this.paramPositions.get(0)).intValue()]));
			}
			param.put("pager", getPager((Pager<Object>) args[this.pageIndex.intValue()]));
			return param;
		}
		for (int i = 0; i < paramCount; i++) {
			param.put((String) this.paramNames.get(i), args[((Integer) this.paramPositions.get(i)).intValue()]);
		}
		param.put("pager", getPager((Pager<Object>) args[this.pageIndex.intValue()]));
		return param;
	}

	/**
	 * 检查Pager对象是否为空，为空初始化一个新的Pager对象
	 * 
	 * @功能描述
	 * @param page
	 * @return
	 */
	private Pager<Object> getPager(Pager<Object> page) {
		if (ObjectUtil.isNull(page))
			page = new Pager<Object>();
		return page;
	}

	/**
	 * 获取方法的名称和参数下标
	 * 
	 * @功能描述
	 */
	private void setupMethodSignature() {
		Class<?>[] argTypes = this.method.getParameterTypes();
		for (int i = 0; i < argTypes.length; i++)
			if (Pager.class.isAssignableFrom(argTypes[i])) {
				this.pageIndex = Integer.valueOf(i);
			} else {
				String paramName = String.valueOf(this.paramPositions.size());
				paramName = getParamNameFromAnnotation(i, paramName);
				this.paramNames.add(paramName);
				this.paramPositions.add(Integer.valueOf(i));
			}
	}

	/**
	 * 获取Param注解的名称
	 * 
	 * @功能描述
	 * @param i
	 * @param paramName
	 * @return
	 */
	private String getParamNameFromAnnotation(int i, String paramName) {
		Object[] paramAnnos = this.method.getParameterAnnotations()[i];
		for (Object paramAnno : paramAnnos) {
			if ((paramAnno instanceof Param)) {
				this.hasNamedParameters = true;
				paramName = ((Param) paramAnno).value();
			}
		}
		return paramName;
	}

	/**
	 * 获取方法对应 sql Mapper 中的 command Name
	 * 
	 * @功能描述
	 */
	private void setupFields() {
		this.commandName = (this.declaringInterface.getName() + "." + this.method.getName());
	}

	/**
	 * 验证 CommandType 是否为Select查询
	 * 
	 * @功能描述
	 */
	private void setupCommandType() {
		MappedStatement ms = this.config.getMappedStatement(this.commandName);
		this.type = ms.getSqlCommandType();
		if (this.type != SqlCommandType.SELECT)
			throw new BindingException("Unsupport execution method for: " + this.commandName);
	}

	private void validateStatement() {
		if (!this.config.hasStatement(this.commandName))
			throw new BindingException("Invalid bound statement (not found): " + this.commandName);
	}
}
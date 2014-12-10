
/**
 * Copyright(c) 2000-2013 HC360.COM, All Rights Reserved.
 * Project: ework 
 * Author: Gao xingkun
 * Createdate: ����3:44:20
 * Version: 1.0
 *
 */

package com.web.maven.common.pageplugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.web.maven.common.pages.PageBean;
import com.web.maven.common.util.ReflectionUtils;



/**
 * ��ҳ�����
 * 
 * @project ework
 * @author Gao xingkun
 * @version 1.0
 * @date 2013-4-24 ����3:44:20   
 */
@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PagingStatementHandlerInterceptor implements Interceptor {
	private final Log logger = LogFactory.getLog(getClass());

	/** mapper.xml����Ҫ���ص�ID(����ƥ��) **/
	private static String PAGESQL_ID = "pageSqlId";
	
//	private String dialect = "oracle";
	private String pageSqlId;
	
	/**
	 * Ϊcount������ò���.
	 * 
	 * @see org.apache.ibatis.executor.parameter.DefaultParameterHandler#setParameters(PreparedStatement)
	 * 
	 * @param ps
	 * @param ms
	 * @param bs
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement ms,
			BoundSql bs, Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters")
				.object(ms.getParameterMap().getId());
		List<ParameterMapping> mappings = bs.getParameterMappings();
		if (mappings == null) {
			return;
		}
		Configuration configuration = ms.getConfiguration();
		TypeHandlerRegistry typeHandlerRegistry = configuration
				.getTypeHandlerRegistry();
		MetaObject metaObject = parameterObject == null ? null : configuration
				.newMetaObject(parameterObject);
		for (int i = 0; i < mappings.size(); i++) {
			ParameterMapping parameterMapping = mappings.get(i);
			if (parameterMapping.getMode() != ParameterMode.OUT) {
				Object value;
				String propertyName = parameterMapping.getProperty();
				PropertyTokenizer prop = new PropertyTokenizer(propertyName);
				if (parameterObject == null) {
					value = null;
				} else if (typeHandlerRegistry.hasTypeHandler(parameterObject
						.getClass())) {
					value = parameterObject;
				} else if (bs.hasAdditionalParameter(propertyName)) {
					value = bs.getAdditionalParameter(propertyName);
				} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
						&& bs.hasAdditionalParameter(prop.getName())) {
					value = bs.getAdditionalParameter(prop.getName());
					if (value != null) {
						value = configuration.newMetaObject(value)
								.getValue(
										propertyName.substring(prop.getName()
												.length()));
					}
				} else {
					value = metaObject == null ? null : metaObject
							.getValue(propertyName);
				}
				TypeHandler typeHandler = parameterMapping.getTypeHandler();
				if (typeHandler == null) {
					throw new ExecutorException(
							"There was no TypeHandler found for parameter "
									+ propertyName + " of statement "
									+ ms.getId());
				}
				typeHandler.setParameter(ps, i + 1, value,
						parameterMapping.getJdbcType());
			}
		}
	}

	private StatementHandler getStatementHandler(Invocation invocation){
		StatementHandler statementHandler = null;
		statementHandler = (StatementHandler) invocation.getTarget();
		Plugin plugin=  (Plugin) ReflectionUtils.getFieldValue(statementHandler, "h");
		if(plugin != null){
			statementHandler = getStatementHandler(plugin);
		}else{
			statementHandler = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
		}
		
		return statementHandler;
	}
	
	private StatementHandler getStatementHandler(Plugin plugin){
		StatementHandler statementHandler = null;
		StatementHandler pluginstatementHandler=  (StatementHandler) ReflectionUtils.getFieldValue(plugin, "target");
//		statementHandler = (BaseStatementHandler) ReflectionUtils.getFieldValue(pluginstatementHandler, "delegate");
		Plugin plugintemp=  (Plugin) ReflectionUtils.getFieldValue(pluginstatementHandler, "h");
		if(plugintemp != null){
			statementHandler = getStatementHandler(plugintemp);
		}else{
			statementHandler = (StatementHandler) ReflectionUtils.getFieldValue(pluginstatementHandler, "delegate");
		}
		return statementHandler;
	}
	/**
	 * ��ҳ��������
	 * <p>������Ҫ������ȡ�ܼ�¼���Ͷ�SQL��ҳ���ء�
	 * 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		
		StatementHandler statementHandler = getStatementHandler(invocation);
			
		StatementHandler delegate = (StatementHandler)statementHandler;

		MappedStatement mappedStatement = (MappedStatement) ReflectionUtils
				.getFieldValue(delegate, "mappedStatement");

		if (mappedStatement.getId().matches(pageSqlId)) {
			BoundSql boundSql = delegate.getBoundSql();
	
			Object parameterObject = boundSql.getParameterObject();
			
			if(parameterObject == null){
				logger.error("����������δʵ������");
				throw new NullPointerException("����������δʵ������");
			}
			
			Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
			PageBean pageBean = (PageBean) parameterMap.get("pageBean");
			
			if(pageBean == null){
				logger.error("��ҳ���󲻴��ڣ�");
				throw new NullPointerException("��ҳ���󲻴��ڣ�");
			}
			
			// ȡ������ 
			Connection connection = (Connection) invocation.getArgs()[0];
			String sql = boundSql.getSql();
			
			//ɾ����ѯ����е�order by�Ӿ�
			String newCountSql = "";
			
			newCountSql = delOrderbySQL(sql);
			
			if(sql.toLowerCase().indexOf("group by") != -1){
				newCountSql = "select count(*) as cnt from (" + sql + ")";
				
			}else{
//				newCountSql = delOrderbySQL(sql);
				int idx = newCountSql.toUpperCase().indexOf("FROM ");
				newCountSql = "select count(*) as cnt " + newCountSql.substring(idx);
			}

			if(logger.isDebugEnabled()){
				logger.debug("��ѯ�ܼ�¼SQL:" + newCountSql);
			}
			
			BoundSql newBoundSql = new BoundSql(mappedStatement
					.getConfiguration(), newCountSql, boundSql
					.getParameterMappings(), parameterObject);
		
		   copyAdditionalParametersByBoundSql(newBoundSql,boundSql);
		 
		   DefaultParameterHandler parameterHandler = new DefaultParameterHandler(
					mappedStatement, parameterObject, newBoundSql);

			
			PreparedStatement ps = null;
			int count = 0;
			try{
				ps = connection.prepareStatement(newCountSql);
				parameterHandler.setParameters(ps);
				
				ResultSet rs = ps.executeQuery();
				count = (rs.next()) ? rs.getInt("cnt") : 0;
				rs.close();
			}catch(SQLException e){
				throw new Exception("ִ�м�¼����SQLʱ�����쳣",e);
			}finally{
				try {
					if (ps != null) ps.close();
				} catch (SQLException e) {
					throw new Exception("�ر�״̬ʱ�����쳣", e);
				}
			}
			
			// �Ѽ�¼�����������pageBean��
			pageBean.setCount(count);
			
			String pageSql = getLimitString(sql,pageBean,boundSql, mappedStatement);
			
			//���°󶨽����ҳ����
			//setParameters(PreparedStatement ps, mappedStatement,newBoundSql, Object parameterObject);
			
			//����ҳsql��䷴���BoundSql. 
			ReflectionUtils.setFieldValue(boundSql, "sql", pageSql);  

		}
		
		return invocation.proceed();
	}

	/**
	 * ɾ����ѯ�����е�order by�Ӿ�
	 * 
	 * @param queryString ��ѯSQL���
	 * @return ɾ����ѯ����е�order by�Ӿ��Ĳ�ѯ���
	 */
	private String delOrderbySQL(String queryString) {
		StringBuffer temp = new StringBuffer();
		queryString = "(" + queryString + ")";

		String[] strArray = queryString.split("order by");
		for (int i = 1; i < strArray.length; i++) {
			strArray[i] = ")";
		}
		for (int i = 0; i < strArray.length; i++) {
			temp.append(strArray[i]);
		}
		String result = temp.toString();
		if (temp.length() >= 2) {
			result = result.substring(1, result.length() - 1);
		}
		return result;
	}

	/**
	 * 
	 * ʵ��ƴװ��ҳsql�ķ�������ͬ�����ݿ���Ҫ�ֱ�ʵ��
	 * @author weiwei
	 * @version 1.0
	 * @date 2013-3-2 ����3:42:33
	 * @param sql
	 * @param pageBean
	 * @param boundSql
	 * @param mappedStatement
	 * @return String
	 */
	protected String getLimitString(String sql, PageBean pageBean,BoundSql boundSql,MappedStatement mappedStatement) {
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if(boundSql.getParameterMappings()==null || boundSql.getParameterMappings().isEmpty()){
			
			pagingSelect=addPageParamByEmptyParameterMapping(pagingSelect,sql,pageBean);
		}else{
			if (pageBean.getStartNo() > 1) {
				pagingSelect.append(
				  "select * from ( select row_.*, rownum rownum_ from ( ");
			} else {
				pagingSelect.append("select * from ( ");
			}
			pagingSelect.append(sql);
			if (pageBean.getStartNo() > 1) {
				pagingSelect.append(" ) row_ where rownum <= ")
				            .append("?")
				            .append(") where rownum_ >= ")
				            .append("?");
				 			//����ҳ�����󶨵�����ӳ����
							ParameterMapping.Builder endNo = new ParameterMapping.Builder(mappedStatement.getConfiguration(), "pageBean.endNo", Object.class);
							boundSql.getParameterMappings().add(endNo.build());
				
							ParameterMapping.Builder startNo = new ParameterMapping.Builder(mappedStatement.getConfiguration(), "pageBean.startNo", Object.class);
							boundSql.getParameterMappings().add(startNo.build());
				   			//params.add(pageBean.getEndNo());
				   		   	//params.add(pageBean.getStartNo());
			} else {
				pagingSelect.append(" ) where rownum <= ")
				            .append("?");
				ParameterMapping.Builder endNo = new ParameterMapping.Builder(mappedStatement.getConfiguration(), "pageBean.endNo", Object.class);
				boundSql.getParameterMappings().add(endNo.build());
							//params.add(pageBean.getEndNo());
			}
		}
		return pagingSelect.toString();
	}
	/**
	 * �ڵ�ǰ��ѯSQL����Ϊ��ʱmybatis���boundSql�в���ӳ�� ParameterMappings
	 * ��Ϊunmodifiable����������£��޷���̬��ӷ�ҳ����
	 * ֻ��ͨ��ƴSQL�ַ�����ʵ�ַ�ҳ��
	 * @author weiwei
	 * @version 1.0
	 * @date 2013-3-2 ����5:56:23
	 * @param sql
	 * @param pageBean
	 * @param boundSql
	 * @param mappedStatement void
	 */
	private StringBuffer addPageParamByEmptyParameterMapping(StringBuffer pagingSelect,String sql, PageBean pageBean){
		if (pageBean.getStartNo() > 1) {
			pagingSelect.append(
			  "select * from ( select row_.*, rownum rownum_ from ( ");
		} else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (pageBean.getStartNo() > 1) {
			pagingSelect.append(" ) row_ where rownum <= ")
			            .append(pageBean.getEndNo())
			            .append(") where rownum_ >= ")
			            .append(pageBean.getStartNo());
			 			
		} else {
			pagingSelect.append(" ) where rownum <= ")
			            .append(pageBean.getEndNo());
		}
		return pagingSelect;
	}
	
	/**
	 * ���ر������
	 * 
	 * @param target
	 * @return
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * ��������ֵ��
	 * 
	 * @param properties
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	public void setProperties(Properties properties){
		pageSqlId = properties.getProperty(PAGESQL_ID);
		if (pageSqlId == null || pageSqlId.length() < 1) {
			logger.error("pageSqlId property is not found!");
		}
	}
	
	/**
	 * 
	 * Ϊ�˽��mybatis��ҳ����ڽ���
	 * select from in (xx,xx,xx)ʱ������������bug����������
	 * ��Ҫ��Ϊ�˽�ԭʼBoundSql������additionalParameters���Ƶ��¶��������ȥ
	 * mybatisԭʼ��û���ṩadditionalParameters�ĸ�ֵ�ӿ�
	 */
	private void copyAdditionalParametersByBoundSql(BoundSql target,BoundSql source){
		List<ParameterMapping> parameterMappings = source.getParameterMappings();
		if(parameterMappings !=null ){
			for(ParameterMapping parameter : parameterMappings){
				if(source.hasAdditionalParameter(parameter.getProperty())){
					
					target.setAdditionalParameter(parameter.getProperty(), source.getAdditionalParameter(parameter.getProperty()));
				}
			}
		}
		 
	}
	
	

}


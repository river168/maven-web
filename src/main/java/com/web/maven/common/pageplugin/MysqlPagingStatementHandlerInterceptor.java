
/**
 * Copyright(c) 2000-2013 HC360.COM, All Rights Reserved.
 * Project: ework 
 * Author: Gao xingkun
 * Createdate: ÏÂÎç3:41:55
 * Version: 1.0
 *
 */

package com.web.maven.common.pageplugin;

import java.sql.Connection;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import com.web.maven.common.pages.PageBean;

/**
 * 
 * @project ework
 * @author Gao xingkun
 * @version 1.0
 * @date 2013-4-24 ÏÂÎç3:41:55   
 */
@Intercepts( { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MysqlPagingStatementHandlerInterceptor extends PagingStatementHandlerInterceptor {


	protected String getLimitString(String sql, PageBean pageBean,BoundSql boundSql,MappedStatement mappedStatement) {
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

		pagingSelect.append("select * from ( ");

		pagingSelect.append(sql);
			pagingSelect.append(" ) as row_  limit ")
			            .append(pageBean.getStartNo()-1)
			            .append(",")
			            .append(pageBean.getPageSize());
		return pagingSelect.toString();
	}

}
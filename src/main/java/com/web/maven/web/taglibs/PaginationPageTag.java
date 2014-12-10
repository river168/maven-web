package com.web.maven.web.taglibs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 分页标签，分页公共组件。
 * @author cj
 * 
 * **/
public class PaginationPageTag extends TagSupport {

	int showpage = 4;
	private static final long serialVersionUID = 1673491971683216709L;
	private String path = null;
	private String formName = null; // 对应表单的form名称：
	private String contextPath = null;

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param formName
	 *            The formName to set.
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {

		PaginationPage pagination = null;

		if (this.path == null) {
			throw new NullPointerException("path属性不能为空");
		}

		// 获取分页对象
		pagination = (PaginationPage) pageContext.getRequest().getAttribute(
				PaginationPage.pagination);

		if (pagination == null) {
			throw new NullPointerException(
					PaginationPage.pagination
							+ "在request中为空!错误原因：前面代码调用出错：导致没有执行：request.setAttribute(PaginationPage.pagination, pagination语句：标签中取值不到)");
		}

		try {
			JspWriter out = pageContext.getOut();
			/* 创建CSS内容 */
			createCSS(out);
			contextPath = encodeURL(this.path, PaginationPage.jumpPage);

			System.out.println("标签中===contextPath==" + contextPath);

			if (pagination.getTotalCount() > 0) {
				if (this.formName != null && this.formName.length() > 0) {
					if (pagination.isHasPreviousPage()) {
						out.println("<button class=\"buttonJump\"  onclick=\"paginationSubmit('1');return false;\" title=\"首页\">首页</button>");
						out.println("<button class=\"buttonJump\"  onclick=\"paginationSubmit('"
								+ pagination.getPreviousPage()
								+ "');return false;\" title=\"上页\">上页</button>");
					} else {
						out.println("<button disabled  class=\"buttonJump\" title=\"首页\">首页</button>");
						out.println("<button disabled  class=\"buttonJump\" title=\"上页\">上页</button>");
					}

					if (pagination.isHasNextPage()) {
						out.println("<button class=\"buttonJump\"   onclick=\"paginationSubmit('"
								+ pagination.getNextPage()
								+ "');return false;\"  title=\"下页\">下页</button>");
						out.println("<button class=\"buttonJump\"   onclick=\"paginationSubmit('"
								+ pagination.getTotalPage()
								+ "');return false;\" title=\"尾页\">尾页</button>");
					} else {
						out.println("<button disabled  class=\"buttonJump\" title=\"下页\">下页</button>");
						out.println("<button disabled  class=\"buttonJump\" title=\"尾页\">尾页</button>");
					}
				} else /* 没有表单form的处理 */
				{
					if (pagination.isHasPreviousPage()) {
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + "1\" title=\"首 页\">首 页</a>");
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getPreviousPage()
								+ "\" title=\"上页\">上页</a>");
					} else {
						out.println("<span title=\"首 页\">首 页</span>");
						out.println("<span title=\"上页\">上页</span>");
					}

					if (pagination.isHasNextPage()) {
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getNextPage()
								+ "\" title=\"下页\">下页</a>");
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getTotalPage()
								+ "\" title=\"尾页\">尾页</a>");
					} else {
						out.println("<span title=\"下页\">下页</span>");
						out.println("<span title=\"尾页\">尾页</span>");
					}
				}
				out.println("&nbsp;");
				out.println("共有" + pagination.getTotalCount() + "条");
				out.print("&nbsp;");
				out.println("每页" + pagination.getPageSize() + "条");
				out.print("&nbsp;");
				out.println("共" + pagination.getTotalPage() + "页");
				out.print("&nbsp;");
				out.println("本页是第" + pagination.getCurrPage() + "页");
				out.println("&nbsp;");
				out.println("跳转到第<input type=\"text\" id=\""
						+ PaginationPage.jumpPage
						+ "\" class=\"textbox\" size=\"3\" maxlength=\"5\">页");
				out.println("&nbsp;");
				out.println("<button onclick=\"paginationGoto();\" class=\"buttonJump\"><font color=red>超链转向</font></button>");
				out.println("<br />");

			} else {
				out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red>本次查询结果没有相关的记录!!!!!!</font>");
			}

			/* 创建js内容 ,对分页表单进行控制 */
			createJS(out, contextPath, pagination.getTotalPage());

			out.println();
		} catch (Exception e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	public int doEndTag() throws JspException {

		return EVAL_PAGE;
	}

	/**
	 * 改变URL使它能访问到action或servlet中去
	 * 
	 * @param href
	 *            从页面传来的href
	 * @param param
	 * @param queryStr
	 *            查询内容
	 * @return
	 * @author cj
	 */
	private final String encodeURL(String path, String param) {
		StringBuffer buffer = new StringBuffer(1000);

		HttpServletRequest request = (HttpServletRequest) this.pageContext
				.getRequest();
		String ContextPath = request.getContextPath();

		// String absolutePath = request.getScheme() + "://"
		// + request.getServerName() + ":" + request.getServerPort()
		// + ContextPath + "/" + path; //获取绝对路径
		// System.out.println("绝对路径："+absolutePath);
		//
		String _path = ContextPath + "/" + path;

		buffer.append(_path);
		int question = _path.indexOf("?");
		if (question > 0) {
			buffer.append("&" + param + "=");
		} else {
			buffer.append("?" + param + "=");
		}
		return buffer.toString();
	}

	/**
	 * 创建js内容如果提交form就用js提交
	 * 
	 * @param out
	 * @param contextPath
	 * @throws Exception
	 * @author cj
	 */
	private final void createJS(JspWriter out, String contextPath, int totalPage)
			throws Exception {
		out.println("<script language=\"javascript\">");
		out.println("function paginationSubmit(pageNum_) { ");
		out.println("  document." + formName + ".action=\'" + contextPath
				+ "\' + pageNum_ + \'\';");
		out.println("  document." + formName + ".submit();");
		out.println("}");

		/* 校验是否全由数字组成 */
		out.println("function isDigit(str)  ");
		out.println("{  ");
		out.println("var reg=/^[0-9]{1,20}$/;   /*定义验证表达式*/ ");
		out.println("return reg.test(str);     /*进行验证*/ ");
		out.println("}  ");

		out.println("function paginationGoto() { ");
		out.println("  if(!isDigit(document.all." + PaginationPage.jumpPage
				+ ".value)) {");
		out.println(" alert('页号必须是整数!!!!!');");
		out.println(" document.all." + PaginationPage.jumpPage + ".value=1;");
		out.println("  }");

		out.println("  pageNum_ = document.all." + PaginationPage.jumpPage
				+ ".value");
		out.println("  if(pageNum_ > 0 && pageNum_ <= " + totalPage + ") {");
		out.println("    document." + formName + ".action=\'" + contextPath
				+ "\' + pageNum_ ;");

		System.out.println("paginationGoto==" + " document." + formName
				+ ".action=\'" + contextPath + "\' + pageNum_ ;");

		out.println("    document." + formName + ".submit();");
		out.println("  } else { ");
		out.println("    alert(\"您输入的页数超出范围\");");
		out.println("  }");
		out.println("}");
		out.println("</script>");
		out.println();
	}

	/**
	 * 创建css内容
	 * 
	 * @param out
	 * @throws Exception
	 * @author cj
	 */
	private final void createCSS(JspWriter out) throws Exception {
		out.println("<style type=\"text/css\">");
		out.println(" .textbox {background-color: #FEFFFD;border: 1px solid #336699;height: 17px;}");
		out.println(" .buttonJump {");
		out.println("  BORDER-RIGHT: #7b9ebd 1px solid; PADDING-RIGHT: 2px; BORDER-TOP: #7b9ebd 1px solid; PADDING-LEFT: 2px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#ffffff, EndColorStr=#cecfde); BORDER-LEFT: #7b9ebd 1px solid; CURSOR: hand; COLOR: black; PADDING-TOP: 2px; BORDER-BOTTOM: #7b9ebd 1px solid");
		out.println("</style>");
	}

}

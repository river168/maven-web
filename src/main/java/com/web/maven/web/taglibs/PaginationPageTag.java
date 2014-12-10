package com.web.maven.web.taglibs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * ��ҳ��ǩ����ҳ���������
 * @author cj
 * 
 * **/
public class PaginationPageTag extends TagSupport {

	int showpage = 4;
	private static final long serialVersionUID = 1673491971683216709L;
	private String path = null;
	private String formName = null; // ��Ӧ����form���ƣ�
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
			throw new NullPointerException("path���Բ���Ϊ��");
		}

		// ��ȡ��ҳ����
		pagination = (PaginationPage) pageContext.getRequest().getAttribute(
				PaginationPage.pagination);

		if (pagination == null) {
			throw new NullPointerException(
					PaginationPage.pagination
							+ "��request��Ϊ��!����ԭ��ǰ�������ó�������û��ִ�У�request.setAttribute(PaginationPage.pagination, pagination��䣺��ǩ��ȡֵ����)");
		}

		try {
			JspWriter out = pageContext.getOut();
			/* ����CSS���� */
			createCSS(out);
			contextPath = encodeURL(this.path, PaginationPage.jumpPage);

			System.out.println("��ǩ��===contextPath==" + contextPath);

			if (pagination.getTotalCount() > 0) {
				if (this.formName != null && this.formName.length() > 0) {
					if (pagination.isHasPreviousPage()) {
						out.println("<button class=\"buttonJump\"  onclick=\"paginationSubmit('1');return false;\" title=\"��ҳ\">��ҳ</button>");
						out.println("<button class=\"buttonJump\"  onclick=\"paginationSubmit('"
								+ pagination.getPreviousPage()
								+ "');return false;\" title=\"��ҳ\">��ҳ</button>");
					} else {
						out.println("<button disabled  class=\"buttonJump\" title=\"��ҳ\">��ҳ</button>");
						out.println("<button disabled  class=\"buttonJump\" title=\"��ҳ\">��ҳ</button>");
					}

					if (pagination.isHasNextPage()) {
						out.println("<button class=\"buttonJump\"   onclick=\"paginationSubmit('"
								+ pagination.getNextPage()
								+ "');return false;\"  title=\"��ҳ\">��ҳ</button>");
						out.println("<button class=\"buttonJump\"   onclick=\"paginationSubmit('"
								+ pagination.getTotalPage()
								+ "');return false;\" title=\"βҳ\">βҳ</button>");
					} else {
						out.println("<button disabled  class=\"buttonJump\" title=\"��ҳ\">��ҳ</button>");
						out.println("<button disabled  class=\"buttonJump\" title=\"βҳ\">βҳ</button>");
					}
				} else /* û�б�form�Ĵ��� */
				{
					if (pagination.isHasPreviousPage()) {
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + "1\" title=\"�� ҳ\">�� ҳ</a>");
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getPreviousPage()
								+ "\" title=\"��ҳ\">��ҳ</a>");
					} else {
						out.println("<span title=\"�� ҳ\">�� ҳ</span>");
						out.println("<span title=\"��ҳ\">��ҳ</span>");
					}

					if (pagination.isHasNextPage()) {
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getNextPage()
								+ "\" title=\"��ҳ\">��ҳ</a>");
						out.println("<a class=\"pagination\" href=\""
								+ contextPath + pagination.getTotalPage()
								+ "\" title=\"βҳ\">βҳ</a>");
					} else {
						out.println("<span title=\"��ҳ\">��ҳ</span>");
						out.println("<span title=\"βҳ\">βҳ</span>");
					}
				}
				out.println("&nbsp;");
				out.println("����" + pagination.getTotalCount() + "��");
				out.print("&nbsp;");
				out.println("ÿҳ" + pagination.getPageSize() + "��");
				out.print("&nbsp;");
				out.println("��" + pagination.getTotalPage() + "ҳ");
				out.print("&nbsp;");
				out.println("��ҳ�ǵ�" + pagination.getCurrPage() + "ҳ");
				out.println("&nbsp;");
				out.println("��ת����<input type=\"text\" id=\""
						+ PaginationPage.jumpPage
						+ "\" class=\"textbox\" size=\"3\" maxlength=\"5\">ҳ");
				out.println("&nbsp;");
				out.println("<button onclick=\"paginationGoto();\" class=\"buttonJump\"><font color=red>����ת��</font></button>");
				out.println("<br />");

			} else {
				out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red>���β�ѯ���û����صļ�¼!!!!!!</font>");
			}

			/* ����js���� ,�Է�ҳ�����п��� */
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
	 * �ı�URLʹ���ܷ��ʵ�action��servlet��ȥ
	 * 
	 * @param href
	 *            ��ҳ�洫����href
	 * @param param
	 * @param queryStr
	 *            ��ѯ����
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
		// + ContextPath + "/" + path; //��ȡ����·��
		// System.out.println("����·����"+absolutePath);
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
	 * ����js��������ύform����js�ύ
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

		/* У���Ƿ�ȫ��������� */
		out.println("function isDigit(str)  ");
		out.println("{  ");
		out.println("var reg=/^[0-9]{1,20}$/;   /*������֤���ʽ*/ ");
		out.println("return reg.test(str);     /*������֤*/ ");
		out.println("}  ");

		out.println("function paginationGoto() { ");
		out.println("  if(!isDigit(document.all." + PaginationPage.jumpPage
				+ ".value)) {");
		out.println(" alert('ҳ�ű���������!!!!!');");
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
		out.println("    alert(\"�������ҳ��������Χ\");");
		out.println("  }");
		out.println("}");
		out.println("</script>");
		out.println();
	}

	/**
	 * ����css����
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

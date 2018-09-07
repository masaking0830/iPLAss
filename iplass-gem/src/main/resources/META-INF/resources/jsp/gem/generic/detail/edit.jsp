<%--
 Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.

 Unless you have purchased a commercial license,
 the following license terms apply:

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://iplass.org/tags/mtp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>

<%@ page import="org.iplass.mtp.util.StringUtil" %>
<%@ page import="org.iplass.mtp.view.generic.*"%>
<%@ page import="org.iplass.mtp.view.generic.element.section.*"%>
<%@ page import="org.iplass.mtp.web.template.TemplateUtil" %>
<%@ page import="org.iplass.gem.command.generic.delete.DeleteCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.DetailFormViewData" %>
<%@ page import="org.iplass.gem.command.generic.detail.InsertCommand"%>
<%@ page import="org.iplass.gem.command.generic.detail.UpdateCommand"%>
<%@ page import="org.iplass.gem.command.generic.search.SearchViewCommand"%>
<%@ page import="org.iplass.gem.command.Constants" %>
<%@ page import="org.iplass.gem.command.GemResourceBundleUtil" %>
<%@ page import="org.iplass.gem.command.ViewUtil"%>
<%
	String newversion = request.getParameter(Constants.NEWVERSION);
	String backPath = request.getParameter(Constants.BACK_PATH);
	String topViewListOffset = request.getParameter(Constants.TOPVIEW_LIST_OFFSET);
	if (topViewListOffset == null) {topViewListOffset = "";}

	//コマンドから
	DetailFormViewData data = (DetailFormViewData) request.getAttribute(Constants.DATA);
	String searchCond = (String) request.getAttribute(Constants.SEARCH_COND);
	String message = (String) request.getAttribute(Constants.MESSAGE);

	OutputType type = OutputType.EDIT;
	String contextPath = TemplateUtil.getTenantContextPath();
	DetailFormView form = data.getView();

	String defName = data.getEntityDefinition().getName();
	String viewName = form.getName();

	//表示名
	String displayName = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList(),
			data.getEntityDefinition().getDisplayName(), data.getEntityDefinition().getLocalizedDisplayNameList());
	//新規or更新
	String execType = data.getExecType();
	//数値のカンマ区切りのセパレータ
	String separator = ViewUtil.getGroupingSeparator();
	//css用クラス
	String className = defName.replaceAll("\\.", "_");
	//タイトルのアイコン
	String imageColor = ViewUtil.getEntityImageColor(form);
	String imageColorStyle = "";
	if (imageColor != null) {
		imageColorStyle = "imgclr_" + imageColor;
	}

	String iconTag = ViewUtil.getIconTag(form);

	//編集対象
	String oid = null;
	Long updateDate = null;
	Long version = null;
	if (data.getEntity() != null) {
		oid = data.getEntity().getOid();
		version = data.getEntity().getVersion();
		if (data.getEntity().getUpdateDate() != null) {
			updateDate = data.getEntity().getUpdateDate().getTime();
		}

		//参照プロパティで利用
		request.setAttribute(Constants.EDITOR_PARENT_ENTITY, data.getEntity());
	}

	//ビュー名があればアクションの後につける
	String urlPath = ViewUtil.getParamMappingPath(defName, viewName);

	//キャンセルアクション
	String cancel = "";
	if (StringUtil.isEmpty(backPath)) {
		if (StringUtil.isNotBlank(form.getCancelActionName())) {
			cancel = form.getCancelActionName() + urlPath;
		} else {
			cancel = SearchViewCommand.SEARCH_ACTION_NAME + urlPath;
		}
	} else {
		cancel = backPath;
	}

	//formにセットする初期アクション（Enter押下時に実行）
	String defaultAction = "";
	if (Constants.EXEC_TYPE_UPDATE.equals(execType)) {
		if (StringUtil.isNotBlank(form.getUpdateActionName())) {
			defaultAction = contextPath + "/" + form.getUpdateActionName() + urlPath;
		} else {
			defaultAction = contextPath + "/" + UpdateCommand.UPDATE_ACTION_NAME + urlPath;
		}
	} else {
		if (StringUtil.isNotBlank(form.getInsertActionName())) {
			defaultAction = contextPath + "/" + form.getInsertActionName() + urlPath;
		} else {
			defaultAction = contextPath + "/" + InsertCommand.INSERT_ACTION_NAME + urlPath;
		}
	}

	//各プロパティでの権限チェック用に定義名をリクエストに保存
	request.setAttribute(Constants.DEF_NAME, defName);
	request.setAttribute(Constants.ROOT_DEF_NAME, defName);	//NestTableの場合にDEF_NAMEが置き換わるので別名でRootのDefNameをセット

	//section以下で参照するパラメータ
	request.setAttribute(Constants.OUTPUT_TYPE, type);
	request.setAttribute(Constants.ENTITY_DATA, data.getEntity());
	request.setAttribute(Constants.ENTITY_DEFINITION, data.getEntityDefinition());
	request.setAttribute(Constants.EXEC_TYPE, execType);

	//ボタンのカスタムスタイル用のscriptKey
	request.setAttribute(Constants.FORM_SCRIPT_KEY, form.getScriptKey());
%>

<h2 class="hgroup-01">
<span class="<c:out value="<%=imageColorStyle%>"/>">
<%if (StringUtil.isNotBlank(iconTag)) {%>
<%=iconTag%>
<%} else {%>
<i class="far fa-circle default-icon"></i>
<%} %>
</span>
<c:out value="<%= displayName %>"/>
</h2>
<%-- XSS対応-メタの設定のため対応なし(displayName) --%>
<div class="generic_detail detail_edit d_<c:out value="<%=className %>"/>">
<%@include file="../../layout/resource/mediaelementResource.jsp" %>
<script type="text/javascript">
separator = "<%= separator %>";

function confirm_delete(action, target) {
	if (confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.deleteMsg')}")) {
		button_onclick(action);
		if (target) $(target).prop("disabled", true);
	}
}
function onclick_save(action, target) {
	button_onclick(action);
	if (target) $(target).prop("disabled", true);
}
function onclick_insert(action, target) {
	button_onclick(action);
	if (target) $(target).prop("disabled", true);
}
function button_onclick(action, hidden) {
	var $form = $("#detailForm");
	if (hidden) {
		if ($form) {
			for (var keyString in hidden) {
				$form.prepend("<input type='hidden\' name='" + keyString + "' value='" + hidden[keyString] + "'/>");
			}
		}
	}
	$form.attr("action", contextPath + "/" + action).submit();
}
function cancel() {
	if ($("#confirmEditCancel").val() == "true" && !confirm("${m:rs('mtp-gem-messages', 'generic.detail.detail.cancelMsg')}")) {
		return;
	}

	submitForm(contextPath + "/<%=StringUtil.escapeJavaScript(cancel)%>", {
		<%=Constants.SEARCH_COND%>:$(":hidden[name='searchCond']").val(),
		<%=Constants.TOPVIEW_LIST_OFFSET%>:"<%=StringUtil.escapeJavaScript(topViewListOffset)%>"
	});
}
</script>
<%
	if (form.isValidJavascriptDetailPage() && StringUtil.isNotBlank(form.getJavaScript())) {
		//View定義で設定されたJavaScript
%>
<script type="text/javascript">
<%=form.getJavaScript()%><%-- XSS対応-メタの設定のため対応なし(form.getJavaScript) --%>
</script>
<%
	}
%>
<h3 class="hgroup-02 hgroup-02-01"><%= GemResourceBundleUtil.resourceString("generic.detail.detail.edit", displayName) %></h3>
<%
	if (StringUtil.isNotBlank(message)) {
%>
<span class="error">
<c:out value="<%= message %>"/>
</span>
<%
	}
%>
<form id="detailForm" method="post" action="<c:out value="<%=defaultAction%>"/>">
${m:outputToken('FORM_XHTML', true)}
<input type="hidden" name="defName" value="<c:out value="<%=defName%>"/>" />
<input type="hidden" name="searchCond" value="<c:out value="<%=searchCond%>"/>" />
<input type="hidden" name="execType" value="<c:out value="<%=execType%>"/>" />
<input type="hidden" name="backPath" value="<c:out value="<%=backPath%>"/>" />
<input type="hidden" name="topViewListOffset" value="<c:out value="<%=topViewListOffset%>"/>" />
<%
	if (newversion != null) {
%>
<input type="hidden" name="newversion" value="true" />
<%
	}
	if (oid != null) {
%>
<input type="hidden" name="oid" value="<c:out value="<%=oid%>"/>" />
<%
	}
	if (updateDate != null) {
%>
<input type="hidden" name="timestamp" value="<c:out value="<%=updateDate%>"/>" />
<%
	}
	if (version != null) {
%>
<input type="hidden" name="version" value="<c:out value="<%=version%>"/>" />
<%
	}
%>
<input type="hidden" id="confirmEditCancel" value="<%=ViewUtil.isConfirmEditCancel()%>" />
<%-- ページトップ部分のボタン --%>
<div class="operation-bar operation-bar_top">
<jsp:include page="editButton.inc.jsp" />

<ul class="nav-disc-all">
<li class="all-open"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allOpen")}</a></li>
<li class="all-close"><a href="#">${m:rs("mtp-gem-messages", "generic.detail.detail.allClose")}</a></li>
</ul>
</div><!--operation-bar-->
<%
	request.setAttribute(Constants.NAV_SECTION, form.getSections());
%>
<jsp:include page="sectionNavi.inc.jsp" />
<%
	for (Section section : form.getSections()) {
		if (!section.isDispFlag() || !ViewUtil.dispElement(section)) continue;
		request.setAttribute(Constants.ELEMENT, section);

		String path = EntityViewUtil.getJspPath(section, ViewConst.DESIGN_TYPE_GEM);
		if (path != null) {
%>
<jsp:include page="<%=path %>" />
<%
		}
	}
%>

<%-- ページボトム部分のボタン --%>
<div class="operation-bar operation-bar_bottom">
<jsp:include page="editButton.inc.jsp" />
</div><!--operation-bar-->
</form>
</div>
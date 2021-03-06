/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 * 
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.impl.command.InterceptorService;
import org.iplass.mtp.impl.command.MetaCommand;
import org.iplass.mtp.impl.command.MetaCommand.CommandRuntime;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.ParameterValueMap;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebProcessRuntimeException;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.actionmapping.ParamMap.ParamMapRuntime;
import org.iplass.mtp.impl.web.actionmapping.Result.ResultRuntime;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria;
import org.iplass.mtp.impl.web.actionmapping.cache.MetaCacheCriteria.CacheCriteriaRuntime;
import org.iplass.mtp.impl.web.template.TemplateService;
import org.iplass.mtp.impl.web.template.MetaTemplate.TemplateRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.actionmapping.definition.ClientCacheType;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.iplass.mtp.web.actionmapping.definition.ParamMapDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.interceptor.RequestInterceptor;

/**
 * ActionMappingの定義。
 *
 * @author K.Higuchi
 */
public class MetaActionMapping extends BaseRootMetaData implements DefinableMetaData<ActionMappingDefinition> {

	//FIXME Intercepter単位に設定のプラグインが出来る機構に。



	private static final long serialVersionUID = -9160640479328183543L;

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String HEAD = "HEAD";
	private static final String OPTIONS = "OPTIONS";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";
	private static final String TRACE = "TRACE";

	/** クライアントでのキャッシュ指定。未指定の場合はキャッシュを許す。 */
	private ClientCacheType clientCacheType;

	/** このActionMappingで指定される表示処理が部品かどうか。trueの場合、クライアントからの直接呼出し不可。 */
	private boolean isParts;

	/** このActionMappingで処理されるCommand,Templateを特権（セキュリティ制約を受けない）にて処理するかどうか。デフォルトはfalse。 */
	private boolean isPrivilaged = false;

	/** このActionの呼び出しをセキュリティ設定によらず呼び出し可能にする場合は、trueを設定。 isPrivilagedとの違いは、Entityの操作などにおいては、セキュリティ制約を受ける。デフォルトはfalse。*/
	private boolean isPublicAction;

	/** このActionMappingが呼び出されたときに実行するCommand。未指定可（Commandは実行せずテンプレートを表示）。 */
	// private String commandId;
	private MetaCommand command;

	/** WebからのパラメータのCommand実行時のParameter名のマップの定義 */
	private ParamMap[] paramMap;

	/**
	 * Commandの実行結果と、表示処理の対応の定義。*指定可（実行したCommand結果によらず当該表示処理を実行）。未指定可（
	 * このActionMappingのnameと同一名のTemplateを表示）。
	 */
	private Result[] result;// null可。nullの場合、nameと同一の名前のTemplateを表示するよう動作する

	/** Tokenチェックの実行設定。未指定可(Tokenチェックは実行されない)。 */
	private MetaTokenCheck tokenCheck;

	/** キャッシュ基準 */
	private MetaCacheCriteria cacheCriteria;

	/** sessionにて同期を行うか否か */
	private boolean synchronizeOnSession;

	/** クライアントキャッシュのmax-age（秒）*/
	private long clientCacheMaxAge = -1;

	/** 許可するHTTP Method。未指定の場合は、すべて許可。 */
	private HttpMethodType[] allowMethod;

	private boolean needTrustedAuthenticate;

	public boolean isNeedTrustedAuthenticate() {
		return needTrustedAuthenticate;
	}

	public void setNeedTrustedAuthenticate(boolean needTrustedAuthenticate) {
		this.needTrustedAuthenticate = needTrustedAuthenticate;
	}

	/**
	 * @see #setAllowMethod(HttpMethodType[])
	 * @return
	 */
	public HttpMethodType[] getAllowMethod() {
		return allowMethod;
	}

	/**
	 * 許可するHTTP Methodを指定。未指定の場合は、すべて許可。
	 *
	 * @param allowMethod
	 */
	public void setAllowMethod(HttpMethodType[] allowMethod) {
		this.allowMethod = allowMethod;
	}

	public long getClientCacheMaxAge() {
		return clientCacheMaxAge;
	}

	public void setClientCacheMaxAge(long clientCacheMaxAge) {
		this.clientCacheMaxAge = clientCacheMaxAge;
	}

	public boolean isSynchronizeOnSession() {
		return synchronizeOnSession;
	}

	public void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
	}


	public MetaCacheCriteria getCacheCriteria() {
		return cacheCriteria;
	}

	public void setCacheCriteria(MetaCacheCriteria cacheCriteria) {
		this.cacheCriteria = cacheCriteria;
	}

	public boolean isPublicAction() {
		return isPublicAction;
	}

	public void setPublicAction(boolean isPublicAction) {
		this.isPublicAction = isPublicAction;
	}

	public boolean isPrivilaged() {
		return isPrivilaged;
	}

	public void setPrivilaged(boolean isPrivilaged) {
		this.isPrivilaged = isPrivilaged;
	}

	public ParamMap[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(ParamMap[] paramMap) {
		this.paramMap = paramMap;
	}

	public boolean isParts() {
		return isParts;
	}

	public void setParts(boolean isParts) {
		this.isParts = isParts;
	}

	public ClientCacheType getClientCacheType() {
		return clientCacheType;
	}

	public void setClientCacheType(ClientCacheType clientCacheType) {
		this.clientCacheType = clientCacheType;
	}

	public MetaCommand getCommand() {
		return command;
	}

	public void setCommand(MetaCommand command) {
		this.command = command;
	}

	public Result[] getResult() {
		return result;
	}

	public void setResult(Result[] result) {
		this.result = result;
	}

	public MetaTokenCheck getTokenCheck() {
		return tokenCheck;
	}

	public void setTokenCheck(MetaTokenCheck tokenCheck) {
		this.tokenCheck = tokenCheck;
	}

	public ActionMappingRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new ActionMappingRuntime(metaDataConfig);
	}

	public MetaActionMapping copy() {
		return ObjectUtil.deepCopy(this);
	}

	//Definition → Meta
	public void applyConfig(ActionMappingDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());
		description = definition.getDescription();

		clientCacheType = definition.getClientCacheType();
		clientCacheMaxAge = definition.getClientCacheMaxAge();

		isParts = definition.isParts();
		isPrivilaged = definition.isPrivilaged();
		isPublicAction = definition.isPublicAction();

		if (definition.getCommandConfig() != null) {
			command = MetaCommand.createInstance(definition.getCommandConfig());
			command.applyConfig(definition.getCommandConfig());
		} else {
			command = null;
		}

		if (definition.getParamMap() != null) {
			paramMap = new ParamMap[definition.getParamMap().length];
			int i = 0;
			for (ParamMapDefinition paramDef : definition.getParamMap()) {
				paramMap[i] = new ParamMap();
				paramMap[i].applyConfig(paramDef);
				i++;
			}
		} else {
			paramMap = null;
		}

		if (definition.getResult() != null) {
			result = new Result[definition.getResult().length];
			int i = 0;
			for (ResultDefinition resultDef : definition.getResult()) {
				result[i] = Result.createInstance(resultDef);
				result[i].applyConfig(resultDef);
				i++;
			}
		} else {
			result = null;
		}

		if (definition.getTokenCheck() != null) {
			tokenCheck = new MetaTokenCheck();
			tokenCheck.applyConfig(definition.getTokenCheck());
		} else {
			tokenCheck = null;
		}

		if (definition.getCacheCriteria() != null) {
			cacheCriteria = MetaCacheCriteria.createInstance(definition.getCacheCriteria());
			cacheCriteria.applyConfig(definition.getCacheCriteria());
		} else {
			cacheCriteria = null;
		}

		synchronizeOnSession = definition.isSynchronizeOnSession();

		if (definition.getAllowMethod() != null) {
			allowMethod = new HttpMethodType[definition.getAllowMethod().length];
			System.arraycopy(definition.getAllowMethod(), 0, allowMethod, 0, allowMethod.length);
		} else {
			allowMethod = null;
		}

		needTrustedAuthenticate = definition.isNeedTrustedAuthenticate();
	}

	//Meta → Definition
	public ActionMappingDefinition currentConfig() {
		ActionMappingDefinition definition = new ActionMappingDefinition();

		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		definition.setDescription(description);

		definition.setClientCacheType(clientCacheType);
		definition.setClientCacheMaxAge(clientCacheMaxAge);
		definition.setParts(isParts);
		definition.setPrivilaged(isPrivilaged);
		definition.setPublicAction(isPublicAction);

		if (command != null) {
			definition.setCommandConfig(command.currentConfig());
		}

		if (paramMap != null) {
			ParamMapDefinition[] paramMapDefinition = new ParamMapDefinition[paramMap.length];
			int i = 0;
			for (ParamMap map : paramMap) {
				paramMapDefinition[i] = map.currentConfig();
				i++;
			}
			definition.setParamMap(paramMapDefinition);
		}

		if (result != null) {
			ResultDefinition[] resultDefinition = new ResultDefinition[result.length];
			int i = 0;
			for (Result res : result) {
				resultDefinition[i] = res.currentConfig();
				i++;
			}
			definition.setResult(resultDefinition);
		}

		if (tokenCheck != null) {
			definition.setTokenCheck(tokenCheck.currentConfig());
		}

		if (cacheCriteria != null) {
			definition.setCacheCriteria(cacheCriteria.currentConfig());
		}

		definition.setSynchronizeOnSession(synchronizeOnSession);

		if (allowMethod != null) {
			definition.setAllowMethod(new HttpMethodType[allowMethod.length]);
			System.arraycopy(allowMethod, 0, definition.getAllowMethod(), 0, allowMethod.length);
		} else {
			definition.setAllowMethod(null);
		}

		definition.setNeedTrustedAuthenticate(needTrustedAuthenticate);

		return definition;
	}

	public class ActionMappingRuntime extends BaseMetaDataRuntime {

		private CommandRuntime cmd;
		private ResultRuntime defaultResult;
		private ResultRuntime[] resultList;
		private Map<String, ResultRuntime> exceptionResultMap;
		private HashMap<String, List<ParamMapRuntime>> paramMapRuntimes;

		private RequestInterceptor[] reqInterceptors;
		private CommandInterceptor[] cmdInterceptors;

		private ClientCacheType clientCacheTypeRuntime;

		private CacheCriteriaRuntime cacheCriteriaRuntime;

		public ActionMappingRuntime(MetaDataConfig metaDataConfig) {

			try {
				if (command != null) {
					cmd = command.createRuntime();
				}

				TemplateService ts = ServiceRegistry.getRegistry().getService(
						TemplateService.class);
				TemplateRuntime tr = ts.getRuntimeByName(name);
				if (tr != null) {
					defaultResult = new TemplateResult("*", tr.getMetaData()
							.getId()).createRuntime();
				}

				if (result != null) {
					resultList = new ResultRuntime[result.length];
					for (int i = 0; i < result.length; i++) {
						resultList[i] = result[i].createRuntime();
						if ("*".equals(result[i].getCommandResultStatus())) {
							defaultResult = resultList[i];
						}
						if (result[i].getExceptionClassName() != null) {
							if (exceptionResultMap == null) {
								exceptionResultMap = new HashMap<>();
							}
							exceptionResultMap.put(result[i].getExceptionClassName(), resultList[i]);
						}
					}
				}

				if (paramMap != null) {
					paramMapRuntimes = new HashMap<>();
					for (ParamMap p: paramMap) {
						ParamMapRuntime pmr = p.createRuntime();
						List<ParamMapRuntime> pmrList = paramMapRuntimes.get(p.getName());
						if (pmrList == null) {
							pmrList = new LinkedList<>();
							paramMapRuntimes.put(p.getName(), pmrList);
						}
						pmrList.add(pmr);
					}
				}

				//デフォルト設定の読み込み
				WebFrontendService wfs = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
				if (getClientCacheType() == null) {
					clientCacheTypeRuntime = wfs.getDefaultClientCacheType();
				} else {
					clientCacheTypeRuntime = getClientCacheType();
				}

				//キャッシュ基準
				if (cacheCriteria != null) {
					cacheCriteriaRuntime = cacheCriteria.createRuntime(MetaActionMapping.this);
				}

				//TODO カスタムインターセプターの追加
				InterceptorService is = ServiceRegistry.getRegistry().getService(InterceptorService.class);
				cmdInterceptors = is.getInterceptors(ActionMappingService.COMMAND_INTERCEPTOR_NAME);
				ActionMappingService as = ServiceRegistry.getRegistry().getService(ActionMappingService.class);
				reqInterceptors = as.getInterceptors();
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}

		}

		public MetaActionMapping getMetaData() {
			return MetaActionMapping.this;
		}

		public ResultRuntime getResult(String cmdResult) {
			checkState();

			ResultRuntime result = null;
			if (resultList == null) {
				result = defaultResult;
			} else {
				for (ResultRuntime r : resultList) {
					if (r.getMetaData().getCommandResultStatus() != null && r.getMetaData().getCommandResultStatus().equals(cmdResult)) {
						result = r;
						break;
					}
				}
				if (result == null && defaultResult != null) {
					result = defaultResult;
				}
			}
			if (result == null) {
				throw new ActionMappingRuntimeException(
						"no define result mapping:" + cmdResult + " of ActionMapping:" + getMetaData().getName());
			}
			return result;
		}

		public boolean isAllowedMethod(HttpMethodType requestMethod) {
			if (allowMethod == null || allowMethod.length == 0) {
				return true;
			}
			for (HttpMethodType m: allowMethod) {
				if (m == requestMethod) {
					return true;
				}
			}
			return false;
		}

		private void doOptions(WebRequestStack req) {
			String allow = allowString();
			req.getResponse().setHeader("Allow", allow);
		}

		private String allowString() {
			String allow;
			if (allowMethod == null || allowMethod.length == 0) {
				allow = GET + ", " + HEAD + ", " + POST + ", " + PUT + ", " + DELETE + ", " + TRACE + ", " + OPTIONS;
			} else {
				StringBuilder sb = new StringBuilder();
				for (HttpMethodType m: allowMethod) {
					if (sb.length() != 0) {
						sb.append(", ");
					}
					switch (m) {
					case GET:
						sb.append(GET + ", " + HEAD);
						break;
					case POST:
						sb.append(POST);
						break;
					case PUT:
						sb.append(PUT);
						break;
					case DELETE:
						sb.append(DELETE);
						break;
					default:
						break;
					}
				}
				sb.append(", " + TRACE + ", " + OPTIONS);
				allow = sb.toString();
			}
			return allow;
		}

		private void doTrace(WebRequestStack req) throws IOException {
			int responseLength;

			String CRLF = "\r\n";
			StringBuilder buffer = new StringBuilder("TRACE ").append(req.getRequest().getRequestURI())
					.append(" ").append(req.getRequest().getProtocol());

			Enumeration<String> reqHeaderEnum = req.getRequest().getHeaderNames();

			while( reqHeaderEnum.hasMoreElements() ) {
				String headerName = reqHeaderEnum.nextElement();
				buffer.append(CRLF).append(headerName).append(": ")
					.append(req.getRequest().getHeader(headerName));
			}

			buffer.append(CRLF);

			responseLength = buffer.length();

			req.getResponse().setContentType("message/http");
			req.getResponse().setContentLength(responseLength);
			ServletOutputStream out = req.getResponse().getOutputStream();
			out.print(buffer.toString());
			out.close();
		}

		public void executeCommand(final WebRequestStack req) throws IOException, ServletException {
			checkState();

			String httpMethod = req.getRequest().getMethod();
			HttpMethodType forCheck = null;
			switch (httpMethod) {
			case TRACE:
				doTrace(req);
				return;//no process action
			case OPTIONS:
				doOptions(req);
				return;//no process action
			case DELETE:
				forCheck = HttpMethodType.DELETE;
				break;
			case HEAD:
				req.setResponse(new NoBodyResponse(req.getResponse()));
				forCheck = HttpMethodType.GET;
				break;
			case GET:
				forCheck = HttpMethodType.GET;
				break;
			case POST:
				forCheck = HttpMethodType.POST;
				break;
			case PUT:
				forCheck = HttpMethodType.PUT;
				break;
			default:
				req.getResponse().sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	            return;
			}

			if (!isAllowedMethod(forCheck)) {
		        String protocol = req.getRequest().getProtocol();
		        if (protocol.endsWith("1.1")) {
					req.getResponse().setHeader("Allow", allowString());
		        	req.getResponse().sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		        } else {
		        	req.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
		        }
		        return;
			}


			// 部品の場合、クライアントからの直接呼出し不可
			if (isParts && req.isClientDirectRequest()) {
				throw new WebProcessRuntimeException(name + " is Parts.Can not direct call.");
			}

			// クライアントキャッシュの設定

			//TODO
			//Last-Modified:Tue, 16 Oct 2012 11:32:43 GMT
			//max-age=3600 <= ファイルに設定
			//サーバキャッシュ有効の場合は、Last-ModifiedとETagを設定
			//ETag: "10c24bc-4ab-457e1c1f"

			switch (clientCacheTypeRuntime) {
			case CACHE:
				WebUtil.setCacheControlHeader(req, true, clientCacheMaxAge);
				break;
			case NO_CACHE:
				WebUtil.setCacheControlHeader(req, false, -1);
				break;
			default:
				break;
			}

			RequestContext requestContext = req.getRequestContext();
			//cache tenantContextPath( and for implicit object: "tenantContextPath" in JSP)
			requestContext.setAttribute(WebRequestConstants.TENANT_CONTEXT_PATH, req.getRequestPath().getTenantContextPath(req.getRequest()));
			
			if (requestContext instanceof WebRequestContext) {
				WebRequestContext webRequestContext = (WebRequestContext) requestContext;
				ParameterValueMap currentValueMap = webRequestContext.getValueMap();
				if (paramMap != null) {
					VariableParameterValueMap variableValueMap = new VariableParameterValueMap(currentValueMap, req.getRequestPath(), this);
					webRequestContext.setValueMap(variableValueMap);
				}
			}

			WebInvocationImpl invoke =  new WebInvocationImpl(reqInterceptors, cmdInterceptors, cmd, requestContext, req, this);

			if (isSynchronizeOnSession()) {
				synchronized (requestContext.getSession()) {
					invoke.proceedRequest();
				}
			} else {
				invoke.proceedRequest();
			}
		}
		
		public Map<String, List<ParamMapRuntime>> getParamMapRuntimes() {
			checkState();
			return paramMapRuntimes;
		}

		public CacheCriteriaRuntime getCacheCriteria() {
			return cacheCriteriaRuntime;
		}

		public ResultRuntime getResult(Throwable t) {
			if (exceptionResultMap != null) {
				for (Class<?> tc = t.getClass(); tc != null && tc != Object.class; tc = tc.getSuperclass()) {
					ResultRuntime rr = exceptionResultMap.get(tc.getName());
					if (rr != null) {
						return rr;
					}
				}
			}
			return null;
		}

	}

}

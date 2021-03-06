/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.delete;

import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.search.DetailSearchCommand;
import org.iplass.gem.command.generic.search.FixedSearchCommand;
import org.iplass.gem.command.generic.search.NormalSearchCommand;
import org.iplass.gem.command.generic.search.SearchCommandBase;
import org.iplass.gem.command.generic.search.SearchContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity全削除コマンド
 * @author lis3wg
 */
@WebApi(
	name=DeleteAllCommand.WEBAPI_NAME,
	displayName="全削除",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={Constants.MESSAGE},
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/DeleteAllCommand", displayName="全削除")
public final class DeleteAllCommand extends DeleteCommandBase {

	private static Logger logger = LoggerFactory.getLogger(DeleteAllCommand.class);

	public static final String WEBAPI_NAME = "gem/generic/delete/deleteAll";

	@Override
	public String execute(final RequestContext request) {

		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}

		if (command != null) {
			SearchContext context = command.getContext(request);

			Query query = new Query();
			query.select(Entity.OID, Entity.VERSION);
			query.from(context.getDefName());
			query.setWhere(context.getWhere());
			query.setVersiond(context.isVersioned());
			SearchResult<Entity> result = em.searchEntity(query);

			boolean isPurge = isPurge(context);
			final DeleteOption option = new DeleteOption(false);
			option.setPurge(isPurge);

			//大量データを考慮してトランザクションを分割(100件毎)
			List<Entity> list = result.getList();
			int count = list.size();
			int countPerHundret = count / 100;
			if (count % 100 > 0) countPerHundret++;
			int current = 0;
			for (int i = 0; i < countPerHundret; i++) {
				current = i * 100;
				int last = current + 100;
				if (last > list.size()) last = list.size();
				final List<Entity> subList = list.subList(current, last);
				Boolean ret = Transaction.requiresNew(t -> {
					for (Entity entity : subList) {
						try {
							em.delete(entity, option);
						} catch (ApplicationException e) {
							if (logger.isDebugEnabled()) {
								logger.debug(e.getMessage(), e);
							}
							request.setAttribute(Constants.MESSAGE, e.getMessage());
							t.rollback();
							return false;
						}
					}
					return true;
				});
				if (!ret) {
					break;
				}
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

	private boolean isPurge(SearchContext context) {
		boolean isPurge = false;
		EntityView entityView = context.getEntityView();
		String viewName = context.getRequest().getParam(Constants.VIEW_NAME);
		SearchFormView view = null;
		if (viewName == null || viewName.equals("")) {
			//デフォルトレイアウトを利用
			if (entityView != null && entityView.getSearchFormViewNames().length > 0) {
				view = entityView.getDefaultSearchFormView();
			}
		} else {
			//指定レイアウトを利用
			if (entityView != null) view = entityView.getSearchFormView(viewName);
		}
		if (view != null) isPurge = view.isPurge();
		return isPurge;
	}

}

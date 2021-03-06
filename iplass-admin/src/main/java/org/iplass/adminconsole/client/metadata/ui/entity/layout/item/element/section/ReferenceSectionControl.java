/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item.element.section;

import org.iplass.adminconsole.client.base.event.MTPEvent;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.PropertyOperationHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.EntityViewFieldSettingDialog;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.item.ItemControl;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateEvent;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield.MetaFieldUpdateHandler;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.HeaderControls;

/**
 *
 * @author lis3wg
 */
public class ReferenceSectionControl extends ItemControl implements SectionControl {

	public static final String SECTION_SUFFIX = "_section";
	public static final String SECTION_COUNT_KEY = "_section_count";

	/** Window破棄前にプロパティの重複チェックリストから削除するためのハンドラ */
	private PropertyOperationHandler handler = null;
	private MetaDataServiceAsync service = null;

	private String loadedDisplayLabel = null;

	/**
	 * コンストラクタ
	 * @param defName
	 */
	public ReferenceSectionControl(final String defName, FieldReferenceType triggerType, final ReferenceSection section) {
		super(defName, triggerType);

		service = MetaDataServiceFactory.get();

		setBackgroundColor("#99FFCC");
		setDragType("section");
		setHeight(22);
		setBorder("1px solid navy");

		setValue("name", section.getPropertyName());

		setHeaderControls(HeaderControls.MINIMIZE_BUTTON, HeaderControls.HEADER_LABEL, setting, HeaderControls.CLOSE_BUTTON);

		setMetaFieldUpdateHandler(new MetaFieldUpdateHandler() {

			@Override
			public void execute(MetaFieldUpdateEvent event) {
				String title = null;
				if (event.getValueMap().containsKey("title")) {
					title = (String) event.getValueMap().get("title");
				}

				createTitle(title);
			}
		});

		setClassName(section.getClass().getName());
		setValueObject(section);

		getDisplayLabel(defName, section.getPropertyName());
	}

	private void getDisplayLabel(String defName, final String propertyName) {
		service.getPropertyDisplayName(TenantInfoHolder.getId(), defName, propertyName, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) {
				loadedDisplayLabel = result;
				createTitle(getSection().getTitle());
			}

		});
	}

	private void createTitle(String title) {
		if (title != null) {
			if (title.equals(loadedDisplayLabel)) {
				setTitle(title);
			} else {
				setTitle(title + "(" + loadedDisplayLabel + ")");
			}
		} else {
			setTitle(loadedDisplayLabel);
		}
	}

	/**
	 * チェック用ハンドラの設定。
	 * @param handler
	 */
	public void setHandler(PropertyOperationHandler handler) {
		this.handler = handler;
	}

	/**
	 * ウィンドウ破棄前の処理。
	 */
	@Override
	protected boolean onPreDestroy() {
		if (handler != null) {
			Object name = getValue("name");

			if (handler.getContext().get(name + SECTION_COUNT_KEY) != null) {
				Integer count = (Integer) handler.getContext().get(name + SECTION_COUNT_KEY);
				if (count > 1) {
					handler.getContext().set(name + SECTION_COUNT_KEY, --count);
				} else {
					// 同一プロパティ名のセクションが消えたらhandlerからプロパティ名削除
					MTPEvent propCheck = new MTPEvent();
					propCheck.setValue("name", name);
					handler.remove(propCheck);

					MTPEvent sectionCheck = new MTPEvent();
					sectionCheck.setValue("name", name + SECTION_SUFFIX);
					handler.remove(sectionCheck);

					handler.getContext().set(name + SECTION_COUNT_KEY, null);
				}
			}
		}
		return true;
	}

	/* (非 Javadoc)
	 * @see org.iplass.adminconsole.client.metadata.ui.entity.layout.item.SectionWindow#getSection()
	 */
	@Override
	public ReferenceSection getSection() {
		ReferenceSection section = (ReferenceSection) getValueObject();
		return section;
	}

	@Override
	protected EntityViewFieldSettingDialog createSubDialog() {
		ReferenceSection section = getSection();
		return new EntityViewFieldSettingDialog(getClassName(), getValueObject(), triggerType, defName, section.getDefintionName());
	}

}

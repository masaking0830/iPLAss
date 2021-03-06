/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaDataIllegalStateException;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

public class MetaDateRangePropertyEditor extends MetaCustomPropertyEditor {

	/** SerialVersionUID */
	private static final long serialVersionUID = 7750500799495855836L;

	public static MetaDateRangePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaDateRangePropertyEditor();
	}

	/** オブジェクトID */
	private String objectId;

	/** プロパティエディタ */
	private MetaPropertyEditor editor;

	/** Toプロパティエディタ */
	private MetaPropertyEditor toEditor;

	/** ToプロパティID */
	private String toPropertyId;

	/** エラーメッセージ */
	private String errorMessage;

	/** エラーメッセージ多言語設定情報 */
	private List<MetaLocalizedString> localizedErrorMessageList = new ArrayList<MetaLocalizedString>();

	/**
	 * オブジェクトIDを取得します。
	 * @return オブジェクトID
	 */
	public String getObjectId() {
	    return objectId;
	}

	/**
	 * オブジェクトIDを設定します。
	 * @param objectId オブジェクトID
	 */
	public void setObjectId(String objectId) {
	    this.objectId = objectId;
	}

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	public MetaPropertyEditor getEditor() {
	    return editor;
	}

	/**
	 * プロパティエディタを設定します。
	 * @param editor プロパティエディタ
	 */
	public void setEditor(MetaPropertyEditor editor) {
	    this.editor = editor;
	}

	/**
	 * Toプロパティエディタを取得します。
	 * @return Toプロパティエディタ
	 */
	public MetaPropertyEditor getToEditor() {
	    return toEditor;
	}

	/**
	 * Toプロパティエディタを設定します。
	 * @param toEditor Toプロパティエディタ
	 */
	public void setToEditor(MetaPropertyEditor toEditor) {
	    this.toEditor = toEditor;
	}

	/**
	 * @return toPropertyId
	 */
	public String getToPropertyId() {
		return toPropertyId;
	}

	/**
	 * @param toPropertyId セットする toPropertyId
	 */
	public void setToPropertyId(String toPropertyId) {
		this.toPropertyId = toPropertyId;
	}

	/**
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage セットする errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return localizedErrorMessageList
	 */
	public List<MetaLocalizedString> getLocalizedErrorMessageList() {
		return localizedErrorMessageList;
	}

	/**
	 * @param localizedErrorMessageList セットする localizedErrorMessageList
	 */
	public void setLocalizedErrorMessageList(List<MetaLocalizedString> localizedErrorMessageList) {
		this.localizedErrorMessageList = localizedErrorMessageList;
	}

	@Override
	public void applyConfig(PropertyEditor _editor) {
		super.fillFrom(_editor);

		DateRangePropertyEditor e = (DateRangePropertyEditor) _editor;

		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerByName(e.getObjectName());

		objectId = entity.getMetaData().getId();
		if (e.getEditor() != null) {
			editor = MetaPropertyEditor.createInstance(e.getEditor());
			editor.applyConfig(e.getEditor());
		}
		if (e.getToEditor() != null) {
			toEditor = MetaPropertyEditor.createInstance(e.getEditor());
			toEditor.applyConfig(e.getToEditor());
		}

		PropertyHandler property = entity.getProperty(e.getToPropertyName(), metaContext);
		if (property != null) {
			toPropertyId = property.getId();
		} else {
			throw new MetaDataIllegalStateException("to property name is not defined.");
		}

		errorMessage = e.getErrorMessage();
		if (e.getLocalizedErrorMessageList() != null && !e.getLocalizedErrorMessageList().isEmpty()) {
			localizedErrorMessageList = I18nUtil.toMeta(e.getLocalizedErrorMessageList());
		}
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		//対象Entityの存在チェック
		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerById(objectId);
		if (entity == null) {
			return null;
		}
		DateRangePropertyEditor _editor = new DateRangePropertyEditor();
		super.fillTo(_editor);

		_editor.setObjectName(entity.getMetaData().getName());
		if (editor != null) {
			_editor.setEditor(editor.currentConfig(propertyName));
		}
		if (toEditor != null) {
			_editor.setToEditor(toEditor.currentConfig(propertyName));
		}

		PropertyHandler property = entity.getPropertyById(toPropertyId, metaContext);
		if (property != null) {
			_editor.setToPropertyName(property.getName());
		}

		_editor.setErrorMessage(errorMessage);
		if (localizedErrorMessageList != null && !localizedErrorMessageList.isEmpty()) {
			_editor.setLocalizedErrorMessageList(I18nUtil.toDef(localizedErrorMessageList));
		}

		return _editor;
	}

	@Override
	public MetaDateRangePropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

}

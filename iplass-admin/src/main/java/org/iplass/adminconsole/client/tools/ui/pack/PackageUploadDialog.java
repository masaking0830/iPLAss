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

package org.iplass.adminconsole.client.tools.ui.pack;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.upload.AdminUploadStatus;
import org.iplass.adminconsole.client.base.io.upload.CustomUploaderConstants;
import org.iplass.adminconsole.client.base.io.upload.UploadResultInfo;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.MessageTabSet;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.tools.dto.pack.PackageUploadProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;
import gwtupload.client.Utils;

/**
 * Packageファイルをアップロードするダイアログ
 */
public class PackageUploadDialog extends AbstractWindow {

	private static final String UPLOAD_SERVICE = "service/packageupload";

	private IButton upload;
	private IButton cancel;

	private MessageTabSet messageTabSet;

	private PackageListPane owner;

	/**
	 * コンストラクタ
	 */
	public PackageUploadDialog(PackageListPane owner) {
		this.owner = owner;

		setWidth(700);
		setMinWidth(500);
		setHeight(500);
		setMinHeight(400);
		setTitle("Upload Package");
		setCanDragResize(true);
		setShowMinimizeButton(false);
		setShowMaximizeButton(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		//------------------------
		//Input Items
		//------------------------
		//Label title = new Label("Upload package archive file.");
		Label title = new Label(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_uploadPackageFile"));
		title.setHeight(20);
		title.setWidth100();

		HLayout fileComposit = new HLayout(5);
		fileComposit.setMargin(10);
		fileComposit.setWidth100();
		fileComposit.setHeight(25);

		Label fileLabel = new Label("File :");
		fileLabel.setWrap(false);
		//fileLabel.setAutoWidth();
		fileLabel.setWidth(70);
		fileLabel.setAlign(Alignment.RIGHT);
		fileComposit.addMember(fileLabel);

		//第2引数に指定したラベルに対してメッセージが表示される。利用しないので空実装
		final SingleUploader uploader = new SingleUploader(new AdminUploadStatus(), new com.google.gwt.user.client.ui.Label(""));
		IUploader.UploaderConstants constants = GWT.create(CustomUploaderConstants.class);	//Messageのカスタマイズ
		uploader.setI18Constants(constants);
		uploader.setServletPath(GWT.getModuleBaseURL() + UPLOAD_SERVICE);					//Servlet
		uploader.setAutoSubmit(false);														//自動送信しない
		uploader.setValidExtensions("zip");													//選択可能拡張子
		uploader.getFileInput().asWidget().setHeight("20px");
		uploader.getFileInput().asWidget().setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH + "px");
		uploader.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {
			@Override
			public void onStart(IUploader uploader) {
				debugUploader(uploader, "onStart");
				startUpload();
			}
		});
		uploader.addOnStatusChangedHandler(new IUploader.OnStatusChangedHandler() {

			@Override
			public void onStatusChanged(IUploader uploader) {
				debugUploader(uploader, "onStatusChanged");
			}
		});
		uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploaderResult) {
				debugUploader(uploaderResult, "onFinish");
				if (uploaderResult.getStatus() == Status.SUCCESS) {
					finishUpload(uploaderResult.getServerMessage().getMessage());
				} else {
					//ServerResponseからメッセージ作成
					errorUpload(getErrorMessage(uploaderResult.getServerRawResponse()));
				}

				//Hidden項目の削除（uploader#reset、clearなどではうまくいかないため下の方法でクリア）
				Widget form = uploader.getForm().getWidget();
				if (form instanceof FlowPanel) {
					FlowPanel formPanel = (FlowPanel)form;
					for (int i = formPanel.getWidgetCount() - 1; i >= 0; i--) {
						Widget child = formPanel.getWidget(i);
						if (child instanceof Hidden) {
							formPanel.remove(child);
						}
					}
				}
			}

			private String getErrorMessage(String response) {
				Document doc = XMLParser.parse(response);
				return Utils.getXmlNodeValue(doc, "error");
			}

		});


		fileComposit.addMember(uploader);

		final TextItem nameField = new TextItem("name","Name");
		nameField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		//nameField.setHint("default upload file name");
		nameField.setHint(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_notSpeIsFileName"));

		final TextAreaItem descriptionField = new TextAreaItem("description", "Comment");
//		descriptionField.setWidth("100%");
		descriptionField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);
		descriptionField.setHeight(100);

		DynamicForm form = new DynamicForm();
		form.setMargin(10);
		//form.setHeight100();
		form.setHeight(120);
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(70, "*");
		form.setAutoFocus(true);

		form.setItems(nameField, descriptionField);

		//------------------------
		//Buttons
		//------------------------
		upload = new IButton("Upload");
		upload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (uploader.getFileName() == null || uploader.getFileName().isEmpty()) {
					SC.warn(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_selectFile"));
					return;
				}
				SC.ask(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_confirm"),
						AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_startUploadConf"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							uploader.add(new Hidden(PackageUploadProperty.TENANT_ID, Integer.toString(TenantInfoHolder.getId())));
							uploader.add(new Hidden(PackageUploadProperty.UPLOAD_FILE_NAME, SmartGWTUtil.getStringValue(nameField)));
							uploader.add(new Hidden(PackageUploadProperty.DESCRIPTION, SmartGWTUtil.getStringValue(descriptionField)));
							uploader.submit();
						}
					}

				});
			}
		});
		cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		HLayout execButtons = new HLayout(5);
		execButtons.setMargin(5);
		execButtons.setHeight(20);
		execButtons.setWidth100();
		execButtons.setAlign(VerticalAlignment.CENTER);
		execButtons.setMembers(upload, cancel);

		//------------------------
		//Input Layout
		//------------------------
		VLayout inputLayout = new VLayout(5);
		inputLayout.setMargin(5);
		inputLayout.setHeight(80);

		inputLayout.addMember(title);
		inputLayout.addMember(fileComposit);
		inputLayout.addMember(form);
		inputLayout.addMember(SmartGWTUtil.separator());
		inputLayout.addMember(execButtons);

		//------------------------
		//MessagePane
		//------------------------
		messageTabSet = new MessageTabSet();

		//------------------------
		//Main Layout
		//------------------------
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();
		mainLayout.setMargin(10);

		mainLayout.addMember(inputLayout);
		mainLayout.addMember(messageTabSet);

		addItem(mainLayout);
	}

	private void startUpload() {
		disableComponent(true);
		messageTabSet.clearMessage();
		messageTabSet.setTabTitleProgress();
	}

	private void debugUploader(IUploader uploader, String eventName) {
		UploadedInfo info = uploader.getServerInfo();

		GWT.log(eventName + ": status                   ->" + uploader.getStatus());
		if (info == null) {
			GWT.log(eventName + ": UploadedInfo is Null.");
		} else {
			GWT.log(eventName + ": File name                ->" + info.name);
			GWT.log(eventName + ": File content-type        ->" + info.ctype);
			GWT.log(eventName + ": File size                ->" + info.size);
		}
		GWT.log(eventName + ": message                  ->" + uploader.getServerMessage().getMessage());
	}

	private void finishUpload(String message) {
		//JSON->Result
		final PackageUploadResultInfo result = new PackageUploadResultInfo(message);

		//画面に結果を表示
		if (!result.isFileUploadStatusSuccess()) {
			messageTabSet.setErrorMessage(result.getLog());

			disableComponent(false);
			messageTabSet.setTabTitleNormal();
		} else {
			messageTabSet.setMessage(result.getLog());

			//一覧再描画
			owner.refresh();

			disableComponent(false);
			messageTabSet.setTabTitleNormal();

			SC.ask(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_confirm"),
					AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_performImportContinue"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						//PackageImportダイアログを表示
						PackageImportDialog.showFullScreen(result.fileOid, owner);

						destroy();
					}
				}
			});

		}
	}

	private void errorUpload(String message) {
		messageTabSet.setErrorMessage(AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_errUploading"));

		disableComponent(false);
		messageTabSet.setTabTitleNormal();
	}

	private void disableComponent(boolean disabled) {
		upload.setDisabled(disabled);
		cancel.setDisabled(disabled);
	}

	private class PackageUploadResultInfo extends UploadResultInfo {

		String fileOid;

		public PackageUploadResultInfo(String json) {
			super(json);

			fileOid = getFileUploadFileOid();
		}

		public List<String> getLog() {
			List<String> logs = new ArrayList<String>();
			logs.add(getFileUploadStatusMessage(getStatus()));
			if (getStatusMessages() != null && !getStatusMessages().isEmpty()) {
				logs.add("-----------------------------------");
				logs.addAll(getStatusMessages());
			}
			if (getMessages() != null && !getMessages().isEmpty()) {
				logs.add("-----------------------------------");
				logs.addAll(getMessages());
			}
			logs.add("-----------------------------------");
			return logs;
		}

		private String getFileUploadFileOid() {
			return getValue(PackageUploadProperty.FILE_OID);
		}

		private String getFileUploadStatusMessage(String status) {
			if (PackageUploadProperty.Status.SUCCESS.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_uploadSuccessful");
			} else if (PackageUploadProperty.Status.WARN.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_uploadWarn");
			} else if (PackageUploadProperty.Status.ERROR.name().equals(status)) {
				return AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_uploadErr");
			} else {
				return AdminClientMessageUtil.getString("ui_tools_pack_PackageUploadDialog_notGetUploadResult");
			}
		}
	}

}

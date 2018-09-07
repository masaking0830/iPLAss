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

package org.iplass.adminconsole.server.tools.rpc.entityexplorer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.io.download.AdminCsvWriter;
import org.iplass.mtp.view.generic.DetailFormView;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.BinaryPropertyEditor;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.NumberPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.Link;
import org.iplass.mtp.view.generic.element.ScriptingElement;
import org.iplass.mtp.view.generic.element.TemplateElement;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.generic.element.section.TemplateSection;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;


public class EntityViewCsvWriter extends AdminCsvWriter {

	private static final String[] HEADER_DETAIL_VIEW = {"", "ViewNo", "ViewName","ViewTitle",
		"EditActionName","CancelActionName","InsertActionName","UpdateActionName","DeleteActionName","IsNoneDispCopyButton"
		};

	private static final String[] HEADER_SEARCH_VIEW = {"", "ViewNo", "ViewName","ViewTitle",
		"NewActionName", "EditActionName", "DeleteActionName", "SearchActionName", "SelectActionName", "ViewActionName", "DownloadActionName",
		};

	private static final String[] HEADER_SECTION = {"", "ViewNo", "ViewName",
		"SectionNo", "SectionClass",
		"(Default)ColNum",  "(Default)SectionTitle", "(Default)Id", "(Default)Style",
		"(Scripting)Script", "(Script)Key",
		"(Template)TemplateName",
		"(SearchCondition)ConditionDispCount", "(SearchCondition)HideDetailCondition", "(SearchCondition)SortKey", "(SearchCondition)SortType",
		"(SearchResult)DispRowCount"
		};

	private static final String[] HEADER_SECTION_ELEMENT = {"", "ViewNo", "ViewName",
		"SectionNo", "SectionClass",
		"ElementNo", "ElementClass",
		"ElementTitle",
		"(Template)TemplateName",
		"(Scripting)Script", "(Script)Key",
		"(Link)DisplayLabel", "(Link)Url", "(Link)DispNewWindow",
		"(VirtualProperty)PropertyName", "(VirtualProperty)DisplayLabel", "(VirtualProperty)Style",
		"(VirtualProperty)Description", "(VirtualProperty)Tooltip", "(VirtualProperty)HideDetail", "(VirtualProperty)HideView"
		};

	private static final String[] HEADER_PROPERTY = {"", "ViewNo", "ViewName",
		"SectionNo", "SectionClass",
		"ElementNo",
		"PropertyNo", "PropertyClass", "PropertyName", "DisplayLabel", "Style",
		"Description", "Tooltip", "HideNormalCondition", "HideDetailCondition",
		};

	private static final String[] HEADER_PROPERTY_EDITOR = {"", "ViewNo", "ViewName",
		"SectionNo", "SectionClass",
		"ElementNo",
		"PropertyNo",
		"EditorClass", "PropertyName", "Multiplicity", "DisplayType",
		"(Binary)DownloadActionName", "(Binary)UploadActionName", "(Binary)Height", "(Binary)Width",
		"(Boolean)FalseLabel", "(Boolean)TrueLabel",
		"(Template)TemplateName",
		"(Number)NumberFormat",
		"(Reference)ObjectName", "(Reference)MappedBy", "(Reference)UrlParameter", "(Reference)AddActionName", "(Reference)SelectActionName",
		"(Reference)ViewActionName", "(Reference)ViewrefActionName", "(Reference)ViewName",
		"(Reference)InsertType", "(Reference)SortItem", "(Reference)SortType", "(Reference)NestProperty*",
		"(Select)ColNum", "(Select)EditorValue*",
		"(String)EditorValue*",
		};

	private static final String[] HEADER_BUTTON = {"", "ViewNo", "ViewName",
		"ButtonNo", "ButtonTitle", "DisplayLabel", "DispFlag", "OnclickEvent"};

	public EntityViewCsvWriter(OutputStream out, String encode) throws IOException{
		super(out, encode);

	}

	public void writeView(EntityView view, int index, String defName) throws IOException {
		csvWriter.writeHeader(index + " " + defName + rs("tools.entityexplorer.EntityViewCsvWriter.fromViewInfo"));

		if (view == null) {
			csvWriter.writeHeader("", rs("tools.entityexplorer.EntityViewCsvWriter.viewDefNotDef"));
			csvWriter.writeHeader("");
			csvWriter.writeHeader("");
			csvWriter.writeHeader("");
			return;
		}

		csvWriter.writeHeader(index + rs("tools.entityexplorer.EntityViewCsvWriter.detailFromViewInfo"));
		outputDetailFormView(view, index + ".1");
		csvWriter.writeHeader("");

		csvWriter.writeHeader(index + rs("tools.entityexplorer.EntityViewCsvWriter.searchFromViewInfo"));
		outputSearchFormView(view, index + ".2");
		csvWriter.writeHeader("");
		csvWriter.writeHeader("");
	}

	private void outputDetailFormView(EntityView view, String prefix) throws SuperCsvException, IOException {

		List<Map<String, Object>> detailFormList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> sectionList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> elementList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> propertytList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> editorList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> buttonList = new ArrayList<Map<String,Object>>();

		int viewIndex = 0;
		String[] viewNames = view.getDetailFormViewNames();
		for (String name : viewNames) {
			DetailFormView detailForm = view.getDetailFormView(name);
			if (detailForm == null) {
				continue;
			}

			viewIndex++;

			//DetailForm定義作成
			Map<String, Object> detailFormMap = createDetailFormMap(prefix, viewIndex, detailForm);
			detailFormList.add(detailFormMap);

			List<Section> sections = detailForm.getSections();
			if (!sections.isEmpty()) {
				int sectionIndex = 0;
				for (Section section : sections) {
					sectionIndex++;
					//Section定義作成
					sectionList.addAll(createSectionMap(detailFormMap, String.valueOf(sectionIndex), section, elementList, propertytList, editorList, buttonList));
				}
			} else {
				//空のSection定義作成
				sectionList.addAll(createSectionMap(detailFormMap, String.valueOf(0), null, elementList, propertytList, editorList, buttonList));
			}

			List<Button> buttons = detailForm.getButtons();
			if (!buttons.isEmpty()) {
				int buttonIndex = 0;
				for (Button button : buttons) {
					buttonIndex++;
					//Button定義作成
					buttonList.add(createButtonMap(detailFormMap, String.valueOf(buttonIndex), button));
				}
			}
		}

		//出力
		writeMap(prefix, "Detail", HEADER_DETAIL_VIEW, detailFormList, sectionList, elementList, propertytList, editorList, buttonList);

	}

	private void outputSearchFormView(EntityView view, String prefix) throws SuperCsvException, IOException {

		List<Map<String, Object>> searchFormList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> sectionList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> elementList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> propertytList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> editorList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> buttonList = new ArrayList<Map<String,Object>>();

		int viewIndex = 0;
		String[] viewNames = view.getSearchFormViewNames();
		for (String name : viewNames) {
			SearchFormView searchForm = view.getSearchFormView(name);
			if (searchForm == null) {
				continue;
			}

			viewIndex++;

			//SearchForm定義作成
			Map<String, Object> searchFormMap = createSearchFormMap(prefix, viewIndex, searchForm);
			searchFormList.add(searchFormMap);

			List<Section> sections = searchForm.getSections();
			if (!sections.isEmpty()) {
				int sectionIndex = 0;
				for (Section section : sections) {
					sectionIndex++;
					//Section定義作成
					sectionList.addAll(createSectionMap(searchFormMap, String.valueOf(sectionIndex), section, elementList, propertytList, editorList, buttonList));
				}
			} else {
				//空のSection定義作成
				sectionList.addAll(createSectionMap(searchFormMap, String.valueOf(0), null, elementList, propertytList, editorList, buttonList));
			}
		}

		//出力
		writeMap(prefix, "Search", HEADER_SEARCH_VIEW, searchFormList, sectionList, elementList, propertytList, editorList, buttonList);

	}

	private Map<String, Object> createDetailFormMap(String prefix, int viewIndex, DetailFormView detailForm) {
		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_DETAIL_VIEW.length);
		recordMap.put("ViewNo", prefix + "." + viewIndex);
		recordMap.put("ViewName", (detailForm.getName() != null && !detailForm.getName().isEmpty() ? detailForm.getName() : "default"));
		recordMap.put("ViewTitle", detailForm.getTitle());

		recordMap.put("EditActionName", detailForm.getEditActionName());
		recordMap.put("CancelActionName", detailForm.getCancelActionName());
		recordMap.put("InsertActionName", detailForm.getInsertActionName());
		recordMap.put("UpdateActionName", detailForm.getUpdateActionName());
		recordMap.put("DeleteActionName", detailForm.getDeleteActionName());
		recordMap.put("IsNoneDispCopyButton", detailForm.isNoneDispCopyButton());
		return recordMap;
	}


	private Map<String, Object> createSearchFormMap(String prefix, int viewIndex, SearchFormView searchForm) {
		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_SEARCH_VIEW.length);
		recordMap.put("ViewNo", prefix + "." + viewIndex);
		recordMap.put("ViewName", (searchForm.getName() != null && !searchForm.getName().isEmpty() ? searchForm.getName() : "default"));
		recordMap.put("ViewTitle", searchForm.getTitle());

		recordMap.put("NewActionName", searchForm.getNewActionName());
		recordMap.put("EditActionName", searchForm.getEditActionName());
		recordMap.put("ViewActionName", searchForm.getViewActionName());
		recordMap.put("DownloadActionName", searchForm.getDownloadActionName());
		return recordMap;
	}

	private List<Map<String, Object>> createSectionMap(Map<String, Object> parentMap, String sectionIndex, Section section,
			List<Map<String, Object>> elementList, List<Map<String, Object>> propertytList, List<Map<String, Object>> editorList,
			List<Map<String, Object>> buttonList) {

		List<Map<String, Object>> sectionList = new ArrayList<Map<String,Object>>();

		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_SECTION.length);
		recordMap.put("ViewNo", parentMap.get("ViewNo"));
		recordMap.put("ViewName", parentMap.get("ViewName"));

		if (section == null) {
			sectionList.add(recordMap);
			return sectionList;
		}

		recordMap.put("SectionNo", sectionIndex);
		recordMap.put("SectionClass", section.getClass().getSimpleName());
		if (section instanceof DefaultSection) {
			DefaultSection anySection = (DefaultSection)section;
			recordMap.put("(Default)ColNum", anySection.getColNum());
			recordMap.put("(Default)SectionTitle", anySection.getTitle());
			recordMap.put("(Default)Id", anySection.getId());
			recordMap.put("(Default)Style", anySection.getStyle());

			//Element定義の作成
			elementList.addAll(createSectionElementMap(recordMap, anySection.getElements(), sectionList, propertytList, editorList, buttonList));

		} else if (section instanceof ScriptingSection) {
			ScriptingSection anySection = (ScriptingSection)section;
			recordMap.put("(Scripting)Script", anySection.getScript());
			recordMap.put("(Scripting)Key", anySection.getKey());
		} else if (section instanceof TemplateSection) {
			TemplateSection anySection = (TemplateSection)section;
			recordMap.put("(Template)TemplateName", anySection.getTemplateName());
		} else if (section instanceof SearchConditionSection) {
			SearchConditionSection anySection = (SearchConditionSection)section;
			recordMap.put("(SearchCondition)ConditionDispCount", anySection.getConditionDispCount());
			recordMap.put("(SearchCondition)HideDetailCondition", anySection.isHideDetailCondition());

			//Element定義の作成
			elementList.addAll(createSectionElementMap(recordMap, anySection.getElements(), sectionList, propertytList, editorList, buttonList));

		} else if (section instanceof SearchResultSection) {
			SearchResultSection anySection = (SearchResultSection)section;
			recordMap.put("(SearchResult)DispRowCount", anySection.getDispRowCount());

			//Element定義の作成
			elementList.addAll(createSectionElementMap(recordMap, anySection.getElements(), sectionList, propertytList, editorList, buttonList));

		}

		sectionList.add(recordMap);
		return sectionList;
	}

	private List<Map<String, Object>> createSectionElementMap(Map<String, Object> parentMap, List<Element> elements,
			List<Map<String, Object>> sectiontList, List<Map<String, Object>> propertytList, List<Map<String, Object>> editorList,
			List<Map<String, Object>> buttonList) {

		List<Map<String, Object>> elementList = new ArrayList<Map<String,Object>>();

		if (!elements.isEmpty()) {
			int elementIndex = 0;
			int propertyIndex = 0;

			for (Element element : elements) {
				elementIndex++;
				propertyIndex++;

				Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_SECTION_ELEMENT.length);
				recordMap.put("ViewNo", parentMap.get("ViewNo"));
				recordMap.put("ViewName", parentMap.get("ViewName"));
				recordMap.put("SectionNo", parentMap.get("SectionNo"));
				recordMap.put("SectionClass", parentMap.get("SectionClass"));

				recordMap.put("ElementNo", elementIndex);
				recordMap.put("ElementClass", element.getClass().getSimpleName());

				elementList.add(recordMap);

				if (element instanceof Section) {
					//Section出力（再帰）
					sectiontList.addAll(createSectionMap(parentMap, parentMap.get("SectionNo") + " - " + elementIndex,
							(Section)element, elementList, propertytList, editorList, buttonList));
				} else if (element instanceof PropertyItem) {
					//Property定義作成
					Map<String, Object> propertyMap = createPropertyItemMap(recordMap, propertyIndex, (PropertyItem)element, editorList);
					propertytList.add(propertyMap);
				} else if (element instanceof PropertyColumn) {
					//PropertyColumn定義作成
					Map<String, Object> propertyMap = createPropertyColumnMap(recordMap, propertyIndex, (PropertyColumn)element, editorList);
					propertytList.add(propertyMap);
				} else if (element instanceof Button) {
					//Button定義作成
					buttonList.add(createButtonMap(parentMap, parentMap.get("SectionNo") + " - " + elementIndex, (Button)element));
				} else if (element instanceof TemplateElement) {
					TemplateElement anyElement = (TemplateElement)element;
					recordMap.put("ElementTitle", anyElement.getTitle());
					recordMap.put("(Template)TemplateName", anyElement.getTemplateName());
				} else if (element instanceof ScriptingElement) {
					ScriptingElement anyElement = (ScriptingElement)element;
					recordMap.put("ElementTitle", anyElement.getTitle());
					recordMap.put("(Scripting)Script", anyElement.getScript());
					recordMap.put("(Scripting)Key", anyElement.getKey());
				} else if (element instanceof Link) {
					Link anyElement = (Link)element;
					recordMap.put("ElementTitle", anyElement.getTitle());
					recordMap.put("(Link)DisplayLabel", anyElement.getDisplayLabel());
					recordMap.put("(Link)Url", anyElement.getUrl());
					recordMap.put("(Link)DispNewWindow", anyElement.isDispNewWindow());
				} else if (element instanceof VirtualPropertyItem) {
					VirtualPropertyItem anyElement = (VirtualPropertyItem)element;
					recordMap.put("(VirtualProperty)PropertyName", anyElement.getPropertyName());
					recordMap.put("(VirtualProperty)DisplayLabel", anyElement.getDisplayLabel());
					recordMap.put("(VirtualProperty)Style", anyElement.getStyle());

					recordMap.put("(VirtualProperty)Description", anyElement.getDescription());
					recordMap.put("(VirtualProperty)Tooltip", anyElement.getTooltip());
					recordMap.put("(VirtualProperty)HideDetail", anyElement.isHideDetail());
					recordMap.put("(VirtualProperty)HideView", anyElement.isHideView());

					if (anyElement.getEditor() != null) {
						editorList.addAll(createPropertyEditorMap(recordMap, anyElement.getEditor()));
					}


				} else if (element instanceof BlankSpace) {
					//BlankSpace anyElement = (BlankSpace)element;
				}
				//SectionElementで追加される可能性があるため先にAddしている
				//elementList.add(recordMap);
			}
		} else {
			Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_SECTION_ELEMENT.length);
			recordMap.put("ViewNo", parentMap.get("ViewNo"));
			recordMap.put("ViewName", parentMap.get("ViewName"));
			recordMap.put("SectionNo", parentMap.get("SectionNo"));
			recordMap.put("SectionClass", parentMap.get("SectionClass"));

			elementList.add(recordMap);
		}

		return elementList;
	}

	private Map<String, Object> createPropertyItemMap(Map<String, Object> parentMap, int propertyIndex, PropertyItem property,
			List<Map<String, Object>> editorList) {

		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_PROPERTY.length);
		recordMap.put("ViewNo", parentMap.get("ViewNo"));
		recordMap.put("ViewName", parentMap.get("ViewName"));
		recordMap.put("SectionNo", parentMap.get("SectionNo"));
		recordMap.put("SectionClass", parentMap.get("SectionClass"));

		recordMap.put("ElementNo", parentMap.get("ElementNo"));

		recordMap.put("PropertyNo", propertyIndex);

		recordMap.put("PropertyClass", property.getClass().getSimpleName());
		recordMap.put("PropertyName", property.getPropertyName());
		recordMap.put("DisplayLabel", property.getDisplayLabel());
		recordMap.put("Style", property.getStyle());

		recordMap.put("Description", property.getDescription());
		recordMap.put("Tooltip", property.getTooltip());
		recordMap.put("HideNormalCondition", property.isHideNormalCondition());
		recordMap.put("HideDetailCondition", property.isHideDetailCondition());

		if (property.getEditor() != null) {
			editorList.addAll(createPropertyEditorMap(recordMap, property.getEditor()));
		}

		return recordMap;
	}

	private Map<String, Object> createPropertyColumnMap(Map<String, Object> parentMap, int propertyIndex, PropertyColumn property,
			List<Map<String, Object>> editorList) {

		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_PROPERTY.length);
		recordMap.put("ViewNo", parentMap.get("ViewNo"));
		recordMap.put("ViewName", parentMap.get("ViewName"));
		recordMap.put("SectionNo", parentMap.get("SectionNo"));
		recordMap.put("SectionClass", parentMap.get("SectionClass"));

		recordMap.put("ElementNo", parentMap.get("ElementNo"));

		recordMap.put("PropertyNo", propertyIndex);

		recordMap.put("PropertyClass", property.getClass().getSimpleName());
		recordMap.put("PropertyName", property.getPropertyName());
		recordMap.put("DisplayLabel", property.getDisplayLabel());
		recordMap.put("Style", property.getStyle());

		if (property.getEditor() != null) {
			editorList.addAll(createPropertyEditorMap(recordMap, property.getEditor()));
		}

		return recordMap;
	}

	private List<Map<String, Object>> createPropertyEditorMap(Map<String, Object> parentMap, PropertyEditor editor) {

		List<Map<String, Object>> editorList = new ArrayList<Map<String,Object>>();

		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_PROPERTY_EDITOR.length);
		recordMap.put("ViewNo", parentMap.get("ViewNo"));
		recordMap.put("ViewName", parentMap.get("ViewName"));
		recordMap.put("SectionNo", parentMap.get("SectionNo"));
		recordMap.put("SectionClass", parentMap.get("SectionClass"));

		recordMap.put("ElementNo", parentMap.get("ElementNo"));

		recordMap.put("PropertyNo", parentMap.get("PropertyNo"));


		recordMap.put("EditorClass", editor.getClass().getSimpleName());
		recordMap.put("PropertyName", editor.getPropertyName());
		recordMap.put("DisplayType", (editor.getDisplayType() != null ? editor.getDisplayType().name() : ""));
		if (editor instanceof BinaryPropertyEditor) {
			BinaryPropertyEditor anyEditor = (BinaryPropertyEditor)editor;
			recordMap.put("(Binary)DownloadActionName", anyEditor.getDownloadActionName());
			recordMap.put("(Binary)UploadActionName", anyEditor.getUploadActionName());
			recordMap.put("(Binary)Height", anyEditor.getHeight());
			recordMap.put("(Binary)Width", anyEditor.getWidth());
		} else if (editor instanceof BooleanPropertyEditor) {
			BooleanPropertyEditor anyEditor = (BooleanPropertyEditor)editor;
			recordMap.put("(Boolean)FalseLabel", anyEditor.getFalseLabel());
			recordMap.put("(Boolean)TrueLabel", anyEditor.getTrueLabel());
		} else if (editor instanceof TemplatePropertyEditor) {
			TemplatePropertyEditor anyEditor = (TemplatePropertyEditor)editor;
			recordMap.put("(Template)TemplateName", anyEditor.getTemplateName());
		} else if (editor instanceof NumberPropertyEditor) {
			NumberPropertyEditor anyEditor = (NumberPropertyEditor)editor;
			recordMap.put("(Number)NumberFormat", anyEditor.getNumberFormat());
		} else if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor anyEditor = (ReferencePropertyEditor)editor;

			recordMap.put("(Reference)ObjectName", anyEditor.getObjectName());
			recordMap.put("(Reference)UrlParameter", anyEditor.getUrlParameter());
			recordMap.put("(Reference)AddActionName", anyEditor.getAddActionName());
			recordMap.put("(Reference)SelectActionName", anyEditor.getSelectActionName());
			recordMap.put("(Reference)ViewrefActionName", anyEditor.getViewrefActionName());
			recordMap.put("(Reference)ViewName", anyEditor.getViewName());
			recordMap.put("(Reference)InsertType", (anyEditor.getInsertType() != null ? anyEditor.getInsertType().name() : ""));
			recordMap.put("(Reference)SortItem", anyEditor.getSortItem());
			recordMap.put("(Reference)SortType", (anyEditor.getSortType() != null ? anyEditor.getSortType().name() : ""));
			if (anyEditor.getNestProperties() != null && !anyEditor.getNestProperties().isEmpty()) {
				recordMap.put("(Reference)NestProperty*", rs("tools.entityexplorer.EntityViewCsvWriter.approp"));
			}

		} else if (editor instanceof SelectPropertyEditor) {
			SelectPropertyEditor anyEditor = (SelectPropertyEditor)editor;
			recordMap.put("(Select)EditorValue*", getEditValueString(anyEditor.getValues()));
		} else if (editor instanceof StringPropertyEditor) {
			StringPropertyEditor anyEditor = (StringPropertyEditor)editor;
			recordMap.put("(String)EditorValue*", getEditValueString(anyEditor.getValues()));
		}

		editorList.add(recordMap);

		if (editor instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor anyEditor = (ReferencePropertyEditor)editor;
			if (anyEditor.getNestProperties() != null && !anyEditor.getNestProperties().isEmpty()) {
				List<NestProperty> nests = anyEditor.getNestProperties();
				String parentPropertyNo = String.valueOf(recordMap.get("PropertyNo"));
				int nestIndex = 0;
				for (NestProperty nest : nests) {
					nestIndex++;
					if (nest.getEditor() != null) {
						Map<String, Object> nestedRecordMap = new HashMap<String, Object>(HEADER_PROPERTY_EDITOR.length);
						nestedRecordMap.putAll(parentMap);
						nestedRecordMap.put("PropertyNo", parentPropertyNo + "_" + nestIndex);

						editorList.addAll(createPropertyEditorMap(nestedRecordMap, nest.getEditor()));
					}
				}
			}
		}

		return editorList;
	}

	private Map<String, Object> createButtonMap(Map<String, Object> parentMap, String buttonIndex, Button button) {

		Map<String, Object> recordMap = new HashMap<String, Object>(HEADER_BUTTON.length);
		recordMap.put("ViewNo", parentMap.get("ViewNo"));
		recordMap.put("ViewName", parentMap.get("ViewName"));

		if (button == null) {
			return recordMap;
		}

		recordMap.put("ButtonNo", buttonIndex);
		recordMap.put("ButtonTitle", button.getTitle());
		recordMap.put("DisplayLabel", button.getDisplayLabel());
		recordMap.put("DispFlag", button.isDispFlag());
		recordMap.put("OnclickEvent", button.getOnclickEvent());

		return recordMap;
	}

	private String getEditValueString(List<EditorValue> values) {
		if (values == null || values.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (EditorValue value : values) {
			builder.append(value.getLabel() + "(" + value.getValue() + "),");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}


	private void writeMap(String prefix, String viewType, String[] viewTypeHeader,
			List<Map<String, Object>> searchFormList, List<Map<String, Object>> sectionList,
			List<Map<String, Object>> elementList, List<Map<String, Object>> propertytList,
			List<Map<String, Object>> editorList, List<Map<String, Object>> buttonList) throws SuperCsvException, IOException {

		csvWriter.writeHeader(prefix + ".1 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewList"));
		csvWriter.writeHeader(viewTypeHeader);
		for (Map<String, Object> recordMap : searchFormList) {
			csvWriter.write(recordMap, viewTypeHeader, createCellProcessor(viewTypeHeader.length));
		}
		csvWriter.writeHeader("");

		csvWriter.writeHeader(prefix + ".2 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewSectionList"));
		csvWriter.writeHeader(HEADER_SECTION);
		for (Map<String, Object> recordMap : sectionList) {
			csvWriter.write(recordMap, HEADER_SECTION, createCellProcessor(HEADER_SECTION.length));
		}
		csvWriter.writeHeader("");

		csvWriter.writeHeader(prefix + ".3 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewElementList"));
		if (elementList.isEmpty()) {
			csvWriter.writeHeader("", rs("tools.entityexplorer.EntityViewCsvWriter.notApprop"));
		} else {
			csvWriter.writeHeader(HEADER_SECTION_ELEMENT);
			for (Map<String, Object> recordMap : elementList) {
				csvWriter.write(recordMap, HEADER_SECTION_ELEMENT, createCellProcessor(HEADER_SECTION_ELEMENT.length));
			}
		}
		csvWriter.writeHeader("");

		csvWriter.writeHeader(prefix + ".4 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewPropertyList"));
		if (propertytList.isEmpty()) {
			csvWriter.writeHeader("", rs("tools.entityexplorer.EntityViewCsvWriter.notApprop"));
		} else {
			csvWriter.writeHeader(HEADER_PROPERTY);
			for (Map<String, Object> recordMap : propertytList) {
				csvWriter.write(recordMap, HEADER_PROPERTY, createCellProcessor(HEADER_PROPERTY.length));
			}
		}
		csvWriter.writeHeader("");

		csvWriter.writeHeader(prefix + ".5 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewPropEditorList"));

		if (editorList.isEmpty()) {
			csvWriter.writeHeader("", rs("tools.entityexplorer.EntityViewCsvWriter.notApprop"));
		} else {
			csvWriter.writeHeader(HEADER_PROPERTY_EDITOR);
			for (Map<String, Object> recordMap : editorList) {
				csvWriter.write(recordMap, HEADER_PROPERTY_EDITOR, createCellProcessor(HEADER_PROPERTY_EDITOR.length));
			}
		}
		csvWriter.writeHeader("");

		csvWriter.writeHeader(prefix + ".6 " + viewType + rs("tools.entityexplorer.EntityViewCsvWriter.formViewButtonList"));

		if (buttonList.isEmpty()) {
			csvWriter.writeHeader("", rs("tools.entityexplorer.EntityViewCsvWriter.notApprop"));
		} else {
			csvWriter.writeHeader(HEADER_BUTTON);
			for (Map<String, Object> recordMap : buttonList) {
				csvWriter.write(recordMap, HEADER_BUTTON, createCellProcessor(HEADER_BUTTON.length));
			}
		}
		csvWriter.writeHeader("");
	}

	private CellProcessor[] createCellProcessor(int length) {

		//Nullの場合落ちるため基本はConvertNullToを指定
		CellProcessor[] processors = new CellProcessor[length];
		for (int i = 0; i < length; i++) {
			processors[i] = new ConvertNullTo("");
		}
		return processors;
	}

	private static String rs(String key, Object... arguments) {
		return AdminResourceBundleUtil.resourceString(key, arguments);
	}

}
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

package org.iplass.mtp.entity.definition.properties;

import java.sql.Time;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;


/**
 * 時間を表すプロパティ定義。
 * 
 * @author K.Higuchi
 *
 */
public class TimeProperty extends PropertyDefinition {
	private static final long serialVersionUID = -833269361107220721L;

	public TimeProperty() {
	}

	public TimeProperty(String name) {
		setName(name);
	}
	
	@Override
	public Class<?> getJavaType() {
		return Time.class;
	}

	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.TIME;
	}
	
//	public TimeProperty copy() {
//		TimeProperty copy = new TimeProperty();
//		copyTo(copy);
//		return copy;
//	}

}
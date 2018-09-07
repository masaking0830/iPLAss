/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.async;

/**
 * 非同期タスクの実行ステータス。
 *
 * @author K.Higuchi
 *
 */
public enum TaskStatus {
	/**
	 * 登録済。まだタスクは開始されていない。
	 */
	SUBMITTED,
	/**
	 * タスク実行中。
	 */
	EXECUTING,
	/**
	 * タスクは中断。
	 */
	ABORTED,
	/**
	 * タスク実行完了し、結果が取得されるのを待っている。
	 */
	RETURNED,
	/**
	 * タスク実行完了（結果取得も完了）。
	 */
	COMPLETED,
	/**
	 * 不明。
	 */
	UNKNOWN
}
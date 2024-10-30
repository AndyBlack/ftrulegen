/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.ResourceBundle;

/**
 * 
 */
public enum PermutationsValue {
	no, not_head, with_head;

	public int getValue() {
		return this.ordinal();
	}

	public static PermutationsValue forValue(int value) {
		return values()[value];
	}

	public static String getString(PermutationsValue value, ResourceBundle bundle) {
		String result = "??";
		switch (value) {
		case no:
			result = bundle.getString("permutations.no");
			break;
		case not_head:
			result = bundle.getString("permutations.nothead");
			break;
		case with_head:
			result = bundle.getString("permutations.withhead");
			break;
		default:
			break;
		}
		return result;
	}
}

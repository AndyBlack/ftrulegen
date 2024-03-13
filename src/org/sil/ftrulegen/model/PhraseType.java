/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

public enum PhraseType {
	source, target;

	public int getValue() {
		return this.ordinal();
	}

	public static PhraseType forValue(int value) {
		return values()[value];
	}
}
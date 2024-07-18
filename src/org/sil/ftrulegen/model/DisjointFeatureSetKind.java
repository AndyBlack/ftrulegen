/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

public enum DisjointFeatureSetKind {
	source, target;

	public int getValue() {
		return this.ordinal();
	}

	public static DisjointFeatureSetKind forValue(int value) {
		return values()[value];
	}
}
package com.amazon.sample.iap.entitlement;

import java.util.HashSet;
import java.util.Set;

public enum MySKU {

    LEVEL2("com.amazon.sample.iap.entitlement.level2");

	private String sku;

	private MySKU(String sku) {
		this.sku = sku;
	}

	public static MySKU valueForSKU(String sku) {
		if (LEVEL2.getSku().equals(sku)) {
			return LEVEL2;
		}
		return null;
	}

	public String getSku() {
		return sku;
	}

	private static Set<String> SKUS = new HashSet<String>();
	static {
		SKUS.add(LEVEL2.getSku());
	}

	public static Set<String> getAll() {
		return SKUS;
	}

}

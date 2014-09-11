package com.amazon.sample.iap.subscription;

import java.util.HashSet;
import java.util.Set;

public enum MySKU {
	
	MY_MAGAZINE_MONTHLY(
			"com.amazon.sample.iap.subscription.mymagazine.month",
			"com.amazon.sample.iap.subscription.mymagazine");
	 
	private String sku;
	private String parentSku;

	private MySKU(String sku, String parentSku) {
		this.sku = sku;
		this.parentSku = parentSku;
	}

	public static MySKU valueForParentSKU(String parentSku) {
		if (MY_MAGAZINE_MONTHLY.getParentSku().equals(parentSku)) {
			return MY_MAGAZINE_MONTHLY;
		}
		return null;
	}

	public String getSku() {
		return sku;
	}

	public String getParentSku() {
		return parentSku;
	}

	private static Set<String> SKUS = new HashSet<String>();
	static {
		SKUS.add(MY_MAGAZINE_MONTHLY.getSku());
	}

	public static Set<String> getAll() {
		return SKUS;
	}

}

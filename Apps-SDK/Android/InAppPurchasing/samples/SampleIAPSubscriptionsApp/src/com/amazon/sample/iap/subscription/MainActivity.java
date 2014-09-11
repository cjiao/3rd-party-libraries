package com.amazon.sample.iap.subscription;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.GetUserIdResponse.GetUserIdRequestStatus;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse.ItemDataRequestStatus;
import com.amazon.inapp.purchasing.PurchaseResponse.PurchaseRequestStatus;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.amazon.sample.iap.subscription.AppPurchasingObserver.PurchaseData;
import com.amazon.sample.iap.subscription.AppPurchasingObserver.PurchaseDataStorage;

/**
 * Sample code for IAP subscriptions
 * 
 * This is the main activity for this project that shows how to call the
 * PurchasingManager methods and how to get notified through the
 * {@link AppPurchasingObserverListener} callbacks.
 */
public class MainActivity extends Activity implements
		AppPurchasingObserverListener {

	// Wrapper around SharedPreferences to save request state
	// and purchase receipt data
	private PurchaseDataStorage purchaseDataStorage;

	/**
	 * Setup IAP SDK and other UI related objects specific to this sample
	 * application.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupApplicationSpecificOnCreate();

		setupIAPOnCreate();
	}

	/**
	 * Setup for IAP SDK called from onCreate. Sets up
	 * {@link PurchaseDataStorage} for storing purchase receipt data,
	 * {@link AppPurchasingObserver} for listening to IAP API callbacks and sets
	 * up this activity as a {@link AppPurchasingObserverListener} to listen for
	 * callbacks from the {@link AppPurchasingObserver}.
	 */
	private void setupIAPOnCreate() {
		purchaseDataStorage = new PurchaseDataStorage(this);

		AppPurchasingObserver purchasingObserver = new AppPurchasingObserver(
				this, purchaseDataStorage);
		purchasingObserver.setListener(this);

		Log.i(TAG, "onCreate: registering AppPurchasingObserver");
		PurchasingManager.registerObserver(purchasingObserver);
	}

	/**
	 * Calls {@link PurchasingManager#initiateGetUserIdRequest()} to get current
	 * userId and {@link PurchasingManager#initiateItemDataRequest(Set)} with
	 * the list of SKUs to verify the SKUs are valid in the Appstore.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		Log.i(TAG, "onResume: call initiateGetUserIdRequest");
		PurchasingManager.initiateGetUserIdRequest();

		Log.i(TAG,
				"onResume: call initiateItemDataRequest for skus: "
						+ MySKU.getAll());
		PurchasingManager.initiateItemDataRequest(MySKU.getAll());
	}

	/**
	 * Click handler called when user clicks button to buy access to monthly magazine
	 * subscription. This method calls
	 * {@link PurchasingManager#initiatePurchaseRequest(String)} with the SKU
	 * for the My Magazine Monthly subscription.
	 */
	public void onBuyMagazineMonthlyClick(View view) {
		String requestId = PurchasingManager
				.initiatePurchaseRequest(MySKU.MY_MAGAZINE_MONTHLY.getSku());
		PurchaseData purchaseData = purchaseDataStorage
				.newPurchaseData(requestId);
		Log.i(TAG, "onBuyMagazineMonthlyClick: requestId (" + requestId
				+ ") requestState (" + purchaseData.getRequestState() + ")");
	}

	/**
	 * Callback for a successful get user id response
	 * {@link GetUserIdResponseStatus#SUCCESSFUL}.
	 * 
	 * In this sample app, if the user changed from the previously stored user,
	 * this method just disables the magazine subscription and relies
   * onPurchaseUpdates from Offset.BEGINNING to sync the display 
   * with the latest subscriptions.
	 * 
	 * @param userId
	 *            returned from {@link GetUserIdResponse#getUserId()}.
	 * @param userChanged
	 *            - whether user changed from previously stored user.
	 */
	@Override
	public void onGetUserIdResponseSuccessful(String userId, boolean userChanged) {
		Log.i(TAG, "onGetUserIdResponseSuccess: update display if userId ("
				+ userId + ") user changed from previous stored user ("
				+ userChanged + ")");

		if (!userChanged)
			return;

		// Reset to original setting where magazine monthly is disabled
		disableMagazineSubscriptionInView();
	}

	/**
	 * Callback for a failed get user id response
	 * {@link GetUserIdRequestStatus#FAILED}
	 * 
	 * @param requestId
	 *            returned from {@link GetUserIdResponsee#getRequestId()} that
	 *            can be used to correlate with original request sent with
	 *            {@link PurchasingManager#initiateGetUserIdRequest()}.
	 */
	@Override
	public void onGetUserIdResponseFailed(String requestId) {
		Log.i(TAG, "onGetUserIdResponseFailed for requestId (" + requestId
				+ ")");
	}

	/**
	 * Callback for successful item data response with unavailable SKUs
	 * {@link ItemDataRequestStatus#SUCCESSFUL_WITH_UNAVAILABLE_SKUS}. This
	 * means that these unavailable SKUs are NOT accessible in developer portal.
	 * 
	 * In this sample app, we disable the buy button for these SKUs.
	 * 
	 * @param unavailableSkus
	 *            - skus that are not valid in developer portal
	 */
	@Override
	public void onItemDataResponseSuccessfulWithUnavailableSkus(
			Set<String> unavailableSkus) {
		Log.i(TAG, "onItemDataResponseSuccessfulWithUnavailableSkus: for ("
				+ unavailableSkus.size() + ") unavailableSkus");
		disableButtonsForUnavailableSkus(unavailableSkus);
	}

	/**
	 * Callback for successful item data response
	 * {@link ItemDataRequestStatus#SUCCESSFUL} with item data
	 * 
	 * @param itemData
	 *            - map of valid SKU->Items
	 */
	@Override
	public void onItemDataResponseSuccessful(Map<String, Item> itemData) {
		for (Entry<String, Item> entry : itemData.entrySet()) {
			String sku = entry.getKey();
			Item item = entry.getValue();
			Log.i(TAG, "onItemDataResponseSuccessful: sku (" + sku + ") item ("
					+ item + ")");
			if (MySKU.MY_MAGAZINE_MONTHLY.getSku().equals(sku)) {
				enableBuyMagazineMonthlyButton();
			}
		}
	}

	/**
	 * Callback for failed item data response
	 * {@link ItemDataRequestStatus#FAILED}.
	 * 
	 * @param requestId
	 */
	public void onItemDataResponseFailed(String requestId) {
		Log.i(TAG, "onItemDataResponseFailed: for requestId (" + requestId
				+ ")");
	}

	/**
	 * Callback on successful purchase response
	 * {@link PurchaseRequestStatus#SUCCESSFUL}. In this sample app, we show
	 * Magazine Subscription as enabled
	 * 
	 * @param sku
	 */
	@Override
	public void onPurchaseResponseSuccess(String userId, String sku,
			String purchaseToken) {
		Log.i(TAG, "onPurchaseResponseSuccess: for userId (" + userId
				+ ") sku (" + sku + ") purchaseToken (" + purchaseToken + ")");
		enableSubscriptionForSKU(sku);
	}

	/**
	 * Callback when user is already entitled
	 * {@link PurchaseRequestStatus#ALREADY_ENTITLED} to sku passed into
	 * initiatePurchaseRequest.
	 * 
	 * @param userId
	 * @param sku
	 */
	@Override
	public void onPurchaseResponseAlreadyEntitled(String userId, String sku) {
		Log.i(TAG, "onPurchaseResponseAlreadyEntitled: for userId (" + userId
				+ ") sku (" + sku + ")");
		// For subscriptions, even if already subscribed, make sure to enable.
		enableSubscriptionForSKU(sku);
	}

	/**
	 * Callback when sku passed into
	 * {@link PurchasingManager#initiatePurchaseRequest} is not valid
	 * {@link PurchaseRequestStatus#INVALID_SKU}.
	 * 
	 * @param userId
	 * @param sku
	 */
	@Override
	public void onPurchaseResponseInvalidSKU(String userId, String sku) {
		Log.i(TAG, "onPurchaseResponseInvalidSKU: for userId (" + userId + ") sku ("+sku+")");
	}

	/**
	 * Callback on failed purchase response {@link PurchaseRequestStatus#FAILED}
	 * .
	 * 
	 * @param requestId
	 * @param sku
	 */
	@Override
	public void onPurchaseResponseFailed(String requestId, String sku) {
		Log.i(TAG, "onPurchaseResponseFailed: for requestId (" + requestId
				+ ") sku ("+sku+")");
	}

	/**
	 * Callback on successful purchase updates response
	 * {@link PurchaseUpdatesRequestStatus#SUCCESSFUL} for each receipt.
	 * 
	 * In this sample app, we show magazine subscription as enabled.
	 * 
	 * @param userId
	 * @param sku
	 * @param purchaseToken
	 * @param isSubscriptionActive
	 */
	@Override
	public void onPurchaseUpdatesResponseSuccess(String userId, String sku,
			String purchaseToken, boolean isSubscriptionActive) {
		Log.i(TAG, "onPurchaseUpdatesResponseSuccess: for userId (" + userId
				+ ") sku (" + sku + ") purchaseToken (" + purchaseToken + ") isSubscriptionActive ("+isSubscriptionActive+")");
		if (isSubscriptionActive) {
			enableSubscriptionForSKU(sku);
		} else {
			disableSubscriptionForSKU(sku);
		}
	}

	/**
	 * Callback on successful purchase updates response
	 * {@link PurchaseUpdatesRequestStatus#SUCCESSFUL} for revoked SKU.
	 * 
	 * @param userId
	 * @param revokedSKU
	 */
	@Override
	public void onPurchaseUpdatesResponseSuccessRevokedSku(String userId,
			String revokedSku) {
		// Not called for subscriptions
		Log.i(TAG, "onPurchaseUpdatesResponseSuccessRevokedSku: for userId ("
				+ userId + ") revokedSku (" + revokedSku + ")");
	}

	/**
	 * Callback on failed purchase updates response
	 * {@link PurchaseUpdatesRequestStatus#FAILED}
	 * 
	 * @param requestId
	 */
	public void onPurchaseUpdatesResponseFailed(String requestId) {
		Log.i(TAG, "onPurchaseUpdatesResponseFailed: for requestId ("
				+ requestId + ")");
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////// Application specific code below
	// ////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////

	private static final String TAG = "SampleIAPSubscriptionsApp";

	private Handler guiThreadHandler;

	// Button to buy monthly subscription to magazine
	private Button buyMonthlyMagazine;

	// TextView shows whether user is subscribed to magazine
	private TextView isMonthlySubscriptionEnabled;

	/**
	 * Setup application specific things, called from onCreate()
	 */
	private void setupApplicationSpecificOnCreate() {
		setContentView(R.layout.activity_main);

		buyMonthlyMagazine = (Button) findViewById(R.id.buy_magazine_monthly_button);

		resetApplication();

		guiThreadHandler = new Handler();
	}

	private void resetApplication() {
		// Show "Subscription Disabled" text in gray color to indicate
		// user is not subscribed to this magazine initially
		isMonthlySubscriptionEnabled = (TextView) findViewById(R.id.is_monthly_magazine_enabled);
		isMonthlySubscriptionEnabled.setText(R.string.subscription_disabled);
		isMonthlySubscriptionEnabled.setTextColor(Color.GRAY);
		isMonthlySubscriptionEnabled.setBackgroundColor(Color.WHITE);
	}

	/**
	 * Disable buy button for any unavailable SKUs. In this sample app, this
	 * would just disable "Buy Monthly Magazine Subscription" button
	 * 
	 * @param unavailableSkus
	 */
	private void disableButtonsForUnavailableSkus(Set<String> unavailableSkus) {
		for (String unavailableSku : unavailableSkus) {
			if (!MySKU.getAll().contains(unavailableSku))
				continue;

			if (MySKU.MY_MAGAZINE_MONTHLY.getSku().equals(unavailableSku)) {
				Log.i(TAG, "disableButtonsForUnavailableSkus: unavailableSKU ("
						+ unavailableSku
						+ "), disabling buyMagazineMonthlyButton");
				disableBuyMagazineMonthlyButton();
			}
		}
	}

	/**
	 * Disable "Buy Monthly Magazine Subscription" button
	 */
	private void disableBuyMagazineMonthlyButton() {
		buyMonthlyMagazine.setEnabled(false);
	}
	
	/**
	 * Enable "Buy Monthly Magazine Subscription" button
	 */
	private void enableBuyMagazineMonthlyButton() {
		buyMonthlyMagazine.setEnabled(true);
	}
	/**
	 * Enable if SKU is for My Monthly Magazine
	 * 
	 * @param sku
	 */
	private void enableSubscriptionForSKU(String sku) {
		if (!MySKU.MY_MAGAZINE_MONTHLY.getSku().equals(sku))
			return;
		enableMagazineSubscriptionInView();
	}

	/**
	 * Show Subscription as enabled in view
	 */
	private void enableMagazineSubscriptionInView() {
		Log.i(TAG,
				"enableMagazineSubscriptionInView: enabling magazine subscription, show by setting text color to blue and highlighting");
		guiThreadHandler.post(new Runnable() {
			public void run() {
				isMonthlySubscriptionEnabled.setText(R.string.subscription_enabled);
				isMonthlySubscriptionEnabled.setTextColor(Color.BLUE);
				isMonthlySubscriptionEnabled.setBackgroundColor(Color.YELLOW);
			}
		});
	}

	/**
	 * Disable if SKU is for My Monthly Magazine
	 * 
	 * @param sku
	 */
	private void disableSubscriptionForSKU(String sku) {
		if (!MySKU.MY_MAGAZINE_MONTHLY.getSku().equals(sku))
			return;
		disableMagazineSubscriptionInView();
	}

	/**
	 * Show Subscription as disabled in view
	 */
	protected void disableMagazineSubscriptionInView() {
		guiThreadHandler.post(new Runnable() {
			public void run() {
				isMonthlySubscriptionEnabled
						.setText(R.string.subscription_disabled);
				isMonthlySubscriptionEnabled.setTextColor(Color.GRAY);
				isMonthlySubscriptionEnabled.setBackgroundColor(Color.WHITE);
			}
		});
	}
}

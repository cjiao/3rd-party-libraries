Here's the 10 minute, 10 step guide that you can follow to integrate the IAP API into your application.

1. First include the IAP jar file as a library into your Build Path and make sure to check the checkbox to export the library.  
   For more details or screenshots, look at the "Setting Up Your Environment" section.

2. Copy the ResponseReceiver into your AndroidManifest.xml file, within the <application> section.
   This will allow your app to receive broadcast intents via the ResponseReceiver class from the Amazon Client.
   For more details, see the "Understanding In-App Purchasing API" section.

    <receiver android:name="com.amazon.inapp.purchasing.ResponseReceiver">
        <intent-filter>
            <action android:name="com.amazon.inapp.purchasing.NOTIFY"
                    android:permission="com.amazon.inapp.purchasing.Permission.NOTIFY" />
        </intent-filter>
    </receiver>
        
3. Copy the following classes/interfaces to your app.
    * AppPurchasingObserver
    * AppPurchasingObserverListener
    * MySKU

4. In the AppPurchasingObserver class, change the static final String TAG to the tag you want to see the logs under.  For example:

    private static final String TAG = "MyApp";

5. In your Activity class, add the following lines of code to onCreate and the member variable for PurchaseDataStorage into your Activity class.
   The code below will create a new PurchaseDataStorage instance to store purchase receipt related data, 
   create a new AppPurchasingObserver and set this Activity as a Listener.
   
    private PurchaseDataStorage purchaseDataStorage;

    @Override
  	protected void onCreate(Bundle savedInstanceState) {
  	
        // Code in your application ....
  
    	purchaseDataStorage = new PurchaseDataStorage(this);
    
    	AppPurchasingObserver purchasingObserver = new AppPurchasingObserver(this, purchaseDataStorage);
    	
    	purchasingObserver.setListener(this);
    
    	PurchasingManager.registerObserver(purchasingObserver);	
    }
   
6. Have your Activity class implement the AppPurchasingObserverListener interface.  
   Your activity can implement each of these interface methods in response to each type of callback from AppPurchasingObserver.  
   If you run your app now, you should see a log statement that you received a callback onSdkAvailable - something similar to what's shown below.  
   This means that the IAP SDK has been initialized successfully and that you are currently in sandbox (testing) mode.

    I/MyApp(10388): onSdkAvailable: isSandboxMode: true

7. Now copy the onResume() method from the sample app's MainActivity class over into your app's Activity class.  
   Add a String TAG variable to make the logging statements compile.  
   Try running your app again.  
   You should now see a bunch of log statements that:
    * initiateGetUserIdRequest was called and the AppPurchasingObserver received a callback onGetUserIdResponse.
    * initiateItemDataRequest was called and the AppPurchasingObserver received a callback onItemDataResponse with the SUCCESSFUL_WITH_UNAVAILABLE itemDataRequestStatus.  Note that the SKU is unavailable right now because you haven't setup the JSON file on your device to be served up by SDK Tester.
    * initiatePurchaseUpdatesRequest was called and received a callback onPurchaseUpdatesResponse.

	@Override
	protected void onResume() {
		super.onResume();

		Log.i(TAG, "onResume: call initiateGetUserIdRequest");
		PurchasingManager.initiateGetUserIdRequest();

		Log.i(TAG, "onResume: call initiateItemDataRequest for skus: " + MySKU.getAll());
		PurchasingManager.initiateItemDataRequest(MySKU.getAll());
	}

8. Now let's setup SDK Tester.  Install the SDK Tester APK provided in the Amazon Mobile App SDK package.

    adb install AmazonSDKTester.apk

9. Copy the amazon.sdktester.json file from this sample app onto your device under the path: /mnt/sdcard/amazon.sdktester.json.  

10. Copy the onBuyMagazineMonthlyClick(View view) method from this sample app's MainActivity class into your app's activity class.  
    Now, add a button to call this method.  If it's easier, you can just copy the buy_magazine_monthly_button button from this sample app's res/layout/activity_main.xml file into your app.  
    Add the necessary string to strings.xml.  Let's try running the app again and click on the button to buy an IAP item.  
    You should see the product detail screen where you can purchase the item.  Once you close the confirmation screen, you should see in your logs that the callback onPurchaseResponse was called.  
    You would fulfill the item in the MainActivity#onPurchaseResponseSuccess method.
    Note that for subscriptions, you would need to call initiatePurchaseUpdatesRequest and implement the onPurchaseUpdatesResponse callback to keep track of subscription end dates.

	public void onBuyMagazineMonthlyClick(View view) {
		String requestId = PurchasingManager.initiatePurchaseRequest(MySKU.MY_MAGAZINE_MONTHLY.getSku());
		
		PurchaseData purchaseData = purchaseDataStorage.newPurchaseData(requestId);
		
		Log.i(TAG, "onBuyMagazineMonthlyClick: requestId (" + requestId + ") requestState (" + purchaseData.getRequestState() + ")");
	}

And that's it.  In 10 steps you've created your own app that integrates with the basic parts of the IAP API.
You can take a look at the sample code and comments in more detail and read the documentation on https://developer.amazon.com/sdk.html,

but go ahead and take that coffee break now.  Enjoy!

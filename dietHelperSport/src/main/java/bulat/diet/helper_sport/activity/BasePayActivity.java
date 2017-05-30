package bulat.diet.helper_sport.activity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.TextView;
import bulat.diet.helper_sport.R;
import bulat.diet.helper_sport.item.Inventory;
import bulat.diet.helper_sport.item.Purchase;
import bulat.diet.helper_sport.item.SkuDetails;
import bulat.diet.helper_sport.utils.IabHelper;
import bulat.diet.helper_sport.utils.IabResult;
import bulat.diet.helper_sport.utils.MessagesUpdater;
import bulat.diet.helper_sport.utils.SaveUtils;
import android.support.v4.app.FragmentActivity;

public class BasePayActivity extends Activity {
	// The helper object
    public static TreeMap<String, String> prices = new TreeMap<String, String>();
	protected IabHelper mHelper;
	String part3 = "hn44nVQt0v0Nf6+yZiVrO3QnTi6b4ByUrqyBgIK9VyMnOA4aTlW75v4pULHaT4yiBjRhBq0"
			+ "K5g5cBMzAFL33fyQAd3Up92mR97EJcLmXIX8odEfmSCER9YwNy8FhuVusxg3tkpvRFMEgaRDITzjDA8k3XoumEt3Kwzx1Vi0hWP7bT4znBgwIDAQAB";
	String part1 = "Dsj2IA6T6sheoCci9Ra22cXv2K4L1UmTVKqqa9+OsiGYtXvNOsbgPaSV2PTGpYjSovxOdiyUujcaokcQHrjXmGALbDUaQ+YqJmQUlmtmResMaG8NbB";
	static final String SKU_PREMIUM = "adds_free";
	static final int RC_REQUEST = 10001;
	private static final String TAG = "BasePayActivity";

	String part2 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxjr4/MnyDoKUStykRcz9zewwha8AmLq3G4QQwmCdPetz4Ty7g";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, part2 + part1 + part3);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(false);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");

				String[] moreSkus = {PaymentsListActivity.SKU_YEAR_VIP, PaymentsListActivity.SKU_YEAR_2017, PaymentsListActivity.SKU_HALFYEAR_2017, PaymentsListActivity.SKU_MUUNTH_2017};
				//mHelper.queryInventoryAsync(mGotInventoryListener);
				mHelper.queryInventoryAsync(true, Arrays.asList(moreSkus),mGotInventoryListener);
			}
		});

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		if(!((Activity) this).isFinishing())
		{
		    //show dialog
			alert(message);
		}
	}

	public void alert(String message) {	
		try {
			AlertDialog.Builder bld = new AlertDialog.Builder(this);
			bld.setMessage(message);
			bld.setNeutralButton("OK", null);
			Log.d(TAG, "Showing alert dialog: " + message);
			bld.create().show();
		}catch (BadTokenException e) {
			e.printStackTrace();
		}
	}

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		if (p != null) {
			return String.valueOf(SaveUtils.getUserAdvId(this)).equals(
					p.getDeveloperPayload());
		} else {
			return false;
		}
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				// complain("Error purchasing: " + result);
				setWaitScreen(false);
				return;
			}
			// if (!verifyDeveloperPayload(purchase)) {
			// complain("Error purchasing. Authenticity verification failed.");
			// setWaitScreen(false);
			// return;
			// }

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought 1/4 tank of gas. So consume it.
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			} else if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				// / alert("Thank you for upgrading to premium!");
				// mIsPremium = true;
				// updateUi();
				setWaitScreen(false);
			}
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			// We know this is the "gas" sku because it's the only one we
			// consume,
			// so we don't check which sku was consumed. If you have more than
			// one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in
				// our
				// game world's logic, which in our case means filling the gas
				// tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
				// mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
				saveData();
			} else {
				// complain("Error while consuming: " + result);
			}
			// updateUi();
			setWaitScreen(false);
			Log.d(TAG, "End consumption flow.");
		}
	};

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */
			Date currDate = new Date();

			updateUserInfo(inventory);

            updatePrices(inventory);


            if (currDate.getTime() > SaveUtils
					.getEndPDate(BasePayActivity.this)) {
				// Do we have the year plan?
				Purchase yearPlanPurchase = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR);
				if (yearPlanPurchase != null
						&& verifyDeveloperPayload(yearPlanPurchase)) {
					SaveUtils.setEndPDate(yearPlanPurchase.getPurchaseTime()
							+ 367 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase munthPlanPurchase = inventory
						.getPurchase(PaymentsListActivity.SKU_MUNTH);
				if (munthPlanPurchase != null
						&& verifyDeveloperPayload(munthPlanPurchase)) {
					SaveUtils.setEndPDate(munthPlanPurchase.getPurchaseTime()
							+ 32 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase munthPlanPurchaseOld = inventory
						.getPurchase(PaymentsListActivity.SKU_MUNTH_OLD);
				if (munthPlanPurchaseOld != null
						&& verifyDeveloperPayload(munthPlanPurchaseOld)) {
					SaveUtils.setEndPDate(
							munthPlanPurchaseOld.getPurchaseTime() + 32
									* DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase yearPlanPurchaseOld = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_OLD);
				if (yearPlanPurchaseOld != null
						&& verifyDeveloperPayload(yearPlanPurchaseOld)) {
					SaveUtils.setEndPDate(yearPlanPurchaseOld.getPurchaseTime()
							+ 367 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}
				
				// Do we have the manth plan?
				Purchase yearPlanPurchaseNew = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_NEW);
				if (yearPlanPurchaseNew != null
						&& verifyDeveloperPayload(yearPlanPurchaseNew)) {
					SaveUtils.setEndPDate(yearPlanPurchaseNew.getPurchaseTime()
							+ 367 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase yearPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_2017);
				if (yearPlanPurchase2017 != null
						&& verifyDeveloperPayload(yearPlanPurchase2017)) {
					SaveUtils.setEndPDate(yearPlanPurchase2017.getPurchaseTime()
									+ 367 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}
				
				// Do we have the manth plan?
				Purchase yearPlanPurchaseVIP = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_VIP);
				if (yearPlanPurchaseVIP != null
						&& verifyDeveloperPayload(yearPlanPurchaseVIP)) {
					SaveUtils.setEndPDate(yearPlanPurchaseVIP.getPurchaseTime()
							+ 367 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}
				
				// Do we have the manth plan?
				Purchase halfYearPlanPurchase = inventory
						.getPurchase(PaymentsListActivity.SKU_HALFYEAR);
				if (halfYearPlanPurchase != null
						&& verifyDeveloperPayload(halfYearPlanPurchase)) {
					SaveUtils.setEndPDate(halfYearPlanPurchase.getPurchaseTime()
							+ 190 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase halfYearPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_HALFYEAR_2017);
				if (halfYearPlanPurchase2017 != null
						&& verifyDeveloperPayload(halfYearPlanPurchase2017)) {
					SaveUtils.setEndPDate(halfYearPlanPurchase2017.getPurchaseTime()
									+ 190 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}
				
				// Do we have the manth plan?
				Purchase munthPlanPurchasenew = inventory
						.getPurchase(PaymentsListActivity.SKU_MUUNTH_NEW);
				if (munthPlanPurchasenew != null
						&& verifyDeveloperPayload(munthPlanPurchasenew)) {
					SaveUtils.setEndPDate(munthPlanPurchasenew.getPurchaseTime()
							+ 32 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				Purchase munthPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_MUUNTH_2017);
				if (munthPlanPurchase2017 != null
						&& verifyDeveloperPayload(munthPlanPurchase2017)) {
					SaveUtils.setEndPDate(munthPlanPurchase2017.getPurchaseTime()
									+ 32 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

			}
		}
	};

    private void updatePrices(Inventory inventory) {
        SkuDetails VIPPlanDetails= inventory
                .getSkuDetails(PaymentsListActivity.SKU_YEAR_VIP);
        if (VIPPlanDetails != null) {
            prices.put(PaymentsListActivity.SKU_YEAR_VIP, VIPPlanDetails.getPrice());
        }

        SkuDetails yearPlanDetails= inventory
                .getSkuDetails(PaymentsListActivity.SKU_YEAR_2017);
        if (yearPlanDetails != null) {
            prices.put(PaymentsListActivity.SKU_YEAR_2017, yearPlanDetails.getPrice());
        }

        SkuDetails halfyearPlanDetails= inventory
                .getSkuDetails(PaymentsListActivity.SKU_HALFYEAR_2017);
        if (halfyearPlanDetails != null) {
            prices.put(PaymentsListActivity.SKU_HALFYEAR_2017, halfyearPlanDetails.getPrice());
        }

        SkuDetails monthPlanDetails= inventory
                .getSkuDetails(PaymentsListActivity.SKU_MUUNTH_2017);
        if (monthPlanDetails != null) {
            prices.put(PaymentsListActivity.SKU_MUUNTH_2017, monthPlanDetails.getPrice());
        }

    }


    IabHelper.QueryInventoryFinishedListener mGetPricesListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
											 Inventory inventory) {
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */
			Date currDate = new Date();



				// Do we have the year plan?
			SkuDetails yearPlanDetails= inventory
						.getSkuDetails(PaymentsListActivity.SKU_YEAR);
				if (yearPlanDetails != null) {


				}

				// Do we have the manth plan?
				Purchase munthPlanPurchase = inventory
						.getPurchase(PaymentsListActivity.SKU_MUNTH);
				if (munthPlanPurchase != null
						&& verifyDeveloperPayload(munthPlanPurchase)) {

				}

				// Do we have the manth plan?
				Purchase munthPlanPurchaseOld = inventory
						.getPurchase(PaymentsListActivity.SKU_MUNTH_OLD);
				if (munthPlanPurchaseOld != null
						&& verifyDeveloperPayload(munthPlanPurchaseOld)) {


				}

				// Do we have the manth plan?
				Purchase yearPlanPurchaseOld = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_OLD);
				if (yearPlanPurchaseOld != null
						&& verifyDeveloperPayload(yearPlanPurchaseOld)) {


				}

				// Do we have the manth plan?
				Purchase yearPlanPurchaseNew = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_NEW);
				if (yearPlanPurchaseNew != null
						&& verifyDeveloperPayload(yearPlanPurchaseNew)) {


				}

				// Do we have the manth plan?
				Purchase yearPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_2017);
				if (yearPlanPurchase2017 != null
						&& verifyDeveloperPayload(yearPlanPurchase2017)) {


				}

				// Do we have the manth plan?
				Purchase yearPlanPurchaseVIP = inventory
						.getPurchase(PaymentsListActivity.SKU_YEAR_VIP);
				if (yearPlanPurchaseVIP != null
						&& verifyDeveloperPayload(yearPlanPurchaseVIP)) {


				}

				// Do we have the manth plan?
				Purchase halfYearPlanPurchase = inventory
						.getPurchase(PaymentsListActivity.SKU_HALFYEAR);
				if (halfYearPlanPurchase != null
						&& verifyDeveloperPayload(halfYearPlanPurchase)) {


				}

				// Do we have the manth plan?
				Purchase halfYearPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_HALFYEAR_2017);
				if (halfYearPlanPurchase2017 != null
						&& verifyDeveloperPayload(halfYearPlanPurchase2017)) {
					SaveUtils.setEndPDate(halfYearPlanPurchase2017.getPurchaseTime()
									+ 190 * DateUtils.DAY_IN_MILLIS,
							BasePayActivity.this);

				}

				// Do we have the manth plan?
				Purchase munthPlanPurchasenew = inventory
						.getPurchase(PaymentsListActivity.SKU_MUUNTH_NEW);
				if (munthPlanPurchasenew != null
						&& verifyDeveloperPayload(munthPlanPurchasenew)) {

				}

				Purchase munthPlanPurchase2017 = inventory
						.getPurchase(PaymentsListActivity.SKU_MUUNTH_2017);
				if (munthPlanPurchase2017 != null
						&& verifyDeveloperPayload(munthPlanPurchase2017)) {

				}

			}
	};

	private void updateUserInfo(Inventory inventory) {
		// Do we have the year plan?
		try {
			Purchase yearPlanPurchase = inventory
					.getPurchase(PaymentsListActivity.SKU_YEAR);
			if (yearPlanPurchase != null && verifyDeveloperPayload(yearPlanPurchase)) {
				if (new Date().getTime() < yearPlanPurchase.getPurchaseTime() + 367 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(yearPlanPurchase.getSku()).getPrice() + "__" + yearPlanPurchase.getSku() + "__" + yearPlanPurchase.getPurchaseTime(), this);
				}
			}

			// Do we have the manth plan?
			Purchase munthPlanPurchase = inventory
					.getPurchase(PaymentsListActivity.SKU_MUNTH);
			if (munthPlanPurchase != null
					&& verifyDeveloperPayload(munthPlanPurchase)) {
				if (new Date().getTime() < munthPlanPurchase.getPurchaseTime() + 32 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(munthPlanPurchase.getSku()).getPrice() + "__" + munthPlanPurchase.getSku() + "__" + munthPlanPurchase.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase munthPlanPurchaseOld = inventory
					.getPurchase(PaymentsListActivity.SKU_MUNTH_OLD);
			if (munthPlanPurchaseOld != null
					&& verifyDeveloperPayload(munthPlanPurchaseOld)) {
				if (new Date().getTime() < munthPlanPurchaseOld.getPurchaseTime() + 32 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(munthPlanPurchaseOld.getSku()).getPrice() + "__" + munthPlanPurchaseOld.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase yearPlanPurchaseOld = inventory
					.getPurchase(PaymentsListActivity.SKU_YEAR_OLD);
			if (yearPlanPurchaseOld != null
					&& verifyDeveloperPayload(yearPlanPurchaseOld)) {

				if (new Date().getTime() < yearPlanPurchaseOld.getPurchaseTime()
						+ 367 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(yearPlanPurchaseOld.getSku()).getPrice() + "__" + yearPlanPurchaseOld.getSku() + "__" + yearPlanPurchaseOld.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase yearPlanPurchaseNew = inventory
					.getPurchase(PaymentsListActivity.SKU_YEAR_NEW);
			if (yearPlanPurchaseNew != null
					&& verifyDeveloperPayload(yearPlanPurchaseNew)) {
				if (new Date().getTime() < yearPlanPurchaseNew.getPurchaseTime()
						+ 367 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(yearPlanPurchaseNew.getSku()).getPrice() + "__" + yearPlanPurchaseNew.getSku() + "__" + yearPlanPurchaseNew.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase yearPlanPurchase2017 = inventory
					.getPurchase(PaymentsListActivity.SKU_YEAR_2017);
			if (yearPlanPurchase2017 != null
					&& verifyDeveloperPayload(yearPlanPurchase2017)) {
				if (new Date().getTime() < yearPlanPurchase2017.getPurchaseTime()
						+ 367 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(yearPlanPurchase2017.getSku()).getPrice() + "__" + yearPlanPurchase2017.getSku() + "__" + yearPlanPurchase2017.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase yearPlanPurchaseVIP = inventory
					.getPurchase(PaymentsListActivity.SKU_YEAR_VIP);
			if (yearPlanPurchaseVIP != null
					&& verifyDeveloperPayload(yearPlanPurchaseVIP)) {
				if (new Date().getTime() < yearPlanPurchaseVIP.getPurchaseTime()
						+ 367 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(yearPlanPurchaseVIP.getSku()).getPrice() + "__" + yearPlanPurchaseVIP.getSku() + "__" + yearPlanPurchaseVIP.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase halfYearPlanPurchase = inventory
					.getPurchase(PaymentsListActivity.SKU_HALFYEAR);
			if (halfYearPlanPurchase != null
					&& verifyDeveloperPayload(halfYearPlanPurchase)) {
				if (new Date().getTime() < halfYearPlanPurchase.getPurchaseTime()
						+ 190 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(halfYearPlanPurchase.getSku()).getPrice() + "__" + halfYearPlanPurchase.getSku() + "__" + halfYearPlanPurchase.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase halfYearPlanPurchase2017 = inventory
					.getPurchase(PaymentsListActivity.SKU_HALFYEAR_2017);
			if (halfYearPlanPurchase2017 != null
					&& verifyDeveloperPayload(halfYearPlanPurchase2017)) {
				if (new Date().getTime() < halfYearPlanPurchase2017.getPurchaseTime()
						+ 190 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(halfYearPlanPurchase2017.getSku()).getPrice() + "__" + halfYearPlanPurchase2017.getSku() + "__" + halfYearPlanPurchase2017.getPurchaseTime(), this);
				}

			}

			// Do we have the manth plan?
			Purchase munthPlanPurchasenew = inventory
					.getPurchase(PaymentsListActivity.SKU_MUUNTH_NEW);
			if (munthPlanPurchasenew != null
					&& verifyDeveloperPayload(munthPlanPurchasenew)) {
				if (new Date().getTime() < munthPlanPurchasenew.getPurchaseTime()
						+ 32 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(munthPlanPurchasenew.getSku()).getPrice() + "__" + munthPlanPurchasenew.getSku() + "__" + munthPlanPurchasenew.getPurchaseTime(), this);
				}

			}

			Purchase munthPlanPurchase2017 = inventory
					.getPurchase(PaymentsListActivity.SKU_MUUNTH_2017);
			if (munthPlanPurchase2017 != null
					&& verifyDeveloperPayload(munthPlanPurchase2017)) {
				if (new Date().getTime() < munthPlanPurchase2017.getPurchaseTime()
						+ 32 * DateUtils.DAY_IN_MILLIS) {
					SaveUtils.setSKU(inventory.getSkuDetails(munthPlanPurchase2017.getSku()).getPrice() + "__" + munthPlanPurchase2017.getSku() + "__" + munthPlanPurchase2017.getPurchaseTime(), this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SaveUtils.setSKU(e.getMessage(), this);
		}
	}

	public void setWaitScreen(boolean set) {
		// show progress bar
	}

	private void saveData() {
		// TODO Auto-generated method stub
		// SaveUtils.setEndPDate(date, context);
	}

}

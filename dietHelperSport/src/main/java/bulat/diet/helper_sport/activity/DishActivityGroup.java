package bulat.diet.helper_sport.activity;

import java.util.ArrayList;
import java.util.Stack;

import com.dm.zbar.android.scanner.ZBarConstants;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;
import bulat.diet.helper_sport.utils.Constants;
import bulat.diet.helper_sport.utils.Consts;
import bulat.diet.helper_sport.utils.Consts.PurchaseState;
import bulat.diet.helper_sport.utils.Consts.ResponseCode;


public class DishActivityGroup extends ActivityGroup{

	private Stack<String> stack;
	private boolean flag=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		
		if (stack == null) {
			stack = new Stack<String>();
		}
		//String lastLoadDate = SaveUtils.loadLastLoadDate(this);
		//if (lastLoadDate.length() == 0){

		//mHandler = new Handler();
       // mDungeonsPurchaseObserver = new StartPurchaseObserver(mHandler);
       //mBillingService = new BillingService();
       // mBillingService.setContext(this.getParent());
       // mBillingService.requestPurchase("android.test.purchased", "inapp", "bulat");
       //mPurchaseDatabase = new PurchaseDatabase(this);
       // setupWidgets();

        // Check if billing is supported.
    //    ResponseHandler.register(mDungeonsPurchaseObserver);
     //   if (!mBillingService.checkBillingSupported()) {
            //showDialog(DIALOG_CANNOT_CONNECT_ID);
     //   }
        
     //   if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_SUBSCRIPTION)) {
            //showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
    //    }   	
		//} else {
			push("DishActivity", new Intent(this, DishActivity.class));
	//	}
			
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//ResponseHandler.register(mDungeonsPurchaseObserver);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
        super.onActivityResult(requestCode, resultCode, data);               
        switch (requestCode) {
        	
        case 2: {	        		 				        
            if (resultCode == RESULT_OK && null != data) {
            	String code = data.getStringExtra(ZBarConstants.SCAN_RESULT); 	               
                if(code!= null && code.length()>3){
                	Constants.ST = code;
                }
            } else if(resultCode == RESULT_CANCELED && data != null) {
                String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                if(!TextUtils.isEmpty(error)) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
            }
            break;}
	        case 1: {	        		 				        
	            if (resultCode == RESULT_OK && null != data) {
	            	String code = data.getStringExtra(ZBarConstants.SCAN_RESULT);
	            	
	                ArrayList<String> text = data
	                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);	 
	                Constants.ST = text.get(0);	  	               
	                if(code!= null && code.length()>3){
	                	Constants.ST = code;
	                }
	            } else if(resultCode == RESULT_CANCELED && data != null) {
	                String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
	                if(!TextUtils.isEmpty(error)) {
	                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	                }
	            }
	            break;
	        }    
	        	 
        }
        flag=false;
       // push("DishListActivity", intent);

    }
	@Override
	protected void onResume() {
		//boolean firstLaunch = SaveUtils.loadFirstLaunchFlag(this);
       // if (firstLaunch){
		if(flag && stack.size() == 0){
			getFirst();
		}else{
			flag=true;
		}
       // }
		super.onResume();
	}
	
	@Override
	  public void finishFromChild(Activity child) {
	    pop();
	  }

	  @Override
	  public void onBackPressed() {
	    pop();
	  }


	  public void push(String id, Intent intent) {
	    Window window = getLocalActivityManager().startActivity(id, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	    if (window != null) {
	      stack.push(id);
	      setContentView(window.getDecorView());
	    }
	  }

	  public void pop() {
		  try{
	    if (stack.size() == 1) {
	    	finish();
	    }else{
		    LocalActivityManager manager = getLocalActivityManager();
		    manager.destroyActivity(stack.pop(), true);
		    if (stack.size() > 0) {
		      Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
		      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
		      setContentView(newWindow.getDecorView());
		    }
	    }
		  }catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  public void getFirst(){
		  if(stack.size() > 0){
			  LocalActivityManager manager = getLocalActivityManager();
			  while (stack.size() != 1){
				  manager.destroyActivity(stack.pop(), true);
			  }
			  Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
		      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
		      setContentView(newWindow.getDecorView());
		  }
	  }
	
	  
	  @Override
	    protected void onStop() {
	        super.onStop();
	        //ResponseHandler.unregister(mDungeonsPurchaseObserver);
	}
		
}

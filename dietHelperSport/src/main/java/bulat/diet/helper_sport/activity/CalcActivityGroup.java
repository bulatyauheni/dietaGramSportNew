package bulat.diet.helper_sport.activity;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class CalcActivityGroup extends ActivityGroup{
	
	private Stack<String> stack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
		if (stack == null) {
			stack = new Stack<String>();
		}
		push("CalcActivity", new Intent(this, CalcActivity.class));			
	}
	
	@Override
		protected void onStart() {
		super.onStart();
			// TODO Auto-generated method stub			
	}
	
	@Override
	protected void onResume() {
        	getFirst();  	
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
	    if (stack.size() == 1) finish();
	    LocalActivityManager manager = getLocalActivityManager();
	    manager.destroyActivity(stack.pop(), true);
	    if (stack.size() > 0) {
	      Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
	      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
	      setContentView(newWindow.getDecorView());
	    }
	  }
	  
	  public void pop(int num) {
		    if (stack.size() == 1) finish();
		    LocalActivityManager manager = getLocalActivityManager();
		    while(stack.size()>num) {
		    	 manager.destroyActivity(stack.pop(), true);
			}
		   
		    if (stack.size() > 0) {
		      Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
		      Window newWindow = manager.startActivity(stack.peek(), lastIntent);
		      setContentView(newWindow.getDecorView());
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
			//stack = null;
			super.onStop();
		}
	  
}

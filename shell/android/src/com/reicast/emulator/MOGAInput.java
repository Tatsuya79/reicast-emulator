package com.reicast.emulator;


/******************************************************************************/

import tv.ouya.console.api.OuyaController;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bda.controller.Controller;
import com.bda.controller.ControllerListener;
import com.bda.controller.KeyEvent;
import com.bda.controller.MotionEvent;
import com.bda.controller.StateEvent;

/******************************************************************************/

/*

*/
public class MOGAInput
{
	private SharedPreferences prefs;

	static final int DELAY = 1000 / 50; // 50 Hz
	
	static final int ACTION_CONNECTED = Controller.ACTION_CONNECTED;
	static final int ACTION_DISCONNECTED = Controller.ACTION_DISCONNECTED;
	static final int ACTION_VERSION_MOGA = Controller.ACTION_VERSION_MOGA;
	static final int ACTION_VERSION_MOGAPRO = Controller.ACTION_VERSION_MOGAPRO;

	Controller mController = null;
	private Handler handler;
	private String notify;
	private GL2JNIView mView;

    public boolean isActive[] = { false, false, false, false };
    public boolean isMogaPro[] = { false, false, false, false };

	private static final int key_CONT_B 			= 0x0002;
	private static final int key_CONT_A 			= 0x0004;
	private static final int key_CONT_START 		= 0x0008;
	private static final int key_CONT_DPAD_UP 		= 0x0010;
	private static final int key_CONT_DPAD_DOWN 	= 0x0020;
	private static final int key_CONT_DPAD_LEFT 	= 0x0040;
	private static final int key_CONT_DPAD_RIGHT 	= 0x0080;
	private static final int key_CONT_Y 			= 0x0200;
	private static final int key_CONT_X 			= 0x0400;

	int[] keys = new int[] {
		KeyEvent.KEYCODE_BUTTON_B, key_CONT_B,
		KeyEvent.KEYCODE_BUTTON_A, key_CONT_A,
		KeyEvent.KEYCODE_BUTTON_X, key_CONT_X,
		KeyEvent.KEYCODE_BUTTON_Y, key_CONT_Y,

		KeyEvent.KEYCODE_DPAD_UP, key_CONT_DPAD_UP,
		KeyEvent.KEYCODE_DPAD_DOWN, key_CONT_DPAD_DOWN,
		KeyEvent.KEYCODE_DPAD_LEFT, key_CONT_DPAD_LEFT,
		KeyEvent.KEYCODE_DPAD_RIGHT, key_CONT_DPAD_RIGHT,

		KeyEvent.KEYCODE_BUTTON_START, key_CONT_START,
	};
	
	int map[][] = { keys, keys, keys, keys };

	Activity act;
	public MOGAInput()
	{
		/*
		mStates.put(StateEvent.STATE_CONNECTION, new ExampleInteger("STATE_CONNECTION......"));
		mStates.put(StateEvent.STATE_POWER_LOW, new ExampleInteger("STATE_POWER_LOW......"));
		mStates.put(StateEvent.STATE_CURRENT_PRODUCT_VERSION, new ExampleInteger("STATE_CURRENT_PRODUCT_VERSION"));
		mStates.put(StateEvent.STATE_SUPPORTED_PRODUCT_VERSION, new ExampleInteger("STATE_SUPPORTED_PRODUCT_VERSION"));

		mKeys.put(KeyEvent.KEYCODE_DPAD_UP, new ExampleInteger("KEYCODE_DPAD_UP......"));
		mKeys.put(KeyEvent.KEYCODE_DPAD_DOWN, new ExampleInteger("KEYCODE_DPAD_DOWN......"));
		mKeys.put(KeyEvent.KEYCODE_DPAD_LEFT, new ExampleInteger("KEYCODE_DPAD_LEFT......"));
		mKeys.put(KeyEvent.KEYCODE_DPAD_RIGHT, new ExampleInteger("KEYCODE_DPAD_RIGHT......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_A, new ExampleInteger("KEYCODE_BUTTON_A......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_B, new ExampleInteger("KEYCODE_BUTTON_B......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_X, new ExampleInteger("KEYCODE_BUTTON_X......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_Y, new ExampleInteger("KEYCODE_BUTTON_Y......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_L1, new ExampleInteger("KEYCODE_BUTTON_L1......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_R1, new ExampleInteger("KEYCODE_BUTTON_R1......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_L2, new ExampleInteger("KEYCODE_BUTTON_L2......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_R2, new ExampleInteger("KEYCODE_BUTTON_R2......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_THUMBL, new ExampleInteger("KEYCODE_BUTTON_THUMBL......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_THUMBR, new ExampleInteger("KEYCODE_BUTTON_THUMBR......"));		
		mKeys.put(KeyEvent.KEYCODE_BUTTON_START, new ExampleInteger("KEYCODE_BUTTON_START......"));
		mKeys.put(KeyEvent.KEYCODE_BUTTON_SELECT, new ExampleInteger("KEYCODE_BUTTON_SELECT......"));

		mMotions.put(MotionEvent.AXIS_X, new ExampleFloat("AXIS_X........."));
		mMotions.put(MotionEvent.AXIS_Y, new ExampleFloat("AXIS_Y........."));
		mMotions.put(MotionEvent.AXIS_Z, new ExampleFloat("AXIS_Z........."));
		mMotions.put(MotionEvent.AXIS_RZ, new ExampleFloat("AXIS_RZ......."));
		mMotions.put(MotionEvent.AXIS_LTRIGGER, new ExampleFloat("AXIS_LTRIGGER........."));
		mMotions.put(MotionEvent.AXIS_RTRIGGER, new ExampleFloat("AXIS_RTRIGGER........."));
		*/
	}
	
	public void setGL2View(GL2JNIView mView) {
		this.mView = mView;
	}

	protected void onCreate(Activity act)
	{
		this.act = act;
		
		handler = new Handler();

		mController = Controller.getInstance(act);
		mController.init();
		mController.setListener(new ExampleControllerListener(), new Handler());
	}

	protected void onDestroy()
	{
		mController.exit();
	}

	protected void onPause()
	{
		mController.onPause();
	}

	protected void onResume()
	{
		mController.onResume();

		/*
		for(final Entry<Integer, ExampleInteger> entry : mStates.entrySet())
		{
			final int key = entry.getKey();
			final ExampleInteger value = entry.getValue();
			value.mValue = mController.getState(key);
		}
		
		for(final Entry<Integer, ExampleInteger> entry : mKeys.entrySet())
		{
			final int key = entry.getKey();
			final ExampleInteger value = entry.getValue();
			value.mValue = mController.getKeyCode(key);
		}

		for(final Entry<Integer, ExampleFloat> entry : mMotions.entrySet())
		{
			final int key = entry.getKey();
			final ExampleFloat value = entry.getValue();
			value.mValue = mController.getAxisValue(key);
		}
		*/
	}
	
	private void setModifiedKeys(int player) {
		prefs = PreferenceManager
				.getDefaultSharedPreferences(act.getApplicationContext());
		String[] players = act.getResources().getStringArray(R.array.controllers);
		String id = players[player].substring(
				players[player].lastIndexOf(" "), players[player].length());
		if (prefs.getBoolean("modified_key_layout"  + id, false)) {
			map[player] = new int[] {
				prefs.getInt("a_button" + id, KeyEvent.KEYCODE_BUTTON_A), key_CONT_A,
				prefs.getInt("b_button" + id, KeyEvent.KEYCODE_BUTTON_B), key_CONT_B,
				prefs.getInt("x_button" + id, KeyEvent.KEYCODE_BUTTON_X), key_CONT_X,
				prefs.getInt("y_button" + id, KeyEvent.KEYCODE_BUTTON_Y), key_CONT_Y,

				prefs.getInt("dpad_up" + id, KeyEvent.KEYCODE_DPAD_UP), key_CONT_DPAD_UP,
				prefs.getInt("dpad_down" + id, KeyEvent.KEYCODE_DPAD_DOWN), key_CONT_DPAD_DOWN,
				prefs.getInt("dpad_left" + id, KeyEvent.KEYCODE_DPAD_LEFT), key_CONT_DPAD_LEFT,
				prefs.getInt("dpad_right" + id, KeyEvent.KEYCODE_DPAD_RIGHT), key_CONT_DPAD_RIGHT,

				prefs.getInt("start_button" + id, KeyEvent.KEYCODE_BUTTON_START), key_CONT_START,
			};
		}
	}

	class ExampleControllerListener implements ControllerListener
	{
		public void onKeyEvent(KeyEvent event)
		{
			Integer playerNum = GL2JNIActivity.deviceDescriptor_PlayerNum.get(GL2JNIActivity.deviceId_deviceDescriptor.get(event.getControllerId()));

	    		if (playerNum == null)
				return;

	    		String[] players = act.getResources().getStringArray(R.array.controllers);
	    		String id = "_" + players[playerNum].substring(
	    				players[playerNum].lastIndexOf(" ") + 1, players[playerNum].length());
	    		if (prefs.getBoolean("modified_key_layout"  + id, false)) {
	    			float x = -1, y = -1;
	    			if (event.getKeyCode() == prefs.getInt("l_button" + id, OuyaController.BUTTON_L1)) {
	    				float LxC = prefs.getFloat("touch_x_shift_left_trigger", 0);
	    				float LyC = prefs.getFloat("touch_y_shift_left_trigger", 0);
	    				x = 440 + LxC + 1;
	    				y = 200 + LyC + 1;
	    			}
	    			if (event.getKeyCode() == prefs.getInt("r_button" + id, OuyaController.BUTTON_R1)) {
	    				float RxC = prefs.getFloat("touch_x_shift_right_trigger", 0);
	    				float RyC = prefs.getFloat("touch_y_shift_right_trigger", 0);
	    				x = 542 + RxC + 1;
	    				y = 200 + RyC + 1;
	    			}
	    			if (mView != null && (x != -1 || y != -1)) {
	    				JNIdc.show_osd();
	    				long downTime = SystemClock.uptimeMillis();
	    				long eventTime = SystemClock.uptimeMillis() + 100;
	    				int metaState = 0;
	    				android.view.MotionEvent motionEvent = android.view.MotionEvent.obtain(downTime, eventTime,
	    						android.view.MotionEvent.ACTION_UP, x, y, metaState);
	    				mView.dispatchTouchEvent(motionEvent);
	    				if (playerNum == 0)
	    					JNIdc.hide_osd();
	    				return;
	    			}
	    		}

			if(playerNum == 0)
				JNIdc.hide_osd();

			for (int i = 0; i < map.length; i += 2) {
				if (map[playerNum][i + 0] == event.getKeyCode()) {
					if (event.getAction() == 0) //FIXME to const
						GL2JNIView.kcode_raw[playerNum] &= ~map[playerNum][i + 1];
					else
						GL2JNIView.kcode_raw[playerNum] |= map[playerNum][i + 1];
					break;
				}
			}
		}

		public void onMotionEvent(MotionEvent event)
		{
			Integer playerNum = GL2JNIActivity.deviceDescriptor_PlayerNum.get(GL2JNIActivity.deviceId_deviceDescriptor.get(event.getControllerId()));

	    		if (playerNum == null)
				return;

			if(playerNum == 0)
				JNIdc.hide_osd();

			float LS_X = event.getAxisValue(MotionEvent.AXIS_X);
			float LS_Y = event.getAxisValue(MotionEvent.AXIS_Y);
			float L2 = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
			float R2 = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);

			GL2JNIView.lt[playerNum] = (int) (L2 * 255);
			GL2JNIView.rt[playerNum] = (int) (R2 * 255);

			GL2JNIView.jx[playerNum] = (int) (LS_X * 126);
			GL2JNIView.jy[playerNum] = (int) (LS_Y * 126);

			/*
			for(final Entry<Integer, ExampleFloat> entry : mMotions.entrySet())
			{
				final int key = entry.getKey();
				final ExampleFloat value = entry.getValue();
				value.mValue = event.getAxisValue(key);
			}*/
		}

		public void onStateEvent(StateEvent event)
		{
			Integer playerNum = GL2JNIActivity.deviceDescriptor_PlayerNum.get(GL2JNIActivity.deviceId_deviceDescriptor.get(event.getControllerId()));

	    		if (playerNum == null)
				return;

			if(playerNum == 0)
				JNIdc.hide_osd();

			if (event.getState() == StateEvent.STATE_CONNECTION && event.getAction() == ACTION_CONNECTED) {
        		int mControllerVersion = mController.getState(Controller.STATE_CURRENT_PRODUCT_VERSION);
        		if (mControllerVersion == Controller.ACTION_VERSION_MOGAPRO) {
        			isActive[playerNum] = true;
        			isMogaPro[playerNum] = true;
        			setModifiedKeys(playerNum);
        			notify = act.getApplicationContext().getString(R.string.moga_pro_connect);
        		} else if (mControllerVersion == Controller.ACTION_VERSION_MOGA) {
        			isActive[playerNum] = true;
        			isMogaPro[playerNum] = false;
        			setModifiedKeys(playerNum);
        			notify = act.getApplicationContext().getString(R.string.moga_connect);
        		}
        		if (notify != null && !notify.equals(null)) {
        			handler.post(new Runnable() {
    					public void run() {
    						Toast.makeText(act.getApplicationContext(), notify, Toast.LENGTH_SHORT).show();
    					}
    				});
        		}
			}
		}
	}
}
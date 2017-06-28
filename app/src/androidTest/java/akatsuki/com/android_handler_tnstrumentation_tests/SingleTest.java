package akatsuki.com.android_handler_tnstrumentation_tests;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Created by Akatsuki on 2017/6/28.
 */
@RunWith(AndroidJUnit4.class)
public class SingleTest {


	//需要先啟動一個Activity來模擬Android 環境, 不然Handle不會正常運作
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	//Test
	@Test
	public void useAppTest() throws Exception {

		//countDownLatch來防止Test做完還沒有回Handler就馬上結束, 所給的參數數量多少, 你的countdown就要用多少次
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		Context appContext = InstrumentationRegistry.getTargetContext();

		//androidTest本身並不是一個android環境, 用Thread來模擬出Android環境, 讓Handler正常執行
		new Thread(new Runnable() {
			@Override
			public void run() {
				//Handler要預先使用Looper來管理Message, 否則Handler不會正常運作
				//P.S. 在一般Android環境內的Activity不需要使用, 系統自動幫你準備好
				Looper.prepare();
				final Handler handler = new Handler(new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						assertEquals(String.valueOf(msg.what), "111");
						//countDownLatch當初傳入1, 所以要用一次countDown來結束這次的測試
						countDownLatch.countDown();

						return true;
					}
				});

				new Thread(new Runnable() {
					@Override
					public void run() {
						Message message = handler.obtainMessage(111);
						handler.sendMessage(message);
					}
				}).start();

				Looper.loop();
			}
		}).start();


		try {
			//讓這次的Test不要結束, 等帶Handler回來
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}

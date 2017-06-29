# Android_Handler_Tnstrumentation_Tests
AndrroidResource


androidTest本身並不是一個android環境, 用Thread來模擬出Android環境, 讓Handler正常執行

 1.使用Junit 做測試
```
  @RunWith(AndroidJUnit4.class)
```

 2.需要先啟動一個Activity來模擬Android 環境, 不然Handle不會正常運作
```
	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
```

  3.運用countDownLatch來防止Test做完還沒有回Handler就馬上結束
```
CountDownLatch countDownLatch = new CountDownLatch(1);
```

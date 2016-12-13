package com.hyc.baige;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.extend.CardMd5;
import com.extend.DataString;
import com.extend.DeleteFilePic;
import com.extend.InstallAPK;
import com.extend.PicDispose;
import com.hyc.bean.APKInfo;
import com.hyc.bean.Company;
import com.hyc.bean.ICCardTime;
import com.hyc.bean.ImgInfo;
import com.hyc.bean.MacEntity;
import com.hyc.bean.NameClass;
import com.hyc.bean.Stu;
import com.hyc.db.DBMacAddress;
import com.hyc.db.DBManagerAdvert;
import com.hyc.db.DBManagerCard;
import com.hyc.db.DBManagerCompany;
import com.hyc.db.DBManagerICCardTime;
import com.hyc.db.DBManagerSchPic;
import com.hyc.db.DBManagerStu;
import com.hyc.db.DBUpload;
import com.hyc.network.GetDeviceID;
import com.hyc.network.IsNetWork;
import com.hyc.network.NetReceiver;
import com.hyc.rec.RecAccessToken;
import com.hyc.rec.RecICCardTime;
import com.hyc.rec.RecNotice;
import com.hyc.rec.RecOneCard;
import com.hyc.rec.RecSchoolInfo;
import com.hyc.rec.RecSchoolPicInfo;
import com.hyc.rec.RecVerSionAPK;
import com.hyc.rec.RecWeather;
import com.hyc.rec.RequestDataOSS;
import com.hyc.rec.ResInstallAPK;
import com.hyc.up.OSSSample;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tony.autoscroll.AutoScrollView;

public class MainActivity extends Activity {
	// TODO 蓝点是可能需要改的地方
	// ------------------------------------------
	DBManagerStu dbManagerstu = new DBManagerStu();
	DBManagerCard dbManagercard = new DBManagerCard();
	DBManagerAdvert dBManageradvert = new DBManagerAdvert();
	DBManagerSchPic dBManagerSchPic = new DBManagerSchPic();
	private String name = null;
	private String classname = null;
	private Context context;
	private EchoThread echoThread = new EchoThread();
	private int client_num;
	private int allcount = 0;
	private int card_count = 0;
	public static int upallcount = 0;

	private boolean noOss = false;
	public static String accesstoken;

	private Broad broad;

	private boolean isProducePic = true;
	private boolean isNetWorkNormal = false;
	private boolean school = true;
	private ServerS servers;
	private ServerSocket server;
	private String IP = null;
	private String serverAddress;
	private DBUpload db;
	private DBMacAddress dbMac;
	private SQLiteDatabase dbWriter, dbReader;
	private List<String> paths;

	private int SchoolID = 0;
	public static Intent intent;
	private OSS oss;
	private List<String> list = new ArrayList<String>();
	public static final String action = "jason.broadcast.action";
	private ImgInfo mImginfo;
	private String parment;
	// ------------------------------------------
	private ImageView imageView1, imageView2, imageView3, imageView4,
			imageView5, imageView6, imageView7, imageView8, imageLOGO, m_pic1,
			m_pic2, m_pic3, m_pic4;

	private TextView textView1_1, textView1_2, textView2_1, textView2_2,
			textView3_1, textView3_2, textView4_1, textView4_2, textView5_1,
			textView5_2, textView6_1, textView6_2, textView7_1, textView7_2,
			textView8_1, textView8_2, textConnect, textIntroduce, textAllcount,
			textAAA, textNotice, textWeather, myWeather;

	private String city, district;

	public static Timer timerAd;
	private Timer timer, timercardno, timerToast, timerTwoUp, timercont,
			timeToken;
	private TimerTask task, taskcont, taskTwoUp, taskToken;
	// ------------------------------------------
	private int imgvCount = 0;
	int number = 0;
	private long tt = 4294967295L;
	// ------------------------------------------

	public static double latitude = 39.9;
	public static double longitude = 116.3;
	private IsNetWork work = new IsNetWork();
	public static int state = 0;
	public static TimerTask timerTask;

	// ------------------------------------------
	// NetReceiver mReceiver = new NetReceiver();
	IntentFilter mFilter = new IntentFilter();

	// 存储学校信息
	DBManagerCompany dbCompany = new DBManagerCompany();
	Company company = new Company();

	// 时间的两个变量
	private static final int msgKey1 = 1;
	private TextView mTime, mData;

	// 線程池
	private ExecutorService mExecutorService = null;
	private ThreadPoolExecutor threadPoolSoc = null;

	// 更新提示
	private Toast toast;

	// 自定义刷卡时间
	DBManagerICCardTime dbCardTime;
	List<ICCardTime> cardTimelist = null;
	int timeIdentify = 0;

	// 屏保
	private LinearLayout linearLayout = null;
	private Animation animation;
	private boolean isOpenPic = false;
	public static Timer screenTimer;
	public static TimerTask screenTimerTask;

	// uphandler数目
	private int upNum = 0;
	private int nameMum = 0;

	// 滚动通知公告
	private AutoScrollView scrollView;

	// ------------------------------------------
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉TITLE
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// 去掉虚拟键(API必须大于或等于14)
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

		setContentView(R.layout.activity_main);
		toast = Toast.makeText(MainActivity.this, "正在更新数据，请不要刷卡 ^_^！",
				Toast.LENGTH_LONG);

		registerUpdate();

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

		db = new DBUpload(MainActivity.this);
		dbWriter = db.getWritableDatabase();
		dbReader = db.getReadableDatabase();
		db.insertTwo("0");
		dbWriter.delete("filepaths", null, null);
		dbWriter.delete("allpaths", null, null);
		dbManagercard.creatDB();
		dBManagerSchPic.creatDB();
		dbManagerstu.creatDB();
		paths = new ArrayList<String>();

		// mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// registerReceiver(mReceiver, mFilter);
		// mReceiver.setBRInteractionListener(this);

		imageView1 = (ImageView) this.findViewById(R.id.imageView1);
		imageView2 = (ImageView) this.findViewById(R.id.imageView2);
		imageView3 = (ImageView) this.findViewById(R.id.imageView3);
		imageView4 = (ImageView) this.findViewById(R.id.imageView4);
		imageView5 = (ImageView) this.findViewById(R.id.imageView5);
		imageView6 = (ImageView) this.findViewById(R.id.imageView6);
		imageView7 = (ImageView) this.findViewById(R.id.imageView7);
		imageView8 = (ImageView) this.findViewById(R.id.imageView8);

		m_pic1 = (ImageView) this.findViewById(R.id.m_pic1);
		m_pic2 = (ImageView) this.findViewById(R.id.m_pic2);
		m_pic3 = (ImageView) this.findViewById(R.id.m_pic3);
		m_pic4 = (ImageView) this.findViewById(R.id.m_pic4);

		textView1_1 = (TextView) this.findViewById(R.id.textView1_1);
		textView1_2 = (TextView) this.findViewById(R.id.textView1_2);
		textView2_1 = (TextView) this.findViewById(R.id.textView2_1);
		textView2_2 = (TextView) this.findViewById(R.id.textView2_2);
		textView3_1 = (TextView) this.findViewById(R.id.textView3_1);
		textView3_2 = (TextView) this.findViewById(R.id.textView3_2);
		textView4_1 = (TextView) this.findViewById(R.id.textView4_1);
		textView4_2 = (TextView) this.findViewById(R.id.textView4_2);
		textView5_1 = (TextView) this.findViewById(R.id.textView5_1);
		textView5_2 = (TextView) this.findViewById(R.id.textView5_2);
		textView6_1 = (TextView) this.findViewById(R.id.textView6_1);
		textView6_2 = (TextView) this.findViewById(R.id.textView6_2);
		textView7_1 = (TextView) this.findViewById(R.id.textView7_1);
		textView7_2 = (TextView) this.findViewById(R.id.textView7_2);
		textView8_1 = (TextView) this.findViewById(R.id.textView8_1);
		textView8_2 = (TextView) this.findViewById(R.id.textView8_2);

		textConnect = (TextView) this.findViewById(R.id.textConnect);
		textIntroduce = (TextView) this.findViewById(R.id.textIntroduce);
		textAllcount = (TextView) this.findViewById(R.id.allcount);
		textAAA = (TextView) this.findViewById(R.id.aaaa);
		textNotice = (TextView) this.findViewById(R.id.textNotice);
		textWeather = (TextView) this.findViewById(R.id.textWeather);
		myWeather = (TextView) this.findViewById(R.id.myWeather);

		imageLOGO = (ImageView) this.findViewById(R.id.school_LOGO);
		scrollView = (AutoScrollView) findViewById(R.id.auto_scrollview);

		// 23:59定时清空一次刷卡数跟上传数
		// clearDataTime();
		// 显示时间，日期，星期
		mTime = (TextView) findViewById(R.id.myTime);
		mData = (TextView) findViewById(R.id.myData);

		mTime.setText(DataString.StringData2());
		mData.setText(DataString.StringData3());

		// new TimeThread().start();

		PicDispose.copyToSD(MainActivity.this);

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				noOss = true;
				dbCompany.creatDB();

				RecVerSionAPK recVerSionAPK = new RecVerSionAPK();
				APKInfo aInfo = recVerSionAPK.getAPKVersion(MainActivity.this);

				// 下载服务端apk
				if (aInfo != null) {
					if (aInfo.getApkUrl() != null) {
						ResInstallAPK resInstallAPK = new ResInstallAPK();
						String path = aInfo.getApkUrl();
						resInstallAPK.getFileFromServer(path, schooleInfo);
					}
				}

				// 获取自定义刷卡时间
				RecICCardTime recICCardTime = new RecICCardTime();
				recICCardTime.recCardTime(mHandler);

				// 获取学校文字介绍
				RecSchoolInfo recSchoolInfo = new RecSchoolInfo(weatherHandler);
				recSchoolInfo.receiveSchInfo();

				// 屏保
				for (int i = 1; i < 5; i++) {
					// 获取展示图片
					RecSchoolPicInfo recSchoolPicInfo = new RecSchoolPicInfo(i);
					recSchoolPicInfo.receiveWeather();
				}

				// 获取公告
				RecNotice recNotice = new RecNotice();
				recNotice.receiveDate(MainActivity.this);

				// 通知主线程
				Message wthInfomsg = new Message();
				wthInfomsg.what = 1;
				MainActivity.this.weatherHandler.sendMessageDelayed(wthInfomsg,
						3000);

				try {
					Thread.sleep(5000);
					// 把学校信息存入数据库
					company = dbCompany.query();
					if (company.getName() == null) {
						System.out.println("添加学校信息到数据库");
						if (RecSchoolInfo.schoolName != null) {
							company.setSchoolid(RecSchoolInfo.logoId);
							company.setName(RecSchoolInfo.schoolName);
							company.setQq(RecSchoolInfo.qq);
							company.setMobile(RecSchoolInfo.mobile);
							company.setEmail(RecSchoolInfo.email);
							company.setProvince(RecSchoolInfo.province);
							company.setCity(RecSchoolInfo.city);
							company.setDistrict(RecSchoolInfo.district);
							company.setAddress(RecSchoolInfo.address);
							company.setContent(RecSchoolInfo.content);
							dbCompany.insert(company);
						}
					} else if (RecSchoolInfo.schoolName == null) {
						if (company.getName() != null) {
							Message msg = new Message();
							msg.what = 3;
							MainActivity.this.weatherHandler.sendMessage(msg);
						}
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		timer.schedule(task, 2000, 1800000);
		initTimer();
		twoUpTimer();

		// 定时加载accesstoken
		timeToken = new Timer();
		taskToken = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 获取AccessToken
				RecAccessToken mRecAccessToken = new RecAccessToken();
				mRecAccessToken.postAccessToken(MainActivity.this);
			}
		};
		timeToken.schedule(taskToken, 0);

		// 删除启动缓存 启动服务端
		timercont = new Timer();
		taskcont = new TimerTask() {
			@Override
			public void run() {
				DeleteFilePic.deletelistFiles(new File(getDir()
						+ "/baige/picFile"));
				DeleteFilePic.deletelistFiles(new File(getDir()
						+ "/baige/twoFile"));
				servers = new ServerS();
				servers.start();
			}
		};
		timercont.schedule(taskcont, 3000);

		// 屏保
		linearLayout = (LinearLayout) findViewById(R.id.schoolinfo);
		animation = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.enlarge);

		Message message = new Message();
		message.what = 0;
		if (screenProtect != null) {
			// 控制屏保弹出时间*毫秒
			screenProtect.sendMessageDelayed(message, 60000);
		}

		// 设置刷卡间隔时间 道闸/中控60秒
		timercardno = new Timer();
		TimerTask task_cardno = new TimerTask() {

			@Override
			public void run() {

				// 控制刷卡时间段
				if (timeIdentify == 0) {
					dbManagercard.delete();
				}
				// 实时更新界面上的显示时间
				Message msg = new Message();
				msg.what = msgKey1;
				mHandler.sendMessage(msg);

				// 定时清除界面的刷卡数跟上传数
				if (DataString.StringData2().equals("23:59")) {
					Message clear_allcount = new Message();
					clear_allcount.what = 456;
					mHandler.sendMessage(clear_allcount);
				}

				// 判断当前时间是否在自定义刷卡时间内
				if (cardTimelist != null) {
					if (cardTimelist.size() > 0) {
						if (new DataString().isSwipingCard(cardTimelist)) {
							// 发消息让socket断开连接，关闭刷卡功能
							System.out.println("打开刷卡");
							mHandler.sendEmptyMessage(110);
						} else {
							// 发消息让socket打开连接，开启刷卡功能
							System.out.println("关闭刷卡");
							mHandler.sendEmptyMessage(111);
						}
					} else {
						mHandler.sendEmptyMessage(110);
					}
				} else {
					mHandler.sendEmptyMessage(110);
				}
				if (oss == null && new IsNetWork().isNetWork()) {
					Message oss_init = new Message();
					oss_init.what = 4;
					weatherHandler.sendMessage(oss_init);
				}
			}
		};
		timercardno.schedule(task_cardno, 60000, 60000);

		IntentFilter filter = new IntentFilter(action);
		registerReceiver(broadcastReceiver, filter);

		// 自定义刷卡时间
		cardTimelist = new ArrayList<ICCardTime>();
		dbCardTime = new DBManagerICCardTime();
		dbCardTime.creatDB();
		// cardTimelist = dbCardTime.query();

		// 调用屏保轮播定时器
		screenSaver();

	}// onCreate 方法結束

	// 发送屏保
	Handler screenProtect = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				linearLayout.startAnimation(animation);
				linearLayout.setVisibility(View.VISIBLE);
				isOpenPic = true;

				break;
			case 1:
				linearLayout.clearAnimation();
				linearLayout.setVisibility(View.INVISIBLE);
				isOpenPic = false;
				break;
			}
		}
	};

	// 屏保图片设置定时器
	private void screenSaver() {
		screenTimer = new Timer();
		screenTimerTask = new TimerTask() {
			int picFileNum = 0;

			@Override
			public void run() {
				if (isOpenPic) {
					File f = new File(getDir() + "/baige/campusPicFile");
					if (f.exists()) {
						File files[] = f.listFiles();
						for (int i = 0; i < files.length; i++) {
							if (files[i].isDirectory()) {
								File filePics[] = files[i].listFiles();
								if (filePics.length > 0
										&& filePics.length > picFileNum) {
									Message screenMsg = new Message();
									screenMsg.what = i;
									Bundle bun = new Bundle();
									bun.putString("url",
											filePics[picFileNum].getPath());
									screenMsg.setData(bun);
									MainActivity.this.screenHandler
											.sendMessage(screenMsg);
								}

							}
						}

						picFileNum++;
						if (picFileNum == 3) {
							picFileNum = 0;
						}
					}
				}

			}
		};

		// screenTimer.schedule(screenTimerTask, 10000, 120000);
		screenTimer.schedule(screenTimerTask, 5000, 5000);

	}

	// 屏保图片设置Handler
	Handler screenHandler = new Handler() {
		public void handleMessage(Message msg) {
			File file1 = new File(msg.getData().getString("url"));
			Uri uri = Uri.fromFile(file1);

			switch (msg.what) {
			case 0:
				m_pic1.setImageURI(uri);
				break;
			case 1:
				m_pic2.setImageURI(uri);
				break;
			case 2:
				m_pic3.setImageURI(uri);
				break;
			case 3:
				m_pic4.setImageURI(uri);
				break;

			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler weatherHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				Bundle bun = msg.getData();
				city = bun.getString("city");
				district = bun.getString("district");
				if (city.indexOf("市") != -1) {
					city = city.substring(0, city.lastIndexOf("市"));
				}
				if (city.indexOf("地区") != -1) {
					city = city.substring(0, city.lastIndexOf("地区"));
				}
				if (city.indexOf("自治州") != -1) {
					city = city.substring(0, city.lastIndexOf("自治州"));
				}
				if (city.indexOf("自治区") != -1) {
					city = city.substring(0, city.lastIndexOf("自治区"));
				}
				if (district.indexOf("县") != -1) {
					district = district.substring(0, district.lastIndexOf("县"));
				}

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						RecWeather mGetWeather = new RecWeather();
						mGetWeather.request(city);
						System.out.println(city + "     " + district);
						System.out.println("天气天气天气天气天气天气天气天气");

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						RecWeather mGetWeather = new RecWeather();
						if (mGetWeather.wendu.equals("暂无")) {
							new AsyncTask<Void, Void, Void>() {

								@Override
								protected Void doInBackground(Void... params) {
									RecWeather mGetWeather = new RecWeather();
									mGetWeather.request(district);
									System.out.println(city + "     "
											+ district);
									System.out.println("天气天气天气天气天气天气天气天气");
									return null;
								}

								protected void onPostExecute(Void result) {
									RecWeather mGetWeather = new RecWeather();
									textWeather.setText("♥百鸽提示您♥:"
											+ mGetWeather.ganmao);
									myWeather
											.setText("天气：" + mGetWeather.type
													+ "\n风力："
													+ mGetWeather.fengli
													+ "\n温度："
													+ mGetWeather.wendu + "℃");
								};
							}.execute();
						} else {
							textWeather
									.setText("♥百鸽提示您♥:" + mGetWeather.ganmao);

							myWeather.setText("天气：" + mGetWeather.type
									+ "\n风力：" + mGetWeather.fengli + "\n温度："
									+ mGetWeather.wendu + "℃");
						}
						super.onPostExecute(result);
					}

				}.execute();

				break;
			case 1:
				// 广告文字滚动
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (!scrollView.isScrolled()) {
							scrollView.setScrolled(true);
						}
					}
				}, 6000);
				// 显示学校信息
				textIntroduce.setText(RecSchoolInfo.schoolName + "\r\n联系方式："
						+ "QQ:" + RecSchoolInfo.qq + "\t电话："
						+ RecSchoolInfo.mobile + "\tEmail："
						+ RecSchoolInfo.email + "\r\n学校地址："
						+ RecSchoolInfo.province + RecSchoolInfo.city
						+ RecSchoolInfo.district + RecSchoolInfo.address);

				String mContent = RecNotice.content;
				mContent = mContent.replace("&nbsp;", "");
				textNotice.setText("通知公告：" + mContent);
				// 显示学校LOGO
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/baige/LOGOFile/0.jpg");
				if (file.exists()) {
					imageLOGO.setImageURI(Uri.fromFile(file));
				}
				break;
			case 3:
				Log.v("dddd", "没网络 去本地找学校信息");
				company = dbCompany.query();
				textIntroduce.setText(company.getName() + "\r\n联系方式：" + "QQ:"
						+ company.getQq() + "\t电话：" + company.getMobile()
						+ "\tEmail：" + company.getEmail() + "\r\n学校地址："
						+ company.getProvince() + company.getCity()
						+ company.getDistrict() + company.getAddress());

				textWeather.setText("♥百鸽提示您♥: 好好学习，天天向上");
				myWeather.setText("正在获取天气");
				break;
			case 4:
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						RecSchoolInfo recSchoolInfo = new RecSchoolInfo();
						recSchoolInfo.receiveSchInfo();
						return null;
					}

					protected void onPostExecute(Void result) {
						SchoolID = RecSchoolInfo.Id;
						if (SchoolID != 0 && String.valueOf(SchoolID) != null) {

							getOSSCertificate();
						} else {

							// 第一次启动apk没网络 从本地数据库获取学校ID
							if (dbCompany.query() != null) {
								company = dbCompany.query();
								int number = 0;

								number = company.getSchoolid();
								if (number != 0) {
									SchoolID = company.getSchoolid();
									getOSSCertificate();
								}
							}

						}
					};
				}.execute();
				break;
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case msgKey1:
				// 更新主界面时间
				mTime.setText(DataString.StringData2());
				mData.setText(DataString.StringData3());

				break;
			case 456:
				// 定时清除刷卡数跟上传数
				upallcount = 0;
				allcount = 0;
				textAllcount.setText("刷卡数 : " + allcount);
				textAAA.setText("上传数 : " + upallcount);
				break;
			case 120:
				textConnect.setText("网络状态：已连接");
				isNetWorkNormal = true;
				break;
			case 121:
				textConnect.setText("网络状态：未连接");
				isNetWorkNormal = false;
				break;
			case 222:
				// 自定义设置刷卡时间
				cardTimelist = dbCardTime.query();
				break;
			case 111:
				timeIdentify = 1;
				break;
			case 110:
				timeIdentify = 0;
				break;
			case 77:
				Toast.makeText(MainActivity.this, "卡号位数不正确", Toast.LENGTH_SHORT)
						.show();
				break;
			case 88:
				Toast.makeText(MainActivity.this, "刷卡设备连接超时或异常！",
						Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(MainActivity.this, "连接超时或异常！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void getOSSCertificate() {

		// 初始化OSS
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				list = new RequestDataOSS().uploadFileOss(MainActivity.this,
						String.valueOf(SchoolID));
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				new Thread() {
					public void run() {
						if (work.isNetWork() && list.size() == 3) {

							OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
									list.get(0), list.get(1), list.get(2));
							ClientConfiguration ccf = new ClientConfiguration();
							ccf.setConnectionTimeout(3000);
							ccf.setSocketTimeout(3000);
							oss = new OSSClient(MainActivity.this,
									"http://oss-bj.360baige.cn",
									credentialProvider, ccf);
							try {
								String url = oss.presignConstrainedObjectURL(
										"sdk-baige", "logo.jpg", 3600);
								System.out
										.println(url
												+ "oooooooooooooooooooooookkkkkkkkkkkkk");
							} catch (ClientException e) {
								// TODO Auto-generated catch
								// block
								e.printStackTrace();
							}
						}
					};
				}.start();
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}

		}.execute();

	}

	// 定时启动二次上传
	private void initTimer() {
		timerAd = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Log.e("haha", "哈哈哈哈哈哈哈");
				if (work.isNetWork() == true) {
					mHandler.sendEmptyMessage(120);
					if (noOss) {
						weatherHandler.sendEmptyMessage(4);
						noOss = false;
					}
					Log.e("haha", "网络正常");

					if (school) {
						RecSchoolInfo recSchoolInfo = new RecSchoolInfo();
						recSchoolInfo.receiveSchoolInfoMain(schooleInfo,
								getMac(), MainActivity.this);

						// 获取学校文字介绍
						RecSchoolInfo recSchoolInfo1 = new RecSchoolInfo(
								weatherHandler);
						recSchoolInfo1.receiveSchInfo();

						// 获取公告
						RecNotice recNotice = new RecNotice();
						recNotice.receiveDate(MainActivity.this);
						weatherHandler.sendEmptyMessage(1);

						school = false;
					}
				} else {
					mHandler.sendEmptyMessage(121);
				}
			}
		};

		timerAd.schedule(timerTask, 0, 3000);
	}

	private void twoUpTimer() {
		timerTwoUp = new Timer();
		taskTwoUp = new TimerTask() {
			@Override
			public void run() {
				if (oss != null && isNetWorkNormal == true) {
					if (db.queryTwo() != null) {
						if (db.queryTwo().equals("0")) {
							two_compressPic();
						}
					}
				}
			}
		};

		timerTwoUp.schedule(taskTwoUp, 0, 3000);
	}

	@SuppressWarnings({ "resource" })
	private void compressPic(ImgInfo imginfo) {
		Log.e("haha", "一次上传启动");
		screenProtect.removeMessages(0);
		Message hidemsg = new Message();
		hidemsg.what = 1;
		MainActivity.this.screenProtect.sendMessage(hidemsg);

		// allcount++;
		try {
			// TODO Auto-generated method stub
			try {
				if (oss != null && isNetWorkNormal == true) {
					System.out.println("22222222222222");
					System.out.println("imginfo" + imginfo.getCardno());
					String testObject = imginfo.getFile().substring(
							imginfo.getFile().lastIndexOf("/") + 1,
							imginfo.getFile().lastIndexOf("."));

					Date nowTime = new Date(System.currentTimeMillis());
					SimpleDateFormat sdFormatter = new SimpleDateFormat(
							"yyyyMMdd");
					String retStrFormatNowDate = sdFormatter.format(nowTime);

					final String uploadFilePath = imginfo.getFile();

					parment = "baige2"
							+ "/"
							+ RecSchoolInfo.Id
							+ "/"
							+ retStrFormatNowDate
							+ "/"
							+ new CardMd5().GetMD5Code(testObject
									+ System.currentTimeMillis()) + ".jpg";
					System.out.println(parment);
					new OSSSample(MainActivity.this, uploadFilePath, oss,
							imginfo, parment, dbWriter, mHandler, screenProtect)
							.upload();

				} else {
					ContentValues values = new ContentValues();
					values.put("type", imginfo.getType());
					values.put("cardno", imginfo.getCardno());
					values.put("alluploadpaths", imginfo.getFile());
					values.put("timecode", System.currentTimeMillis() / 1000);
					dbWriter.insert("allpaths", null, values);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v("tt", "http://" + "sdk-baige."
					+ "oss-cn-beijing.aliyuncs.com/" + serverAddress);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	// http://sdk-baige.oss-cn-beijing.aliyuncs.com/2813/2016-04-13/c25e34b4f1ef506928f1d6bf52b6270f
	private void two_compressPic() {

		ArrayList<String> reup = GetFileName();
		System.out.println("待传人数" + reup.size());
		db.insertTwo("1");
		try {
			context = MainActivity.this;
			int num = reup.size();
			for (int j = 0; j < num; j++) {
				ArrayList<String> listCode = queryRecode(reup.get(j));
				String content = reup.get(j);
				int type = Integer.parseInt(listCode.get(1));
				long cardno = Long.parseLong(listCode.get(0));
				long time = Long.parseLong(listCode.get(2));

				ImgInfo info = new ImgInfo();
				info.setType(type);
				info.setCardno(cardno);

				String testObject = content.substring(
						content.lastIndexOf("/") + 1, content.lastIndexOf("."));

				Date nowTime = new Date(System.currentTimeMillis());
				SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd");
				String retStrFormatNowDate = sdFormatter.format(nowTime);

				String filePath = content;

				parment = "baige2"
						+ "/"
						+ RecSchoolInfo.Id
						+ "/"
						+ retStrFormatNowDate
						+ "/"
						+ new CardMd5().GetMD5Code(testObject
								+ System.currentTimeMillis()) + ".jpg";

				if (oss != null) {
					new OSSSample(MainActivity.this, filePath, oss, info,
							parment, dbWriter, mHandler, screenProtect)
							.sycupload(time);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.insertTwo("0");
		}
		// reup.clear();
		// paths.clear();
	}

	public void transImage1(String fromFile, String toFile, int width,
			int height, int quality) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = (float) width / bitmapWidth;
			float scaleHeight = (float) height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmapWidth, bitmapHeight, matrix, false);
			// save file
			File myCaptureFile = new File(toFile);
			FileOutputStream out = new FileOutputStream(myCaptureFile);
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
				out.close();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler upHandler = new Handler() {
		public void handleMessage(Message msg) {
			// System.out.println(imgvCount);
			if (msg.what == 1) {
				File file1 = new File(msg.getData().getString("File"));
				// new
				// ComprssBitmap().getSmallBitmap(msg.getData().getString("File"));
				Uri uri = Uri.fromFile(file1);
				ImgInfo imginfo = new ImgInfo();
				imginfo.setCardno(msg.getData().getLong("cardno"));
				imginfo.setType(msg.getData().getInt("type"));
				imginfo.setFile(msg.getData().getString("File"));
				mImginfo = imginfo;
				switch (imgvCount) {
				case 0:
					// Log.e("uri", uri.toString());
					// displayFromSDCard(uri.toString(), imageView1);
					imageView1.setImageURI(uri);
					// imageView1.setImageBitmap(bitmap);
					// System.out.println("imageView1");
					imgvCount++;
					break;
				case 1:
					imageView2.setImageURI(uri);
					// imageView2.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView2);
					imgvCount++;
					break;
				case 2:
					imageView3.setImageURI(uri);
					// imageView3.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView3);
					imgvCount++;
					break;
				case 3:
					imageView4.setImageURI(uri);
					// imageView4.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView4);
					imgvCount++;
					break;
				case 4:
					imageView5.setImageURI(uri);
					// imageView5.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView5);
					imgvCount++;
					break;
				case 5:
					imageView6.setImageURI(uri);
					// imageView6.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView6);
					imgvCount++;
					break;
				case 6:
					imageView7.setImageURI(uri);
					// imageView7.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView7);
					imgvCount++;
					break;
				case 7:
					imageView8.setImageURI(uri);
					// imageView8.setImageBitmap(bitmap);
					// displayFromSDCard(uri.toString(), imageView8);
					imgvCount = 0;
					break;
				default:
					break;
				}
				compressPic(imginfo);

			}
		}
	};

	public static byte[] fromHex(String hexString) throws NumberFormatException {
		hexString = hexString.trim();
		String s[] = hexString.split(" ");
		byte ret[] = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			ret[i] = (byte) Integer.parseInt(s[i], 16);
		}
		return ret;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String mClassname = "班级：";
				int mType = 8;
				mType = msg.getData().getInt("type");

				if (mType != 8) {
					mClassname = "职位：";
				}
				name = msg.getData().getString("name");
				classname = msg.getData().getString("classname");
				switch (imgvCount) {
				case 0:
					textView1_1.setText("姓名：" + name);
					textView1_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 1:
					textView2_1.setText("姓名：" + name);
					textView2_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 2:
					textView3_1.setText("姓名：" + name);
					textView3_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 3:
					textView4_1.setText("姓名：" + name);
					textView4_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 4:
					textView5_1.setText("姓名：" + name);
					textView5_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 5:
					textView6_1.setText("姓名：" + name);
					textView6_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 6:
					textView7_1.setText("姓名：" + name);
					textView7_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum++;
					break;
				case 7:
					textView8_1.setText("姓名：" + name);
					textView8_2.setText(mClassname + classname);
					textAllcount.setText("刷卡数 : " + allcount);
					// textAAA.setText("上传数 : " + upallcount);
					name = null;
					classname = null;
					// nameMum=0;
					break;

				default:
					break;
				}
			}

		}
	};

	Handler schooleInfo = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				dbManagerstu.delete();
				Log.e("在这下载", "下载数据");
				Log.e("comecome", "进入更新数据了0.6");
				new Thread() {
					public void run() {
						Login l = new Login();
						l.myFun(MainActivity.this);
					};
				}.start();
				break;
			case 3:
				MainActivity.intent = new Intent(MainActivity.this,
						MyService.class);
				startService(MainActivity.intent);
				break;
			case 5:
				// 没换学校，日常更新数据
				Log.e("comecome", "进入更新数据了5.0");
				dbManagerstu.delete();
				new Thread() {
					public void run() {
						Login l = new Login();
						l.myFun(MainActivity.this);
					};
				}.start();
				break;

			case 6:
				// 静默安装apk
				InstallAPK installAPK = new InstallAPK();
				installAPK.onClick_install();
				break;
			}

		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// if (mReceiver != null) {
		// unregisterReceiver(mReceiver);
		// }
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		if (intent != null) {
			stopService(intent);
		}

		upallcount = 0;

		Login.accesstoken = null;

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerAd != null) {
			timerAd.cancel();
			timerAd = null;
		}
		if (timercont != null) {
			timercont.cancel();
			timercont = null;
		}

		if (timerTwoUp != null) {
			timerTwoUp.cancel();
			timerTwoUp = null;
		}

		if (broad != null) {
			unregisterReceiver(broad);
		}

		if (dbCardTime != null) {
			dbCardTime.closeDB();
		}
		dbManagercard.closeDB();
		dbManagerstu.closeDB();
		// dBManageradvert.closeDB();
		dBManagerSchPic.closeDB();

		if (dbMac != null) {
			dbMac.closeDBMac();
		}

		if (scrollView.isScrolled()) {
			scrollView.setScrolled(false);
		}

		try {

			if (server != null)
				server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			echoThread.server_socket.close();
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("关闭------------------");
		// super.onDestroy();
	}

	// 更新数据广播
	private void registerUpdate() {
		broad = new Broad();
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("com.baige.ui.service");
		registerReceiver(broad, filter);// 注册Broadcast Receive
	}

	private class Broad extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getStringExtra("miss") != null) {
				if (intent.getStringExtra("miss").equals("2")) {
					timeIdentify = 0;
					if (toast != null) {
						if (timerToast != null) {
							timerToast.cancel();
							toast.cancel();
						}
					}
				}
			}

			if (intent.getStringExtra("reflush") != null) {
				if (intent.getStringExtra("reflush").equals("1")) {
					timeIdentify = 1;
					if (toast != null) {
						timerToast = new Timer();
						timerToast.schedule(new TimerTask() {
							@Override
							public void run() {
								toast.show();
							}
						}, 0, 3000);
					}
				}
			}
		}

	}

	private class ServerS extends Thread {
		@Override
		public void run() {
			super.run();

			try {
				server = new ServerSocket(3333);
				// mExecutorService = Executors.newCachedThreadPool();//
				// 創建一個線程pool
				threadPoolSoc = new ThreadPoolExecutor(3, 50, 0,
						TimeUnit.SECONDS,
						// 缓冲队列为3
						new ArrayBlockingQueue<Runnable>(3),
						// 抛弃旧的任务
						new ThreadPoolExecutor.DiscardOldestPolicy());

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			while (true) {
				try {
					if (server != null) {
						Socket server_socket = server.accept();
						threadPoolSoc.execute(new EchoThread(server_socket));
						// mExecutorService.execute(new
						// EchoThread(server_socket));// 執行里面的線程
						Log.e("线程", "...线程线程...");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	class EchoThread extends Thread {

		private Socket server_socket;
		private BufferedOutputStream outStream = null;
		private String socket_class, socket_name;
		private int type;
		private File pictureFile = null;
		private String picFilename, picFilepath = null;
		private Long cardno;
		private NameClass nameClass;

		private boolean out = true;
		private boolean cardok = true;
		private boolean carbad = true;
		private boolean picok = true;
		private boolean picbad = true;

		private String result = null;
		private int l = 0;
		private int j = 0;

		private int resetTime = 0;

		public EchoThread(Socket server_socket) {
			this.server_socket = server_socket;
		}

		public EchoThread() {

		}

		@Override
		public void run() {
			BufferedInputStream br = null;
			OutputStream server_out = null;
			try {
				server_socket.setSoLinger(true, 0);
				server_socket.setReuseAddress(true);
				server_socket.setSoTimeout(10000);
				br = new BufferedInputStream(server_socket.getInputStream());
				server_out = server_socket.getOutputStream();

				boolean temp = true;
				while (temp) {
					final byte[] buff = new byte[1024];
					int len = -1;

					while ((len = br.read(buff)) != -1) {
						result = new String(buff, 0, len);

						if (result.indexOf(result.valueOf("end")) != -1) {
							// System.out.println(result);
							if (result.indexOf(result.valueOf("cardok")) != -1
									&& cardok) {
								String[] card_string = result.split(":");

								for (int i = 0; i < card_string.length; i++) {

									if (i == 1) {
										byte[] card_data = new byte[len];
										for (int j = 0; j < len; j++) {
											card_data[j] = buff[j];
										}

										nameClass = card_client(server_out,
												card_data);
										long carNun = 0;
										if (nameClass != null) {

											carNun = nameClass.getCardno();
										}

										Date nowTime = new Date(
												System.currentTimeMillis());
										SimpleDateFormat sdFormatter = new SimpleDateFormat(
												"yyyy-MM-dd");

										String cardTime1 = sdFormatter
												.format(nowTime);
										CardMd5 cardMd5 = new CardMd5();

										if (carNun != 0) {
											serverAddress = SchoolID
													+ "/"
													+ cardTime1
													+ "/"
													+ cardMd5
															.GetMD5Code(carNun
																	+ nowTime
																			.toString());
											System.out.println(serverAddress
													+ "0000010");
										}

										if (nameClass != null) {
											Log.e("判断卡号", "en");
											temp = nameClass.getTemp();
											if (temp) {
												Log.e("卡号正确", "en");
												allcount++;
												nameClass.setStuNo(String
														.valueOf(allcount));
											}
										}
									}
								}
								cardok = false;
							} else if (result.indexOf(result
									.valueOf("cardokbad")) != -1 && carbad) {
								String[] cardbad_string = result.split(":");
								for (int i = 0; i < cardbad_string.length; i++) {
									if (i == 1) {
										// System.out.println(cardbad_string[i]);
									}
								}
								// outStream.write(buff, 0, len);
								// outStream.flush();
								temp = false;
								carbad = false;
							} else if (result.indexOf(result
									.valueOf("picturebad")) != -1 && picbad) {

								String[] picturebad_string = result.split(":");
								for (int i = 0; i < picturebad_string.length; i++) {
									if (i == 1) {
										System.out.println("picturebad...."
												+ picturebad_string[i]);
									}
								}
								// outStream.write(buff, 0, len);
								// outStream.flush();
								temp = false;
								picbad = false;
							} else {

								outStream.write(buff, 0, len);
								outStream.flush();
								l++;
								if (nameClass.getPath() != null) {
									File f = new File(nameClass.getPath());
									if (f.exists()) {
										String message2 = "recv:ok:end";
										server_out.write(message2
												.getBytes("UTF-8"));
										// System.out.println(message);
										server_out.flush();
									}
								}
							}
						} else {

							if (result.indexOf(result.valueOf("pictureok")) != -1
									&& picok) {
								Log.e("图片过来了", "过来了");
								String[] picture_string = result.split(":");

								int lenght = 0;
								for (int i = 0; i < 4; i++) {
									if (i == 1)
										lenght = picture_string[i].length();

									if ((i == 3) && (nameClass != null)) {

										picFilename = CardMd5.GetMD5Code(String
												.valueOf(System
														.currentTimeMillis()))
												+ nameClass.getStuNo();
										// picFilename
										// =Long.toString(physicsno);

										pictureFile = new File(
												Environment
														.getExternalStorageDirectory()
														+ "/baige/picFile/"
														+ picFilename + ".jpg");
										FileOutputStream outputStream = new FileOutputStream(
												pictureFile);
										outStream = new BufferedOutputStream(
												outputStream);
										outStream.write(buff, (18 + lenght),
												len - (18 + lenght));
										outStream.flush();

										nameClass.setPath(Environment
												.getExternalStorageDirectory()
												+ "/baige/picFile/"
												+ picFilename + ".jpg");
									}
								}
								picok = false;
								j++;
							} else {
								outStream.write(buff, 0, len);
								outStream.flush();

							}
						}

					}
					Log.e("跳出来", 555 + "");
					if (nameClass != null) {
						if (nameClass.getTemp() && nameClass.getPath() == null) {
							mHandler.sendEmptyMessage(88);
							String path = Environment
									.getExternalStorageDirectory()
									+ "/baige/picFile/"
									+ CardMd5.GetMD5Code(String.valueOf(System
											.currentTimeMillis()))
									+ nameClass.getStuNo() + ".jpg";
							if (new File(getDir() + "/baige/LOGOFile/1.jpg")
									.exists()) {
								new OSSSample().copyFile(getDir()
										+ "/baige/LOGOFile/1.jpg", path);
								nameClass.setPath(path);

								while (!new File(nameClass.getPath()).exists()) {

								}
								Message message1 = new Message();
								Bundle bundle = new Bundle();
								bundle.putString("name", nameClass.getName());
								bundle.putString("classname",
										nameClass.getClasses());
								bundle.putInt("type", nameClass.getType());
								System.out.println("刷卡人类型");
								System.out.println(nameClass.getType());

								message1.setData(bundle);
								message1.what = 1;
								MainActivity.this.handler.sendMessage(message1);

								Message message = new Message();
								message.what = 1;
								Bundle bundle1 = new Bundle();
								bundle1.putInt("type", nameClass.getType());
								bundle1.putLong("cardno", nameClass.getCardno());
								bundle1.putString("File", nameClass.getPath());
								message.setData(bundle1);
								MainActivity.this.upHandler
										.sendMessage(message);
								if (nameClass.getType() == 8) {
									dbManagercard.deletePre(Long
											.toString(nameClass.getCardno()));
								}
							}
							temp = false;

						} else {
							temp = false;
						}
					} else {
						temp = false;
					}

					if ((len == -1 && l > 0) || (len == -1 && j > 0)) {
						if (nameClass != null) {
							if (nameClass.getPath() != null) {
								if (new File(nameClass.getPath()).exists()) {
									if (isNoImage(nameClass.getPath())) {
										new OSSSample().copyFile(getDir()
												+ "/baige/LOGOFile/1.jpg",
												nameClass.getPath());
										while (!new File(nameClass.getPath())
												.exists()) {

										}
										if (nameClass.getTemp()) {
											Message message1 = new Message();
											Bundle bundle = new Bundle();
											bundle.putString("name",
													nameClass.getName());
											bundle.putString("classname",
													nameClass.getClasses());
											message1.setData(bundle);
											message1.what = 1;
											MainActivity.this.handler
													.sendMessage(message1);
											Message message = new Message();
											message.what = 1;
											Bundle bundle1 = new Bundle();
											bundle1.putInt("type",
													nameClass.getType());
											bundle1.putLong("cardno",
													nameClass.getCardno());
											bundle1.putString("File",
													nameClass.getPath());
											message.setData(bundle1);
											MainActivity.this.upHandler
													.sendMessage(message);

										}

										temp = false;
									} else {
										if (nameClass.getTemp()) {
											Message message1 = new Message();
											Bundle bundle = new Bundle();
											bundle.putString("name",
													nameClass.getName());
											bundle.putString("classname",
													nameClass.getClasses());
											bundle.putInt("type",
													nameClass.getType());
											System.out.println("刷卡人类型");
											System.out.println(nameClass
													.getType());

											message1.setData(bundle);
											message1.what = 1;

											MainActivity.this.handler
													.sendMessage(message1);
											Message message = new Message();
											message.what = 1;
											Bundle bundle1 = new Bundle();
											bundle1.putInt("type",
													nameClass.getType());
											bundle1.putLong("cardno",
													nameClass.getCardno());
											bundle1.putString("File",
													nameClass.getPath());
											message.setData(bundle1);
											MainActivity.this.upHandler
													.sendMessage(message);

										}

										temp = false;
									}
								} else {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg",
											nameClass.getPath());
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									if (nameClass.getTemp()) {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								}
							}
						}
					}
				}
				result = null;
				if (br != null) {
					br.close();
					br = null;
				}
				if (outStream != null) {
					outStream.close();
					outStream = null;
				}
				if (server_out != null) {
					server_out.close();
					server_out = null;
				}
				if (server_socket != null) {
					server_socket.close();
					server_socket = null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (out) {
					result = null;
					try {
						if (br != null) {
							br.close();
							br = null;
						}
						if (outStream != null) {
							outStream.close();
							outStream = null;
						}
						if (server_out != null) {
							server_out.close();
							server_out = null;
						}
						if (server_socket != null) {
							server_socket.close();
							server_socket = null;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (nameClass != null) {
						if (nameClass.getTemp()) {
							mHandler.sendEmptyMessage(99);
							if (nameClass.getPath() != null) {
								if (new File(nameClass.getPath()).exists()) {
									if (isNoImage(nameClass.getPath())) {
										new OSSSample().copyFile(getDir()
												+ "/baige/LOGOFile/1.jpg",
												nameClass.getPath());
										while (!new File(nameClass.getPath())
												.exists()) {

										}
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);
									} else {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										bundle.putInt("type",
												nameClass.getType());
										System.out.println("刷卡人类型");
										System.out.println(nameClass.getType());

										message1.setData(bundle);
										message1.what = 1;

										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								} else {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg",
											nameClass.getPath());
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									if (nameClass.getTemp()) {
										Message message1 = new Message();
										Bundle bundle = new Bundle();
										bundle.putString("name",
												nameClass.getName());
										bundle.putString("classname",
												nameClass.getClasses());
										message1.setData(bundle);
										message1.what = 1;
										MainActivity.this.handler
												.sendMessage(message1);
										Message message = new Message();
										message.what = 1;
										Bundle bundle1 = new Bundle();
										bundle1.putInt("type",
												nameClass.getType());
										bundle1.putLong("cardno",
												nameClass.getCardno());
										bundle1.putString("File",
												nameClass.getPath());
										message.setData(bundle1);
										MainActivity.this.upHandler
												.sendMessage(message);

									}
								}
							} else {
								String path = Environment
										.getExternalStorageDirectory()
										+ "/baige/picFile/"
										+ CardMd5.GetMD5Code(String
												.valueOf(System
														.currentTimeMillis()))
										+ nameClass.getStuNo() + ".jpg";
								if (new File(getDir() + "/baige/LOGOFile/1.jpg")
										.exists()) {
									new OSSSample().copyFile(getDir()
											+ "/baige/LOGOFile/1.jpg", path);
									nameClass.setPath(path);
									while (!new File(nameClass.getPath())
											.exists()) {

									}
									Message message1 = new Message();
									Bundle bundle = new Bundle();
									bundle.putString("name",
											nameClass.getName());
									bundle.putString("classname",
											nameClass.getClasses());
									message1.setData(bundle);
									message1.what = 1;
									MainActivity.this.handler
											.sendMessage(message1);

									Message message = new Message();
									message.what = 1;
									Bundle bundle1 = new Bundle();
									bundle1.putInt("type", nameClass.getType());
									bundle1.putLong("cardno",
											nameClass.getCardno());
									bundle1.putString("File",
											nameClass.getPath());
									message.setData(bundle1);
									MainActivity.this.upHandler
											.sendMessage(message);
									if (nameClass.getType() == 8) {
										dbManagercard
												.deletePre(Long
														.toString(nameClass
																.getCardno()));
									}
								}
							}

						}
					}
					out = false;
				}
			}

			// TODO Auto-generated method stub
		}

	}

	private NameClass card_client(OutputStream server_out, byte card_data[]) {
		NameClass nameClass = null;
		StringBuilder sMsg3 = new StringBuilder();
		boolean temp = true;

		try {
			byte[] bRec = null;
			bRec = new byte[card_data.length];
			for (int i = 0; i < card_data.length; i++) {
				bRec[i] = card_data[i];
			}
			sMsg3.append(MyFunc.ByteArrToHex(bRec));
			Log.e("--sMsg3--", sMsg3.toString());
			System.out.println(sMsg3.toString());
			// String frcardno_datd[] = sMsg3.toString().split("3A");
			String frcardno_datd = sMsg3.substring(sMsg3.indexOf("3A") + 2,
					sMsg3.lastIndexOf("3A"));

			/*
			 * for (int i = 0; i < frcardno_datd.length; i++) { if (i == 1) {
			 */

			System.out.println("frcardno_datd" + frcardno_datd);
			System.out.println("frcardno_datd.length()"
					+ frcardno_datd.length());
			if (frcardno_datd.length() == 31) {
				String frcardno = MainActivity.fromCardno(frcardno_datd
						.substring(12, frcardno_datd.length() - 7));
				System.out.println(frcardno);
				nameClass = server_read(frcardno);
			} else if (frcardno_datd.length() > 31) {
				String str = frcardno_datd.substring(0, 31);
				String frcardno = MainActivity.fromCardno(str.substring(12,
						str.length() - 7));
				nameClass = server_read(frcardno);
			} else {
				Message msg = new Message();
				msg.what = 77;
				Bundle b = new Bundle();
				b.putString("car", frcardno_datd);
				msg.setData(b);
				mHandler.sendEmptyMessage(77);

			}

			/*
			 * } }
			 */
			sMsg3 = new StringBuilder();
			String message = null;
			if (card_count == 0) {
				if (nameClass != null) {
					if (nameClass.getName() != null) {
						// 判断有没有自定义的刷卡时间
						if (timeIdentify == 0) {
							message = "cardno:" + nameClass.getCardno()
									+ ":contentok:" + nameClass.getClasses()
									+ nameClass.getName() + ":end";
							temp = true;
							nameClass.setTemp(temp);
						} else {
							message = "cardno:" + nameClass.getCardno()
									+ ":contentagain:end";
							temp = false;
							nameClass.setTemp(temp);
						}
					} else {
						message = "cardno:" + nameClass.getCardno()
								+ ":contentbad:end";
						temp = false;
						nameClass.setTemp(temp);
					}
				}
			} else {
				message = "cardno:" + nameClass.getCardno()
						+ ":contentagain:end";
				temp = false;
				nameClass.setTemp(temp);
			}
			if (message != null) {
				server_out.write(message.getBytes("UTF-8"));
				// System.out.println(message);
			}

			server_out.flush();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nameClass;
	}

	private NameClass server_read(String read_string) {
		long physicsno = 0;

		System.out.println(read_string + "oooooooooo" + read_string.length()
				+ "pppppp");

		NameClass nameClass = new NameClass();

		// physicsno = (16777215 - (Long.parseLong(read_string, 16) %
		// 16777216)); // 德生

		physicsno = Long.parseLong(read_string, 16);

		nameClass.setCardno(tt - physicsno);

		System.out.println("000aaa" + Long.toString(tt - physicsno));
		System.out.println("000bbb"
				+ dbManagercard.query(Long.toString(tt - physicsno)));
		if (!(Long.toString(tt - physicsno).equals(dbManagercard.query(Long
				.toString(tt - physicsno))))) {

			Log.e("MainActivity", physicsno + "");
			card_count = 0;
			Stu stu = dbManagerstu.query(nameClass.getCardno() + "");
			nameClass.setType(stu.getType());
			nameClass.setName(stu.getName());
			nameClass.setClasses(stu.getClassname());
			// System.out.println(server_class);
			if (nameClass.getName() != null) {
				if (nameClass.getType() == 8) {
					dbManagercard.insert(Long.toString(nameClass.getCardno()));

					System.out.println("server_name = " + nameClass.getName()
							+ " server_class = " + nameClass.getClasses());
				}
			} else if (nameClass.getName() == null) {
				RecOneCard recOneCard = new RecOneCard();
				stu = recOneCard.receiveDate(physicsno);
				nameClass.setType(stu.getType());
				nameClass.setName(stu.getName());
				nameClass.setClasses(stu.getClassname());
				if (nameClass.getName() != null) {
					// type=8是学生 插入数据库限制重复刷卡 如果不是学生，就不限制
					if (nameClass.getType() == 8) {
						dbManagercard.insert(Long.toString(nameClass
								.getCardno()));

						System.out.println("server_name = "
								+ nameClass.getName() + " server_class = "
								+ nameClass.getClasses());
					}
				} else if (nameClass.getName() == null) {
					nameClass.setCardno(0);
					nameClass.setName(null);
					nameClass.setClasses(null);
				}
			}

		} else {
			card_count = 1;
		}
		physicsno = 0;
		return nameClass;
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + 1;
	}

	/**
	 * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片，这是一种非常简单的方式。
	 * 
	 * @param imageFile
	 * @return
	 */
	public boolean isNoImage(String imagepath) {
		//
		Bitmap bitmap = BitmapFactory.decodeFile(imagepath);

		try {
			if (bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
				return true;
			}
			BufferedOutputStream stream;
			stream = new BufferedOutputStream(new FileOutputStream(new File(
					imagepath)));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
			return false;
		} catch (Exception e) {
			return false;
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}
			bitmap = null;
		}
	}

	public void displayFromSDCard(String uri, ImageView imageView) {
		// String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
		ImageLoader.getInstance().displayImage(uri, imageView);
	}

	public boolean fileIsExists(String file) {
		try {
			File f = new File(file);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList GetFileName() {
		ArrayList<String> vector = new ArrayList<String>();
		// Cursor c = dbReader.query("allpaths", null, null, null, null, null,
		// null);
		Cursor c = dbReader.rawQuery(
				"select alluploadpaths from allpaths limit 0,10", null);
		while (c.moveToNext()) {
			vector.add(c.getString(c.getColumnIndex("alluploadpaths")));
		}
		c.close();
		return vector;
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector GetFileName1(String fileAbsolutePath, String form) {
		Vector vecFile = new Vector();
		File file = new File(fileAbsolutePath);
		if (file.exists() && file.isDirectory()) {
			if (file.listFiles().length > 0) {
				File[] subFile = file.listFiles();
				for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
					// 判断是否为文件夹
					if (!subFile[iFileLength].isDirectory()) {
						String filename = subFile[iFileLength].getName();
						// 判断是否为MP4结尾
						if (filename.trim().toLowerCase().endsWith(form)) {
							vecFile.add(filename);
						}
					}
				}
			}
		}
		return vecFile;
	}

	// ----------------------------------------------------获取文件夹
	public File getDir() {
		// 得到SD卡根目录
		File dir = Environment.getExternalStorageDirectory();
		if (dir.exists()) {
			return dir;
		} else {
			dir.mkdirs();
			return dir;
		}
	}

	public static String fromCardno(String hexString) {
		hexString = hexString.trim();
		String s[] = hexString.split(" ");
		StringBuilder builder = new StringBuilder();
		for (int i = s.length - 1; i >= 0; i--) {
			builder.append(s[i]);
		}
		return builder.toString();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (upallcount > allcount) {
				upallcount = allcount;
			}
			if (intent.getAction().equals(action)) {
				textAAA.setText("上传数 : " + upallcount);
			}
		}
	};

	private static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private ArrayList<String> queryRecode(String val) {
		ArrayList<String> list = new ArrayList<String>();
		System.out.println(val);
		String cardq = "c";
		String type = "b";
		String time = "d";
		String[] columns = { "type", "cardno", "timecode" };
		String[] selectionArgs = { val };
		Cursor c = dbReader.query("allpaths", columns, "alluploadpaths=?",
				selectionArgs, null, null, null);
		while (c.moveToNext()) {
			cardq = c.getString(c.getColumnIndex("cardno"));
			type = c.getString(c.getColumnIndex("type"));
			time = c.getString(c.getColumnIndex("timecode"));
		}
		c.close();
		list.add(String.valueOf(cardq));
		list.add(String.valueOf(type));
		list.add(String.valueOf(time));
		return list;
	}

	private String getMac() {

		dbMac = new DBMacAddress();
		dbMac.creatDB();
		dbMac.creatDB_ID();
		MacEntity macEntity = new MacEntity();
		macEntity = dbMac.query();
		if (macEntity.getMac() != null) {
			System.out.println("mac:  " + macEntity.getMac());
			accesstoken = macEntity.getMac();
			System.out.println("从数据库获取MAC");
		}
		if (accesstoken == null) {
			GetDeviceID getDeviceID = new GetDeviceID();
			accesstoken = getDeviceID.getMacAddress();
			System.out.println("accesstoken  " + accesstoken);
			if (accesstoken != null) {
				dbMac.insert(accesstoken);
			}
		}
		return accesstoken;
	}

}

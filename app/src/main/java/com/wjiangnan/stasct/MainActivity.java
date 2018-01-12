package com.wjiangnan.stasct;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;

import ly.coursetable.demo.R;


public class MainActivity extends  AppCompatActivity {
	/**第一个无内容的格子**/
	protected TextView empty;
	/**星期的格子*/
	protected TextView monColum;
	protected TextView tueColum;
	protected TextView wedColum;
	protected TextView thrusColum;
	protected TextView friColum;
	protected TextView satColum;
	protected TextView sunColum;
	/**课程表body部分布局*/
	protected RelativeLayout course_table_layout;
	/**屏幕宽度**/
	protected int screenWidth;
	/**课程格子平均宽度**/
	protected int aveWidth;
	private DrawerLayout mDrawerLayout;
	DisplayMetrics dm = new DisplayMetrics();
	int Noweek=0;
	//数据库
	private MyDatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DateBaseHelper();
		getScreen();

		getTime();

		setToolbar();
		setDrawerLayout();
		setMenuPull();
//		setBottomNavigationBar();

		setWeekViews();
		course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);
		todayOfWeekChangeColor();

		//设置课表界面
		//动态生成12 * maxCourseNum个textview(空白课程网格）
		eggs();
		drawView();


	}

	@Override
	protected void onStart() {
		super.onStart();
		//贴出课程
		Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("第"+Noweek+"周");
		addAllCourseView(Noweek);

	}

	@Override
	protected void onRestart(){
		super.onRestart();
		course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);
		removeView();
		drawView();

	}

	//TODO 界面
	void removeView(){
		course_table_layout.removeAllViews();
	}
	void drawView(){
		String [] Time={"8:00\n1","8:50\n2","9:55\n3","10:45\n4","11:35\n5","13:30\n6","14:20\n7","15:25\n8","16:15\n9","18:30\n10","19:20\n11","20:10\n12","21:15\n13","22:05\n14"};
		int time=0;
		int height = dm.heightPixels;
		int gridHeight = height / 12;
		for (int i = 0; i < 14; i++) {
			for (int j = 1; j <= 8; j++) {

				TextView tx = new TextView(MainActivity.this);
				tx.setId((i) * 8 + j);

//				tx.setBackgroundResource(R.drawable.course_text_view_bg);
				//相对布局参数(控件本身的宽和高)
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
						aveWidth * 33 / 32 + 1,
						gridHeight);
				//文字对齐方式
				tx.setGravity(Gravity.CENTER_HORIZONTAL);
				//字体样式
//				tx.setTextAppearance(R.style.courseTableText);			//require API 23 ,current is 22（min)
				tx.setTextAppearance(this, R.style.courseTableText);
				//如果是第一列，需要设置课的序号（1 到 12）
				if (j == 1) {
					tx.setText(Time[time]+"\n"+" ");//"\n"+String.valueOf(i + 1)
					time++;
					rp.width = aveWidth * 3 / 4;
					//设置他们的相对位置
					if (i == 0)
						rp.addRule(RelativeLayout.BELOW, empty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i) * 8);
				} else {
					//与一个view(ID) 的相对位置
					rp.addRule(RelativeLayout.RIGHT_OF, (i) * 8 + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i) * 8 + j - 1);
					tx.setText("");
//					tx.setBackgroundColor(Color.alpha(255));
				}
//				tx.setBackgroundColor(Color.alpha(255));
				tx.setLayoutParams(rp);
				course_table_layout.addView(tx);
			}
		}
	}
	//此方式需要优化
	void setMenuPull(){

		NavigationView navView1=(NavigationView) findViewById(R.id.nav_view);
		navView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
			@Override
			public boolean onNavigationItemSelected(MenuItem item){
				mDrawerLayout.closeDrawers();
				Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
				removeView();
				drawView();
				switch (item.getItemId()){
					case R.id.nav_one:
						toolbar.setTitle("第"+1+"周");
						addAllCourseView(1);
						break;
					case R.id.nav_two:
						toolbar.setTitle("第"+2+"周");
						addAllCourseView(2);
						break;
					case R.id.nav_three:
						toolbar.setTitle("第"+3+"周");
						addAllCourseView(3);
						break;
					case R.id.nav_four:
						toolbar.setTitle("第"+4+"周");
						addAllCourseView(4);
						break;
					case R.id.nav_five:
						toolbar.setTitle("第"+5+"周");
						addAllCourseView(5);
						break;
					case R.id.nav_six:
						toolbar.setTitle("第"+6+"周");
						addAllCourseView(6);
						break;
					case R.id.nav_seven:
						toolbar.setTitle("第"+7+"周");
						addAllCourseView(7);
						break;
					case R.id.nav_eight:
						toolbar.setTitle("第"+8+"周");
						addAllCourseView(8);
						break;
					case R.id.nav_nine:
						toolbar.setTitle("第"+9+"周");
						addAllCourseView(9);
						break;
					case R.id.nav_ten:
						toolbar.setTitle("第"+10+"周");
						addAllCourseView(10);
						break;
					case R.id.nav_eleven:
						toolbar.setTitle("第"+11+"周");
						addAllCourseView(11);
						break;
					case R.id.nav_twelve:
						toolbar.setTitle("第"+12+"周");
						addAllCourseView(12);
						break;
					case R.id.nav_thirteen:
						toolbar.setTitle("第"+13+"周");
						addAllCourseView(13);
						break;
					case R.id.nav_fourteen:
						toolbar.setTitle("第"+14+"周");
						addAllCourseView(14);
						break;
					case R.id.nav_fifteen:
						toolbar.setTitle("第"+15+"周");
						addAllCourseView(15);
						break;
					case R.id.nav_sixteen:
						toolbar.setTitle("第"+16+"周");
						addAllCourseView(16);
						break;
					case R.id.nav_seventeen:
						toolbar.setTitle("第"+17+"周");
						addAllCourseView(17);
						break;
					case R.id.background:

				}
				return true;
			}
		});


		switch (Noweek){
			case 1:
				navView1.setCheckedItem(R.id.nav_one);break;
			case 2:
				navView1.setCheckedItem(R.id.nav_two);break;
			case 3:
				navView1.setCheckedItem(R.id.nav_one);break;
			case 4:
				navView1.setCheckedItem(R.id.nav_four);break;
			case 5:
				navView1.setCheckedItem(R.id.nav_five);	break;
			case 6:
				navView1.setCheckedItem(R.id.nav_six);break;
			case 7:
				navView1.setCheckedItem(R.id.nav_seven);break;
			case 8:
				navView1.setCheckedItem(R.id.nav_eight);break;
			case 9:
				navView1.setCheckedItem(R.id.nav_nine);break;
			case 11:
				navView1.setCheckedItem(R.id.nav_eleven);break;
			case 12:
				navView1.setCheckedItem(R.id.nav_twelve);break;
			case 13:
				navView1.setCheckedItem(R.id.nav_thirteen);break;
			case 14:
				navView1.setCheckedItem(R.id.nav_fourteen);break;
			case 15:
				navView1.setCheckedItem(R.id.nav_fifteen);break;
			case 16:
				navView1.setCheckedItem(R.id.nav_sixteen);break;
			case 17:
				navView1.setCheckedItem(R.id.nav_seventeen);break;
			default:
		}



	}
	void setBottomNavigationBar(){
		BottomNavigationBar bottomNavigationBar;
		bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

		int lastSelectedPosition = 0;
		bottomNavigationBar
				.addItem(new BottomNavigationItem(R.drawable.ic_action_add, "位置").setActiveColor(R.color.color1))
				.addItem(new BottomNavigationItem(R.drawable.logo, "发现"))
				.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "爱好").setActiveColor(R.color.color3))
				.setFirstSelectedPosition(lastSelectedPosition)
				.initialise();
	}
	void setDrawerLayout(){
		mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
		setNavigationView();
	}
	void setNavigationView(){
		NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
		View navigationHeader = navigationView.inflateHeaderView(R.layout.nav_header);

		TextView qqGroup = (TextView)navigationHeader.findViewById(R.id.QQGroup);
		qqGroup.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData mClipData = ClipData.newPlainText("Label", "600697480");
				cm.setPrimaryClip(mClipData);
				Toast.makeText(getApplicationContext(), "群号码复制成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
	void setToolbar(){
		Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("第"+Noweek+"周");//TODO
		toolbar.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				removeView();
				drawView();
				addAllCourseView(Noweek);
				Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
				toolbar.setTitle("第"+Noweek+"周");
				Toast.makeText(MainActivity.this,"返回当前周",Toast.LENGTH_SHORT).show();
			}
		});
		setSupportActionBar(toolbar);

		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		if(actionBar!=null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
		}

	}
		//菜单响应
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.toolbar, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent = new Intent(MainActivity.this, addcourse.class);
				startActivityForResult(intent, 1);
				break;
			case R.id.action_weekly:
//				Toast.makeText(this,"第"+Noweek+"周",Toast.LENGTH_SHORT).show();break;
//				Intent intentAuto = new Intent(MainActivity.this,autoImport.class);
//				startActivity(intentAuto);
				Intent intentLogin = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intentLogin);
				break;
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);break;

			default:
		}
		return true;
	}

	//TODO 初始化
	void getScreen(){
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.screenWidth = dm.widthPixels;
		this.aveWidth = this.screenWidth/8;
	}
	void setWeekViews(){
		//获得列头的控件
		empty = (TextView) this.findViewById(R.id.test_empty);
		monColum = (TextView) this.findViewById(R.id.test_monday_course);
		tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
		wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
		thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
		friColum = (TextView) this.findViewById(R.id.test_friday_course);
		satColum = (TextView) this.findViewById(R.id.test_saturday_course);
		sunColum = (TextView) this.findViewById(R.id.test_sunday_course);

		//第一个空白格子设置为25宽
		empty.setWidth(aveWidth * 3 / 4);
		monColum.setWidth(aveWidth * 33 / 32 + 1);
		tueColum.setWidth(aveWidth * 33 / 32 + 1);
		wedColum.setWidth(aveWidth * 33 / 32 + 1);
		thrusColum.setWidth(aveWidth * 33 / 32 + 1);
		friColum.setWidth(aveWidth * 33 / 32 + 1);
		satColum.setWidth(aveWidth * 33 / 32 + 1);
		sunColum.setWidth(aveWidth * 33 / 32 + 1);
	}
	void todayOfWeekChangeColor(){
		Calendar cal = Calendar.getInstance();
		int i = cal.get(Calendar.DAY_OF_WEEK);
		switch (i) {
			case 1:
				sunColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 2:
				monColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 3:
				tueColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 4:
				wedColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 5:
				thrusColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 6:
				friColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			case 7:
				satColum.setBackgroundResource(R.drawable.course_info_yellow);break;
			default:break;
		}
	}
	//获取时间差
	void getTime(){

		DateFormat df = new SimpleDateFormat("yyyyMMdd");	//设置日期格式
		try
		{
			if(Noweek==0) {
				Date d1 = df.parse("20180108");//开学日期
				Date d2 = new Date(System.currentTimeMillis());//当前日期
				long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
				long days = diff / (1000 * 60 * 60 * 24);
				if (days>0) {
					Noweek = (int) days / 7 + 1;
				}else{
					Noweek = 1;
					days = -days;
					Toast.makeText(getApplicationContext(), "距离开学还有"+days+"天", Toast.LENGTH_SHORT).show();
				}

			}
		}
		catch (Exception e)
		{

		}
	}
	void DateBaseHelper(){
		dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
		dbHelper.getWritableDatabase();
	}

	//TODO 单个课程相关
	public void newCourse(String name, String address, int week, int start, int num, int id) {

		int[] background = {R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5};
		//	int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green,
		//	R.color.color_black, R.color.color5,
		//	R.color.color4};
		//配色方案备用（弃用）
		final int xid=id;

		TextView courseInfo = new TextView(this);
		courseInfo.setText(name + "\n" + address);
		courseInfo.setTypeface(Typeface.SANS_SERIF);
		setCourseRelativeLayoutParams(courseInfo,start,num,week);
		//字体剧中
		courseInfo.setGravity(Gravity.CENTER);
		// 设置一种背景
		courseInfo.setTextSize(12);
		courseInfo.setTextColor(Color.argb(255,67,67,67));
		Random rand = new Random();
		int i = rand.nextInt(5);
		courseInfo.setBackgroundResource(background[i]);
		//设置不透明度
		courseInfo.getBackground().setAlpha(222);
		course_table_layout.addView(courseInfo);

		courseInfo.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this, getcourse.class);
				intent.putExtra("id",xid+"");
				startActivity(intent);
			}
		});
	}
	void addAllCourseView(int NoWeek){
		int NoEach=NoWeek%2;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 查询Book表中所有的数据
		Cursor cursor = db.query("Book", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				// 遍历Cursor对象，取出数据并打印
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String address = cursor.getString(cursor.getColumnIndex("address"));
				int week = cursor.getInt(cursor.getColumnIndex("week"));
				int num = cursor.getInt(cursor.getColumnIndex("num"));
				int start=cursor.getInt(cursor.getColumnIndex("start"));
				int id=cursor.getInt(cursor.getColumnIndex("id"));
				int first=cursor.getInt(cursor.getColumnIndex("first"));
				int last=cursor.getInt(cursor.getColumnIndex("last"));
				int each=cursor.getInt(cursor.getColumnIndex("each"));
//				Log.d(TAG,""+name+address+each);
				if(NoWeek>=first&&NoWeek<=last&&(NoEach==each||each==2)) {
					newCourse(name, address, week, start, num, id);
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
	}
	void setCourseRelativeLayoutParams(TextView courseInfo,int start, int num,int week){
		int gridHeight = dm.heightPixels/12;

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth, (gridHeight) * num);//上课节数
		rlp.topMargin = 5 + (start - 1) * gridHeight;//第（几+1）节课开始上
		rlp.addRule(RelativeLayout.RIGHT_OF, week);//周几的课
		rlp.leftMargin = 1;
		courseInfo.setLayoutParams(rlp);
	}
	void eggs(){
		empty.setOnClickListener(new View.OnClickListener() {
			int emmm = 1;
			@Override
			public void onClick(View view) {
				switch (emmm){
					case 1:Toast.makeText(getApplicationContext(), "如有bug或者改进的意见\n欢迎加入交流群（点击群号自动复制到粘贴板上）", Toast.LENGTH_LONG).show();emmm+=1;break;
					case 2:Toast.makeText(getApplicationContext(), "特别感谢:\n希希,泽大爷,项目组\n以及内测的小伙伴们", Toast.LENGTH_SHORT).show();emmm+=1;break;
					case 3:Toast.makeText(getApplicationContext(), "感谢希希的提供的核心代码", Toast.LENGTH_LONG).show();emmm+=1;break;
					case 4:Toast.makeText(getApplicationContext(), "Coder : happts", Toast.LENGTH_SHORT).show();emmm+=1;break;
					default:Toast.makeText(getApplicationContext(), "作者的QQ:763405108", Toast.LENGTH_SHORT).show();emmm=1;break;
				}
			}
		});


		if(Noweek>=4){
			Toast.makeText(getApplicationContext(), "小学期已过，请在Q群下载最新版本", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(getApplicationContext(), "2018小学期临时版本", Toast.LENGTH_SHORT).show();
		}
	}
}
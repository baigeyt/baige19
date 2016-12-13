package com.tony.autoscroll;
//Download by http://www.codefans.net
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;


/**
 * link: blog.csdn.net/t12x3456
 * @author Tony
 *
 */
public class AutoScrollView extends ScrollView {
	private final Handler handler      = new Handler();
	private long          duration     = 1000;
	private boolean       isScrolled   = false;
	private int           currentIndex = 0;
	private long          period       = 1000;
	private int           currentY     = -1;
	private double			  x;
	private double 		  y;
	private int type = -1;
	/**
	 * @param context
	 */
	public AutoScrollView(Context context) {
		this(context, null);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public AutoScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AutoScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	    
	/**
	 * 判断当前是否为滚动状态
	 * 
	 * @return the isScrolled
	 */
	public boolean isScrolled() {
		return isScrolled;
	}
	
	/**
	 * 开启或者关闭自动滚动功能
	 * 
	 * @param isScrolled
	 *            true为开启，false为关闭
	 */
	public void setScrolled(boolean isScrolled) {
		this.isScrolled = isScrolled;
		autoScroll();
	}
	
	/**
	 * 获取当前滚动到结尾时的停顿时间，单位：毫秒
	 * 
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}
	
	/**
	 * 设置当前滚动到结尾时的停顿时间，单位：毫秒
	 * 
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}
	
	/**
	 * 获取当前的滚动速度，单位：毫秒，值越小，速度越快。
	 * 
	 * @return the speed
	 */
	public long getSpeed() {
		return duration;
	}
	
	/**
	 * 设置当前的滚动速度，单位：毫秒，值越小，速度越快。
	 * 
	 * @param speed
	 *            the duration to set
	 */
	public void setSpeed(long speed) {
		this.duration = speed;
	}
	public void setType(int type){
		this.type = type;
	}
	private void autoScroll() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				boolean flag = isScrolled;
				if (flag) {
//					Log.d("test", "currentY = " + currentY + "  getScrollY() = "+ getScrollY()  );
					if (currentY == getScrollY()) {
						try {
							Thread.sleep(period);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						currentIndex = 0;
						scrollTo(0, 0);
						handler.postDelayed(this, period);
					} else {
						currentY = getScrollY();
						handler.postDelayed(this, duration);
						currentIndex++;
						scrollTo(0, currentIndex *10);
					}
				} else {
//					currentIndex = 0;
//					scrollTo(0, 0);
				}
			}
		}, duration);
	}
}

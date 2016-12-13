package com.tony.autoscroll;
//Download by http://www.codefans.net
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
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
	    
	public boolean isScrolled() {
		return isScrolled;
	}
	
	
	public void setScrolled(boolean isScrolled) {
		this.isScrolled = isScrolled;
		autoScroll();
	}
	
	/**
	 *
	 * 
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}
	
	/**
	 * 
	 * 
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}
	
	/**
	 *
	 * 
	 * @return the speed
	 */
	public long getSpeed() {
		return duration;
	}
	
	/**
	 * 
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

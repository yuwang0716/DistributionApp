package com.liuhesan.app.distributionapp.widget;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.liuhesan.app.distributionapp.R;

/**
 * 短信倒计时控件<br>
 * 1.可以在代码和布局中设置属性 <br>
 * 2.修复了倒计时时间错乱问题，提供了默认的设置 <br>
 * 3.提供了倒计时完成和进行时的监听<br>
 * 4.控件的样式在布局或者代码中如同TextView的用法设置<br>
 *
 */
public class MSGCountTimeView extends TextView {
	// handler的Message
	private static final int COUNTTIME = 1;

	// 提供默认的设置
	private static final String INITTEXT = "获取验证码";
	private static final String PREFIXRUNTEXT = "剩余时间";
	private static final String SUFFIXRUNTEXT = "秒";
	private static final String FINISHTEXT = "点击重新获取";
	private static final int TOTALTIME = 60 * 1000;
	private static final int ONETIME = 1000;
	private static final int COLOR = Color.RED;

	// 来自布局文件中的属性设置
	private String mInittext;// 初始化文本
	private String mPrefixRuntext;// 运行时的文本前缀
	private String mSuffixRuntext;// 运行时的文本后缀
	private String mFinishtext;// 完成倒计时后的文本显示
	private int mTotaltime;// 倒计时的总时间
	private int mOnetime;// 一次时间
	private int mColor;

	// 实际使用的总时间
	private int Totaltime;

	// 判断是否在倒计时中，防止多次点击
	private boolean isRun;
	// 是否允许倒计时
	private boolean isAllowRun;

	// 处理倒计时的方法
	private Timer mTimer;
	private TimerTask mTimerTask;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case COUNTTIME:
					// 对秒数进行格式化
					DecimalFormat df = new DecimalFormat("#00");
					String strTotaltime = df.format(Totaltime / 1000);
					String runtimeText = mPrefixRuntext + strTotaltime + mSuffixRuntext;

					// 对秒数进行颜色设置
					Spannable spannable = new SpannableString(runtimeText);
					ForegroundColorSpan redSpan = new ForegroundColorSpan(mColor);
					spannable.setSpan(redSpan, mPrefixRuntext.length(), mPrefixRuntext.length() + strTotaltime.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					MSGCountTimeView.this.setText(spannable);
					Totaltime -= mOnetime;
					mDownTime.onDown();
					if (Totaltime < 0) {
						MSGCountTimeView.this.setText(mFinishtext);
						isRun = false;
						clearTimer();
						mDownTime.onFinish();
					}
					break;

				default:
					break;
			}
		}
	};

	/**
	 * 倒计时的监听
	 */
	private onDownTime mDownTime = new onDownTime() {

		@Override
		public void onFinish() {

		}

		@Override
		public void onDown() {

		}
	};

	public MSGCountTimeView(Context context) {
		this(context, null);
	}

	public MSGCountTimeView(Context context, AttributeSet attrs) {
		// 如果不写android.R.attr.textViewStyle会丢失很多属性
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public MSGCountTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 1. 在布局文件中提供设置
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MSGCountTimeView);
		mInittext = ta.getString(R.styleable.MSGCountTimeView_inittext);
		mPrefixRuntext = ta.getString(R.styleable.MSGCountTimeView_prefixruntext);
		mSuffixRuntext = ta.getString(R.styleable.MSGCountTimeView_suffixruntext);
		mFinishtext = ta.getString(R.styleable.MSGCountTimeView_finishtext);
		mTotaltime = ta.getInteger(R.styleable.MSGCountTimeView_totaltime, TOTALTIME);
		mOnetime = ta.getInteger(R.styleable.MSGCountTimeView_onetime, ONETIME);
		mColor = ta.getColor(R.styleable.MSGCountTimeView_timecolor, COLOR);
		ta.recycle();
		// 2.代码设置值
		// 3.如果布局和代码都没有设置，则给予默认值
		initData();
		initTimer();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 如果为空，则设置默认的值

		if (TextUtils.isEmpty(mInittext)) {
			mInittext = INITTEXT;
		}
		if (TextUtils.isEmpty(mPrefixRuntext)) {
			mPrefixRuntext = PREFIXRUNTEXT;
		}
		if (TextUtils.isEmpty(mSuffixRuntext)) {
			mSuffixRuntext = SUFFIXRUNTEXT;
		}
		if (TextUtils.isEmpty(mFinishtext)) {
			mFinishtext = FINISHTEXT;
		}
		if (mTotaltime < 0) {
			mTotaltime = TOTALTIME;
		}
		if (mOnetime < 0) {
			mOnetime = ONETIME;
		}
		MSGCountTimeView.this.setText(mInittext);
	}

	/**
	 * 初始化时间
	 */
	private void initTimer() {
		Totaltime = mTotaltime;
		mTimer = new Timer();
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				mHandler.sendEmptyMessage(COUNTTIME);
			}
		};
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!isAllowRun) {
			} else {
				if (!isRun) {
					// 每次开始倒计时时初始化
					initTimer();
					// 倒计时任务启动
					mTimer.schedule(mTimerTask, 0, mOnetime);
					isRun = true;
				}
			}
		}
		;
		return true;
	}

	/**
	 * 清除时间
	 */
	private void clearTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	/**
	 * 设置初始化的文字
	 *
	 * @param mInittext
	 * @return
	 */
	public MSGCountTimeView setInittext(String mInittext) {
		this.mInittext = mInittext;
		MSGCountTimeView.this.setText(mInittext);
		return this;
	}

	/**
	 * 设置运行时的文字前缀
	 *
	 * @param mRuntext
	 * @return
	 */
	public MSGCountTimeView setPrefixRuntext(String mPrefixRuntext) {
		this.mPrefixRuntext = mPrefixRuntext;
		return this;
	}

	/**
	 * 设置运行时的文字后缀
	 *
	 * @param mRuntext
	 * @return
	 */
	public MSGCountTimeView setSuffixRuntext(String mSuffixRuntext) {
		this.mSuffixRuntext = mSuffixRuntext;
		return this;
	}

	/**
	 * 设置结束的文字
	 *
	 * @param mFinishtext
	 * @return
	 */
	public MSGCountTimeView setFinishtext(String mFinishtext) {
		this.mFinishtext = mFinishtext;
		return this;
	}

	/**
	 * 设置倒计时的总时间
	 *
	 * @param mTotaltime
	 * @return
	 */
	public MSGCountTimeView setTotaltime(int mTotaltime) {
		this.mTotaltime = mTotaltime;
		return this;
	}

	/**
	 * 设置一次倒计时的时间
	 *
	 * @param mOnetime
	 * @return
	 */
	public MSGCountTimeView setOnetime(int mOnetime) {
		this.mOnetime = mOnetime;
		return this;
	}

	/**
	 * 设置默认倒计时秒数的颜色
	 *
	 * @param mColor
	 * @return
	 */
	public MSGCountTimeView setTimeColor(int mColor) {
		this.mColor = mColor;
		return this;
	}

	/**
	 * 对外提供接口，编写倒计时时和倒计时完成时的操作
	 *
	 *
	 */
	public interface onDownTime {
		void onDown();

		void onFinish();
	}

	public void onDownTime(onDownTime mDownTime) {
		this.mDownTime = mDownTime;
	}

	/**
	 * 窗口销毁时，倒计时停止
	 */
	@Override
	protected void onDetachedFromWindow() {

		super.onDetachedFromWindow();
		clearTimer();
	}

	/**
	 * 是否允许倒计时
	 */
	public void isAllowRun(Boolean isAllowRun) {
		this.isAllowRun = isAllowRun;
	}
}
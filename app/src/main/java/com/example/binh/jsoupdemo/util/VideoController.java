package com.example.binh.jsoupdemo.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;
import com.example.binh.jsoupdemo.R;

/**
 * Created by binh on 5/26/16.
 */
public class VideoController extends FrameLayout {
    public static final String TAG = "VideoController";


    private MediaPlayController mPlayer;
    private final Context mContext;
    private View mAnchor;
    private View mRoot;
    private WindowManager mWindowManager;
    private Window mWindow;
    private View mDecor;
    private WindowManager.LayoutParams mDecorLayoutParams;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private final boolean mUseFastForward;
    private boolean mFromXml;
    private boolean mListenersSet;
    private View.OnClickListener mNextListener, mPrevListener;

    private ImageButton mPauseButton;
    private ImageButton mNextButton;
    private ImageButton mFullScreenButton;

    private AccessibilityManager mAccessibilityManager;



    public VideoController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
        mAccessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    public VideoController(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
//        initFloatingWindowLayout();
//        initFloatingWindow();
        mAccessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    public VideoController(Context context) {
        this(context, true);
    }

    @Override
    protected void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    private  void initFloatingWindow() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        mWindow = new PhoneWindow();
        mWindow.setWindowManager(mWindowManager, null, null);

        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();

    }

    private void initFloatingWindowLayout() {
        mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = Gravity.TOP | Gravity.LEFT;
        p.height = LayoutParams.WRAP_CONTENT;
        p.x = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        p.token = null;
        p.windowAnimations = 0;

    }

    private void updateFloatingWindowLayout() {
        int [] anchorPos = new int[2];
        mAnchor.getLocationOnScreen(anchorPos);
        // we need to know the size of the controller so we can properly position it
        // within its space
        mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), MeasureSpec.AT_MOST));
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.width = mAnchor.getWidth();
        p.x = anchorPos[0] + (mAnchor.getWidth() - p.width) / 2;
        p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
    }

    public void setAnchorView(View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null)
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    private final OnLayoutChangeListener mLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//            updateFloatingWindowLayout();
            if (mShowing) {
                mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
            }
        }
    };

    protected View makeControllerView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflater.inflate(R.layout.media_controller, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        Resources res = mContext.getResources();

        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);

        }

        mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seekBar = (SeekBar) mProgress;
                seekBar.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(100);
        }

        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mEndTime = (TextView) v.findViewById(R.id.time);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser)
                return;
            long duration = mPlayer.getDuration();
            long newPosition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newPosition);
            if (mCurrentTime != null) {
                mCurrentTime.setText(stringForTime((int) newPosition));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            show(36000);
            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };


    public void setMediaPlayer(MediaPlayController player) {
        mPlayer = player;
        updatePausePlay();
    }

    private int setProgress() {
        if (mPlayer == null || mDragging ) {
           return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (mPlayer.getDuration() > 0) {
               long  pos = 1000L * position / mPlayer.getDuration();
               mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null) {
           mEndTime.setText(stringForTime(duration));
        }

        if (mCurrentTime != null) {
            mCurrentTime.setText(stringForTime(position));
        }
        return position;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0);
                break;
            case MotionEvent.ACTION_UP:
                show(sDefaultTimeout);
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return  true;
    }

    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
        }

        updatePausePlay();
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hide() {
        if (mAnchor != null)
            return;
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                Log.e(TAG, "already removed");
            }
            mShowing = false;

        }
    }

    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }

            if (mProgress != null && !mPlayer.canSeekBackward() && !mPlayer.canSeekForward()) {
                mProgress.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {

        }
    }

    private  final Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            int pos;

            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private final View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };


    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;
        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    private void updateFullScreen() {
        if (mRoot == null || mFullScreenButton == null)
            return;
        if (mPlayer.isFullScreen()) {
            mFullScreenButton.setImageResource(R.drawable.ic_media_fullscreen_shrink);
        } else {
            mFullScreenButton.setImageResource(R.drawable.ic_media_fullscreen_stretch);
        }

    }


    public interface MediaPlayController {
        void start();
        void pause();
        int getDuration();
        int getCurrentPosition();
        void seekTo(int pos);
        boolean isPlaying();
        int getBufferPercentage();
        boolean canPause();
        boolean canSeekBackward();
        boolean canSeekForward();
        boolean isFullScreen();

        int getAudioSessionId();
    }

}

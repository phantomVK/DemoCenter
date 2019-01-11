package com.phantomvk.democenter.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.util.Pools.SynchronizedPool;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Helper class for inflating layouts asynchronously. To use, construct
 * an instance of {@link AsyncLayoutInflater} on the UI thread and call
 * {@link #inflate(int, ViewGroup, OnInflateFinishedListener)}. The
 * {@link OnInflateFinishedListener} will be invoked on the UI thread
 * when the inflate request has completed.
 *
 * <p>This is intended for parts of the UI that are created lazily or in
 * response to user interactions. This allows the UI thread to continue
 * to be responsive & animate while the relatively heavy inflate
 * is being performed.
 *
 * <p>For a layout to be inflated asynchronously it needs to have a parent
 * whose {@link ViewGroup#generateLayoutParams(AttributeSet)} is thread-safe
 * and all the Views being constructed as part of inflation must not create
 * any {@link Handler}s or otherwise call {@link Looper#myLooper()}. If the
 * layout that is trying to be inflated cannot be constructed
 * asynchronously for whatever reason, {@link AsyncLayoutInflater} will
 * automatically fall back to inflating on the UI thread.
 *
 * <p>NOTE that the inflated View hierarchy is NOT added to the parent. It is
 * equivalent to calling {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
 * with attachToRoot set to false. Callers will likely want to call
 * {@link ViewGroup#addView(View)} in the {@link OnInflateFinishedListener}
 * callback at a minimum.
 *
 * <p>This inflater does not support setting a {@link LayoutInflater.Factory}
 * nor {@link LayoutInflater.Factory2}. Similarly it does not support inflating
 * layouts that contain fragments.
 */
public class AsyncLayoutInflater {

    private static final String TAG = "AsyncLayoutInflater";

    private LayoutInflater mInflater;
    private Handler mHandler;

    private static final int CORE_POOL_SIZE = 2;
    private static ThreadPoolExecutor sExecutor;

    private static SynchronizedPool<InflateRequest> sRequestPool = new SynchronizedPool<>(10);

    static {
        final int processors = Runtime.getRuntime().availableProcessors();
        final int threads = Math.max(CORE_POOL_SIZE, processors - 2);
        sExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        sExecutor.allowCoreThreadTimeOut(true);
    }

    public AsyncLayoutInflater(@NonNull Context context) {
        mInflater = new BasicInflater(context);
        mHandler = new Handler(mHandlerCallback);
    }

    @UiThread
    public void inflate(@LayoutRes int resid, @Nullable ViewGroup parent,
                        @NonNull OnInflateFinishedListener callback) {
        InflateRequest request = obtainRequest();
        request.inflater = this;
        request.resid = resid;
        request.parent = parent;
        request.callback = callback;
        sExecutor.execute(new InflateRunnable(request));
    }

    private Callback mHandlerCallback = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            InflateRequest request = (InflateRequest) msg.obj;
            if (request.view == null) {
                request.view = mInflater.inflate(
                        request.resid, request.parent, false);
            }
            request.callback.onInflateFinished(request.view, request.resid, request.parent);
            releaseRequest(request);
            return true;
        }
    };

    public interface OnInflateFinishedListener {
        void onInflateFinished(@NonNull View view, @LayoutRes int resid,
                               @Nullable ViewGroup parent);
    }

    private static class InflateRequest {
        AsyncLayoutInflater inflater;
        ViewGroup parent;
        int resid;
        View view;
        OnInflateFinishedListener callback;

        InflateRequest() {
        }
    }

    private static class InflateRunnable implements Runnable {
        private InflateRequest request;

        private InflateRunnable(InflateRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                request.view = request.inflater.mInflater.inflate(
                        request.resid, request.parent, false);
            } catch (RuntimeException ex) {
                // Probably a Looper failure, retry on the UI thread
                Log.w(TAG, "Failed to inflate resource in the background! Retrying on the UI"
                        + " thread", ex);
            }
            Message.obtain(request.inflater.mHandler, 0, request).sendToTarget();
        }
    }

    private static void setLayoutParam(Context context, ViewGroup parent, int resId, View view) {
        final XmlResourceParser parser = context.getResources().getLayout(resId);
        try {
            final AttributeSet attrs = Xml.asAttributeSet(parser);
            ViewGroup.LayoutParams params = parent.generateLayoutParams(attrs);
            view.setLayoutParams(params);
        } catch (Exception e) {
            Log.e(TAG, "Failed to set layout param to view in the background.", e);
        } finally {
            parser.close();
        }
    }

    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {
                "android.widget.",
                "android.webkit.",
                "android.app."
        };

        BasicInflater(Context context) {
            super(context);
            if (context instanceof AppCompatActivity) {
                AppCompatDelegate delegate = ((AppCompatActivity) context).getDelegate();
                if (delegate instanceof LayoutInflater.Factory2) {
                    LayoutInflaterCompat.setFactory2(this, (LayoutInflater.Factory2) delegate);
                }
            }
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack at it.
                }
            }

            return super.onCreateView(name, attrs);
        }
    }

    private static InflateRequest obtainRequest() {
        InflateRequest obj = sRequestPool.acquire();
        if (obj == null) {
            obj = new InflateRequest();
        }
        return obj;
    }

    private static void releaseRequest(InflateRequest obj) {
        obj.callback = null;
        obj.inflater = null;
        obj.parent = null;
        obj.resid = 0;
        obj.view = null;
        sRequestPool.release(obj);
    }
}

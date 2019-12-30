package com.tuacy.workmanagerdev.periodic;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tuacy.workmanagerdev.R;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class PeriodicWorkerActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, PeriodicWorkerActivity.class));
	}

	private Button mButtonStart1;
	private TextView mTextOut;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periodic_worker);
		initView();
		initEvent();
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WorkManager.getInstance().cancelAllWork();
	}

	private void initView() {
		mButtonStart1 = findViewById(R.id.button_periodic_1);
		mTextOut = findViewById(R.id.text_out);
	}

	private void initEvent() {
		mButtonStart1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startPeriodicWorkerByPeriodicWorkRequest();
			}
		});
	}

	private void initData() {

	}

	/**
	 * 通过PeriodicWorkRequest实现周期任务
	 */
	private void startPeriodicWorkerByPeriodicWorkRequest() {
		// 定义一个PeriodicWorkRequest，并且关联PeriodicWorker。任务15m循环(源码里面已经规定了最小时间间隔15分钟)
		PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(PeriodicWorker.class, 15, TimeUnit.MINUTES).build();
		// 任务入队，WorkManager调度执行
		WorkManager.getInstance().getStatusById(request.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});
		WorkManager.getInstance().enqueue(request);
	}

}

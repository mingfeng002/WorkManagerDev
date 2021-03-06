package com.tuacy.workmanagerdev.then;

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

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class OrderWorkerActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, OrderWorkerActivity.class));
	}

	private Button mButtonStart;
	private TextView mTextOut;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_worker);
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
		mButtonStart = findViewById(R.id.button_start);
		mTextOut = findViewById(R.id.text_out);
	}

	private void initEvent() {
		mButtonStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startWorker();
			}
		});
	}

	private void initData() {

	}

	/**
	 * A,B,C三个任务顺序执行
	 */
	private void startWorker() {
		// A
		OneTimeWorkRequest requestA = new OneTimeWorkRequest.Builder(OrderWorkerA.class).build();
		// B
		OneTimeWorkRequest requestB = new OneTimeWorkRequest.Builder(OrderWorkerB.class).build();
		// C
		OneTimeWorkRequest requestC = new OneTimeWorkRequest.Builder(OrderWorkerC.class).build();
		// 任务入队，WorkManager调度执行
		WorkManager.getInstance().getStatusById(requestA.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("requestA 任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("requestA 任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("requestA 任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});
		WorkManager.getInstance().getStatusById(requestB.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("requestB 任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("requestB 任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("requestB 任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});
		WorkManager.getInstance().getStatusById(requestC.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("requestC 任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("requestC 任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("requestC 任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});
		WorkManager.getInstance().beginWith(requestA).then(requestB).then(requestC).enqueue();
	}


}

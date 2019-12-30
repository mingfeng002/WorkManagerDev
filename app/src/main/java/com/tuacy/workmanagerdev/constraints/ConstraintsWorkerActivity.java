package com.tuacy.workmanagerdev.constraints;

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

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class ConstraintsWorkerActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, ConstraintsWorkerActivity.class));
	}

	private Button mButtonStart;
	private TextView mTextOut;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constraints_worker);
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
	 * 启动约束任务
	 */
	private void startWorker() {
		// 设置只有在wifi状态下才能执行
		Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build();
		// 设置约束条件
		OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ConstraintsWorker.class).setConstraints(constraints).build();
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
		// 任务入队，WorkManager调度执行
		WorkManager.getInstance().enqueue(request);
	}

}

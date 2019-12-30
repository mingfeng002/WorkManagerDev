package com.tuacy.workmanagerdev.combine;

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
import com.tuacy.workmanagerdev.then.OrderWorkerA;
import com.tuacy.workmanagerdev.then.OrderWorkerB;
import com.tuacy.workmanagerdev.then.OrderWorkerC;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.State;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class CombineWorkerActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, CombineWorkerActivity.class));
	}

	private Button mButtonStart;
	private TextView mTextOut;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conbine_worker_layout);
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
	}

	private void initEvent() {
		mTextOut = findViewById(R.id.text_out);
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
	 * 组合任务
	 */
	private void startWorker() {
		OneTimeWorkRequest requestA = new OneTimeWorkRequest.Builder(ConbineWorkerA.class).build();
		OneTimeWorkRequest requestB = new OneTimeWorkRequest.Builder(ConbineWorkerB.class).build();
		OneTimeWorkRequest requestC = new OneTimeWorkRequest.Builder(ConbineWorkerC.class).build();
		OneTimeWorkRequest requestD = new OneTimeWorkRequest.Builder(ConbineWorkerD.class).build();
		OneTimeWorkRequest requestE = new OneTimeWorkRequest.Builder(ConbineWorkerE.class).build();

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
		WorkManager.getInstance().getStatusById(requestD.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("requestD 任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("requestD 任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("requestD 任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});
		WorkManager.getInstance().getStatusById(requestE.getId()).observe(this, new Observer<WorkStatus>() {
			@Override
			public void onChanged(@Nullable WorkStatus workStatus) {
				if (workStatus == null) {
					return;
				}
				if (workStatus.getState() == State.ENQUEUED) {
					mTextOut.setText("requestE 任务入队");
				}
				if (workStatus.getState() == State.RUNNING) {
					mTextOut.setText("requestE 任务正在执行");
				}
				if (workStatus.getState().isFinished()) {
					Data data = workStatus.getOutputData();
					mTextOut.setText("requestE 任务完成" + "-结果：" + data.getString("key_name", "null"));
				}
			}
		});

		//A,B任务链
		WorkContinuation continuationAB = WorkManager.getInstance().beginWith(requestA).then(requestB);
		//C,D任务链
		WorkContinuation continuationCD = WorkManager.getInstance().beginWith(requestC).then(requestD);
		//合并上面两个任务链，在接入requestE任务，入队执行
		WorkContinuation.combine(continuationAB, continuationCD).then(requestE).enqueue();
	}


}

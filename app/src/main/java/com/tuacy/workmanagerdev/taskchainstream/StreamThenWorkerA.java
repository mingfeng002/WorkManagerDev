package com.tuacy.workmanagerdev.taskchainstream;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;

/**
 * A任务输出10
 */
public class StreamThenWorkerA extends Worker {

	@NonNull
	@Override
	public Result doWork() {
		try {
			//模拟耗时任务
			Thread.sleep(1000);
			Log.d("tuacy", "StreamThenWorkerA work");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Data data = new Data.Builder().putInt("a_out", 10).build();
		setOutputData(data);
		return Result.SUCCESS;
	}
}

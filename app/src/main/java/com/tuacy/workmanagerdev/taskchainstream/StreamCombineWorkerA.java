package com.tuacy.workmanagerdev.taskchainstream;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;

/**
 * A任务的输出中只有一个key： a_key -> 100
 */
public class StreamCombineWorkerA extends Worker {

	@NonNull
	@Override
	public Result doWork() {
		try {
			//模拟耗时任务
			Thread.sleep(1000);
			Log.d("tuacy", Thread.currentThread().getName()+"StreamCombineWorkerA work");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Data data = new Data.Builder().putInt("a_key", 100).build();
		setOutputData(data);
		return Result.SUCCESS;
	}
}

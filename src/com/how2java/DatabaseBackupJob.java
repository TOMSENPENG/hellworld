package com.how2java;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DatabaseBackupJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail detail=context.getJobDetail();
		String database=detail.getJobDataMap().getString("database");
		System.out.printf("给数据库%s备份，耗时10秒 %n",detail,database);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}

package com.how2java;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

public class StoppableJob implements InterruptableJob{
	private boolean stop=false;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		while(true){
			if(stop)
			  break;
			try {
					System.out.println("每隔1秒，进行一次检查，看看是否停止");
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();		
			}
			System.out.println("持续工作中。。。。。");
		}
		
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.out.println("被调度叫停");
		stop=true;
	}
	
}

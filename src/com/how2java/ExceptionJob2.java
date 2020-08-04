package com.how2java;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExceptionJob2 implements Job{
	static int i=0;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try {
			System.out.println(100/i);
		} catch (Exception e) {
			System.out.println("发生了异常，修改一下参数，立刻重新执行");
			i=1;
			JobExecutionException je=new JobExecutionException(e);
			je.setRefireImmediately(true);
			e.printStackTrace();
		}
	}
	
}

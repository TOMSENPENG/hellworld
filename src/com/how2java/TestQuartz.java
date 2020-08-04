package com.how2java;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.cronSchedule;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

public class TestQuartz {
	public static void main(String[] args) throws Exception{
            //jobDataMap();
            //stop();
		    jobDataMap();
		    //cromtrigger();
		    //jobListener();
	}
	private static void jobListener() throws Exception {
		 //创建调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        //定义一个触发器
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义名称和所属的租
            .startNow()
            .withSchedule(simpleSchedule()
                .withIntervalInSeconds(2) //每隔2秒执行一次
                .withRepeatCount(10)) //总共执行11次(第一次执行不基数)
            .build();

        //定义一个JobDetail
        JobDetail mailJob = newJob(MailJob.class) //指定干活的类MailJob
            .withIdentity("mailjob1", "mailgroup") //定义任务名称和分组
            .usingJobData("email", "admin@10086.com") //定义属性
            .build();

        //增加Job监听
        MailJobListener mailJobListener= new MailJobListener();
        KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(mailJob.getKey());
        scheduler.getListenerManager().addJobListener(mailJobListener, keyMatcher);
         System.out.println(mailJobListener.getName());
        //调度加入这个job
        scheduler.scheduleJob(mailJob, trigger);

        //启动
        scheduler.start();
         
        //等待20秒，让前面的任务都执行完了之后，再关闭调度器
        Thread.sleep(20000);
        scheduler.shutdown(true);
		
	}
	private static void cromtrigger() throws SchedulerException, InterruptedException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		   
        Date startTime = DateBuilder.nextGivenSecondDate(null, 8);

        JobDetail job = newJob(MailJob.class).withIdentity("mailJob", "mailGroup").build();

        CronTrigger trigger = newTrigger()
        		.withIdentity("trigger1", "group1")
        		.startAt(startTime)
        		.withSchedule(cronSchedule("0/2 * * * * ?"))
                .build();

        // schedule it to run!
        Date ft = scheduler.scheduleJob(job, trigger);
         
        System.out.println("使用的Cron表达式是："+trigger.getCronExpression());
//        System.out.printf("%s 这个任务会在 %s 准时开始运行，累计运行%d次，间隔时间是%d毫秒%n", job.getKey(), ft.toLocaleString(), trigger.getRepeatCount()+1, trigger.getRepeatInterval());
         
        scheduler.start();
           
        //等待200秒，让前面的任务都执行完了之后，再关闭调度器
        Thread.sleep(200000);
        scheduler.shutdown(true);
	}
	private static void stop() throws Exception {
		// TODO Auto-generated method stub
		Scheduler scheduler=StdSchedulerFactory.getDefaultScheduler();
		Trigger trigger=newTrigger()
				.withIdentity("trigger1","group1")
				.startNow()
				.build();
		JobDetail detail=newJob(StoppableJob.class)
				.withIdentity("exceptionjob1","somejobGroup1")
				.build();
		scheduler.scheduleJob(detail,trigger);
		scheduler.start();
		Thread.sleep(5000);
		System.out.println("过5秒，调度停止job");
		scheduler.interrupt(detail.getKey());

		Thread.sleep(20000);
		scheduler.shutdown();
		
	}
	private static void jobDataMap() throws Exception{
		 Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		   
         Date startTime = DateBuilder.nextGivenSecondDate(null, 8);
          
         JobDetail job = newJob(MailJob.class).withIdentity("mailJob", "mailGroup").build();

         SimpleTrigger trigger = (SimpleTrigger) newTrigger()
        		 .withIdentity("trigger1", "group1")
        		 .startAt(startTime)
        		 .withSchedule(simpleSchedule()
        				 .repeatForever()
        				 .withIntervalInSeconds(1))
        		 .build();

         // schedule it to run!
         Date ft = scheduler.scheduleJob(job, trigger);
           
         System.out.println("当前时间是：" + new Date().toLocaleString());
         System.out.printf("%s 这个任务会在 %s 准时开始运行，累计运行%d次，间隔时间是%d毫秒%n", job.getKey(), ft.toLocaleString(), trigger.getRepeatCount()+1, trigger.getRepeatInterval());
          
         scheduler.start();
            
         //等待200秒，让前面的任务都执行完了之后，再关闭调度器
         Thread.sleep(200000);
         scheduler.shutdown(true);
	}
}

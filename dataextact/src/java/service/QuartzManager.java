package service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.logging.Logger;

/**
 * Created by dylan on 2017/7/29.
 */
public class QuartzManager {
    static Scheduler sched = null;

    static {
        try {
            sched = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";
    private static Logger logger = Logger.getLogger(QuartzManager.class.getName());

    /**
     * 添加一个任务
     *
     * @param jobName
     * @param time
     */
    public static void addJob(String jobName, String time) {
        try {
            removeJob(jobName);
            JobDetail jobDetail = JobBuilder.newJob(ExtractJob.class)
                    .withIdentity(jobName, JOB_GROUP_NAME).build();// 任务名，任务组，任务执行类
            // 调度器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder
                    .cronSchedule(time); // 触发器时间设定

            //触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,
                    TRIGGER_GROUP_NAME).withSchedule(cronScheduleBuilder)

                    .build();

            QuartzManager.sched.scheduleJob(jobDetail, trigger);

            // 启动
            if (!sched.isShutdown()) {
                sched.start();
                logger.info("jobName:" + jobName + "已经启动");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param jobName
     */
    public static void removeJob(String jobName) {
        try {
            logger.info("任务停止jobName：" + jobName);

            sched.pauseTrigger(TriggerKey.triggerKey(jobName,
                    TRIGGER_GROUP_NAME));// 停止触发器
            sched.unscheduleJob(TriggerKey.triggerKey(jobName, JOB_GROUP_NAME));// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));// 删除任务

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("任务停止：" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

package service;

import dao.DeviceDao;
import dao.DeviceDaoImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import thread.SaleRecordThread;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dylan on 2017/7/29.
 */
public class ExtractJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        DeviceDao deviceDao = new DeviceDaoImpl();
        Map<String, Integer> serverList = deviceDao.getServerList();
        for (String key : serverList.keySet()) {
            pool.execute(new SaleRecordThread(key, serverList.get(key)));
        }
    }




}

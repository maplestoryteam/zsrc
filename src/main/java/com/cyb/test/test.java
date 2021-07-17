package com.cyb.test;

import com.cyb.MonitorInfoBean;
import com.cyb.dao.IMonitorService;
import com.cyb.dao.MonitorServiceImpl;

public class test {
    public static void main(String[] args) throws Exception {  
        IMonitorService service = new MonitorServiceImpl();  
        MonitorInfoBean monitorInfo = service.getMonitorInfoBean();  
        System.out.println("cpu占有率=" + monitorInfo.getCpuRatio());  
          
        System.out.println("可使用内存=" + monitorInfo.getTotalMemory());  
        System.out.println("剩余内存=" + monitorInfo.getFreeMemory());  
        System.out.println("最大可使用内存=" + monitorInfo.getMaxMemory());  
          
        System.out.println("操作系统=" + monitorInfo.getOsName());  
        System.out.println("总的物理内存=" + monitorInfo.getTotalMemorySize() + "kb");  
        System.out.println("剩余的物理内存=" + monitorInfo.getFreeMemory() + "kb");  
        System.out.println("已使用的物理内存=" + monitorInfo.getUsedMemory() + "kb");  
        System.out.println("线程总数=" + monitorInfo.getTotalThread() + "kb");  
    }  
}

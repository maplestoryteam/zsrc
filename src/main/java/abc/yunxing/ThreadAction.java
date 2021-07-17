package abc.yunxing;

public class ThreadAction {// 操作查看JVM虚拟机中所的线程和线程组的类
    // 显示线程信息

    private static void threadMessage(Thread thread, String index) {
        if (thread == null) {
            return;
        }
        System.out.println(index + "ThreadName- " + thread.getName()
                + "  Priority- " + thread.getPriority()
                + (thread.isDaemon() ? " Daemon" : "")
                + (thread.isAlive() ? "" : " Inactive"));
    }
    // 显示线程组信息

    private static void threadGroupMessage(ThreadGroup group, String index) {
        if (group == null) {
            return; // 判断线程组
        }
        int count = group.activeCount(); // 获得活动的线程数
        // 获得活动的线程组数
        int countGroup = group.activeGroupCount();
        // 根据活动的线程数创建指定个数的线程数组
        Thread[] threads = new Thread[count];
        // 根据活动的线程组数创建指定个数的线程组数组
        ThreadGroup[] groups = new ThreadGroup[countGroup];
        group.enumerate(threads, false); // 把所有活动子组的引用复制到指定数组中，false表示不包括对子组的所有活动子组的引用
        group.enumerate(groups, false);
        System.out.println(index + "ThreadGroupName-" + group.getName()
                + "MaxPriority- " + group.getMaxPriority()
                + (group.isDaemon() ? " Daemon" : ""));
        // 循环显示当前活动的线程信息
        for (int i = 0; i < count; i++) {
            threadMessage(threads[i], index + "    ");
        }
        for (int i = 0; i < countGroup; i++) // 循环显示当前活动的线程组信息
        {
            threadGroupMessage(groups[i], index + "    ");// 递归调用方法
        }
    }

    public static void threadsList() { // 找到根线程组并列出它递归的信息
        ThreadGroup currentThreadGroup; // 当前线程组
        ThreadGroup rootThreadGroup; // 根线程组
        ThreadGroup parent;
        // 获得当前活动的线程组
        currentThreadGroup = Thread.currentThread().getThreadGroup();
        rootThreadGroup = currentThreadGroup; // 获得根线程组
        parent = rootThreadGroup.getParent(); // 获得根线程
        while (parent != null) { // 循环对根线程组重新赋值
            rootThreadGroup = parent;
            parent = parent.getParent();
        }
        threadGroupMessage(rootThreadGroup, ""); // 显示根线程组
    }

    public static void main(String[] args) { // java程序主入口处
        System.out.println("查看JVM中所有的线程的活动状况如下：");
        ThreadAction.threadsList(); // 调用方法显示所有线程的信息
    }
}

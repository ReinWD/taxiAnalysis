package moe.reinwd.taxi.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author ReinWD 张巍 2016214874
 * @e-mail ReinWDD@gmail.com
 */
public class ConcurrentUtil {
    private static List<ExecutorService> registeredExecutorList = new LinkedList<>();
    private static List<StopListener> stopListenerList = new LinkedList<>();

    public static ExecutorService getExecutor(String name, ExecutorType type){
        return getExecutor(name,type,null);
    }

    public static ExecutorService getExecutor(String name, ExecutorType type, StopListener stoplistener){
        ExecutorService executorService = null;
        if(type == null) return null;
        switch (type){
            case SINGLE_THREAD:
                executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, name));
                break;
            case CACHED:
                executorService = Executors.newCachedThreadPool(r -> new Thread(r, name));
                break;
            case SCHEDULED:
                executorService = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, name));
                break;
        }
        if (executorService!= null)
            registeredExecutorList.add(executorService);
        if (stoplistener!=null)stopListenerList.add(stoplistener);
        return executorService;
    }

    public static void registerExecutor(ExecutorService executor){
        if (executor != null) registeredExecutorList.add(executor);
    }

    public static void stop(){
        if (!registeredExecutorList.isEmpty()) registeredExecutorList.forEach(ExecutorService::shutdown);
        if (!stopListenerList.isEmpty()) stopListenerList.forEach(StopListener::onStop);

    }

    public static void unRegister(ScheduledExecutorService executor) {
        registeredExecutorList.remove(executor);
    }

    public enum ExecutorType {
        SINGLE_THREAD, CACHED, SCHEDULED
    }

    private interface StopListener {
        void onStop();
    }
}

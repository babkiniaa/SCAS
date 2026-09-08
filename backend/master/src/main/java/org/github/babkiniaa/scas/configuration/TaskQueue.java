package org.github.babkiniaa.scas.configuration;

import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TaskQueue {

    private final BlockingQueue<AnalyserDto> queue = new LinkedBlockingQueue<>();

    private final ConcurrentMap<Long, String> status = new ConcurrentHashMap<>();

    public void addTask(AnalyserDto task) throws InterruptedException {
        queue.put(task);
        status.put(task.getIdProject(), "TODO");
    }

    public String getTaskStatus(long taskId) {
        return status.getOrDefault(taskId, "NotFound");
    }

    public AnalyserDto takeTask() throws InterruptedException {
        AnalyserDto task = queue.take();
        status.remove(task.getIdProject());
        return task;
    }

    public void banTask(long id){
        status.put(id, "Ban");
        queue.removeIf(task -> task.getIdProject() == id);
    }

    public int count(){
        return queue.size();
    }
}

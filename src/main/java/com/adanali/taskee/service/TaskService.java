package com.adanali.taskee.service;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    void updateTask(Task task);

    void updateTaskStatus(Long taskId, Long userId, TaskStatus status);

    void delete(Long taskId, Long userId);

    List<Task> getAll(Long userId);

    Task getById(Long taskId, Long userId);

}

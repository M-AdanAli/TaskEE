package com.adanali.taskee.service;

import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.dto.Page;

public interface TaskService {

    Task createTask(Task task);

    void updateTask(Task task);

    void updateTaskStatus(Long taskId, Long userId, TaskStatus status);

    void delete(Long taskId, Long userId);

    Task getById(Long taskId, Long userId);

    Page<Task> getAll(Long userId, int page, int size);

    Page<Task> getTasksByStatus(Long userId, TaskStatus status, int page, int size);

    long getTaskCountByStatus(Long userId, TaskStatus status);

    long countGlobalAllTasks();

    long countGlobalCompletedTasks();
}

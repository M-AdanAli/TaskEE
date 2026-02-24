package com.adanali.taskee.service;

import com.adanali.taskee.dao.TaskDAO;
import com.adanali.taskee.dao.TaskDaoJDBCImpl;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.TaskNotFoundException;

import java.sql.SQLException;
import java.util.List;

public class TaskServiceImpl implements TaskService{

    private final TaskDAO taskDAO;

    TaskServiceImpl(){
        taskDAO = new TaskDaoJDBCImpl();
    }

    TaskServiceImpl(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }

    @Override
    public Task createTask(Task task) {
        try{
            return taskDAO.save(task);
        }catch (SQLException e){
            throw new ServiceException("Unable to save the Task due to Database Error.");
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            Task existing = taskDAO.findById(task.getId())
                    .orElseThrow(TaskNotFoundException::new);

            if (!existing.getUserId().equals(task.getUserId())) {
                throw new AuthorizationException("Access Denied: You do not own this task.");
            }

            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setTaskStatus(task.getTaskStatus());

            taskDAO.update(existing);
        }catch (SQLException e){
            throw new ServiceException("Failed to update Task due to System error.");
        }
    }

    @Override
    public void updateTaskStatus(Long taskId, Long userId, TaskStatus status) {
        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                throw new AuthorizationException("Access Denied: You do not own this task.");
            }

            task.setTaskStatus(status);
            taskDAO.update(task);
        }catch (SQLException e){
            throw new ServiceException("Failed to update Task Status due to System error.");
        }
    }

    @Override
    public void delete(Long taskId, Long userId) {
        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                throw new AuthorizationException("Access Denied: You do not own this task.");
            }

            taskDAO.deleteById(taskId);
        }catch (SQLException e){
            throw new ServiceException("Failed to delete the Task due to Database Error.", e);
        }
    }

    @Override
    public List<Task> getAll(Long userId) {
        try {
            return taskDAO.findAllByUserId(userId);
        } catch (SQLException e) {
            throw new ServiceException("Failed to retrieve Tasks.", e);
        }
    }

    @Override
    public Task getById(Long taskId, Long userId) {
        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                throw new AuthorizationException("Access Denied: You do not own this task.");
            }

            return task;
        } catch (SQLException e) {
            throw new ServiceException("Failed to retrieve the Task.", e);
        }
    }
}

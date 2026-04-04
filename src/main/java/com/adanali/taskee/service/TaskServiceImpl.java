package com.adanali.taskee.service;

import com.adanali.taskee.dao.TaskDAO;
import com.adanali.taskee.dao.TaskDaoJDBCImpl;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.exception.AuthorizationException;
import com.adanali.taskee.exception.ServiceException;
import com.adanali.taskee.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TaskServiceImpl implements TaskService{

    private final TaskDAO taskDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(){
        taskDAO = new TaskDaoJDBCImpl();
    }

    TaskServiceImpl(TaskDAO taskDAO){
        this.taskDAO = taskDAO;
    }

    @Override
    public Task createTask(Task task) {
        LOGGER.info("Attempting to CREATE Task for User#{} ...",task.getUserId());

        try{
            Task savedTask = taskDAO.save(task);
            LOGGER.info("Successfully CREATED Task#{} for Uer#{} !",savedTask.getUserId(),savedTask.getUserId());
            return savedTask;
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While CREATING Task for User#%s: %s".formatted(task.getUserId(), e.getMessage()), e);
        }
    }

    @Override
    public void updateTask(Task task) {
        LOGGER.info("Attempting to UPDATE Task#{} for User#{} ...",task.getId(),task.getUserId());

        try {
            Task existing = taskDAO.findById(task.getId())
                    .orElseThrow(TaskNotFoundException::new);

            if (!existing.getUserId().equals(task.getUserId())) {
                LOGGER.warn("SECURITY: User#{} tried to UPDATE Task#{} belonging to User#{}",task.getUserId(), task.getId(),existing.getUserId());
                throw new AuthorizationException();
            }

            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            existing.setTaskStatus(task.getTaskStatus());

            taskDAO.update(existing);
            LOGGER.info("Successfully UPDATED Task#{} for User#{} !",task.getId(),task.getUserId());
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While UPDATING Task#%s for User#%s : %s".formatted(task.getId(), task.getUserId(), e.getMessage()), e);
        }
    }

    @Override
    public void updateTaskStatus(Long taskId, Long userId, TaskStatus status) {
        LOGGER.info("Attempting to UPDATE Status of Task#{} for User#{} ...",taskId,userId);

        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                LOGGER.warn("SECURITY: User#{} tried to UPDATE Status of Task#{} belonging to User#{}",userId , taskId,task.getUserId());
                throw new AuthorizationException();
            }

            task.setTaskStatus(status);
            taskDAO.update(task);
            LOGGER.info("Successfully UPDATED Status of Task#{} for User#{} !",taskId,userId);
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While UPDATING Status of Task#%s for User#%s: %s".formatted(taskId, userId, e.getMessage()), e);
        }
    }

    @Override
    public void delete(Long taskId, Long userId) {
        LOGGER.info("Attempting to DELETE Task#{} for User#{} ...",taskId,userId);

        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                LOGGER.warn("SECURITY: User#{} tried to DELETE Task#{} belonging to User#{}",userId , taskId,task.getUserId());
                throw new AuthorizationException();
            }

            taskDAO.deleteById(taskId);
            LOGGER.info("Successfully DELETED Task#{} for User#{} !",taskId, userId);
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While DELETING Task#%s for User#%s: %s".formatted(taskId, userId, e.getMessage()), e);
        }
    }

    @Override
    public List<Task> getAll(Long userId) {
        LOGGER.info("Attempting to FETCH All tasks for User#{} ...",userId);

        try {
            List<Task> tasks = taskDAO.findAllByUserId(userId);
            LOGGER.info("Successfully FETCHED All Tasks for User#{} !",userId);
            return tasks;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING All Tasks for User#%s: %s".formatted(userId, e.getMessage()), e);
        }
    }

    @Override
    public Task getById(Long taskId, Long userId) {
        LOGGER.info("Attempting to FETCH Task#{} for User#{} ...",taskId,userId);

        try {
            Task task = taskDAO.findById(taskId)
                    .orElseThrow(TaskNotFoundException::new);

            if (!task.getUserId().equals(userId)) {
                LOGGER.warn("SECURITY: User#{} tried to FETCH Task#{} belonging to User#{}",userId , taskId,task.getUserId());
                throw new AuthorizationException();
            }

            LOGGER.info("Successfully FETCHED Task#{} for User#{} !", taskId, userId);
            return task;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING Task#%s for User#%s: %s".formatted(taskId,userId, e.getMessage()), e);
        }
    }

    @Override
    public List<Task> getTasksByStatus(Long userId, TaskStatus status) {
        LOGGER.info("Attempting to FETCH '{}' Tasks for User#{} ...",status.name(),userId);

        try {
            List<Task> tasks = taskDAO.findAllByUserIdAndStatus(userId, status);
            LOGGER.info("Successfully FETCHED '{}' Tasks for User#{} !",status.name(),userId);
            return tasks;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING '%s' Tasks for User#%s: %s".formatted(status.name(),userId, e.getMessage()), e);
        }
    }

    @Override
    public long countGlobalAllTasks() {
        LOGGER.info("Attempting to FETCH Count of All Global Tasks...");

        try {
            long tasksCount = taskDAO.countAllGlobalTasks();
            LOGGER.info("Successfully FETCHED Count of All Global Tasks!");
            return tasksCount;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING Global Task Count: %s".formatted(e.getMessage()), e);
        }
    }

    @Override
    public long countGlobalCompletedTasks() {
        LOGGER.info("Attempting to FETCH Count of 'COMPLETED' Global Tasks...");

        try {
            long tasksCount = taskDAO.countCompletedGlobalTasks();
            LOGGER.info("Successfully FETCHED Count of 'COMPLETED' Global Tasks!");
            return tasksCount;
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING Global 'COMPLETED' Task Count: %s".formatted(e.getMessage()), e);
        }
    }
}

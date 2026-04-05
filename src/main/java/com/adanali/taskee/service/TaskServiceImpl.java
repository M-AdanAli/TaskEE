package com.adanali.taskee.service;

import com.adanali.taskee.dao.TaskDAO;
import com.adanali.taskee.dao.TaskDaoJDBCImpl;
import com.adanali.taskee.domain.Task;
import com.adanali.taskee.domain.enums.TaskStatus;
import com.adanali.taskee.dto.Page;
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
    public Page<Task> getAll(Long userId, int page, int size) {
        LOGGER.info("Attempting to FETCH All tasks for User#{} ...",userId);

        try {
            long totalTasks = taskDAO.countByUserId(userId);
            long totalPages = (int) Math.ceil((double) totalTasks / size);
            if (totalPages == 0) totalPages = 1;
            if (page < 1 || page > totalPages) page = 1;

            int offset = (page - 1) * size;
            List<Task> tasks = taskDAO.findAllByUserId(userId, size, offset);
            LOGGER.info("Successfully FETCHED All Tasks for User#{} !",userId);

            return new Page<>(tasks, page, totalPages, totalTasks );
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
    public Page<Task> getTasksByStatus(Long userId, TaskStatus status, int page, int size) {
        LOGGER.info("Attempting to FETCH '{}' Tasks for User#{} ...",status.name(),userId);

        try {
            long totalTasks = taskDAO.countByUserIdAndStatus(userId,status);
            long totalPages = (int) Math.ceil((double) totalTasks / size);
            if (totalPages == 0) totalPages = 1;
            if (page < 1 || page > totalPages) page = 1;

            int offset = (page - 1) * size;
            List<Task> tasks = taskDAO.findAllByUserIdAndStatus(userId, status, size, offset);
            LOGGER.info("Successfully FETCHED '{}' Tasks for User#{} !",status.name(),userId);

            return new Page<>(tasks, page, totalPages, totalTasks);
        } catch (SQLException e) {
            throw new ServiceException("DATABASE ERROR: While FETCHING '%s' Tasks for User#%s: %s".formatted(status.name(),userId, e.getMessage()), e);
        }
    }

    @Override
    public long getTaskCountByStatus(Long userId, TaskStatus status){
        LOGGER.info("Attempting to FETCH '{}' Tasks Count for User#{} ...",status.name(),userId);

        try {
            return taskDAO.countByUserIdAndStatus(userId, status);
        }catch (SQLException e){
            throw new ServiceException("DATABASE ERROR: While FETCHING '%s' Tasks Count for User#%s: %s".formatted(status.name(),userId, e.getMessage()), e);
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

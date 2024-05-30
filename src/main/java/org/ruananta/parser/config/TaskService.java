package org.ruananta.parser.config;

import org.ruananta.parser.entity.Task;
import org.ruananta.parser.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task addTask(Task task) {
        if(taskRepository.existsById(task.getId())) {
            throw new IllegalArgumentException("Task already exists!");
        }
        return taskRepository.save(task);
    }
}

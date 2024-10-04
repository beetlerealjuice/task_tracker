package com.example.task_tracker.dto;

import com.example.task_tracker.entity.TaskStatus;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;
@Data
public class TaskDto {

    private String id;
    private String name;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;

    private UserDto author;
    private UserDto assignee;
    private List<UserDto> observers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Set<String> getObserverIds() {
        return observerIds;
    }

    public void setObserverIds(Set<String> observerIds) {
        this.observerIds = observerIds;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public UserDto getAssignee() {
        return assignee;
    }

    public void setAssignee(UserDto assignee) {
        this.assignee = assignee;
    }

    public List<UserDto> getObservers() {
        return observers;
    }

    public void setObservers(List<UserDto> observers) {
        this.observers = observers;
    }
}

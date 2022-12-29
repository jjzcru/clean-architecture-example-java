package dev.fennex.clean.domain.model;

import java.util.Date;

public class Todo {
    public String id;
    public String userId;
    public Boolean complete = false;
    public String content;
    public Date createdAt;
    public Date completedAt;
}

package com.project.volunteerOrganization.repository;

import com.project.volunteerOrganization.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepository extends JpaRepository<Task,Long>
{
    Task findByFilename(String filename);
}

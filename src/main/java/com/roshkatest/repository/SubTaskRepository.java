package com.roshkatest.repository;

import com.roshkatest.entity.SubTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    Page<SubTask> findByTaskId(Long taskId, Pageable pageable);
}

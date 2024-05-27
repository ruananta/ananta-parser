package org.ruananta.parser.repository;

import org.ruananta.parser.engine.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Task.Link, Long> {
    
    public Task.Link findLinkById(Long linkId);
}

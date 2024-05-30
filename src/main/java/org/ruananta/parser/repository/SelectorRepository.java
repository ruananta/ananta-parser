package org.ruananta.parser.repository;

import org.ruananta.parser.engine.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectorRepository extends JpaRepository<Task.Selector, Long> {

}

package com.skitrainer.repository;

import com.skitrainer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN e.trainers t WHERE t.id = :userId " +
            "UNION " +
            "SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId")
    List<Event> findByUserInvolved(String userId);
}

package com.alihassan.ssimp.repository;

import com.alihassan.ssimp.model.Ticket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    Optional<Ticket> findByTicketIdAndUser_UserId(
        Long ticketId, Long userId
    );
}
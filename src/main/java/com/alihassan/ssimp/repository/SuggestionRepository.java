package com.alihassan.ssimp.repository;

import com.alihassan.ssimp.model.Suggestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository
        extends JpaRepository<Suggestion, Long> {

    List<Suggestion> findAllByOrderByStepNumberAsc();
}
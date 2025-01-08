package com.example.SearchEngine.repository;

import com.example.SearchEngine.entity.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends JpaRepository<Index,Long> {
}

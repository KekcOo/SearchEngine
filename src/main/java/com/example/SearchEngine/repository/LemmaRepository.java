package com.example.SearchEngine.repository;

import com.example.SearchEngine.entity.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Long> {
}

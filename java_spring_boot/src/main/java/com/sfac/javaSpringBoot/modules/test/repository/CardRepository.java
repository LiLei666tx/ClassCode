package com.sfac.javaSpringBoot.modules.test.repository;

import com.sfac.javaSpringBoot.modules.test.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 2020/8/12
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
}

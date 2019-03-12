package com.teamtreehouse.repository;

import com.teamtreehouse.model.Step;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends PagingAndSortingRepository<Step, Long>
{
}

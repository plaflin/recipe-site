package com.teamtreehouse.repository;

import com.teamtreehouse.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@RepositoryRestResource(exported = false)
public interface UserRepository extends PagingAndSortingRepository<User, Long>
{
    User findByUsernameIgnoreCase(String username);

    List<User> findAll();

    List<User> findByFavoritesId(Long id);
}

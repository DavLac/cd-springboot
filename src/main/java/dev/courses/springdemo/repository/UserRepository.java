package dev.courses.springdemo.repository;

import dev.courses.springdemo.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, RevisionRepository<User, Long, Integer> {

}

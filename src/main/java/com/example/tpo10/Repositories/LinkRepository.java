package com.example.tpo10.Repositories;

import com.example.tpo10.Models.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends CrudRepository<Link, String> {
}

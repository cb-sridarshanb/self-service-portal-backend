package com.example.selfservice.portal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortalRepository extends JpaRepository<PortalModel, Integer> {
    public Optional<PortalModel> findByEmail(String email);
    public void deleteByEmail(String email);
}

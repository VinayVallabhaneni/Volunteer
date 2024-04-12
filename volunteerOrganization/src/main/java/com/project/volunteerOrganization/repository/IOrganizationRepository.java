package com.project.volunteerOrganization.repository;

import com.project.volunteerOrganization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrganizationRepository extends JpaRepository<Organization,Long>
{
    Organization findByEmailAndPassword(String email,String password);
}

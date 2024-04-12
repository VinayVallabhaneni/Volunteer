package com.project.volunteerOrganization.repository;

import com.project.volunteerOrganization.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVolunteerRepository extends JpaRepository<Volunteer,Long>
{
    Volunteer findByEmailAndPassword(String email,String password);
}

package com.project.volunteerOrganization.repository;

import com.project.volunteerOrganization.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatRepository extends JpaRepository<Chat,Long>
{
    @Query(value="select * from chat where sender = ?1 or sender = ?2 and receiver = ?2 or receiver = ?1 ",nativeQuery = true)
    List<Chat> getChatBySenderAndReceiver(Long sender,Long receiver);
}

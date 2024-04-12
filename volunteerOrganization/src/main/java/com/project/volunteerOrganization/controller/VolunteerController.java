package com.project.volunteerOrganization.controller;


import com.project.volunteerOrganization.model.Volunteer;
import com.project.volunteerOrganization.repository.IVolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("volunteer")
@CrossOrigin("*")
public class VolunteerController
{
    @Autowired
    private IVolunteerRepository repository;

    @PostMapping
    private ResponseEntity<?> addVolunteer(@RequestBody Volunteer volunteer)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            repository.save(volunteer);
            res.put("success",true);
            res.put("msg","Volunteer Added Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to add the volunteer");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping
    private ResponseEntity<?> getAllVolunteers(){
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            List<Volunteer> volunteers = repository.findAll();
            res.put("success",true);
            res.put("volunteers",volunteers);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available volunteers");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getVolunteerById(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            Volunteer volunteer = repository.findById(id).get();
            res.put("success",true);
            res.put("volunteer",volunteer);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available volunteers by id"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @PutMapping("update/{id}")
    private ResponseEntity<?> updateVolunteer(@PathVariable Long id,@RequestBody Volunteer volunteer)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            Volunteer volunteer1 = repository.findById(id).get();

            volunteer1.setName(volunteer.getName());
            volunteer1.setEmail(volunteer.getEmail());
            volunteer1.setPassword(volunteer.getPassword());
            volunteer1.setPhone(volunteer.getPhone());
            volunteer1 .setAddress(volunteer.getAddress());

            repository.save(volunteer1);
            res.put("success",true);
            res.put("msg","volunteer updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","failed to update the volunteer");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteVolunteer(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>() ;
        try{
            repository.deleteById(id);
            res.put("success" ,true) ;
            res .put("msg","volunteer deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to delete the volunteer by provided id is"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
}

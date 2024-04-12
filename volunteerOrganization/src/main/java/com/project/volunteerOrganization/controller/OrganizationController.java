package com.project.volunteerOrganization.controller;

import com.project.volunteerOrganization.model.Organization;
import com.project.volunteerOrganization.model.Volunteer;
import com.project.volunteerOrganization.repository.IOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/organization")
@CrossOrigin("*")
public class OrganizationController
{
    @Autowired
    private IOrganizationRepository repository;

    @PostMapping
    private ResponseEntity<?> addOrganization(@RequestBody Organization organization)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            repository.save(organization);
            res.put("success",true);
            res.put("msg","Organization Added Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to add the organization");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping
    private ResponseEntity<?> getAllOrganizations(){
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            List<Organization> organizations = repository.findAll();
            res.put("success",true);
            res.put("volunteers",organizations);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available organizations");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getOrganizationById(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            Organization organization = repository.findById(id).get();
            res.put("success",true);
            res.put("volunteer",organization);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available organizations by id"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @PutMapping("update/{id}")
    private ResponseEntity<?> updateOrganization(@PathVariable Long id,@RequestBody Organization organization)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            Organization organization1 = repository.findById(id).get();

            organization1.setName(organization.getName());
            organization1.setEmail(organization.getEmail());
            organization1.setPassword(organization.getPassword());
            organization1.setPhone(organization.getPhone());
            organization1 .setAddress(organization.getAddress());

            repository.save(organization1);
            res.put("success",true);
            res.put("msg","organization updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","failed to update the Organization");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteOrganization(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>() ;
        try{
            repository.deleteById(id);
            res.put("success" ,true) ;
            res .put("msg","Organization deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to delete the organization by provided id is"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }
}

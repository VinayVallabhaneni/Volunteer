package com.project.volunteerOrganization.controller;

import com.project.volunteerOrganization.model.Organization;
import com.project.volunteerOrganization.model.Task;
import com.project.volunteerOrganization.model.Volunteer;
import com.project.volunteerOrganization.repository.IOrganizationRepository;
import com.project.volunteerOrganization.repository.ITaskRepository;
import com.project.volunteerOrganization.repository.IVolunteerRepository;
import com.project.volunteerOrganization.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
@CrossOrigin("*")
public class TaskController
{
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IOrganizationRepository organizationRepository;

    @Autowired
    private IVolunteerRepository volunteerRepository;

    @PostMapping("/{organizationId}/assign/{volunteerId}")
    public ResponseEntity<?> addTask(
            @PathVariable Long organizationId,
            @PathVariable Long volunteerId,
            @RequestParam String taskName,
            @RequestParam String description,
            @RequestParam LocalDateTime createdAt,
            @RequestParam MultipartFile file
    ) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            Organization organization = organizationRepository.findById(organizationId).orElse(null);
            Volunteer volunteer = volunteerRepository.findById(volunteerId).orElse(null);

            if (organization == null || volunteer == null) {
                res.put("success", false);
                res.put("msg", "Organization or volunteer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }

            if (file.isEmpty()) {
                res.put("success", false);
                res.put("msg", "File is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            // Validate other parameters if needed

            // Create Task
            Task task = Task.builder()
                    .taskName(taskName)
                    .description(description)
                    .filename(UUID.randomUUID().toString())
                    .file(FileUtils.compressImage(file.getBytes()))
                    .createdAt(createdAt)
                    .build();

            // Assign task to organization and volunteer
            organization.getTasks().add(task);
            volunteer.getTasks().add(task);

            // Save task, organization, and volunteer
            taskRepository.save(task);
            organizationRepository.save(organization);
            volunteerRepository.save(volunteer);

            res.put("success", true);
            res.put("msg", "Task successfully assigned to the volunteer");
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            res.put("success", false);
            res.put("msg", "Failed to assign the task to the volunteer");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }


    @GetMapping("/download/{filename}")
    private ResponseEntity<?> downloadFile(String filename)
    {
        byte[] fileBytes = taskRepository.findByFilename(filename).getFile();
        return ResponseEntity.ok()
                .contentType(determineContentType(filename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(fileBytes);
    }

    private MediaType determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "txt": return MediaType.TEXT_PLAIN;
            case "pdf": return MediaType.APPLICATION_PDF;
            case "jpg": case "jpeg": return MediaType.IMAGE_JPEG;
            case "png": return MediaType.IMAGE_PNG;
            case "xlsx": return MediaType.APPLICATION_XML;
            default: return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping
    private ResponseEntity<?> getAllTasks(){
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            List<Task> tasks = taskRepository.findAll();
            res.put("success",true);
            res.put("volunteers",tasks);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available task");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getOrganizationById(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>();
        try
        {
            Task task = taskRepository.findById(id).get();
            res.put("success",true);
            res.put("volunteer",task);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to fetch the available task by id"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> deleteTask(@PathVariable Long id)
    {
        HashMap<String,Object> res = new HashMap<>() ;
        try{
            taskRepository.deleteById(id);
            res.put("success" ,true) ;
            res .put("msg","Task deleted Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        catch (Exception e)
        {
            res.put("success",false);
            res.put("msg","Failed to delete the task by provided id is"+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

}

package com.example.selfservice.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class PortalController {
    @Autowired
    private PortalService portalService;

    @GetMapping("/portal")
    public List<PortalModel> initialRequest(){
        return portalService.getAllRecords();
    }

    @PostMapping("/portal/login")
    public ResponseEntity<Optional<PortalModel>> login(@RequestBody PortalModel portalModel){
        Optional<PortalModel> userDetails = portalService.getUserDetails(portalModel);
        if(userDetails.isPresent()){
            System.out.println("Inside if");
            return new ResponseEntity<>(userDetails,HttpStatus.OK);
        }
        else{
            System.out.println("Inside else");
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/portal/signup")
    public ResponseEntity userSignUp(@RequestBody PortalModel portalModel){
        PortalModel user = portalService.saveUserDetails(portalModel);
        if(user != null){
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(portalModel.getID()).toUri();
//            return ResponseEntity.created(location).build();
            return ResponseEntity.ok().body(portalModel);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details provided");
        }
    }

    @PutMapping("/portal/edit-details")
    public ResponseEntity editUserDetails(@RequestBody PortalModel portalModel){
        PortalModel user = portalService.editUserDetails(portalModel);
        if(user != null){
            return ResponseEntity.ok().body("Successfully edited");
        }
        else{
            return ResponseEntity.badRequest().body("Details cannot be edited");
        }
    }

    @DeleteMapping("/portal/remove-user/{email}")
    public ResponseEntity removeUser(@PathVariable String email){
        System.out.println(email);
        PortalModel portalModel = portalService.deleteUserDetails(email);
        if(portalModel != null){
            return ResponseEntity.ok().body("Deleted user");
        }
        else{
            return ResponseEntity.badRequest().body("Incorrect email provided");
        }
    }
}

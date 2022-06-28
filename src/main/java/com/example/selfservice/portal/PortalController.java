package com.example.selfservice.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "*")
@RestController
public class PortalController {
    @Autowired
    private PortalService portalService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtility jwtUtility;

//    This should be changed to ROLES - ADMIN. A similar request should be made in place for a POST/PUT request for ADMIN
    @GetMapping("/portal")
    public List<PortalModel> initialRequest(Principal principal){
//        System.out.println(principal.getName() + " " + pri);
        return portalService.getAllRecords();
    }

    @GetMapping("/portal/details/{id}")
    public ResponseEntity<Optional<PortalModel>> fetchUserDetails(@PathVariable Integer id){
        Optional<PortalModel> userInfo = portalService.getUserInfo(id);
        if(userInfo.isPresent()){
            return new ResponseEntity<>(userInfo,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/portal/login")
    public JwtTokenResponse login(@RequestBody PortalModel portalModel) throws Exception{
        System.out.println("Inside controller body");
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            portalModel.getEmail(),
                            portalModel.getPassword(),
                            new ArrayList<>()
                    )
            );
        }
        catch(BadCredentialsException ex){
            throw new Exception("Invalid credentials",ex);
        }
        System.out.println("Reached this point");
        Optional<PortalModel> userDetails = portalService.getUserDetails(portalModel);
        System.out.println(userDetails.get());
        if(userDetails.isPresent()) {
            String token = jwtUtility.generateToken(userDetails.get());
            return new JwtTokenResponse(userDetails.get(),token);
        }
        else{
            throw new Exception("Invalid credentials. Cannot login");
        }
    }

    @PostMapping("/portal/signup")
    public ResponseEntity userSignUp(@RequestBody PortalModel portalModel){
        PortalModel user = portalService.saveUserDetails(portalModel);
        if(user != null){
            return ResponseEntity.ok().body(portalModel);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid details provided");
        }
    }

    @PutMapping("/portal/edit-details")
    public ResponseEntity editUserDetails(@RequestBody PortalModel portalModel){
//        Boolean canAccess = portalService.verifyAccess(principal.getName(), portalModel.getID());
        if(portalModel.getID() == jwtUtility.getID()){
            PortalModel user = portalService.editUserDetails(portalModel);
            if(user != null){
                return ResponseEntity.ok().body(user);
            }
            else{
                return ResponseEntity.badRequest().body("Details cannot be edited");
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/portal/remove-user/{email}")
    public ResponseEntity removeUser(@PathVariable String email){
        PortalModel portalModel = portalService.deleteUserDetails(email);
        if(portalModel != null){
            return ResponseEntity.ok().body("Deleted user");
        }
        else{
            return ResponseEntity.badRequest().body("Incorrect email provided");
        }
    }
}

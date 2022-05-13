package com.example.selfservice.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortalService {
    @Autowired
    private PortalRepository portalRepository;

    public List<PortalModel> getAllRecords(){
        return portalRepository.findAll();
    }

    public Optional<PortalModel> getUserDetails(PortalModel portalModel){
        Optional<PortalModel> user = portalRepository.findByEmail(portalModel.getEmail());
        System.out.println(user.get().getEmail() + " " + portalModel.getEmail() + " " + portalModel.getEmail());
        if(user.isPresent() && user.get().getEmail().equals(portalModel.getEmail()) &&
        user.get().getPassword().equals(portalModel.getPassword())){
            System.out.println("Inside if");
            return user;
        }
        return null;
    }

    public PortalModel saveUserDetails(PortalModel portalModel){
        if(portalModel.getEmail().isEmpty()
        || portalModel.getPassword().isEmpty()
        || portalModel.getFirstName().isEmpty()
        || portalModel.getLastName().isEmpty()){
            return null;
        }
        Optional<PortalModel> user = portalRepository.findByEmail(portalModel.getEmail());
        if(!user.isPresent()){
            portalRepository.save(portalModel);
            return portalModel;
        }
        return null;
    }

    public PortalModel editUserDetails(PortalModel portalModel){
        System.out.println("ID is: " + portalModel.getID());
        Optional<PortalModel> user = portalRepository.findById(portalModel.getID());
        System.out.println(user.get().getID() + " " + user.get().getEmail());
        if(user.isPresent()){
            if(!portalModel.getFirstName().isEmpty()
            && !portalModel.getLastName().isEmpty()){
                user.get().setFirstName(portalModel.getFirstName());
                user.get().setLastName(portalModel.getLastName());
                portalRepository.save(user.get());
                return portalModel;
            }
            else{
                return null;
            }
        }
        return null;
    }

    public PortalModel deleteUserDetails(String email){
        System.out.println(portalRepository.count());
        Optional<PortalModel> user = portalRepository.findByEmail(email);
        if(user.isPresent()){
            portalRepository.deleteById(user.get().getID());
            return user.get();
        }
        else{
            return null;
        }
    }
}

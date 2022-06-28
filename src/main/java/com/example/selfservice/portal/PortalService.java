package com.example.selfservice.portal;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PortalService implements UserDetailsService {
    @Autowired
    private PortalRepository portalRepository;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtUser loadUserByUsername(String email) throws UsernameNotFoundException{
        System.out.println("Inside service body");
        PortalModel portalModel = portalRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email"));
        if(portalModel == null){
            throw new UsernameNotFoundException("Invalid credentials");
        }
        else{
            System.out.println(portalModel.getID() + " " + portalModel.getEmail());
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            return new JwtUser(email, portalModel.getPassword(), true, authorities);
        }
    }

    public Boolean verifyAccess(String email, int ID){
       Optional<PortalModel> user = portalRepository.findByEmail(email);
       if(user.isPresent() && user.get().getID() == ID){
           return true;
       }
       return false;
    }

    public Optional<PortalModel> getUserInfo(Integer id){
        return portalRepository.findById(id);
    }

    public List<PortalModel> getAllRecords(){
        return portalRepository.findAll();
    }

    public Optional<PortalModel> getUserDetails(PortalModel portalModel){
        System.out.println("Inside service getuserdetails" + " " + portalModel.getEmail());
        Optional<PortalModel> user = portalRepository.findByEmail(portalModel.getEmail());
        System.out.println(user.get().getEmail());
//        System.out.println("Stored password" + " " + user.get().getPassword());
//        System.out.println("Received password" + " " + bCryptPasswordEncoder.encode(portalModel.getPassword()));
        if(user.isPresent() && user.get().getEmail().equals(portalModel.getEmail()) &&
        user.get().getPassword().equals(portalModel.getPassword())){
            System.out.println("Inside if");
            user.get().setPassword(null);
            return user;
        }
        return Optional.empty();
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
//            portalModel
//                    .setPassword(bCryptPasswordEncoder
//                            .encode(portalModel.getPassword()));
            portalRepository.save(portalModel);
            return portalModel;
        }
        return null;
    }

    public PortalModel editUserDetails(PortalModel portalModel){
        Optional<PortalModel> user = portalRepository.findById(portalModel.getID());
        if(user.isPresent()){
            if(!portalModel.getEmail().equals(user.get().getEmail())){
                return null;
            }
            if(!portalModel.getFirstName().isEmpty()
            && !portalModel.getLastName().isEmpty()){
                user.get().setFirstName(portalModel.getFirstName());
                user.get().setLastName(portalModel.getLastName());
                user.get().setAge(portalModel.getAge());
                user.get().setCity(portalModel.getCity());
                user.get().setCountry(portalModel.getCountry());
                user.get().setState(portalModel.getState());
                user.get().setAddressLine1(portalModel.getAddressLine1());
                user.get().setAddressLine2(portalModel.getAddressLine2());
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

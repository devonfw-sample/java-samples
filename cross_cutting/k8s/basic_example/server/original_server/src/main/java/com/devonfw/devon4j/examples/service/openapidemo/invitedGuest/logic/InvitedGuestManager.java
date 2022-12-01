package com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.logic;

import com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.domain.InvitedGuest;
import com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.domain.InvitedGuestRepository;
import com.devonfw.devon4j.generated.api.model.InvitedGuestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitedGuestManager {

    @Autowired
    InvitedGuestRepository invitedGuestRepository;


    public List<InvitedGuestTO> getAllGuest() {
        List<InvitedGuest> foundGuests = invitedGuestRepository.findAll();
        List<InvitedGuestTO> resultGuests = new ArrayList<>();
        foundGuests.forEach(guest -> {
            InvitedGuestTO resultGuest = new InvitedGuestTO();
            resultGuest
                    .id(guest.getId())
                    .email(guest.getEmail())
                    .modificationCounter(guest.getModificationCounter());
            resultGuests.add(resultGuest);
        });
        return resultGuests;
    }

    public InvitedGuestTO getGuest(Long id) {
        InvitedGuest foundGuest = invitedGuestRepository.getReferenceById(id);
        InvitedGuestTO resultGuest = new InvitedGuestTO();
        resultGuest
                .id(foundGuest.getId())
                .email(foundGuest.getEmail())
                .modificationCounter(foundGuest.getModificationCounter());
        return resultGuest;
    }

    public InvitedGuestTO createInvitedGuest(InvitedGuestTO invitedGuestTO) {
        InvitedGuest guest = new InvitedGuest();
        guest.setId(invitedGuestTO.getId());
        guest.setModificationCounter(invitedGuestTO.getModificationCounter());
        guest.setEmail(invitedGuestTO.getEmail());
        invitedGuestRepository.save(guest);
        return invitedGuestTO;
    }
}

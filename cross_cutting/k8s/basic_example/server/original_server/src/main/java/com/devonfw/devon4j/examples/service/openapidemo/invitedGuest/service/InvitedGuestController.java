package com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.service;

import com.devonfw.devon4j.examples.service.openapidemo.invitedGuest.logic.InvitedGuestManager;
import com.devonfw.devon4j.generated.api.model.InvitedGuestTO;
import com.devonfw.devon4j.generated.api.service.InvitedGuestApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvitedGuestController implements InvitedGuestApi {

    InvitedGuestManager invitedGuestManager;

    public InvitedGuestController(InvitedGuestManager invitedGuestManager) {
        this.invitedGuestManager = invitedGuestManager;
    }

    @Override
    public ResponseEntity<InvitedGuestTO> createInvitedGuest(InvitedGuestTO invitedGuestTO) {
        return new ResponseEntity<>(invitedGuestManager.createInvitedGuest(invitedGuestTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<InvitedGuestTO>> getInvitedGuestAll() {
        return new ResponseEntity<>(invitedGuestManager.getAllGuest(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InvitedGuestTO> getInvitedGuestById(Long guestId) {
        return new ResponseEntity<>(invitedGuestManager.getGuest(guestId), HttpStatus.OK);
    }
}

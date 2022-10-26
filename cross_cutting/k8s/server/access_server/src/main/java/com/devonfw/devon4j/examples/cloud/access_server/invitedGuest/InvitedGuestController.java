package com.devonfw.devon4j.examples.cloud.access_server.invitedGuest;

import com.devonfw.devon4j.generated.client.handler.ApiException;
import com.devonfw.devon4j.generated.client.model.InvitedGuestTO;
import com.devonfw.devon4j.generated.client.service.InvitedGuestApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InvitedGuestController {

    InvitedGuestApi invitedGuestApi;

    public InvitedGuestController(InvitedGuestApi invitedGuestApi) {
        this.invitedGuestApi = invitedGuestApi;
    }

    @GetMapping("/guest")
    public List<InvitedGuestTO> getAllGuests() throws ApiException {
        return invitedGuestApi.getInvitedGuestAll();
    }


    @GetMapping("/guest/{id}")
    public InvitedGuestTO getAllGuests(@PathVariable Long id) throws ApiException {
        return invitedGuestApi.getInvitedGuestById(id);
    }


    @PostMapping("/guest")
    public InvitedGuestTO addGuests(@RequestBody InvitedGuestTO invitedGuestTO) throws ApiException {
        return invitedGuestApi.createInvitedGuest(invitedGuestTO);
    }


}

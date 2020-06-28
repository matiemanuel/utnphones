package edu.utn.utnphones.utils;

import edu.utn.utnphones.model.Call;
import edu.utn.utnphones.model.PhoneLine;
import edu.utn.utnphones.model.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class RestUtils {

    public static URI getLocation(Call call) {

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("?idPhoneLine={idPhoneCall}")
                .buildAndExpand(call.getId())
                .toUri();
    }
    //todo change path

    public static URI getLocation(PhoneLine phoneLine) {

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("?idPhoneLine={idPhoneLine}")
                .buildAndExpand(phoneLine.getId())
                .toUri();
    }
    //todo change path

    public static URI getLocation(User user) {

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("userId={idUser}")
                .buildAndExpand(user.getId())
                .toUri();
    }
}

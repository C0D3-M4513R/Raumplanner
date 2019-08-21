package com.piinfo.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piinfo.db.data.Room;
import com.piinfo.db.data.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;
import org.springframework.ui.Model;

import java.util.List;


@Controller
public class PlannerController {

    @Autowired
    private RoomRepository rooms;

    @GetMapping(value = "/room",params = {})
    private String room(Model model, Authentication auth){
        model.addAttribute("username", HtmlUtils.htmlEscape(auth.getName()));
        String[] scripts = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.js"
                , "/mindmup-editabletable.js", "/numeric-input-example.js"};
        String[] links = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.css"};
        model.addAttribute("scripts", scripts);
        return "rooms";
    }

    @GetMapping(value = "/room",params = {"type"})
    private @ResponseBody String roomData(@RequestParam String type) throws JsonProcessingException {
        if(type.equals("data")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Room> roomData = rooms.findAll();
            roomData.add(new Room("New Room"));
            return mapper.writeValueAsString(roomData);
        }
        return "500";
    }

}

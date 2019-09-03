package com.piinfo.springboot;

import com.piinfo.db.data.Room;
import com.piinfo.db.data.RoomRepository;
import com.piinfo.service.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
public class PlannerController {


    @Autowired
    private RoomRepository rooms;

    @GetMapping("/room")
    public String getPlannerList(Model model, Authentication auth) {
        Header[] headers = {
                new Header("Id", "no", true),
                new Header("Name", "name", true),
                new Header("Link", "Link", false)
        };

        model.addAttribute("username", HtmlUtils.htmlEscape(auth.getName()));
        String[] scripts = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.js"
                , "/mindmup-editabletable.js", "/numeric-input-example.js"};
        String[] links = {"webjars/bootstrap-table/1.14.1/dist/bootstrap-table.css"};


        model.addAttribute("scripts", scripts);
        model.addAttribute("headers", headers);
        model.addAttribute("username", HtmlUtils.htmlEscape(auth.getName()));

        return "tableAndEdit";
    }


    @GetMapping(value = "/room", params = "type")
    public @ResponseBody
    String getRoomData(@RequestParam String type, Authentication auth) throws Exception {
        switch (type) {
            case "data":
                List<Room> roomList = rooms.findAll();
                //Allow for new Room creation
                //Could be a static db entry to have less update() code
                roomList.add(new Room(0, "New Room", 0, "<a href='room/create/'>Zum Raum</a>", null));

                JSONArray arr = new JSONArray();
                roomList.forEach(room->{
                    arr.put(room.toJson());
                });
                return arr.toString();
            default:
                return "Please specify a request type!";
        }
    }

    @PostMapping(value = "/room", params = {"name", "value", "id"})
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestParam int name, @RequestParam String value, @RequestParam String id) {
        long intId;
        Room room=null;
        boolean creating = id.equals("create");
        if (!creating) {
            intId = Long.parseLong(id);
             room = rooms.findById(intId);
        }
        switch (name) {
            case 0:
                if(creating) rooms.saveAndFlush(new Room("New Room",Integer.parseInt(value)));
                else room.no=Integer.parseInt(value);
                break;
            case 1:
                if(creating) rooms.saveAndFlush(new Room(value,0));
                else room.name=value;
                break;
        }
        if(!creating) rooms.saveAndFlush(room);

    }

    @GetMapping(value = "/room/create")
    public @ResponseBody String createRoom(){
        //TODO: Add a proper form
        return "This is still a stub!";
    }

    @GetMapping(value = "/room/{id}")
    public @ResponseBody String showRoom(@PathVariable("id") long id){
        //Todo: Show the actual room editor
        return rooms.findById(id).toJson().toString();
    }




}

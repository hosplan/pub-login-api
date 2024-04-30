package com.iuni.login.controller;

import com.iuni.login.domain.Member;
import com.iuni.login.domain.MemberMap;
import com.iuni.login.service.MemberMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/membermap")
public class MemberMapController {
    private final MemberMapService memberMapService;

    @GetMapping()
    public ArrayList<Member> loadFriends(@RequestParam("id") Long id){
        return memberMapService.loadFriends(id);
    }

    @PostMapping()
    public HashMap<String, String> create(@RequestBody MemberMap data){
        HashMap<String, String> result = new HashMap<>();
        result.put("result", memberMapService.create(data));
        return result;
    }
}

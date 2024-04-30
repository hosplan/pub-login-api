package com.iuni.login.controller;

import com.iuni.login.domain.IuniStyle;
import com.iuni.login.domain.MemberIuniMap;
import com.iuni.login.service.IuniStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/iuniStyle")
public class IuniStyleController {
    private final IuniStyleService iuniStyleService;


    @PostMapping()
    public IuniStyle create(@RequestBody IuniStyle data){ return this.iuniStyleService.create(data); }

    @GetMapping()
    public IuniStyle get(@RequestHeader Map<String, String> headers) {
        return this.iuniStyleService.get(headers.get("authorization"));
    }

    @PostMapping("/loadJoinMember")
    public ArrayList<IuniStyle> loadJoinMember(@RequestBody ArrayList<MemberIuniMap> dataList){
        return iuniStyleService.loadJoinMember(dataList);
    }
    @GetMapping("/detail")
    public IuniStyle getByCreator(@RequestParam("id") Long id){
        return this.iuniStyleService.getById(id);
    }

    @PatchMapping()
    public Boolean update(@RequestBody IuniStyle data) { return this.iuniStyleService.update(data); }
}

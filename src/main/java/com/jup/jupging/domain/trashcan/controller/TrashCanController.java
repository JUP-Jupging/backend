package com.jup.jupging.domain.trashcan.controller;

import com.jup.jupging.domain.trashcan.dto.TrashCanDto;
import com.jup.jupging.domain.trashcan.service.TrashCanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/trash-can", produces = "application/json; charset=utf8")
public class TrashCanController {

    private final TrashCanService trashCanService;

    public TrashCanController(TrashCanService trashCanService) {
        this.trashCanService = trashCanService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> insertTrashCans() throws IOException, InterruptedException {
        trashCanService.insertTrashCans();

        return ResponseEntity.ok("휴지통 insert 완료");
    }


    @GetMapping
    public ResponseEntity<?> findTrashCans(){
        List<TrashCanDto> trashCans = trashCanService.findTrashCans();

        return ResponseEntity.ok(trashCans);

    }



    @GetMapping("/{trashCanId}")
    public ResponseEntity<?> getTrashCanDetail(@PathVariable Long trashCanId){

        TrashCanDto trashCanDto = trashCanService.getTrashCanDetail(trashCanId);
        return ResponseEntity.ok(trashCanDto);
    }




}

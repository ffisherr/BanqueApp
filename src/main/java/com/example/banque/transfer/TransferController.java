package com.example.banque.transfer;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @ApiOperation(value = "Perform transfer")
    @PostMapping
    public ResponseEntity<?> transfer(@RequestBody TransferCreationRequest request) {
        transferService.transfer(request);
        return ResponseEntity.ok().build();
    }
}

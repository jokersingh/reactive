package com.example.reactive.controller;

import com.example.reactive.domain.Data;
import com.example.reactive.service.ExcelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/")
    Flux<Data> getExcelData(){
      return excelService.getData();
    }
}

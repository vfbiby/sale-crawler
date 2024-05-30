package com.muhuang.salecrawler.sale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/sales")
public class SaleController {

    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @PostMapping
    void saveSales(@RequestBody Sale sale) {
        saleRepository.save(sale);
    }

    @GetMapping
    Page<Sale> getSales() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        return saleRepository.findAll(pageRequest);
    }

}

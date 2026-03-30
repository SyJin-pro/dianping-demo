package com.jj.dianpingdemo.controller;

import com.jj.dianpingdemo.entity.Result;
import com.jj.dianpingdemo.entity.VoucherDto;
import com.jj.dianpingdemo.service.VoucherService;
import org.springframework.web.bind.annotation.*;

/**
 * @author: JSY
 * @version: 1.0
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping("/add")
    public Result addVoucher(@RequestBody VoucherDto dto) {
        return voucherService.addVoucher(dto);
    }

    @PostMapping("/seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherService.seckillVoucher(voucherId);
    }
}

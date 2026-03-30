package com.jj.dianpingdemo.service;

import com.jj.dianpingdemo.entity.*;
import com.jj.dianpingdemo.mapper.SeckillVoucherMapper;
import com.jj.dianpingdemo.mapper.VoucherMapper;
import com.jj.dianpingdemo.mapper.VoucherOrderMapper;
import com.jj.dianpingdemo.util.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @author: JSY
 * @version: 1.0
 */
@Service
public class VoucherService {

    private final VoucherMapper voucherMapper;
    private final SeckillVoucherMapper seckillVoucherMapper;

    private final VoucherOrderMapper voucherOrderMapper;

    public VoucherService(VoucherMapper voucherMapper,
                          SeckillVoucherMapper seckillVoucherMapper,
                          VoucherOrderMapper voucherOrderMapper) {
        this.voucherMapper = voucherMapper;
        this.seckillVoucherMapper = seckillVoucherMapper;
        this.voucherOrderMapper = voucherOrderMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addVoucher(VoucherDto dto) {
        // 秒杀券参数校验
        if (Integer.valueOf(1).equals(dto.getType())) {
            Assert.notNull(dto.getStock(), "秒杀券库存不能为空");
            Assert.notNull(dto.getBeginTime(), "秒杀开始时间不能为空");
            Assert.notNull(dto.getEndTime(), "秒杀结束时间不能为空");
        }

        // 1. 组装 Voucher 实体
        Voucher voucher = new Voucher();
        voucher.setShopId(dto.getShopId());
        voucher.setTitle(dto.getTitle());
        voucher.setSubTitle(dto.getSubTitle());
        voucher.setRules(dto.getRules());
        voucher.setPayValue(dto.getPayValue());
        voucher.setActualValue(dto.getActualValue());
        voucher.setType(dto.getType() == null ? 0 : dto.getType());
        voucher.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());

        // ==============================================
        // 🔥 补上创建时间、更新时间（核心修复）
        // ==============================================
        voucher.setCreateTime(LocalDateTime.now());
        voucher.setUpdateTime(LocalDateTime.now());

        // 2. 插入基础优惠券表
        voucherMapper.insert(voucher);

        // 3. 秒杀券额外处理
        if (Integer.valueOf(1).equals(voucher.getType())) {
            SeckillVoucher seckillVoucher = new SeckillVoucher();
            seckillVoucher.setVoucherId(voucher.getId());
            seckillVoucher.setStock(dto.getStock());
            seckillVoucher.setBeginTime(dto.getBeginTime());
            seckillVoucher.setEndTime(dto.getEndTime());

            // ==============================================
            // 🔥 秒杀券也补上创建时间、更新时间
            // ==============================================
            seckillVoucher.setCreateTime(LocalDateTime.now());
            seckillVoucher.setUpdateTime(LocalDateTime.now());

            seckillVoucherMapper.insert(seckillVoucher);
        }

        return Result.ok("新增优惠券成功，ID：" + voucher.getId());
    }

    // ==================== 秒杀抢购核心方法 ====================
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        // 1. 获取当前登录用户（从ThreadLocal）
        UserDto user = UserHolder.getUser();
        if (user == null) {
            return Result.fail("请先登录");
        }
        Long userId = user.getId();

        // 2. 查询秒杀券信息
        SeckillVoucher seckillVoucher = seckillVoucherMapper.selectByVoucherId(voucherId);
        if (seckillVoucher == null) {
            return Result.fail("优惠券不存在");
        }

        // 3. 校验秒杀时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillVoucher.getBeginTime())) {
            return Result.fail("秒杀尚未开始");
        }
        if (now.isAfter(seckillVoucher.getEndTime())) {
            return Result.fail("秒杀已经结束");
        }

        // 4. 校验库存
        if (seckillVoucher.getStock() < 1) {
            return Result.fail("库存不足");
        }

        // ===================== 【危险！模拟并发延迟】 =====================
        // 这里卡住100ms，让100个请求同时查到库存=10，然后一起扣库存
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        // =================================================================

        // 5. 一人一单：判断用户是否已经购买过
        Long orderId = voucherOrderMapper.existsByUserIdAndVoucherId(userId, voucherId);
        if (orderId != null) {
            return Result.fail("每人限购一张");
        }

        // ===================== 【危险！再卡一次，放大一人一单漏洞】 =====================
        try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        // ============================================================================

        // 6. 扣减库存
        int rows = seckillVoucherMapper.deductStock(voucherId);
        if (rows == 0) {
            return Result.fail("库存不足");
        }

        // 7. 创建订单（订单ID先用雪花算法，这里先简单用时间戳）
        VoucherOrder order = new VoucherOrder();
        order.setId(System.currentTimeMillis()); // 临时订单ID
        order.setUserId(userId);
        order.setVoucherId(voucherId);
        order.setStatus(1); // 未支付
        voucherOrderMapper.insert(order);

        // 8. 返回成功
        return Result.ok("秒杀成功！订单号：" + order.getId());
    }
}

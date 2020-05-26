package com.haier.mailcenter.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.mailcenter.model.BlackList;
import com.haier.mailcenter.service.BlackListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 黑名单接口
 */
@Slf4j
@Api(tags = {"黑名单接口"})
@RestController
@RequestMapping("/blackList")
public class BlackListController {

    @Autowired
    private BlackListService blackListService;

    @ApiOperation(value = "获取黑名单", notes = "获取黑名单")
    @GetMapping
    public ResponseEntity<List<BlackList>> getBlackList() {
        return ResponseEntity.ok(blackListService.list());
    }

    @ApiOperation(value = "新增黑名单", notes = "新增黑名单", produces = "application/json")
    @PostMapping
    public ResponseEntity<BlackList> addBlackList(@RequestBody BlackList blackList) {
        log.info("增加黑名单地址：{}", blackList.getMailAddress());
        blackListService.save(blackList);
        QueryWrapper<BlackList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mail_address", blackList.getMailAddress());
        queryWrapper.eq("client_id", blackList.getClientId());
        return ResponseEntity.ok(blackListService.getOne(queryWrapper));
    }

    @ApiOperation(value = "删除黑名单", notes = "删除黑名单", response = Boolean.class)
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Boolean> delBlackList(@PathVariable Long id) {
        log.info("删除黑名单ID：{}", id);
        return ResponseEntity.ok(blackListService.removeById(id));
    }
}

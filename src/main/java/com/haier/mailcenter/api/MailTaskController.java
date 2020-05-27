package com.haier.mailcenter.api;

import com.haier.mailcenter.common.ResponseMap;
import com.haier.mailcenter.dto.SendMailDto;
import com.haier.mailcenter.model.MailSendTask;
import com.haier.mailcenter.service.MailTaskQueueService;
import com.haier.mailcenter.service.MailTaskQueueSwitchService;
import com.haier.mailcenter.service.SendMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 任务接口
 */
@Slf4j
@Api(tags = {"邮件任务接口"})
@RestController
@RequestMapping("/mailTask")
public class MailTaskController {

    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private MailTaskQueueService mailTaskQueueService;
    @Autowired
    private MailTaskQueueSwitchService mailTaskQueueSwitchService;

    @ApiOperation(value = "新增邮件任务", notes = "通过校验参数后，将任务添加至队列")
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addMailTaskQueue(@RequestBody @Valid SendMailDto sendMailDto) {
        ResponseEntity<Map<String, Object>> result = ResponseEntity.ok(ResponseMap.assembleResultMessage(200, "成功添加任务"));
        if (!mailTaskQueueSwitchService.isQueueSwitchOpen()) {
            log.info("队列总开关关闭，暂时无法新增任务");
            return ResponseEntity.ok(ResponseMap.assembleResultMessage(500, "队列总开关关闭，暂时无法新增任务"));
        }
        try {
            sendMailService.sendMailNow(sendMailDto);
        } catch (Exception ce) {
            ce.printStackTrace();
            log.error("接口调用异常：{}", ce.getMessage());
            result = ResponseEntity.status(400).body(ResponseMap.assembleResultMessage(400, ce.getMessage(), ce.getLocalizedMessage()));
        }
        return result;
    }

    @ApiOperation(value = "查询任务列表", notes = "获取非即时任务清单，延迟执行或定时执行，可通过taskId取消任务")
    @GetMapping("list")
    public ResponseEntity<List<MailSendTask>> getMailTaskQueue() {

        return ResponseEntity.ok(mailTaskQueueService.getMailTaskQueue());
    }

    @ApiOperation(value = "查询备份任务列表", notes = "此列表为服务器重启或异常导致任务未正确执行列表")
    @GetMapping("listBak")
    public ResponseEntity<List<MailSendTask>> getMailTaskQueueBak() {
        return ResponseEntity.ok(mailTaskQueueService.getMailTaskQueueBak());
    }

    @ApiOperation(value = "查询单条任务", notes = "根据任务TaskID查询任务")
    @GetMapping("/{id:\\w+}")
    public ResponseEntity<MailSendTask> getMailTaskById(@PathVariable String id) {
        return ResponseEntity.ok(mailTaskQueueService.getMailTask(id));
    }


}

package com.haier.mailcenter.api;

import com.haier.mailcenter.service.MailTaskQueueSwitchService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"队列开关"})
@RestController
@RequestMapping("/queueSwitch")
public class MailQueueSwitchController {

    private final MailTaskQueueSwitchService mailTaskQueueSwitchService;

    public MailQueueSwitchController(MailTaskQueueSwitchService mailTaskQueueSwitchService) {
        this.mailTaskQueueSwitchService = mailTaskQueueSwitchService;
    }

    @PostMapping("/open")
    public ResponseEntity<Boolean> openQueueSwitch() {
        return ResponseEntity.ok(mailTaskQueueSwitchService.openQueueSwitch());
    }

    @PostMapping("/close")
    public ResponseEntity<Boolean> closeQueueSwitch() {
        return ResponseEntity.ok(mailTaskQueueSwitchService.closeQueueSwitch());
    }
}

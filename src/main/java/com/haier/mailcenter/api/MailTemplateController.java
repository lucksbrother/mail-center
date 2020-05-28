package com.haier.mailcenter.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.mailcenter.common.ResponseMap;
import com.haier.mailcenter.model.ClientInfo;
import com.haier.mailcenter.model.MailTemplate;
import com.haier.mailcenter.service.AuthService;
import com.haier.mailcenter.service.MailTemplateService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"邮件模版接口"})
@RestController
@RequestMapping("/mailTemplate")
public class MailTemplateController {

    private final MailTemplateService mailTemplateService;
    private final AuthService authService;

    public MailTemplateController(MailTemplateService mailTemplateService, AuthService authService) {
        this.mailTemplateService = mailTemplateService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Object> getMailTemplates(String clientSecret) {
        ClientInfo clientInfo = authService.getClientInfo(clientSecret);
        if (clientInfo == null) {
            return ResponseEntity.ok(ResponseMap.assembleResultMessage(500, "客户端信息不存在"));
        }
        QueryWrapper<MailTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("client_id", clientInfo.getId());
        return ResponseEntity.ok(mailTemplateService.list(queryWrapper));
    }

    @PostMapping
    public ResponseEntity<MailTemplate> addMailTemplate(@RequestBody MailTemplate mailTemplate) {
        mailTemplateService.save(mailTemplate);
        QueryWrapper<MailTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_name", mailTemplate.getTemplateName());
        return ResponseEntity.ok(mailTemplateService.getOne(queryWrapper));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<MailTemplate> updateMailTemplate(@RequestBody MailTemplate mailTemplate, @PathVariable Long id) {
        mailTemplateService.updateById(mailTemplate);
        return ResponseEntity.ok(mailTemplateService.getById(id));
    }

    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<Boolean> delMailTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(mailTemplateService.removeById(id));
    }

}

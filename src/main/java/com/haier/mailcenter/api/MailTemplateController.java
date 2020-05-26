package com.haier.mailcenter.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.haier.mailcenter.model.MailTemplate;
import com.haier.mailcenter.service.MailTemplateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"邮件模版接口"})
@RestController
@RequestMapping("/mailTemplate")
public class MailTemplateController {

    @Autowired
    private MailTemplateService mailTemplateService;

    @GetMapping()
    public ResponseEntity<List<MailTemplate>> getMailTemplates() {
        return ResponseEntity.ok(mailTemplateService.list());
    }

    @PostMapping()
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

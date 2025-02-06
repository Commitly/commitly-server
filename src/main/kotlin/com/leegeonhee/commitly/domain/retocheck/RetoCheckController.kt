package com.leegeonhee.commitly.domain.retocheck

import com.leegeonhee.commitly.domain.retocheck.model.RetoCheckRequestBody
import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("reto")
class RetoCheckController(
    val retoCheckService: RetoCheckService
) {
    @GetMapping("get")
    fun isMeanGet(
        @GetAuthenticatedId id: Long
    ) = retoCheckService.getMyDate(id)

    @PostMapping("post")
    fun isMeanPost(
        @GetAuthenticatedId id: Long,
        @RequestBody requestBody: RetoCheckRequestBody
    ) = retoCheckService.saveRetoDate(id,requestBody.date)
}
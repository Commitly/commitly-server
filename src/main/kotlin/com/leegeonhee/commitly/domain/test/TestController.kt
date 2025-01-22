//package com.leegeonhee.commitly.domain.test
//
//import com.leegeonhee.commitly.gloabl.common.BaseResponse
//import com.leegeonhee.commitly.gloabl.common.annotation.GetAuthenticatedId
//import com.leegeonhee.commitly.gloabl.jwt.AuthenticationFacade
//import com.leegeonhee.commitly.gloabl.jwt.JwtUserDetails
//import org.springframework.data.jpa.domain.AbstractPersistable_.id
//import org.springframework.security.core.annotation.AuthenticationPrincipal
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/test")
//class TestController(
//    private val authenticationFacade: AuthenticationFacade
//) {
//
//    @GetMapping("/nosuspend")
//    fun heellow() : BaseResponse<String> = BaseResponse(200, "OK", "굿")
//    @GetMapping("/yessuspend")
//    suspend fun noheellow(@GetAuthenticatedId userId: Long): BaseResponse<String> {
//        println("Authentication: " + SecurityContextHolder.getContext().authentication)
//        println("Principal: " + SecurityContextHolder.getContext().authentication.principal)
//        return BaseResponse(200, "OK", userId.toString())
//    }
//    @GetMapping("/gooddog")
//    fun noheellgooddw(@AuthenticationPrincipal principal: JwtUserDetails): BaseResponse<String> {
//
//        return BaseResponse(200, "OK", principal.id.toString())
//    }
//
//    @GetMapping("/iamgoodman")
//    fun iamgoodamn(): BaseResponse<String> {
//        val userId = authenticationFacade.getAuthenticatedUserId()
//        return BaseResponse(200, "OK", userId.toString())
//    }
//}
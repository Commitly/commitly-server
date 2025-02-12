package com.leegeonhee.commitly.domain.github.repository

import com.leegeonhee.commitly.domain.github.domain.entity.CommitInfoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface GithubRepository : JpaRepository<CommitInfoEntity, Long> {
    @Query(value = "SELECT * FROM commit_info WHERE user_name = :userName AND committed_date LIKE :day%", nativeQuery = true)
    fun findByUserNameAndDay(@Param("userName") userName: String, @Param("day") day: String): List<CommitInfoEntity>
}
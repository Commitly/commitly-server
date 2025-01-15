package com.leegeonhee.commitly.domain.github

import CommitInfo
import GitHubResponse
import com.leegeonhee.commitly.domain.auth.domain.entity.UserEntity
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.domain.github.domain.entity.CommitInfoEntity
import com.leegeonhee.commitly.domain.github.repository.GithubRepo
import com.leegeonhee.commitly.domain.gpt.GptService
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import com.leegeonhee.commitly.domain.gpt.repository.GptResponseRepository
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class GitHubService(
    private val webClient: WebClient,
    private val githubRepo: GithubRepo,
    private val gptService: GptService,
    private val userRepository: UserRepository,
    private val gptResponseRepository: GptResponseRepository,
    private val dataSourceScriptDatabaseInitializer: SqlDataSourceScriptDatabaseInitializer
) {
    fun getFromDB(name: String, date: String): BaseResponse<List<CommitInfoEntity>> {
        val commit = githubRepo.findByUserNameAndDay(name, date)
        return if (commit.isNotEmpty()) {
            BaseResponse(
                status = 200,
                message = "굿",
                data = commit
            )
        } else {
            BaseResponse(
                status = 404,
                message = "없음",
                data = emptyList()
            )
        }
    }

    suspend fun getCommitMessages(username: String, date: LocalDate): BaseResponse<List<CommitInfo>> {
        val duplicationChecker = getFromDB(username, date.toString())
        if (!duplicationChecker.data.isNullOrEmpty()) {
            println("좋은거 찾음")
            return BaseResponse(
                status = 200,
                message = "잘 찾음",
                data = duplicationChecker.data.map {
                    CommitInfo(
                        repositoryName = it.repositoryName,
                        message = it.message,
                        committedDate = it.committedDate
                    )
                }
            )
        }
        println("ㅇㅇㅇ: $username")
        val fromDate = date.atStartOfDay()
        val toDate = date.atTime(LocalTime.MAX)
        val query = """
        query {
          user(login: "$username") {
            repositories(first: 100, orderBy: {field: PUSHED_AT, direction: DESC}) {
              nodes {
                name
                owner {
                  login
                }
                defaultBranchRef {
                  target {
                    ... on Commit {
                      history(
                        first: 100,
                        since: "${fromDate.format(DateTimeFormatter.ISO_DATE_TIME)}",
                        until: "${toDate.format(DateTimeFormatter.ISO_DATE_TIME)}"
                      ) {
                        nodes {
                          message
                          committedDate
                          repository {
                            name
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            organizations(first: 10) {
              nodes {
                login
                repositories(first: 100, orderBy: {field: PUSHED_AT, direction: DESC}) {
                  nodes {
                    name
                    defaultBranchRef {
                      target {
                        ... on Commit {
                          history(
                            first: 100,
                            since: "${fromDate.format(DateTimeFormatter.ISO_DATE_TIME)}",
                            until: "${toDate.format(DateTimeFormatter.ISO_DATE_TIME)}"
                          ) {
                            nodes {
                              message
                              committedDate
                              repository {
                                name
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
    """.trimIndent()

        val response = webClient.post()
            .bodyValue(mapOf("query" to query))
            .retrieve()
            .awaitBody<GitHubResponse>()

        val commitInfos = response.data.user.repositories.nodes.flatMap { repo ->
            repo.defaultBranchRef?.target?.history?.nodes?.map { commit ->
                CommitInfo(
                    repositoryName = repo.name,
                    message = commit.message,
                    committedDate = commit.committedDate
                )
            } ?: emptyList() // 커밋이 없으면 빈 리스트로 처리
        }

        if (commitInfos.isNotEmpty()) {
            withContext(Dispatchers.IO){
                commitInfos.forEach {
                    githubRepo.save(
                        CommitInfoEntity(
                            repositoryName = it.repositoryName,
                            userName = username,
                            message = it.message,
                            committedDate = it.committedDate
                        )
                    )
                }
            }
        }
        return if (commitInfos.isEmpty()) {
            BaseResponse(
                status = 404,
                message = "커밋을 찾을 수 없습니다ddddd.",
                data = emptyList()
            )
        } else {
            BaseResponse(
                status = 200,
                message = "커밋을 성공적으로 조회했습니다.",
                data = commitInfos
            )
        }
    }


    suspend fun generateMemoirWithGpt(login: String, date: LocalDate): BaseResponse<String> {
        val userCommit = getCommitMessages(login, date)
        val userInfo = withContext(Dispatchers.IO) {
            userRepository.findByLogin(login)
        }
        println("adjadjadjkfakjdfkjd---------------${userInfo.login}")
        if (userCommit.data.isNullOrEmpty()) {
            return BaseResponse(
                status = 404,
                message = "이 날의 커밋 기록이 없습니다.",
                data = null
            )
        }

        val response = gptService.askToGptRequest(userCommit.data.toString())
        withContext(Dispatchers.IO) {
            gptResponseRepository.save(
                GptResponseEntity(
                    user = UserEntity(
                        userId = userInfo.userId,
                        login = userInfo.login,
                        name = userInfo.name,
                    ),
                    response = response,
                    date = date.toString()
                )
            )
        }
        println(userCommit.data.toString())
        return BaseResponse(
            status = 200,
            message = "잘옴",
            data = response
        )
    }


}
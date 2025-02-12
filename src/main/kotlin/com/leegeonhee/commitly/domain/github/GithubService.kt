package com.leegeonhee.commitly.domain.github

import CommitInfo
import GitHubResponse
import com.leegeonhee.commitly.domain.auth.domain.model.LiteGptGetDb
import com.leegeonhee.commitly.domain.auth.domain.repository.UserRepository
import com.leegeonhee.commitly.domain.github.domain.dto.CommitBaseResponse
import com.leegeonhee.commitly.domain.github.domain.entity.CommitInfoEntity
import com.leegeonhee.commitly.domain.github.repository.GithubRepository
import com.leegeonhee.commitly.domain.gpt.GptService
import com.leegeonhee.commitly.domain.gpt.domain.entity.GptResponseEntity
import com.leegeonhee.commitly.domain.gpt.repository.GptResponseRepository
import com.leegeonhee.commitly.domain.retocheck.RetoCheckService
import com.leegeonhee.commitly.gloabl.common.BaseResponse
import com.leegeonhee.commitly.gloabl.jwt.JwtUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.SortArgumentResolver
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.reflect.typeOf

@Service
class GitHubService(
    private val webClient: WebClient,
    private val githubRepository: GithubRepository,
    private val gptService: GptService,
    private val userRepository: UserRepository,
    private val gptResponseRepository: GptResponseRepository,
    private val retoCheckService: RetoCheckService,
) {

    fun getFromDB(name: String, date: String): BaseResponse<List<CommitInfoEntity>> {
        val commit = githubRepository.findByUserNameAndDay(name, date)
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

    fun getCommitMessages(userId: Long, date: LocalDate): CommitBaseResponse<List<CommitInfo>> {
        val username =
            userRepository.findByIdOrNull(userId) ?: return CommitBaseResponse(
                status = 404,
                message = "당신을 찾을수가 없어요",
                tag = emptySet()
            )
        println(username)

        val duplicationChecker = getFromDB(username.login, date.toString())
        if (!duplicationChecker.data.isNullOrEmpty()) {
            return CommitBaseResponse(
                status = 200,
                message = "잘 찾음",
                tag = duplicationChecker.data.map {
                    it.repositoryName
                }.toSet(),
                data = duplicationChecker.data.map {
                    CommitInfo(
                        repositoryName = it.repositoryName,
                        message = it.message,
                        committedDate = it.committedDate
                    )
                }
            )
        }
        val onlyYearAndMont  =  date.toString().substring(0 until 7)
        val monthIsEmpty = githubRepository.findByUserNameAndDay(username.login,onlyYearAndMont)
//        println("-afa-fad-fadd-fsad-f-a-fdas")
//        monthIsEmpty.forEach{
//            println(it.message)
//        }
//        println(monthIsEmpty.size)
//        println("-afa-fad-fadd-fsad-f-a-fdas")

        if(monthIsEmpty.isNotEmpty()) {
            return CommitBaseResponse(
                status = 404,
                message = "커밋을 찾을 수 없습니다ddddd.",
                tag = emptySet(),
                data = emptyList()
            )
        }


        val firstDay = date.withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.of("+09:00"))
        val lastDay = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX).atOffset(ZoneOffset.of("+09:00"))
        println(firstDay)
        println(lastDay)
        val query = """
    query {
      user(login: "${username.login}") {
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
                    since: "${firstDay.format(DateTimeFormatter.ISO_DATE_TIME)}",
                    until: "${lastDay.format(DateTimeFormatter.ISO_DATE_TIME)}"
                  ) {
                    nodes {
                      message
                      committedDate
                      author {
                        user {
                          login
                        }
                      }
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
                        since: "${firstDay.format(DateTimeFormatter.ISO_DATE_TIME)}",
                        until: "${lastDay.format(DateTimeFormatter.ISO_DATE_TIME)}"
                      ) {
                        nodes {
                          message
                          committedDate
                          author {
                            user {
                              login
                            }
                          }
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
            .bodyToMono<GitHubResponse>()
            .doOnError { e ->
                println("API 요청 실패: ${e.message}")
            }
            .block()
        println("ㅎㅇㅎㅁㅎㅁㅎㅇㅁㅎㅇㄴ$response")

        val commitInfos = response?.data?.user?.repositories?.nodes?.flatMap { repo ->
            repo.defaultBranchRef?.target?.history?.nodes?.map { commit ->
                CommitInfo(
                    repositoryName = repo.name,
                    message = commit.message,
                    committedDate = commit.committedDate
                )
            } ?: emptyList() // 커밋이 없으면 빈 리스트로 처리
        } ?: emptyList() // repositories가 null일 경우 빈 리스트 반환

        if (commitInfos.isNotEmpty()) {
            commitInfos.forEach {
//                if (!it.committedDate.startsWith(date.toString())) {
//                    return@forEach
//                }
                githubRepository.save(
                    CommitInfoEntity(
                        repositoryName = it.repositoryName,
                        userName = username.login,
                        message = it.message,
                        committedDate = it.committedDate
                    )
                )
                retoCheckService.saveRetoDate(
                    userId = userId,
                    date = it.committedDate
                )
            }
        }
        val responseCommit = getFromDB(name = username.login, date.toString())
        if (!responseCommit.data.isNullOrEmpty()) {
            val commitTag = responseCommit.data.map {
                it.repositoryName
            }.toSet()
            return CommitBaseResponse(
                status = 200,
                message = "커밋을 성공적으로 조회했습니다.",
                tag = commitTag,
                data = commitInfos
            )
        }
        return CommitBaseResponse(
            status = 404,
            message = "커밋을 찾을 수 없습니다ddddd.",
            tag = emptySet(),
            data = emptyList()
        )
    }


    fun generateMemoirWithGpt(userId: Long, date: LocalDate, repositoryName: String): BaseResponse<String> {
        val userCommit = getCommitMessages(userId, date)
        if (userCommit.data.isNullOrEmpty()) {
            return BaseResponse(
                status = 404,
                message = "이 날의 커밋 기록이 없습니다.",
                data = null
            )
        }

        val realSlimUserCommit = userCommit.data.filter {
            it.repositoryName.startsWith(repositoryName)
        }
        val response =
            if (repositoryName == "ALL") gptService.askToGptRequest(userCommit.data.toString()) else gptService.askToGptRequest(
                realSlimUserCommit.toString()
            )

        val user = userRepository.findById(userId).orElseThrow {
            IllegalStateException("User not found with id: $userId")
        }

        gptResponseRepository.save(
            GptResponseEntity(
                user = user,
                response = response,
                responseDate = date.toString(),
                registrationDate = LocalDateTime.now()
            )
        )

        return BaseResponse(
            status = 200,
            message = "잘옴",
            data = response
        )
    }

    fun getGptResponseFromDb(userId: Long, day: String): BaseResponse<List<LiteGptGetDb>> {
        return BaseResponse(
            status = 200,
            data = gptResponseRepository.findByUserNameAndDay(userId, day).map {
                LiteGptGetDb(
                    response = it.response,
                    responseDate = it.responseDate
                )
            },
            message = "굿"
        )
    }
}
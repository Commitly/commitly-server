package com.leegeonhee.commitly.domain.github

import CommitInfo
import GitHubResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class GitHubService(private val webClient: WebClient) {
    suspend fun getCommitMessages(username: String, date: LocalDate): List<CommitInfo> {
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

        return response.data.user.repositories.nodes
            .flatMap { repo ->
                repo.defaultBranchRef?.target?.history?.nodes?.map { commit ->
                    CommitInfo(
                        repositoryName = repo.name,
                        message = commit.message,
                        committedDate = commit.committedDate
                    )
                } ?: emptyList()
            }
    }


    suspend fun getOrganizationCommitMessages(username: String, date: LocalDate, organization: String): List<CommitInfo> {
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
              }
            }
          }
        }
    """.trimIndent()

        val response = webClient.post()
            .bodyValue(mapOf("query" to query))
            .retrieve()
            .awaitBody<GitHubResponse>()

        // 사용자 리포지토리
        val userRepos = response.data.user.repositories.nodes

        // 한 개의 조직만 필터링 (organization 이름이 일치하는 조직만 처리)
        val orgRepos = response.data.user.organizations.nodes
            .firstOrNull { it.login == organization } // 해당 조직만 가져오기
            ?.repositories
            ?.nodes ?: emptyList() // 리포지토리가 없으면 빈 리스트 반환

        // 사용자와 조직의 리포지토리 합침
        val allRepos = userRepos + orgRepos

        // 커밋 메시지 반환
        return allRepos.flatMap { repo ->
            repo.defaultBranchRef?.target?.history?.nodes?.map { commit ->
                CommitInfo(
                    repositoryName = repo.name,
                    message = commit.message,
                    committedDate = commit.committedDate
                )
            } ?: emptyList()
        }
    }


}
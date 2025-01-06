package com.leegeonhee.commitly

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommitlyApplication

fun main(args: Array<String>) {
    runApplication<CommitlyApplication>(*args)
}

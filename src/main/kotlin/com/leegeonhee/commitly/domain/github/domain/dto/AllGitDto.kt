data class GitHubResponse(
    val data: Data
)

data class Data(
    val user: User
)

data class User(
    val repositories: Repositories,
    val organizations: Organizations
)

data class Repositories(
    val nodes: List<RepositoryNode>
)

data class Organizations(
    val nodes: List<OrganizationNode>
)

data class RepositoryNode(
    val name: String,
    val owner: Owner?,
    val defaultBranchRef: DefaultBranchRef?
)

data class OrganizationNode(
    val login: String,
    val repositories: Repositories
)

data class Owner(
    val login: String
)

data class DefaultBranchRef(
    val target: Target?
)

data class Target(
    val history: History?
)

data class History(
    val nodes: List<CommitNode>
)

data class CommitNode(
    val message: String,
    val committedDate: String,
    val repository: Repository
)

data class Repository(
    val name: String
)

data class CommitInfo(
    val repositoryName: String,
    val message: String,
    val committedDate: String
)

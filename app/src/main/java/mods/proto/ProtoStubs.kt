package mods.proto

/**
 * Lightweight stubs replacing generated protobuf classes so the update flow compiles
 * without protoc output. These are minimal and do not perform real proto parsing.
 */

class PollRequest private constructor() {
    companion object {
        @JvmStatic fun newBuilder() = Builder()
    }
    class Builder {
        fun build(): ByteArray = ByteArray(0) // empty payload
    }
}

class PollResponse {
    var updateInfo: UpdateInfo? = null
    var pollingIntervalMs: Long? = null
    var enableEvents: Boolean? = null
    var devIds: List<Long> = emptyList()

    companion object {
        @JvmStatic
        fun parser(): (java.io.InputStream) -> PollResponse = { PollResponse() }
    }
}

class UpdateInfo {
    enum class UpdateAction { NOT_NEEDED, UPDATE }

    var action: UpdateAction = UpdateAction.NOT_NEEDED
    var newVersionCode: Long = 0
    var updateMessage: String = ""
    var fileUrl: String = ""
    var homePageUrl: String = ""
    var fileSha384Hash: String = ""
    var fileSize: Long = 0
}

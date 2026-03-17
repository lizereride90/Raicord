package mods.activity.update

import mods.DiscordTools
import mods.constants.PreferenceKeys
import mods.net.proto.RaicordService
import mods.preference.Prefs
import mods.promise.Promise
import mods.promise.asResolvedPromise
import mods.promise.runCatchingOrLog
import mods.proto.PollResponse
import mods.utils.DevBadge
import mods.utils.LogUtils
import mods.utils.OnceFunc
import mods.utils.StoreUtils
import java.io.File
import java.util.concurrent.TimeUnit

object ServerConfigStorage {

    private val TAG = ServerConfigStorage::class.java.simpleName

    private val DEFAULT_CONFIG = PollResponse()

    private val file by lazy { File(DiscordTools.context.filesDir, "config.pb") }

    private val config = OnceFunc {
        runCatchingOrLog {
            load(file)
        }.getOrElse { DEFAULT_CONFIG }
    }

    @JvmStatic
    val pollingIntervalMs: Long
        get() = config.get()?.pollingIntervalMs?.takeIf { it > 0L }
            ?: TimeUnit.MINUTES.toMillis(30)

    @JvmStatic
    fun load(): Promise<PollResponse> {
        return pollServer()
    }

    @JvmStatic
    fun loadNow(): PollResponse {
        return loadNowOrNull() ?: DEFAULT_CONFIG
    }

    @JvmStatic
    fun loadNowOrNull(): PollResponse? {
        return config.get()
    }

    @JvmStatic
    fun maybePollServer(): Promise<PollResponse> {
        val config = this.config.get()
        return if (config == null || config == DEFAULT_CONFIG) {
            LogUtils.log(TAG, "config is null, polling")
            pollServer()
        } else if (StoreUtils.getServerSyncedTime() > Prefs.getLong(PreferenceKeys.WEBSITE_LAST_FETCHED_TIMESTAMP, -1)) {
            LogUtils.log(TAG, "config is expired, polling")
            pollServer()
        } else {
            config.asResolvedPromise()
        }
    }

    @JvmStatic
    private fun pollServer(): Promise<PollResponse> {
        return RaicordService.poll().doOnSuccess {
            runCatchingOrLog { store(it) }
            config.set(it)
            DevBadge.update()
            Prefs.setLong(
                PreferenceKeys.WEBSITE_LAST_FETCHED_TIMESTAMP,
                StoreUtils.getServerSyncedTime() + (it.pollingIntervalMs ?: 0L)
            )
        }.doOnError {
            LogUtils.log(TAG, "failed to poll", it)
        }
    }

    // Stub helpers since proto is stubbed
    private fun store(resp: PollResponse) {
        runCatchingOrLog {
            file.outputStream().use { os -> os.write(ByteArray(0)) }
        }
    }

    private fun load(file: File): PollResponse {
        // no-op: return empty stub
        return PollResponse()
    }
}

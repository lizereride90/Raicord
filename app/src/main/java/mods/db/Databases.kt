package mods.db

import android.database.sqlite.SQLiteException
import mods.DiscordTools

object Databases {

    private const val DB_NAME = "Raicord.db"
    private var db: RaicordDatabase? = null

    @JvmStatic
    @Throws(SQLiteException::class)
    fun get(): RaicordDatabase {
        var db = this.db
        if (db == null || !db.isHealthy) {
            db = RaicordDatabase(DiscordTools.context.openOrCreateDatabase(DB_NAME, 0, null))
        }
        return db
    }
}

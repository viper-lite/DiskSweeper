package io.viper.android.disk.sweeper

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "records")
class FileRecord {
    @DatabaseField(id = true)
    private var path: String? = null

    @DatabaseField(canBeNull = false)
    private var size: Long = 0

    constructor()

    constructor(path: String?, size: Long) {
        this.path = path
        this.size = size
    }
}

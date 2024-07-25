package io.viper.android.disk.sweeper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "records")
public class FileRecord {
    @DatabaseField(id = true)
    private String path;
    @DatabaseField(canBeNull = false)
    private long size;

    public FileRecord() {
        //For OrmLite
    }

    public FileRecord(String path, long size) {
        this.path = path;
        this.size = size;
    }
}

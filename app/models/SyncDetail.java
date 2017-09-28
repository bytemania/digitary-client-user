package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class SyncDetail {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private Timestamp syncStamp;

    public SyncDetail() {
    }

    public SyncDetail(Timestamp syncStamp) {
        this.syncStamp = syncStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getSyncStamp() {
        return syncStamp;
    }

    public void setSyncStamp(Timestamp syncStamp) {
        this.syncStamp = syncStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyncDetail that = (SyncDetail) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

package me.kurwixon.kits;

import java.util.UUID;

public class Kit {

    private UUID uuid;
    private long kit1;
    private long kit2;
    private long kit3;

    public Kit(final UUID uuid) {
        this.uuid = uuid;
        this.kit1 = 0L;
        this.kit2 = 0L;
        this.kit3 = 0L;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Kit setUuid(final UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public long getKit1() {
        return kit1;
    }

    public Kit setKit1(final long kit1) {
        this.kit1 = kit1;
        return this;
    }

    public long getKit2() {
        return kit2;
    }

    public Kit setKit2(final long kit2) {
        this.kit2 = kit2;
        return this;
    }

    public long getKit3() {
        return kit3;
    }

    public Kit setKit3(final long kit3) {
        this.kit3 = kit3;
        return this;
    }

    public String toMap(){
        return getKit1() + "YYY" + getKit2() + "YYY" + getKit3();
    }

    public Kit(final UUID uuid, final long kit1, final long kit2, final long kit3) {
        this.uuid = uuid;
        this.kit1 = kit1;
        this.kit2 = kit2;
        this.kit3 = kit3;
    }
}

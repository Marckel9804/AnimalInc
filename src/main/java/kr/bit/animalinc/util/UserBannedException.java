package kr.bit.animalinc.util;

import java.util.Date;

public class UserBannedException extends RuntimeException {
    private final String banReason;
    private final Date unlockDate;

    public UserBannedException(String banReason, Date unlockDate) {
        super("User is banned. Reason: " + banReason + ", Unlock date: " + unlockDate);
        this.banReason = banReason;
        this.unlockDate = unlockDate;
    }

    public String getBanReason() {
        return banReason;
    }

    public Date getUnlockDate() {
        return unlockDate;
    }
}

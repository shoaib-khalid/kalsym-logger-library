package com.kalsym.log;

import java.util.Date;
import java.util.GregorianCalendar;

class RollingCalendar extends GregorianCalendar {

    int type = -1;

    /**
     *
     * @param type
     */
    void setType(int type) {
        this.type = type;
    }

    public long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
    }

    /**
     *
     * @param now
     * @return next check date
     */
    public Date getNextCheckDate(Date now) {
        setTime(now);

        switch (this.type) {
            case 0:
                set(13, 0);
                set(14, 0);
                add(12, 1);
                break;
            case 1:
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(11, 1);
                break;
            case 2:
                set(12, 0);
                set(13, 0);
                set(14, 0);
                int hour = get(11);
                if (hour < 12) {
                    set(11, 12);
                } else {
                    set(11, 0);
                    add(5, 1);
                }
                break;
            case 3:
                set(11, 0);
                set(12, 0);
                set(13, 0);
                set(14, 0);
                add(5, 1);
                break;
            case 4:
                set(7, getFirstDayOfWeek());
                set(11, 0);
                set(13, 0);
                set(14, 0);
                add(3, 1);
                break;
            case 5:
                set(5, 1);
                set(11, 0);
                set(13, 0);
                set(14, 0);
                add(2, 1);
                break;
            default:
                throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }
}

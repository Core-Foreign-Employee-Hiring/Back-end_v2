package com.forwork.backend.api.recruit.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum WorkingDays {

    MONDAY(1, "월"),
    TUESDAY(1 << 1, "화"),
    WEDNESDAY(1 << 2, "수"),
    THURSDAY(1 << 3, "목"),
    FRIDAY(1 << 4, "금"),
    SATURDAY(1 << 5, "토"),
    SUNDAY(1 << 6, "일");

    private final int bit;
    private final String displayName;

    WorkingDays(int bit, String displayName) {
        this.bit = bit;
        this.displayName = displayName;
    }


    /**
     * Enum Set -> Bit
     */
    public static int toBit(Set<WorkingDays> days) {
        return days.stream()
                .mapToInt(WorkingDays::getBit)
                .reduce(0, (a, b) -> a | b);
    }

    /**
     * Bit -> Enum Set
     */
    public static Set<WorkingDays> fromBit(int bitMask) {
        return Arrays.stream(values())
                .filter(day -> (bitMask & day.bit) != 0)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(WorkingDays.class)));
    }


    /**
     * 특정 요일 포함 여부
     */

    public static boolean contains(int bitMask, WorkingDays day) {
        return (bitMask & day.bit) != 0;
    }


    /**
     * util
     */
    public static int weekdays() {
        return MONDAY.bit | TUESDAY.bit | WEDNESDAY.bit | THURSDAY.bit | FRIDAY.bit;
    }

    public static int weekends() {
        return SATURDAY.bit | SUNDAY.bit;
    }

    public static int fullWeek() {
        return weekdays() | weekends();
    }
}


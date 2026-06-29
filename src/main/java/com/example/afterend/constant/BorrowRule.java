package com.example.afterend.constant;

/**
 * 借阅规则常量
 * @author cch
 * @since 2026-06-29
 */
public final class BorrowRule {
    public static final int STUDENT_MAX = 5;
    public static final int TEACHER_MAX = 10;
    public static final int STUDENT_DAYS = 30;
    public static final int TEACHER_DAYS = 60;
    public static final int MAX_RENEW_COUNT = 3;
    public static final double FINE_PER_DAY = 1.00;

    private BorrowRule() {}
}

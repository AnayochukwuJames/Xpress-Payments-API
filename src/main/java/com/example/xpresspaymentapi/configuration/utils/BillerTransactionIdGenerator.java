package com.example.xpresspaymentapi.configuration.utils;

public class BillerTransactionIdGenerator {
    public static long generateTransactionId() {
        return System.currentTimeMillis();
    }
}

package com.savantspender.util;

import com.savantspender.db.entity.TransactionEntity;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;

public class DemoUtil {
    private static final String[] Prefixes = { "STARBUCKS", "KFC", "PIZZA HUT", "MOBIL", "MCDONALDS", "CINEMA", "STEAM", "ONION RINGS WW", "RANDOM CHARGE", "UBER", "Touchstone Climbing" };
    private static final String[] Suffixes = { "//@", "-11a", "SF**POOL**", "GUSTO PAY 123456"};
    private static final double MinAmount = 4.5;
    private static final double MaxAmount = 25.0;

    public static TransactionEntity generateRandom() {
        Random rnd = new Random();
        Calendar curDate = Calendar.getInstance();

        int prefix = Math.abs(rnd.nextInt()) % Prefixes.length;
        int suffix = Math.abs(rnd.nextInt()) % Suffixes.length;
        int day = (Math.abs(rnd.nextInt()) % curDate.get(DATE)) + 1;

        double amount = rnd.nextDouble() * (MaxAmount - MinAmount) + MinAmount;

        curDate.set(DAY_OF_MONTH, day);

        return new TransactionEntity(UUID.randomUUID().toString(), Constants.ManualAccountId, Constants.ManualItemId, Prefixes[prefix] + " " + Suffixes[suffix], amount, false, curDate.getTime());
    }
}

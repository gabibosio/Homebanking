package com.MindHub.homebanking.utils;

import com.MindHub.homebanking.repositories.AccountRepository;
import com.MindHub.homebanking.repositories.CardRepository;

public final class CardUtils {

    private CardUtils() {
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}

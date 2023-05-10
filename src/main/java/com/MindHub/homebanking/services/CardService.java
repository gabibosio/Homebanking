package com.MindHub.homebanking.services;

import com.MindHub.homebanking.models.Card;

public interface CardService {

    void cardSave(Card card);
    Card findById(long id);
    String getCardNumber();
    Card findByNumber(String number);
}

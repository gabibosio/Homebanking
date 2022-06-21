package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.models.Card;
import com.MindHub.homebanking.repositories.CardRepository;
import com.MindHub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.MindHub.homebanking.utils.Util.getRandomNumber;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Override
    public void cardSave(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public String getCardNumber() {
        String card = getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000);
        while(cardRepository.findByNumber(card) != null) {
            card = getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000) + "-"+getRandomNumber(1000,10000);
        }
        return card;
    }


}

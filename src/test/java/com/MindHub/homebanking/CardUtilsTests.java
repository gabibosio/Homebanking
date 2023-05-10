package com.MindHub.homebanking;

import com.MindHub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
public class CardUtilsTests {

    @Test
    public void randomNumberIsCreated(){
        int cardNumber = CardUtils.getRandomNumber(1,10);
        assertThat(cardNumber,isA(Integer.class));
    }

    @Test
    public void randomNumberBigger(){
        int cardNumber = CardUtils.getRandomNumber(1,10);
        assertThat(cardNumber,greaterThan(0));
    }
}

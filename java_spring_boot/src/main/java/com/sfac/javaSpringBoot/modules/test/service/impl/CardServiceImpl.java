package com.sfac.javaSpringBoot.modules.test.service.impl;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.test.entity.Card;
import com.sfac.javaSpringBoot.modules.test.repository.CardRepository;
import com.sfac.javaSpringBoot.modules.test.service.CardService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    @Transactional      //事务注解
    public Result<Card> insertCard(Card card) {
        cardRepository.saveAndFlush(card);
        return new Result<Card>(
                Result.ResultStatus.SUCCESS.status,
                "InsertCard Success!!",card
        );

    }
}

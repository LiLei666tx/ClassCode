package com.sfac.javaSpringBoot.modules.test.service;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.test.entity.Card;

/**
 * 2020/8/12
 */
public interface CardService {

    Result<Card> insertCard(Card card);
}

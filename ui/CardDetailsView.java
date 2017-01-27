package com.lodoss.examples.ui;

import java.util.List;

/**
 *
 */
public interface CardDetailsView {
    int NAME_ON_CARD = 0;
    int CARD_NUMBER = 1;
    int CVV = 2;
    int EXPIRATION_DATE = 3;
    int NICKNAME = 4;

    void updateStepProgress();

    void showSelectExpirationDateDialog(List<Integer> months, List<Integer> years, String defaultMonth, String defaultYear);

    void fillViews(String nameOnCard, String cardNumber, String cvv, String expirationDate, boolean shouldSaveCard);

    void showDepositAmountOnButton(String amountValue, String promoCodeAmount);

    void showCircularRevealAnimation(Runnable onAnimationEndAction);

    void showErrorForField(int field, String errorMessage);

    void hideErrorForField(int field);

    void moveFocusToCvv();

    void showCorrectedCardNumber(String cardNumber);

    void makeButtonActive();

    void makeButtonInActive();
}

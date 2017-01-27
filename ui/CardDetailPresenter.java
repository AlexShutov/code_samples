package com.lodoss.examples.ui;

/**
 * Middleware betweeen {@link CardDetailsView} and business logic
 */
public abstract class CardDetailPresenter extends BasePresenter<CardDetailsView> {
    public CardDetailPresenter(com.myapp.sdk.service.EventBus mBus) {
        super(mBus);
    }

    /**
     * Prepare data for picking expiration dialog, and pass it to {@link CardDetailsView}
     * @param defaultMonth
     * @param defaultYear
     */
    public abstract void onExpirationDateClicked(final String defaultMonth, final String defaultYear);

    /**
     * Handles changes of credit card owner's name
     * @param nameOnCard
     */
    public abstract void onNameOnCardChanged(String nameOnCard);

    /**
     * Handles changes of "Save card" checkmark
     * @param saveCard
     */
    public abstract void onSaveCardCheckmarkChanged(boolean saveCard);

    /**
     * Continue entering data
     */
    public abstract void onEnterBillingAddressClicked(boolean shouldMakeDepositAfterAddingNewCard);

    /**
     * Handle changes on card number
     * @param cardNumber
     */
    public abstract void onCardNumberhanged(String cardNumber);

    /**
     * Handle CVV changes
     * @param cvv
     */
    public abstract void onCvvChanged(String cvv);

    /**
     * Handle expiration date changes
     * @param expirationDate
     */
    public abstract void onExpirationDateChanged(String expirationDate);

    /**
     * Handle nickname changes
     * @param nickname
     */
    public abstract void onNicknameChanged(String nickname);
}

package com.lodoss.examples.ui;

/**
 * {@inheritDoc}
 */
public class CardDetailPresenterImpl extends CardDetailPresenter {

    private ContextWrapper mContextWrapper;
    private Utils mUtils;
    private ValidationUtil mValidationUtil;
    private CardNumberUtil mCardNumberUtil;
    private SdkFacade mSdkFacade;

    public CardDetailPresenterImpl(com.myapp.sdk.service.EventBus mBus,
                                   Utils utils,
                                   ContextWrapper contextWrapper,
                                   ValidationUtil validationUtil,
                                   CardNumberUtil cardNumberUtil,
                                   SdkFacade sdkFacade) {
        super(mBus);
        mUtils = utils;
        mValidationUtil = validationUtil;
        this.mCardNumberUtil = cardNumberUtil;
        this.mContextWrapper = contextWrapper;
        this.mSdkFacade = sdkFacade;
    }


    @Override
    public void attachView(CardDetailsView view) {
        super.attachView(view);
        mView.updateStepProgress();

        String nameOnCard = mDataProvider.getNameOnCard();
        String cardNumber = mDataProvider.getCardNumber();
        String cvv = mDataProvider.getCvv();
        String expirationDate = mDataProvider.getExpirationDate();
        boolean shouldSaveCardFlag = mDataProvider.getShouldSaveCardFlag();
        mView.fillViews(
                nameOnCard,
                cardNumber,
                cvv,
                expirationDate,
                shouldSaveCardFlag
        );
        updateButton();
    }

    @Override
    public void detachView() {
        mView.fillViews("", "", "", "", false);
        super.detachView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExpirationDateClicked(final String defaultMonth, final String defaultYear) {
        mUtils.provideExpirationDateData(defaultMonth, defaultYear)
                .subscribe(new Action1<Pair<List<Integer>, List<Integer>>>() {
                    @Override
                    public void call(Pair<List<Integer>, List<Integer>> container) {
                        mView.showSelectExpirationDateDialog(container.first, container.second, defaultMonth, defaultYear);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //TODO: most probably, there will not be error ever, but anyway, it would be better to handle it
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNameOnCardChanged(String nameOnCard) {
        mDataProvider.putNameOnCard(nameOnCard);

        //TODO move it to base method, and pass just field name
        ValidationResult validationResult = mValidationUtil.validateNameOnCard(nameOnCard);
        if(validationResult.isValid()){
            mView.hideErrorForField(CardDetailsView.NAME_ON_CARD);
        } else {
            mView.showErrorForField(CardDetailsView.NAME_ON_CARD, validationResult.getMessage());
        }
        updateButton();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveCardCheckmarkChanged(boolean saveCard) {
        mDataProvider.putShouldSaveCardFlag(saveCard);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCardNumberhanged(String cardNumber) {
        String numberWithoutDashes = cardNumber.replaceAll("-", "");
        if (mCardNumberUtil.isCardNumberHavingIllegalSymbols(numberWithoutDashes)){
            numberWithoutDashes = mCardNumberUtil.removeIllegalSymbolsFromCardNumber(numberWithoutDashes);
            cardNumber = mCardNumberUtil.removeIllegalSymbolsFromCardNumber(cardNumber);
            /** update view with corrected card number (with dashes) */
            mView.showCorrectedCardNumber(cardNumber);
        }
        mDataProvider.putCardNumber(cardNumber);
        ValidationResult result;
        try {
            result = mValidationUtil.validateCardNumber(numberWithoutDashes);
        }catch (Exception e){
            mView.showErrorForField(CardDetailsView.CARD_NUMBER, mContextWrapper.getString(R.string.text_invalid_card_number));
            return;
        }

        if(result.isValid()){
            mView.hideErrorForField(CardDetailsView.CARD_NUMBER);
        } else {
            mView.showErrorForField(CardDetailsView.CARD_NUMBER, result.getMessage());
        }
        updateButton();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCvvChanged(String cvv) {
        mDataProvider.putCvv(cvv);
        ValidationResult result = mValidationUtil.validateCvv(cvv, mDataProvider.getCardNumberWithoutDashes());
        if(result.isValid()){
            mView.hideErrorForField(CardDetailsView.CVV);
        } else {
            mView.showErrorForField(CardDetailsView.CVV, result.getMessage());
        }
        updateButton();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExpirationDateChanged(String expirationDate) {
        mDataProvider.putExpirationDate(expirationDate);
        ValidationResult result = mValidationUtil.validateExpirationDate(expirationDate);
        if(result.isValid()){
            mView.hideErrorForField(CardDetailsView.EXPIRATION_DATE);
        } else {
            mView.showErrorForField(CardDetailsView.EXPIRATION_DATE, result.getMessage());
        }
        updateButton();
    }

    @Override
    public void onNicknameChanged(String nickname) {
        mDataProvider.putNickname(nickname);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnterBillingAddressClicked(boolean shouldMakeDepositAfterAddingNewCard) {
        boolean isValid;
        ValidationResult cardNumberValidationResult = mValidationUtil.validateCardNumber(mDataProvider.getCardNumberWithoutDashes());
        if(!cardNumberValidationResult.isValid()){
            mView.showErrorForField(CardDetailsView.CARD_NUMBER, cardNumberValidationResult.getMessage());
        }
        ValidationResult cvvValidationResult = mValidationUtil.validateCvv(mDataProvider.getCvv(), mDataProvider.getCardNumberWithoutDashes());
        if(!cvvValidationResult.isValid()){
            mView.showErrorForField(CardDetailsView.CVV, cvvValidationResult.getMessage());
        }
        ValidationResult nameOnCardValidationResult = mValidationUtil.validateNameOnCard(mDataProvider.getNameOnCard());
        if(!nameOnCardValidationResult.isValid()){
            mView.showErrorForField(CardDetailsView.NAME_ON_CARD, nameOnCardValidationResult.getMessage());
        }
        ValidationResult expirationDateValidationResult = mValidationUtil.validateExpirationDate(mDataProvider.getExpirationDate());
        if(!expirationDateValidationResult.isValid()){
            mView.showErrorForField(CardDetailsView.EXPIRATION_DATE, expirationDateValidationResult.getMessage());
        }

        isValid = cardNumberValidationResult.isValid() && cvvValidationResult.isValid()
                && nameOnCardValidationResult.isValid() && expirationDateValidationResult.isValid();

        if(!isValid){
            return;
        }
        mSdkFacade.logEvent(mContextWrapper.getContext(),
                "Add Card Form Complete",
                null,
                mDataProvider.getUserId(),
                mDataProvider.getAdvertisingId(),
                null);

        //if PM shouldn't be saved, then just make a deposit using this credentials
        if(!shouldMakeDepositAfterAddingNewCard && !mDataProvider.getShouldSaveCardFlag()){
            shouldMakeDepositAfterAddingNewCard = true;
        }

        mNavigator.showBillingAddress(shouldMakeDepositAfterAddingNewCard);
    }

    private void updateButton(){
        boolean isValid = mValidationUtil.validateNameOnCard(mDataProvider.getNameOnCard()).isValid();
        isValid &= mValidationUtil.validateCardNumber(mDataProvider.getCardNumberWithoutDashes()).isValid();
        isValid &= mValidationUtil.validateCvv(mDataProvider.getCvv(), mDataProvider.getCardNumberWithoutDashes()).isValid();
        isValid &= mValidationUtil.validateExpirationDate(mDataProvider.getExpirationDate()).isValid();

        if(isValid){
            mView.makeButtonActive();
        } else {
            mView.makeButtonInActive();
        }
    }
}

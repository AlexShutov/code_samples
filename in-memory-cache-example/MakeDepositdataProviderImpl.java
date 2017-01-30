package com.betcade.sdk.data.impl;

import android.location.Location;

import com.betcade.sdk.data.MakeDepositdataProvider;
import com.betcade.sdk.model.Amount;
import com.betcade.sdk.model.Casino;
import com.betcade.sdk.model.Category;
import com.betcade.sdk.model.CountryData;
import com.betcade.sdk.model.GameApplication;
import com.betcade.sdk.model.Incentive;
import com.betcade.sdk.model.PaymentMethod;
import com.betcade.sdk.model.PromoCode;
import com.betcade.sdk.model.User;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by user on 05/04/16.
 */
public class MakeDepositdataProviderImpl implements MakeDepositdataProvider {

    private String orderId;
    private String gameId;
    private String userId;
    private String loginId;
    private String loginType;
    private String userDeviceId;
    private float balance;
    private User user;
    private GameApplication gameApplication;
    private PaymentMethod defaultPaymentMethod;
    private ArrayList<PaymentMethod> paymentMethodList;
    private Amount selectedAmount;
    //    private Double promoCodeAmount;
    private ArrayList<PromoCode> promoCodeList;
    private Location location;
    private Amount currentAmount;
    private Amount customAmount;

    //Billing address stuff
    private CountryData selectedCountry;
    private String houseNumberAndStreet;
    private String flatNoApartment;
    private String townCity;
    private String stateName;
    private String postalCode;

    private String nameOnCard;
    private String cardNumber;
    private String cvv;
    private String expirationDate;
    private String nickname;
    private boolean shouldSaveCard;

    private int promoCodeCount;
    private PaymentMethod selectedPaymentMethod;

    private ArrayList<CountryData> countryList;
    private List<String> countryStringList;
    private String extCasinoTransId;
    private String extUserId;
    private String extCasinoPaymentGatewayId;
    private String phone;
    private String email;
    private String dateOfBirth;
    private String refPLOrderId;
    private String billingTransactionOrderId;
    private boolean isBillingTransactionAddingCard;
    private List<Incentive> incentiveList;
    private String deviceId;
    private String accessCountryCode;

    private boolean isCustomAmountAllowed;
    private boolean isUserLimited;
    private double maximumDepositValue;
    private double minimumDepositValue;
    private ArrayList<Amount> casinoCustomPresetAmountList;
    private float extCasinoAmount;
    private String extCasinoCurrency;
    private HashMap<String, String> currencyCodeMapper;

    public MakeDepositdataProviderImpl(){
        currencyCodeMapper = new HashMap<String, String>();
        currencyCodeMapper.put("GBP", "£");
        currencyCodeMapper.put("USD", "$");
    }

    @Override
    public String getGameId() {
        return (gameId != null) ? gameId : "";
    }

    @Override
    public void putGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String getUserId() {
        return (userId != null) ? userId : "";
    }

    @Override
    public void putUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void putUser(User user) {
        this.user = user;
    }

    @Override
    public GameApplication getGameApplication() {
        return gameApplication;
    }

    @Override
    public void putGameApplication(GameApplication gameApp) {
        this.gameApplication = gameApp;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        for(PaymentMethod pm : paymentMethodList){
            if(pm.getDefaultPaymentInfo() != null && pm.getDefaultPaymentInfo()){
                return pm;
            }
        }
        return defaultPaymentMethod;
    }

    @Override
    public void putPaymentMethod(PaymentMethod pm) {
        defaultPaymentMethod = pm;
    }

    @Override
    public Amount getSelectedAmount() {
        return selectedAmount;
    }

    @Override
    public void putSelectedAmount(Amount a) {
        selectedAmount = a;
    }

    @Override
    public List<PromoCode> getPromoCodeAmountList() {
        if(this.promoCodeList == null) {
            return new ArrayList<PromoCode>();
        }
        return this.promoCodeList;
    }

    @Override
    public void putPromoCodeAmountList(List<PromoCode> list) {
        if(this.promoCodeList == null) {
            promoCodeList = new ArrayList<PromoCode>();
        }

        if(list == null){
            promoCodeList.clear();
            return;
        }
        promoCodeList.addAll(list);
    }

    @Override
    public Double getPromoCodeSumm() {
        Double result = 0d;
        if(promoCodeList != null && promoCodeList.size() > 0){
            for(int i = 0; i < promoCodeList.size(); i++){
                result += promoCodeList.get(i).getBonusAmount();
            }
        }
        return result;
    }

    @Override
    @Deprecated
    public void putPromoCodeAmountSum(Double sum) {

    }

    @Override
    public void addPromoCodeAmount(PromoCode promoCodeAmount) {
        if(this.promoCodeList == null) {
            this.promoCodeList = new ArrayList<PromoCode>();
        }
        this.promoCodeList.add(promoCodeAmount);
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public void putBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public void putAmount(Amount amount) {
        currentAmount = amount;
    }

    @Override
    public Amount getCurrentAmount() {
        return currentAmount;
    }

    @Override
    public void putCustomAmount(Amount amount) {
        customAmount = amount;
    }

    @Override
    public Amount getCustomAmount() {
        return customAmount;
    }

    @Override
    public void putSelectedCountry(CountryData country) {
        this.selectedCountry = country;
    }

    @Override
    public CountryData getCountry() {
        if(selectedCountry == null && countryList != null){
            selectedCountry = countryList.get(0);
        }
        return selectedCountry;
    }

    @Override
    public void putHouseNoAndStreet(String houseNoAndStreet) {
        this.houseNumberAndStreet = houseNoAndStreet;
    }

    @Override
    public String getHouseNoAndStreet() {
        return houseNumberAndStreet;
    }

    @Override
    public void putFlatNoApartment(String flatNoApartment) {
        this.flatNoApartment = flatNoApartment;
    }

    @Override
    public String getFlatNoApartment() {
        return flatNoApartment;
    }

    @Override
    public void putTownCity(String townCity) {
        this.townCity = townCity;
    }

    @Override
    public String getTownCity() {
        return townCity;
    }

    @Override
    public String getStateName() {
        return stateName;
    }

    @Override
    public void putStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public void putPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getPostalCode() {
        return this.postalCode;
    }

    @Override
    public void putNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    @Override
    public String getNameOnCard() {
        return this.nameOnCard;
    }

    @Override
    public void putCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String getCardNumber() {
        return this.cardNumber;
    }
    @Override
    public String getCardNumberWithoutDashes() {
        if(StringUtils.isEmpty(this.cardNumber)){
            return "";
        }

        return this.cardNumber.replaceAll("-","");
    }

    @Override
    public void putCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String getCvv() {
        return this.cvv;
    }

    @Override
    public void putExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public void putNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() {
        if(nickname == null){
            return "";
        }
        return nickname;
    }

    @Override
    public void putShouldSaveCardFlag(boolean shouldSaveCardFlag) {
        this.shouldSaveCard = shouldSaveCardFlag;
    }

    @Override
    public boolean getShouldSaveCardFlag() {
        return this.shouldSaveCard;
    }

    @Override
    public void increaseUsedPromoCodeCount() {
        promoCodeCount++;
    }

    @Override
    public int getPromoCodeCount() {
        return promoCodeCount;
    }

    @Override
    public void putPromoCodeCount(int promoCodeCount) {
        this.promoCodeCount = promoCodeCount;
    }

    @Override
    public List<CountryData> getCountryList() {
        if(countryList == null){
            return new ArrayList<CountryData>();
        }
        return countryList;
    }

    @Override
    public List<String> getCountryStringList() {
        ArrayList<String> list = new ArrayList<String>();
        if(countryStringList != null) {
            list.addAll(countryStringList);
        }
        return list;
    }

    @Override
    public void putCountryList(List<CountryData> countryList) {
        if(this.countryList == null){
            this.countryList = new ArrayList<CountryData>();
        }
        if(countryList != null) {
            this.countryList.clear();
            this.countryList.addAll(countryList);
        }
    }

    @Override
    public void putCountryStringList(List<String> countryList) {
        countryStringList = countryList;
    }

    @Override
    public List<PaymentMethod> getPaymentMethodList() {
        List<PaymentMethod> result = new ArrayList<PaymentMethod>();
        if(paymentMethodList != null){
            result.addAll(paymentMethodList);
        }
        return result;
//        return (paymentMethodList != null) ? paymentMethodList : new ArrayList<PaymentMethod>();
    }

    @Override
    public void addPaymentMethodToList(PaymentMethod pm) {
        if(paymentMethodList == null){
            paymentMethodList = new ArrayList<PaymentMethod>();
        }

        paymentMethodList.add(0, pm);
    }

    @Override
    public void putPaymentMethodList(List<PaymentMethod> pmList) {
        if(paymentMethodList == null){
            paymentMethodList = new ArrayList<PaymentMethod>();
        }
        paymentMethodList.clear();
        if(pmList != null) {
            paymentMethodList.addAll(pmList);
        }
    }

    @Override
    public PaymentMethod getSelectedPaymentMethod() {
        if(selectedPaymentMethod != null){
            return selectedPaymentMethod;
        }
        for(PaymentMethod pm : paymentMethodList){
            if(pm.getDefaultPaymentInfo() != null && pm.getDefaultPaymentInfo()){
                selectedPaymentMethod = pm;
                return selectedPaymentMethod;
            }
        }
        if(paymentMethodList.size() > 0){
            return paymentMethodList.get(0);
        }
        return null;
    }

    @Override
    public void putSelectedPaymentMethod(PaymentMethod pm) {
        selectedPaymentMethod = pm;
    }

    @Override
    public String getLoginType() {
        return loginType;
    }

    @Override
    public void putLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public String getLoginId() {
        return loginId;
    }

    @Override
    public void putLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String getExtCasinoTransId() {
        if(extCasinoTransId == null){
            return "";
        }
        return extCasinoTransId;
    }

    @Override
    public void putExtCasinoPaymentGatewayId(String extCasinoPaymentGatewayId) {
        this.extCasinoPaymentGatewayId = extCasinoPaymentGatewayId;
    }

    @Override
    public String getExtCasinoPaymentGatewayId() {
        if(extCasinoPaymentGatewayId == null){
            return "";
        }
        return extCasinoPaymentGatewayId;
    }

    @Override
    public void putExtCasinoTransId(String extCasinoTransId) {
        this.extCasinoTransId = extCasinoTransId;
    }

    @Override
    public String getExtCasinoUserId() {
        if(extUserId == null){
            return "";
        }
        return extUserId;
    }

    @Override
    public void putExtCasinoUserId(String extCasinoUserId) {
        extUserId = extCasinoUserId;
    }

    @Override
    public String getAdvertisingId() {
        return null;
    }

    @Override
    public void putPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPhone() {
        if(phone == null){
            return "";
        }
        return phone;
    }

    @Override
    public String getUnformattedPhone() {
        if(phone == null){
            return "";
        }
        return phone.replaceAll("[^0-9]", "").trim();
    }

    @Override
    public void putEmail(String email) {
        this.email = email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void putDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getDateOfBirthPayletterFormat() {
        if(StringUtils.isNotEmpty(dateOfBirth)){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat plSdf = new SimpleDateFormat("yyyyMMdd");
            try {
                String result = plSdf.format(sdf.parse(dateOfBirth));
                return result;
            } catch (ParseException e) {
                return "";
            }
        }
        return "";
    }

    @Override
    public String getDateOfBirthKycFormat() {
        if(StringUtils.isNotEmpty(dateOfBirth)){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat plSdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String result = plSdf.format(sdf.parse(dateOfBirth));
                return result;
            } catch (ParseException e) {
                return "";
            }
        }
        return "";
    }

    @Override
    public boolean isKYCInfoFullFilled() {
        return StringUtils.isNotEmpty(dateOfBirth) && StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(email);
    }

    @Override
    public void putIncentives(List<Incentive> incentives) {
        if(incentives == null){
            return;
        }
        incentiveList = incentives;
    }

    @Override
    public List<Incentive> getIncentives() {
        if(incentiveList == null){
            return new ArrayList<Incentive>();
        }
        return incentiveList;
    }

    @Override
    public Double getTotalIncentiveAmount() {
        Double result = 0d;
        if(incentiveList != null && incentiveList.size() > 0){
            for(int i = 0; i < incentiveList.size(); i++){
                Incentive incentive = incentiveList.get(i);
                if(incentive.getType().equals(Incentive.TYPE_FIXED)) {
                    result += incentive.getRedeemAmount();
                }
            }
        }
        return result;
    }

    @Override
    public Double getTotalIncentivePercent() {
        Double result = 0d;
        if(incentiveList != null && incentiveList.size() > 0){
            for(int i = 0; i < incentiveList.size(); i++){
                Incentive incentive = incentiveList.get(i);
                if(incentive.getType().equals(Incentive.TYPE_PERCENT)) {
                    result += incentive.getRedeemAmount();
                }
            }
        }
        return result;
    }

    @Override
    public int getIncentiveCount() {
        if(incentiveList == null){
            return 0;
        }
        return incentiveList.size();
    }

    @Override
    public void putDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getDeviceId() {
        if(deviceId == null){
            return "";
        }
        return deviceId;
    }

    @Override
    public void putIsCustomAmountAllowed(boolean isCustomAmountAllowed) {
        this.isCustomAmountAllowed = isCustomAmountAllowed;
    }

    @Override
    public boolean isCustomAmountAllowed() {
        return isCustomAmountAllowed;
    }

    @Override
    public void putCustomPresetAmountList(ArrayList<Amount> list) {
        casinoCustomPresetAmountList = list;
    }

    @Override
    public ArrayList<Amount> getPresetAmountList() {
        if((gameApplication == null || gameApplication.getPresetPriceList() == null || gameApplication.getPresetPriceList().size() == 0) &&
                (casinoCustomPresetAmountList == null || casinoCustomPresetAmountList.size() == 0)){
            return new ArrayList<Amount>();
        }

        ArrayList<Amount> result = new ArrayList<Amount>();
        final int PRESET_LIMIT = isCustomAmountAllowed ? 4 : 5;


        if(casinoCustomPresetAmountList == null || casinoCustomPresetAmountList.size() == 0){
            List<Double> presetPriceList = gameApplication.getPresetPriceList();

            for(int i = 0; (i < presetPriceList.size() && i < PRESET_LIMIT); i++){
                Double value = presetPriceList.get(i);
                if(value <= maximumDepositValue && value >= minimumDepositValue){
                    Amount amount = new Amount();
                    amount.setId(String.valueOf(value));
                    amount.setAmountValue(String.valueOf(value));
                    result.add(amount);
                }
            }
        } else {
            List<Amount> amounts = casinoCustomPresetAmountList.subList(0, Math.min(isCustomAmountAllowed ? 4 : 5, casinoCustomPresetAmountList.size()));
            List<Amount> res = Observable.from(amounts)
                    .filter(new Func1<Amount, Boolean>() {
                        @Override
                        public Boolean call(Amount amount) {
                            double value = Double.parseDouble(amount.getAmountValue());
                            return value >= minimumDepositValue && value <= maximumDepositValue;
                        }
                    })
                    .toList()
                    .toBlocking()
                    .first();

            result.addAll(res);
        }

        if(isCustomAmountAllowed){
            Amount a = new Amount();
            a.setAmountValue(Amount.CUSTOM_AMOUNT_FILLING);
            a.setAmountCurrency(Amount.CURRENCY_GBP);
            a.setId(Amount.TYPE_CUSTOM);
            result.add(a);
        }
        return result;
    }

    @Override
    public void putMaximumDepositValue(double maximumDepositValue) {
        this.maximumDepositValue = maximumDepositValue;
    }

    @Override
    public double getMaximumDepositValue() {
        return maximumDepositValue;
    }

    @Override
    public void putIsUserLimited(boolean isUserLimited) {
        this.isUserLimited = isUserLimited;
    }

    @Override
    public boolean isUserLimited() {
        return isUserLimited;
    }

    @Override
    public void putMinimumDepositValue(double min) {
        minimumDepositValue = min;
    }

    @Override
    public double getMinimumDepositValue() {
        return minimumDepositValue;
    }

    @Override
    public List<String> getCategoryNames() {
        List<String> result = new ArrayList<String>();
        if(gameApplication == null || gameApplication.getCategories() == null ||
                gameApplication.getCategories().size() == 0){
            return result;
        }
        List<Category> categories = gameApplication.getCategories();
        for(Category c : categories){
            result.add(c.getName());
        }

        return result;
    }

    @Override
    public String getCasinoId() {
        if(gameApplication == null){
            return "";
        }
        Casino casino = gameApplication.getCasino();
        if(casino == null){
            return "";
        }

        String casinoId = casino.getId();
        if(casinoId == null){
            return "";
        }
        return casinoId;
    }

    @Override
    public void putAccessCountryCode(String cc) {
        accessCountryCode = cc;
    }

    @Override
    public String getAccessCountryCode() {
        return accessCountryCode;
    }

    @Override
    public void putExtCasinoAmount(float extCasinoAmount) {
        this.extCasinoAmount = extCasinoAmount;
    }

    @Override
    public float getExtCasinoAmount() {
        return this.extCasinoAmount;
    }

    @Override
    public void putExtCasinoCurrency(String currency) {
        this.extCasinoCurrency = currency;
    }

    @Override
    public String getCurrencySymbol() {
        if(StringUtils.isEmpty(this.extCasinoCurrency) || !currencyCodeMapper.containsKey(this.extCasinoCurrency)){
            return currencyCodeMapper.get("GBP");
        }
        return currencyCodeMapper.get(this.extCasinoCurrency);

    }

    @Override
    public String getExtCasinoCurrency() {
        if(this.extCasinoCurrency == null){
            return "GBP";
        }
        return this.extCasinoCurrency;
    }

    @Override
    public void putOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getOrderId() {
        return this.orderId;
    }

    @Override
    public void putRefPLOrderId(String refPLOrderId) {
        this.refPLOrderId = refPLOrderId;
    }

    @Override
    public String getRefPLOrderId() {
        return this.refPLOrderId;
    }

    @Override
    public void putBillingTransactionOrderId(String orderId) {
        this.billingTransactionOrderId = orderId;
    }

    @Override
    public String getBillingTransactionOrderId() {
        if(StringUtils.isNotEmpty(this.billingTransactionOrderId)){
            return this.billingTransactionOrderId;
        }
        return "";
    }

    @Override
    public void putBillingTransactionType(boolean isAddingCard) {
        isBillingTransactionAddingCard = isAddingCard;
    }

    @Override
    public boolean isBillingTransactionAddingNewCard() {
        return isBillingTransactionAddingCard;
    }
}
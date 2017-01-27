package com.lodoss.examples.ui;

/**
 * Created by user on 27/01/17.
 */

public class CardDetailsFragment {
    public static final String TAG = CardDetailsFragment.class.getSimpleName() + "Tag";
    private static final String LOG_TAG = CardDetailsFragment.class.getSimpleName();

    public static final String ARG_SHOULD_MAKE_DEPOSIT_AFTER_ADDING_NEW_CARD = "arg_should_make_deposit_after_adding_new_card";

    public static final String ARG_SHOULD_DIALOG_BE_RECREATED = "arg_should_dialog_be_recreated";
    public static final String ARG_YEAR_FROM_DIALOG = "arg_year_from_dialog";
    public static final String ARG_MONTH_FROM_DIALOG = "arg_month_from_dialog";
    public static final String ARG_IS_FIRST_START = "is_first_start";

    @Inject
    public CardDetailPresenter pres;

    @Inject
    public MakeDepositNavigator navigator;

    @Inject
    public UiUtil uiUtil;

    @Inject
    public MakeDepositdataProvider makeDepositdataProvider;

    @Inject
    public EventBus bus;

    @Inject
    public ContextWrapper mContextWrapper;

    private ViewHolder mViewHolder;
    private CustomDialog mExpDatePickerDialog;
    private boolean shouldMakeDepositAfterAddingNewCard;

    private boolean shouldRecreateDialog;
    private DatePickerAdapter monthAdapter;
    private DatePickerAdapter yearAdapter;
    private WheelView monthPickerView;
    private WheelView yearPickerView;
    private String month;
    private String year;

    private TextView[] errorTextArray;
    private EditText[] textArray;
    private View[] underlineArray;
    private Drawable errorDrawable;
    private CustomDialog errorView;


    private TextWatcher nameOnCardTextWatcher;
    private TextWatcher cardNumberTextWatcher1;
    private TextWatcher cardNumberTextWatcher2;
    private TextWatcher cvvTextWatcher;
    private TextWatcher expirationDateTextWatcher;
    private TextWatcher nicknameTextWatcher;
    private TextWatcher allFieldsAreFilled;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
    private boolean isFirstStart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isFirstStart = getArguments().getBoolean(ARG_IS_FIRST_START, true);
        if(isFirstStart) {
            getArguments().putBoolean(ARG_IS_FIRST_START, false);
        }

        Bundle arguments = getArguments();
        shouldMakeDepositAfterAddingNewCard = arguments != null && arguments.getBoolean(ARG_SHOULD_MAKE_DEPOSIT_AFTER_ADDING_NEW_CARD, false);

        mViewHolder = new ViewHolder(view);

        errorDrawable = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ?
                getResources().getDrawable(R.drawable.ic_error, null) :
                getResources().getDrawable(R.drawable.ic_error);
        errorDrawable.setBounds(60,0, 0, 60);

        textArray = new EditText[]{
                mViewHolder.nameOnCard,
                mViewHolder.cardNumber,
                mViewHolder.cvv,
                mViewHolder.expirationDate,
                mViewHolder.nickname
        };

        errorTextArray = new TextView[]{
                mViewHolder.nameOnCardErrorText,
                mViewHolder.cardNumberErrorText,
                mViewHolder.cvvErrorText,
                mViewHolder.expirationDateErrorText,
                mViewHolder.nicknameErrorText
        };

        underlineArray = new View[]{
                mViewHolder.nameOnCardUnderline,
                mViewHolder.cardNumberUnderline,
                mViewHolder.cvvUnderline,
                mViewHolder.expirationDateUnderline,
                mViewHolder.nicknameUnderline
        };

        ((HasComponent<ActivityComponent>) getActivity())
                .getComponent().inject(this);

        uiUtil.applyFilterToEditText(mViewHolder.expirationDate, new ExpirationDateFilter());

        pres.setMakeDepositDataProvider(makeDepositdataProvider);
        pres.setMakeDepositNavigator(navigator);

        if(!shouldMakeDepositAfterAddingNewCard){
            mViewHolder.btnConfirm.setText(mContextWrapper.getString(R.string.text_continue).toUpperCase());
        }


    }

    @Override
    public void showCorrectedCardNumber(String cardNumber) {
        mViewHolder.cardNumber.setText(cardNumber);
        if (!cardNumber.isEmpty()) {
            mViewHolder.cardNumber.setSelection(cardNumber.length());
        }
    }

    @Override
    public void makeButtonActive() {
        mViewHolder.btnConfirm.setBackgroundResource(R.drawable.button_background);
    }

    @Override
    public void makeButtonInActive() {
        mViewHolder.btnConfirm.setBackgroundResource(R.drawable.button_disabled_background);
    }

    public static CardDetailsFragment newInstance(boolean shouldMakeDepositAfterAddingNewCard){
        CardDetailsFragment f = new CardDetailsFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOULD_MAKE_DEPOSIT_AFTER_ADDING_NEW_CARD, shouldMakeDepositAfterAddingNewCard);
        f.setArguments(args);
        return f;
    }

    @Override
    public void updateStepProgress() {
        HasPresenter<MakeDepositContainerPresenter> activity = (HasPresenter<MakeDepositContainerPresenter>) getActivity();
        if(activity != null) {
            MakeDepositContainerPresenter presenter = activity.getPresenter();
            presenter.onStepProgressChanged(MakeDepositContainerPresenter.STEP_3);
        }
    }

    @Override
    public void showSelectExpirationDateDialog(List<Integer> months, List<Integer> years, String defaultMonth, String defaultYear) {
        if(mExpDatePickerDialog != null && mExpDatePickerDialog.isShowing()){
            return;
        }
        mExpDatePickerDialog = new CustomDialog.Builder()
                .shouldHideDivider(false)
                .withTitleResId(R.string.text_select_expiration_date)
                .withContent(R.layout.dialog_expiration_date)
                .withCenterButtonTextResId(R.string.text_cancel)
                .withRightButtonTextResId(R.string.text_add)
                .build(getActivity());

        View datePickerView = mExpDatePickerDialog.getCustomContentView();
        monthPickerView = (WheelView) datePickerView.findViewById(R.id.month_picker);
        yearPickerView = (WheelView) datePickerView.findViewById(R.id.year_picker);

        monthAdapter = new DatePickerAdapter(mContextWrapper.getContext(), months);
        yearAdapter = new DatePickerAdapter(mContextWrapper.getContext(), years);

        monthPickerView.setViewAdapter(monthAdapter);
        yearPickerView.setViewAdapter(yearAdapter);

        monthPickerView.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH));
        setupWhhelView(monthPickerView);
        setupWhhelView(yearPickerView);

        mExpDatePickerDialog.getRightButton().setTextColor(getResources().getColor(R.color.theme_accent));

        mExpDatePickerDialog.show();
        mExpDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mExpDatePickerDialog.setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month = getMonth();
                String year = getYear();
                mViewHolder.expirationDate.setText(month + "/" + year.substring(2, 4));

                //create function for that
                dismissDialog();
            }
        });

        mExpDatePickerDialog.setOnCenterButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        if(defaultMonth != null && defaultMonth.length() > 0){
            int monthNumber = Integer.parseInt(defaultMonth);
            Integer monthIndex = months.indexOf(monthNumber);
            monthPickerView.setCurrentItem(monthIndex, false);
        }

        if(defaultYear != null && defaultYear.length() > 0){
            int yearNumber = Integer.parseInt(defaultYear);
            Integer yearIndex = years.indexOf(yearNumber);
            yearPickerView.setCurrentItem(yearIndex, false);
        }
    }

    @Override
    public void fillViews(String nameOnCard, String cardNumber, String cvv, String expirationDate, boolean shouldSaveCard) {
        if(nameOnCard != null) {
            mViewHolder.nameOnCard.setText(nameOnCard);
        }
        if(cardNumber != null) {
            mViewHolder.cardNumber.setText(cardNumber);
        }
        if(cvv != null) {
            mViewHolder.cvv.setText(cvv);
        }
        if(expirationDate != null){
            mViewHolder.expirationDate.setText(expirationDate);
        }

        mViewHolder.saveCardCheckmark.setOnCheckedChangeListener(null);
        mViewHolder.saveCardCheckmark.setChecked(shouldSaveCard);
        mViewHolder.saveCardCheckmark.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    public void showDepositAmountOnButton(String amountValue, String promoCodeAmount) {
        mViewHolder.btnConfirm.setText(mContextWrapper.getString(R.string.text_continue).toUpperCase());
    }

    @Override
    public void showCircularRevealAnimation(Runnable onAnimationEndAction) {
        MakeDepositContainerView view = (MakeDepositContainerView) getActivity();
        view.showCircularReveal(mViewHolder.btnConfirm, onAnimationEndAction);
        view.showTitle(getString(R.string.text_confirming_deposit));
    }

    @Override
    public void showErrorForField(int field, String errorMessage) {
        errorTextArray[field].setText(errorMessage);
        errorTextArray[field].setVisibility(View.VISIBLE);
        underlineArray[field].setBackgroundResource(R.color.error_color_dark_blue);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            textArray[field].setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }else {
            textArray[field].setCompoundDrawables(null, null, errorDrawable, null);
        }
    }

    @Override
    public void hideErrorForField(int field) {
        textArray[field].setCompoundDrawables(null, null, null, null);
        errorTextArray[field].setText("");
        errorTextArray[field].setVisibility(View.GONE);
        underlineArray[field].setBackgroundResource(R.color.text_color_preset_amount);
    }

    @Override
    public void moveFocusToCvv() {
        mViewHolder.cvv.requestFocus();
    }

    private void setupWhhelView(WheelView pickerView){
        pickerView.setWheelBackground(R.drawable.wheel_bg_holo);
        pickerView.setWheelForeground(R.drawable.wheel_val_holo);
        pickerView.setVisibleItems(3);
        pickerView.setDrawShadows(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewHolder.nickname.requestFocus();
        uiUtil.showKeyboard(getActivity(), mViewHolder.nickname);
    }


    private String extractMonthFromExpDate(){
        String expDate = mViewHolder.expirationDate.getText().toString();
        if(expDate.length() == 0 || !expDate.contains("/")){
            return "";
        }

        String[] parts = expDate.split("/");
        if(parts.length != 2){
            return "";
        }

        return parts[0];
    }

    private String extractYearFromExpDate(){
        String expDate = mViewHolder.expirationDate.getText().toString();
        if(expDate.length() == 0 || !expDate.contains("/")){
            return "";
        }

        String[] parts = expDate.split("/");
        if(parts.length != 2){
            return "";
        }

        return "20" + parts[1];
    }

    private String getMonth(){
        String month = "";
        if(monthAdapter != null && monthPickerView != null) {
            month = (String) monthAdapter.getItemText(monthPickerView.getCurrentItem());
            if (month.length() == 1) {
                month = "0" + month;
            }
        }

        return month;
    }

    private String getYear(){
        if(yearAdapter == null || yearPickerView == null){
            return "";
        }

        return (String) yearAdapter.getItemText(yearPickerView.getCurrentItem());
    }

    private void tryToHandleDialogOnPause(){
        if(mExpDatePickerDialog != null && mExpDatePickerDialog.isShowing()){
            shouldRecreateDialog = true;
            month = getMonth();
            year = getYear();
            dismissDialog();
        }else {
            shouldRecreateDialog = false;
        }
    }

    @Override
    public void onPause() {
        tryToHandleDialogOnPause();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!bus.isRegistered(this)) {
            bus.register(this);
        }
        pres.attachView(this);
        initWatchers();
        initListeners();
    }

    @Override
    public void onStop() {
        removeListeners();
        // saveFields();
        pres.detachView();
        super.onStop();

        if(bus.isRegistered(this)){
            bus.unregister(this);
        }
    }

    private void dismissDialog(){
        if(mExpDatePickerDialog != null) {
            mExpDatePickerDialog.dismiss();
            mExpDatePickerDialog = null;
        }
    }

    private void saveVariables(Bundle outState){
        outState.putBoolean(ARG_SHOULD_DIALOG_BE_RECREATED, shouldRecreateDialog);
        outState.putString(ARG_MONTH_FROM_DIALOG, month);
        outState.putString(ARG_YEAR_FROM_DIALOG, year);
    }

    private void restoreState(Bundle savedInstanceState){
        if(savedInstanceState == null){
            return;
        }

        if(savedInstanceState.containsKey(ARG_MONTH_FROM_DIALOG)){
            month = savedInstanceState.getString(ARG_MONTH_FROM_DIALOG);
            savedInstanceState.remove(ARG_MONTH_FROM_DIALOG);
        }
        if(savedInstanceState.containsKey(ARG_YEAR_FROM_DIALOG)){
            year = savedInstanceState.getString(ARG_YEAR_FROM_DIALOG);
            savedInstanceState.remove(ARG_YEAR_FROM_DIALOG);
        }
        if(savedInstanceState.containsKey(ARG_SHOULD_DIALOG_BE_RECREATED)){
            shouldRecreateDialog = savedInstanceState.getBoolean(ARG_SHOULD_DIALOG_BE_RECREATED, false);
            savedInstanceState.remove(ARG_SHOULD_DIALOG_BE_RECREATED);

            if(shouldRecreateDialog){
                shouldRecreateDialog = false;
                pres.onExpirationDateClicked(month, year);
            }
        }
    }

    //    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
//        super.onRestoreState(savedInstanceState);
        restoreState(savedInstanceState);
    }

    //    @Override
    protected void onSaveState(Bundle outState) {
        saveVariables(outState);
//        super.onSaveState(outState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessEvent(SuccessEvent e){
        fillViews("", "", "", "", false);
    }

    @Override
    public void clean() {
    }

    private boolean isEntryFieldsEmpty(){
        if (null == mViewHolder){
            return true;
        }
        boolean isEmpty = true;
        String expiration_date = mViewHolder.expirationDate.getText().toString();
        isEmpty &= expiration_date.equals("");
        String nameOnCard = mViewHolder.nameOnCard.getText().toString();
        isEmpty &= nameOnCard.equals("");
        String card_number = mViewHolder.cardNumber.getText().toString();
        isEmpty &= card_number.equals("");
        String cvv = mViewHolder.cvv.getText().toString();
        isEmpty &= cvv.equals("");

        return isEmpty;
    }

    public static final class ViewHolder{

        public View nameOnCardContainer;
        public EditText expirationDate;
        public EditText nameOnCard;
        public EditText cardNumber;
        public EditText cvv;
        public EditText nickname;
        public CheckBox saveCardCheckmark;
        public Button btnConfirm;

        public View nameOnCardUnderline;
        public View cardNumberUnderline;
        public View cvvUnderline;
        public View expirationDateUnderline;
        public View nicknameUnderline;

        public TextView nameOnCardErrorText;
        public TextView cvvErrorText;
        public TextView cardNumberErrorText;
        public TextView expirationDateErrorText;
        public TextView nicknameErrorText;

        public ViewHolder(View rootView){
            expirationDate = (EditText) rootView.findViewById(R.id.expiration_date);
            nameOnCard = (EditText) rootView.findViewById(R.id.name_on_card);
            cardNumber = (EditText) rootView.findViewById(R.id.card_number);
            cvv = (EditText) rootView.findViewById(R.id.cvv);
            nickname = (EditText) rootView.findViewById(R.id.nickname);
            saveCardCheckmark = (CheckBox) rootView.findViewById(R.id.check_save_card);
            btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm_deposit);

            nameOnCardUnderline = rootView.findViewById(R.id.name_on_card_underline);
            cardNumberUnderline = rootView.findViewById(R.id.card_number_underline);
            cvvUnderline = rootView.findViewById(R.id.cvv_divider);
            expirationDateUnderline = rootView.findViewById(R.id.expiration_date_underline);
            nicknameUnderline = rootView.findViewById(R.id.nickname_underline);

            nameOnCardErrorText = (TextView) rootView.findViewById(R.id.name_on_card_error_text);
            cvvErrorText = (TextView) rootView.findViewById(R.id.cvv_error_text);
            cardNumberErrorText = (TextView) rootView.findViewById(R.id.card_number_error_text);
            expirationDateErrorText = (TextView) rootView.findViewById(R.id.expiration_date_error_text);
            nicknameErrorText = (TextView) rootView.findViewById(R.id.nickname_error_text);

            nameOnCardContainer = rootView.findViewById(R.id.name_on_card_container);
        }

    }
}

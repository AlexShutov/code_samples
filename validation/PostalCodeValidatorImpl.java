package com.lodoss.examples.validation;

import android.content.ContextWrapper;
import android.text.TextUtils;

import com.lodoss.examples.R;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@inheritDoc}
 */
public class PostalCodeValidatorImpl implements PostalCodeValidator {
    private ContextWrapper cw;

    public PostalCodeValidatorImpl(ContextWrapper cw){
        this.cw = cw;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostalCodeValidationResult validatePostalCode(CountryData countryName, String postalCode) {
        Map<String, String> rulesMap = new HashMap<String, String>();
        rulesMap.put("United Kingdom", "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$");
        rulesMap.put("United States", "^[0-9]{5}(?:-[0-9]{4})?$");

        String PATTERN;
        if(rulesMap.containsKey(countryName.getName())) {
            PATTERN = rulesMap.get(countryName.getName());
        }else {
            if(postalCode != null && postalCode.length() >= 5 && TextUtils.isDigitsOnly(postalCode)){
                return new PostalCodeValidationResult(true, "", cw);
            } else {
                return new PostalCodeValidationResult(false, cw.getString(R.string.text_postal_code_is_invalid), cw);
            }
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(postalCode);

        boolean isValid = matcher.matches();

        PostalCodeValidationResult result = new PostalCodeValidationResult();
        result.setIsValid(isValid);
        if(isValid){
            result.withMessage("");
        } else {
            result.setMessage(cw.getString(R.string.text_postal_code_is_invalid));
        }

        return result;
    }
}

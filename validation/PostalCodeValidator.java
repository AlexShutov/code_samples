package com.lodoss.examples.validation;

/**
 * Example of extracting validation logic to standalone component
 * to make it reusable
 */

public interface PostalCodeValidator {

    /**
     * Validates postal code by {@link CountryData}
     * @param countryName - container for country - related data, i.e. id, name, country code, etc.
     * @param postalCode - postal / ZIP code, which should be validated
     * @return
     */
    PostalCodeValidationResult validatePostalCode(CountryData countryName, String postalCode);
}

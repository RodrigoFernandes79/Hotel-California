package com.challenge.hotel_california.validatorRefactor;

import com.challenge.hotel_california.DTOs.CustomerEntryDTO;

public interface IValidator {
    void verifyValidators(CustomerEntryDTO customerEntryDTO);
}

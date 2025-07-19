/*
 * Copyright (C) 2025 NotYourAverageDev
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.notyouraverage.base.validations.validators;

import dev.notyouraverage.base.validations.annotations.ValidDateRange;
import dev.notyouraverage.commons.utils.PropertyUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String beforeFieldName;

    private String afterFieldName;

    @Override
    public void initialize(ValidDateRange validDateRange) {
        beforeFieldName = validDateRange.before();
        afterFieldName = validDateRange.after();
        ConstraintValidator.super.initialize(validDateRange);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            if (value == null)
                return true;

            final LocalDate beforeDate = (LocalDate) PropertyUtils.getProperty(value, beforeFieldName);
            final LocalDate afterDate = (LocalDate) PropertyUtils.getProperty(value, afterFieldName);

            if (beforeDate == null || afterDate == null)
                return true;
            return beforeDate.isEqual(afterDate) || beforeDate.isBefore(afterDate);

        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return false;
        }
    }
}

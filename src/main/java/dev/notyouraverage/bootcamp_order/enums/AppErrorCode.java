package dev.notyouraverage.bootcamp_order.enums;

import dev.notyouraverage.base.constants.Constants;
import dev.notyouraverage.base.dtos.response.wrapper.ErrorCodeTrait;
import lombok.Getter;

@Getter
public enum AppErrorCode implements ErrorCodeTrait {
    USER_NOT_FOUND("001", "User with the provided ID not found"),
    ORDER_LIFECYCLE_NOT_FOUND("002", "Order Lifecycle with the provided order ID not found");

    private final String code;

    private final String message;

    AppErrorCode(String code, String message) {
        this.code = Constants.BASE_ERROR_CODE_PREFIX + code;
        this.message = message;
    }
}

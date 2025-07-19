package dev.notyouraverage.commons.utils;

import dev.notyouraverage.base.exceptions.AppException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class CompletableFutureUtils {
    public static <T> T unchekedGet(CompletableFuture<T> completableFuture) {
        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AppException(e);
        } catch (ExecutionException e) {
            throw new AppException(e);
        }
    }
}

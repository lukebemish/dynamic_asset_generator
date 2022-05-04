package com.github.lukebemish.dynamic_asset_generator.client.util;

@FunctionalInterface
public interface SupplierWithException<T,E extends Throwable> {
    T get() throws E;
}
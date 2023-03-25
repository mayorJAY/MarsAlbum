package com.josycom.mayorjay.marsalbum.common.util

fun Any?.isEmptyOrNull(): Boolean {
    if (this is Collection<*>?) return this.isNullOrEmpty()
    if (this is String?) return this.isNullOrEmpty()
    return this == null
}
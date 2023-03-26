package com.josycom.mayorjay.marsalbum.common.domain.util

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)

class NetworkException(message: String): Exception(message)
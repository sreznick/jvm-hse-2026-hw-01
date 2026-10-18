package org.errors

sealed class DataProcessingException(override val message: String) : Exception(message)

class ParsingException(message: String) : DataProcessingException(message)

class ValidationException(message: String) : DataProcessingException(message)

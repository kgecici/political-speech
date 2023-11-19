package org.political.speeches

open class ValidationExceptionBase(message: String? = null, base: String) :
    Exception(message(base, message))

open class UnexpectedFileFormat(message: String? = null, base: String = "Unexpected File Format") :
    ValidationExceptionBase(message, base)

open class FileFormatSecurityException(message: String? = null, base: String = "Unexpected File Format") :
    ValidationExceptionBase(message, base)
open class DataInconsistencyException(message: String? = null, base: String = "Inconsistent Data") :
    ValidationExceptionBase(message, base)
open class DuplicateUrlException(message: String? = null, base: String = "Duplicate Url") :
    ValidationExceptionBase(message, base)
open class NoQueryParamStartsWithUrl(message: String? = null, base: String = "No urlX oparam given") :
    ValidationExceptionBase(message, base)

open class UrlCannotReachable(message: String? = null, base: String = "Url cannot reachable") :
    ValidationExceptionBase(message, base)
open class FieldParseException(message: String? = null, base: String = "Field cannot be parsed") :
    ValidationExceptionBase(message, base)

private fun message(base: String, message: String?) = base + if (message != null) ": $message" else ""

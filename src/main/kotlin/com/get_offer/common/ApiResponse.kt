class ApiResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null,
) {
    companion object {
        fun <T> success(data: T?): ApiResponse<T> {
            return ApiResponse(
                status = "SUCCESS",
                data = data
            )
        }

        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(
                status = "ERROR",
                message = message,
                data = null
            )
        }
    }
}

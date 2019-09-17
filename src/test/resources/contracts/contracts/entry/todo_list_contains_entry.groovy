package contracts.entry

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method GET()
        url '/todos/0'
        headers {
            contentType(applicationJson())
        }
    }
    response {
        headers {
            contentType(applicationJson())
        }
        status OK()
        body(
                message: anyNonBlankString()
        )
    }
}
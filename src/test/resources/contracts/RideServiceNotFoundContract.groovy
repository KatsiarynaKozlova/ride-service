package contracts

import org.springframework.cloud.contract.spec.Contract

import static org.springframework.cloud.contract.spec.internal.HttpHeaders.CONTENT_TYPE;

Contract.make {
    description "should return 404 ride not found"
    request {
        method GET()
        url "/rides/99"
    }
    response {
        status 404
    }
}

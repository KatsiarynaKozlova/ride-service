package contracts

import org.springframework.cloud.contract.spec.Contract

import static org.springframework.cloud.contract.spec.internal.HttpHeaders.CONTENT_TYPE

Contract.make {
    description "should return a ride by ID"
    request {
        method GET()
        url "/rides/1"
    }
    response {
        status 200
        body([
                id       : 1,
                driverId        : 1,
                passengerId: 1,
                routeStart       : "Point A",
                routeEnd: "Point B",
                price: 123.45,
                dateTimeCreate: [2024,9,1,5,51,26],
                status: "ACCEPTED"
        ])
        headers {
            header(CONTENT_TYPE, 'application/json')
        }
    }
}

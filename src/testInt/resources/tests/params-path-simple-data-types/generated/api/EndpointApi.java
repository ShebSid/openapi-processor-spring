/*
 * DO NOT MODIFY - this class was auto generated by openapi-processor-spring
 *
 * test
 * time
 * https://docs.openapiprocessor.io/spring
 */

package generated.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface EndpointApi {

    @GetMapping(path = "/endpoint/{foo}")
    void getEndpointFoo(@PathVariable(name = "foo") String foo);

}

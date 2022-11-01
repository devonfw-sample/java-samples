package com.devonfw.quarkus;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(tags = {
@Tag(name = "product", description = "Product API.") }, info = @Info(title = "Quarkus Keycloak AuthN AuthZ demo", version = "1.0.0", contact = @Contact(name = "API Support", email = "support@devonfw.com")))
/**
 * JaxRS application is not required in quarkus, but it is useful to place central API docs somewhere. We could also use
 * package-info.java for this.
 */
public class DemoApplication extends Application {
}
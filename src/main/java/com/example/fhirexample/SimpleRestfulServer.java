package com.example.fhirexample;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.fhirexample.ResourceProvider.PatientResourceProvider;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/*")
public class SimpleRestfulServer extends RestfulServer {
    @Override
    protected void initialize()throws ServletException {
        setFhirContext(FhirContext.forR5());
        registerProvider(new PatientResourceProvider());
    }
}
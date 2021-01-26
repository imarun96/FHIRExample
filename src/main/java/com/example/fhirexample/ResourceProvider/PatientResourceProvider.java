package com.example.fhirexample.ResourceProvider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PatientResourceProvider implements IResourceProvider {

    /**
     * Constructor
     */
    public PatientResourceProvider() {
    }

    //A Map to hold all the patients.

    private Map<String, Patient> patientMap = new HashMap<String, Patient>();
    /* You want to be a law abiding class so please
     Implement abstract method getResourceProvider() from IResourceProvider
     */

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Patient.class;
    }
    //Create a Map to hold patients data

    /**
     * The "@Search" annotation indicates that this method supports the
     * search operation. Similarly we can have more Search annotations defined
     * they can each take different parameters
     * Documentation just gets added to your Capability Statement
     * <p>
     *
     * @return This method returns a list of Patients. This list may contain multiple
     * matching resources, or it may also be empty.
     * This annotation takes a "name" parameter which specifies the parameter's name (as it will appear in the search URL).
     * FHIR defines standardized parameter names for each resource, and these are available as constants on the individual
     * HAPI resource classes.
     * @paramtheFamilyName This operation takes one parameter which is the search criteria. It is
     * annotated with the "@Required" annotation. This annotation takes one argument,
     * a string containing the name of the search criteria. The datatype here
     * is StringParam, but there are other possible parameter types depending on the
     * specific search criteria.
     * There is a "@Optional" tag which can be used if you want to have a search criteria as optional
     */

    @Search
    public List<Patient> search(@RequiredParam(name = Patient.SP_FAMILY) StringParam theParam) {
        System.out.println("theParam Param value = " + theParam.getValue());
        List<Patient> patients = new ArrayList<>();
        patients = this.searchByFamilyName(theParam.getValue());
        return patients;
    }

    /**
     * Simple implementation of the "read" method
     * The "@Read" annotation indicates that this method supports the
     * read operation. Read operations should return a single resource
     * instance.
     * <p>
     * This method will support a query like this http://localhost:8080/Patient/1
     */

    @Read()
    public Patient read(@IdParam IdType theId) {
        System.out.println("theId value = "+theId.getValue());
        System.out.println("getIdPart value = "+theId.getIdPart());
        loadDummyPatients();
        Patient retVal = patientMap.get(theId.getIdPart());
        if (retVal == null) {
            throw new ResourceNotFoundException(theId);
        }
        return retVal;
    }

    private List<Patient> searchByFamilyName(String familyName) {
        StringUtils.isEmpty(familyName);
        System.out.println("familyName Param value = " + familyName);
        List<Patient> retPatients;
        loadDummyPatients();
        // Encode the output, including the narrative - see below
        // Loop through the patients looking for matches
        retPatients = patientMap.values()
                .stream()
                .filter(next -> familyName.toLowerCase().equals(next.getNameFirstRep().getFamily().toLowerCase()))
                .collect(Collectors.toList());
        return retPatients;
    }

    private void loadDummyPatients() {
        CodeableConcept cc = new CodeableConcept();
        cc.addCoding().setCode("M");
        Patient patient = new Patient();
        patient.addTelecom().setValue("virubalaji@gmail.com");
        patient.setId("1");
        patient.setMaritalStatus(cc);
        patient.addIdentifier().setSystem("http://optum.com/MRNs").setValue("007");
        patient.addName().setFamily("Balaji").addGiven("Arun").addGiven("A");
        patient.addAddress().addLine("Address Line 1");
        patient.addAddress().setCity("Mumbai");
        patient.addAddress().setCountry("India");
        patient.addTelecom().setValue("111-111-1111");
        this.patientMap.put("1", patient);
        for (int i = 2; i <= 5; i++) {
            patient = new Patient();
            patient.setId(Integer.toString(i));
            patient.addIdentifier().setSystem("http://optum.com/MRNs").setValue("007" + i);
            patient.addName().setFamily("Bond" + i).addGiven("James").addGiven("J");
            patient.addAddress().addLine("House Line " + i);
            patient.addAddress().setCity("Your City");
            patient.addAddress().setCountry("USA");
            this.patientMap.put(Integer.toString(i), patient);
        }
    }
}
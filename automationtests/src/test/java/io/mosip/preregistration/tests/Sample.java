package io.mosip.preregistration.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import io.mosip.service.ApplicationLibrary;
import io.mosip.service.BaseTestCase;
import io.mosip.util.CommonLibrary;
import io.mosip.util.PreRegistrationLibrary;
import io.restassured.response.Response;

public class Sample extends BaseTestCase implements ITest {
	Logger logger = Logger.getLogger(BatchJob.class);
	PreRegistrationLibrary lib = new PreRegistrationLibrary();
	String testSuite;
	String preRegID = null;
	String createdBy = null;
	Response response = null;
	String preID = null;
	protected static String testCaseName = "";
	static String folder = "preReg";
	private static CommonLibrary commonLibrary = new CommonLibrary();
	ApplicationLibrary applnLib = new ApplicationLibrary();

	Configuration config = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
			.mappingProvider(new JacksonMappingProvider()).build();

	@BeforeClass
	public void readPropertiesFile() {
		initialize();
	}

	@Test
	public void updateDemographicDetailsOfPendingAppointmentApplication() {
		testSuite = "Create_PreRegistration/createPreRegistration_smoke";
		JSONObject createPregRequest = lib.createRequest(testSuite);
		Response createResponse = lib.CreatePreReg(createPregRequest);
		preID = createResponse.jsonPath().get("response[0].preRegistrationId").toString();
		Response documentResponse = lib.documentUploadParm(createResponse,preID);
		Response avilibityResponse = lib.FetchCentre();
		String expectedRegCenterId = avilibityResponse.jsonPath().get("response.regCenterId").toString();
		String expectedCenterDetails = avilibityResponse.jsonPath().get("response.centerDetails[0].timeSlots[0]").toString();
		//lib.BookAppointment(documentResponse, avilibityResponse, preID);
		
	}

	@BeforeMethod
	public void getAuthToken() {
		authToken = lib.getToken();
	}

	@Override
	public String getTestName() {
		return this.testCaseName;

	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		logger.info("method name:" + result.getMethod().getMethodName());
		lib.logOut();

	}

}

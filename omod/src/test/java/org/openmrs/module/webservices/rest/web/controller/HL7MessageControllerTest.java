/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.test.Util;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests functionality of {@link HL7MessageController}.
 */
public class HL7MessageControllerTest extends BaseModuleWebContextSensitiveTest {
	
	private static final String hl7SourceName = "TEST";
	
	private static final String hl7SourceKey = "test";
	
	private static final String hl7Data = "OBR|1|||1238^MEDICAL RECORD OBSERVATIONS^DCT|";
	
	private HL7Service service;
	
	private HL7MessageController controller;
	
	private MockHttpServletRequest request;
	
	private HttpServletResponse response;
	
	private static final String datasetFilename = "customTestDataset.xml";
	
	@Before
	public void before() throws Exception {
		this.service = Context.getHL7Service();
		this.controller = new HL7MessageController();
		this.request = new MockHttpServletRequest();
		this.response = new MockHttpServletResponse();
		executeDataSet(datasetFilename);
	}
	
	@Test
	public void enqueHl7Message_shouldEnqueueHl7InQueueMessage() throws Exception {
		int before = service.getAllHL7InQueues().size();
		String json = "{ \"source\":\"" + hl7SourceName + "\", \"sourceKey\": \"" + hl7SourceKey + "\", \"data\": \""
		        + hl7Data + "\" }";
		SimpleObject post = new ObjectMapper().readValue(json, SimpleObject.class);
		Object newHl7Message = controller.create(post, request, response);
		Util.log("Enqued hl7 message", newHl7Message);
		Assert.assertEquals(before + 1, service.getAllHL7InQueues().size());
	}
}
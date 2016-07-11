package org.stagemonitor.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.stagemonitor.core.util.JsonUtils;
import org.stagemonitor.junit.ConditionalTravisTestRunner;
import org.stagemonitor.junit.ExcludeOnTravis;

@RunWith(ConditionalTravisTestRunner.class)
public class MeasurementSessionTest {

	@Test
	@ExcludeOnTravis
	public void testGetHostname() {
		assertNotNull(CorePlugin.getNameOfLocalHost());
	}

	@Test
	@Ignore
	public void testGetHostnameFromEnv() {
		assertNotNull(CorePlugin.getHostNameFromEnv());
	}

	@Test
	public void testToJson() throws Exception {
		MeasurementSession measurementSession = new MeasurementSession(
				"a056120d-d28d-4438-98ef-7991f36886c5", "app", "host", "127.0.0.1", "instance");
		final MeasurementSession jsonSession = JsonUtils.getMapper().readValue(JsonUtils.toJson(measurementSession), MeasurementSession.class);
		assertEquals(measurementSession.getApplicationName(), jsonSession.getApplicationName());
		assertEquals(measurementSession.getHostName(), jsonSession.getHostName());
		assertEquals(measurementSession.getHostIPv4(), jsonSession.getHostIPv4());
		assertEquals(measurementSession.getInstanceName(), jsonSession.getInstanceName());
		assertEquals(measurementSession.getGid(), jsonSession.getGid());
		assertEquals(measurementSession.getId(), jsonSession.getId());
		assertEquals(measurementSession.getStart(), jsonSession.getStart());
	}
}

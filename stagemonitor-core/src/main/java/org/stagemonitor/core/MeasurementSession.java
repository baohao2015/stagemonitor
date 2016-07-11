package org.stagemonitor.core;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static org.stagemonitor.core.util.StringUtils.replaceWhitespacesWithDash;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.stagemonitor.core.util.StringUtils;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = ANY, setterVisibility = NONE)
public class MeasurementSession {

	private final String id;
	private final String gid;
	private final String applicationName;
	private final String hostName;
	private final String hostIPv4;
	private final String instanceName;
	private final long startTimestamp;
	private long endTimestamp;
	@JsonIgnore
	private final String stringRepresentation;

	public MeasurementSession(
			String gid,
			String applicationName,
			String hostName,
			String hostIPv4,
			String instanceName) {
		this(UUID.randomUUID().toString(), gid, applicationName, hostName, hostIPv4, instanceName);
	}

	@JsonCreator
	public MeasurementSession(@JsonProperty("id") String id,
							  @JsonProperty("gid") String gid,
							  @JsonProperty("applicationName") String applicationName,
							  @JsonProperty("hostName") String hostName,
							  @JsonProperty("hostIPv4") String hostIPv4,
							  @JsonProperty("instanceName") String instanceName) {

		this.gid = replaceWhitespacesWithDash(gid);
		this.applicationName = replaceWhitespacesWithDash(applicationName);
		this.hostName = replaceWhitespacesWithDash(hostName);
		this.hostIPv4 = replaceWhitespacesWithDash(hostIPv4);
		this.instanceName = replaceWhitespacesWithDash(instanceName);
		this.id = id;
		stringRepresentation = "[gid=" + gid + "] [application=" + applicationName + "] " +
				"[instance=" + instanceName + "] [host=" + hostName + "] [ip=" + hostIPv4 + "]";
		startTimestamp = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public String getGid() {
		return gid;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getHostName() {
		return hostName;
	}

	public String getHostIPv4() {
		return hostIPv4;
	}

	public String getInstanceName() {
		return instanceName;
	}

	@JsonIgnore
	public boolean isInitialized() {
		return gid != null && applicationName != null && instanceName != null && hostName != null && hostIPv4 != null;
	}

	public String getStart() {
		return StringUtils.timestampAsIsoString(startTimestamp);
	}

	@JsonProperty("@timestamp")
	public long getStartTimestamp() {
		return startTimestamp;
	}

	public String getEnd() {
		if (endTimestamp > 0) {
			return StringUtils.timestampAsIsoString(endTimestamp);
		}
		return null;
	}

	public Long getEndTimestamp() {
		if (endTimestamp > 0) {
			return endTimestamp;
		}
		return null;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	@JsonIgnore
	public boolean isNull() {
		return gid == null && applicationName == null && instanceName == null && hostName == null && hostIPv4 == null;
	}

	@JsonIgnore
	public Map<String, String> asMap() {
		final TreeMap<String, String> result = new TreeMap<String, String>();
		result.put("measurement_start", Long.toString(startTimestamp));
		result.put("gid", gid);
		result.put("application", applicationName);
		result.put("host", hostName);
		result.put("host_ipv4", hostIPv4);
		result.put("instance", instanceName);
		return Collections.unmodifiableMap(result);
	}

	@Override
	public String toString() {
		return stringRepresentation;
	}

}

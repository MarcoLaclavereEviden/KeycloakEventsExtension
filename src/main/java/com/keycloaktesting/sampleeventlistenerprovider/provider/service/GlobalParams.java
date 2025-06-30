package com.keycloaktesting.sampleeventlistenerprovider.provider.service;

import java.net.URI;

public class GlobalParams {
	public static final URI OpenFgaControlerURL() {
		return URI.create("http://host.docker.internal:1101");
	}
}

package codetrack.client;

import codetrack.Utils;

public class LocalProjectConfig {
	private Integer id;
	private String name;
	private boolean allowed = false;
	private boolean fullFirstSyncDone = false;

	public LocalProjectConfig(Integer id, String name, boolean allowed) {
		this.setId(id);
		this.setName(name);
		this.setAllowed(allowed);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static boolean exists(String path) {
		return Utils.fileExists(path);
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public boolean isFullFirstSyncDone() {
		return fullFirstSyncDone;
	}

	public void setFullFirstSyncDone(boolean fullFirstSyncDone) {
		this.fullFirstSyncDone = fullFirstSyncDone;
	}
}

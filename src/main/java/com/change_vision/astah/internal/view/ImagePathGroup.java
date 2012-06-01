package com.change_vision.astah.internal.view;

import java.util.ArrayList;
import java.util.List;

public class ImagePathGroup {

	private String name;
	private List<String> paths;

	public ImagePathGroup(String name) {
		this.name = name;
		this.paths = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public List<String> getPaths() {
		return paths;
	}

	@Override
	public String toString() {
		return "ImagePathGroup [name=" + name + ", paths=" + paths + "]";
	}

}

package com.change_vision.astah.internal.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImagePathLoader {
	
	private static final Pattern groupPattern = Pattern.compile("^\\[(.*)\\]");
	
	public List<ImagePathGroup> load(BufferedReader reader) throws IOException{
		if(reader == null) throw new IllegalArgumentException();

		ArrayList<ImagePathGroup> result = new ArrayList<ImagePathGroup>();
		String line = null;
		ImagePathGroup currentGroup = null;
		while ((line = reader.readLine()) != null){
			Matcher matcher = groupPattern.matcher(line);
			if(matcher.matches()){
				currentGroup = new ImagePathGroup(matcher.group(1));
				result.add(currentGroup);
			}else{
				if(currentGroup != null) currentGroup.getPaths().add(line);
			}
		}
		return result;
	}

}
